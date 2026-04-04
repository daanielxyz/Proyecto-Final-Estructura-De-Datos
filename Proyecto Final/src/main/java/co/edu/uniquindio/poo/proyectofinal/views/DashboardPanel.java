package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public static final String SECTION_WALLETS       = "WALLETS";
    public static final String SECTION_TRANSACTIONS  = "TRANSACTIONS";
    public static final String SECTION_HISTORY       = "HISTORY";
    public static final String SECTION_SCHEDULED     = "SCHEDULED";
    public static final String SECTION_NOTIFICATIONS = "NOTIFICATIONS";
    public static final String SECTION_ANALYTICS     = "ANALYTICS";

    private JLabel levelLabel;
    private JLabel userNameLabel;
    private final MainWindow mainWindow;
    private final User currentUser;
    private final CardLayout contentLayout;
    private final JPanel contentPanel;

    public DashboardPanel(MainWindow mainWindow, User currentUser) {
        this.mainWindow    = mainWindow;
        this.currentUser   = currentUser;
        this.contentLayout = new CardLayout();
        this.contentPanel  = new JPanel(contentLayout);

        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);

        buildSidebar();
        buildContent();

        // Mostrar billeteras por defecto
        contentLayout.show(contentPanel, SECTION_WALLETS);
    }

    // -------------------------------------------------------------------------
    // Sidebar
    // -------------------------------------------------------------------------

    private void buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.BACKGROUND_SECONDARY);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        // Saludo al usuario
        JLabel greeting = UIFactory.createSubtitleLabel("Hola,");
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);

        userNameLabel = UIFactory.createBodyLabel(currentUser.getFullName().split(" ")[0]);
        userNameLabel.setFont(AppFonts.BODY_BOLD);
        userNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        levelLabel = UIFactory.createSecondaryLabel(
                "Nivel: " + currentUser.getLevel().getDisplayName() +
                        " · " + currentUser.getTotalPoints() + " pts");
        levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = UIFactory.createSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Botones de navegación
        JButton walletsBtn       = createNavButton("💳  Billeteras",      SECTION_WALLETS);
        JButton transactionsBtn  = createNavButton("💸  Operaciones",     SECTION_TRANSACTIONS);
        JButton historyBtn       = createNavButton("📋  Historial",       SECTION_HISTORY);
        JButton scheduledBtn     = createNavButton("🗓  Programadas",     SECTION_SCHEDULED);
        JButton notificationsBtn = createNavButton("🔔  Notificaciones",  SECTION_NOTIFICATIONS);
        JButton analyticsBtn     = createNavButton("📊  Analítica",       SECTION_ANALYTICS);

        // Botón cerrar sesión al fondo
        JButton logoutBtn = UIFactory.createSecondaryButton("Cerrar sesión");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        logoutBtn.addActionListener(e -> {
            if (UIFactory.showConfirmation(this, "¿Deseas cerrar sesión?")) {
                mainWindow.onLogout();
            }
        });

        // Ensamblar sidebar
        sidebar.add(greeting);
        sidebar.add(Box.createVerticalStrut(2));
        sidebar.add(userNameLabel);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(levelLabel);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(walletsBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(transactionsBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(historyBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(scheduledBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(notificationsBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(analyticsBtn);
        sidebar.add(Box.createVerticalGlue()); // empuja logout al fondo
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton createNavButton(String text, String section) {
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BODY);
        btn.setForeground(AppColors.TEXT_PRIMARY);
        btn.setBackground(AppColors.BACKGROUND_SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addActionListener(e -> contentLayout.show(contentPanel, section));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.ACCENT_PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(AppColors.BACKGROUND_SECONDARY);
            }
        });

        return btn;
    }

    // -------------------------------------------------------------------------
    // Contenido
    // -------------------------------------------------------------------------

    private void buildContent() {
        contentPanel.setBackground(AppColors.BACKGROUND_PRIMARY);

        contentPanel.add(new WalletsPanel(mainWindow, currentUser),       SECTION_WALLETS);
        contentPanel.add(new TransactionsPanel(mainWindow, currentUser),  SECTION_TRANSACTIONS);
        contentPanel.add(new HistoryPanel(mainWindow, currentUser),       SECTION_HISTORY);
        contentPanel.add(new ScheduledPanel(mainWindow, currentUser),     SECTION_SCHEDULED);
        contentPanel.add(new NotificationsPanel(mainWindow, currentUser), SECTION_NOTIFICATIONS);
        contentPanel.add(new AnalyticsPanel(mainWindow, currentUser),     SECTION_ANALYTICS);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void refreshUserInfo() {
        User updated = mainWindow.getUserService().findById(currentUser.getId());
        levelLabel.setText("Nivel: " + updated.getLevel().getDisplayName() +
                " · " + updated.getTotalPoints() + " pts");
        levelLabel.revalidate();
        levelLabel.repaint();
        
        // Refrescar panel de billeteras también
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof WalletsPanel) {
                ((WalletsPanel) c).refresh();
            }
        }
    }
}