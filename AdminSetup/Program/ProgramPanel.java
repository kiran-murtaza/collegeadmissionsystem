package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProgramPanel extends JPanel {
    private final CollegeManager collegeManager;
    private final JComboBox<String> collegeDropdown;
    private final JTextField nameField;
    private final JTextField seatsField;
    private final JTextField eligibilityField;
    private final JTextField feeField;
    private final List<JCheckBox> streamChecks = new java.util.ArrayList<>();
    private final String[] streams = {
            "Pre-Medical",
            "Pre-Engineering",
            "Computer Science",
            "Commerce",
            "Humanities/Arts",
            "General Science"
    };
    private final DefaultListModel<String> programListModel;
    private final JList<String> programList;

    public ProgramPanel() {
        this.collegeManager = new CollegeManager();

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Program"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel collegeLabel = new JLabel("Select College:");
        collegeDropdown = new JComboBox<>();
        loadColleges();

        JLabel nameLabel = new JLabel("Program Name:");
        nameField = new JTextField(15);

        JLabel seatsLabel = new JLabel("Total Seats:");
        seatsField = new JTextField(10);

        JLabel eligibilityLabel = new JLabel("Eligibility Score:");
        eligibilityField = new JTextField(10);

        JLabel feeLabel = new JLabel("Program Fee:");
        feeField = new JTextField(10);

        for (String stream : streams) {
            streamChecks.add(new JCheckBox(stream));
        }

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(collegeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(collegeDropdown, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(seatsLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(seatsField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(eligibilityLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(eligibilityField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(feeLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(feeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Allowed Streams:"), gbc);
        gbc.gridx = 1;
        JPanel streamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JCheckBox cb : streamChecks) {
            streamPanel.add(cb);
        }
        formPanel.add(streamPanel, gbc);

        programListModel = new DefaultListModel<>();
        programList = new JList<>(programListModel);
        JScrollPane listScrollPane = new JScrollPane(programList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Programs in College"));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Program");
        JButton removeButton = new JButton("Remove Program");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        add(formPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addProgram());
        removeButton.addActionListener(e -> removeProgram());
        refreshButton.addActionListener(e -> clearFields());

        collegeDropdown.addActionListener(e -> refreshProgramList());
        refreshProgramList();
    }


    private void addProgram() {
        String collegeName = (String) collegeDropdown.getSelectedItem();
        String programName = nameField.getText().trim();
        String seatText = seatsField.getText().trim();
        String eligibilityText = eligibilityField.getText().trim();
        String feeText = feeField.getText().trim();

        if (collegeName == null || programName.isEmpty() || seatText.isEmpty()
                || eligibilityText.isEmpty() || feeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            int seats = Integer.parseInt(seatText);
            int eligibility = Integer.parseInt(eligibilityText);
            double fee = Double.parseDouble(feeText);

            College college = collegeManager.getCollegeByName(collegeName);
            if (college != null) {
                for (Program p : college.getPrograms()) {
                    if (p.getName().equalsIgnoreCase(programName)) {
                        JOptionPane.showMessageDialog(this, "Program already exists in this college.");
                        return;
                    }
                }

                Program newProgram = new Program(programName, seats, eligibility, fee);
                boolean streamSelected = false;
                for (JCheckBox cb : streamChecks) {
                    if (cb.isSelected()) {
                        newProgram.addAllowedStream(cb.getText());
                        streamSelected = true;
                    }
                }
                if (!streamSelected) {
                    JOptionPane.showMessageDialog(this, "Please select at least one allowed stream.");
                    return;
                }
                college.addProgram(newProgram);
                collegeManager.saveToFile("colleges.txt");

                JOptionPane.showMessageDialog(this, "Program added successfully!");
                refreshProgramList();
                clearFields();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats, Eligibility, and Fee must be valid numbers.");
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
                List<Program> programs = college.getPrograms();
                programs.removeIf(p -> p.getName().equalsIgnoreCase(programName));

                collegeManager.saveToFile("colleges.txt");
                refreshProgramList();
                JOptionPane.showMessageDialog(this, "Program removed.");
            }
        }
    }

    private void refreshProgramList() {
        programListModel.clear();
        String collegeName = (String) collegeDropdown.getSelectedItem();
        if (collegeName != null) {
            College college = collegeManager.getCollegeByName(collegeName);
            if (college != null) {
                for (Program p : college.getPrograms()) {
                    programListModel.addElement(p.getProgramDetails());
                }
            }
        }
    }


    private void loadColleges() {
        String selectedCollege = (String) collegeDropdown.getSelectedItem(); // Save current selection
        collegeDropdown.removeAllItems();
        for (College c : collegeManager.getAllColleges()) {
            collegeDropdown.addItem(c.getName());
        }
        if (selectedCollege != null) {
            collegeDropdown.setSelectedItem(selectedCollege); // Restore selection
        }
    }


    private void clearFields() {
        nameField.setText("");
        seatsField.setText("");
        eligibilityField.setText("");
        feeField.setText("");
        for (JCheckBox cb : streamChecks) {
            cb.setSelected(false);
        }
        loadColleges();
        refreshProgramList();
    }
}