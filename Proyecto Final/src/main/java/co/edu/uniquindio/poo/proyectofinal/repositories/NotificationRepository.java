package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.Notification;
import co.edu.uniquindio.poo.proyectofinal.structures.linear.Cola;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

public class NotificationRepository {

    // Acceso rápido por ID
    private final TablaHash<String, Notification> notificationsById;

    // Historial por usuario
    private final TablaHash<String, ListaDoble<Notification>> byUser;

    // Cola de despacho pendiente
    private final Cola<Notification> dispatchQueue;

    public NotificationRepository() {
        this.notificationsById = new TablaHash<>();
        this.byUser = new TablaHash<>();
        this.dispatchQueue = new Cola<>();
    }

    public void save(Notification notification) {
        notificationsById.put(notification.getId(), notification);

        // Agregar al historial del usuario
        if (!byUser.containsKey(notification.getUserId())) {
            byUser.put(notification.getUserId(), new ListaDoble<>());
        }
        byUser.get(notification.getUserId()).addLast(notification);

        // Encolar para despacho
        dispatchQueue.enqueue(notification);
    }

    public Notification findById(String id) {
        return notificationsById.get(id);
    }

    /**
     * Historial de notificaciones de un usuario, del más reciente al más antiguo.
     */
    public ListaDoble<Notification> findByUserId(String userId) {
        ListaDoble<Notification> userNotifications = byUser.get(userId);
        return userNotifications != null ? userNotifications : new ListaDoble<>();
    }

    /**
     * Despacha la siguiente notificación pendiente.
     */
    public Notification dispatchNext() {
        return dispatchQueue.isEmpty() ? null : dispatchQueue.dequeue();
    }

    public boolean hasPendingDispatch() {
        return !dispatchQueue.isEmpty();
    }
}