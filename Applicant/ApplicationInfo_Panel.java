package Applicant;

import Authentication.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ApplicationInfo_Panel extends JFrame {
    private Applicant userInfo;

    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_WHITE = Color.WHITE;

    public ApplicationInfo_Panel(Applicant userInfo) {
        setLayout(new BorderLayout());
        setBackground(COLORAZ_WHITE);
this.userInfo= userInfo;
        // Heading label
        JLabel title = new JLabel("Aapki Applications ka Record");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(COLORAZ_BLACK);
        add(title, BorderLayout.NORTH);

        // Table columns
        String[] columns = {
                "Application No.",
                "Program Name",
                "College Name",
                "Email ID",
                "Status",
                "Entry Test Date",
                "Test Details"
        };

        ArrayList<ApplicationForm_Panel> applications = userInfo.getSubmittedApplications();
        Object[][] data = new Object[applications.size()][columns.length];

        for (int i = 0; i < applications.size(); i++) {
            ApplicationForm_Panel app = applications.get(i);
            data[i][0] = app.getApplicantID();
            data[i][1] = app.getSelectedProgram();
            data[i][2] = app.getSelectedCollege();
            data[i][3] = app.getEmailId();
            data[i][4] = app.getStatus();

//            // Entry test date
//            if (app.getTestDate() != null) {
//                data[i][5] = app.getTestDate().toString();
//            } else {
//                data[i][5] = "Not Scheduled";
//            }

//            // Test details or action button
//            if (app.getTestDate() != null && app.getTestDate().isEqual(LocalDate.now())) {
//                data[i][6] = "Take Test";
//            } else {
//                data[i][6] = "Unavailable";
//            }
//        }
//
//            DefaultTableModel model = new DefaultTableModel(data, columns) {
//                @Override
//                public boolean isCellEditable(int row, int column) {
//                    return column == 6 && getValueAt(row, column).equals("Take Test");
//                }
//            };

//            JTable table = new JTable(model);
//            table.setRowHeight(30);

//            // Add button renderer and editor
//            table.getColumn("Test Details").setCellRenderer(new ButtonRenderer());
//            table.getColumn("Test Details").setCellEditor(new ButtonEditor(new JCheckBox(), applications));
//
//            JScrollPane scrollPane = new JScrollPane(table);
//            add(scrollPane, BorderLayout.CENTER);
//        }

            // Button renderer class
            class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
                public ButtonRenderer() {
                    setOpaque(true);
                }

                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    setText((value == null) ? "" : value.toString());
                    return this;
                }
            }

            // Button editor class
            class ButtonEditor extends DefaultCellEditor {
                private JButton button;
                private String label;
                private boolean isPushed;
                private ArrayList<ApplicationForm_Panel> applications;
                private int selectedRow;

                public ButtonEditor(JCheckBox checkBox, ArrayList<ApplicationForm_Panel> applications) {
                    super(checkBox);
                    this.applications = applications;
                    button = new JButton();
                    button.setOpaque(true);
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            fireEditingStopped();
                            ApplicationForm_Panel app = applications.get(selectedRow);
                            JOptionPane.showMessageDialog(null, "Test Started for: " + app.getSelectedProgram());
                            // Open test panel here
                        }
                    });
                }

                public Component getTableCellEditorComponent(JTable table, Object value,
                                                             boolean isSelected, int row, int column) {
                    selectedRow = row;
                    label = (value == null) ? "" : value.toString();
                    button.setText(label);
                    isPushed = true;
                    return button;
                }

                public Object getCellEditorValue() {
                    return label;
                }

                public boolean stopCellEditing() {
                    isPushed = false;
                    return super.stopCellEditing();
                }
            }
        }
    }
}
//    public static void main(String[] args) {
//        // Dummy application list
//        ArrayList<ApplicationForm> dummyApps = new ArrayList<>();
//        dummyApps.add(new ApplicationForm("APP001", "BS CS", "ABC College", "test1@mail.com", LocalDate.now(), "10:00 AM", ApplicationStatus.SUBMITTED));
//        dummyApps.add(new ApplicationForm("APP002", "BBA", "XYZ University", "test2@mail.com", LocalDate.now().plusDays(1), "11:00 AM", ApplicationStatus.APPROVED));
//
//        // Dummy applicant object with empty constructor or modify panel to take list directly
//        Applicant dummyApplicant = new Applicant("dummy@mail.com", "Dummy", "User",);
//        dummyApplicant.setSubmittedApplications(dummyApps); // or inject apps another way
//
//        JFrame frame = new JFrame("Application Info Test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1000, 400);
//        frame.add(new ApplicationInfoPanel(dummyApplicant));
//        frame.setVisible(true);



