package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.Notification;
import co.edu.uniquindio.poo.proyectofinal.models.enums.NotificationType;
import co.edu.uniquindio.poo.proyectofinal.repositories.NotificationRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification send(String userId, NotificationType type, String message) {
        String id = IDGenerator.generateNotificationId();
        Notification notification = new Notification(id, userId, type, message);
        notificationRepository.save(notification);
        return notification;
    }

    public ListaDoble<Notification> getByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId);
        if (notification == null)
            throw new RuntimeException("Notificación no encontrada: " + notificationId);
        notification.setRead(true);
    }

    /**
     * Despacha la siguiente notificación pendiente en la cola.
     */
    public Notification dispatchNext() {
        return notificationRepository.dispatchNext();
    }

    public boolean hasPendingDispatch() {
        return notificationRepository.hasPendingDispatch();
    }

    // -------------------------------------------------------------------------
    // Métodos de conveniencia para generar alertas específicas
    // -------------------------------------------------------------------------

    public void sendLowBalanceAlert(String userId, String walletName, double balance) {
        send(userId, NotificationType.LOW_BALANCE,
                "Saldo bajo en billetera '" + walletName + "': $" + String.format("%.2f", balance));
    }

    public void sendLevelUpAlert(String userId, String newLevel) {
        send(userId, NotificationType.LEVEL_UP,
                "¡Felicitaciones! Subiste al nivel " + newLevel);
    }

    public void sendOperationRejectedAlert(String userId, String reason) {
        send(userId, NotificationType.OPERATION_REJECTED,
                "Operación rechazada: " + reason);
    }

    public void sendSecurityAlert(String userId, String description) {
        send(userId, NotificationType.SECURITY_ALERT,
                "Alerta de seguridad: " + description);
    }

    public void sendScheduledExecutedAlert(String userId, String description, double amount) {
        send(userId, NotificationType.SCHEDULED_EXECUTED,
                "Operación programada ejecutada: " + description + " por $" + String.format("%.2f", amount));
    }

    public void sendReversedAlert(String userId, String transactionId) {
        send(userId, NotificationType.OPERATION_REVERSED,
                "Operación " + transactionId + " revertida exitosamente.");
    }
}