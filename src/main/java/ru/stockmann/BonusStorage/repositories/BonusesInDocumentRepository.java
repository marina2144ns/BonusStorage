package ru.stockmann.BonusStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stockmann.BonusStorage.models.BonusesInDocument;
import ru.stockmann.BonusStorage.models.Document;

import java.util.List;


public interface BonusesInDocumentRepository extends JpaRepository<BonusesInDocument, Integer> {
    List<BonusesInDocument> findByDocument(Document document);
    public void deleteByDocument(Document document);
}
