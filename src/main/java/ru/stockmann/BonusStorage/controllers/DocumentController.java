package ru.stockmann.BonusStorage.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ru.stockmann.BonusStorage.utils.Converting.convertUUIDToBinary;


@RestController
@RequestMapping("/bonusstorage/v1.0/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final BonusesInDocumentService bonusesInDocumentService;

    @Autowired
    public DocumentController(DocumentService documentService, BonusesInDocumentService bonusesInDocumentService) {
        this.documentService = documentService;
        this.bonusesInDocumentService = bonusesInDocumentService;
    }

    @Value("${uploadingbonuses.documentsCount}")
    private int documentsCount;

    @Value("${uploadingbonuses.urlbonusnik}")
    private String bonusesInDocumentsURL;

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @GetMapping("/changed") //todo добавить пейджинацию
    public List<Document> getDocuments(){
        return documentService.findChanged(documentsCount);
    }

    @GetMapping("/changedcount")
    public int getNumberOfChangedDocuments(){
        return documentService.countChanged();
    }

    @GetMapping("/uploadbonuses")
    public void uploadBonuses(){

    }

    @GetMapping("/development")
    @Transactional
    public String getDevelopment(){

        //Готовим параметры вызова сервиса
        List<Document> documentList = documentService.findChanged(documentsCount);
        List <DocumentPost> documentIds = new ArrayList<>();

        for(Document document:documentList){
            DocumentPost documentPost = new DocumentPost(document.getExtIdRRef(),document.getDocumentType());
            documentIds.add(documentPost);
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

            // Создаем объект запроса с методом POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(bonusesInDocumentsURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Отправляем запрос и получаем ответ
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.error("Request to Bonusnik failed. Response code: {}, Response body: {}, Session: {}",
                        response.statusCode(), response.body(),session.toString());
                return ""; //todo исправить
            }
            serviceResult = response.body();
        }
        catch (HttpStatusCodeException e) {
            // Обрабатываем ошибку HTTP
            logger.error("HTTP error occurred. Response code: {}, Response body: {}, Session: {}",e.getStatusCode(), e.getResponseBodyAsString(),session.toString());
            return ""; //todo исправить
        }
        catch (Exception e) {
            logger.error("Bonusnik post service failed",e);
            return ""; //todo исправить
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ResultInput result = objectMapper.readValue(serviceResult, ResultInput.class);

            System.out.println("Session: " + result.getSession());

            for (DocumentInput documentInput : result.getDocuments()) {

                System.out.println(documentInput.getDocumentGuid());
                Document document = documentService.findByExtIdRRef(documentInput.getDocumentGuid());
                    if (document != null){
                        // Удаляем все записи в таблице BonusesInDocuments, относящиеся к текущему Document
                        bonusesInDocumentService.deleteByDocument(document);
                        // Создаем новые записи из объекта BonusesInput
                        for (BonusesInput bonusesInput : documentInput.getEvents()) {
                            BonusesInDocument bonusesInDocument = new BonusesInDocument();
                            bonusesInDocument.setSourceBase(document.getSourceBase());//todo проверить откуда лучше
                            bonusesInDocument.setDocumentType(documentInput.getDocumentType());
                            bonusesInDocument.setStoreId(documentInput.getDocumentStoreId());
                            bonusesInDocument.setStoreName(documentInput.getDocumentStoreName());
                            bonusesInDocument.setCardNumber(bonusesInput.getCardNumber());
                            bonusesInDocument.setTypeOfIncrement(bonusesInput.getTypeOfIncrement());
                            bonusesInDocument.setValue(bonusesInput.getValue());
                            bonusesInDocument.setTextOperation(bonusesInput.getTextOperation());
                            bonusesInDocument.setOrderId(documentInput.getDocumentNumber());
                            bonusesInDocument.setStartDate(bonusesInput.getBonusDate().getStartDate());
                            bonusesInDocument.setEndDate(bonusesInput.getBonusDate().getEndDate());
                            bonusesInDocument.setDocument(document);
                            // Сохраняем в базе данных
                            bonusesInDocumentService.save(bonusesInDocument);
                        }
                    }
                }
        } catch (Exception e) {
            logger.error("Error while reading json from Bonusnik, Session: {}",session.toString(),e);
            return "";
        }


        return "";
    }

    private class DocumentPost {
        private final UUID documentGuid;
        private final Integer documentType;

        public DocumentPost(UUID documentGuid, Integer documentType) {
            this.documentGuid = documentGuid;
            this.documentType = documentType;
        }

        public UUID getDocumentGuid() {
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