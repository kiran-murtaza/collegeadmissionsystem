package AdminSetup;
//Add/remove/edit colleges
import java.util.ArrayList;
import java.util.List;

public class CollegeManager {
    private String name;
    private List<ProgramManager> cours = new ArrayList<>();
    private List<Student> applicants = new ArrayList<>();
    private AdmissionCriteria admissionCriteria;
    private ScholarshipCriteria scholarshipCriteria;

    public CollegeManager(String name) { this.name = name; }
    public String getName() { return name; }
    public void addCourse(ProgramManager c) { cours.add(c); }
    public List<ProgramManager> getCourses() { return cours; }
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
                for (ProgramManager c : cours) {
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
