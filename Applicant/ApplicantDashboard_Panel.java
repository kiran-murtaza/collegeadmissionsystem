package Applicant;


import AdminSetup.College.CollegeManager;
import AdminSetup.Program.ProgramManager;
import Authentication.LoginFrame;
import Authentication.Users;

import javax.swing.*;
import java.awt.*;

public class ApplicantDashboard_Panel extends JFrame {
    private final Applicant userInfo;
    private JPanel contentPanel;
    private ProgramManager programManager;
    private CollegeManager collegeManager;

    // Color theme
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = new Color(255, 255, 255);




    public ApplicantDashboard_Panel(Applicant userInfo, ProgramManager programManager , CollegeManager collegeManager){
        this.userInfo = userInfo;
        this.programManager = programManager;
        this.collegeManager = collegeManager;
        setupFrame();
        initUI();
    }

    private void setupFrame() {
        setTitle("Applicant Dashboard");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLORAZ_WHITE);
        setLocationRelativeTo(null); // Center the window
    }

    private void initUI() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLORAZ_SAGE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Colaraz, " + userInfo.getFirstName(), SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(COLORAZ_BLACK);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Menu Bar
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        menuPanel.setBackground(COLORAZ_BLACK);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] menuItems = {
                "Apply for College",
                "Submitted Form List",
                "College List",
                "Program List",
                "View Eligibility",
                "Payment Portal",
                "Apply for Scholarship",
                "Logout"
        };

        for (String item : menuItems) {
            JButton menuButton = new JButton(item);
            menuButton.setForeground(COLORAZ_WHITE);
            menuButton.setBackground(COLORAZ_BLACK);
            menuButton.setBorderPainted(false);
            menuButton.setFocusPainted(false);
            menuButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            menuButton.addActionListener(e -> handleMenuClick(item));
            menuPanel.add(menuButton);
            menuPanel.add(Box.createHorizontalStrut(10));
        }

        // Content Panel (Dynamic)
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLORAZ_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel defaultContent = new JLabel("Select an option from the menu above", SwingConstants.CENTER);
        defaultContent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        contentPanel.add(defaultContent, BorderLayout.CENTER);

        // Combine menu + content
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(menuPanel, BorderLayout.NORTH);
        middlePanel.add(contentPanel, BorderLayout.CENTER);
        add(middlePanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(COLORAZ_SAGE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel copyright =
                new JLabel("© 2025 Colaraz. All rights reserved.", SwingConstants.CENTER);
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerPanel.add(copyright);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleMenuClick(String menuItem) {
        switch (menuItem) {
            case "Apply for College" -> showApplicationForm(userInfo, programManager, collegeManager);
            case "Submitted Form List" -> showSubmittedFormList();
            case "College List" -> showCollegeList();
            case "Program List" -> showProgramList();
            case "View Eligibility" -> showEligibilityCriteria();
            case "Payment Portal" -> showPaymentPortal();
            case "Apply for Scholarship" -> showScholarshipForm();
            case "Logout" -> {dispose();
            new LoginFrame().setVisible(true);
            }
        }
    }

    // ---- Menu Action Stubs ----

//    private void showApplicationForm() {
//        contentPanel.removeAll();
//        contentPanel.add(new JLabel("Application Form Here", SwingConstants.CENTER), BorderLayout.CENTER);
//        contentPanel.revalidate();
//        contentPanel.repaint();
//    }

    private void showApplicationForm(Users user, ProgramManager programManager, CollegeManager collegeManager) {
//
        ApplicationForm_Panel formPanel = new ApplicationForm_Panel(userInfo, this.programManager, this.collegeManager);
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSubmittedFormList(){
        SubmittedFormList_Panel submittedFormListPanel = new SubmittedFormList_Panel(userInfo);
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(submittedFormListPanel, BorderLayout.CENTER);
//        contentPanel.add(new JLabel("Application Status & PDF Download", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCollegeList() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("College List Dropdown (Admin)", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showProgramList() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Course List / Programs / Departments", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showEligibilityCriteria() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Eligibility / Admission Criteria", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showPaymentPortal() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Payment Portal", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showScholarshipForm() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Scholarship Form", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


}
