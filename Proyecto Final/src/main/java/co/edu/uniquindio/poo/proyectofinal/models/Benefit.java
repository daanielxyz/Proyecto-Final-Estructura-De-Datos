package co.edu.uniquindio.poo.proyectofinal.models;

import java.time.LocalDateTime;

public class Benefit {

    private String id;
    private String userId;
    private String name;
    private String description;
    private int pointsCost;
    private LocalDateTime redeemedAt;
    private boolean active;

    public Benefit(String id, String userId, String name, String description, int pointsCost) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.pointsCost = pointsCost;
        this.redeemedAt = LocalDateTime.now();
        this.active = true;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getPointsCost() { return pointsCost; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Costo: %d pts | Canjeado: %s",
                id, name, pointsCost, redeemedAt.toString());
    }
}