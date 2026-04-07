package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.RiskLevel;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionStatus;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;

import java.time.LocalDateTime;

public class Transaction {

    private String id;
    private LocalDateTime date;
    private TransactionType type;
    private double amount;
    private String sourceWalletId;
    private String destinationWalletId;
    private TransactionStatus status;
    private int pointsGenerated;
    private RiskLevel riskLevel;
    private String description;
    private boolean reversible;

    public Transaction(String id, LocalDateTime date, TransactionType type,
                       double amount, String sourceWalletId, String destinationWalletId,
                       String description) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        this.description = description;
        this.status = TransactionStatus.COMPLETED;
        this.riskLevel = RiskLevel.NONE;
        this.pointsGenerated = 0;
        this.reversible = true;
    }

    public String getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getSourceWalletId() { return sourceWalletId; }
    public String getDestinationWalletId() { return destinationWalletId; }
    public TransactionStatus getStatus() { return status; }
    public int getPointsGenerated() { return pointsGenerated; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public String getDescription() { return description; }
    public boolean isReversible() { return reversible; }

    public void setStatus(TransactionStatus status) { this.status = status; }
    public void setPointsGenerated(int pointsGenerated) { this.pointsGenerated = pointsGenerated; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public void setReversible(boolean reversible) { this.reversible = reversible; }
    public void setDescription(String description) { this.description = description; }

    public int calculateBasePoints() {
        return (int) (amount / 10000) * type.getPointsPerHundred();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | $%.2f | %s | Riesgo: %s",
                id, date.toString(), type.getDisplayName(),
                amount, status.getDisplayName(), riskLevel.getDisplayName());
    }
}