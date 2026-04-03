package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum TransactionStatus {
    COMPLETED("Completada"),
    PENDING("Pendiente"),
    REVERSED("Revertida"),
    REJECTED("Rechazada"),
    FLAGGED("Marcada - Revisión");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}