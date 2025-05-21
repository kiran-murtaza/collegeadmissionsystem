package AdminSetup;

import AdminSetup.AddAdmin.AddAdmin_Panel;
import AdminSetup.CollegeManager;
import AdminSetup.ProgramManager;
import Applicant.ApplicationForm_Panel;
import Authentication.LoginFrame;
import Authentication.Users;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard_Panel extends JFrame {
    private JPanel contentPanel;
    private final ProgramManager programManager;
    private final CollegeManager collegeManager;

    // Color theme
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = new Color(255, 255, 255);

    public AdminDashboard_Panel(ProgramManager programManager, CollegeManager collegeManager) {
        this.programManager = programManager;
        this.collegeManager = collegeManager;

        setupFrame();
        initUI();
    }

    private void setupFrame() {
        setTitle("Admin Dashboard");
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

        JLabel welcomeLabel = new JLabel("Welcome, Admin", SwingConstants.LEFT);
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
                "View College",
                "View Applicant",
                "View Program",
                "Set Test",
                "Add Admin",
                "Set Criteria",
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

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(COLORAZ_WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel defaultContent = new JLabel("Select an option from the menu above", SwingConstants.CENTER);
        defaultContent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        contentPanel.add(defaultContent, BorderLayout.CENTER);

        // Middle Panel
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
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        switch (menuItem) {
            case "View College" -> contentPanel.add(new JLabel("College List Panel Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
            case "View Applicant" -> contentPanel.add(new JLabel("Applicant List Panel Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
            case "View Program" -> contentPanel.add(new JLabel("Program List Panel Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
            case "Set Test" -> contentPanel.add(new JLabel("Set Test Panel Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
            case "Add Admin" -> showAddAdmin();
            case "Set Criteria" -> contentPanel.add(new JLabel("Admission Criteria Panel Placeholder", SwingConstants.CENTER), BorderLayout.CENTER);
            case "Logout" -> {
                dispose();
                new LoginFrame().setVisible(true);
                return;
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAddAdmin() {
        AddAdmin_Panel formPanel = new AddAdmin_Panel();
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


}
