package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {

    private String address;

    private String board10;
    private String year10;
    private String percent10;
    private String stream10;

    private String board12;
    private String year12;
    private String percent12;
    private String stream12;

    private String selectedProgram;
    private String selectedCollege;

        private ArrayList<ApplicationForm_Panel> submittedApplications;
    private ScholarshipForm_Panel scholarshipForm;
    private ArrayList<AdmissionRecord> admissions;

    private ArrayList<Document> documents = new ArrayList<>();

        public Applicant(String firstName, String lastName, String email, String password,
                         String securityAnswer, String cnic, LocalDate dob,
                         Gender gender, String phone, String userID) {


            super(firstName,lastName,email,password,securityAnswer,cnic,dob,gender,phone,userID);

            this.submittedApplications = new ArrayList<>();
            admissions = new ArrayList<>();
        }

        // Add a submitted application
        public void addApplication(ApplicationForm_Panel application) {
            submittedApplications.add(application);
        }

        // Get all submitted applications
        public ArrayList<ApplicationForm_Panel> getSubmittedApplications() {
            return submittedApplications;
        }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getBoard10() { return board10; }
    public void setBoard10(String board10) { this.board10 = board10; }

    public String getYear10() { return year10; }
    public void setYear10(String year10) { this.year10 = year10; }

    public String getPercent10() { return percent10; }
    public void setPercent10(String percent10) { this.percent10 = percent10; }

    public String getStream10() { return stream10; }
    public void setStream10(String stream10) { this.stream10 = stream10; }

    public String getBoard12() { return board12; }
    public void setBoard12(String board12) { this.board12 = board12; }

    public String getYear12() { return year12; }
    public void setYear12(String year12) { this.year12 = year12; }

    public String getPercent12() { return percent12; }
    public void setPercent12(String percent12) { this.percent12 = percent12; }

    public String getStream12() { return stream12; }
    public void setStream12(String stream12) { this.stream12 = stream12; }

    public String getSelectedProgram() { return selectedProgram; }
    public void setSelectedProgram(String selectedProgram) { this.selectedProgram = selectedProgram; }

    public String getSelectedCollege() { return selectedCollege; }
    public void setSelectedCollege(String selectedCollege) { this.selectedCollege = selectedCollege; }
}






