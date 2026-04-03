package co.edu.uniquindio.poo.proyectofinal.structures.nodes;

public class Nodo<T> {

    private Nodo<T> next;
    private T value;

    public Nodo(T value) {
        this.value = value;
        this.next = null;
    }

    public Nodo<T> getNext() { return next; }
    public void setNext(Nodo<T> next) { this.next = next; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}