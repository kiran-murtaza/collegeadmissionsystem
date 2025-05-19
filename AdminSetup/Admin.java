package AdminSetup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private static Admin instance;
    private final List<College> colleges = new ArrayList<>();

    private Admin() {}

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    public void addCollege(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("College name cannot be null or empty");
        }
        colleges.add(new College(name.trim()));
    }

    public void addCourse(String collegeName, String courseName, int seats) {
        College college = findCollegeByName(collegeName);
        if (college == null) {
            throw new IllegalArgumentException("College not found: " + collegeName);
        }
        college.addCourse(new Course(courseName.trim(), seats));
    }

    public void setEligibility(String collegeName, String courseName, int score) {
        College college = findCollegeByName(collegeName);
        if (college == null) {
            throw new IllegalArgumentException("College not found: " + collegeName);
        }

        for (Course course : college.getCourses()) {
            if (course.getName().equals(courseName)) {
                course.setEligibility(score);
                return;
            }
        }
        throw new IllegalArgumentException("Course not found: " + courseName);
    }

    public void evaluate() {
        for (College college : colleges) {
            college.evaluateApplicants();
            college.evaluateScholarships();
        }
    }

    public String getReport(String type) {
        StringBuilder report = new StringBuilder();

        for (College college : colleges) {
            switch (type) {
                case "Admitted":
                    appendStudentsByStatus(report, college, Status.ADMITTED);
                    break;
                case "Rejected":
                    appendStudentsByStatus(report, college, Status.REJECTED);
                    break;
                case "Colleges":
                    report.append("College: ").append(college.getName()).append("\n");
                    break;
                case "Courses":
                    report.append("College: ").append(college.getName()).append("\n");
                    for (Course course : college.getCourses()) {
                        report.append("  ").append(course).append("\n");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown report type: " + type);
            }
        }
        return report.toString();
    }

    private void appendStudentsByStatus(StringBuilder builder, College college, Status status) {
        for (Student student : college.getApplicants()) {
            if (student.getStatus() == status) {
                builder.append(student).append("\n");
            }
        }
    }

    public void save(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (College college : colleges) {
                writer.println("College:" + college.getName());

                for (Course course : college.getCourses()) {
                    writer.println(String.format("Course:%s,%d,%d",
                            course.getName(),
                            course.getSeats(),
                            course.getEligibility()));
                }

                for (Student student : college.getApplicants()) {
                    writer.println(String.format("Student:%s,%s,%s,%d,%s",
                            student.getName(),
                            student.getCollegeName(),
                            student.getCourseName(),
                            student.getScore(),
                            student.getStatus()));
                }
            }
        }
    }

    public void load(String fileName) {
        colleges.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            College currentCollege = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("College:")) {
                    currentCollege = new College(line.substring(8));
                    colleges.add(currentCollege);
                }
                else if (line.startsWith("Course:") && currentCollege != null) {
                    String[] parts = line.substring(7).split(",");
                    Course course = new Course(parts[0], Integer.parseInt(parts[1]));
                    course.setEligibility(Integer.parseInt(parts[2]));
                    currentCollege.addCourse(course);
                }
                else if (line.startsWith("Student:") && currentCollege != null) {
                    String[] parts = line.substring(8).split(",");
                    Student student = new Student(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                    student.setStatus(Status.valueOf(parts[4]));
                    currentCollege.addApplicant(student);
                }
            }
        } catch (IOException e) {
            // File not found is acceptable for first run
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public List<String> getCollegeNames() {
        List<String> names = new ArrayList<>();
        for (College college : colleges) {
            names.add(college.getName());
        }
        return names;
    }

    public List<College> getColleges() {
        return new ArrayList<>(colleges);
    }

    private College findCollegeByName(String name) {
        if (name == null) return null;

        for (College college : colleges) {
            if (college.getName().equals(name.trim())) {
                return college;
            }
        }
        return null;
    }
}
