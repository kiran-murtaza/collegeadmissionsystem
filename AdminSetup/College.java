package AdminSetup;

import java.util.ArrayList;
import java.util.List;

public class College {
    private String name;
    private List<Course> courses = new ArrayList<>();
    private List<Student> applicants = new ArrayList<>();
    private AdmissionCriteria admissionCriteria;
    private ScholarshipCriteria scholarshipCriteria;

    public College(String name) { this.name = name; }
    public String getName() { return name; }
    public void addCourse(Course c) { courses.add(c); }
    public List<Course> getCourses() { return courses; }
    public void addApplicant(Student s) { applicants.add(s); }
    public List<Student> getApplicants() { return applicants; }

    public void setAdmissionCriteria(AdmissionCriteria criteria) { this.admissionCriteria = criteria; }
    public AdmissionCriteria getAdmissionCriteria() { return admissionCriteria; }

    public void setScholarshipCriteria(ScholarshipCriteria criteria) { this.scholarshipCriteria = criteria; }
    public ScholarshipCriteria getScholarshipCriteria() { return scholarshipCriteria; }

    public void evaluateApplicants() {
        for (Student s : applicants) {
            if (admissionCriteria != null) {
                s.setStatus(admissionCriteria.isEligible(s) ? Status.ADMITTED : Status.REJECTED);
            } else {
                for (Course c : courses) {
                    if (c.getName().equals(s.getCourseName())) {
                        s.setStatus(s.getScore() >= c.getEligibility() ? Status.ADMITTED : Status.REJECTED);
                    }
                }
            }
        }
    }

    public void evaluateScholarships() {
        if (scholarshipCriteria == null) return;
        for (Student s : applicants) {
            if (s.getStatus() == Status.ADMITTED) {
                s.setScholarshipApproved(scholarshipCriteria.isEligible(s));
            }
        }
    }
}
