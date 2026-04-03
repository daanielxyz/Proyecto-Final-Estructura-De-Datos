package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum RiskLevel {
    NONE("Sin riesgo"),
    LOW("Riesgo bajo"),
    MEDIUM("Riesgo medio"),
    HIGH("Riesgo alto"),
    CRITICAL("Riesgo crítico");

    private final String displayName;

    RiskLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}