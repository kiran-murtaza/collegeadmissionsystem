package AdminSetup.Program;

import AdminSetup.College.College;

import java.util.ArrayList;


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
//...................................................................................//
//public class Program {
//    private final String name;
//    private final int seatLimit;
//    private int eligibility;
//
//    public Program(String name, int seatLimit, int eligibility) {
//        this.name = name;
//        this.seatLimit = seatLimit;
//        this.eligibility = eligibility;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getSeats() {
//        return seatLimit;
//    }
//    public void setEligibility(int eligibility) {
//        this.eligibility = eligibility;
//    }
//    public int getEligibility() {
//        return eligibility;
//    }
//
//    // Convert to file format: "ProgramName,SeatLimit"
//    public String toFileFormat() {
//        return name + "," + seatLimit + "," + eligibility;
//    }
//
//    public static Program fromFileLine(String line) {
//        String[] parts = line.split(",");
//        if (parts.length == 3) {
//            return new Program(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
//        }
//        else {
//            // Backward compatibility if no eligibility stored yet
//            return new Program(parts[0], Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
//        }
//
//    }
//
//    public String getProgramDetails() {
//        return getName() + " (Seats: " + getSeats() + ", Min Score: " + getEligibility() + ")";
//    }
//}


