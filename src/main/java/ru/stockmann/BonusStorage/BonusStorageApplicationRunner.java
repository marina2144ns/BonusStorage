package ru.stockmann.BonusStorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import ru.stockmann.BonusStorage.models.API.BonusesInput;
import ru.stockmann.BonusStorage.models.API.DocumentInput;
import ru.stockmann.BonusStorage.models.API.ResultInput;
import ru.stockmann.BonusStorage.models.BonusesInDocument;
import ru.stockmann.BonusStorage.models.Document;
import ru.stockmann.BonusStorage.services.BonusesInDocumentService;
import ru.stockmann.BonusStorage.services.DocumentService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
public class BonusStorageApplicationRunner implements ApplicationRunner {
    private final DocumentService documentService;
    private final BonusesInDocumentService bonusesInDocumentService;


    @Value("${uploadingbonuses.documentsCount}")
    private int documentsCount;
    @Value("${uploadingbonuses.urlbonusnik}")
    private String bonusesInDocumentsURL;
    @Value("${uploadingbonuses.user}")
    private String username;
    @Value("${uploadingbonuses.password}")
    private String password;


    private static final Logger logger = LoggerFactory.getLogger(BonusStorageApplication.class);

    @Autowired
    public BonusStorageApplicationRunner(DocumentService documentService, BonusesInDocumentService bonusesInDocumentService) {
        this.documentService = documentService;
        this.bonusesInDocumentService = bonusesInDocumentService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        updateBonusData();
    }
    @Transactional
    public void updateBonusData(){

        //Готовим параметры вызова сервиса
        List<Document> documentList = documentService.findChanged(documentsCount);
        List <DocumentPost> documentIds = new ArrayList<>();

        for(Document document:documentList){
            DocumentPost documentPost = new DocumentPost(document.getOneCId(), document.getDocumentType());
            documentIds.add(documentPost);
            document.setChanged(Boolean.FALSE);
            document.setBonusesUploaded(LocalDateTime.now());
            documentService.save(document);
        }

        UUID session = UUID.randomUUID();
        // Создаем JSON-строку с данными
        String requestBody = """
                    {
                        "session": "%s",
                        "documents": %s
                    }
                    """.formatted(session, documentsToJsonArray(documentIds));
        String serviceResult="";

        try {
            // Создаем объект клиента HTTP
            HttpClient client = HttpClient.newHttpClient();

            String credentials = username + ":" + password;
            String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            //logger.info(requestBody);
            logger.info("start request");

            // Создаем объект запроса с методом POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(bonusesInDocumentsURL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + base64Credentials) //todo
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.error("Request to Bonusnik failed. Response code: {}, Response body: {}, Session: {}",
                        response.statusCode(), response.body(),session.toString());
                return;
            }
            serviceResult = response.body();
            logger.info("got response");
        }
        catch (HttpStatusCodeException e) {
            // Обрабатываем ошибку HTTP
            logger.error("HTTP error occurred. Response code: {}, Response body: {}, Session: {}",e.getStatusCode(), e.getResponseBodyAsString(),session.toString());
            return;
        }
        catch (Exception e) {
            logger.error("Bonusnik post service failed",e);
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ResultInput result = objectMapper.readValue(serviceResult, ResultInput.class);

            for (DocumentInput documentInput : result.getDocuments()) {

                logger.info("try to get document");
                Document document = documentService.findByOneCId(documentInput.getDocumentGuid());
                logger.info("got document");

                if (document != null) {
                    // Удаляем все записи в таблице BonusesInDocuments, относящиеся к текущему Document
                    bonusesInDocumentService.deleteByDocument(document);
                    logger.info("      delete bonuses in document");
                    // Создаем новые записи из объекта BonusesInput
                    for (BonusesInput bonusesInput : documentInput.getEvents()) {
                        BonusesInDocument bonusesInDocument = new BonusesInDocument();
                        bonusesInDocument.setSourceBase(document.getSourceBase());
                        bonusesInDocument.setDocumentType(documentInput.getDocumentType());
                        bonusesInDocument.setStoreId(documentInput.getDocumentStoreId());
                        bonusesInDocument.setStoreName(documentInput.getDocumentStoreName());
                        bonusesInDocument.setCardNumber(bonusesInput.getCardNumber());
                        bonusesInDocument.setTypeOfIncrement(bonusesInput.getTypeOfIncrement());
                        bonusesInDocument.setValue(bonusesInput.getValue());
                        bonusesInDocument.setTypeOfOperation(bonusesInput.getTypeOfOperation());
                        bonusesInDocument.setTextOperation(bonusesInput.getTextOperation());
                        bonusesInDocument.setOrderId(documentInput.getDocumentNumber());
                        bonusesInDocument.setStartDate(bonusesInput.getBonusDate().getStartDate());
                        bonusesInDocument.setEndDate(bonusesInput.getBonusDate().getEndDate());
                        bonusesInDocument.setDocument(document);
                        bonusesInDocument.setCreated(LocalDateTime.now());
                        // Сохраняем в базе данных
                        bonusesInDocumentService.save(bonusesInDocument);
                        logger.info("      save bonuses");
                    }
                }
                else{
                    logger.error("Could not find document: {}",documentInput.getDocumentGuid());
                }
            }
        } catch (Exception e) {
            logger.error("Error while reading json from Bonusnik, Session: {}",session.toString(),e);
            return;
        }

        return;
    }

    private static class DocumentPost {
        private final String documentGuid;
        private final Integer documentType;

        public DocumentPost(String documentGuid, Integer documentType) {
            this.documentGuid = documentGuid;
            this.documentType = documentType;
        }

        public String getDocumentGuid() {
            return documentGuid;
        }

        public Integer getDocumentType() {
            return documentType;
        }
    }

    // Метод для преобразования списка документов в JSON-массив
    private static String documentsToJsonArray(List<DocumentPost> documents) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (DocumentPost document : documents) {
            jsonArray.append("""
                    {
                        "document_guid": "%s",
                        "document_type": "%s"
                    }
                    """.formatted(document.getDocumentGuid(), document.getDocumentType()));
            if (documents.indexOf(document) < documents.size() - 1) {
                jsonArray.append(",");
            }
        }
        jsonArray.append("]");
        return jsonArray.toString();
    }

}
