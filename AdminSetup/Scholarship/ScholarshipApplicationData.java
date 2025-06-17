package AdminSetup.Scholarship;

public class ScholarshipApplicationData {
    private String applicantId;
    private String name;
    private String email;
    private String gender;
    private String dob;
    protected String school;
    private String percentage;
    private String achievements;
    private String income;
    private String otherAid;
    private String explanation;
    private String clubs;
    private String volunteerWork;
    private String sports;
    private String leadership;
    private String proofIncome;
    private String portfolio;
    private String signature;
    private String status;
    private String submissionDate;

    public ScholarshipApplicationData(String[] parts) {
        this.applicantId = parts[0];
        this.name = parts[1];
        this.email = parts[2];
        this.gender = parts[3];
        this.dob = parts[4];
        this.school = parts[5];
        this.percentage = parts[6];
        this.achievements = parts[7];
        this.income = parts[8];
        this.otherAid = parts[9];
        this.explanation = parts[10];
        this.clubs = parts[11];
        this.volunteerWork = parts[12];
        this.sports = parts[13];
        this.leadership = parts[14];
        this.proofIncome = parts[15];
        this.portfolio = parts[16];
        this.signature = parts[17];
        this.status = parts[18];
        this.submissionDate = parts[19];
    }

    public String getApplicantId() { return applicantId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPercentage() { return percentage; }
    public String getIncome() { return income; }
    public String getStatus() { return status; }
    public String getSubmissionDate() { return submissionDate; }
    public String getExplanation() { return explanation; }
    public String getAchievements() { return achievements; }
    public String getSchool(){
        return school;
    }
    public String getProofIncome(){
        return proofIncome;
    }
    public String getPortfolio(){
        return portfolio;
    }
}


