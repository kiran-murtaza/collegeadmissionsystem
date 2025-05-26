package Applicant;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ScholarshipForm_Panel extends JPanel {
    private JComboBox<String> scholarshipComboBox;
    private JTextField studentNameField;
    private JTextField studentIdField;
    private JButton applyButton;

    public ScholarshipForm_Panel(List<String> scholarships) {
        // Set layout to null for absolute positioning
        setLayout(null);

        setVisible(true);


        JLabel selectLabel = new JLabel("Select Scholarship:");
        selectLabel.setBounds(20, 20, 150, 25);
        add(selectLabel);

        scholarshipComboBox = new JComboBox<>(scholarships.toArray(new String[0]));
        scholarshipComboBox.setBounds(180, 20, 180, 25);
        add(scholarshipComboBox);

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setBounds(20, 60, 150, 25);
        add(nameLabel);

        studentNameField = new JTextField();
        studentNameField.setBounds(180, 60, 180, 25);
        add(studentNameField);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(20, 100, 150, 25);
        add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(180, 100, 180, 25);
        add(studentIdField);

        applyButton = new JButton("Apply");
        applyButton.setBounds(120, 150, 100, 30);
        add(applyButton);

        applyButton.addActionListener(e -> applyForScholarship());
    }

    private void applyForScholarship() {
        String scholarship = (String) scholarshipComboBox.getSelectedItem();
        String name = studentNameField.getText().trim();
        String studentId = studentIdField.getText().trim();

        if (name.isEmpty() || studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all student details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Application submitted!\n" +
                        "Student: " + name + "\n" +
                        "ID: " + studentId + "\n" +
                        "Scholarship: " + scholarship,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        clearFields();
    }

    private void clearFields() {
        studentNameField.setText("");
        studentIdField.setText("");
        scholarshipComboBox.setSelectedIndex(0);
    }
}
