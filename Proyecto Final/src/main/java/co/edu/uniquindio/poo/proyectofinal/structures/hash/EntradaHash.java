package co.edu.uniquindio.poo.proyectofinal.structures.hash;

/**
 * Representa una entrada clave-valor dentro de la TablaHash.
 */
public class EntradaHash<K, V> {

    private K key;
    private V value;

    public EntradaHash(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }

    @Override
    public String toString() {
        return key + " -> " + value;
    }
}