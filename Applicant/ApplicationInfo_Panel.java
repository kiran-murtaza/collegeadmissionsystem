package Applicant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ApplicationInfo_Panel extends JPanel {
    private Applicant userInfo;

    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_WHITE = Color.WHITE;

    public ApplicationInfo_Panel(Applicant userInfo2) {
        this.userInfo = userInfo2;

        setLayout(new BorderLayout());
        setBackground(COLORAZ_WHITE);

        // Title Label
        JLabel title = new JLabel("Aapki Applications ka Record");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(COLORAZ_BLACK);
        add(title, BorderLayout.NORTH);

        // Check if applications exist
        ArrayList<ApplicationForm_Panel> applications = userInfo.getSubmittedApplications();

        if (applications == null || applications.isEmpty()) {
            JLabel noAppsLabel = new JLabel("Ab tak koi application submit nahi hui hai.");
            noAppsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noAppsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            add(noAppsLabel, BorderLayout.CENTER);
        } else {
            // Define columns
            String[] columns = {
                    "Application No.",
                    "Program Name",
                    "College Name",
                    "Email ID",
                    "Status",
                    "Entry Test Date",
                    "Test Details"
            };

            Object[][] data = new Object[applications.size()][columns.length];

            for (int i = 0; i < applications.size(); i++) {
                ApplicationForm_Panel app = applications.get(i);
                data[i][0] = app.getApplicantID();
                data[i][1] = app.getSelectedProgram();
                data[i][2] = app.getSelectedCollege();
                data[i][3] = app.getEmailId();
                data[i][4] = app.getStatus();

                // Placeholder values for now
                data[i][5] = "Not Scheduled";
                data[i][6] = "Unavailable";
            }

            DefaultTableModel model = new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}
