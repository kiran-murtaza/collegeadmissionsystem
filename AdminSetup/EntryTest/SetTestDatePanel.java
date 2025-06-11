package AdminSetup.EntryTest;

import AdminSetup.PaymentManager;
import Applicant.ApplicationFormData;
import Applicant.Status;
import Applicant.ApplicantManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class SetTestDatePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private EntryTestRecordManager recordManager;
    private JButton batchSetButton, batchSetSubjectsButton;
    private JCheckBox mathCheckBox, engCheckBox, advancedMathCheckBox, biologyCheckBox;
    private List<String> allSubjects = List.of("Math", "English", "Advanced Math", "Biology");

    public SetTestDatePanel(EntryTestRecordManager recordManager) {
        this.recordManager = recordManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Set Entry Test Date & Time");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title, BorderLayout.NORTH);

        // Control Panel for batch operations
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Batch Date/Time Selection
        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        batchSetButton = new JButton("Batch Set Date/Time");
        batchSetButton.addActionListener(this::handleBatchDateSet);
        dateTimePanel.add(batchSetButton);

        // Batch Subject Selection
        JPanel subjectBatchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        batchSetSubjectsButton = new JButton("Batch Set Subjects");
        batchSetSubjectsButton.addActionListener(this::handleBatchSubjectSet);
        subjectBatchPanel.add(batchSetSubjectsButton);

        // Subject Selection
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.setBorder(BorderFactory.createTitledBorder("Test Subjects"));
        mathCheckBox = new JCheckBox("Math");
        engCheckBox = new JCheckBox("English");
        advancedMathCheckBox = new JCheckBox("Advanced Math");
        biologyCheckBox = new JCheckBox("Biology");
        subjectPanel.add(mathCheckBox);
        subjectPanel.add(engCheckBox);
        subjectPanel.add(advancedMathCheckBox);
        subjectPanel.add(biologyCheckBox);

        controlPanel.add(dateTimePanel);
        controlPanel.add(subjectBatchPanel);
        controlPanel.add(subjectPanel);

        titlePanel.add(controlPanel, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Select", "Applicant ID", "Test Date & Time", "Attempted", "Score", "Subjects", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : super.getColumnClass(column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 6;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        loadTestData();

        // Add checkbox renderer/editor for first column
        table.getColumnModel().getColumn(0).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumnModel().getColumn(0).setCellEditor(table.getDefaultEditor(Boolean.class));

        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(new JCheckBox(), model));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void handleBatchDateSet(ActionEvent e) {
        String dateTimeStr = JOptionPane.showInputDialog(
                this,
                "Enter Test Date and Time (YYYY-MM-DD HH:MM):",
                "Batch Set Test Date & Time",
                JOptionPane.PLAIN_MESSAGE);

        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr.trim(), formatter);

            for (int row = 0; row < model.getRowCount(); row++) {
                if ((Boolean) model.getValueAt(row, 0)) {
                    String applicantId = (String) model.getValueAt(row, 1);
                    EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                    if (record == null) {
                        record = new EntryTestRecordManager.EntryTestRecord(applicantId, dateTime, false, 0);
                    } else {
                        record.setTestDateTime(dateTime);
                    }
                    recordManager.saveRecord(record);
                    ApplicantManager.updateApplicationStatus(applicantId, Status.TEST_SCHEDULED);
                    model.setValueAt(dateTime.format(formatter), row, 2);
                }
            }
            JOptionPane.showMessageDialog(this, "Batch date update completed!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use YYYY-MM-DD HH:MM",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBatchSubjectSet(ActionEvent e) {
        List<String> selectedSubjects = getSelectedSubjects();
        if (selectedSubjects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one subject",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int row = 0; row < model.getRowCount(); row++) {
            if ((Boolean) model.getValueAt(row, 0)) {
                String applicantId = (String) model.getValueAt(row, 1);
                EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                if (record == null) {
                    record = new EntryTestRecordManager.EntryTestRecord(applicantId, null, false, 0);
                }
                record.setSubjects(new ArrayList<>(selectedSubjects));
                recordManager.saveRecord(record);
                model.setValueAt(String.join(", ", selectedSubjects), row, 5);
            }
        }
        JOptionPane.showMessageDialog(this, "Batch subject update completed!");
    }

    private List<String> getSelectedSubjects() {
        List<String> subjects = new ArrayList<>();
        if (mathCheckBox.isSelected()) subjects.add("Math");
        if (engCheckBox.isSelected()) subjects.add("English");
        if (advancedMathCheckBox.isSelected()) subjects.add("Advanced Math");
        if (biologyCheckBox.isSelected()) subjects.add("Biology");
        return subjects;
    }

    private void loadTestData() {
        model.setRowCount(0);
        List<EntryTestRecordManager.EntryTestRecord> existingRecords = recordManager.loadAllRecords();
        List<String> applicantIds = ApplicantManager.getAllApplicantIds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (String id : applicantIds) {
            if (!PaymentManager.isFeePaid(id)) continue;

            EntryTestRecordManager.EntryTestRecord record = existingRecords.stream()
                    .filter(r -> r.getApplicantId().equals(id))
                    .findFirst()
                    .orElse(new EntryTestRecordManager.EntryTestRecord(id, null, false, 0));

            model.addRow(new Object[]{
                    false,
                    record.getApplicantId(),
                    record.getTestDateTime() != null ? record.getTestDateTime().format(formatter) : "Not Set",
                    record.isAttempted() ? "Yes" : "No",
                    record.getScore(),
                    record.getSubjects() != null ? String.join(", ", record.getSubjects()) : "",
                    "Edit"
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No applicants have paid the fee yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private final JButton button;

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            button = new JButton("Edit");
            add(button);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class ActionCellEditor extends DefaultCellEditor {
        private final JButton button;
        private final JPanel panel;
        private int editingRow;

        public ActionCellEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            panel = new JPanel(new FlowLayout());
            button = new JButton("Edit");
            panel.add(button);

            button.addActionListener(e -> {
                editingRow = table.getSelectedRow();
                if (editingRow == -1) return;

                String applicantId = (String) model.getValueAt(editingRow, 1);
                showEditDialog(applicantId, editingRow);
                fireEditingStopped();
            });
        }

        private void showEditDialog(String applicantId, int row) {
            JDialog dialog = new JDialog();
            dialog.setLayout(new BorderLayout());
            dialog.setTitle("Edit Test Details for " + applicantId);

            JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Date/Time Panel
            JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel dateLabel = new JLabel("Test Date/Time:");
            JTextField dateField = new JTextField(20);
            datePanel.add(dateLabel);
            datePanel.add(dateField);

            // Subject Panel
            JPanel subjectPanel = new JPanel(new GridLayout(0, 1));
            subjectPanel.setBorder(BorderFactory.createTitledBorder("Test Subjects"));
            JCheckBox math = new JCheckBox("Math");
            JCheckBox eng = new JCheckBox("English");
            JCheckBox advancedMath = new JCheckBox("Advanced Math");
            JCheckBox biology = new JCheckBox("Biology");

            // Preselect current subjects
            String currentSubjects = (String) model.getValueAt(row, 5);
            if (currentSubjects != null) {
                math.setSelected(currentSubjects.contains("Math"));
                eng.setSelected(currentSubjects.contains("English"));
                advancedMath.setSelected(currentSubjects.contains("Advanced Math"));
                biology.setSelected(currentSubjects.contains("Biology"));
            }
            subjectPanel.add(math);
            subjectPanel.add(eng);
            subjectPanel.add(advancedMath);
            subjectPanel.add(biology);

            // Button Panel
            JPanel buttonPanel = new JPanel();
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(ev -> {
                try {
                    LocalDateTime dateTime = null;
                    if (!dateField.getText().trim().isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        dateTime = LocalDateTime.parse(dateField.getText().trim(), formatter);
                    }

                    List<String> subjects = new ArrayList<>();
                    if (math.isSelected()) subjects.add("Math");
                    if (eng.isSelected()) subjects.add("English");
                    if (advancedMath.isSelected()) subjects.add("Advanced Math");
                    if (biology.isSelected()) subjects.add("Biology");

                    EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                    if (record == null) {
                        record = new EntryTestRecordManager.EntryTestRecord(applicantId, dateTime, false, 0);
                    } else {
                        if (dateTime != null) {
                            record.setTestDateTime(dateTime);
                        }
                    }
                    record.setSubjects(subjects);
                    recordManager.saveRecord(record);

                    if (dateTime != null) {
                        model.setValueAt(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), row, 2);
                    }
                    model.setValueAt(String.join(", ", subjects), row, 5);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Invalid date format. Use YYYY-MM-DD HH:MM or leave blank",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(saveButton);

            contentPanel.add(datePanel);
            contentPanel.add(subjectPanel);
            contentPanel.add(buttonPanel);

            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            editingRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}