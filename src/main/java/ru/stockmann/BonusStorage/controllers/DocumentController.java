package ru.stockmann.BonusStorage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
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
import java.util.UUID;

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

    @Value("${uploadingbonuses.urlbonusnik}")
    private String bonusesInDocumentsURL;

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
    public String getDevelopment(){

        List<Document> documentList = documentService.findTop1Changed(); //todo изменить на значение documentsCount
        List <DocumentWithType> documentIds = new ArrayList<>();

        for(Document document:documentList){
            DocumentWithType documentWithType = new DocumentWithType(document.getExtIdRRef(),document.getDocumentType());
            documentIds.add(documentWithType);
        }

        UUID uuid = UUID.randomUUID(); //todo добавить в вызов
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<String> resultFromBonusnik = restTemplate.postForEntity(bonusesInDocumentsURL, documentIds, String.class);
        if (resultFromBonusnik.getStatusCode()!=HttpStatus.OK){
            System.out.println("Bad response code from Bonusnik"); //todo logging
            return "Bad response code from Bonusnik";
        }

        System.out.println(resultFromBonusnik.getBody());
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode actualObj = mapper.readTree(resultFromBonusnik.getBody());
        } catch (JsonProcessingException e) {
            System.out.println("Bad response from Bonusnik"); //todo logging
            e.printStackTrace();
        }
        return "";
    }
}