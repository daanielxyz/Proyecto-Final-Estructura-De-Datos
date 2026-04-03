package co.edu.uniquindio.poo.proyectofinal.models;

import co.edu.uniquindio.poo.proyectofinal.models.enums.WalletType;

public class Wallet {

    private String id;
    private String name;
    private WalletType type;
    private double balance;
    private boolean active;
    private String ownerId;

    // PLACEHOLDER Fase 2 → CustomLinkedList<Transaction> transactionHistory

    public Wallet(String id, String name, WalletType type, String ownerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.balance = 0.0;
        this.active = true;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public WalletType getType() { return type; }
    public double getBalance() { return balance; }
    public boolean isActive() { return active; }
    public String getOwnerId() { return ownerId; }

    public void setName(String name) { this.name = name; }
    public void setType(WalletType type) { this.type = type; }
    public void setActive(boolean active) { this.active = active; }

    public void addBalance(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser positivo.");
        this.balance += amount;
    }

    public void subtractBalance(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto debe ser positivo.");
        if (amount > this.balance) throw new IllegalStateException("Saldo insuficiente.");
        this.balance -= amount;
    }

    public boolean hasSufficientFunds(double amount) {
        return this.balance >= amount;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Tipo: %s | Saldo: $%.2f | %s",
                id, name, type.getDisplayName(), balance,
                active ? "Activa" : "Inactiva");
    }
}