package ru.stockmann.BonusStorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stockmann.BonusStorage.models.Document;
import ru.stockmann.BonusStorage.repositories.DocumentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll(){
        return documentRepository.findAll();
    }
}