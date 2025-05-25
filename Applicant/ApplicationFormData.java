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
    private Program selectedProgram;
    private College selectedCollege;
    private String email;
    private String testSchedule;
    private String testScore;
    private Status status;
    public ApplicationFormData(String applicationId, Applicant users, String address,
                               String board10, String year10, String percent10, String stream10,
                               String board12, String year12, String percent12, String stream12,
                               Program selectedProgram, College selectedCollege,String email) {
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
        this.email = email;      // can be set later via setter
        this.testSchedule = "N/A";  // default initial value
        this.testScore = "N/A";     // default initial value
        this.status = Status.SUBMITTED; // default initial status
    }
    public void saveToFile() throws IOException {
        try (FileWriter writer = new FileWriter("all_applications.txt", true)) {
            String line = String.join(",",
                    applicationId,
                    applicant.getUserID(),
                    email,
                    address,
                    board10,
                    year10,
                    percent10,
                    stream10,
                    board12,
                    year12,
                    percent12,
                    stream12,
                    selectedProgram != null ? selectedProgram.getName() : "N/A",
                    selectedCollege != null ? selectedCollege.getName() : "N/A",
                    status.toString(),
                    java.time.LocalDateTime.now().toString()
            );
            writer.write(line + "\n");
        }
    }


    // Getters and Setters



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

    public Program getSelectedProgram() {
        return selectedProgram;
    }

    public void setSelectedProgram(Program selectedProgram) {
        this.selectedProgram = selectedProgram;
    }

    public College getSelectedCollege() {
        return selectedCollege;
    }

    public void setSelectedCollege(College selectedCollege) {
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


}
