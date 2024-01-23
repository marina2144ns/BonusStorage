package ru.stockmann.BonusStorage.models.API;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DocumentInput {
    private String documentGuid;
    private Integer documentType;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String documentStatus;
    @JsonProperty("documentStoreID")
    private UUID documentStoreId;
    private String documentStoreName;
    private List<BonusesInput> events;

    public String getDocumentGuid() {
        return documentGuid;
    }

    public void setDocumentGuid(String documentGuid) {
        this.documentGuid = documentGuid;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDateTime getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDateTime documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public UUID getDocumentStoreId() {
        return documentStoreId;
    }

    public void setDocumentStoreId(UUID documentStoreId) {
        this.documentStoreId = documentStoreId;
    }

    public String getDocumentStoreName() {
        return documentStoreName;
    }

    public void setDocumentStoreName(String documentStoreName) {
        this.documentStoreName = documentStoreName;
    }

    public List<BonusesInput> getEvents() {
        return events;
    }

    public void setEvents(List<BonusesInput> events) {
        this.events = events;
    }
}
