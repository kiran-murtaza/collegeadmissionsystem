package AdminSetup.Applicants;

import Applicant.ApplicantManager;
import Applicant.ApplicationFormData;
import Applicant.Applicant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
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
                "Application ID", "Address", "10th Board", "10th Year", "10th %", "10th Stream",
                "12th Board", "12th Year", "12th %", "12th Stream", "Program", "College"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(28);

        try {
            File file = new File("applications.txt");
            ArrayList<ApplicationFormData> applicants = ApplicantManager.loadApplicantsFromFile(file);

            for (ApplicationFormData app : applicants) {
                model.addRow(new Object[]{
                        app.getApplicationId(),
                        app.getAddress(),
                        app.getBoard10(),
                        app.getYear10(),
                        app.getPercent10(),
                        app.getStream10(),
                        app.getBoard12(),
                        app.getYear12(),
                        app.getPercent12(),
                        app.getStream12(),
                        app.getSelectedProgram() != null ? app.getSelectedProgram().getName() : "N/A",
                        app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A"
                });
            }

        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found: applicants.txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
