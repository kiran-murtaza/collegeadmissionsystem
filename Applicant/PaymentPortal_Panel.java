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

        // Info panel at top with user details
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Applicant ID: " + userInfo.getUserID()));
        infoPanel.add(new JLabel(" | Email: " + userInfo.getEmail()));
        add(infoPanel, BorderLayout.NORTH);

        // Define columns
        String[] columnNames = {"Application Form", "Program", "College", "Due Date", "Fee Status", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only "Action" column (Pay Now button) is editable
                return column == 5;
            }
        };

        // Setup table
        table = new JTable(tableModel);
        table.setRowHeight(30);

        // Custom renderer for Action column to display buttons properly
        table.getColumn("Action").setCellRenderer(new ButtonRenderer());
        table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Enable sorting
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Search filter panel at bottom
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField filterField = new JTextField(20);
        filterField.setToolTipText("Filter by any column...");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(filterField);
        add(filterPanel, BorderLayout.SOUTH);

        // Filter listener for live search
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
        tableModel.setRowCount(0); // clear existing data
        boolean foundAny = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 16) continue;

                String appId = parts[0];
                String email = parts[13];
                String status = parts[14].toUpperCase();

                if (!userInfo.getEmail().equalsIgnoreCase(email)) {
                    continue; // skip non-user records
                }

                // Show only APPROVED, SUBMITTED, PAYMENT_CLEARED applications
                if (!(status.equals("APPROVED") || status.equals("PAYMENT_CLEARED"))) {
                    continue;
                }

                foundAny = true;

                String program = parts[11];
                String college = parts[12];

                // Fee status for display
                String feeStatusDisplay = status.equals("PAYMENT_CLEARED") ? "Paid" : "Unpaid";

                // For simplicity, due date = today + 7 days
                String dueDate = DATE_FORMAT.format(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));

                // Add row: Action column will hold button label (button editor will handle actual button)
                tableModel.addRow(new Object[]{appId, program, college, dueDate, feeStatusDisplay, "Pay Now"});
            }

            if (!foundAny) {
                JOptionPane.showMessageDialog(this, "No forms found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading applications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Updates the payment status and refreshes the table
    private void markAsPaid(String appId) {
        File originalFile = new File(APPLICATION_FILE);
        File tempFile = new File("all_applications_temp.txt");

        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 16) {
                    writer.write(line);
                    writer.newLine();
                    continue;
                }

                if (parts[0].equals(appId)) {
                    parts[14] = "PAYMENT_CLEARED"; // status
                    parts[15] = "N/A"; // test schedule reset
                    if (parts.length > 16) {
                        parts[16] = "N/A"; // test score reset if exists
                    }
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

        // Replace original file with updated temp file
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

    // Renderer for button cells
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD, 12));
            setForeground(Color.BLACK);
            setBackground(new Color(255, 165, 0)); // orange
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

    // Editor for button cells (handles click event)
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
                // On button click, mark as paid
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
