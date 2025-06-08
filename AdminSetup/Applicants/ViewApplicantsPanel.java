package AdminSetup.Applicants;

import Applicant.ApplicantManager;
import Applicant.ApplicationFormData;
import Applicant.Status;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;

public class ViewApplicantsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;

    public ViewApplicantsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Submitted Applicant Applications");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] columns = {
                "Application ID", "10th Board", "10th Year", "10th %", "10th Stream",
                "12th Board", "12th Year", "12th %", "12th Stream",
                "Program", "College", "Status", "Action"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 12; // Only "Action" column editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);

        loadApplicants();

        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(table, model));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadApplicants() {
        model.setRowCount(0); // Clear previous rows
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
                        app.getSelectedProgram() != null ? app.getSelectedProgram() : "N/A",
                        app.getSelectedCollege() != null ? app.getSelectedCollege() : "N/A",
                        app.getStatus().toString(),
                        "Action"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnAccept = new JButton("Accept");
        private final JButton btnReject = new JButton("Reject");

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            add(btnAccept);
            add(btnReject);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String status = (String) table.getValueAt(row, 11);
            boolean enabled = status.equalsIgnoreCase(Status.SUBMITTED.toString());
            btnAccept.setEnabled(enabled);
            btnReject.setEnabled(enabled);

            setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }

    class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton btnAccept;
        private JButton btnReject;
        private JTable table;
        private DefaultTableModel model;

        public ActionCellEditor(JTable table, DefaultTableModel model) {
            this.table = table;
            this.model = model;

            btnAccept = new JButton("Accept");
            btnReject = new JButton("Reject");

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.add(btnAccept);
            panel.add(btnReject);

            btnAccept.addActionListener(e -> {
                try {
                    int row = table.getEditingRow();
                    if (row == -1) return;

                    String appId = (String) model.getValueAt(row, 0);
                    Status status = ApplicantManager.getApplicationStatus(appId);

                    if (status == Status.SUBMITTED) {
                        ApplicantManager.updateApplicationStatus(appId, Status.APPROVED);
                        model.setValueAt(Status.APPROVED.toString(), row, 11);
                        JOptionPane.showMessageDialog(null, "Application ID " + appId + " has been ACCEPTED.");
                        stopCellEditing();
                        table.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Application already processed.");
                        stopCellEditing();
                        table.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while accepting application.");
                }
            });

            btnReject.addActionListener(e -> {
                try {
                    int row = table.getEditingRow();
                    if (row == -1) return;

                    String appId = (String) model.getValueAt(row, 0);
                    Status status = ApplicantManager.getApplicationStatus(appId);

                    if (status == Status.SUBMITTED) {
                        ApplicantManager.updateApplicationStatus(appId, Status.REJECTED);
                        model.setValueAt(Status.REJECTED.toString(), row, 11);
                        JOptionPane.showMessageDialog(null, "Application ID " + appId + " has been REJECTED.");
                        stopCellEditing();
                        table.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Application already processed.");
                        stopCellEditing();
                        table.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while rejecting application.");
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            String status = (String) model.getValueAt(row, 11);
            boolean submitted = status.equalsIgnoreCase(Status.SUBMITTED.toString());
            btnAccept.setEnabled(submitted);
            btnReject.setEnabled(submitted);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
