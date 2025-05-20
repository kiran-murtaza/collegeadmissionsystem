package AdminSetup;

import java.util.ArrayList;
import java.util.List;
//
public class Program {
    private String name;
    private int seats;
    private int eligibility;
    private ArrayList<String> allowedStreams;

    public Program(String name, int seats, int eligibility) {
        this.name = name;
        this.seats = seats;
        this.eligibility = eligibility;
        this.allowedStreams = new ArrayList<>();
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

    public void addAllowedStream(String stream) {
        if (!allowedStreams.contains(stream)) {
            allowedStreams.add(stream);
        }
    }

    public boolean isStreamAllowed(String stream) {
        return allowedStreams.contains(stream);
    }

    public ArrayList<String> getAllowedStreams() {
        return allowedStreams;
    }

    public void setAllowedStreams(ArrayList<String> streams) {
        this.allowedStreams = streams;
    }
}


