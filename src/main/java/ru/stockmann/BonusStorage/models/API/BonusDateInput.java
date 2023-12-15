package ru.stockmann.BonusStorage.models.API;

import java.time.LocalDateTime;

public class BonusDateInput {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
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
