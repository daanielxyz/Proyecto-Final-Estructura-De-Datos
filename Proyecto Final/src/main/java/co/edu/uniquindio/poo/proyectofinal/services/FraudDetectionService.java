package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.AuditEvent;
import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.enums.RiskLevel;
import co.edu.uniquindio.poo.proyectofinal.repositories.AuditRepository;
import co.edu.uniquindio.poo.proyectofinal.repositories.TransactionRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

import java.time.LocalDateTime;

public class FraudDetectionService {

    private static final int MAX_TRANSACTIONS_PER_MINUTE = 5;
    private static final double HIGH_AMOUNT_MULTIPLIER = 3.0;
    private static final int REPEATED_DESTINATION_THRESHOLD = 3;

    private final AuditRepository auditRepository;
    private final NotificationService notificationService;

    public FraudDetectionService(AuditRepository auditRepository,
                                 NotificationService notificationService) {
        this.auditRepository = auditRepository;
        this.notificationService = notificationService;
    }

    /**
     * Analiza una transacción recién creada y detecta patrones sospechosos.
     */
    public void analyze(String userId, Transaction transaction,
                        TransactionRepository transactionRepository) {

        ListaDoble<Transaction> history = transactionRepository.findByWalletId(
                transaction.getSourceWalletId() != null
                        ? transaction.getSourceWalletId()
                        : transaction.getDestinationWalletId());

        checkHighFrequency(userId, transaction, history);
        checkHighAmount(userId, transaction, history);
        checkRepeatedDestination(userId, transaction, history);
        checkUnusualHour(userId, transaction);
    }

    // -------------------------------------------------------------------------
    // Reglas de detección
    // -------------------------------------------------------------------------

    /**
     * Detecta múltiples transacciones consecutivas en menos de 1 minuto.
     */
    private void checkHighFrequency(String userId, Transaction transaction,
                                    ListaDoble<Transaction> history) {
        LocalDateTime oneMinuteAgo = transaction.getDate().minusMinutes(1);
        int count = 0;

        for (Transaction t : history) {
            if (t.getDate().isAfter(oneMinuteAgo) && !t.getId().equals(transaction.getId())) {
                count++;
            }
        }

        if (count >= MAX_TRANSACTIONS_PER_MINUTE) {
            registerEvent(userId, transaction.getId(),
                    "Alta frecuencia: " + count + " transacciones en menos de 1 minuto.",
                    RiskLevel.HIGH);
        }
    }

    /**
     * Detecta montos inusualmente altos respecto al promedio del usuario.
     */
    private void checkHighAmount(String userId, Transaction transaction,
                                 ListaDoble<Transaction> history) {
        if (history.isEmpty()) return;

        double total = 0;
        int count = 0;

        for (Transaction t : history) {
            if (!t.getId().equals(transaction.getId())) {
                total += t.getAmount();
                count++;
            }
        }

        if (count == 0) return;

        double average = total / count;
        if (transaction.getAmount() > average * HIGH_AMOUNT_MULTIPLIER) {
            registerEvent(userId, transaction.getId(),
                    "Monto inusualmente alto: $" + String.format("%.2f", transaction.getAmount())
                            + " (promedio: $" + String.format("%.2f", average) + ")",
                    RiskLevel.MEDIUM);
        }
    }

    /**
     * Detecta transferencias repetitivas hacia el mismo destino en poco tiempo.
     */
    private void checkRepeatedDestination(String userId, Transaction transaction,
                                          ListaDoble<Transaction> history) {
        if (transaction.getDestinationWalletId() == null) return;

        LocalDateTime oneHourAgo = transaction.getDate().minusHours(1);
        int count = 0;

        for (Transaction t : history) {
            if (t.getDate().isAfter(oneHourAgo)
                    && transaction.getDestinationWalletId().equals(t.getDestinationWalletId())
                    && !t.getId().equals(transaction.getId())) {
                count++;
            }
        }

        if (count >= REPEATED_DESTINATION_THRESHOLD) {
            registerEvent(userId, transaction.getId(),
                    "Transferencias repetidas al mismo destino: "
                            + transaction.getDestinationWalletId() + " (" + count + " veces en 1 hora)",
                    RiskLevel.MEDIUM);
        }
    }

    /**
     * Detecta actividad en horarios inusuales (entre 1am y 5am).
     */
    private void checkUnusualHour(String userId, Transaction transaction) {
        int hour = transaction.getDate().getHour();
        if (hour >= 1 && hour <= 5) {
            registerEvent(userId, transaction.getId(),
                    "Transacción en horario inusual: " + hour + ":00",
                    RiskLevel.LOW);
        }
    }

    // -------------------------------------------------------------------------
    // Registro de evento
    // -------------------------------------------------------------------------

    private void registerEvent(String userId, String transactionId,
                               String description, RiskLevel riskLevel) {
        String id = IDGenerator.generateAuditEventId();
        AuditEvent event = new AuditEvent(id, userId, transactionId, description, riskLevel);
        auditRepository.save(event);
        notificationService.sendSecurityAlert(userId, description);
    }
}