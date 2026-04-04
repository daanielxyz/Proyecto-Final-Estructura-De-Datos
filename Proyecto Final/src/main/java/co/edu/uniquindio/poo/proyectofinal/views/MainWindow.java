package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.repositories.*;
import co.edu.uniquindio.poo.proyectofinal.services.*;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    // -------------------------------------------------------------------------
    // Constantes de navegación
    // -------------------------------------------------------------------------
    public static final String PANEL_LOGIN     = "LOGIN";
    public static final String PANEL_REGISTER  = "REGISTER";
    public static final String PANEL_DASHBOARD = "DASHBOARD";

    // -------------------------------------------------------------------------
    // Layout y panel principal
    // -------------------------------------------------------------------------
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // -------------------------------------------------------------------------
    // Servicios (se instancian aquí y se pasan a los paneles)
    // -------------------------------------------------------------------------
    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    private final ScheduledOperationService scheduledOperationService;
    private final AnalyticsService analyticsService;
    private final FraudDetectionService fraudDetectionService;

    // Usuario actualmente logueado
    private User currentUser;

    public MainWindow() {
        // --- Inicializar repositorios ---
        UserRepository userRepository             = new UserRepository();
        WalletRepository walletRepository         = new WalletRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        NotificationRepository notificationRepository = new NotificationRepository();
        ScheduledOperationRepository scheduledRepo = new ScheduledOperationRepository();
        AuditRepository auditRepository           = new AuditRepository();

        // --- Inicializar servicios ---
        this.userService         = new UserService(userRepository);
        this.walletService       = new WalletService(walletRepository);
        this.notificationService = new NotificationService(notificationRepository);
        this.fraudDetectionService = new FraudDetectionService(auditRepository, notificationService);
        this.transactionService  = new TransactionService(
                transactionRepository, walletRepository,
                userService, walletService,
                notificationService, fraudDetectionService);
        this.scheduledOperationService = new ScheduledOperationService(
                scheduledRepo, transactionService, notificationService);
        this.analyticsService    = new AnalyticsService(
                transactionRepository, walletRepository, userService);

        // --- Configurar ventana ---
        setTitle("FinWallet — Plataforma Fintech");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setBackground(AppColors.BACKGROUND_PRIMARY);

        // --- CardLayout ---
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(AppColors.BACKGROUND_PRIMARY);

        // --- Agregar paneles ---
        mainPanel.add(new LoginPanel(this), PANEL_LOGIN);
        mainPanel.add(new RegisterPanel(this), PANEL_REGISTER);
        // DashboardPanel se agrega dinámicamente al hacer login

        add(mainPanel);
        cardLayout.show(mainPanel, PANEL_LOGIN);
        setVisible(true);
    }

    // -------------------------------------------------------------------------
    // Navegación
    // -------------------------------------------------------------------------

    public void navigateTo(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    /**
     * Llamado tras login exitoso. Crea el dashboard con el usuario autenticado.
     */
    public void onLoginSuccess(User user) {
        this.currentUser = user;
        DashboardPanel dashboard = new DashboardPanel(this, user);
        mainPanel.add(dashboard, PANEL_DASHBOARD);
        cardLayout.show(mainPanel, PANEL_DASHBOARD);
    }

    /**
     * Cierra sesión y vuelve al login.
     */
    public void onLogout() {
        this.currentUser = null;
        mainPanel.remove(mainPanel.getComponentCount() - 1); // remueve dashboard
        cardLayout.show(mainPanel, PANEL_LOGIN);
    }

    // -------------------------------------------------------------------------
    // Getters de servicios (los paneles los usan)
    // -------------------------------------------------------------------------

    public UserService getUserService() { return userService; }
    public WalletService getWalletService() { return walletService; }
    public TransactionService getTransactionService() { return transactionService; }
    public NotificationService getNotificationService() { return notificationService; }
    public ScheduledOperationService getScheduledOperationService() { return scheduledOperationService; }
    public AnalyticsService getAnalyticsService() { return analyticsService; }
    public User getCurrentUser() { return currentUser; }
}