package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.AuditEvent;
import co.edu.uniquindio.poo.proyectofinal.models.enums.RiskLevel;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

public class AuditRepository {

    private final TablaHash<String, AuditEvent> eventsById;
    private final ListaSimple<AuditEvent> allEvents;

    public AuditRepository() {
        this.eventsById = new TablaHash<>();
        this.allEvents = new ListaSimple<>();
    }

    public void save(AuditEvent event) {
        eventsById.put(event.getId(), event);
        allEvents.addLast(event);
    }

    public AuditEvent findById(String id) {
        return eventsById.get(id);
    }

    public ListaSimple<AuditEvent> findAll() {
        return allEvents;
    }

    public ListaSimple<AuditEvent> findByUserId(String userId) {
        ListaSimple<AuditEvent> result = new ListaSimple<>();
        for (AuditEvent event : allEvents) {
            if (event.getUserId().equals(userId)) result.addLast(event);
        }
        return result;
    }

    public ListaSimple<AuditEvent> findByRiskLevel(RiskLevel riskLevel) {
        ListaSimple<AuditEvent> result = new ListaSimple<>();
        for (AuditEvent event : allEvents) {
            if (event.getRiskLevel() == riskLevel) result.addLast(event);
        }
        return result;
    }
}