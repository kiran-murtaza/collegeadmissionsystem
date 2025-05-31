package Applicant;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class CollegeAndProgramViewer_Panel extends JPanel {
    private JTextField searchField;
    private JButton searchButton, refreshButton;
    private JPanel resultPanel;
    private JScrollPane scrollPane;

    public CollegeAndProgramViewer_Panel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Top Search Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 245, 255));
        topPanel.setBorder(BorderFactory.createTitledBorder("🔍 Search College / Program / Stream"));

        searchField = new JTextField(25);
        searchButton = new JButton("Search");
        refreshButton = new JButton("Refresh");

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        //  Result Panel with Scroll
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(resultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Load all data at startup
        loadCollegeData("");

        // Refresh
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadCollegeData("");
        });

        //  Search
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            loadCollegeData(query);
        });
    }

    private void loadCollegeData(String query) {
        resultPanel.removeAll();

        try (BufferedReader br = new BufferedReader(new FileReader("colleges.txt"))) {
            String line;
            String currentCollege = null;
            JPanel collegePanel = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("College:")) {
                    currentCollege = line.substring(8).trim();
                    collegePanel = createCollegePanel(currentCollege);
                } else if (line.startsWith("Program:")) {
                    String[] parts = line.substring(8).split(",");
                    String programName = parts[0].trim();
                    int seats = Integer.parseInt(parts[1].trim());
                    int eligibility = Integer.parseInt(parts[2].trim());
                    double fee = Double.parseDouble(parts[3].trim());

                    ArrayList<String> streams = new ArrayList<>();
                    // Read all streams after Program
                    br.mark(1000); // Mark current line in case we need to reset
                    while ((line = br.readLine()) != null && line.startsWith("Stream:")) {
                        streams.add(line.substring(7).trim());
                        br.mark(1000);
                    }
                    br.reset(); // Rewind to last non-stream line

                    // Check if matches search
                    boolean matchesQuery = query.isEmpty()
                            || currentCollege.toLowerCase().contains(query)
                            || programName.toLowerCase().contains(query)
                            || String.valueOf(seats).contains(query)
                            || String.valueOf(eligibility).contains(query)
                            || String.valueOf(fee).contains(query)
                            || streams.stream().anyMatch(s -> s.toLowerCase().contains(query));

                    if (matchesQuery && collegePanel != null) {
                        JPanel programPanel = new JPanel();
                        programPanel.setLayout(new GridLayout(5, 1));
                        programPanel.setBackground(new Color(255, 250, 240));
                        programPanel.setBorder(BorderFactory.createTitledBorder(programName));

                        programPanel.add(new JLabel("Program Name: " + programName));
                        programPanel.add(new JLabel("Seats: " + seats));
                        programPanel.add(new JLabel("Eligibility: " + eligibility + "%"));
                        programPanel.add(new JLabel("Fee: PKR " + String.format("%,.2f", fee)+ " per semester"));
                        programPanel.add(new JLabel("Streams: " + String.join(", ", streams)));

                        collegePanel.add(programPanel);
                    }

                }
                if (collegePanel != null && !Arrays.asList(resultPanel.getComponents()).contains(collegePanel)) {
                    resultPanel.add(collegePanel);
                }
            }

            if (resultPanel.getComponentCount() == 0) {
                resultPanel.add(new JLabel(" No results found for: " + query));
            }

        } catch (IOException e) {
            resultPanel.add(new JLabel("⚠ Error reading college.txt!"));
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    private JPanel createCollegePanel(String collegeName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("College: " + collegeName));
        panel.setBackground(new Color(230, 255, 250));
        return panel;
    }
}
