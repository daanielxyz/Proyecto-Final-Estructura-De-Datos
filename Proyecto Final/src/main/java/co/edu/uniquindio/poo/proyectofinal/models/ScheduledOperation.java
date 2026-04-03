package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;

import java.time.LocalDateTime;

public class ScheduledOperation implements Comparable<ScheduledOperation> {

    private String id;
    private String userId;
    private String sourceWalletId;
    private String destinationWalletId;
    private TransactionType type;
    private double amount;
    private LocalDateTime scheduledDate;
    private boolean executed;
    private boolean cancelled;
    private String description;
    private boolean recurring;
    private int recurrenceDaysInterval;

    public ScheduledOperation(String id, String userId, String sourceWalletId,
                              String destinationWalletId, TransactionType type,
                              double amount, LocalDateTime scheduledDate, String description) {
        this.id = id;
        this.userId = userId;
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.type = type;
        this.amount = amount;
        this.scheduledDate = scheduledDate;
        this.description = description;
        this.executed = false;
        this.cancelled = false;
        this.recurring = false;
        this.recurrenceDaysInterval = 0;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getSourceWalletId() { return sourceWalletId; }
    public String getDestinationWalletId() { return destinationWalletId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public boolean isExecuted() { return executed; }
    public boolean isCancelled() { return cancelled; }
    public String getDescription() { return description; }
    public boolean isRecurring() { return recurring; }
    public int getRecurrenceDaysInterval() { return recurrenceDaysInterval; }

    public void setExecuted(boolean executed) { this.executed = executed; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }
    public void setRecurrenceDaysInterval(int days) { this.recurrenceDaysInterval = days; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    @Override
    public int compareTo(ScheduledOperation other) {
        return this.scheduledDate.compareTo(other.scheduledDate);
    }

    public boolean isDue() {
        return !executed && !cancelled &&
                (LocalDateTime.now().isAfter(scheduledDate) ||
                        LocalDateTime.now().isEqual(scheduledDate));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | $%.2f | Programada: %s | %s",
                id, type.getDisplayName(), description, amount,
                scheduledDate.toString(),
                executed ? "Ejecutada" : (cancelled ? "Cancelada" : "Pendiente"));
    }
}