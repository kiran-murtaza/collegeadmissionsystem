package Applicant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class SubmittedFormList_Panel extends JPanel {
    private Applicant userInfo;

    private static final Color COLOR_BLACK = Color.BLACK;
    private static final Color COLOR_WHITE = Color.WHITE;
    private ArrayList<ApplicationFormData> applications;

    public SubmittedFormList_Panel(Applicant userInfo) {
        this.userInfo = userInfo;

        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);


        JLabel title = new JLabel("Submitted Applications Record");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(COLOR_BLACK);
        add(title, BorderLayout.NORTH);

        applications = new ArrayList<>();
        File file = new File("applications.txt");
        try {
            applications = ApplicantManager.loadApplicantsFromFile( file);
        }
        catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading applications.");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.CENTER);
            return;
        }

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
                data[i][1] = app.getSelectedProgram() != null ? app.getSelectedProgram().getName() : "N/A";
                data[i][2] = app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A";
                data[i][3] = (app.getUsers() != null && app.getUsers().getEmail() != null) ? app.getUsers().getEmail() : "N/A";
                data[i][4] = formatStatus(app.getStatus());

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

    private String formatStatus(Status status) {
        if (status == null) return "Unknown";
        return switch (status) {
            case SUBMITTED -> "Submitted";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            case TEST_SCHEDULED -> "Test Scheduled";
            case TEST_TAKEN -> "Test Taken";
            case ADMISSION_OFFERED -> "Admission Offered";
            case WAIT_LISTED -> "Wait List";
            case ADMISSION_SECURED -> "Admission Secured";
            case ADMISSION_WITHDRAWN -> "Admission Withdrawn";
        };
    }
}