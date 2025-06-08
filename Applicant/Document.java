//package Applicant;
//import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//import Applicant.Applicant;//
//
//
//public class PaymentPortal_Panel extends JPanel {
//    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
//    private static final Color COLORAZ_WHITE = Color.WHITE;
//    private JTable paymentTable;
//    private DefaultTableModel tableModel;
//    private JComboBox<String> statusFilter;
//    private JTextField searchField;
//    private JLabel applicantIdLabel;
//    private Applicant userInfo;
//    private List<String[]> approvedApplications;
//    private final String[] columns = {"App ID", "Program", "College", "Amount", "Due Date", "Method", "Status", "Ref#", "Action"};
//
//    public PaymentPortal_Panel(Applicant userInfo) {
//        this.userInfo = userInfo;
//        setLayout(new BorderLayout());
//        setBackground(COLORAZ_WHITE);
//
//        // Header panel
//        JPanel topPanel = new JPanel(new BorderLayout());
//        applicantIdLabel = new JLabel("Applicant ID: " + userInfo.getUserID());
//        applicantIdLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        topPanel.add(applicantIdLabel, BorderLayout.WEST);
//
//        // Search and filter panel
//        JPanel searchPanel = new JPanel();
//        searchField = new JTextField(20);
//        JButton searchButton = new JButton("Search");
//        statusFilter = new JComboBox<>(new String[]{"All", "Paid", "Unpaid"});
//        searchPanel.add(new JLabel("Search: "));
//        searchPanel.add(searchField);
//        searchPanel.add(searchButton);
//        searchPanel.add(new JLabel("Payment Status: "));
//        searchPanel.add(statusFilter);
//        topPanel.add(searchPanel, BorderLayout.EAST);
//
//        add(topPanel, BorderLayout.NORTH);
//
//        // Table
//        tableModel = new DefaultTableModel(columns, 0) {
//            public boolean isCellEditable(int row, int column) {
//                return column == 8; // Only Pay button is editable
//            }
//        };
//        paymentTable = new JTable(tableModel);
//        paymentTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
//        paymentTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
//        JScrollPane scrollPane = new JScrollPane(paymentTable);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // Load data
//        loadApprovedApplications();
//        updateTable("", "All");
//
//        // Action listeners
//        searchButton.addActionListener(e -> updateTable(searchField.getText(), (String) statusFilter.getSelectedItem()));
//        statusFilter.addActionListener(e -> updateTable(searchField.getText(), (String) statusFilter.getSelectedItem()));
//    }
//
//    private void loadApprovedApplications() {
//        approvedApplications = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader("all_applications.txt"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 15 && parts[13].equals(userInfo.getEmail()) && parts[14].equals("APPROVED")) {
//                    approvedApplications.add(parts);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateTable(String keyword, String statusFilter) {
//        tableModel.setRowCount(0);
//        for (String[] app : approvedApplications) {
//            String appId = app[0];
//            String program = app[11];
//            String college = app[12];
//            String amount = "10000";
//            String dueDate = "30-June-2025";
//            String method = "Online";
//            String status = getPaymentStatus(appId);
//            String ref = status.equals("Paid") ? generateRef(appId) : "";
//
//            boolean matchesSearch = keyword.isEmpty() || appId.contains(keyword) || program.toLowerCase().contains(keyword.toLowerCase()) || college.toLowerCase().contains(keyword.toLowerCase());
//            boolean matchesFilter = statusFilter.equals("All") || status.equalsIgnoreCase(statusFilter);
//
//            if (matchesSearch && matchesFilter) {
//                tableModel.addRow(new Object[]{appId, program, college, amount, dueDate, method, status, ref, "Pay Now"});
//            }
//        }
//    }
//
//    private String getPaymentStatus(String appId) {
//        try (BufferedReader br = new BufferedReader(new FileReader("payment_status.txt"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts[0].equals(appId)) return parts[1];
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "Unpaid";
//    }
//
//    private void updatePaymentStatus(String appId) {
//        File inputFile = new File("payment_status.txt");
//        File tempFile = new File("temp_payment_status.txt");
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
//
//            String line;
//            boolean found = false;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts[0].equals(appId)) {
//                    writer.write(appId + ",Paid\n");
//                    found = true;
//                } else {
//                    writer.write(line + "\n");
//                }
//            }
//            if (!found) writer.write(appId + ",Paid\n");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        inputFile.delete();
//        tempFile.renameTo(inputFile);
//    }
//
//    private String generateRef(String appId) {
//        return "REF-" + appId.substring(appId.length() - 4) + "-PAID";
//    }
//
//    class ButtonRenderer extends JButton implements TableCellRenderer {
//        public ButtonRenderer() {
//            setText("Pay Now");
//        }
//
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                                                       boolean isSelected, boolean hasFocus, int row, int column) {
//            setText((value == null) ? "Pay Now" : value.toString());
//            return this;
//        }
//    }
//
//    class ButtonEditor extends DefaultCellEditor {
//        private JButton button;
//        private String appId;
//        private boolean isPushed;
//
//        public ButtonEditor(JCheckBox checkBox) {
//            super(checkBox);
//            button = new JButton();
//            button.setOpaque(true);
//            button.addActionListener(e -> fireEditingStopped());
//        }
//
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            appId = table.getValueAt(row, 0).toString();
//            button.setText("Pay Now");
//            isPushed = true;
//            return button;
//        }
//
//        public Object getCellEditorValue() {
//            if (isPushed) {
//                updatePaymentStatus(appId);
//                JOptionPane.showMessageDialog(button, "Payment successful for: " + appId);
//                updateTable(searchField.getText(), (String) statusFilter.getSelectedItem());
//            }
//            isPushed = false;
//            return "Pay Now";
//        }
//    }
//}
