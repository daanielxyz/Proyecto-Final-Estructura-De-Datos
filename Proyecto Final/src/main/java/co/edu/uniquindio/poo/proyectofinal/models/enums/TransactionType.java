package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum TransactionType {
    RECHARGE("Recarga", 1),
    WITHDRAWAL("Retiro", 2),
    TRANSFER("Transferencia", 3),
    SCHEDULED_PAYMENT("Pago programado", 3);

    private final String displayName;
    private final int pointsPerHundred;

    TransactionType(String displayName, int pointsPerHundred) {
        this.displayName = displayName;
        this.pointsPerHundred = pointsPerHundred;
    }

    public String getDisplayName() { return displayName; }
    public int getPointsPerHundred() { return pointsPerHundred; }

    @Override
    public String toString() { return displayName; }
}