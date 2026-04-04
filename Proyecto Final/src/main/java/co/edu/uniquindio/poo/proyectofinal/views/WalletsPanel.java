package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.enums.WalletType;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class WalletsPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;
    private DefaultTableModel tableModel;
    private JTable table;

    public WalletsPanel(MainWindow mainWindow, User currentUser) {
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

        JLabel title = UIFactory.createTitleLabel("💳 Mis Billeteras");
        JButton addBtn = UIFactory.createPrimaryButton("+ Nueva billetera");
        addBtn.addActionListener(e -> showCreateDialog());

        header.add(title, BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);

        // Tabla
        String[] columns = {"ID", "Nombre", "Tipo", "Saldo", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(AppColors.BACKGROUND_SECONDARY);
        scrollPane.getViewport().setBackground(AppColors.BACKGROUND_SECONDARY);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Panel de botones de acción
        JPanel actions = UIFactory.createPanel();
        actions.setBorder(BorderFactory.createEmptyBorder(12, 28, 24, 28));
        actions.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JButton deactivateBtn = UIFactory.createSecondaryButton("Desactivar");
        JButton refreshBtn    = UIFactory.createSecondaryButton("Actualizar");

        deactivateBtn.addActionListener(e -> handleDeactivate());
        refreshBtn.addActionListener(e -> loadData());

        actions.add(deactivateBtn);
        actions.add(refreshBtn);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        loadData();

        addBtn.addActionListener(e -> {
            showCreateDialog();
        });
    }


    private void loadData() {
        tableModel.setRowCount(0);
        ListaSimple<Wallet> wallets = mainWindow.getWalletService()
                .findByOwnerId(currentUser.getId());

        for (Wallet w : wallets) {
            tableModel.addRow(new Object[]{
                    w.getId(),
                    w.getName(),
                    w.getType().getDisplayName(),
                    String.format("$%.2f", w.getBalance()),
                    w.isActive() ? "Activa" : "Inactiva"
            });
        }
    }

    private void showCreateDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Nueva billetera");
        dialog.setSize(380, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);

        JPanel panel = UIFactory.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField nameField = UIFactory.createTextField("Nombre de la billetera");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JComboBox<WalletType> typeCombo = new JComboBox<>(WalletType.values());
        typeCombo.setFont(AppFonts.BODY);
        typeCombo.setBackground(AppColors.ACCENT_PRIMARY);
        typeCombo.setForeground(AppColors.TEXT_PRIMARY);
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton confirmBtn = UIFactory.createPrimaryButton("Crear");
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            WalletType type = (WalletType) typeCombo.getSelectedItem();
            if (name.isEmpty()) {
                UIFactory.showError(dialog, "El nombre no puede estar vacío.");
                return;
            }
            try {
                mainWindow.getWalletService().create(currentUser.getId(), name, type);
                UIFactory.showSuccess(dialog, "Billetera creada exitosamente.");
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                UIFactory.showError(dialog, ex.getMessage());
            }
        });

        panel.add(UIFactory.createBodyLabel("Nombre"));
        panel.add(Box.createVerticalStrut(6));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(UIFactory.createBodyLabel("Tipo de billetera"));
        panel.add(Box.createVerticalStrut(6));
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(confirmBtn);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void handleDeactivate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            UIFactory.showError(this, "Selecciona una billetera primero.");
            return;
        }
        String walletId = (String) tableModel.getValueAt(row, 0);
        if (UIFactory.showConfirmation(this, "¿Desactivar esta billetera?")) {
            try {
                mainWindow.getWalletService().deactivate(walletId);
                loadData();
            } catch (Exception ex) {
                UIFactory.showError(this, ex.getMessage());
            }
        }
    }

    private void styleTable(JTable table) {
        table.setBackground(AppColors.BACKGROUND_SECONDARY);
        table.setForeground(AppColors.TEXT_PRIMARY);
        table.setFont(AppFonts.BODY);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(AppColors.ACCENT_PRIMARY);
        table.getTableHeader().setForeground(AppColors.TEXT_PRIMARY);
        table.getTableHeader().setFont(AppFonts.BODY_BOLD);
        table.setSelectionBackground(AppColors.ACCENT_HIGHLIGHT);
        table.setSelectionForeground(AppColors.TEXT_PRIMARY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void refresh() {
        loadData();
    }
}