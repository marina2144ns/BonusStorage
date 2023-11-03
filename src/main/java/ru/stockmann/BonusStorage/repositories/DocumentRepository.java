package ru.stockmann.BonusStorage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stockmann.BonusStorage.models.Document;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
}
