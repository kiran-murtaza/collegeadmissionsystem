package Applicant.Tests;

import javax.swing.*;
import java.awt.*;

public class CommerceTest extends JFrame {
    private String[] questions = {
            "1. What is 'Assets'?",
            "2. What is GST?",
            "3. Primary goal of accounting?",
            "4. What is 'Liability'?",
            "5. Define 'Capital'?",
            "6. What is inflation?",
            "7. Who issues currency notes?",
            "8. What is 'Market Economy'?",
            "9. What is balance sheet?",
            "10. What is 'Revenue'?"
    };

    private String[][] options = {
            {"Resources owned", "Debts owed", "Profits earned", "Expenses incurred"},
            {"Goods and Services Tax", "General Sales Tax", "Government Service Tax", "Global Sales Tax"},
            {"Record transactions", "Calculate tax", "Manage payroll", "Audit accounts"},
            {"What a company owes", "What a company owns", "Profits of company", "Capital invested"},
            {"Owner's investment", "Company debt", "Current assets", "Expenses"},
            {"Rise in prices", "Decrease in GDP", "Growth in sales", "Increase in exports"},
            {"RBI", "Finance Ministry", "Income Tax Dept", "SEBI"},
            {"Government controlled", "Consumer controlled", "Free enterprise system", "Barter system"},
            {"Financial statement showing assets & liabilities", "Sales report", "Profit & loss statement", "Cash flow statement"},
            {"Income earned", "Expenses paid", "Capital invested", "Tax paid"}
    };

    private String[] answers = {
            "Resources owned", "Goods and Services Tax", "Record transactions", "What a company owes",
            "Owner's investment", "Rise in prices", "RBI", "Free enterprise system",
            "Financial statement showing assets & liabilities", "Income earned"
    };

    private JRadioButton[][] radios = new JRadioButton[10][4];
    private ButtonGroup[] groups = new ButtonGroup[10];

    public CommerceTest() {
        setTitle("Commerce Entry Test");
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
        new CommerceTest();
    }
}
