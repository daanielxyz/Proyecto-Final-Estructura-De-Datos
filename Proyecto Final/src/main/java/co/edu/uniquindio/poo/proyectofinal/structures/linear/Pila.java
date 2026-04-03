package co.edu.uniquindio.poo.proyectofinal.structures.linear;

import co.edu.uniquindio.poo.proyectofinal.structures.nodes.NodoDoble;

/**
 * Pila (LIFO) propia basada en NodoDoble.
 * Se usa para manejar operaciones reversibles del usuario.
 */
public class Pila<T> {

    private NodoDoble<T> top;
    private int size;

    public Pila() {
        top = null;
        size = 0;
    }

    public void push(T value) {
        NodoDoble<T> newNode = new NodoDoble<>(value);
        if (!isEmpty()) {
            newNode.setNext(top);
            top.setPrevious(newNode);
        }
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new RuntimeException("La pila está vacía.");
        T value = top.getValue();
        top = top.getNext();
        if (top != null) top.setPrevious(null);
        size--;
        return value;
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("La pila está vacía.");
        return top.getValue();
    }

    public boolean isEmpty() { return top == null; }
    public int getSize() { return size; }
}