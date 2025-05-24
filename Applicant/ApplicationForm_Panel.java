package Applicant;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;
import AdminSetup.Program.Program;
import AdminSetup.Program.ProgramManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

//Application form class to submit forms - this class will also generate id of forms globally for every applicant
public class ApplicationForm_Panel extends JPanel {

    // Constants for UI colors
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;


    private  String applicationId;
    private static final String COUNTER_FILE = "application_counter.txt";
    private int applicationCount;

    // UI components
    private JComboBox<String> programDropdown, collegeDropdown, stream12Dropdown;
    private JTextField addressField, board10Field, year10Field, percent10Field, stream10Field;
    private JTextField board12Field, year12Field, percent12Field;
    private JButton submitButton;

    // Managers and state
    private ProgramManager programManager;
    private CollegeManager collegeManager;
    private Applicant userInfo;
    private Status status;
    private ArrayList<College> colleges;


    public String generateApplicationId() {
        applicationCount++;
        writeCounter(applicationCount);
        return "APP-FORM-" + String.format("%03d", applicationCount);
    }

    // Constructor
    public ApplicationForm_Panel(Applicant userInfo, ProgramManager programManager, CollegeManager collegeManager) {
        this.userInfo = userInfo;
        this.programManager = programManager;
        this.collegeManager = collegeManager;
        this.colleges = new ArrayList<>();
        applicationCount = readCounter();

        setLayout(new GridLayout(0, 2));
        setBackground(COLORAZ_WHITE);

        // Title
        JLabel title = new JLabel("Admission Application Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(COLORAZ_BLACK);
        setVisible(true);
        this.add(title);
        this.add(new JLabel(""));

        // Initialize dropdowns
        collegeDropdown = new JComboBox<>();
        programDropdown = new JComboBox<>();

        // Applicant Info Display
        addField("Applicant ID:", userInfo.getUserID());
        addField("Email:", userInfo.getEmail());
        addField("First Name:", userInfo.getFirstName());
        addField("Last Name:", userInfo.getLastName());
        addField("Phone Number:", userInfo.getPhone());
        addField("Date of Birth:", String.valueOf(userInfo.getDateOfBirth()));
        addField("Gender:", String.valueOf(userInfo.getGender()));

        // Address and Education Fields
        addressField = addTextField("Address:");
        board10Field = addTextField("10th Board:");
        year10Field = addTextField("10th Year:");
        percent10Field = addTextField("10th Percentage:");
        stream10Field = addTextField("10th Stream/Field:");

        board12Field = addTextField("12th Board:");
        year12Field = addTextField("12th Year:");
        percent12Field = addTextField("12th Percentage:");

        // Stream Dropdown for 12th
        stream12Dropdown = new JComboBox<>();
        add(new JLabel("12th Stream/Field:"));
        String[] streams = {"Commerce", "Engineering", "Medical"};
        for (String s : streams) stream12Dropdown.addItem(s);

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

        // Program and College dropdowns
        add(new JLabel("Select Program:"));
        add(programDropdown);
        add(new JLabel("Select College:"));
        add(collegeDropdown);

        // Load initial programs and colleges
        if (stream12Dropdown.getItemCount() > 0) {
            loadProgramsByStream((String) stream12Dropdown.getItemAt(0));
            if (programDropdown.getItemCount() > 0) {
                updateCollegeDropdown((String) programDropdown.getSelectedItem());
            }
        }

        // Program dropdown action
        programDropdown.addActionListener(e -> {
            String selectedProgram = (String) programDropdown.getSelectedItem();
            if (selectedProgram != null) {
                updateCollegeDropdown(selectedProgram);
            }
        });

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setBackground(COLORAZ_SAGE);
        submitButton.addActionListener(e -> validateForm());
        add(submitButton);
    }

    // Add label and text field to panel
    private JTextField addTextField(String labelText) {
        add(new JLabel(labelText));
        JTextField textField = new JTextField();
        add(textField);
        return textField;
    }

    // Add a static label
    private void addField(String label, String value) {
        add(new JLabel(label));
        add(new JLabel(value));
    }

    // Load programs based on selected stream
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

    // Update college dropdown based on selected program
    private void updateCollegeDropdown(String programName) {
        collegeDropdown.removeAllItems();
        ArrayList<College> filteredColleges = collegeManager.getCollegesByProgramName(programName);
        for (College c : filteredColleges) {
            collegeDropdown.addItem(c.getName());
        }
        if (collegeDropdown.getItemCount() == 0) {
            collegeDropdown.addItem("No colleges available");
        }
    }

    // Validate form and create application data
    private void validateForm() {
        if (addressField.getText().isEmpty() || board10Field.getText().isEmpty() ||
                year10Field.getText().isEmpty() || percent10Field.getText().isEmpty() ||
                stream10Field.getText().isEmpty() || board12Field.getText().isEmpty() ||
                year12Field.getText().isEmpty() || percent12Field.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        applicationId = generateApplicationId();

        String selectedProgramName = (String) programDropdown.getSelectedItem();
        String selectedCollegeName = (String) collegeDropdown.getSelectedItem();

        Program selectedProgram = programManager.getProgramByName(selectedProgramName);
        College selectedCollege = collegeManager.getCollegeByName(selectedCollegeName);

        ApplicationFormData applicationFormData = new ApplicationFormData(
                applicationId, userInfo,
                addressField.getText(),
                board10Field.getText(),
                year10Field.getText(),
                percent10Field.getText(),
                stream10Field.getText(),
                board12Field.getText(),
                year12Field.getText(),
                percent12Field.getText(),
                stream12Dropdown.getSelectedItem().toString(),
                selectedProgram,
                selectedCollege
        );

        String directoryPath = "applications/" + userInfo.getUserID();
        String filename = directoryPath + "/" + applicationId + ".txt";

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            ApplicantManager.saveToFile(applicationFormData, new File(filename));

            JOptionPane.showMessageDialog(this,
                    "Application Submitted Successfully!\n" +
                            "Application ID: " + applicationId + "\n" +
                            "Saved to: " + filename,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Ask user if they want to fill another form
            int response = JOptionPane.showConfirmDialog(this,
                    "Do you want to submit another application?",
                    "New Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                clearForm();
            } else {
                // Attempt to find the top-level frame that contains this panel
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

                if (topFrame != null) {


                    // Open the main dashboard
                    ApplicantDashboard_Panel dashboard = new ApplicantDashboard_Panel(userInfo, programManager, collegeManager);
                    dashboard.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to locate main window. Exiting application.");
                    System.exit(0);
                }
            }



        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving application: " + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void clearForm() {
        addressField.setText("");
        board10Field.setText("");
        year10Field.setText("");
        percent10Field.setText("");
        stream10Field.setText("");
        board12Field.setText("");
        year12Field.setText("");
        percent12Field.setText("");
        stream12Dropdown.setSelectedIndex(0);
        programDropdown.setSelectedIndex(0);
        collegeDropdown.setSelectedIndex(0);
    }
    // Getters for retrieving form data (used for further processing or testing)
    public String getAddress() { return addressField.getText(); }
    public String getBoard10() { return board10Field.getText(); }
    public String getYear10() { return year10Field.getText(); }
    public String getPercent10() { return percent10Field.getText(); }
    public String getStream10() { return stream10Field.getText(); }
    public String getBoard12() { return board12Field.getText(); }
    public String getYear12() { return year12Field.getText(); }
    public String getPercent12() { return percent12Field.getText(); }
    public String getStream12() { return (String) stream12Dropdown.getSelectedItem(); }
    public String getSelectedProgram() { return (String) programDropdown.getSelectedItem(); }
    public String getSelectedCollege() { return (String) collegeDropdown.getSelectedItem(); }
    public String getApplicantID() { return userInfo.getUserID(); }
    public String getApplicationId() { return applicationId; }
    public Status getStatus() { return status; }
    public String getEmailId() { return userInfo.getEmail(); }

    private int readCounter() {
        try (Scanner sc = new Scanner(new File(COUNTER_FILE))) {
            if (sc.hasNextInt()) return sc.nextInt();
        } catch (Exception e) {
            // agar file nahi mili ya error, start from 0
        }
        return 0;
    }
    private void writeCounter(int count) {
        try (FileWriter fw = new FileWriter(COUNTER_FILE)) {
            fw.write(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
