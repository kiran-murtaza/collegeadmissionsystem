package AdminSetup;
import AdminSetup.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class AdminDashboardGUI extends JFrame {
    private final Admin admin = Admin.getInstance();
    private final String DATA_FILE = "admission_data.txt";
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public AdminDashboardGUI() {
        admin.load(DATA_FILE);

        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createWelcomePanel(), "welcome");
        mainPanel.add(createCollegesPanel(), "colleges");
        mainPanel.add(createProgramsPanel(), "programs");
        mainPanel.add(createApplicantsPanel(), "applicants");
        mainPanel.add(createReportsPanel(), "reports");
        mainPanel.add(createCriteriaPanel(), "criteria");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Select an option from the menu above", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(204, 214, 192));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        JLabel welcomeLabel = new JLabel("Welcome Admin", JLabel.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        //Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        menuBar.setPreferredSize(new Dimension(getWidth(), 35));

        String[] menuItems = {"Colleges", "Programs", "Applicants", "Entry Test", "Reports", "Criteria", "Logout"};

        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            styleMenuButton(menuButton);
            menuButton.addActionListener(new MenuActionHandler());
            menuBar.add(menuButton);
        }

        headerPanel.add(menuBar, BorderLayout.SOUTH);
        return headerPanel;
    }

    private void styleMenuButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private class MenuActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "Colleges":
                    cardLayout.show(mainPanel, "colleges");
                    break;
                case "Programs":
                    cardLayout.show(mainPanel, "programs");
                    break;
                case "Applicants":
                    cardLayout.show(mainPanel, "applicants");
                    break;
                case "Entry Test":
                    admin.evaluate();
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Applicants evaluated successfully.");
                    break;
                case "Reports":
                    cardLayout.show(mainPanel, "reports");
                    break;
                case "Criteria":
                    cardLayout.show(mainPanel, "criteria");
                    break;
                case "Logout":
                    logout();
                    break;
            }
        }

        private void logout() {
            try {
                admin.save(DATA_FILE);
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Data saved. Exiting.");
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Error saving data.");
            }
        }
    }

    private JPanel createCollegesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField collegeNameField = new JTextField();
        JButton addButton = new JButton("Add College");

        inputPanel.add(new JLabel("College Name:"));
        inputPanel.add(collegeNameField);
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);

        addButton.addActionListener(e -> {
            String name = collegeNameField.getText().trim();
            if (!name.isEmpty()) {
                admin.addCollege(name);
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "College added.");
                collegeNameField.setText("");
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createProgramsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        // College dropdown
        JComboBox<String> collegeCombo = new JComboBox<>();
        updateCollegeComboBox(collegeCombo);

        JTextField programField = new JTextField();
        JTextField seatsField = new JTextField();
        JButton addButton = new JButton("Add Program");
        JButton refreshButton = new JButton("Refresh Colleges");

        inputPanel.add(new JLabel("College:"));
        inputPanel.add(collegeCombo);
        inputPanel.add(new JLabel("Program Name:"));
        inputPanel.add(programField);
        inputPanel.add(new JLabel("Available Seats:"));
        inputPanel.add(seatsField);
        inputPanel.add(refreshButton);
        inputPanel.add(addButton);

        refreshButton.addActionListener(e -> updateCollegeComboBox(collegeCombo));

        addButton.addActionListener(e -> {
            try {
                int seats = Integer.parseInt(seatsField.getText());
                String college = (String) collegeCombo.getSelectedItem();
                if (college != null && !college.isEmpty()) {
                    admin.addCourse(college, programField.getText(), seats);
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Program added.");
                    programField.setText("");
                    seatsField.setText("");
                } else {
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Please select a college.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Invalid seat number.");
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        return panel;
    }

    private void updateCollegeComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        List<String> colleges = admin.getCollegeNames();
        for (String college : colleges) {
            comboBox.addItem(college);
        }
    }

    private JPanel createApplicantsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Display applicants in a table
        JTextArea applicantsArea = new JTextArea(admin.getReport("Admitted") + "\n\n" + admin.getReport("Rejected"));
        applicantsArea.setEditable(false);
        applicantsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(applicantsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton admittedBtn = new JButton("Admitted Applicants");
        JButton rejectedBtn = new JButton("Rejected Applicants");
        JButton collegesBtn = new JButton("Colleges Report");
        JButton programsBtn = new JButton("Programs Report");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(admittedBtn);
        buttonPanel.add(rejectedBtn);
        buttonPanel.add(collegesBtn);
        buttonPanel.add(programsBtn);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        ActionListener reportListener = e -> {
            String reportType = ((JButton)e.getSource()).getText()
                    .replace(" Report", "")
                    .replace(" Applicants", "");
            reportArea.setText(admin.getReport(reportType));
        };

        admittedBtn.addActionListener(reportListener);
        rejectedBtn.addActionListener(reportListener);
        collegesBtn.addActionListener(reportListener);
        programsBtn.addActionListener(reportListener);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCriteriaPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Admission Criteria Tab
        JPanel admissionPanel = new JPanel(new BorderLayout());
        admissionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel admissionInputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // College dropdown for criteria
        JComboBox<String> admissionCollegeCombo = new JComboBox<>();
        updateCollegeComboBox(admissionCollegeCombo);

        JTextField admissionScoreField = new JTextField();
        JButton setAdmissionBtn = new JButton("Set Admission Criteria");

        admissionInputPanel.add(new JLabel("College:"));
        admissionInputPanel.add(admissionCollegeCombo);
        admissionInputPanel.add(new JLabel("Minimum Score:"));
        admissionInputPanel.add(admissionScoreField);
        admissionInputPanel.add(new JLabel());
        admissionInputPanel.add(setAdmissionBtn);

        setAdmissionBtn.addActionListener(e -> {
            try {
                int score = Integer.parseInt(admissionScoreField.getText());
                String college = (String) admissionCollegeCombo.getSelectedItem();
                if (college != null && !college.isEmpty()) {
                    // Implementation depends on your College class
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Admission criteria set for " + college);
                    admissionScoreField.setText("");
                } else {
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Please select a college.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Invalid score number.");
            }
        });

        admissionPanel.add(admissionInputPanel, BorderLayout.NORTH);
        tabbedPane.addTab("Admission Criteria", admissionPanel);

        // Scholarship Criteria Tab
        JPanel scholarshipPanel = new JPanel(new BorderLayout());
        scholarshipPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel scholarshipInputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // College dropdown for scholarship
        JComboBox<String> scholarshipCollegeCombo = new JComboBox<>();
        updateCollegeComboBox(scholarshipCollegeCombo);

        JTextField scholarshipScoreField = new JTextField();
        JButton setScholarshipBtn = new JButton("Set Scholarship Criteria");

        scholarshipInputPanel.add(new JLabel("College:"));
        scholarshipInputPanel.add(scholarshipCollegeCombo);
        scholarshipInputPanel.add(new JLabel("Minimum Score:"));
        scholarshipInputPanel.add(scholarshipScoreField);
        scholarshipInputPanel.add(new JLabel());
        scholarshipInputPanel.add(setScholarshipBtn);

        setScholarshipBtn.addActionListener(e -> {
            try {
                int score = Integer.parseInt(scholarshipScoreField.getText());
                String college = (String) scholarshipCollegeCombo.getSelectedItem();
                if (college != null && !college.isEmpty()) {
                    // Implementation depends on your College class
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Scholarship criteria set for " + college);
                    scholarshipScoreField.setText("");
                } else {
                    JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Please select a college.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AdminDashboardGUI.this, "Invalid score number.");
            }
        });

        scholarshipPanel.add(scholarshipInputPanel, BorderLayout.NORTH);
        tabbedPane.addTab("Scholarship Criteria", scholarshipPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboardGUI dashboard = new AdminDashboardGUI();
            dashboard.setVisible(true);
        });
    }
}
