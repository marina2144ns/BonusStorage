package ru.stockmann.BonusStorage.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stockmann.BonusStorage.models.BonusesInDocument;
import ru.stockmann.BonusStorage.models.Document;
import ru.stockmann.BonusStorage.repositories.BonusesInDocumentRepository;
import ru.stockmann.BonusStorage.repositories.DocumentRepository;

@Service
@Transactional(readOnly = true)
public class BonusesInDocumentService {
    private final BonusesInDocumentRepository bonusesInDocumentRepository;
    @Autowired
    public BonusesInDocumentService(BonusesInDocumentRepository bonusesInDocumentRepository) {
        this.bonusesInDocumentRepository = bonusesInDocumentRepository;
    }
    public void deleteByDocument(Document document){
        bonusesInDocumentRepository.deleteByDocument(document);
    };
    public BonusesInDocument save(BonusesInDocument bonusesInDocument){
        return bonusesInDocumentRepository.save(bonusesInDocument);
    }
}
