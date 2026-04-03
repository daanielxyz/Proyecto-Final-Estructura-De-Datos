package co.edu.uniquindio.poo.proyectofinal.structures.nodes;

public class NodoDoble<T> {

    private NodoDoble<T> next;
    private NodoDoble<T> previous;
    private T value;

    public NodoDoble(T value) {
        this.value = value;
        this.next = null;
        this.previous = null;
    }

    public NodoDoble<T> getNext() { return next; }
    public void setNext(NodoDoble<T> next) { this.next = next; }
    public NodoDoble<T> getPrevious() { return previous; }
    public void setPrevious(NodoDoble<T> previous) { this.previous = previous; }
    public T getValue() { return value; }
    public void setValue(T value) { this.value = value; }
}