package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    private final MainWindow mainWindow;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setBackground(AppColors.BACKGROUND_PRIMARY);
        buildUI();
    }

    private void buildUI() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(AppColors.BACKGROUND_PRIMARY);

        JPanel card = UIFactory.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(440, 560));

        JLabel title = UIFactory.createTitleLabel("Crear cuenta");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = UIFactory.createSecondaryLabel("Ingresa tus datos para registrarte");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = UIFactory.createSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Campos
        nameField            = UIFactory.createTextField("Nombre completo");
        emailField           = UIFactory.createTextField("correo@ejemplo.com");
        phoneField           = UIFactory.createTextField("Teléfono");
        passwordField        = UIFactory.createPasswordField();
        confirmPasswordField = UIFactory.createPasswordField();

        for (JComponent field : new JComponent[]{
                nameField, emailField, phoneField, passwordField, confirmPasswordField}) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        }

        JButton registerBtn = UIFactory.createPrimaryButton("Registrarme");
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton backBtn = UIFactory.createSecondaryButton("Volver al login");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        registerBtn.addActionListener(e -> handleRegister());
        backBtn.addActionListener(e -> mainWindow.navigateTo(MainWindow.PANEL_LOGIN));

        // Ensamblar
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));
        card.add(UIFactory.createBodyLabel("Nombre completo"));
        card.add(Box.createVerticalStrut(5));
        card.add(nameField);
        card.add(Box.createVerticalStrut(10));
        card.add(UIFactory.createBodyLabel("Correo electrónico"));
        card.add(Box.createVerticalStrut(5));
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));
        card.add(UIFactory.createBodyLabel("Teléfono"));
        card.add(Box.createVerticalStrut(5));
        card.add(phoneField);
        card.add(Box.createVerticalStrut(10));
        card.add(UIFactory.createBodyLabel("Contraseña"));
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));
        card.add(UIFactory.createBodyLabel("Confirmar contraseña"));
        card.add(Box.createVerticalStrut(5));
        card.add(confirmPasswordField);
        card.add(Box.createVerticalStrut(22));
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(backBtn);

        centerWrapper.add(card);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private void handleRegister() {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            UIFactory.showError(this, "Nombre, email y contraseña son obligatorios.");
            return;
        }
        if (!password.equals(confirm)) {
            UIFactory.showError(this, "Las contraseñas no coinciden.");
            return;
        }

        try {
            User user = mainWindow.getUserService().register(name, email, phone, password);
            UIFactory.showSuccess(this, "Cuenta creada exitosamente. ¡Bienvenido, " + user.getFullName() + "!");
            mainWindow.navigateTo(MainWindow.PANEL_LOGIN);
        } catch (Exception ex) {
            UIFactory.showError(this, ex.getMessage());
        }
    }
}