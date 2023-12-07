package ru.stockmann.BonusStorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.stockmann.BonusStorage.models.Document;
import ru.stockmann.BonusStorage.models.API.DocumentWithType;
import ru.stockmann.BonusStorage.services.DocumentService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bonusstorage/v1.0/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Value("${uploadingbonuses.documentsCount}")
    private Integer documentsCount;

    @GetMapping("/changed") //todo добавить пейджинацию
    public List<Document> getDocuments(){
        return documentService.findChanged();
    }

    @GetMapping("/changedcount")
    public int getNumberOfChangedDocuments(){
        return documentService.countChanged();
    }

    @GetMapping("/uploadbonuses")
    public void uploadBonuses(){

    }

    @GetMapping("/development")
    public List<DocumentWithType> getDevelopment(){

        List<Document> documentList = documentService.findTop1Changed(); //todo изменить на значение documentsCount
        List <DocumentWithType> documentIds = new ArrayList<>();

        for(Document document:documentList){
            DocumentWithType documentWithType = new DocumentWithType(document.getExtIdRRef(),document.getDocumentType());
            documentIds.add(documentWithType);
        }

        String bonusesInDocumentsURL = "https://testos2.free.beeceptor.com/todos";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> result = restTemplate.postForEntity(bonusesInDocumentsURL, "", String.class);


        System.out.println(result);

        return documentIds;

    }
}