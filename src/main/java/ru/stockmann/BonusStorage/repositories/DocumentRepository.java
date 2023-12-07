package ru.stockmann.BonusStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stockmann.BonusStorage.models.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
    List<Document> findByIsChanged(Boolean isChanged);
    Integer countByIsChanged(Boolean isChanged);
    List<Document> findTop1ByIsChanged(Boolean isChanged);
}
