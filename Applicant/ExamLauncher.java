//package Applicant;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class ExamLauncher {
//
//    private JFrame frame;
//    private ArrayList<Question> questions;
//    private ArrayList<ButtonGroup> optionGroups;
//    private JButton submitButton;
//
//    public ExamLauncher(String subjectName, int maxQuestions) {
//        loadQuestions(subjectName, maxQuestions);
//        if (questions.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "No questions found for " + subjectName);
//            return;
//        }
//        initUI(subjectName);
//    }
//
//    private void initUI(String subjectName) {
//        frame = new JFrame(subjectName + " Test");
//        frame.setSize(800, 700);
//        frame.setLayout(new BorderLayout());
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel questionsPanel = new JPanel();
//        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
//        optionGroups = new ArrayList<>();
//
//        for (int i = 0; i < questions.size(); i++) {
//            Question q = questions.get(i);
//
//            JPanel qPanel = new JPanel(new BorderLayout());
//            qPanel.setBorder(BorderFactory.createTitledBorder("Q" + (i + 1) + ": " + q.question));
//
//            JPanel optionsPanel = new JPanel(new GridLayout(2, 2));
//            ButtonGroup group = new ButtonGroup();
//
//            for (int j = 0; j < 4; j++) {
//                JRadioButton option = new JRadioButton(q.options[j]);
//                group.add(option);
//                optionsPanel.add(option);
//            }
//
//            qPanel.add(optionsPanel, BorderLayout.CENTER);
//            questionsPanel.add(qPanel);
//            optionGroups.add(group);
//        }
//
//        JScrollPane scrollPane = new JScrollPane(questionsPanel);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//        submitButton = new JButton("Submit Test");
//        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
//        submitButton.setBackground(Color.GREEN);
//        submitButton.setForeground(Color.WHITE);
//        submitButton.addActionListener(e -> submitTest());
//
//        frame.add(scrollPane, BorderLayout.CENTER);
//        frame.add(submitButton, BorderLayout.SOUTH);
//
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//
//    private void submitTest() {
//        int score = 0;
//
//        for (int i = 0; i < questions.size(); i++) {
//            Question q = questions.get(i);
//            ButtonGroup group = optionGroups.get(i);
//
//            int selectedIndex = getSelectedIndex(group);
//            int correctIndex = q.correctAnswer - 'A';
//
//            if (selectedIndex == correctIndex) {
//                score++;
//            }
//        }
//
//        JOptionPane.showMessageDialog(frame, "Test Completed!\nScore: " + score + "/" + questions.size());
//        frame.dispose();
//    }
//
//
//    private void loadQuestions(String subjectName, int maxQuestions) {
//        questions = new ArrayList<>();
//
//        try {
//            Scanner scanner = new Scanner(new File("questions.txt"));
//            int count = 0;
//
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                String[] parts = line.split(",");
//
//                if (parts.length == 7 && parts[0].equalsIgnoreCase(subjectName)) {
//                    String questionText = parts[1];
//                    String[] options = new String[]{parts[2], parts[3], parts[4], parts[5]};
//                    char correct = parts[6].trim().toUpperCase().charAt(0);
//                    questions.add(new Question(questionText, options, correct));
//                    count++;
//                }
//
//                if (count >= maxQuestions) break;
//            }
//
//            scanner.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    static class Question {
//        String question;
//        String[] options;
//        char correctAnswer;
//
//        public Question(String question, String[] options, char correctAnswer) {
//            this.question = question;
//            this.options = options;
//            this.correctAnswer = correctAnswer;
//        }
//    }
//}
