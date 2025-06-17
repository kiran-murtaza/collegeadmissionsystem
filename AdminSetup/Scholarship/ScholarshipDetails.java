package AdminSetup.Scholarship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class ScholarshipDetails extends JFrame {
    public ScholarshipDetails() {


        String[] columnNames = {"ID", "Name", "Marks","check","check"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);// 0 = initially no rows

        String [] r1 = {"001","Ahmed","50"};
        loadDataFromFile("EntryTestRecords.txt",model);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Student Table");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);



    }
    private void loadDataFromFile(String filename, DefaultTableModel model) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(","); // or use "\\t" for tab-separated
                model.addRow(data); // Add to table
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ScholarshipDetails();
    }
}
