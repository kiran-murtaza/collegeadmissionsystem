//package AdminSetup;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.*;
//
//public class Admin {
//    private static Admin instance;
//    private final List<CollegeManager> collegeManagers = new ArrayList<>();
//
//    private Admin() {}
//
//    public static Admin getInstance() {
//        if (instance == null) {
//            instance = new Admin();
//        }
//        return instance;
//    }
//
//    public void addCollege(String name) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new IllegalArgumentException("College name cannot be null or empty");
//        }
//        collegeManagers.add(new CollegeManager(name.trim()));
//    }
//
//    public void addCourse(String collegeName, String courseName, int seats) {
//        CollegeManager collegeManager = findCollegeByName(collegeName);
//        if (collegeManager == null) {
//            throw new IllegalArgumentException("College not found: " + collegeName);
//        }
//        collegeManager.addCourse(new ProgramManager(courseName.trim(), seats));
//    }
//
//    public void setEligibility(String collegeName, String courseName, int score) {
//        CollegeManager collegeManager = findCollegeByName(collegeName);
//        if (collegeManager == null) {
//            throw new IllegalArgumentException("College not found: " + collegeName);
//        }
//
//        for (ProgramManager programManager : collegeManager.getCourses()) {
//            if (programManager.getName().equals(courseName)) {
//                programManager.setEligibility(score);
//                return;
//            }
//        }
//        throw new IllegalArgumentException("Course not found: " + courseName);
//    }
//
//    public void evaluate() {
//        for (CollegeManager collegeManager : collegeManagers) {
//            collegeManager.evaluateApplicants();
//            collegeManager.evaluateScholarships();
//        }
//    }
//
//    public String getReport(String type) {
//        StringBuilder report = new StringBuilder();
//
//        for (CollegeManager collegeManager : collegeManagers) {
//            switch (type) {
//                case "Admitted":
//                    appendStudentsByStatus(report, collegeManager, Status.ADMITTED);
//                    break;
//                case "Rejected":
//                    appendStudentsByStatus(report, collegeManager, Status.REJECTED);
//                    break;
//                case "Colleges":
//                    report.append("College: ").append(collegeManager.getName()).append("\n");
//                    break;
//                case "Courses":
//                    report.append("College: ").append(collegeManager.getName()).append("\n");
//                    for (ProgramManager programManager : collegeManager.getCourses()) {
//                        report.append("  ").append(programManager).append("\n");
//                    }
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unknown report type: " + type);
//            }
//        }
//        return report.toString();
//    }
//
//    private void appendStudentsByStatus(StringBuilder builder, CollegeManager collegeManager, Status status) {
//        for (Student student : collegeManager.getApplicants()) {
//            if (student.getStatus() == status) {
//                builder.append(student).append("\n");
//            }
//        }
//    }
//
//    public void save(String fileName) throws IOException {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
//            for (CollegeManager collegeManager : collegeManagers) {
//                writer.println("College:" + collegeManager.getName());
//
//                for (ProgramManager programManager : collegeManager.getCourses()) {
//                    writer.println(String.format("Course:%s,%d,%d",
//                            programManager.getName(),
//                            programManager.getSeats(),
//                            programManager.getEligibility()));
//                }
//
//                for (Student student : collegeManager.getApplicants()) {
//                    writer.println(String.format("Student:%s,%s,%s,%d,%s",
//                            student.getName(),
//                            student.getCollegeName(),
//                            student.getCourseName(),
//                            student.getScore(),
//                            student.getStatus()));
//                }
//            }
//        }
//    }
//
//    public void load(String fileName) {
//        collegeManagers.clear();
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            CollegeManager currentCollegeManager = null;
//
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("College:")) {
//                    currentCollegeManager = new CollegeManager(line.substring(8));
//                    collegeManagers.add(currentCollegeManager);
//                }
//                else if (line.startsWith("Course:") && currentCollegeManager != null) {
//                    String[] parts = line.substring(7).split(",");
//                    ProgramManager programManager = new ProgramManager(parts[0], Integer.parseInt(parts[1]));
//                    programManager.setEligibility(Integer.parseInt(parts[2]));
//                    currentCollegeManager.addCourse(programManager);
//                }
//                else if (line.startsWith("Student:") && currentCollegeManager != null) {
//                    String[] parts = line.substring(8).split(",");
//                    Student student = new Student(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
//                    student.setStatus(Status.valueOf(parts[4]));
//                    currentCollegeManager.addApplicant(student);
//                }
//            }
//        } catch (IOException e) {
//            // File not found is acceptable for first run
//        } catch (Exception e) {
//            System.err.println("Error loading data: " + e.getMessage());
//        }
//    }
//
//    public List<String> getCollegeNames() {
//        List<String> names = new ArrayList<>();
//        for (CollegeManager collegeManager : collegeManagers) {
//            names.add(collegeManager.getName());
//        }
//        return names;
//    }
//
//    public List<CollegeManager> getColleges() {
//        return new ArrayList<>(collegeManagers);
//    }
//
//    private CollegeManager findCollegeByName(String name) {
//        if (name == null) return null;
//
//        for (CollegeManager collegeManager : collegeManagers) {
//            if (collegeManager.getName().equals(name.trim())) {
//                return collegeManager;
//            }
//        }
//        return null;
//    }
//}
