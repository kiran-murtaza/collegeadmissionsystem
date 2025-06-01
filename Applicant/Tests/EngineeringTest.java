package Applicant.Tests;

import javax.swing.*;
import java.awt.*;

public class EngineeringTest extends JFrame {
    private String[] questions = {
            "1. Unit of electrical resistance?",
            "2. Newton's Second Law?",
            "3. SI unit of force?",
            "4. Material used for electrical wires?",
            "5. Speed of light?",
            "6. Thermodynamics studies?",
            "7. Ohm's law relates?",
            "8. Gear ratio is?",
            "9. Stress is force per unit?",
            "10. SI unit of power?"
    };

    private String[][] options = {
            {"Ohm", "Watt", "Volt", "Ampere"},
            {"F=ma", "E=mc^2", "V=IR", "P=IV"},
            {"Newton", "Joule", "Watt", "Pascal"},
            {"Copper", "Iron", "Steel", "Aluminum"},
            {"3 x 10^8 m/s", "3 x 10^6 m/s", "3 x 10^5 m/s", "3 x 10^7 m/s"},
            {"Heat and work", "Motion of planets", "Electricity", "Magnetism"},
            {"Voltage and current", "Force and mass", "Energy and power", "Work and energy"},
            {"Output speed/input speed", "Input speed/output speed", "Torque ratio", "Power ratio"},
            {"Length", "Area", "Volume", "Cross-sectional area"},
            {"Watt", "Newton", "Joule", "Horsepower"}
    };

    private String[] answers = {
            "Ohm", "F=ma", "Newton", "Copper", "3 x 10^8 m/s",
            "Heat and work", "Voltage and current", "Output speed/input speed",
            "Cross-sectional area", "Watt"
    };

    private JRadioButton[][] radios = new JRadioButton[10][4];
    private ButtonGroup[] groups = new ButtonGroup[10];

    public EngineeringTest() {
        setTitle("Engineering Entry Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 10; i++) {
            JPanel qPanel = new JPanel();
            qPanel.setLayout(new BoxLayout(qPanel, BoxLayout.Y_AXIS));
            qPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            groups[i] = new ButtonGroup();

            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
            innerPanel.setBorder(BorderFactory.createTitledBorder(questions[i]));
            innerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            innerPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));

            for (int j = 0; j < 4; j++) {
                radios[i][j] = new JRadioButton(options[i][j]);
                radios[i][j].setAlignmentX(Component.LEFT_ALIGNMENT);
                groups[i].add(radios[i][j]);
                innerPanel.add(radios[i][j]);
            }

            qPanel.add(innerPanel);
            contentPanel.add(qPanel);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        JButton submitBtn = new JButton("Submit");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(submitBtn);

        submitBtn.addActionListener(e -> {
            int score = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 4; j++) {
                    if (radios[i][j].isSelected() && radios[i][j].getText().equals(answers[i])) {
                        score++;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Score: " + score + "/10");
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new EngineeringTest();
    }
}
