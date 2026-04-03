package co.edu.uniquindio.poo.proyectofinal.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {

    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public static String generateUserId() { return "USR-" + now() + "-" + nextCount(); }
    public static String generateWalletId() { return "WAL-" + now() + "-" + nextCount(); }
    public static String generateTransactionId() { return "TXN-" + now() + "-" + nextCount(); }
    public static String generateScheduledOperationId() { return "SCH-" + now() + "-" + nextCount(); }
    public static String generateNotificationId() { return "NOT-" + now() + "-" + nextCount(); }
    public static String generateBenefitId() { return "BEN-" + now() + "-" + nextCount(); }
    public static String generateAuditEventId() { return "AUD-" + now() + "-" + nextCount(); }

    private static String now() { return LocalDateTime.now().format(formatter); }
    private static String nextCount() { return String.format("%04d", counter.getAndIncrement()); }
}