package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.enums.WalletType;
import co.edu.uniquindio.poo.proyectofinal.repositories.WalletRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet create(String ownerId, String name, WalletType type) {
        if (name == null || name.isBlank())
            throw new RuntimeException("El nombre de la billetera no puede estar vacío.");

        String id = IDGenerator.generateWalletId();
        Wallet wallet = new Wallet(id, name, type, ownerId);
        walletRepository.save(wallet);
        return wallet;
    }

    public Wallet findById(String id) {
        Wallet wallet = walletRepository.findById(id);
        if (wallet == null) throw new RuntimeException("Billetera no encontrada: " + id);
        return wallet;
    }

    public ListaSimple<Wallet> findByOwnerId(String ownerId) {
        return walletRepository.findByOwnerId(ownerId);
    }

    public ListaSimple<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Wallet update(String id, String newName) {
        Wallet wallet = findById(id);
        if (newName != null && !newName.isBlank()) wallet.setName(newName);
        walletRepository.save(wallet);
        return wallet;
    }

    public void deactivate(String id) {
        Wallet wallet = findById(id);
        wallet.setActive(false);
        walletRepository.save(wallet);
    }

    public double getBalance(String walletId) {
        return findById(walletId).getBalance();
    }

    /**
     * Valida que la billetera exista, esté activa y pertenezca al usuario dado.
     */
    public void validateOwnership(String walletId, String ownerId) {
        Wallet wallet = findById(walletId);
        if (!wallet.isActive())
            throw new RuntimeException("La billetera está inactiva.");
        if (!wallet.getOwnerId().equals(ownerId))
            throw new RuntimeException("La billetera no pertenece al usuario.");
    }
}