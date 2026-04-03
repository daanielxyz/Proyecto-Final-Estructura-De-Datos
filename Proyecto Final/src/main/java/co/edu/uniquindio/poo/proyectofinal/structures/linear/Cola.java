package co.edu.uniquindio.poo.proyectofinal.structures.linear;

import co.edu.uniquindio.poo.proyectofinal.structures.nodes.Nodo;

/**
 * Cola (FIFO) propia basada en Nodo.
 * Se usa para despachar notificaciones pendientes en orden de llegada.
 */
public class Cola<T> {

    private Nodo<T> first;
    private Nodo<T> last;
    private int size;

    public Cola() {
        first = null;
        last = null;
        size = 0;
    }

    public void enqueue(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            last.setNext(newNode);
            last = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("La cola está vacía.");
        T value = first.getValue();
        first = first.getNext();
        if (first == null) last = null;
        size--;
        return value;
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("La cola está vacía.");
        return first.getValue();
    }

    public boolean isEmpty() { return first == null; }
    public int getSize() { return size; }
}