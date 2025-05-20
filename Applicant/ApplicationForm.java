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
    private JComboBox<String> programDropdown, collegeDropdown,stream12Dropdown;

    private JTextField addressField, board10Field, year10Field, percent10Field, stream10Field;
    private JTextField board12Field, year12Field, percent12Field;
    private JButton submitButton;
//    private JLabel applicationIdLabel;

    private ProgramManager programManager;
    private CollegeManager collegeManager;
    private Users userInfo;
    private ArrayList<College> colleges;



    private String generateApplicationId() {
        applicationFormCount++;
        return "APP-" + String.format("%03d", applicationFormCount);
    }

    public ApplicationForm(Users userInfo, ProgramManager programManager, CollegeManager collegeManager) {
        colleges = new ArrayList<>();

        this.programManager= programManager;
        this.collegeManager=collegeManager;
        this.userInfo = userInfo;

        setLayout(new GridLayout(0, 2));
        setBackground(COLORAZ_WHITE);

        // Title
        JLabel title = new JLabel("Admission Application Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(COLORAZ_BLACK);
        setVisible(true);
       this.add(title);
       this.add(new JLabel(""));
        collegeDropdown = new JComboBox<>();
        programDropdown = new JComboBox<>();



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

        stream12Dropdown = new JComboBox<>();
        add(new JLabel("12th Stream/Field:"));
        String[] streams = {"Commerce", "Engineering", "Medical"};
        for (String s : streams) {
            stream12Dropdown.addItem(s);
        }

        stream12Dropdown.addActionListener(e -> {
            String selectedStream = (String) stream12Dropdown.getSelectedItem();
            if (selectedStream != null) {
                loadProgramsByStream(selectedStream);
                if (programDropdown.getItemCount() > 0) {
                    updateCollegeDropdown((String) programDropdown.getSelectedItem());
                }
            }
        });

        add(stream12Dropdown);

        add(new JLabel("Select Program:"));
        add(programDropdown);
        add(new JLabel("Select College:"));
        add(collegeDropdown);


        if (stream12Dropdown.getItemCount() > 0) {
            loadProgramsByStream((String) stream12Dropdown.getItemAt(0));
            if (programDropdown.getItemCount() > 0) {
                updateCollegeDropdown((String) programDropdown.getSelectedItem());
            }
        }

        programDropdown.addActionListener(e -> {
            String selectedProgram = (String) programDropdown.getSelectedItem();
            if (selectedProgram != null) {
                updateCollegeDropdown(selectedProgram);
            }
        });

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBackground(COLORAZ_SAGE);
        submitButton.addActionListener(e -> validateForm());
        add(submitButton);








    }


    private void loadProgramsByStream(String stream) {
        programDropdown.removeAllItems();
        ArrayList<Program> filteredPrograms = programManager.getProgramsByStream(stream);
        if (filteredPrograms.isEmpty()) {
            programDropdown.addItem("No programs available");
        } else {
            for (Program p : filteredPrograms) {
                programDropdown.addItem(p.getName());
            }
        }
    }


    private void updateCollegeDropdown(String programName) {
        collegeDropdown.removeAllItems();
        ArrayList<College> filteredColleges = collegeManager.getCollegesByProgramName(programName);
        for (College c : filteredColleges) {
            collegeDropdown.addItem(c.getName());
        }
        // If no college available for program, add placeholder
        if (collegeDropdown.getItemCount() == 0) {
            collegeDropdown.addItem("No colleges available");
        }

    }

    private void validateForm() {
        if (addressField.getText().isEmpty() ||
                board10Field.getText().isEmpty() || year10Field.getText().isEmpty() ||
                percent10Field.getText().isEmpty() || stream10Field.getText().isEmpty() ||
                board12Field.getText().isEmpty() || year12Field.getText().isEmpty() ||
                percent12Field.getText().isEmpty() ) {

            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            // ✅ Generate Application ID here
            String applicationId = generateApplicationId();
            System.out.println("Generated App ID: " + applicationId);

            // You can show it in a dialog or label
            JOptionPane.showMessageDialog(this, "Application Submitted Successfully!\nApplication ID: " + applicationId, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Optional: Save application to database or file here
           // ---------//

        }
    }

//    private void showProgramAndCollegeDialog() {
//        JPanel panel = new JPanel(new GridLayout(0, 2));
//        JComboBox<String> programBox = new JComboBox<>();
//        JComboBox<String> collegeBox = new JComboBox<>();
//
////        for (Program p : programManager.getAllPrograms()) {
////            programBox.addItem(p.getName());
////        }
//        for (College college : collegeManager.getAllColleges()) {
//            collegeBox.addItem(college.getName());
//        }
//
//
//        if (programBox.getItemCount() > 0) {
//            programBox.setSelectedIndex(0);
//        }
//
//        panel.add(new JLabel("Select Program:"));
//        panel.add(programBox);
//        panel.add(new JLabel("Select College:"));
//        panel.add(collegeBox);
//
//    }









}


