package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.repositories.TransactionRepository;
import co.edu.uniquindio.poo.proyectofinal.repositories.WalletRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;

import java.time.LocalDateTime;

public class AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;

    public AnalyticsService(TransactionRepository transactionRepository,
                            WalletRepository walletRepository,
                            UserService userService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userService = userService;
    }

    /**
     * Monto total movilizado en un rango de fechas.
     */
    public double getTotalAmountInRange(LocalDateTime from, LocalDateTime to) {
        double total = 0;
        for (Transaction t : transactionRepository.findByDateRange(from, to)) {
            total += t.getAmount();
        }
        return total;
    }

    /**
     * Usuario con más transacciones en un rango de fechas.
     */
    public User getMostActiveUser(LocalDateTime from, LocalDateTime to) {
        ListaSimple<User> users = userService.findAll();
        User mostActive = null;
        int maxCount = 0;

        for (User user : users) {
            int count = 0;
            ListaSimple<Wallet> wallets = walletRepository.findByOwnerId(user.getId());
            for (Wallet wallet : wallets) {
                for (Transaction t : transactionRepository.findByDateRange(from, to)) {
                    if (wallet.getId().equals(t.getSourceWalletId()) ||
                            wallet.getId().equals(t.getDestinationWalletId())) {
                        count++;
                    }
                }
            }
            if (count > maxCount) {
                maxCount = count;
                mostActive = user;
            }
        }
        return mostActive;
    }

    /**
     * Billetera con mayor volumen de transacciones.
     */
    public Wallet getMostActiveWallet() {
        ListaSimple<Wallet> wallets = walletRepository.findAll();
        Wallet mostActive = null;
        int maxCount = 0;

        for (Wallet wallet : wallets) {
            int count = transactionRepository.findByWalletId(wallet.getId()).getSize();
            if (count > maxCount) {
                maxCount = count;
                mostActive = wallet;
            }
        }
        return mostActive;
    }

    /**
     * Frecuencia de transacciones por tipo.
     */
    public int getCountByType(TransactionType type) {
        return transactionRepository.findByType(type).getSize();
    }

    /**
     * Transacciones de mayor valor (top N).
     */
    public ListaSimple<Transaction> getTopTransactions(int n) {
        ListaDoble<Transaction> all = transactionRepository.findAll();
        ListaSimple<Transaction> top = new ListaSimple<>();

        // Selección simple de los N mayores
        for (int i = 0; i < n; i++) {
            Transaction max = null;
            for (Transaction t : all) {
                if (!isInList(top, t) && (max == null || t.getAmount() > max.getAmount())) {
                    max = t;
                }
            }
            if (max != null) top.addLast(max);
        }
        return top;
    }

    private boolean isInList(ListaSimple<Transaction> list, Transaction transaction) {
        for (Transaction t : list) {
            if (t.getId().equals(transaction.getId())) return true;
        }
        return false;
    }
}