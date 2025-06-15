package AdminSetup.EntryTest;

import AdminSetup.PaymentManager;
import Applicant.ApplicationFormData;
import Applicant.Status;
import Applicant.ApplicantManager;

import javax.swing.*;
import java.awt.*;
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

    public SetTestDatePanel(EntryTestRecordManager recordManager) {
        this.recordManager = recordManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Set Entry Test Date, Time & Subjects");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Applicant ID", "Test Date & Time", "Attempted", "Score", "Subjects", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        loadTestData();

        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(new JCheckBox(), model));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTestData() {
        model.setRowCount(0);
        List<EntryTestRecordManager.EntryTestRecord> existingRecords = recordManager.loadAllRecords();
        List<String> applicantIds = ApplicantManager.getAllApplicantIds();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (String id : applicantIds) {
            if (!PaymentManager.isFeePaid(id)) {
                continue;
            }

            EntryTestRecordManager.EntryTestRecord record = null;
            for (EntryTestRecordManager.EntryTestRecord r : existingRecords) {
                if (r.getApplicantId().equals(id)) {
                    record = r;
                    break;
                }
            }

            if (record == null) {
                record = new EntryTestRecordManager.EntryTestRecord(id, null, false, 0);
            }

            model.addRow(new Object[]{
                    record.getApplicantId(),
                    record.getTestDateTime() != null ? record.getTestDateTime().format(formatter) : "Not Set",
                    record.isAttempted() ? "Yes" : "No",
                    record.getScore(),
                    record.getSubjects() != null ? String.join(", ", record.getSubjects()) : "Not Set",
                    "Set Details"
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No applicants have paid the fee yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private final JButton dateButton;
        private final JButton subjectButton;

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            dateButton = new JButton("Set Date");
            subjectButton = new JButton("Set Subjects");
            add(dateButton);
            add(subjectButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    class ActionCellEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton dateButton;
        private final JButton subjectButton;
        private int editingRow;

        public ActionCellEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            panel = new JPanel(new FlowLayout());
            dateButton = new JButton("Set Date");
            subjectButton = new JButton("Set Subjects");
            panel.add(dateButton);
            panel.add(subjectButton);

            dateButton.addActionListener(e -> {
                editingRow = table.getSelectedRow();
                if (editingRow == -1) return;

                String applicantId = (String) model.getValueAt(editingRow, 0);

                String input = JOptionPane.showInputDialog(null, "Enter Test Date and Time (YYYY-MM-DD HH:MM):");

                if (input == null || input.trim().isEmpty()) return;

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(input.trim(), formatter);

                    EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                    if (record == null) {
                        record = new EntryTestRecordManager.EntryTestRecord(applicantId, dateTime, false, 0);
                    } else {
                        record.setTestDateTime(dateTime);
                    }

                    ApplicationFormData applicationFormData = ApplicantManager.getApplicationByAppId(applicantId);
                    applicationFormData.setTestSchedule(dateTime.toString());

                    recordManager.saveRecord(record);
                    ApplicantManager.updateApplicationStatus(applicantId, Status.TEST_SCHEDULED);

                    JOptionPane.showMessageDialog(null, "Test date/time set for " + applicantId);
                    loadTestData();
                    fireEditingStopped();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid date format. Use YYYY-MM-DD HH:MM",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            subjectButton.addActionListener(e -> {
                editingRow = table.getSelectedRow();
                if (editingRow == -1) return;

                String applicantId = (String) model.getValueAt(editingRow, 0);
                JPanel inputPanel = new JPanel(new GridLayout(0, 1));

                String[] subjectOptions = {"Math", "Add Maths", "English", "Biology"};
                List<JCheckBox> checkBoxes = new ArrayList<>();

                for (String subject : subjectOptions) {
                    JCheckBox cb = new JCheckBox(subject);
                    checkBoxes.add(cb);
                    inputPanel.add(cb);
                }

                int result = JOptionPane.showConfirmDialog(null, inputPanel, "Select Subjects", JOptionPane.OK_CANCEL_OPTION);
                if (result != JOptionPane.OK_OPTION) return;

                ArrayList<String> selectedSubjects = new ArrayList<>();
                for (JCheckBox cb : checkBoxes) {
                    if (cb.isSelected()) selectedSubjects.add(cb.getText());
                }

                if (selectedSubjects.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select at least one subject.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                if (record == null) {
                    record = new EntryTestRecordManager.EntryTestRecord(applicantId, null, false, 0);
                }
                record.setSubjects(selectedSubjects);

                recordManager.saveRecord(record);

                JOptionPane.showMessageDialog(null, "Subjects set for " + applicantId);
                loadTestData();
                fireEditingStopped();
            });
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
