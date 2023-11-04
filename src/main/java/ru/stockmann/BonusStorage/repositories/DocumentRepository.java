package ru.stockmann.BonusStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stockmann.BonusStorage.models.Document;

import java.awt.print.Book;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
    List<Document> findByIsChanged(Boolean isChanged);
}
