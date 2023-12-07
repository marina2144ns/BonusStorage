package ru.stockmann.BonusStorage.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Documents")
@IdClass(Document.class)
public class Document implements java.io.Serializable{
    @Id
    @Column(name="Id")
    private Integer id;

    @Column(name = "SourceBase")
    private Integer sourceBase;

    @Column(name = "DocumentType")
    private Integer documentType;

    @Column(name = "Ext_IDRRef")
    private UUID extIdRRef;

    @Column(name = "Ext_Date_Time")
    private LocalDateTime extDateTime;

    @Column(name = "Ext_Number")
    private String extNumber;

    @Column(name = "IsChanged")
    private Boolean isChanged;

    public Document() {
    }

    public Document(Integer id, Integer sourceBase, Integer documentType, UUID extIdRRef, LocalDateTime extDateTime, String extNumber, Boolean isChanged) {
        this.id = id;
        this.sourceBase = sourceBase;
        this.documentType = documentType;
        this.extIdRRef = extIdRRef;
        this.extDateTime = extDateTime;
        this.extNumber = extNumber;
        this.isChanged = isChanged;
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

    public UUID getExtIdRRef() {
        return extIdRRef;
    }

    public void setExtIdRRef(UUID extIdRRef) {
        this.extIdRRef = extIdRRef;
    }

    public LocalDateTime getExtDateTime() {
        return extDateTime;
    }

    public void setExtDateTime(LocalDateTime extDateTime) {
        this.extDateTime = extDateTime;
    }

    public String getExtNumber() {
        return extNumber;
    }

    public void setExtNumber(String extNumber) {
        this.extNumber = extNumber;
    }

    public Boolean getChanged() {
        return isChanged;
    }

    public void setChanged(Boolean changed) {
        isChanged = changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document that = (Document) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
