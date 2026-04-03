package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum WalletType {
    SAVINGS("Ahorro"),
    DAILY_EXPENSES("Gastos diarios"),
    SHOPPING("Compras"),
    TRANSPORT("Transporte"),
    INVESTMENT("Inversión");

    private final String displayName;

    WalletType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}