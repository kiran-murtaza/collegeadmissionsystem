package AdminSetup.Scholarship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ViewScholarshipsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public ViewScholarshipsPanel() {
        setLayout(new BorderLayout());

        String[] columns = {
                "Applicant ID", "Name", "Email", "Gender", "DOB", "School", "Percentage",
                "Achievements", "Income", "Other Aid", "Explanation", "Clubs", "Volunteer",
                "Sports", "Leadership", "Proof Income", "Portfolio", "Signature",
                "Status", "Date"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadScholarshipData();
    }

    private void loadScholarshipData() {
        File file = new File("allscholarships.txt");
        System.out.println("Looking for file: " + file.getAbsolutePath());

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File 'allscholarships.txt' not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                System.out.println("Reading line " + lineNum + ": " + line);

                String[] parts = line.split("\\|", -1);
                System.out.println(" → parts.length = " + parts.length);

                if (parts.length == 20) {
                    String[] row = new String[20];
                    for (int i = 0; i < 20; i++) {
                        row[i] = parts[i].trim();
                    }
                    model.addRow(row);
                } else {
                    System.err.println("Skipping line " + lineNum + ": Only " + parts.length + " fields.");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading scholarships: " + e.getMessage());
        }
    }

}





