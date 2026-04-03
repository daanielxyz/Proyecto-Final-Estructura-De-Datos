package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.NotificationType;

import java.time.LocalDateTime;

public class Notification {

    private String id;
    private String userId;
    private NotificationType type;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;

    public Notification(String id, String userId, NotificationType type, String message) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }

    public void setRead(boolean read) { this.read = read; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s (%s)",
                type.getDisplayName(), createdAt.toString(),
                message, read ? "Leída" : "No leída");
    }
}