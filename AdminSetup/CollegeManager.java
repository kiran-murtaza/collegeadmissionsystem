package AdminSetup;

import java.util.ArrayList;

public class CollegeManager {
    private ArrayList<College> colleges;

    public CollegeManager() {
        colleges = new ArrayList<>();
    }

    public void addCollege(String id, String name, ArrayList<String> programNames) {
        colleges.add(new College(id, name, programNames));
    }

    public boolean removeCollegeById(String id) {
        return colleges.removeIf(c -> c.getId().equals(id));
    }

    public ArrayList<College> getAllColleges() {
        return colleges;
    }

    public ArrayList<College> getCollegesByProgram(String programName) {
        ArrayList<College> result = new ArrayList<>();
        for (College c : colleges) {
            if (c.getProgramNames().contains(programName)) {
                result.add(c);
            }
        }
        return result;
    }
    public boolean updateCollegePrograms(String collegeId, ArrayList<String> newPrograms) {
        for (College c : colleges) {
            if (c.getId().equals(collegeId)) {
                c.setProgramNames(newPrograms);
                return true;
            }
        }
        return false;
    }
}


//example idea


//ArrayList<String> medPrograms = new ArrayList<>();
//medPrograms.add("Medical");
//
//ArrayList<String> engPrograms = new ArrayList<>();
//engPrograms.add("Engineering");
//
//CollegeManager collegeManager = new CollegeManager();
//collegeManager.addCollege("C001", "Dow Medical College", medPrograms);
//collegeManager.addCollege("C002", "NED University", engPrograms);
//
//// To get colleges offering "Medical"
//ArrayList<College> medicalColleges = collegeManager.getCollegesByProgram("Medical");
//for (College c : medicalColleges) {
//        System.out.println(c.getName());
//        }






//package AdminSetup;
////Add/remove/edit colleges
//import java.util.ArrayList;
//import java.util.List;
//
//public class CollegeManager {
//    private String name;
//    private List<ProgramManager> cours = new ArrayList<>();
//    private List<Student> applicants = new ArrayList<>();
//    private AdmissionCriteria admissionCriteria;
//    private ScholarshipCriteria scholarshipCriteria;
//
//    public CollegeManager(String name) { this.name = name; }
//    public String getName() { return name; }
//    public void addCourse(ProgramManager c) { cours.add(c); }
//    public List<ProgramManager> getCourses() { return cours; }
//    public void addApplicant(Student s) { applicants.add(s); }
//    public List<Student> getApplicants() { return applicants; }
//
//    public void setAdmissionCriteria(AdmissionCriteria criteria) { this.admissionCriteria = criteria; }
//    public AdmissionCriteria getAdmissionCriteria() { return admissionCriteria; }
//
//    public void setScholarshipCriteria(ScholarshipCriteria criteria) { this.scholarshipCriteria = criteria; }
//    public ScholarshipCriteria getScholarshipCriteria() { return scholarshipCriteria; }
//
//    public void evaluateApplicants() {
//        for (Student s : applicants) {
//            if (admissionCriteria != null) {
//                s.setStatus(admissionCriteria.isEligible(s) ? Status.ADMITTED : Status.REJECTED);
//            } else {
//                for (ProgramManager c : cours) {
//                    if (c.getName().equals(s.getCourseName())) {
//                        s.setStatus(s.getScore() >= c.getEligibility() ? Status.ADMITTED : Status.REJECTED);
//                    }
//                }
//            }
//        }
//    }
//
//    public void evaluateScholarships() {
//        if (scholarshipCriteria == null) return;
//        for (Student s : applicants) {
//            if (s.getStatus() == Status.ADMITTED) {
//                s.setScholarshipApproved(scholarshipCriteria.isEligible(s));
//            }
//        }
//    }
//}
