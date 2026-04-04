package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;
    private DefaultTableModel tableModel;

    public HistoryPanel(MainWindow mainWindow, User currentUser) {
        this.mainWindow  = mainWindow;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel header = UIFactory.createPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));

        JLabel title = UIFactory.createTitleLabel("📋 Historial de movimientos");

        // Selector de billetera
        JPanel filterPanel = UIFactory.createPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JComboBox<String> walletCombo = new JComboBox<>();
        walletCombo.setFont(AppFonts.BODY);
        walletCombo.setBackground(AppColors.ACCENT_PRIMARY);
        walletCombo.setForeground(AppColors.TEXT_PRIMARY);
        walletCombo.addItem("Todas las billeteras");

        ListaSimple<Wallet> wallets = mainWindow.getWalletService()
                .findByOwnerId(currentUser.getId());
        for (Wallet w : wallets) walletCombo.addItem(w.getId());

        filterPanel.add(UIFactory.createBodyLabel("Billetera:"));
        filterPanel.add(walletCombo);

        header.add(title, BorderLayout.WEST);
        header.add(filterPanel, BorderLayout.EAST);

        // Tabla
        String[] columns = {"ID", "Fecha", "Tipo", "Monto", "Origen", "Destino", "Estado", "Puntos", "Riesgo"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(AppColors.BACKGROUND_SECONDARY);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 28, 28, 28));

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        loadAllTransactions();

        walletCombo.addActionListener(e -> {
            String selected = (String) walletCombo.getSelectedItem();
            if ("Todas las billeteras".equals(selected)) {
                loadAllTransactions();
            } else {
                loadByWallet(selected);
            }
        });
    }

    private void loadAllTransactions() {
        tableModel.setRowCount(0);
        ListaSimple<Wallet> wallets = mainWindow.getWalletService()
                .findByOwnerId(currentUser.getId());

        for (Wallet w : wallets) {
            ListaDoble<Transaction> history = mainWindow.getTransactionService()
                    .getHistory(w.getId());
            for (Transaction t : history) {
                // Evitar duplicados si una transacción aparece en origen y destino
                boolean alreadyAdded = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(t.getId())) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (!alreadyAdded) addRow(t);
            }
        }
    }

    private void loadByWallet(String walletId) {
        tableModel.setRowCount(0);
        ListaDoble<Transaction> history = mainWindow.getTransactionService()
                .getHistory(walletId);
        for (Transaction t : history) addRow(t);
    }

    private void addRow(Transaction t) {
        tableModel.addRow(new Object[]{
                t.getId(),
                t.getDate().toLocalDate().toString(),
                t.getType().getDisplayName(),
                String.format("$%.2f", t.getAmount()),
                t.getSourceWalletId() != null ? t.getSourceWalletId() : "-",
                t.getDestinationWalletId() != null ? t.getDestinationWalletId() : "-",
                t.getStatus().getDisplayName(),
                t.getPointsGenerated(),
                t.getRiskLevel().getDisplayName()
        });
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