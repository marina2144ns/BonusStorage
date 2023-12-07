package ru.stockmann.BonusStorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public List<Document> findChanged(){
        return documentRepository.findByIsChanged(Boolean.TRUE);
    }

    public Integer countChanged(){
        return documentRepository.countByIsChanged(Boolean.TRUE);
    }

    public List<Document> findTop1Changed(){
        return documentRepository.findTop1ByIsChanged(Boolean.TRUE);
    }

    public List<Document> findAll(){return documentRepository.findAll();}
}
