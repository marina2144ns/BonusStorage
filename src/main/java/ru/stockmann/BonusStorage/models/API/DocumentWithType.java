package ru.stockmann.BonusStorage.models.API;

import java.util.UUID;

public class DocumentWithType {
    private UUID document_guid;
    private Integer document_type;

    public DocumentWithType(UUID document_guid, Integer document_type) {
        this.document_guid = document_guid;
        this.document_type = document_type;
    }

    public UUID getDocument_guid() {
        return document_guid;
    }

    public void setDocument_guid(UUID document_guid) {
        this.document_guid = document_guid;
    }

    public Integer getDocument_type() {
        return document_type;
    }

    public void setDocument_type(Integer document_type) {
        this.document_type = document_type;
    }

    @Override
    public String toString() {
        return "DocumentWithType{" +
                "document_guid=" + document_guid +
                ", document_type=" + document_type +
                '}';
    }
}
