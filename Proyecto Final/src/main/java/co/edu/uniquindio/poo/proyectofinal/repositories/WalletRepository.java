package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

public class WalletRepository {

    private final TablaHash<String, Wallet> wallets;

    public WalletRepository() {
        this.wallets = new TablaHash<>();
    }

    public void save(Wallet wallet) {
        wallets.put(wallet.getId(), wallet);
    }

    public Wallet findById(String id) {
        return wallets.get(id);
    }

    public void delete(String id) {
        if (!wallets.containsKey(id)) {
            throw new RuntimeException("Billetera no encontrada: " + id);
        }
        wallets.remove(id);
    }

    public boolean exists(String id) {
        return wallets.containsKey(id);
    }

    public ListaSimple<Wallet> findAll() {
        return wallets.values();
    }

    /**
     * Retorna todas las billeteras de un usuario específico.
     */
    public ListaSimple<Wallet> findByOwnerId(String ownerId) {
        ListaSimple<Wallet> result = new ListaSimple<>();
        for (Wallet wallet : wallets.values()) {
            if (wallet.getOwnerId().equals(ownerId)) {
                result.addLast(wallet);
            }
        }
        return result;
    }
}