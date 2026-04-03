package co.edu.uniquindio.poo.proyectofinal.structures.lists;

import co.edu.uniquindio.poo.proyectofinal.structures.nodes.NodoDoble;

import java.util.Iterator;

public class ListaDoble<T> implements Iterable<T> {

    private NodoDoble<T> first;
    private NodoDoble<T> last;
    private int size;

    public ListaDoble() {
        first = null;
        last = null;
        size = 0;
    }

    public void addFirst(T value) {
        NodoDoble<T> newNode = new NodoDoble<>(value);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            newNode.setNext(first);
            first.setPrevious(newNode);
            first = newNode;
        }
        size++;
    }

    public void addLast(T value) {
        NodoDoble<T> newNode = new NodoDoble<>(value);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            last.setNext(newNode);
            newNode.setPrevious(last);
            last = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) throw new RuntimeException("La lista está vacía.");
        T value = first.getValue();
        first = first.getNext();
        if (first != null) {
            first.setPrevious(null);
        } else {
            last = null;
        }
        size--;
        return value;
    }

    public T removeLast() {
        if (isEmpty()) throw new RuntimeException("La lista está vacía.");
        T value = last.getValue();
        last = last.getPrevious();
        if (last != null) {
            last.setNext(null);
        } else {
            first = null;
        }
        size--;
        return value;
    }

    public T remove(T value) {
        NodoDoble<T> current = first;

        while (current != null) {
            if (current.getValue().equals(value)) {
                if (current.getPrevious() != null) {
                    current.getPrevious().setNext(current.getNext());
                } else {
                    first = current.getNext();
                }
                if (current.getNext() != null) {
                    current.getNext().setPrevious(current.getPrevious());
                } else {
                    last = current.getPrevious();
                }
                size--;
                return value;
            }
            current = current.getNext();
        }
        throw new RuntimeException("Elemento no encontrado.");
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new RuntimeException("Índice fuera de rango.");
        NodoDoble<T> current = first;
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
        NodoDoble<T> current = first;
        while (current != null) {
            if (current.getValue().equals(value)) return true;
            current = current.getNext();
        }
        return false;
    }

    public boolean isEmpty() { return first == null; }
    public int getSize() { return size; }

    /**
     * Recorre la lista de último a primero.
     * Útil para mostrar historial del más reciente al más antiguo.
     */
    public Iterator<T> iteratorReverse() {
        return new Iterator<T>() {
            private NodoDoble<T> current = last;

            public boolean hasNext() { return current != null; }

            public T next() {
                T value = current.getValue();
                current = current.getPrevious();
                return value;
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoDoble<T> current = first;

            public boolean hasNext() { return current != null; }

            public T next() {
                T value = current.getValue();
                current = current.getNext();
                return value;
            }
        };
    }
}