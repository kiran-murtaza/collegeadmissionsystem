package Applicant;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExamLauncher {

    private JFrame frame;
    private List<Question> questions;
    private List<JPanel> questionPanels;
    private List<ButtonGroup> optionGroups;
    private JButton submitButton;

    public ExamLauncher(String subjectName, int maxQuestions) {
        loadQuestions(subjectName, maxQuestions);
        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions found for " + subjectName);
            return;
        }
        initUI(subjectName);
    }

    private void initUI(String subjectName) {
        frame = new JFrame(subjectName + " Test");
        frame.setSize(800, 700);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionPanels = new ArrayList<>();
        optionGroups = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            JPanel qPanel = new JPanel(new BorderLayout());
            qPanel.setBorder(BorderFactory.createTitledBorder("Q" + (i + 1) + ": " + q.question));

            JPanel optionsPanel = new JPanel(new GridLayout(2, 2));
            ButtonGroup group = new ButtonGroup();
            for (int j = 0; j < 4; j++) {
                JRadioButton option = new JRadioButton(q.options[j]);
                group.add(option);
                optionsPanel.add(option);
            }

            qPanel.add(optionsPanel, BorderLayout.CENTER);
            questionsPanel.add(qPanel);

            questionPanels.add(qPanel);
            optionGroups.add(group);
        }

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        submitButton = new JButton("Submit Test");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> submitTest());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(submitButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void submitTest() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            ButtonGroup group = optionGroups.get(i);
            for (AbstractButton btn : java.util.Collections.list(group.getElements())) {
                if (btn.isSelected() && btn.getText().startsWith(q.correctAnswer + ")")) {
                    score++;
                    break;
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "Test Completed!\nScore: " + score + "/" + questions.size());
        frame.dispose();
    }

    private void loadQuestions(String subjectName, int maxQuestions) {
        List<String> lines;
        questions = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get("questions.txt"));
            boolean capture = false;
            List<String> temp = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().equalsIgnoreCase("=== Subject: " + subjectName + " ===")) {
                    capture = true;
                    continue;
                }
                if (line.startsWith("===") && capture) break;
                if (capture) temp.add(line);
            }

            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).startsWith("Q")) {
                    String q = temp.get(i);
                    String[] opts = new String[4];
                    for (int j = 0; j < 4; j++) {
                        opts[j] = temp.get(i + j + 1);
                    }
                    String ansLine = temp.get(i + 5);
                    char correct = ansLine.charAt(ansLine.length() - 1);
                    questions.add(new Question(q, opts, correct));
                    i += 5;
                }
                if (questions.size() >= maxQuestions) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Question {
        String question;
        String[] options;
        char correctAnswer;

        public Question(String question, String[] options, char correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }
}
