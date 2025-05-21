package AdminSetup.AddAdmin;

import AdminSetup.AdminDashboard_Panel;
import AdminSetup.CollegeManager;
import AdminSetup.ProgramManager;
import Authentication.AdminLogin;

import javax.swing.*;
import java.awt.*;

public class AddAdmin_Panel extends JPanel {
    private static final Color COLORAZ_BLACK = Color.BLACK;
    private static final Color COLORAZ_SAGE = new Color(180, 195, 180);
    private static final Color COLORAZ_WHITE = Color.WHITE;
    AdminLogin login = new AdminLogin();
    private JTextField emailField;
    private JPasswordField passwordField;
    AddAdmin addAdmin;

    public AddAdmin_Panel() {
        setLayout(new BorderLayout());
        setBackground(COLORAZ_WHITE);
        initUI();
        addAdmin=new AddAdmin();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("Add New Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(COLORAZ_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(COLORAZ_WHITE);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 400, 50, 400)); // Centered with padding

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addButton = new JButton("Add Admin");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(COLORAZ_BLACK);
        addButton.setForeground(COLORAZ_WHITE);
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(e -> handleAddAdmin());

        centerPanel.add(emailLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(emailField);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(passwordLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(passwordField);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(addButton);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void handleAddAdmin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();



        String result = addAdmin.setAdmin(email,password);

        switch (result) {
            case "All fields are required.":
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                break;

            case "Fields cannot contain commas.":
                JOptionPane.showMessageDialog(this, "Email and password cannot contain commas.", "Invalid Characters", JOptionPane.ERROR_MESSAGE);
                break;

            case "Invalid email address.":
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                break;

            case "Password must be at least 6 characters.":
                JOptionPane.showMessageDialog(this, "Password should be at least 6 characters long.", "Weak Password", JOptionPane.WARNING_MESSAGE);
                break;

            case "email already exists.":
                JOptionPane.showMessageDialog(this, "This email is already registered as an admin.", "Duplicate Email", JOptionPane.ERROR_MESSAGE);
                break;

            case "new admin added":
                JOptionPane.showMessageDialog(this, "Admin added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                break;

            default:
                JOptionPane.showMessageDialog(this, result, "Unknown Response", JOptionPane.ERROR_MESSAGE);
                break;
        }


        login.saveAdmin();

    }
}
