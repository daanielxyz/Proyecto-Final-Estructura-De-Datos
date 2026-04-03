package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionStatus;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.repositories.TransactionRepository;
import co.edu.uniquindio.poo.proyectofinal.repositories.WalletRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.linear.Pila;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

import java.time.LocalDateTime;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final NotificationService notificationService;
    private final FraudDetectionService fraudDetectionService;

    // Pila global de operaciones reversibles
    private final Pila<Transaction> reversibleStack;

    public TransactionService(TransactionRepository transactionRepository,
                              WalletRepository walletRepository,
                              UserService userService,
                              WalletService walletService,
                              NotificationService notificationService,
                              FraudDetectionService fraudDetectionService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.notificationService = notificationService;
        this.fraudDetectionService = fraudDetectionService;
        this.reversibleStack = new Pila<>();
    }

    // -------------------------------------------------------------------------
    // Recarga
    // -------------------------------------------------------------------------

    public Transaction recharge(String userId, String walletId, double amount, String description) {
        validateAmount(amount);
        walletService.validateOwnership(walletId, userId);

        Wallet wallet = walletService.findById(walletId);
        wallet.addBalance(amount);
        walletRepository.save(wallet);

        Transaction transaction = createTransaction(
                TransactionType.RECHARGE, amount, null, walletId, description);

        processPoints(userId, transaction);
        reversibleStack.push(transaction);
        checkLowBalance(userId, wallet);
        fraudDetectionService.analyze(userId, transaction, transactionRepository);

        return transaction;
    }

    // -------------------------------------------------------------------------
    // Retiro
    // -------------------------------------------------------------------------

    public Transaction withdraw(String userId, String walletId, double amount, String description) {
        validateAmount(amount);
        walletService.validateOwnership(walletId, userId);

        Wallet wallet = walletService.findById(walletId);
        if (!wallet.hasSufficientFunds(amount))
            throw new RuntimeException("Saldo insuficiente para el retiro.");

        wallet.subtractBalance(amount);
        walletRepository.save(wallet);

        Transaction transaction = createTransaction(
                TransactionType.WITHDRAWAL, amount, walletId, null, description);

        processPoints(userId, transaction);
        reversibleStack.push(transaction);
        checkLowBalance(userId, wallet);
        fraudDetectionService.analyze(userId, transaction, transactionRepository);

        return transaction;
    }

    // -------------------------------------------------------------------------
    // Transferencia entre billeteras del mismo usuario
    // -------------------------------------------------------------------------

    public Transaction transferInternal(String userId, String sourceWalletId,
                                        String destinationWalletId, double amount,
                                        String description) {
        validateAmount(amount);
        walletService.validateOwnership(sourceWalletId, userId);
        walletService.validateOwnership(destinationWalletId, userId);

        Wallet source = walletService.findById(sourceWalletId);
        Wallet destination = walletService.findById(destinationWalletId);

        if (!source.hasSufficientFunds(amount))
            throw new RuntimeException("Saldo insuficiente en la billetera origen.");

        source.subtractBalance(amount);
        destination.addBalance(amount);
        walletRepository.save(source);
        walletRepository.save(destination);

        Transaction transaction = createTransaction(
                TransactionType.TRANSFER, amount, sourceWalletId, destinationWalletId, description);

        processPoints(userId, transaction);
        reversibleStack.push(transaction);
        checkLowBalance(userId, source);
        fraudDetectionService.analyze(userId, transaction, transactionRepository);

        return transaction;
    }

    // -------------------------------------------------------------------------
    // Transferencia a otro usuario
    // -------------------------------------------------------------------------

    public Transaction transferExternal(String senderUserId, String sourceWalletId,
                                        String destinationWalletId, double amount,
                                        String description) {
        validateAmount(amount);
        walletService.validateOwnership(sourceWalletId, senderUserId);

        Wallet source = walletService.findById(sourceWalletId);
        Wallet destination = walletService.findById(destinationWalletId);

        if (!source.hasSufficientFunds(amount))
            throw new RuntimeException("Saldo insuficiente en la billetera origen.");

        source.subtractBalance(amount);
        destination.addBalance(amount);
        walletRepository.save(source);
        walletRepository.save(destination);

        Transaction transaction = createTransaction(
                TransactionType.TRANSFER, amount, sourceWalletId, destinationWalletId, description);

        processPoints(senderUserId, transaction);
        reversibleStack.push(transaction);
        checkLowBalance(senderUserId, source);
        fraudDetectionService.analyze(senderUserId, transaction, transactionRepository);

        return transaction;
    }

    // -------------------------------------------------------------------------
    // Reversión
    // -------------------------------------------------------------------------

    public Transaction reverse(String userId) {
        if (reversibleStack.isEmpty())
            throw new RuntimeException("No hay operaciones reversibles disponibles.");

        Transaction transaction = reversibleStack.pop();

        if (!transaction.isReversible())
            throw new RuntimeException("Esta operación no puede ser revertida.");
        if (transaction.getStatus() == TransactionStatus.REVERSED)
            throw new RuntimeException("Esta operación ya fue revertida.");

        // Deshacer movimiento de saldo
        if (transaction.getSourceWalletId() != null) {
            Wallet source = walletService.findById(transaction.getSourceWalletId());
            source.addBalance(transaction.getAmount());
            walletRepository.save(source);
        }
        if (transaction.getDestinationWalletId() != null) {
            Wallet destination = walletService.findById(transaction.getDestinationWalletId());
            destination.subtractBalance(transaction.getAmount());
            walletRepository.save(destination);
        }

        // Descontar puntos generados
        userService.subtractPoints(userId, transaction.getPointsGenerated());

        transaction.setStatus(TransactionStatus.REVERSED);
        transaction.setReversible(false);
        transactionRepository.save(transaction);

        notificationService.sendReversedAlert(userId, transaction.getId());

        return transaction;
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    public Transaction findById(String id) {
        Transaction t = transactionRepository.findById(id);
        if (t == null) throw new RuntimeException("Transacción no encontrada: " + id);
        return t;
    }

    public ListaDoble<Transaction> getHistory(String walletId) {
        return transactionRepository.findByWalletId(walletId);
    }

    public ListaDoble<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public ListaDoble<Transaction> getByDateRange(LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findByDateRange(from, to);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private Transaction createTransaction(TransactionType type, double amount,
                                          String sourceWalletId, String destinationWalletId,
                                          String description) {
        String id = IDGenerator.generateTransactionId();
        Transaction transaction = new Transaction(
                id, LocalDateTime.now(), type, amount,
                sourceWalletId, destinationWalletId, description);
        transactionRepository.save(transaction);
        return transaction;
    }

    private void processPoints(String userId, Transaction transaction) {
        User user = userService.findById(userId);
        int basePoints = transaction.calculateBasePoints();
        int finalPoints = (int) (basePoints * user.getLevel().getPointsMultiplier());
        transaction.setPointsGenerated(finalPoints);
        boolean leveledUp = userService.addPoints(userId, finalPoints);
        if (leveledUp) {
            User updated = userService.findById(userId);
            notificationService.sendLevelUpAlert(userId, updated.getLevel().getDisplayName());
        }
    }

    private void checkLowBalance(String userId, Wallet wallet) {
        if (wallet.getBalance() < 50000) {
            notificationService.sendLowBalanceAlert(
                    userId, wallet.getName(), wallet.getBalance());
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) throw new RuntimeException("El monto debe ser mayor a cero.");
    }
}