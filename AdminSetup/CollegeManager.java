package AdminSetup;

import java.util.ArrayList;
import java.util.List;

public class CollegeManager {
    private final ArrayList<College> colleges;

    public CollegeManager() {
        colleges = new ArrayList<>();
        College techUniversity = new College("Tech University");
        ArrayList<Program> csPrograms = new ArrayList<>();
        csPrograms.add(new Program("Computer Science",99,22));  // assuming Program has constructor Program(String name)
        techUniversity.setPrograms(csPrograms);
        colleges.add(techUniversity);

        College businessSchool = new College("Business School");
        ArrayList<Program> baPrograms = new ArrayList<>();
        baPrograms.add(new Program("Business Admin",100,102));
        businessSchool.setPrograms(baPrograms);
        colleges.add(businessSchool);
    }

    public void addCollege(String name) {
        colleges.add(new College( name));
    }

    public boolean removeCollegeByName(String name) {
        return colleges.removeIf(c -> c.getName().equalsIgnoreCase(name));
    }

    public ArrayList<College> getAllColleges() {
        return colleges;
    }


    public ArrayList<College> getCollegesByName(String collegeName) {
        ArrayList<College> result = new ArrayList<>();
        for (College c : colleges) {
            if (c.getName().equalsIgnoreCase(collegeName)) {
                result.add(c);
            }
        }
        return result;
    }

    public ArrayList<College> getCollegesByProgramName(String programName) {
        ArrayList<College> result = new ArrayList<>();
        for (College c : colleges) {
            for (Program p : c.getPrograms()) {
                if (p.getName().equalsIgnoreCase(programName)) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }

    public boolean updateCollegeProgramsByName(String collegeName, ArrayList<Program> newPrograms) {
        for (College c : colleges) {
            if (c.getName().equalsIgnoreCase(collegeName)) {
                c.setPrograms(newPrograms);
                return true;
            }
        }
        return false;
    }
}






//example
//


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
