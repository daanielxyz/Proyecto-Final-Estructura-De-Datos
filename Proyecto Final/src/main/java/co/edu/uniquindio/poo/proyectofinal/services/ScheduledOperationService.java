package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.ScheduledOperation;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.repositories.ScheduledOperationRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

import java.time.LocalDateTime;

public class ScheduledOperationService {

    private final ScheduledOperationRepository scheduledOperationRepository;
    private final TransactionService transactionService;
    private final NotificationService notificationService;

    public ScheduledOperationService(ScheduledOperationRepository scheduledOperationRepository,
                                     TransactionService transactionService,
                                     NotificationService notificationService) {
        this.scheduledOperationRepository = scheduledOperationRepository;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }

    public ScheduledOperation schedule(String userId, String sourceWalletId,
                                       String destinationWalletId, TransactionType type,
                                       double amount, LocalDateTime scheduledDate,
                                       String description, boolean recurring,
                                       int recurrenceDays) {
        if (scheduledDate.isBefore(LocalDateTime.now()))
            throw new RuntimeException("La fecha programada debe ser futura.");
        if (amount <= 0)
            throw new RuntimeException("El monto debe ser mayor a cero.");

        String id = IDGenerator.generateScheduledOperationId();
        ScheduledOperation operation = new ScheduledOperation(
                id, userId, sourceWalletId, destinationWalletId,
                type, amount, scheduledDate, description);
        operation.setRecurring(recurring);
        operation.setRecurrenceDaysInterval(recurrenceDays);

        scheduledOperationRepository.save(operation);
        return operation;
    }

    public void cancel(String operationId) {
        ScheduledOperation operation = scheduledOperationRepository.findById(operationId);
        if (operation == null)
            throw new RuntimeException("Operación no encontrada: " + operationId);
        if (operation.isExecuted())
            throw new RuntimeException("No se puede cancelar una operación ya ejecutada.");
        operation.setCancelled(true);
    }

    /**
     * Procesa todas las operaciones programadas que ya son ejecutables.
     * Debe llamarse periódicamente (simulación de scheduler).
     */
    public void processDueOperations() {
        while (scheduledOperationRepository.hasOperationsDue()) {
            ScheduledOperation operation = scheduledOperationRepository.pollNext();
            executeOperation(operation);
        }
    }

    public ListaSimple<ScheduledOperation> getPendingByUser(String userId) {
        return scheduledOperationRepository.findPendingByUserId(userId);
    }

    // -------------------------------------------------------------------------
    // Privados
    // -------------------------------------------------------------------------

    private void executeOperation(ScheduledOperation operation) {
        try {
            switch (operation.getType()) {
                case RECHARGE -> transactionService.recharge(
                        operation.getUserId(), operation.getDestinationWalletId(),
                        operation.getAmount(), operation.getDescription());

                case WITHDRAWAL -> transactionService.withdraw(
                        operation.getUserId(), operation.getSourceWalletId(),
                        operation.getAmount(), operation.getDescription());

                case TRANSFER, SCHEDULED_PAYMENT -> transactionService.transferInternal(
                        operation.getUserId(), operation.getSourceWalletId(),
                        operation.getDestinationWalletId(), operation.getAmount(),
                        operation.getDescription());
            }

            operation.setExecuted(true);
            notificationService.sendScheduledExecutedAlert(
                    operation.getUserId(), operation.getDescription(), operation.getAmount());

            // Sí es recurrente, programar la siguiente ejecución
            if (operation.isRecurring() && operation.getRecurrenceDaysInterval() > 0) {
                schedule(operation.getUserId(), operation.getSourceWalletId(),
                        operation.getDestinationWalletId(), operation.getType(),
                        operation.getAmount(),
                        operation.getScheduledDate().plusDays(operation.getRecurrenceDaysInterval()),
                        operation.getDescription(), true, operation.getRecurrenceDaysInterval());
            }

        } catch (Exception e) {
            operation.setCancelled(true);
            notificationService.sendOperationRejectedAlert(
                    operation.getUserId(), e.getMessage());
        }
    }
}