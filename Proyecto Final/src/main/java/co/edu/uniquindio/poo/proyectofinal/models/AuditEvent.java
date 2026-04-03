package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.RiskLevel;

import java.time.LocalDateTime;

public class AuditEvent {

    private String id;
    private String userId;
    private String transactionId;
    private String description;
    private RiskLevel riskLevel;
    private LocalDateTime detectedAt;
    private boolean reviewed;

    public AuditEvent(String id, String userId, String transactionId,
                      String description, RiskLevel riskLevel) {
        this.id = id;
        this.userId = userId;
        this.transactionId = transactionId;
        this.description = description;
        this.riskLevel = riskLevel;
        this.detectedAt = LocalDateTime.now();
        this.reviewed = false;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTransactionId() { return transactionId; }
    public String getDescription() { return description; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public boolean isReviewed() { return reviewed; }

    public void setReviewed(boolean reviewed) { this.reviewed = reviewed; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    @Override
    public String toString() {
        return String.format("[AUDIT][%s] Usuario: %s | %s | Riesgo: %s | %s",
                id, userId, description, riskLevel.getDisplayName(),
                detectedAt.toString());
    }
}