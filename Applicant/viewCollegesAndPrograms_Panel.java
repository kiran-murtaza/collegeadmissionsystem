//package Applicant;
//
//import AdminSetup.College;
//import AdminSetup.CollegeManager;
//import AdminSetup.Program;
//import AdminSetup.ProgramManager;
//
//import javax.swing.*;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.List;
//
//public class CollegeProgramViewer extends JFrame {
//
//    private final CollegeManager collegeManager;
//    private final ProgramManager programManager;
//
//    // UI Components
//    private JTable collegeTable;
//    private JList<String> programList;
//    private JTextArea detailsText;
//    private JTextField searchField;
//
//    public CollegeProgramViewer(CollegeManager collegeManager, ProgramManager programManager) {
//        this.collegeManager = collegeManager;
//        this.programManager = programManager;
//
//        initializeUI();
//        loadData();
//    }
//
//    private void initializeUI() {
//        setTitle("College and Program Directory");
//        setSize(1000, 700);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new BorderLayout());
//
//        // Header with search
//        add(createHeaderPanel(), BorderLayout.NORTH);
//
//        // Main content with tabs
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab("Colleges", createCollegePanel());
//        tabbedPane.addTab("Programs", createProgramPanel());
//        add(tabbedPane, BorderLayout.CENTER);
//
//        // Footer
//        add(createFooterPanel(), BorderLayout.SOUTH);
//    }
//
//    private JPanel createHeaderPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBackground(new Color(0, 102, 204));
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JLabel titleLabel = new JLabel("College & Program Directory");
//        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        titleLabel.setForeground(Color.WHITE);
//
//        // Search panel
//        JPanel searchPanel = new JPanel(new BorderLayout());
//        searchPanel.setOpaque(false);
//
//        searchField = new JTextField();
//        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        searchField.setColumns(30);
//        searchField.getDocument().addDocumentListener(new SearchListener());
//
//        JButton searchButton = new JButton("Search");
//        searchButton.addActionListener(e -> performSearch(searchField.getText()));
//
//        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
//        searchPanel.add(searchField, BorderLayout.CENTER);
//        searchPanel.add(searchButton, BorderLayout.EAST);
//
//        panel.add(titleLabel, BorderLayout.WEST);
//        panel.add(searchPanel, BorderLayout.EAST);
//
//        return panel;
//    }
//
//    private JPanel createCollegePanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Table setup
//        String[] columns = {"College Name", "Programs", "Location", "Rating"};
//        DefaultTableModel model = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        collegeTable = new JTable(model);
//        customizeTable(collegeTable);
//
//        JScrollPane scrollPane = new JScrollPane(collegeTable);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        return panel;
//    }
//
//    private JPanel createProgramPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        // Program list
//        programList = new JList<>();
//        programList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        // Details area
//        detailsText = new JTextArea();
//        detailsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        detailsText.setLineWrap(true);
//        detailsText.setWrapStyleWord(true);
//        detailsText.setEditable(false);
//
//        // Selection listener
//        programList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                String selected = programList.getSelectedValue();
//                if (selected != null) {
//                    showProgramDetails(selected);
//                }
//            }
//        });
//
//        // Split pane layout
//        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//                new JScrollPane(programList),
//                new JScrollPane(detailsText));
//        splitPane.setDividerLocation(300);
//
//        panel.add(splitPane, BorderLayout.CENTER);
//        return panel;
//    }
//
//    private JPanel createFooterPanel() {
//        JPanel panel = new JPanel();
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        JButton closeButton = new JButton("Close");
//        closeButton.addActionListener(e -> dispose());
//
//        panel.add(closeButton);
//        return panel;
//    }
//
//    private void loadData() {
//        // Load colleges
//        DefaultTableModel model = (DefaultTableModel) collegeTable.getModel();
//        model.setRowCount(0); // Clear existing data
//
//        for (College college : collegeManager.getAllColleges()) {
//            List<Program> programs = programManager.getProgramsByCollege(college.getName());
//            model.addRow(new Object[]{
//                    college.getName(),
//                    formatProgramList(programs),
//                    college.getLocation(),
//                    college.getRating()
//            });
//        }
//
//        // Load programs
//        DefaultListModel<String> programModel = new DefaultListModel<>();
//        for (Program program : programManager.getAllPrograms()) {
//            programModel.addElement(program.getName());
//        }
//        programList.setModel(programModel);
//    }
//
//    private String formatProgramList(List<Program> programs) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < programs.size(); i++) {
//            sb.append(programs.get(i).getName());
//            if (i < programs.size() - 1) sb.append(", ");
//        }
//        return sb.toString();
//    }
//
//    private void showProgramDetails(String programName) {
//        Program program = programManager.getProgramByName(programName);
//        if (program != null) {
//            detailsText.setText(String.format(
//                    "Program: %s\n\n" +
//                            "Degree: %s\n" +
//                            "Duration: %s\n\n" +
//                            "Eligibility:\n%s\n\n" +
//                            "Description:\n%s",
//                    program.getName(),
//                    program.getDegreeType(),
//                    program.getDuration(),
//                    program.getEligibility(),
//                    program.getDescription()
//            ));
//        }
//    }
//
//    // Overloaded search methods
//    private void performSearch(String query) {
//        if (query.isEmpty()) {
//            loadData();
//            return;
//        }
//
//        // Search colleges
//        DefaultTableModel model = (DefaultTableModel) collegeTable.getModel();
//        model.setRowCount(0);
//        for (College college : collegeManager.searchColleges(query)) {
//            List<Program> programs = programManager.getProgramsByCollege(college.getId());
//            model.addRow(new Object[]{
//                    college.getName(),
//                    formatProgramList(programs),
//                    college.getLocation(),
//                    college.getRating()
//            });
//        }
//
//        // Search programs
//        DefaultListModel<String> programModel = new DefaultListModel<>();
//        for (Program program : programManager.searchPrograms(query)) {
//            programModel.addElement(program.getName());
//        }
//        programList.setModel(programModel);
//    }
//
//    private void customizeTable(JTable table) {
//        table.setRowHeight(30);
//        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//        table.setAutoCreateRowSorter(true);
//    }
//
//    private class SearchListener implements DocumentListener {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            performSearch(searchField.getText());
//        }
//
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            performSearch(searchField.getText());
//        }
//
//        @Override
//        public void changedUpdate(DocumentEvent e) {
//            performSearch(searchField.getText());
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            // Example usage:
//            // CollegeManager collegeManager = new CollegeManager();
//            // ProgramManager programManager = new ProgramManager();
//            // new CollegeProgramViewer(collegeManager, programManager).setVisible(true);
//        });
//    }
//}