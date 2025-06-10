package Applicant;

import AdminSetup.EntryTest.EntryTestRecordManager;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SubmittedFormList_Panel extends JPanel {
    private Applicant userInfo;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private ArrayList<ApplicationFormData> userApplications;

    public SubmittedFormList_Panel(Applicant userInfo) {
        this.userInfo = userInfo;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title and Search
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setToolTipText("Search by Application No., Program or College");

        JLabel title = new JLabel("Submitted Applications Record", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.BLACK);

        searchPanel.add(title, BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        try {
            this.userApplications = new ArrayList<>(ApplicantManager.getApplicationsByUserEmail(userInfo.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            add(new JLabel("Error loading applications.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        if (userApplications == null || userApplications.isEmpty()) {
            add(new JLabel("Ab tak koi application submit nahi hui hai.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        String[] columns = {
                "Application No.",
                "Program",
                "College",
                "Email",
                "Status",           // combined application + fee status
                "Test Schedule",
                "Test Score",
                "Give Test"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only "Give Test" button editable
            }
        };

        table.setRowHeight(40);
        table.setDefaultRenderer(Object.class, new CustomRowRenderer());
        table.getColumn("Give Test").setCellRenderer(new ButtonRenderer());
        table.getColumn("Give Test").setCellEditor(new ButtonEditor(new JCheckBox()));

        populateTable(userApplications);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                model.setRowCount(0);
                for (ApplicationFormData app : userApplications) {
                    if (app.getApplicationId().toLowerCase().contains(query)
                            || app.getSelectedProgram().toLowerCase().contains(query)
                            || app.getSelectedCollege().toLowerCase().contains(query)) {
                        addRow(app);
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void populateTable(ArrayList<ApplicationFormData> applications) {
        for (ApplicationFormData app : applications) {
            addRow(app);
        }
    }

    private void addRow(ApplicationFormData app) {
        EntryTestRecordManager entryTestRecordManager = new EntryTestRecordManager();
        EntryTestRecordManager.EntryTestRecord recordById = entryTestRecordManager.getRecordById(app.getApplicationId());

        String schedule = "Not Scheduled";
        String score = "N/A";
        String actionText = "Unavailable";

        if (recordById != null) {
            LocalDateTime testDate = recordById.getTestDateTime();
            if (testDate != null) {
                schedule = testDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

                if (LocalDate.now().equals(testDate.toLocalDate())) {
                    actionText = "Give Test Now";
                }
            }

            if (recordById.isAttempted()) {
                score = String.valueOf(recordById.getScore());
                actionText = "Completed";
            }
        }

        String combinedStatus = formatCombinedStatus(app.getStatus(), app.getFeeStatus());

        model.addRow(new Object[]{
                app.getApplicationId(),
                app.getSelectedProgram() != null ? app.getSelectedProgram() : "N/A",
                app.getSelectedCollege() != null ? app.getSelectedCollege() : "N/A",
                app.getUsers() != null ? app.getUsers().getEmail() : "N/A",
                combinedStatus,
                schedule,
                score,
                actionText
        });
    }




    private boolean isToday(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.equalsIgnoreCase("null") || dateTimeStr.equalsIgnoreCase("N/A")) return false;
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return LocalDate.now().equals(dateTime.toLocalDate());
        } catch (Exception e) {
            System.err.println("Date parsing error: " + e.getMessage());
            return false;
        }
    }


    private String formatCombinedStatus(Status status, FeeStatus feeStatus) {
        String statusText = switch (status) {
            case SUBMITTED -> "Submitted";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            case TEST_SCHEDULED -> "Test Scheduled";
            case TEST_TAKEN -> "Test Taken";
            case ADMISSION_OFFERED -> "Admission Offered";
            case WAIT_LISTED -> "Wait Listed";
            case ADMISSION_SECURED -> "Admission Secured";
            case ADMISSION_WITHDRAWN -> "Withdrawn";
        };

        String feeText = "";
        if (feeStatus != null) {
            feeText = switch (feeStatus) {
                case PAID -> " (Fee Paid)";
                case UNPAID -> " (Fee Unpaid)";
                default -> "";
            };
        }

        return statusText + feeText;
    }

    private class CustomRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {  // only set background if not selected
                String status = table.getValueAt(row, 4).toString();  // combined status column

                if (status.contains("Approved") || status.contains("ADMISSION_OFFERED")) {
                    c.setBackground(new Color(198, 239, 206)); // Light green (success)
                } else if (status.contains("Rejected") || status.contains("ADMISSION_WITHDRAWN")) {
                    c.setBackground(new Color(255, 199, 206)); // Light red (error)
                } else if (status.contains("Submitted") || status.contains("Test Scheduled")) {
                    c.setBackground(new Color(255, 235, 156)); // Light yellow (pending)
                } else if (status.contains("Test Taken")) {
                    c.setBackground(new Color(209, 222, 255)); // Light blue (completed action)
                } else if (status.contains("WAIT_LISTED")) {
                    c.setBackground(new Color(255, 207, 159)); // Light orange (waiting)
                } else if (status.contains("ADMISSION_SECURED")) {
                    c.setBackground(new Color(183, 225, 205)); // Medium green (confirmed)
                } else {
                    c.setBackground(Color.WHITE); // Default
                }
            }
            return c;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String text = (value == null) ? "" : value.toString();
            setText(text);
            boolean isGiveTest = "Give Test Now".equals(text);
            setEnabled(isGiveTest);
            setBackground(isGiveTest ? Color.GREEN : Color.LIGHT_GRAY);
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }
//
        @Override
        public Object getCellEditorValue() {
            if (isPushed && "Give Test Now".equals(label)) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                ApplicationFormData selectedApp = userApplications.get(modelRow);

                JFrame testFrame = new JFrame("Online Test");
                testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                testFrame.setSize(600, 400);
                testFrame.setLocationRelativeTo(null);

                JPanel testPanel = new JPanel();
                testPanel.add(new JLabel("Your test starts here..."));
                testFrame.setContentPane(testPanel);
                testFrame.setVisible(true);
            }
            isPushed = false;
            return label;
        }

    }
}
