package co.edu.uniquindio.poo.proyectofinal.structures.lists;

import co.edu.uniquindio.poo.proyectofinal.structures.nodes.Nodo;

import java.util.Iterator;

public class ListaSimple<T> implements Iterable<T> {

    private Nodo<T> first;
    private Nodo<T> last;
    private int size;

    public ListaSimple() {
        first = null;
        last = null;
        size = 0;
    }

    public void addFirst(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            newNode.setNext(first);
            first = newNode;
        }
        size++;
    }

    public void addLast(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            last.setNext(newNode);
            last = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new RuntimeException("La lista está vacía.");
        T value = first.getValue();
        first = first.getNext();
        if (first == null) last = null;
        size--;
        return value;
    }

    public T remove(T value) {
        Nodo<T> current = first;
        Nodo<T> previous = null;

        while (current != null) {
            if (current.getValue().equals(value)) {
                if (previous == null) {
                    first = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                if (current == last) last = previous;
                size--;
                return value;
            }
            previous = current;
            current = current.getNext();
        }
        throw new RuntimeException("Elemento no encontrado.");
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new RuntimeException("Índice fuera de rango.");
        Nodo<T> current = first;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
    }

    public T getFirst() {
        if (isEmpty()) throw new RuntimeException("La lista está vacía.");
        return first.getValue();
    }

    public T getLast() {
        if (isEmpty()) throw new RuntimeException("La lista está vacía.");
        return last.getValue();
    }

    public boolean contains(T value) {
        Nodo<T> current = first;
        while (current != null) {
            if (current.getValue().equals(value)) return true;
            current = current.getNext();
        }
        return false;
    }

    public boolean isEmpty() { return first == null; }
    public int getSize() { return size; }

    public void printList() {
        Nodo<T> current = first;
        while (current != null) {
            System.out.print(current.getValue() + "\t");
            current = current.getNext();
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> current = first;

            public boolean hasNext() { return current != null; }

            public T next() {
                T value = current.getValue();
                current = current.getNext();
                return value;
            }
        };
    }
}