package AdminSetup;

class Course {
    private String name;
    private int seats;
    private int eligibility;

    public Course(String name, int seats) {
        this.name = name;
        this.seats = seats;
    }

    public String getName() { return name; }
    public int getSeats() { return seats; }
    public void setEligibility(int score) { eligibility = score; }
    public int getEligibility() { return eligibility; }

    public String toString() {
        return name + " (Seats: " + seats + ", Min Score: " + eligibility + ")";
    }
}
