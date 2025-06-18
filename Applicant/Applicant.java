package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.time.LocalDate;
import java.util.ArrayList;

public class Applicant extends Users {

    private ArrayList<ApplicationFormData> submittedApplications;
    private FeeStatus feeStatus = FeeStatus.UNPAID;


    public Applicant(String firstName, String lastName, String email, String password,
                     String securityAnswer, String cnic, LocalDate dob,
                     Gender gender, String phone, String userID) {

        super(firstName, lastName, email, password, securityAnswer, cnic, dob, gender, phone, userID);
        this.submittedApplications = new ArrayList<>();
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




