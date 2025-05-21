package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {


        private ArrayList<ApplicationForm> submittedApplications;
    private ScholarshipForm scholarshipForm;
    private ArrayList<Document> documents = new ArrayList<>();

        public Applicant(String firstName, String lastName, String email, String password,
                         String securityAnswer, String cnic, LocalDate dob,
                         Gender gender, String phone, String userID) {


            super(firstName,lastName,email,password,securityAnswer,cnic,dob,gender,phone,userID);

            this.submittedApplications = new ArrayList<>();
        }

        // Add a submitted application
        public void addApplication(ApplicationForm application) {
            submittedApplications.add(application);
        }

        // Get all submitted applications
        public ArrayList<ApplicationForm> getSubmittedApplications() {
            return submittedApplications;
        }

    }




