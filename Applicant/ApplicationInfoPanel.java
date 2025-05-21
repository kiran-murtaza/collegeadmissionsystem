//package Applicant;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.ArrayList;
//import javax.swing.table.TableCellRenderer;
//
///**
// * AppliedForPanel aik custom JPanel hai jo applicant ki submitted applications ko
// * table ki form mein display karta hai, jisme Fee Challan aur Entry Test Schedule bhi hota hai.
// */
//public class AppliedForPanel extends JPanel {
//
//    // Custom colors define kiye gaye hain
//    private static final Color COLORAZ_BLACK = Color.BLACK;
//    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
//    private static final Color COLORAZ_WHITE = Color.WHITE;
//
//    public AppliedForPanel(Applicant applicant) {
//        setLayout(new BorderLayout());           // Panel ka layout BorderLayout set kiya gaya hai
//        setBackground(COLORAZ_WHITE);            // Panel ka background safed rakha gaya hai
//
//        // Heading label banaya gaya hai
//        JLabel title = new JLabel("Applied Colleges");
//        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        title.setForeground(COLORAZ_BLACK);
//
//        // Table ke columns define kiye gaye hain
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
//        // Applicant ki tamam submitted applications list mein le rahe hain
//        ArrayList<ApplicationForm> applications = applicant.getSubmittedApplications();
//        Object[][] data = new Object[applications.size()][columns.length];
//
//        // Har application ka data table ke rows mein set kiya gaya hai
//        for (int i = 0; i < applications.size(); i++) {
//            ApplicationForm app = applications.get(i);
//            data[i][0] = app.getApplicationNumber();
//            data[i][1] = app.getProgramName();
//            data[i][2] = app.getCollegeName();
//            data[i][3] = app.getEmailId();
//            data[i][4] = app.getApplicationDate().toString();
//            data[i][5] = app.getStatus();
//
//            // Agar application accepted hai to "Download" dikhaye, warna blank
//            if (app.getStatus() == ApplicationStatus.ACCEPTED) {
//                data[i][6] = "Download";
//            } else {
//                data[i][6] = "";
//            }
//
//            // Entry test ki date aur time set ki gayi hai
//            data[i][7] = app.getTestDate() + ", " + app.getTestTime();
//        }
//
//        // Table model banaya gaya hai, jisme sirf 'Fee Challan' column editable hoga agar status accepted ho
//        DefaultTableModel model = new DefaultTableModel(data, columns) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                ApplicationForm app = applications.get(row);
//                if (column == 6 && app.getStatus() == ApplicationStatus.ACCEPTED) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        };
//
//        JTable table = new JTable(model);
//
//        // Custom cell renderer: Fee Challan column mein agar accepted hai to button show hoga
//        table.getColumn("Fee Challan").setCellRenderer(new TableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                                                           boolean isSelected, boolean hasFocus,
//                                                           int row, int column) {
//                ApplicationForm app = applications.get(row);
//                if (app.getStatus() == ApplicationStatus.ACCEPTED) {
//                    JButton button = new JButton("Download");
//                    button.setBackground(COLORAZ_SAGE);
//                    button.setForeground(COLORAZ_BLACK);
//                    return button;
//                } else {
//                    return new JLabel(""); // Agar accepted nahi hai to empty label show hoga
//                }
//            }
//        });
//
//        // Custom cell editor: Button pe click karne par download action perform karega
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
//                        stopCellEditing(); // Editing stop karni zaroori hai warna bug aa sakta hai
//                    });
//                    return button;
//                } else {
//                    return super.getCellEditorComponent(table, "", isSelected, row, column);
//                }
//            }
//
//            @Override
//            public Object getCellEditorValue() {
//                return ""; // Return empty string kyunke actual data text nahi hai
//            }
//        });
//
//        // Table styling aur formatting
//        table.setRowHeight(30);
//        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//        table.getTableHeader().setBackground(COLORAZ_SAGE);
//        table.getTableHeader().setForeground(COLORAZ_BLACK);
//
//        // Table ko JScrollPane mein wrap kiya gaya hai for scrollability
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        // Final wrapping panel jisme title aur table dono honge
//        JPanel wrapper = new JPanel(new BorderLayout());
//        wrapper.setBackground(COLORAZ_WHITE);
//        wrapper.add(title, BorderLayout.NORTH);
//        wrapper.add(scrollPane, BorderLayout.CENTER);
//
//        add(wrapper, BorderLayout.CENTER); // Final component ko panel mein add kiya gaya
//    }
//}
