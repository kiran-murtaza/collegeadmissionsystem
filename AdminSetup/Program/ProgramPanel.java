package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProgramPanel extends JPanel {
    private final ProgramManager programManager;
    private final JTextField nameField;
    private final JTextField seatsField;
    private final JTextField eligibilityField;
    private final DefaultListModel<String> programListModel;
    private final JList<String> programList;

    public ProgramPanel(ProgramManager programManager) {
        this.programManager = programManager;
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Program Details"));

        nameField = new JTextField();
        seatsField = new JTextField();
        eligibilityField = new JTextField();

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
        refreshButton.addActionListener(e -> refreshProgramList());

        refreshProgramList();
    }

    private void addProgram() {

        String name = nameField.getText().trim();
        String seatsText = seatsField.getText().trim();
        String eligibilityText = eligibilityField.getText().trim();

        if (name.isEmpty() || seatsText.isEmpty() || eligibilityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try {
            int seats = Integer.parseInt(seatsText);
            int eligibility = Integer.parseInt(eligibilityText);
            boolean added = programManager.addProgram(name, seats, eligibility);

            if (added) {
                JOptionPane.showMessageDialog(this, "Program added");
                refreshProgramList();
            }
            else {
                JOptionPane.showMessageDialog(this, "Program already exists");
            }
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats and Eligibility must be numbers");
        }
    }

    private void removeProgram() {
        String selected = programList.getSelectedValue();
        if (selected != null) {
            String programName = selected.split(" \\(")[0];
            programManager.removeProgram(programName);
            refreshProgramList();
            JOptionPane.showMessageDialog(this, "Program removed");
        }
        else {
            JOptionPane.showMessageDialog(this, "Select a program to remove.");
        }
    }

    private void refreshProgramList() {
        programListModel.clear();
        for (Program p : programManager.getAllPrograms()) {
            programListModel.addElement(p.getProgramDetails());
        }
    }
}

//.....................................................................//
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//
//public class ProgramPanel extends JPanel {
//    private final CollegeManager collegeManager = new CollegeManager();
//
//    private final JComboBox<String> collegeDropdown = new JComboBox<>();
//    private final JTextField programNameField = new JTextField(15);
//    private final JTextField seatLimitField = new JTextField(5);
//    private final JTextField eligibilityField = new JTextField(5);
//
//    private final DefaultListModel<String> programListModel = new DefaultListModel<>();
//    private final JList<String> programList = new JList<>(programListModel);
//
//    public Program_Panel() {
//        setLayout(new BorderLayout());
//        setBackground(Color.WHITE);
//
//        try {
//            collegeManager.loadFromFile("colleges.txt");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Could not load college data.", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        initUI();
//        refreshCollegeDropdown();
//    }
//
//    private void initUI() {
//        JLabel title = new JLabel("Program Management", JLabel.CENTER);
//        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
//        add(title, BorderLayout.NORTH);
//
//        JPanel center = new JPanel();
//        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
//        center.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));
//        center.setBackground(Color.WHITE);
//
//        center.add(new JLabel("Select College:"));
//        center.add(collegeDropdown);
//
//        center.add(Box.createVerticalStrut(10));
//        center.add(new JLabel("Program Name:"));
//        center.add(programNameField);
//
//        center.add(Box.createVerticalStrut(10));
//        center.add(new JLabel("Seat Limit:"));
//        center.add(seatLimitField);
//
//        center.add(Box.createVerticalStrut(10));
//        center.add(new JLabel("Minimum Merit Score (Eligibility):"));
//        center.add(eligibilityField);
//
//        JButton addButton = new JButton("Add Program");
//        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        addButton.addActionListener(e -> handleAddProgram());
//
//        center.add(Box.createVerticalStrut(20));
//        center.add(addButton);
//
//        center.add(Box.createVerticalStrut(20));
//        center.add(new JLabel("Programs for Selected College:"));
//        center.add(new JScrollPane(programList));
//
//        collegeDropdown.addActionListener(e -> refreshProgramList());
//
//        add(center, BorderLayout.CENTER);
//    }
//
//    private void refreshCollegeDropdown() {
//        collegeDropdown.removeAllItems();
//        for (String name : collegeManager.getCollegeNames()) {
//            collegeDropdown.addItem(name);
//        }
//        refreshProgramList();
//    }
//
//    private void refreshProgramList() {
//        programListModel.clear();
//        String selectedCollege = (String) collegeDropdown.getSelectedItem();
//        if (selectedCollege != null) {
//            for (Program p : collegeManager.getCollegeByName(selectedCollege).getPrograms()) {
//                programListModel.addElement(p.getProgramDetails());
//            }
//        }
//    }
//
//    private void handleAddProgram() {
//        String collegeName = (String) collegeDropdown.getSelectedItem();
//        String programName = programNameField.getText().trim();
//        String seatText = seatLimitField.getText().trim();
//        String eligibilityText = eligibilityField.getText().trim();
//
//        if (collegeName == null || programName.isEmpty() || seatText.isEmpty() || eligibilityText.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Missing Input", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        try {
//            int seats = Integer.parseInt(seatText);
//            int eligibility = Integer.parseInt(eligibilityText);
//
//            College college = collegeManager.getCollegeByName(collegeName);
//            if (college != null) {
//                for (Program existing : college.getPrograms()) {
//                    if (existing.getName().equalsIgnoreCase(programName)) {
//                        JOptionPane.showMessageDialog(this, "This program already exists in the selected college.");
//                        return;
//                    }
//                }
//
//                Program newProgram = new Program(programName, seats, eligibility);
//                college.getPrograms().add(newProgram);
//                collegeManager.saveToFile("colleges.txt");
//
//                refreshProgramList();
//                programNameField.setText("");
//                seatLimitField.setText("");
//                eligibilityField.setText("");
//
//                JOptionPane.showMessageDialog(this, "Program added successfully.");
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Invalid number format for seat or eligibility.");
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Failed to save changes.");
//        }
//    }
//}

