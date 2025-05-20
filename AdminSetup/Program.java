package AdminSetup;

public class Program {
    private String name;
    private int seats;
    private int eligibility;

    public Program(String name, int seats, int eligibility) {
        this.name = name;
        this.seats = seats;
        this.eligibility = eligibility;
    }


    public String getName() {
        return name;
    }
    public int getSeats() {
        return seats;
    }
    public int getEligibility() {
        return eligibility;
    }


    public void setSeats(int seats) {
        this.seats = seats;
    }
    public void setEligibility(int eligibility) {
        this.eligibility = eligibility;
    }

    public String getProgramDetails() {
        return getName() + " (Seats: " + getSeats() + ", Min Score: " + getEligibility() + ")";
    }
}