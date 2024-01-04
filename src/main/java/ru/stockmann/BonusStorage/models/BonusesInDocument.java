package ru.stockmann.BonusStorage.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "BonusesInDocuments")
public class BonusesInDocument implements java.io.Serializable{
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "SourceBase")
    private Integer sourceBase;
    @Column(name = "DocumentType")
    private Integer documentType;
    @ManyToOne
    @JoinColumn(name = "Document", referencedColumnName = "Id")
    private Document document;
    @Column(name = "StoreId")
    private UUID storeId;
    @Column(name = "StoreName")
    private String storeName;
    @Column(name = "CardNumber")
    private String cardNumber;
    @Column(name = "TypeOfIncrement")
    private String typeOfIncrement;
    @Column(name = "Value")
    private Double value;
    @Column(name = "TextOperation")
    private String textOperation;
    @Column(name = "OrderID")
    private String orderId;
    @Column(name = "BonusStartDate")
    private LocalDateTime startDate;
    @Column(name = "BonusEndDate")
    private LocalDateTime endDate;


    public BonusesInDocument() {
    }

    public BonusesInDocument(Integer id, Integer sourceBase, Integer documentType, Document document, UUID storeId, String storeName, String cardNumber, String typeOfIncrement, Double value, String textOperation, String orderId, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.sourceBase = sourceBase;
        this.documentType = documentType;
        this.document = document;
        this.storeId = storeId;
        this.storeName = storeName;
        this.cardNumber = cardNumber;
        this.typeOfIncrement = typeOfIncrement;
        this.value = value;
        this.textOperation = textOperation;
        this.orderId = orderId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceBase() {
        return sourceBase;
    }

    public void setSourceBase(Integer sourceBase) {
        this.sourceBase = sourceBase;
    }

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTypeOfIncrement() {
        return typeOfIncrement;
    }

    public void setTypeOfIncrement(String typeOfIncrement) {
        this.typeOfIncrement = typeOfIncrement;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getTextOperation() {
        return textOperation;
    }

    public void setTextOperation(String textOperation) {
        this.textOperation = textOperation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
