package Applicant;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentPortal_Panel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private static final String APPLICATION_FILE = "all_applications.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Applicant userInfo;

    public PaymentPortal_Panel(Applicant userInfo) {
        this.userInfo = userInfo;
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Applicant ID: " + userInfo.getUserID()));
        infoPanel.add(new JLabel(" | Email: " + userInfo.getEmail()));
        add(infoPanel, BorderLayout.NORTH);

        String[] columnNames = {"Application Form", "Program", "College", "Due Date", "Fee Status", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField filterField = new JTextField(20);
        filterField.setToolTipText("Filter by any column...");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(filterField);
        add(filterPanel, BorderLayout.SOUTH);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                search(filterField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                search(filterField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                search(filterField.getText());
            }

            private void search(String text) {
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        loadApplications();
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        boolean foundAny = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 18) continue;

                String appId = parts[0];
                String email = parts[13];
                String status = parts[14].toUpperCase();     // Application status
                String feeStatus = parts[17].toUpperCase();  // Fee status

                if (!userInfo.getEmail().equalsIgnoreCase(email)) continue;

                // Show if application was approved, scheduled, or payment cleared
                if (!(status.equals("APPROVED") || status.equals("TEST_SCHEDULED") || status.equals("PAYMENT_CLEARED"))) {
                    continue;
                }

                foundAny = true;

                String program = parts[11];
                String college = parts[12];
                String dueDate = DATE_FORMAT.format(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
                String feeStatusDisplay = feeStatus.equals("PAID") ? "Paid" : "Unpaid";

                tableModel.addRow(new Object[]{appId, program, college, dueDate, feeStatusDisplay, "Pay Now"});
            }

            if (!foundAny) {
                JOptionPane.showMessageDialog(this, "No forms found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading applications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAsPaid(String appId) {
        File originalFile = new File(APPLICATION_FILE);
        File tempFile = new File("all_applications_temp.txt");

        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 18) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                if (parts[0].equals(appId)) {
                    // ✅ ONLY update fee status
                    // DO NOT touch parts[14] — this is the application status (e.g. APPROVED)

                    parts[17] = "PAID";        // feeStatus
                    parts[15] = "N/A";         // extra fee fields, optional
                    if (parts.length > 16) parts[16] = "N/A";

                    updated = true;
                    line = String.join(",", parts);
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating payment status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
            JOptionPane.showMessageDialog(this, "Error finalizing update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (updated) {
            JOptionPane.showMessageDialog(this, "Payment successful! Status updated.");
            loadApplications();
        } else {
            JOptionPane.showMessageDialog(this, "Application not found or already paid.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
//

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD, 12));
            setForeground(Color.BLACK);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String feeStatus = table.getValueAt(row, 4).toString();
            if (feeStatus.equalsIgnoreCase("Paid")) {
                setText("Paid");
                setEnabled(false);
                setBackground(Color.GRAY);
            } else {
                setText("Pay Now");
                setEnabled(true);
                setBackground(new Color(255, 165, 0));
            }
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String appId;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setForeground(Color.BLACK);
            button.setBackground(new Color(255, 165, 0));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            appId = table.getValueAt(row, 0).toString();
            String feeStatus = table.getValueAt(row, 4).toString();

            if (feeStatus.equalsIgnoreCase("Paid")) {
                button.setText("Paid");
                button.setEnabled(false);
                button.setBackground(Color.GRAY);
            } else {
                button.setText("Pay Now");
                button.setEnabled(true);
                button.setBackground(new Color(255, 165, 0));
            }

            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                markAsPaid(appId);
            }
            isPushed = false;
            return "Pay Now";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
