package co.edu.uniquindio.poo.proyectofinal.structures.hash;

import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;

/**
 * Tabla hash propia con manejo de colisiones por encadenamiento.
 * Cada posición del arreglo contiene una ListaSimple de EntradaHash.
 *
 * K = tipo de la clave (ej: String para IDs)
 * V = tipo del valor (ej: User, Wallet)
 */
public class TablaHash<K, V> {

    private static final int DEFAULT_CAPACITY = 64;

    private ListaSimple<EntradaHash<K, V>>[] table;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public TablaHash(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.table = new ListaSimple[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ListaSimple<>();
        }
    }

    public TablaHash() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Función hash: convierte la clave en un índice del arreglo.
     */
    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    /**
     * Inserta o actualiza un par clave-valor.
     */
    public void put(K key, V value) {
        int index = hash(key);
        ListaSimple<EntradaHash<K, V>> bucket = table[index];

        for (EntradaHash<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }

        bucket.addLast(new EntradaHash<>(key, value));
        size++;
    }

    /**
     * Retorna el valor asociado a la clave, o null si no existe.
     */
    public V get(K key) {
        int index = hash(key);
        ListaSimple<EntradaHash<K, V>> bucket = table[index];

        for (EntradaHash<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Elimina la entrada con la clave dada.
     */
    public void remove(K key) {
        int index = hash(key);
        ListaSimple<EntradaHash<K, V>> bucket = table[index];

        for (EntradaHash<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
        throw new RuntimeException("Clave no encontrada: " + key);
    }

    /**
     * Verifica si existe una entrada con la clave dada.
     */
    public boolean containsKey(K key) {
        int index = hash(key);
        for (EntradaHash<K, V> entry : table[index]) {
            if (entry.getKey().equals(key)) return true;
        }
        return false;
    }

    /**
     * Retorna todos los valores almacenados en la tabla como ListaSimple.
     */
    public ListaSimple<V> values() {
        ListaSimple<V> result = new ListaSimple<>();
        for (int i = 0; i < capacity; i++) {
            for (EntradaHash<K, V> entry : table[i]) {
                result.addLast(entry.getValue());
            }
        }
        return result;
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }
}