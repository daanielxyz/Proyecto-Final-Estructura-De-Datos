package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum NotificationType {
    LOW_BALANCE("Saldo bajo"),
    SCHEDULED_UPCOMING("Operación programada próxima"),
    OPERATION_REJECTED("Operación rechazada"),
    LEVEL_UP("Ascenso de nivel"),
    BENEFIT_REDEEMED("Beneficio canjeado"),
    SECURITY_ALERT("Alerta de seguridad"),
    OPERATION_REVERSED("Operación revertida"),
    SCHEDULED_EXECUTED("Operación programada ejecutada");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}