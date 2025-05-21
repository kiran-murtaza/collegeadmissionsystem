package Applicant;

import AdminSetup.College.College;
import AdminSetup.Program;
import Authentication.Users;

import java.util.ArrayList;

public class ApplicationFormData {
    private Users users;
    private String applicationId;
    private String address;
    private String board10, year10, percent10, stream10;
    private String board12, year12, percent12, stream12;
    private Program selectedProgram;
    private ArrayList<College> selectedCollege;
    private Applicant applicant;
    private Status status = Status.SUBMITTED;

//    public ApplicationFormData() {
//        this.users = users;
//        this.applicationId = applicationId;
//        this.address = address;
//        this.board10 = board10;
//        this.year10 = year10;
//        this.percent10 = percent10;
//        this.stream10 = stream10;
//        this.board12 = board12;
//        this.year12 = year12;
//        this.percent12 = percent12;
//        this.stream12 = stream12;
//        this.program = program;
//        this.college = college;
//    }

    // Add getters if needed
    public String getApplicationId() { return applicationId; }
    public String getAddress() { return address; }
    public String getBoard10() { return board10; }
    public String getYear10() { return year10; }
    public String getPercent10() { return percent10; }
    public String getStream10() { return stream10; }
    public String getBoard12() { return board12; }
    public String getYear12() { return year12; }
    public String getPercent12() { return percent12; }
    public String getStream12() { return stream12; }
    public Program getSelectedProgram() { return selectedProgram; }
    public ArrayList<College> getSelectedCollege() { return selectedCollege; }

    public Applicant getApplicant() {
        return applicant;
    }
    public void setApplicant(Applicant applicant){
        this.applicant= applicant;

    }
    public void setApplicationId(String applicationId){
        this.applicationId = applicationId;
    }
    public void setAddress(String address){
        this.address = address;

    }

        public void setBoard10(String board10) {
            this.board10 = board10;
        }


        public void setYear10(String year10) {
            this.year10 = year10;
        }


        public void setPercent10(String percent10) {
            this.percent10 = percent10;
        }



        public void setStream10(String stream10) {
            this.stream10 = stream10;
        }



        public void setBoard12(String board12) {
            this.board12 = board12;
        }



        public void setYear12(String year12) {
            this.year12 = year12;
        }


        public void setPercent12(String percent12) {
            this.percent12 = percent12;
        }


        public void setStream12(String stream12) {
            this.stream12 = stream12;
        }



        public void setSelectedProgram(Program program) {
            this.selectedProgram = program;
        }
        public String getEmail(){
        return users.getEmail();
        }



        public void setSelectedCollege(ArrayList<College> college) {
            this.selectedCollege = college;
        }

        public Status getStatus(){
        return status;

        }
        public void setStatus(Status status){
        this.status= status;
        }



}
