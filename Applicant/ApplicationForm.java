package Applicant;

import AdminSetup.College;
import AdminSetup.CollegeManager;
import AdminSetup.Program;
import AdminSetup.ProgramManager;
import Authentication.Users;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ApplicationForm extends JPanel {
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;
    private static int applicationFormCount = 0;
    private JComboBox<String> programDropdown, collegeDropdown;
    private JTextField addressField, board10Field, year10Field, percent10Field, stream10Field;
    private JTextField board12Field, year12Field, percent12Field, stream12Field;
    private JButton submitButton;
//    private JLabel applicationIdLabel;

    private ProgramManager programManager;
    private CollegeManager collegeManager;
    private Users userInfo;




    private String generateApplicationId() {
        applicationFormCount++;
        return "APP-" + String.format("%03d", applicationFormCount);
    }

    public ApplicationForm(Users userInfo, ProgramManager programManager, CollegeManager collegeManager) {
        this.programManager= programManager;
        this.collegeManager=collegeManager;
        this.userInfo = userInfo;


//        setTitle("Admission Application Form");
//        setSize(600, 700);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));
        setBackground(COLORAZ_WHITE);
//        getContentPane().setBackground(COLORAZ_WHITE);

        // Title
        JLabel title = new JLabel("Admission Application Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(COLORAZ_BLACK);

       this.add(title);
       this.add(new JLabel(""));

        // Program Dropdown
        add(new JLabel("Select Program:"));
        programDropdown = new JComboBox<>();
        add(programDropdown);

        // College Dropdown
        add(new JLabel("Select College:"));
        collegeDropdown = new JComboBox<>();
        add(collegeDropdown);

        // Load programs into programDropdown
        loadPrograms();

        // Listener for program selection to update colleges dropdown
        programDropdown.addActionListener(e -> {
            String selectedProgram = (String) programDropdown.getSelectedItem();
            if (selectedProgram != null) {
                updateCollegeDropdown(selectedProgram);
            }
        });

        // Initial update for colleges based on first program if exists
        if (programDropdown.getItemCount() > 0) {
            updateCollegeDropdown((String) programDropdown.getItemAt(0));
        }
    }

    private void loadPrograms() {
        programDropdown.removeAllItems();
        for (Program p : programManager.getAllPrograms()) {
            programDropdown.addItem(p.getName());
        }
    }

    private void updateCollegeDropdown(String programName) {
        collegeDropdown.removeAllItems();
        ArrayList<College> filteredColleges = collegeManager.getCollegesByProgram(programName);
        for (College c : filteredColleges) {
            collegeDropdown.addItem(c.getName());
        }
        // If no college available for program, add placeholder
        if (collegeDropdown.getItemCount() == 0) {
            collegeDropdown.addItem("No colleges available");
        }

//        // Add Program Dropdown
//        add(new JLabel("Select Program:"));
//        programDropdown = new JComboBox<>(program.toArray(new String[0]));
//        add(programDropdown);
//
//// Add College Dropdown
//        add(new JLabel("Select College:"));
//        collegeDropdown = new JComboBox<>(colleges.toArray(new String[0]));
//        add(collegeDropdown);

//        // Program and College Dropdowns
//        add(new JLabel("Select Program:"));
//        programDropdown = new JComboBox<>(programs.toArray(new String[0]));
//        add(programDropdown);
//
//        add(new JLabel("Select College:"));
//        collegeDropdown = new JComboBox<>(colleges.toArray(new String[0]));
//        add(collegeDropdown);

        // Pre-filled Details
        add(new JLabel("Applicant ID:"));
        add(new JLabel(userInfo.getUserID()));

        add(new JLabel("Email:"));
        add(new JLabel(userInfo.getEmail()));

        add(new JLabel("First Name:"));
        add(new JLabel(userInfo.getFirstName()));

        add(new JLabel("Last Name:"));
        add(new JLabel(userInfo.getLastName()));
        

        add(new JLabel("Phone Number:"));
        add(new JLabel(userInfo.getPhone()));

        add(new JLabel("Date of Birth:"));
        add(new JLabel(String.valueOf(userInfo.getDateOfBirth())));

        add(new JLabel("Gender:"));
        add(new JLabel(String.valueOf(userInfo.getGender())));


        // Auto-generated Application ID
//        String appId = programDropdown.getSelectedItem() + "-" + System.currentTimeMillis();
//        applicationIdLabel = new JLabel(appId);
//        add(new JLabel("Application ID:"));
//        add(applicationIdLabel);

//        String appId = generateApplicationId(); // generate ID
//        JLabel applicationIdLabel = new JLabel(appId);
//        add(new JLabel("Application ID:"));
//        add(applicationIdLabel);

        // Address
        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        // 10th Qualification
        add(new JLabel("10th Board:"));
        board10Field = new JTextField();
        add(board10Field);

        add(new JLabel("10th Year:"));
        year10Field = new JTextField();
        add(year10Field);

        add(new JLabel("10th Percentage:"));
        percent10Field = new JTextField();
        add(percent10Field);

        add(new JLabel("10th Stream/Field:"));
        stream10Field = new JTextField();
        add(stream10Field);

        // 12th Qualification
        add(new JLabel("12th Board:"));
        board12Field = new JTextField();
        add(board12Field);

        add(new JLabel("12th Year:"));
        year12Field = new JTextField();
        add(year12Field);

        add(new JLabel("12th Percentage:"));
        percent12Field = new JTextField();
        add(percent12Field);

        add(new JLabel("12th Stream/Field:"));
        stream12Field = new JTextField();
        add(stream12Field);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBackground(COLORAZ_SAGE);
        submitButton.addActionListener(e -> validateForm());
        add(submitButton);

        setVisible(true);
    }

    private void validateForm() {
        if (addressField.getText().isEmpty() ||
                board10Field.getText().isEmpty() || year10Field.getText().isEmpty() ||
                percent10Field.getText().isEmpty() || stream10Field.getText().isEmpty() ||
                board12Field.getText().isEmpty() || year12Field.getText().isEmpty() ||
                percent12Field.getText().isEmpty() || stream12Field.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // ✅ Generate Application ID here
            String applicationId = generateApplicationId();
            System.out.println("Generated App ID: " + applicationId);

            // You can show it in a dialog or label
            JOptionPane.showMessageDialog(this, "Application Submitted Successfully!\nApplication ID: " + applicationId, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optional: Save application to database or file here
           // ---------//

        }

    }
}


