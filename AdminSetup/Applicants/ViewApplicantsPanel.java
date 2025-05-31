package AdminSetup.Applicants;

import Applicant.ApplicantManager;
import Applicant.ApplicationFormData;
import Applicant.Status;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class ViewApplicantsPanel extends JPanel {

    public ViewApplicantsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Submitted Applicant Applications");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {
                "Application ID", "10th Board", "10th Year", "10th %", "10th Stream",
                "12th Board", "12th Year", "12th %", "12th Stream", "Program", "College", "Action"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 11; // Only the "Action" column is editable
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);

        try {
            ArrayList<ApplicationFormData> applicants = ApplicantManager.loadAllApplications();

            for (ApplicationFormData app : applicants) {
                model.addRow(new Object[]{
                        app.getApplicationId(),
                        app.getBoard10(),
                        app.getYear10(),
                        app.getPercent10(),
                        app.getStream10(),
                        app.getBoard12(),
                        app.getYear12(),
                        app.getPercent12(),
                        app.getStream12(),
                        app.getSelectedProgram() != null ? app.getSelectedProgram().getName() : "N/A",
                        app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A",
                        "Action"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(new JCheckBox(), model));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Renderer for buttons
    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            add(new JButton("Accept"));
            add(new JButton("Reject"));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    // Editor for buttons
    class ActionCellEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton btnAccept;
        protected JButton btnReject;
        private int editingRow = -1;

        public ActionCellEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            btnAccept = new JButton("Accept");
            btnReject = new JButton("Reject");

            panel.add(btnAccept);
            panel.add(btnReject);

            btnAccept.addActionListener(e -> {
                int row = editingRow;
                if (row == -1) return;

                String appId = (String) model.getValueAt(row, 0);
                ApplicantManager.updateApplicationStatus(appId, Status.APPROVED);
                JOptionPane.showMessageDialog(null, "Application ID " + appId + " has been ACCEPTED.");
                fireEditingStopped();
            });



            btnReject.addActionListener(e -> {
                int row = editingRow;
                if (row == -1) return;

                String appId = (String) model.getValueAt(row, 0);
                ApplicantManager.updateApplicationStatus(appId, Status.REJECTED);
                JOptionPane.showMessageDialog(null, "Application ID " + appId + " has been REJECTED.");
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
