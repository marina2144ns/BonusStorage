package ru.stockmann.BonusStorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stockmann.BonusStorage.models.Document;
import ru.stockmann.BonusStorage.services.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/bonusstorage/v1.0/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    @GetMapping("/")
    public List<Document> getDocuments(){
        return documentService.findAll();
    }
}
