package ru.stockmann.BonusStorage.models.API;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class BonusesInput {
    private String cardNumber;
    private String typeOfIncrement;
    private Double value;
    private Integer typeOfOperation;
    private String textOperation;
    @JsonProperty("orderID")
    private String orderId;
    BonusDateInput bonusDate;


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

    public Integer getTypeOfOperation() {
        return typeOfOperation;
    }

    public void setTypeOfOperation(Integer typeOfOperation) {
        this.typeOfOperation = typeOfOperation;
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

    public BonusDateInput getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(BonusDateInput bonusDate) {
        this.bonusDate = bonusDate;
    }
}