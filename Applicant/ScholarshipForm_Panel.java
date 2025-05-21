package Applicant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScholarshipForm_Panel {

    private static JTextField fullNameField, emailField, dobField, addressField, contactField;
    private static JTextField percentageField, institutionField, fieldOfStudyField;
    private static JTextField[] activityRoles = new JTextField[5];
    private static JTextField[] activityDurations = new JTextField[5];
    private static JTextArea scholarshipReasonTextArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scholarship Application Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form components
        fullNameField = new JTextField();
        emailField = new JTextField();
        dobField = new JTextField();  // Date of Birth
        addressField = new JTextField();
        contactField = new JTextField();
        percentageField = new JTextField();
        institutionField = new JTextField();
        fieldOfStudyField = new JTextField();

        // Create extracurricular activity fields
        for (int i = 0; i < 5; i++) {
            activityRoles[i] = new JTextField();
            activityDurations[i] = new JTextField();
        }

        // Create the scholarship reason text area
        scholarshipReasonTextArea = new JTextArea(5, 30);

        // Add form fields to the formPanel
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Email Address:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        formPanel.add(dobField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);

        formPanel.add(new JLabel("Percentage or GPA:"));
        formPanel.add(percentageField);

        formPanel.add(new JLabel("Institution Name:"));
        formPanel.add(institutionField);

        formPanel.add(new JLabel("Field of Study:"));
        formPanel.add(fieldOfStudyField);

        // Extracurricular Activities (Up to 5)
        formPanel.add(new JLabel("Extracurricular Activity 1 (Role & Duration):"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(activityRoles[0]);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(activityDurations[0]);

        formPanel.add(new JLabel("Extracurricular Activity 2 (Role & Duration):"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(activityRoles[1]);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(activityDurations[1]);

        formPanel.add(new JLabel("Extracurricular Activity 3 (Role & Duration):"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(activityRoles[2]);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(activityDurations[2]);

        formPanel.add(new JLabel("Extracurricular Activity 4 (Role & Duration):"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(activityRoles[3]);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(activityDurations[3]);

        formPanel.add(new JLabel("Extracurricular Activity 5 (Role & Duration):"));
        formPanel.add(new JLabel("Role:"));
        formPanel.add(activityRoles[4]);
        formPanel.add(new JLabel("Duration:"));
        formPanel.add(activityDurations[4]);

        // Submit Button
        JButton submitButton = new JButton("Submit Application");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitApplication();
            }
        });

        formPanel.add(submitButton);

        // Add form panel to frame
        frame.add(formPanel);
        frame.setVisible(true);
    }

    // Method to handle the submission of the application
    private static void submitApplication() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String dob = dobField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();
        String percentage = percentageField.getText().trim();
        String institution = institutionField.getText().trim();
        String fieldOfStudy = fieldOfStudyField.getText().trim();

        // Collect extracurricular activity details
        String[] activitiesRoles = new String[5];
        String[] activitiesDurations = new String[5];
        for (int i = 0; i < 5; i++) {
            activitiesRoles[i] = activityRoles[i].getText().trim();
            activitiesDurations[i] = activityDurations[i].getText().trim();
        }

        String scholarshipReason = scholarshipReasonTextArea.getText().trim();

        // Validation (you can add more checks as needed)
        if (fullName.isEmpty() || email.isEmpty() || dob.isEmpty() || address.isEmpty() ||
                contact.isEmpty() || percentage.isEmpty() || institution.isEmpty() || fieldOfStudy.isEmpty() ||
                scholarshipReason.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all the required fields.");
            return;
        }

        // Process the collected data (e.g., save it to a file or database)
        System.out.println("Scholarship Application Submitted!");
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Address: " + address);
        System.out.println("Contact: " + contact);
        System.out.println("Percentage/GPA: " + percentage);
        System.out.println("Institution: " + institution);
        System.out.println("Field of Study: " + fieldOfStudy);
        for (int i = 0; i < 5; i++) {
            System.out.println("Activity " + (i + 1) + ": Role - " + activitiesRoles[i] + ", Duration - " + activitiesDurations[i]);
        }
        System.out.println("Scholarship Reason: " + scholarshipReason);

        JOptionPane.showMessageDialog(null, "Application submitted successfully!");
    }
}
