package co.edu.uniquindio.poo.proyectofinal.views;

import co.edu.uniquindio.poo.proyectofinal.models.Notification;
import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaDoble;
import co.edu.uniquindio.poo.proyectofinal.utils.AppColors;
import co.edu.uniquindio.poo.proyectofinal.utils.AppFonts;
import co.edu.uniquindio.poo.proyectofinal.utils.UIFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Iterator;

public class NotificationsPanel extends JPanel {

    private final MainWindow mainWindow;
    private final User currentUser;
    private DefaultTableModel tableModel;

    public NotificationsPanel(MainWindow mainWindow, User currentUser) {
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

        JLabel title = UIFactory.createTitleLabel("🔔 Notificaciones");
        JButton refreshBtn = UIFactory.createSecondaryButton("Actualizar");
        refreshBtn.addActionListener(e -> loadData());

        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);

        String[] columns = {"Tipo", "Mensaje", "Fecha", "Estado"};
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

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        ListaDoble<Notification> notifications = mainWindow.getNotificationService()
                .getByUserId(currentUser.getId());

        // Recorremos del más reciente al más antiguo usando iteratorReverse
        Iterator<Notification> it = notifications.iteratorReverse();
        while (it.hasNext()) {
            Notification n = it.next();
            tableModel.addRow(new Object[]{
                    n.getType().getDisplayName(),
                    n.getMessage(),
                    n.getCreatedAt().toLocalDate().toString(),
                    n.isRead() ? "Leída" : "Nueva"
            });
        }
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