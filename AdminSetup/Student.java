package AdminSetup;

class Student {
    private static int count = 1000;
    private int id;
    private String name, college, course;
    private int score;
    private Status status = Status.PENDING;
    private boolean scholarshipApproved = false;

    public Student(String name, String college, String course, int score) {
        this.id = count++;
        this.name = name;
        this.college = college;
        this.course = course;
        this.score = score;
    }

    public String getName() { return name; }
    public String getCollegeName() { return college; }
    public String getCourseName() { return course; }
    public int getScore() { return score; }
    public Status getStatus() { return status; }
    public void setStatus(Status s) { status = s; }

    public boolean isScholarshipApproved() { return scholarshipApproved; }
    public void setScholarshipApproved(boolean approved) { this.scholarshipApproved = approved; }

    public String toString() {
        return id + " - " + name + ", " + college + ", " + course + ", Score: " + score + ", Status: " + status + (scholarshipApproved ? ", Scholarship: Yes" : ", Scholarship: No");
    }
}
