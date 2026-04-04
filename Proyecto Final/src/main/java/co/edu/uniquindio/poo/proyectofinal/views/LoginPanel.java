package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final MainWindow mainWindow;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        // Panel central con ancho fijo
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(AppColors.BACKGROUND_PRIMARY);

        JPanel card = UIFactory.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 480));

        // Logo / título
        JLabel logo = UIFactory.createTitleLabel("💳 FinWallet");
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = UIFactory.createSecondaryLabel("Plataforma de billeteras digitales");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Separador
        JSeparator sep = UIFactory.createSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Campos
        JLabel emailLabel = UIFactory.createBodyLabel("Correo electrónico");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField = UIFactory.createTextField("correo@ejemplo.com");
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel passLabel = UIFactory.createBodyLabel("Contraseña");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = UIFactory.createPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Botones
        JButton loginBtn = UIFactory.createPrimaryButton("Iniciar sesión");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton registerBtn = UIFactory.createSecondaryButton("Crear cuenta");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Eventos
        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> mainWindow.navigateTo(MainWindow.PANEL_REGISTER));
        passwordField.addActionListener(e -> handleLogin()); // Enter en password

        // Ensamblar card
        card.add(Box.createVerticalStrut(10));
        card.add(logo);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(24));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(registerBtn);

        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer
        JLabel footer = UIFactory.createSecondaryLabel("© 2025 FinWallet — Proyecto Estructuras de Datos");
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(footer, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            UIFactory.showError(this, "Por favor completa todos los campos.");
            return;
        }

        try {
            User user = mainWindow.getUserService().findByEmail(email);
            if (!mainWindow.getUserService().validatePassword(user.getId(), password)) {
                UIFactory.showError(this, "Contraseña incorrecta.");
                return;
            }
            if (!user.isActive()) {
                UIFactory.showError(this, "Esta cuenta está desactivada.");
                return;
            }
            passwordField.setText("");
            mainWindow.onLoginSuccess(user);

        } catch (Exception ex) {
            UIFactory.showError(this, "Usuario no encontrado.");
        }
    }
}