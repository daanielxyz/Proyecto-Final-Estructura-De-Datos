package co.edu.uniquindio.poo.proyectofinal.structures.graphs;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;

/**
 * Grafo dirigido y ponderado que representa transferencias entre usuarios.
 * Permite analizar relaciones, detectar rutas frecuentes y ciclos.
 *
 * STUB — implementación completa en Fase 6 cuando se vea el tema en clase.
 */
public class GrafoTransferencias {

    // TODO Fase 6: definir representación (matriz de adyacencia o lista de adyacencia)

    public void agregarUsuario(String userId) {
        // TODO Fase 6
    }

    public void registrarTransferencia(String fromUserId, String toUserId, double amount) {
        // TODO Fase 6
    }

    public ListaSimple<String> obtenerDestinosFrecuentes(String userId) {
        // TODO Fase 6
        return new ListaSimple<>();
    }

    public boolean existeCiclo() {
        // TODO Fase 6
        return false;
    }

    public ListaSimple<Transaction> obtenerRutaEntreUsuarios(String fromUserId, String toUserId) {
        // TODO Fase 6
        return new ListaSimple<>();
    }
}