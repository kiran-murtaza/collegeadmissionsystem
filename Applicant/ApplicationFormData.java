package Applicant;

import AdminSetup.College.College;
import AdminSetup.Program.Program;
import Authentication.Users;

import java.io.FileWriter;
import java.io.IOException;

public class ApplicationFormData {
    private String applicationId;
    private Applicant applicant;
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
    private String email;
    private String testSchedule;
    private String testScore;
    private Status status;
    private Boolean submitted;
    private Boolean scholarshipSubmitted;

    public ApplicationFormData(String applicationId, Applicant users, String address,
                               String board10, String year10, String percent10, String stream10,
                               String board12, String year12, String percent12, String stream12,
                              String selectedProgram, String selectedCollege,String email) {
        this.applicationId = applicationId;
        this.applicant = users;
        this.address = address;
        this.board10 = board10;
        this.year10 = year10;
        this.percent10 = percent10;
        this.stream10 = stream10;
        this.board12 = board12;
        this.year12 = year12;
        this.percent12 = percent12;
        this.stream12 = stream12;
        this.selectedProgram = selectedProgram;
        this.selectedCollege = selectedCollege;
        this.email = email;         // can be set later via setter
        this.testSchedule = "N/A";  // default initial value
        this.testScore = "N/A";     // default initial value
        this.status = Status.SUBMITTED; // default initial status//
    }



    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Applicant getUsers() {
        return applicant;
    }

    public void setApplicant(Applicant users) {
        this.applicant = users;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBoard10() {
        return board10;
    }

    public void setBoard10(String board10) {
        this.board10 = board10;
    }

    public String getYear10() {
        return year10;
    }

    public void setYear10(String year10) {
        this.year10 = year10;
    }

    public String getPercent10() {
        return percent10;
    }

    public void setPercent10(String percent10) {
        this.percent10 = percent10;
    }

    public String getStream10() {
        return stream10;
    }

    public void setStream10(String stream10) {
        this.stream10 = stream10;
    }

    public String getBoard12() {
        return board12;
    }

    public void setBoard12(String board12) {
        this.board12 = board12;
    }

    public String getYear12() {
        return year12;
    }

    public void setYear12(String year12) {
        this.year12 = year12;
    }

    public String getPercent12() {
        return percent12;
    }

    public void setPercent12(String percent12) {
        this.percent12 = percent12;
    }

    public String getStream12() {
        return stream12;
    }

    public void setStream12(String stream12) {
        this.stream12 = stream12;
    }

    public String getSelectedProgram() {
        return selectedProgram;
    }

    public void setSelectedProgram(String selectedProgram) {
        this.selectedProgram = selectedProgram;
    }

    public String getSelectedCollege() {
        return selectedCollege;
    }

    public void setSelectedCollege(String selectedCollege) {
        this.selectedCollege = selectedCollege;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTestSchedule() {
        return testSchedule;
    }

    public void setTestSchedule(String testSchedule) {
        this.testSchedule = testSchedule;
    }

    public String getTestScore() {
        return testScore;
    }

    public void setTestScore(String testScore) {
        this.testScore = testScore;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isSubmitted() { return submitted; }
    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean isScholarshipSubmitted() { return scholarshipSubmitted; }
    public void setScholarshipSubmitted(boolean scholarshipSubmitted) {
        this.scholarshipSubmitted = scholarshipSubmitted;
    }



}