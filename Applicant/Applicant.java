package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {

        private ArrayList<ApplicationForm> submittedApplications;

        public Applicant(String firstName, String lastName, String password, String securityAnswer,
                         String cnic, LocalDate dateOfBirth, Gender gender, String phone, String email,
                         String fatherName, String address) {
            super(firstName, lastName, password, securityAnswer, cnic, dateOfBirth, gender, phone, email);
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


