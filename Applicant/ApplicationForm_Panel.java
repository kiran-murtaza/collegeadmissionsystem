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
    private ProgramManager programManager;
    private CollegeManager collegeManager;

    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;

    private String applicationId;
    private static final String COUNTER_FILE = "application_counter.txt";
    private int applicationCount;

    private JComboBox<String> programDropdown, collegeDropdown, stream12Dropdown;
    private JTextField addressField, board10Field, year10Field, percent10Field, stream10Field;
    private JTextField board12Field, year12Field, percent12Field;
    private JButton submitButton;

    private Applicant userInfo;
    private Status status;
    private ArrayList<College> colleges;

    public String generateApplicationId() {
        applicationCount++;
        writeCounter(applicationCount);
        return "APP-FORM-" + String.format("%03d", applicationCount);
    }

    public ApplicationForm_Panel(Applicant userInfo, ProgramManager programManager, CollegeManager collegeManager) throws IOException {
        this.userInfo = userInfo;
        this.collegeManager = collegeManager;
        this.programManager = programManager;
        this.colleges = new ArrayList<>();
        applicationCount = readCounter();
        collegeManager.loadFromFile("colleges.txt");

        setLayout(new BorderLayout());
        setBackground(COLORAZ_WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(COLORAZ_WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel title = new JLabel("Admission Application Form", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(COLORAZ_BLACK);
        add(title, BorderLayout.NORTH);

        // Applicant Info Display
        row = addLabelAndValue(formPanel, "Applicant ID:", userInfo.getUserID(), row, gbc);
        row = addLabelAndValue(formPanel, "Email:", userInfo.getEmail(), row, gbc);
        row = addLabelAndValue(formPanel, "First Name:", userInfo.getFirstName(), row, gbc);
        row = addLabelAndValue(formPanel, "Last Name:", userInfo.getLastName(), row, gbc);
        row = addLabelAndValue(formPanel, "Phone Number:", userInfo.getPhone(), row, gbc);
        row = addLabelAndValue(formPanel, "Date of Birth:", String.valueOf(userInfo.getDateOfBirth()), row, gbc);
        row = addLabelAndValue(formPanel, "Gender:", String.valueOf(userInfo.getGender()), row, gbc);

        addressField = addFieldWithPlaceholder(formPanel, "Address:", "Enter your address", row++, gbc);
        board10Field = addFieldWithPlaceholder(formPanel, "10th Board:", "Enter your 10th board", row++, gbc);
        year10Field = addFieldWithPlaceholder(formPanel, "10th Year:", "Enter year of 10th", row++, gbc);
        percent10Field = addFieldWithPlaceholder(formPanel, "10th Percentage:", "Enter your 10th %", row++, gbc);
        stream10Field = addFieldWithPlaceholder(formPanel, "10th Stream:", "Enter your 10th stream", row++, gbc);

        board12Field = addFieldWithPlaceholder(formPanel, "12th Board:", "Enter your 12th board", row++, gbc);
        year12Field = addFieldWithPlaceholder(formPanel, "12th Year:", "Enter year of 12th", row++, gbc);
        percent12Field = addFieldWithPlaceholder(formPanel, "12th Percentage:", "Enter your 12th %", row++, gbc);

        stream12Dropdown = new JComboBox<>(new String[] {
                "Select your 12th Stream", "Pre-Medical", "Pre-Engineering",
                "Computer Science", "Commerce", "Humanities/Arts", "General Science"
        });
        stream12Dropdown.addActionListener(e -> {
            String selectedStream = (String) stream12Dropdown.getSelectedItem();
            if (selectedStream != null && !selectedStream.equals("Select your 12th Stream")) {
                loadProgramsByStream(selectedStream);
            } else {
                programDropdown.removeAllItems();
                programDropdown.addItem("Select a stream first");

                collegeDropdown.removeAllItems();
                collegeDropdown.addItem("Select a program first");
            }
        });
        addLabelAndComponent(formPanel, "12th Stream:", stream12Dropdown, row++, gbc);

        programDropdown = new JComboBox<>();
        addLabelAndComponent(formPanel, "Program:", programDropdown, row++, gbc);

        collegeDropdown = new JComboBox<>();
        addLabelAndComponent(formPanel, "College:", collegeDropdown, row++, gbc);

        programDropdown.addActionListener(e -> {
            String selectedProgram = (String) programDropdown.getSelectedItem();
            if (selectedProgram != null && !selectedProgram.equals("No programs available") &&
                    !selectedProgram.equals("Select a stream first") && !selectedProgram.trim().isEmpty()) {
                updateCollegeDropdown(selectedProgram);
            } else {
                collegeDropdown.removeAllItems();
                collegeDropdown.addItem("Select a program first");
            }
        });

        submitButton = new JButton("Submit");
        submitButton.setBackground(COLORAZ_SAGE);
        submitButton.addActionListener(e -> validateForm());

        gbc.gridx = 1;
        gbc.gridy = row;
        formPanel.add(submitButton, gbc);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private int addLabelAndValue(JPanel panel, String label, String value, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);

        return row + 1;
    }

    private JTextField addFieldWithPlaceholder(JPanel panel, String label, String placeholder, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField();
        field.setToolTipText(placeholder);
        panel.add(field, gbc);

        return field;
    }

    private void addLabelAndComponent(JPanel panel, String label, JComponent component, int row, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    // Load programs based on selected stream
    private void loadProgramsByStream(String stream) {
        programDropdown.removeAllItems();
        ArrayList<Program> filteredPrograms = collegeManager.getProgramsByStream(stream);


        System.out.println("Loading programs for stream: " + stream);
        System.out.println("Programs found: " + filteredPrograms.size());

        if (filteredPrograms.isEmpty()) {
            programDropdown.addItem("No programs available");
        } else {
            for (Program p : filteredPrograms) {
                programDropdown.addItem(p.getName());
            }

            // Select the first valid program automatically:
            programDropdown.setSelectedIndex(0);
        }
    }


    // Update college dropdown based on selected program
    private void updateCollegeDropdown(String programName) {
        collegeDropdown.removeAllItems();
        ArrayList<College> filteredColleges = collegeManager.getCollegesByProgramName(programName);

        System.out.println("Loading colleges for program: " + programName);
        System.out.println("Colleges found: " + filteredColleges.size());

        if (filteredColleges.isEmpty()) {
            collegeDropdown.addItem("No colleges available");
        } else {
            for (College c : filteredColleges) {
                collegeDropdown.addItem(c.getName());
            }
            collegeDropdown.setSelectedIndex(0);
        }

        // Enable submit only if a valid college is selected
        collegeDropdown.addActionListener(e -> {
            String selectedCollege = (String) collegeDropdown.getSelectedItem();

            if (selectedCollege != null
                    && !selectedCollege.equals("No colleges available")
                    && !selectedCollege.equals("Select a program first")
                    && !selectedCollege.trim().isEmpty()) {

                submitButton.setEnabled(true);
                System.out.println("Selected college: " + selectedCollege);
            } else {
                submitButton.setEnabled(false);
            }
        });
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

//            String selectedProgram = programManager.getProgramByName(selectedProgramName);
//            String selectedCollege = collegeManager.getCollegeByName(selectedCollegeName);

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
                    selectedProgramName,
                    selectedCollegeName,
                    userInfo.getEmail()
            );

            // Now set the remaining fields with setters:

            applicationFormData.setTestSchedule(null);
            applicationFormData.setTestScore("N/A");
            applicationFormData.setStatus(Status.SUBMITTED);
            applicationFormData.setSubmitted(true);

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
        }
        catch (IOException | NumberFormatException e) {
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

}