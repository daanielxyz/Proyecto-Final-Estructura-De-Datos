package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.UserLevel;

public class User {

    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private int totalPoints;
    private UserLevel level;
    private boolean active;

    // PLACEHOLDER Fase 2 → CustomLinkedList<Wallet> wallets
    // PLACEHOLDER Fase 2 → CustomLinkedList<Transaction> transactionHistory
    // PLACEHOLDER Fase 2 → CustomLinkedList<Notification> notifications
    // PLACEHOLDER Fase 2 → CustomLinkedList<Benefit> redeemedBenefits
    // PLACEHOLDER Fase 2 → CustomLinkedList<AuditEvent> auditEvents
    // PLACEHOLDER Fase 2 → CustomStack<Transaction> reversibleOperations

    public User(String id, String fullName, String email, String phone, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.totalPoints = 0;
        this.level = UserLevel.BRONZE;
        this.active = true;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public int getTotalPoints() { return totalPoints; }
    public UserLevel getLevel() { return level; }
    public boolean isActive() { return active; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setActive(boolean active) { this.active = active; }

    public void addPoints(int points) {
        if (points < 0) throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        this.totalPoints += points;
        recalculateLevel();
    }

    public void subtractPoints(int points) {
        if (points < 0) throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        this.totalPoints = Math.max(0, this.totalPoints - points);
        recalculateLevel();
    }

    public boolean recalculateLevel() {
        UserLevel newLevel = UserLevel.fromPoints(this.totalPoints);
        if (newLevel != this.level) {
            this.level = newLevel;
            return true;
        }
        return false;
    }

    public boolean hasEnoughPoints(int requiredPoints) {
        return this.totalPoints >= requiredPoints;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Email: %s | Puntos: %d | Nivel: %s | %s",
                id, fullName, email, totalPoints,
                level.getDisplayName(), active ? "Activo" : "Inactivo");
    }
}