package Applicant;//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.ArrayList;
//import javax.swing.table.TableCellRenderer;
//
//public class AppliedForPanel extends JPanel {
//    private static final Color COLORAZ_BLACK = Color.BLACK;
//    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
//    private static final Color COLORAZ_WHITE = Color.WHITE;
//
//    public AppliedForPanel(Applicant applicant) {
//        setLayout(new BorderLayout());
//        setBackground(COLORAZ_WHITE);
//
//        JLabel title = new JLabel("Applied Colleges");
//        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        title.setForeground(COLORAZ_BLACK);
//
//        String[] columns = {
//                "Application No.",
//                "Program Name",
//                "College Name",
//                "Email ID",
//                "Application Date",
//                "Status",
//                "Fee Challan",
//                "Entry Test Schedule"
//        };
//
//        ArrayList<ApplicationForm> applications = applicant.getSubmittedApplications();
//        Object[][] data = new Object[applications.size()][columns.length];
//
//        for (int i = 0; i < applications.size(); i++) {
//            ApplicationForm app = applications.get(i);
//            data[i][0] = app.getApplicationNumber();
//            data[i][1] = app.getProgramName();
//            data[i][2] = app.getCollegeName();
//            data[i][3] = app.getEmailId();
//            data[i][4] = app.getApplicationDate().toString();
//            data[i][5] = app.getStatus();
//
//            // Using if-else for Fee Challan column
//            if (app.getStatus() == ApplicationStatus.ACCEPTED) {
//                data[i][6] = "Download";
//            } else {
//                data[i][6] = ""; // Empty for non-accepted applications
//            }
//
//            data[i][7] = app.getTestDate() + ", " + app.getTestTime();
//        }
//
//        DefaultTableModel model = new DefaultTableModel(data, columns) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                // Using if-else for cell editability
//                ApplicationForm app = applications.get(row);
//                if (column == 6 && app.getStatus() == ApplicationStatus.ACCEPTED) {
//                    return true; // Only editable if accepted
//                } else {
//                    return false; // Not editable otherwise
//                }
//            }
//        };
//
//        JTable table = new JTable(model);
//
//        // Custom renderer with if-else
//        table.getColumn("Fee Challan").setCellRenderer(new TableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                                                           boolean isSelected, boolean hasFocus,
//                                                           int row, int column) {
//                ApplicationForm app = applications.get(row);
//
//                if (app.getStatus() == ApplicationStatus.ACCEPTED) {
//                    JButton button = new JButton("Download");
//                    button.setBackground(COLORAZ_SAGE);
//                    button.setForeground(COLORAZ_BLACK);
//                    return button;
//                } else {
//                    return new JLabel(""); // empty label if not accepted
//                }
//            }
//        });
//
//        // Custom editor with if-else
//        table.getColumn("Fee Challan").setCellEditor(new DefaultCellEditor(new JTextField()) {
//            @Override
//            public Component getTableCellEditorComponent(JTable table, Object value,
//                                                         boolean isSelected, int row, int column) {
//                ApplicationForm app = applications.get(row);
//                if (app.getStatus() == ApplicationStatus.ACCEPTED) {
//                    JButton button = new JButton("Download");
//                    button.addActionListener(e -> {
//                        JOptionPane.showMessageDialog(AppliedForPanel.this,
//                                "Downloading fee challan for: " + app.getApplicationNumber());
//                        stopCellEditing();
//                    });
//                    return button;
//                } else {
//                    return super.getTableCellEditorComponent(table, "", isSelected, row, column);
//                }
//            }
//
//            @Override
//            public Object getCellEditorValue() {
//                return "";
//            }
//        });
//
//        table.setRowHeight(30);
//        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//        table.getTableHeader().setBackground(COLORAZ_SAGE);
//        table.getTableHeader().setForeground(COLORAZ_BLACK);
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        JPanel wrapper = new JPanel(new BorderLayout());
//        wrapper.setBackground(COLORAZ_WHITE);
//        wrapper.add(title, BorderLayout.NORTH);
//        wrapper.add(scrollPane, BorderLayout.CENTER);
//
//        add(wrapper, BorderLayout.CENTER);
//    }
//}