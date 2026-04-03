package co.edu.uniquindio.poo.proyectofinal.models.enums;

public enum UserLevel {
    BRONZE("Bronce", 0, 500, 0.0, 1.0),
    SILVER("Plata", 501, 1000, 0.05, 1.1),
    GOLD("Oro", 1001, 5000, 0.10, 1.25),
    PLATINUM("Platino", 5001, Integer.MAX_VALUE, 0.15, 1.5);

    private final String displayName;
    private final int minPoints;
    private final int maxPoints;
    private final double commissionDiscount;
    private final double pointsMultiplier;

    UserLevel(String displayName, int minPoints, int maxPoints,
              double commissionDiscount, double pointsMultiplier) {
        this.displayName = displayName;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.commissionDiscount = commissionDiscount;
        this.pointsMultiplier = pointsMultiplier;
    }

    public String getDisplayName() { return displayName; }
    public int getMinPoints() { return minPoints; }
    public int getMaxPoints() { return maxPoints; }
    public double getCommissionDiscount() { return commissionDiscount; }
    public double getPointsMultiplier() { return pointsMultiplier; }

    public static UserLevel fromPoints(int points) {
        for (UserLevel level : values()) {
            if (points >= level.minPoints && points <= level.maxPoints) {
                return level;
            }
        }
        return BRONZE;
    }

    @Override
    public String toString() { return displayName; }
}