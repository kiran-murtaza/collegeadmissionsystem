package Applicant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SubmittedFormList_Panel extends JPanel {
    private Applicant userInfo;

    private static final Color COLOR_BLACK = Color.BLACK;
    private static final Color COLOR_WHITE = Color.WHITE;

    public SubmittedFormList_Panel(Applicant userInfo) {
        this.userInfo = userInfo;

        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        // Title
        JLabel title = new JLabel("Submitted Applications Record");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(COLOR_BLACK);
        add(title, BorderLayout.NORTH);

        ArrayList<ApplicationFormData> applications = Applicant.getSubmittedApplications();

        if (applications == null || applications.isEmpty()) {
            JLabel noAppsLabel = new JLabel("Ab tak koi application submit nahi hui hai.");
            noAppsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noAppsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            add(noAppsLabel, BorderLayout.CENTER);
        } else {
            String[] columns = {
                    "Application No.",
                    "Program Name",
                    "College Name",
                    "Email ID",
                    "Status",
                    "Entry Test Status",
                    "Test Details"
            };

            Object[][] data = new Object[applications.size()][columns.length];

            for (int i = 0; i < applications.size(); i++) {
                ApplicationFormData app = applications.get(i);

                data[i][0] = app.getApplicationId();
                data[i][1] = app.getSelectedProgram() != null ? app.getSelectedProgram() : "N/A";
                data[i][2] = (app.getSelectedCollege() != null) ? app.getSelectedCollege() : "N/A";
                data[i][3] = app.getEmail() != null ? app.getEmail() : "N/A";

                // Dynamic status from enum
                data[i][4] = formatStatus(app.getStatus());

                // Entry test info (you can customize logic here)
                if (app.getStatus() == Status.TEST_SCHEDULED || app.getStatus() == Status.TEST_TAKEN) {
                    data[i][5] = "Scheduled";
                    data[i][6] = "Test details will be emailed.";
                } else {
                    data[i][5] = "Not Scheduled";
                    data[i][6] = "Unavailable";
                }
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

    // Utility to format status enum
    private String formatStatus(Status status) {
        if (status == null) return "Unknown";
        return switch (status) {
            case SUBMITTED -> "Submitted";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            case TEST_SCHEDULED -> "Test Scheduled";
            case TEST_TAKEN -> "Test Taken";
            case ADMISSION_OFFERED -> "Admission Offered";
            case ADMISSION_SECURED -> "Admission Secured";
            case ADMISSION_WITHDRAWN -> "Admission Withdrawn";
        };
    }
}
