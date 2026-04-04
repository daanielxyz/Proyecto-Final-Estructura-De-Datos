package co.edu.uniquindio.poo.proyectofinal.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIFactory {

    /**
     * Crea un JButton estilizado con el color de acento principal.
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppFonts.BUTTON);
        button.setBackground(AppColors.ACCENT_HIGHLIGHT);
        button.setForeground(AppColors.TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        return button;
    }

    /**
     * Crea un JButton secundario (fondo oscuro, borde de acento).
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppFonts.BUTTON);
        button.setBackground(AppColors.ACCENT_PRIMARY);
        button.setForeground(AppColors.TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        return button;
    }

    /**
     * Crea un JTextField estilizado.
     */
    public static JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(AppFonts.BODY);
        field.setBackground(AppColors.ACCENT_PRIMARY);
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setCaretColor(AppColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.ACCENT_HIGHLIGHT, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    /**
     * Crea un JPasswordField estilizado.
     */
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(AppFonts.BODY);
        field.setBackground(AppColors.ACCENT_PRIMARY);
        field.setForeground(AppColors.TEXT_PRIMARY);
        field.setCaretColor(AppColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.ACCENT_HIGHLIGHT, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return field;
    }

    /**
     * Crea un JLabel de título.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.TITLE);
        label.setForeground(AppColors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Crea un JLabel de subtítulo.
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.SUBTITLE);
        label.setForeground(AppColors.ACCENT_HIGHLIGHT);
        return label;
    }

    /**
     * Crea un JLabel de cuerpo.
     */
    public static JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.BODY);
        label.setForeground(AppColors.TEXT_PRIMARY);
        return label;
    }

    /**
     * Crea un JLabel secundario (texto menos importante).
     */
    public static JLabel createSecondaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppFonts.SMALL);
        label.setForeground(AppColors.TEXT_SECONDARY);
        return label;
    }

    /**
     * Crea un JPanel con fondo primario.
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AppColors.BACKGROUND_PRIMARY);
        return panel;
    }

    /**
     * Crea un JPanel con fondo secundario y padding interno.
     */
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(AppColors.BACKGROUND_SECONDARY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    /**
     * Crea un JSeparator estilizado.
     */
    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(AppColors.ACCENT_PRIMARY);
        sep.setBackground(AppColors.ACCENT_PRIMARY);
        return sep;
    }

    /**
     * Muestra un diálogo de error estilizado.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo de éxito.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo de confirmación y retorna true si el usuario acepta.
     */
    public static boolean showConfirmation(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmar",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
}