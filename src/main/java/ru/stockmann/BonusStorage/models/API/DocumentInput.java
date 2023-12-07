package ru.stockmann.BonusStorage.models.API;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DocumentInput {
    private UUID documentGuid;
    private String documentType;
    private String documentNumber;
    private LocalDateTime documentDate;
    private String documentStatus;
    private UUID documentStoreId;
    private String documentStoreName;
    private List<BonusesInput> events;
}
