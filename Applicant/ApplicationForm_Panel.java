package Applicant;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;
import AdminSetup.Program.Program;
import AdminSetup.Program.ProgramManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Application form class to submit forms - this class will also generate id of forms globally for every applicant
public class ApplicationForm_Panel extends JPanel {

    // Constants for UI colors
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;

    private String applicationId;
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
    public ApplicationForm_Panel(Applicant userInfo, ProgramManager programManager, CollegeManager collegeManager) throws IOException {
        this.userInfo = userInfo;
        this.programManager = programManager;
        this.collegeManager = collegeManager;
        this.colleges = new ArrayList<>();
        applicationCount = readCounter();
        collegeManager.loadFromFile("colleges.txt");
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
        String[] streams = {"Commerce", "Engineering", "Biology"};
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
        }
        else {
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

    private void validateForm() {
        // Check if any required field is empty
        if (addressField.getText().isEmpty() || board10Field.getText().isEmpty() ||
                year10Field.getText().isEmpty() || percent10Field.getText().isEmpty() ||
                stream10Field.getText().isEmpty() || board12Field.getText().isEmpty() ||
                year12Field.getText().isEmpty() || percent12Field.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            // Generate a unique application ID
            applicationId = generateApplicationId();

            // Get selected program and college from dropdowns
            String selectedProgramName = (String) programDropdown.getSelectedItem();
            String selectedCollegeName = (String) collegeDropdown.getSelectedItem();

            Program selectedProgram = programManager.getProgramByName(selectedProgramName);
            College selectedCollege = collegeManager.getCollegeByName(selectedCollegeName);

            // Create ApplicationFormData object with collected inputs
            ApplicationFormData applicationFormData = new ApplicationFormData(
                    applicationId,
                    userInfo,                 // User object, not email
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
                    selectedCollege,
                    userInfo.getEmail()
            );

            // Now set the remaining fields with setters:

            applicationFormData.setTestSchedule("N/A");
            applicationFormData.setTestScore("N/A");
            applicationFormData.setStatus(Status.SUBMITTED);

            // applicationFormData.setTestSchedule(testSchedule);
            // applicationFormData.setTestScore(testScore);
//            applicationFormData.setStatus(Status.SUBMITTED);


            //Save application details to the file "all_applications.txt"
            ApplicantManager.saveToFile(applicationFormData);

            JOptionPane.showMessageDialog(this,
                    "Application Submitted Successfully!\nApplication ID: " + applicationId,
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
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

                if (topFrame != null) {


                    // Open the main dashboard
                    ApplicantDashboard_Panel dashboard = new ApplicantDashboard_Panel(userInfo, programManager, collegeManager);
                    dashboard.setVisible(true);
                    topFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to locate main window. Exiting application.");
                    System.exit(0);
                }
            }

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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

    // Reads application counter from file or returns 0 if file not found or invalid
    private int readCounter() {
        try (Scanner scanner = new Scanner(new File(COUNTER_FILE))) {
            return scanner.nextInt();
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    // Writes updated counter to file
    private void writeCounter(int count) {
        try (FileWriter writer = new FileWriter(COUNTER_FILE)) {
            writer.write(Integer.toString(count));
        } catch (IOException e) {
            // Handle write failure silently or log error
        }
    }
}
