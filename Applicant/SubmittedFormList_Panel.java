package Applicant;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
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
                "Status",
                "Test Schedule",
                "Test Score",
                "Give Test"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
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
                            || app.getSelectedProgram().getName().toLowerCase().contains(query)
                            || app.getSelectedCollege().getName().toLowerCase().contains(query)) {
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
        String schedule = (app.getStatus() == Status.TEST_SCHEDULED || app.getStatus() == Status.TEST_TAKEN)
                ? (app.getTestSchedule() == null || app.getTestSchedule().equals("N/A") ? "Not Scheduled" : app.getTestSchedule())
                : "Not Scheduled";

        String score = (app.getStatus() == Status.TEST_TAKEN && app.getTestScore() != null && !app.getTestScore().equals("N/A"))
                ? app.getTestScore()
                : "N/A";

        String actionText = "Unavailable";
        if ("Give Test Now".equals(actionText) && isToday(app.getTestSchedule())) {
            actionText = "Give Test Now";
        } else if (app.getStatus() == Status.TEST_TAKEN) {
            actionText = "Completed";
        }

        model.addRow(new Object[]{
                app.getApplicationId(),
                app.getSelectedProgram() != null ? app.getSelectedProgram().getName() : "N/A",
                app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A",
                app.getUsers() != null ? app.getUsers().getEmail() : "N/A",
                formatStatus(app.getStatus()),
                schedule,
                score,
                actionText
        });
    }


    private boolean isToday(String dateStr) {
        try {
            return LocalDate.now().toString().equals(dateStr);
        } catch (Exception e) {
            return false;
        }
    }

    private String formatStatus(Status status) {
        return switch (status) {
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
    }

    // Renderer for status coloring
    private class CustomRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = table.getValueAt(row, 4).toString();
            switch (status) {
                case "Approved" -> c.setBackground(new Color(198, 239, 206));
                case "Rejected" -> c.setBackground(new Color(255, 199, 206));
                case "Submitted" -> c.setBackground(new Color(255, 235, 156));
                default -> c.setBackground(Color.WHITE);
            }
            return c;
        }
    }

    // Renderer for button column
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

    // Editor for button column
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

        @Override
        public Object getCellEditorValue() {
            if (isPushed && "Give Test Now".equals(label)) {
                ApplicationFormData selectedApp = userApplications.get(selectedRow);

                // Replace this with your real TestPanel logic
                JFrame testFrame = new JFrame("Online Test");
                testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                testFrame.setSize(600, 400);
                testFrame.setLocationRelativeTo(null);

//                // Simulated TestPanel (Replace with real implementation)
//                JPanel testPanel = new TestPanel(selectedApp); // Make sure this class exists!
//                testFrame.setContentPane(testPanel);
//                testFrame.setVisible(true);
            }
            isPushed = false;
            return label;
        }
    }
}

