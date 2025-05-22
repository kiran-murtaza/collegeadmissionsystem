package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {


//        private ArrayList<ApplicationForm_Panel> submittedApplications;
    private ScholarshipForm_Panel scholarshipForm;
    private ArrayList<AdmissionRecord> admissions;

    private static ArrayList<ApplicationFormData> submittedApplications = new ArrayList<>();

    private ArrayList<Document> documents = new ArrayList<>();

        public Applicant(String firstName, String lastName, String email, String password,
                         String securityAnswer, String cnic, LocalDate dob,
                         Gender gender, String phone, String userID) {


            super(firstName,lastName,email,password,securityAnswer,cnic,dob,gender,phone,userID);

            this.submittedApplications = new ArrayList<>();
            admissions = new ArrayList<>();
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

    public static void addSubmittedApplication(ApplicationFormData application) {
        submittedApplications.add(application);
    }

    public static ArrayList<ApplicationFormData> getSubmittedApplications() {
        return submittedApplications;
    }
    }




