package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class AnalyticsPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;

    public AnalyticsPanel(MainWindow mainWindow, User currentUser) {
        this.mainWindow  = mainWindow;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        JPanel header = UIFactory.createPanel();
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));
        header.setLayout(new BorderLayout());
        header.add(UIFactory.createTitleLabel("📊 Analítica"), BorderLayout.WEST);

        // Panel de métricas rápidas
        JPanel metricsPanel = UIFactory.createPanel();
        metricsPanel.setLayout(new GridLayout(1, 3, 16, 0));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(8, 28, 16, 28));

        metricsPanel.add(buildMetricCard("Billetera más activa", getMostActiveWallet()));
        metricsPanel.add(buildMetricCard("Usuario más activo (hoy)", getMostActiveUser()));
        metricsPanel.add(buildMetricCard("Total movilizado (hoy)",
                String.format("$%.2f", getTotalToday())));

        // Tabla de frecuencia por tipo
        JLabel freqTitle = UIFactory.createSubtitleLabel("Frecuencia por tipo de transacción");
        freqTitle.setBorder(BorderFactory.createEmptyBorder(0, 28, 8, 0));

        String[] freqCols = {"Tipo", "Cantidad"};
        DefaultTableModel freqModel = new DefaultTableModel(freqCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (TransactionType type : TransactionType.values()) {
            freqModel.addRow(new Object[]{
                    type.getDisplayName(),
                    mainWindow.getAnalyticsService().getCountByType(type)
            });
        }

        JTable freqTable = new JTable(freqModel);
        styleTable(freqTable);

        // Tabla top transacciones
        JLabel topTitle = UIFactory.createSubtitleLabel("Top 5 transacciones por monto");
        topTitle.setBorder(BorderFactory.createEmptyBorder(16, 28, 8, 0));

        String[] topCols = {"ID", "Tipo", "Monto", "Fecha"};
        DefaultTableModel topModel = new DefaultTableModel(topCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        ListaSimple<Transaction> top = mainWindow.getAnalyticsService().getTopTransactions(5);
        for (Transaction t : top) {
            topModel.addRow(new Object[]{
                    t.getId(),
                    t.getType().getDisplayName(),
                    String.format("$%.2f", t.getAmount()),
                    t.getDate().toLocalDate().toString()
            });
        }

        JTable topTable = new JTable(topModel);
        styleTable(topTable);

        // Ensamblar contenido
        JPanel content = UIFactory.createPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(0, 28, 28, 28));
        content.add(freqTitle);
        content.add(new JScrollPane(freqTable));
        content.add(topTitle);
        content.add(new JScrollPane(topTable));

        add(header, BorderLayout.NORTH);
        add(metricsPanel, BorderLayout.CENTER);
        add(content, BorderLayout.SOUTH);
    }

    private JPanel buildMetricCard(String label, String value) {
        JPanel card = UIFactory.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel labelComp = UIFactory.createSecondaryLabel(label);
        labelComp.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueComp = UIFactory.createSubtitleLabel(value != null ? value : "N/A");
        valueComp.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(8));
        card.add(labelComp);
        card.add(Box.createVerticalStrut(8));
        card.add(valueComp);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    private String getMostActiveWallet() {
        Wallet w = mainWindow.getAnalyticsService().getMostActiveWallet();
        return w != null ? w.getName() : "Sin datos";
    }

    private String getMostActiveUser() {
        LocalDateTime from = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime to   = LocalDateTime.now();
        User u = mainWindow.getAnalyticsService().getMostActiveUser(from, to);
        return u != null ? u.getFullName().split(" ")[0] : "Sin datos";
    }

    private double getTotalToday() {
        LocalDateTime from = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime to   = LocalDateTime.now();
        return mainWindow.getAnalyticsService().getTotalAmountInRange(from, to);
    }

    private void styleTable(JTable table) {
        table.setBackground(AppColors.BACKGROUND_SECONDARY);
        table.setForeground(AppColors.TEXT_PRIMARY);
        table.setFont(AppFonts.BODY);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(AppColors.ACCENT_PRIMARY);
        table.getTableHeader().setForeground(AppColors.TEXT_PRIMARY);
        table.getTableHeader().setFont(AppFonts.BODY_BOLD);
        table.setSelectionBackground(AppColors.ACCENT_HIGHLIGHT);
        table.setSelectionForeground(AppColors.TEXT_PRIMARY);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}