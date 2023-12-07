package ru.stockmann.BonusStorage.models.API;

import java.time.LocalDateTime;

public class BonusesInput {
    private String cardNumber;
    private String typeOfIncrement;
    private Double value;
    private Integer typeOfOperation;
    private String textOperation;
    private String orderId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}