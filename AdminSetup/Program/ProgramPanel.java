package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ProgramPanel extends JPanel {
    private final ProgramManager programManager;
    private final CollegeManager collegeManager;

    private final JComboBox<String> collegeDropdown;
    private final JTextField nameField;
    private final JTextField seatsField;
    private final JTextField eligibilityField;

    private final DefaultListModel<String> programListModel;
    private final JList<String> programList;

    public ProgramPanel(ProgramManager programManager, CollegeManager collegeManager) {
        this.programManager = programManager;
        this.collegeManager = collegeManager;

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Program Details"));

        collegeDropdown = new JComboBox<>();
        nameField = new JTextField();
        seatsField = new JTextField();
        eligibilityField = new JTextField();

        inputPanel.add(new JLabel("Select College:"));
        inputPanel.add(collegeDropdown);
        inputPanel.add(new JLabel("Program Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Seats:"));
        inputPanel.add(seatsField);
        inputPanel.add(new JLabel("Eligibility Score:"));
        inputPanel.add(eligibilityField);

        programListModel = new DefaultListModel<>();
        programList = new JList<>(programListModel);
        JScrollPane listScrollPane = new JScrollPane(programList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Programs"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        add(inputPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> addProgram());
        removeButton.addActionListener(e -> removeProgram());
        refreshButton.addActionListener(e -> {
            loadCollegesIntoDropdown();
            refreshProgramList();

            nameField.setText("");
            seatsField.setText("");
            eligibilityField.setText("");
        });



        collegeDropdown.addActionListener(e -> refreshProgramList());


        loadCollegesIntoDropdown();
        refreshProgramList();
    }

    private void addProgram() {
        String collegeName = (String) collegeDropdown.getSelectedItem();
        String name = nameField.getText().trim();
        String seatsText = seatsField.getText().trim();
        String eligibilityText = eligibilityField.getText().trim();

        if (collegeName == null || name.isEmpty() || seatsText.isEmpty() || eligibilityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try {
            int seats = Integer.parseInt(seatsText);
            int eligibility = Integer.parseInt(eligibilityText);

            College college = collegeManager.getCollegeByName(collegeName);
            if (college != null) {
                for (Program p : college.getPrograms()) {
                    if (p.getName().equalsIgnoreCase(name)) {
                        JOptionPane.showMessageDialog(this, "Program already exists in this college");
                        return;
                    }
                }

                Program newProgram = new Program(name, seats, eligibility);
                college.addProgram(newProgram);
                collegeManager.saveToFile("colleges.txt");
                JOptionPane.showMessageDialog(this, "Program added to " + collegeName);

                refreshProgramList();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats and Eligibility must be numbers");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving program: " + ex.getMessage());
        }
    }

    private void removeProgram() {
        String selected = programList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a program to remove.");
            return;
        }

        String programName = selected.split(" \\(")[0];
        String collegeName = (String) collegeDropdown.getSelectedItem();

        if (collegeName != null) {
            College college = collegeManager.getCollegeByName(collegeName);
            if (college != null) {
                college.getPrograms().removeIf(p -> p.getName().equalsIgnoreCase(programName));
                try {
                    collegeManager.saveToFile("colleges.txt");
                    refreshProgramList();
                    JOptionPane.showMessageDialog(this, "Program removed");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
                }
            }
        }
    }

    private void refreshProgramList() {
        programListModel.clear();
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        if (selectedCollege != null) {
            College college = collegeManager.getCollegeByName(selectedCollege);
            if (college != null) {
                for (Program p : college.getPrograms()) {
                    programListModel.addElement(p.getProgramDetails());
                }
            }
        }
    }

    private void loadCollegesIntoDropdown() {
        collegeDropdown.removeAllItems();
        try {
            collegeManager.loadFromFile("colleges.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading colleges: " + e.getMessage());
        }

        for (College c : collegeManager.getAllColleges()) {
            collegeDropdown.addItem(c.getName());
        }
    }
}





