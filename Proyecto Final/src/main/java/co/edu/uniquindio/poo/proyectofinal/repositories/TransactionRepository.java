package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

import java.time.LocalDateTime;

public class TransactionRepository {

    // Acceso rápido por ID
    private final TablaHash<String, Transaction> transactionsById;

    // Historial completo ordenado cronológicamente (más reciente al final)
    private final ListaDoble<Transaction> allTransactions;

    public TransactionRepository() {
        this.transactionsById = new TablaHash<>();
        this.allTransactions = new ListaDoble<>();
    }

    public void save(Transaction transaction) {
        transactionsById.put(transaction.getId(), transaction);
        allTransactions.addLast(transaction);
    }

    public Transaction findById(String id) {
        return transactionsById.get(id);
    }

    public boolean exists(String id) {
        return transactionsById.containsKey(id);
    }

    public ListaDoble<Transaction> findAll() {
        return allTransactions;
    }

    /**
     * Transacciones de una billetera específica (origen o destino).
     */
    public ListaDoble<Transaction> findByWalletId(String walletId) {
        ListaDoble<Transaction> result = new ListaDoble<>();
        for (Transaction t : allTransactions) {
            if (walletId.equals(t.getSourceWalletId()) ||
                    walletId.equals(t.getDestinationWalletId())) {
                result.addLast(t);
            }
        }
        return result;
    }

    /**
     * Transacciones de un usuario en un rango de fechas.
     */
    public ListaDoble<Transaction> findByDateRange(LocalDateTime from, LocalDateTime to) {
        ListaDoble<Transaction> result = new ListaDoble<>();
        for (Transaction t : allTransactions) {
            if (!t.getDate().isBefore(from) && !t.getDate().isAfter(to)) {
                result.addLast(t);
            }
        }
        return result;
    }

    /**
     * Transacciones por tipo.
     */
    public ListaDoble<Transaction> findByType(TransactionType type) {
        ListaDoble<Transaction> result = new ListaDoble<>();
        for (Transaction t : allTransactions) {
            if (t.getType() == type) result.addLast(t);
        }
        return result;
    }
}