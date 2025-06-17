package Applicant;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExamLauncher{

    private JFrame frame;
    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    public ExamLauncher(String subjectName, int maxQuestions) {
        loadQuestions(subjectName, maxQuestions);
        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions found for " + subjectName);
            return;
        }
        initUI(subjectName);
        displayQuestion();
    }

    private void initUI(String subjectName) {
        frame = new JFrame(subjectName + " Test");
        frame.setSize(600, 300);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1));

        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            centerPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion();
                } else {
                    JOptionPane.showMessageDialog(frame, "Test Completed!\nScore: " + score + "/" + questions.size());
                    frame.dispose();
                }
            }
        });

        frame.add(questionLabel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(nextButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void displayQuestion() {
        group.clearSelection();
        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + q.question);
        for (int i = 0; i < 21; i++) {
            options[i].setText(q.options[i]);
        }
    }

    private void checkAnswer() {
        Question q = questions.get(currentQuestionIndex);
        for (int i = 0; i < 21; i++) {
            if (options[i].isSelected() && options[i].getText().startsWith(q.correctAnswer + ")")) {
                score++;
            }
        }
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
