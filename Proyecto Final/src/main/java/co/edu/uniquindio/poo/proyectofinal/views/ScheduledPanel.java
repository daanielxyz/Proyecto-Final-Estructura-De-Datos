package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.ScheduledOperation;
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

public class ScheduledPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;
    private DefaultTableModel tableModel;

    public ScheduledPanel(MainWindow mainWindow, User currentUser) {
        this.mainWindow  = mainWindow;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        JPanel header = UIFactory.createPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));

        JLabel title = UIFactory.createTitleLabel("🗓 Operaciones Programadas");
        JButton addBtn = UIFactory.createPrimaryButton("+ Programar");
        addBtn.addActionListener(e -> showScheduleDialog());

        header.add(title, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);

        String[] columns = {"ID", "Tipo", "Monto", "Descripción", "Fecha programada", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(AppColors.BACKGROUND_SECONDARY);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        JPanel actions = UIFactory.createPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(12, 28, 24, 28));
        actions.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton processBtn = UIFactory.createPrimaryButton("▶ Ejecutar pendientes");
        JButton refreshBtn = UIFactory.createSecondaryButton("Actualizar");
        processBtn.addActionListener(e -> handleProcess());
        refreshBtn.addActionListener(e -> loadData());

        actions.add(processBtn);
        actions.add(refreshBtn);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        ListaSimple<ScheduledOperation> ops = mainWindow.getScheduledOperationService()
                .getPendingByUser(currentUser.getId());
        for (ScheduledOperation op : ops) {
            tableModel.addRow(new Object[]{
                    op.getId(),
                    op.getType().getDisplayName(),
                    String.format("$%.2f", op.getAmount()),
                    op.getDescription(),
                    op.getScheduledDate().toString(),
                    op.isExecuted() ? "Ejecutada" : (op.isCancelled() ? "Cancelada" : "Pendiente")
            });
        }
    }

    private void showScheduleDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Programar operación");
        dialog.setSize(420, 440);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = UIFactory.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<TransactionType> typeCombo = new JComboBox<>(TransactionType.values());
        typeCombo.setFont(AppFonts.BODY);
        typeCombo.setBackground(AppColors.ACCENT_PRIMARY);
        typeCombo.setForeground(AppColors.TEXT_PRIMARY);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JComboBox<Wallet> sourceCombo = buildWalletCombo();
        JComboBox<Wallet> destCombo   = buildWalletCombo();

        JTextField amountField = UIFactory.createTextField("Monto");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField descField = UIFactory.createTextField("Descripción");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField dateField = UIFactory.createTextField("yyyy-MM-ddTHH:mm (ej: 2025-12-01T10:00)");
        dateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JCheckBox recurringCheck = new JCheckBox("Recurrente");
        recurringCheck.setFont(AppFonts.BODY);
        recurringCheck.setForeground(AppColors.TEXT_PRIMARY);
        recurringCheck.setBackground(AppColors.BACKGROUND_SECONDARY);

        JTextField daysField = UIFactory.createTextField("Intervalo en días");
        daysField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        daysField.setEnabled(false);
        recurringCheck.addActionListener(e -> daysField.setEnabled(recurringCheck.isSelected()));

        JButton confirmBtn = UIFactory.createPrimaryButton("Programar");
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmBtn.addActionListener(e -> {
            try {
                Wallet source = (Wallet) sourceCombo.getSelectedItem();
                Wallet dest   = (Wallet) destCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                LocalDateTime date = LocalDateTime.parse(dateField.getText().trim());
                boolean recurring = recurringCheck.isSelected();
                int days = recurring ? Integer.parseInt(daysField.getText().trim()) : 0;

                mainWindow.getScheduledOperationService().schedule(
                        currentUser.getId(),
                        source != null ? source.getId() : null,
                        dest != null ? dest.getId() : null,
                        (TransactionType) typeCombo.getSelectedItem(),
                        amount, date, descField.getText().trim(), recurring, days);

                UIFactory.showSuccess(dialog, "Operación programada exitosamente.");
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                UIFactory.showError(dialog, "Error: " + ex.getMessage());
            }
        });

        panel.add(UIFactory.createBodyLabel("Tipo de operación"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UIFactory.createBodyLabel("Billetera origen"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(sourceCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UIFactory.createBodyLabel("Billetera destino"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(destCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UIFactory.createBodyLabel("Monto"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UIFactory.createBodyLabel("Fecha y hora"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(dateField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(UIFactory.createBodyLabel("Descripción"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(descField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(recurringCheck);
        panel.add(Box.createVerticalStrut(5));
        panel.add(daysField);
        panel.add(Box.createVerticalStrut(16));
        panel.add(confirmBtn);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void handleProcess() {
        if (!UIFactory.showConfirmation(this, "¿Ejecutar todas las operaciones pendientes?")) return;
        try {
            mainWindow.getScheduledOperationService().processDueOperations();
            UIFactory.showSuccess(this, "Operaciones procesadas.");
            loadData();
        } catch (Exception ex) {
            UIFactory.showError(this, ex.getMessage());
        }
    }

    private JComboBox<Wallet> buildWalletCombo() {
        JComboBox<Wallet> combo = new JComboBox<>();
        combo.setFont(AppFonts.BODY);
        combo.setBackground(AppColors.ACCENT_PRIMARY);
        combo.setForeground(AppColors.TEXT_PRIMARY);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        ListaSimple<Wallet> wallets = mainWindow.getWalletService()
                .findByOwnerId(currentUser.getId());
        for (Wallet w : wallets) if (w.isActive()) combo.addItem(w);
        return combo;
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