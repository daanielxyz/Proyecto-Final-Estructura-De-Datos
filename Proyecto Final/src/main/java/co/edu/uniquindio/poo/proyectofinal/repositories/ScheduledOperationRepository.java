package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.ScheduledOperation;
import co.edu.uniquindio.poo.proyectofinal.structures.linear.ColaPrioridad;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

public class ScheduledOperationRepository {

    // Acceso rápido por ID
    private final TablaHash<String, ScheduledOperation> operationsById;

    // Cola de prioridad para procesarlas en orden cronológico
    private final ColaPrioridad<ScheduledOperation> queue;

    public ScheduledOperationRepository() {
        this.operationsById = new TablaHash<>();
        this.queue = new ColaPrioridad<>();
    }

    public void save(ScheduledOperation operation) {
        operationsById.put(operation.getId(), operation);
        queue.enqueue(operation);
    }

    public ScheduledOperation findById(String id) {
        return operationsById.get(id);
    }

    public boolean exists(String id) {
        return operationsById.containsKey(id);
    }

    /**
     * Retorna la próxima operación a ejecutar sin sacarla de la cola.
     */
    public ScheduledOperation peekNext() {
        return queue.isEmpty() ? null : queue.peek();
    }

    /**
     * Saca y retorna la próxima operación a ejecutar.
     */
    public ScheduledOperation pollNext() {
        return queue.isEmpty() ? null : queue.dequeue();
    }

    public boolean hasOperationsDue() {
        return !queue.isEmpty() && queue.peek().isDue();
    }

    /**
     * Todas las operaciones pendientes de un usuario.
     */
    public ListaSimple<ScheduledOperation> findPendingByUserId(String userId) {
        ListaSimple<ScheduledOperation> result = new ListaSimple<>();
        for (ScheduledOperation op : operationsById.values()) {
            if (op.getUserId().equals(userId) && !op.isExecuted() && !op.isCancelled()) {
                result.addLast(op);
            }
        }
        return result;
    }
}