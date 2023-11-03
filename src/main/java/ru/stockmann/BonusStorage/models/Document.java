package ru.stockmann.BonusStorage.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private byte[] extIdRRef;

    @Column(name = "Ext_Date_Time")
    private LocalDateTime extDateTime;

    @Column(name = "Ext_Number")
    private String extNumber;

    public Document() {
    }

    public Document(Integer id, Integer sourceBase, Integer documentType, byte[] extIdRRef, LocalDateTime extDateTime, String extNumber) {
        this.id = id;
        this.sourceBase = sourceBase;
        this.documentType = documentType;
        this.extIdRRef = extIdRRef;
        this.extDateTime = extDateTime;
        this.extNumber = extNumber;
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

    public byte[] getExtIdRRef() {
        return extIdRRef;
    }

    public void setExtIdRRef(byte[] extIdRRef) {
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
