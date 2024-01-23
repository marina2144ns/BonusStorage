package ru.stockmann.BonusStorage.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stockmann.BonusStorage.models.Document;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document,Integer> {
    @Query("SELECT d FROM Document d WHERE d.isChanged = true ORDER BY d.id ASC")
    List<Document> findFirstNByIsChangedTrue(Pageable pageable);
    Document findByOneCId(String oneCId);
}
