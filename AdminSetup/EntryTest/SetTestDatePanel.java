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
import java.util.List;


public class SetTestDatePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private EntryTestRecordManager recordManager;

    public SetTestDatePanel(EntryTestRecordManager recordManager) {
        this.recordManager = recordManager;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Set Entry Test Date & Time");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        String[] columns = {"Applicant ID", "Test Date & Time", "Attempted", "Score", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
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
        List<String> applicantIds = ApplicantManager.getAllApplicantIds(); // All applicants
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
                    "Set Date"
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
            button = new JButton("Set Date");
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
            button = new JButton("Set Date");
            panel.add(button);

            button.addActionListener(e -> {
                editingRow = table.getSelectedRow();
                if (editingRow == -1) return;

                String applicantId = (String) model.getValueAt(editingRow, 0);
                String dateTimeStr = JOptionPane.showInputDialog(
                        null,
                        "Enter Test Date and Time (YYYY-MM-DD HH:MM):",
                        "Set Test Date & Time",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
                    return;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr.trim(), formatter);

                    // 1. Update test record
                    EntryTestRecordManager.EntryTestRecord record = recordManager.getRecordById(applicantId);
                    if (record == null) {
                        record = new EntryTestRecordManager.EntryTestRecord(applicantId, dateTime, false, 0);
                    } else {
                        record.setTestDateTime(dateTime);
                    }
                    recordManager.saveRecord(record);

                    // 2. Update ApplicationFormData

                    // 3. Update status
                    ApplicantManager.updateApplicationStatus(applicantId, Status.TEST_SCHEDULED);

                    JOptionPane.showMessageDialog(null, "Test date updated for applicant " + applicantId);
                    loadTestData();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid date format. Use YYYY-MM-DD HH:MM",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

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




