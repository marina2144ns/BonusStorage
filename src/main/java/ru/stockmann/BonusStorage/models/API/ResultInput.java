package ru.stockmann.BonusStorage.models.API;

import java.util.List;
import java.util.UUID;

public class ResultInput {
    private UUID session;
    private List<DocumentInput> documents;

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    public List<DocumentInput> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentInput> documents) {
        this.documents = documents;
    }
}
