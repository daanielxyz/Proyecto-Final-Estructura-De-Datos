package co.edu.uniquindio.poo.proyectofinal.structures.linear;

import co.edu.uniquindio.poo.proyectofinal.structures.nodes.Nodo;

/**
 * Cola de prioridad propia basada en Nodo.
 * Ordena los elementos usando su compareTo natural (menor valor = mayor prioridad).
 * Se usa para procesar operaciones programadas en orden cronológico.
 */
public class ColaPrioridad<T extends Comparable<T>> {

    private Nodo<T> first;
    private int size;

    public ColaPrioridad() {
        first = null;
        size = 0;
    }

    /**
     * Inserta el elemento en la posición correcta según su prioridad.
     * Menor compareTo = se ubica primero.
     */
    public void enqueue(T value) {
        Nodo<T> newNode = new Nodo<>(value);

        // Si la cola está vacía o el nuevo tiene mayor prioridad que el primero
        if (isEmpty() || value.compareTo(first.getValue()) < 0) {
            newNode.setNext(first);
            first = newNode;
        } else {
            Nodo<T> current = first;
            while (current.getNext() != null &&
                    current.getNext().getValue().compareTo(value) <= 0) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("La cola de prioridad está vacía.");
        T value = first.getValue();
        first = first.getNext();
        size--;
        return value;
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("La cola de prioridad está vacía.");
        return first.getValue();
    }

    public boolean isEmpty() { return first == null; }
    public int getSize() { return size; }
}