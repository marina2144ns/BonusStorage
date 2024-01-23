package ru.stockmann.BonusStorage.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import ru.stockmann.BonusStorage.utils.IdConverter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Documents")
public class Document implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Integer id;

    @Column(name = "SourceBase")
    private Integer sourceBase;

    @Column(name = "DocumentType")
    private Integer documentType;

    @Column(name = "Ext_IDRRef")
    private UUID extIdRRef;

    @Column(name = "OneCId")
    private String oneCId;

    @Column(name = "Ext_Date_Time")
    private LocalDateTime extDateTime;

    @Column(name = "Ext_Number")
    private String extNumber;

    @Column(name = "IsChanged")
    private Boolean isChanged;

    @Column(name = "BonusesUploaded")
    private LocalDateTime bonusesUploaded;

    public Document() {
    }

    public Document(Integer id, Integer sourceBase, Integer documentType, UUID extIdRRef, String oneCId, LocalDateTime extDateTime, String extNumber, Boolean isChanged, LocalDateTime bonusesUploaded) {
        this.id = id;
        this.sourceBase = sourceBase;
        this.documentType = documentType;
        this.extIdRRef = extIdRRef;
        this.oneCId = oneCId;
        this.extDateTime = extDateTime;
        this.extNumber = extNumber;
        this.isChanged = isChanged;
        this.bonusesUploaded = bonusesUploaded;
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

    public String getOneCId() {
        return oneCId;
    }

    public void setOneCId(String oneCId) {
        this.oneCId = oneCId;
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

    public LocalDateTime getBonusesUploaded() {
        return bonusesUploaded;
    }

    public void setBonusesUploaded(LocalDateTime bonusesUploaded) {
        this.bonusesUploaded = bonusesUploaded;
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
