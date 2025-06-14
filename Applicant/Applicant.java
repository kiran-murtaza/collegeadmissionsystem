package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {

    private ScholarshipForm_Panel scholarshipForm;
    private ArrayList<AdmissionRecord> admissions;
    private ArrayList<ApplicationFormData> submittedApplications;
//    private ArrayList<Document> documents = new ArrayList<>();
    private FeeStatus feeStatus = FeeStatus.UNPAID;


    public Applicant(String firstName, String lastName, String email, String password,
                     String securityAnswer, String cnic, LocalDate dob,
                     Gender gender, String phone, String userID) {

        super(firstName, lastName, email, password, securityAnswer, cnic, dob, gender, phone, userID);
        this.submittedApplications = new ArrayList<>();
        this.admissions = new ArrayList<>();
    }



    public  void addSubmittedApplication(ApplicationFormData application) {
        submittedApplications.add(application);
    }

    public ArrayList<ApplicationFormData> getSubmittedApplications() {
        return submittedApplications;
    }

    public void setFeeStatus(FeeStatus feeStatus) {
        this.feeStatus = feeStatus;
    }

    public FeeStatus getFeeStatus() {
        return feeStatus;
    }
}



//
//        // Add a submitted application
//        public void addApplication(ApplicationForm_Panel application) {
//            submittedApplications.add(application);
//        }
//
//        // Get all submitted applications
//        public ArrayList<ApplicationForm_Panel> getSubmittedApplications() {
//            return submittedApplications;
//        }

