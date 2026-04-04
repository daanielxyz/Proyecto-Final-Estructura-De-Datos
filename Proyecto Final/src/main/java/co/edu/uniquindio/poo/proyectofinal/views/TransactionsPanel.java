package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.Wallet;
import co.edu.uniquindio.poo.proyectofinal.models.Transaction;
import co.edu.uniquindio.poo.proyectofinal.models.enums.TransactionType;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import java.awt.*;

public class TransactionsPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;

    public TransactionsPanel(MainWindow mainWindow, User currentUser) {
        this.mainWindow  = mainWindow;
        this.currentUser = currentUser;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        // Header
        JPanel header = UIFactory.createPanel();
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 16, 28));
        header.setLayout(new BorderLayout());
        header.add(UIFactory.createTitleLabel("💸 Operaciones"), BorderLayout.WEST);

        // Panel de tarjetas de operaciones
        JPanel cardsPanel = UIFactory.createPanel();
        cardsPanel.setLayout(new GridLayout(2, 2, 16, 16));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(8, 28, 28, 28));

        cardsPanel.add(buildOperationCard(
                "Recargar", "Agregar saldo a una billetera", "💰",
                e -> showRechargeDialog()));
        cardsPanel.add(buildOperationCard(
                "Retirar", "Retirar saldo de una billetera", "🏧",
                e -> showWithdrawDialog()));
        cardsPanel.add(buildOperationCard(
                "Transferencia interna", "Entre tus propias billeteras", "🔄",
                e -> showInternalTransferDialog()));
        cardsPanel.add(buildOperationCard(
                "Transferencia externa", "A billetera de otro usuario", "📤",
                e -> showExternalTransferDialog()));

        // Botón de reversión
        JPanel bottomPanel = UIFactory.createPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 28, 24, 28));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton reverseBtn = UIFactory.createSecondaryButton("↩ Revertir última operación");
        reverseBtn.addActionListener(e -> handleReverse());
        bottomPanel.add(reverseBtn);

        add(header, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel buildOperationCard(String title, String description,
                                      String icon, java.awt.event.ActionListener action) {
        JPanel card = UIFactory.createCard();
        card.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel titleLabel = UIFactory.createSubtitleLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = UIFactory.createSecondaryLabel(description);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btn = UIFactory.createPrimaryButton("Realizar");
        btn.addActionListener(action);

        JPanel textPanel = UIFactory.createCard();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(iconLabel);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);
        textPanel.add(Box.createVerticalStrut(12));
        textPanel.add(btn);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    // -------------------------------------------------------------------------
    // Diálogos de operaciones
    // -------------------------------------------------------------------------

    private void showRechargeDialog() {
        JDialog dialog = buildDialog("Recargar billetera", 360, 280);
        JPanel panel = buildDialogPanel(dialog);

        JComboBox<Wallet> walletCombo = buildWalletCombo();
        JTextField amountField = UIFactory.createTextField("Monto");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField descField = UIFactory.createTextField("Descripción");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton confirmBtn = UIFactory.createPrimaryButton("Recargar");
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmBtn.addActionListener(e -> {
            try {
                Wallet selected = (Wallet) walletCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                Transaction t = mainWindow.getTransactionService().recharge(
                        currentUser.getId(), selected.getId(),
                        amount, descField.getText().trim());
                UIFactory.showSuccess(dialog, "Recarga exitosa. Puntos ganados: " + t.getPointsGenerated());
                dialog.dispose();
            } catch (NumberFormatException ex) {
                UIFactory.showError(dialog, "Ingresa un monto válido.");
            } catch (Exception ex) {
                UIFactory.showError(dialog, ex.getMessage());
            }
        });

        assembleDialogFields(panel, confirmBtn,
                "Billetera", walletCombo,
                "Monto", amountField,
                "Descripción", descField);
        dialog.setVisible(true);
    }

    private void showWithdrawDialog() {
        JDialog dialog = buildDialog("Retirar saldo", 360, 280);
        JPanel panel = buildDialogPanel(dialog);

        JComboBox<Wallet> walletCombo = buildWalletCombo();
        JTextField amountField = UIFactory.createTextField("Monto");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField descField = UIFactory.createTextField("Descripción");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton confirmBtn = UIFactory.createPrimaryButton("Retirar");
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmBtn.addActionListener(e -> {
            try {
                Wallet selected = (Wallet) walletCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                mainWindow.getTransactionService().withdraw(
                        currentUser.getId(), selected.getId(),
                        amount, descField.getText().trim());
                UIFactory.showSuccess(dialog, "Retiro exitoso.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                UIFactory.showError(dialog, "Ingresa un monto válido.");
            } catch (Exception ex) {
                UIFactory.showError(dialog, ex.getMessage());
            }
        });

        assembleDialogFields(panel, confirmBtn,
                "Billetera", walletCombo,
                "Monto", amountField,
                "Descripción", descField);
        dialog.setVisible(true);
    }

    private void showInternalTransferDialog() {
        JDialog dialog = buildDialog("Transferencia interna", 380, 320);
        JPanel panel = buildDialogPanel(dialog);

        JComboBox<Wallet> sourceCombo = buildWalletCombo();
        JComboBox<Wallet> destCombo   = buildWalletCombo();
        JTextField amountField = UIFactory.createTextField("Monto");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField descField = UIFactory.createTextField("Descripción");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton confirmBtn = UIFactory.createPrimaryButton("Transferir");
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmBtn.addActionListener(e -> {
            try {
                Wallet source = (Wallet) sourceCombo.getSelectedItem();
                Wallet dest   = (Wallet) destCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                mainWindow.getTransactionService().transferInternal(
                        currentUser.getId(), source.getId(), dest.getId(),
                        amount, descField.getText().trim());
                UIFactory.showSuccess(dialog, "Transferencia exitosa.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                UIFactory.showError(dialog, "Ingresa un monto válido.");
            } catch (Exception ex) {
                UIFactory.showError(dialog, ex.getMessage());
            }
        });

        assembleDialogFields(panel, confirmBtn,
                "Origen", sourceCombo,
                "Destino", destCombo,
                "Monto", amountField,
                "Descripción", descField);
        dialog.setVisible(true);
    }

    private void showExternalTransferDialog() {
        JDialog dialog = buildDialog("Transferencia externa", 380, 320);
        JPanel panel = buildDialogPanel(dialog);

        JComboBox<Wallet> sourceCombo = buildWalletCombo();
        JTextField destWalletField = UIFactory.createTextField("ID de billetera destino");
        destWalletField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField amountField = UIFactory.createTextField("Monto");
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JTextField descField = UIFactory.createTextField("Descripción");
        descField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton confirmBtn = UIFactory.createPrimaryButton("Transferir");
        confirmBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmBtn.addActionListener(e -> {
            try {
                Wallet source = (Wallet) sourceCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText().trim());
                mainWindow.getTransactionService().transferExternal(
                        currentUser.getId(), source.getId(),
                        destWalletField.getText().trim(),
                        amount, descField.getText().trim());
                UIFactory.showSuccess(dialog, "Transferencia exitosa.");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                UIFactory.showError(dialog, "Ingresa un monto válido.");
            } catch (Exception ex) {
                UIFactory.showError(dialog, ex.getMessage());
            }
        });

        assembleDialogFields(panel, confirmBtn,
                "Billetera origen", sourceCombo,
                "ID billetera destino", destWalletField,
                "Monto", amountField,
                "Descripción", descField);
        dialog.setVisible(true);
    }

    private void handleReverse() {
        if (!UIFactory.showConfirmation(this, "¿Revertir la última operación realizada?")) return;
        try {
            Transaction t = mainWindow.getTransactionService().reverse(currentUser.getId());
            UIFactory.showSuccess(this, "Operación revertida: " + t.getId());
        } catch (Exception ex) {
            UIFactory.showError(this, ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Helpers de UI
    // -------------------------------------------------------------------------

    private JComboBox<Wallet> buildWalletCombo() {
        JComboBox<Wallet> combo = new JComboBox<>();
        combo.setFont(AppFonts.BODY);
        combo.setBackground(AppColors.ACCENT_PRIMARY);
        combo.setForeground(AppColors.TEXT_PRIMARY);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        ListaSimple<Wallet> wallets = mainWindow.getWalletService()
                .findByOwnerId(currentUser.getId());
        for (Wallet w : wallets) {
            if (w.isActive()) combo.addItem(w);
        }
        return combo;
    }

    private JDialog buildDialog(String title, int width, int height) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        return dialog;
    }

    private JPanel buildDialogPanel(JDialog dialog) {
        JPanel panel = UIFactory.createCard();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        dialog.add(panel);
        return panel;
    }

    private void assembleDialogFields(JPanel panel, JButton confirmBtn, Object... labelsAndFields) {
        for (int i = 0; i < labelsAndFields.length; i += 2) {
            String label = (String) labelsAndFields[i];
            JComponent field = (JComponent) labelsAndFields[i + 1];
            panel.add(UIFactory.createBodyLabel(label));
            panel.add(Box.createVerticalStrut(5));
            panel.add(field);
            panel.add(Box.createVerticalStrut(10));
        }
        panel.add(Box.createVerticalStrut(6));
        panel.add(confirmBtn);
    }
}