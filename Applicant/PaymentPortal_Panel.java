package Applicant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class PaymentPortal_Panel extends JPanel {
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;

    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> statusFilter;
    private JTextField searchField;
    private JLabel applicantEmailLabel;

    private Applicant userInfo;
    private List<String[]> allApplications;

    private final String APPLICATION_FILE = "all_applications.txt";

    private final String[] TABLE_COLUMNS = {"App ID", "Program", "College", "Status"};

    public PaymentPortal_Panel(Applicant userInfo) {
        this.userInfo = userInfo;
        setLayout(new BorderLayout());
        setBackground(COLORAZ_SAGE);

        // Top panel with search and filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(COLORAZ_SAGE);

        topPanel.add(new JLabel("Search (App ID / Program): "));
        searchField = new JTextField(15);
        topPanel.add(searchField);

        topPanel.add(new JLabel("Status Filter: "));
        statusFilter = new JComboBox<>(new String[]{"All", "Payment Cleared", "Unpaid"});
        topPanel.add(statusFilter);

        JButton refreshBtn = new JButton("Refresh");
        topPanel.add(refreshBtn);

        JButton markPaidBtn = new JButton("Mark Payment Cleared");
        topPanel.add(markPaidBtn);

        applicantEmailLabel = new JLabel("Applicant: " + userInfo.getEmail());
        applicantEmailLabel.setForeground(Color.DARK_GRAY);
        topPanel.add(applicantEmailLabel);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no direct editing
            }
        };
        paymentTable = new JTable(tableModel);
        paymentTable.setFillsViewportHeight(true);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paymentTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load data and apply filters on init
        loadApplicationsFromFile();
        applyFilters();

        // Add listeners for filter/search
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                applyFilters();
            }
        });

        statusFilter.addActionListener(e -> applyFilters());

        refreshBtn.addActionListener(e -> {
            loadApplicationsFromFile();
            applyFilters();
        });

        markPaidBtn.addActionListener(e -> markSelectedAsPaid());
    }

    private void loadApplicationsFromFile() {
        allApplications = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 17) {
                    // Validate status field
                    try {
                        Status.valueOf(parts[14]);
                    } catch (IllegalArgumentException ex) {
                        parts[14] = Status.APPROVED.name();
                    }
                    allApplications.add(parts);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading applications file: " + e.getMessage());
        }
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim().toLowerCase();
        String statusSelected = (String) statusFilter.getSelectedItem();

        tableModel.setRowCount(0);

        for (String[] app : allApplications) {
            String email = app[13];
            if (!email.equalsIgnoreCase(userInfo.getEmail())) continue;

            Status status = Status.valueOf(app[14]);
            if (!(status == Status.APPROVED || status == Status.PAYMENT_CLEARED)) continue;

            // Status filter logic
            if (statusSelected != null && !statusSelected.equals("All")) {
                if (statusSelected.equals("Payment Cleared") && status != Status.PAYMENT_CLEARED) continue;
                if (statusSelected.equals("Unpaid") && status == Status.PAYMENT_CLEARED) continue;
            }

            if (!searchText.isEmpty()) {
                String appId = app[0] != null ? app[0].toLowerCase() : "";
                String program = app[11] != null ? app[11].toLowerCase() : "";
                if (!appId.contains(searchText) && !program.contains(searchText)) continue;
            }

            tableModel.addRow(new Object[]{
                    app[0],   // App ID
                    app[11],  // Program
                    app[12],  // College
                    app[14]   // Status
            });
        }
    }

    private void markSelectedAsPaid() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application to mark as payment cleared.");
            return;
        }

        String appId = (String) tableModel.getValueAt(selectedRow, 0);
        Status currentStatus = Status.valueOf((String) tableModel.getValueAt(selectedRow, 3));

        if (currentStatus == Status.PAYMENT_CLEARED) {
            JOptionPane.showMessageDialog(this, "This application is already marked as PAYMENT_CLEARED.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark application ID " + appId + " as PAYMENT_CLEARED?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean updated = false;
        for (int i = 0; i < allApplications.size(); i++) {
            String[] app = allApplications.get(i);
            if (app[0].equals(appId)) {
                app[14] = Status.PAYMENT_CLEARED.name();  // update status field
                allApplications.set(i, app);
                updated = true;
                break;
            }
        }

        if (!updated) {
            JOptionPane.showMessageDialog(this, "Application not found or cannot update.");
            return;
        }

        // Write back all apps to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(APPLICATION_FILE, false))) {
            for (String[] app : allApplications) {
                bw.write(String.join(",", app));
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating applications file: " + e.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this, "Application marked as PAYMENT_CLEARED successfully.");
        applyFilters();
    }
}
