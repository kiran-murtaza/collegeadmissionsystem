package AdminSetup.EntryTest;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Stack;

public class EntryTest_Panel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<String[]> paidApplicants = new ArrayList<>();
    private Stack<String[]> assignmentHistory = new Stack<>();
    private JComboBox<String> timeComboBox;
    private JSpinner dateSpinner;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;

    private String[] columnNames = {"Form ID", "12th Stream", "Program", "College", "Subjects", "Time", "Date", "Status"};

    public EntryTest_Panel() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("\uD83D\uDDC8 Entry Test Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        filterComboBox = new JComboBox<>(new String[]{"All", "Assigned", "Unassigned"});

        topPanel.add(new JLabel("\uD83D\uDD0D Search by Form ID:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("\u2B07\uFE0F Filter by Assignment:"));
        topPanel.add(filterComboBox);
        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
            private void filterTable() {
                String text = searchField.getText();
                sorter.setRowFilter(text.trim().length() == 0 ? null : RowFilter.regexFilter(text));
            }
        });

        filterComboBox.addActionListener(e -> filterByAssignment());

        JPanel assignPanel = new JPanel();
        assignPanel.setLayout(new GridLayout(3, 1));

        JPanel dateTimePanel = new JPanel();
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        timeComboBox = new JComboBox<>(new String[]{"9AM", "10AM", "11AM", "12PM"});

        dateTimePanel.add(new JLabel("\uD83D\uDCC5 Test Date:"));
        dateTimePanel.add(dateSpinner);
        dateTimePanel.add(new JLabel("\u23F0 Test Time:"));
        dateTimePanel.add(timeComboBox);

        assignPanel.add(dateTimePanel);

        JPanel subjectPanel = new JPanel();
        JCheckBox eng = new JCheckBox("English");
        JCheckBox math = new JCheckBox("Math");
        JCheckBox advMath = new JCheckBox("Adv Math");
        JCheckBox bio = new JCheckBox("Biology");

        subjectPanel.add(new JLabel("\uD83D\uDCD8 Subjects:"));
        subjectPanel.add(eng);
        subjectPanel.add(math);
        subjectPanel.add(advMath);
        subjectPanel.add(bio);

        assignPanel.add(subjectPanel);

        JButton assignBtn = new JButton("\uD83D\uDE80 Assign Test");
        JButton refreshBtn = new JButton("\uD83D\uDD04 Refresh");

        assignBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int modelIndex = table.convertRowIndexToModel(selectedRow);
                String formID = (String) tableModel.getValueAt(modelIndex, 0);
                String subjects = (eng.isSelected() ? "Eng," : "") +
                        (math.isSelected() ? "Math," : "") +
                        (advMath.isSelected() ? "AdvMath," : "") +
                        (bio.isSelected() ? "Bio," : "");
                if (!subjects.isEmpty()) subjects = subjects.substring(0, subjects.length() - 1);

                String time = (String) timeComboBox.getSelectedItem();
                String date = new SimpleDateFormat("yyyy-MM-dd").format((Date) dateSpinner.getValue());

                tableModel.setValueAt(subjects, modelIndex, 5);
                tableModel.setValueAt(time, modelIndex, 6);
                tableModel.setValueAt(date, modelIndex, 7);
                tableModel.setValueAt("Assigned", modelIndex, 8);

                updateFileData(formID, subjects, time, date);
            }
        });

        refreshBtn.addActionListener(e -> loadPaidApplicants());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(assignBtn);
        assignPanel.add(buttonPanel);

        add(assignPanel, BorderLayout.SOUTH);

        loadPaidApplicants();
    }

    private void filterByAssignment() {
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        String selection = (String) filterComboBox.getSelectedItem();
        if (selection.equals("All")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(selection));
        }
    }

    private void loadPaidApplicants() {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("all_applications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith("PAID,")) {
                    String[] parts = line.split(",");
                    String formID = parts[0];
                    String stream = parts[10];
                    String program = parts[11];
                    String college = parts[12];
                    String subjects = "";
                    String status = parts[14];
                    String dateTime = parts[15];

                    String time = "", date = "";
                    if (!dateTime.equals("null")) {
                        LocalDateTime dt = LocalDateTime.parse(dateTime);
                        date = dt.toLocalDate().toString();
                        time = dt.toLocalTime().getHour() + "AM";
                    }

                    String assignedStatus = status.equals("TEST_SCHEDULED") ? "Assigned" : "Unassigned";
                    tableModel.addRow(new String[]{formID, stream, program, college, subjects, time, date, assignedStatus});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFileData(String formID, String subjects, String time, String date) {
        try {
            File file = new File("all_applications.txt");
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(formID + ",")) {
                        String[] parts = line.split(",");
                        parts[14] = "TEST_SCHEDULED";
                        String dt = date + "T" + LocalTime.parse(time.replace("AM", ":00"));
                        parts[15] = dt;
                        lines.add(String.join(",", parts));
                    } else {
                        lines.add(line);
                    }
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
