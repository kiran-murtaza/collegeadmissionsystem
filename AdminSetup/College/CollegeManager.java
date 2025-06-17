package AdminSetup.College;

import AdminSetup.Program.Program;

import java.io.*;
import java.util.ArrayList;

public class CollegeManager {
    private final ArrayList<College> colleges;
    private final String fileName = "colleges.txt";

    public CollegeManager() {
        colleges = new ArrayList<>();
        loadFromFile(fileName);
    }

    // Add a new college
    public void addCollege(String name) {
        colleges.add(new College(name));
    }

    // Remove a college by name
    public boolean removeCollegeByName(String name) {
        return colleges.removeIf(c -> c.getName().equalsIgnoreCase(name));
    }

    // Get all colleges
    public ArrayList<College> getAllColleges() {
        return colleges;
    }

    // Get a specific college by name
    public College getCollegeByName(String collegeName) {
        for (College c : colleges) {
            if (c.getName().equalsIgnoreCase(collegeName)) {
                return c;
            }
        }
        return null;
    }

    // Get all colleges that offer a given program
    public ArrayList<College> getCollegesByProgramName(String programName) {
        ArrayList<College> result = new ArrayList<>();

        for (College college : colleges) {
            for (Program program : college.getPrograms()) {
                if (program.getName().equalsIgnoreCase(programName)) {
                    boolean alreadyExists = false;

                    for (College existing : result) {
                        if (existing.getName().equalsIgnoreCase(college.getName())) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    if (!alreadyExists) {
                        result.add(college);
                    }

                    break;
                }
            }
        }

        return result;
    }

    // Get all programs that allow a specific stream
    public ArrayList<Program> getProgramsByStream(String stream) {
        ArrayList<Program> result = new ArrayList<>();

        for (College college : colleges) {
            for (Program program : college.getPrograms()) {
                if (program.getAllowedStreams().contains(stream)) {
                    boolean alreadyExists = false;

                    for (Program existing : result) {
                        if (existing.getName().equalsIgnoreCase(program.getName())) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    if (!alreadyExists) {
                        result.add(program);
                    }
                }
            }
        }
        return result;
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (College college : colleges) {
                writer.write("College:" + college.getName());
                writer.newLine();
                for (Program program : college.getPrograms()) {
                    writer.write("Program:" + program.getName() + "," + program.getSeats() + "," +
                            program.getEligibility() + "," + program.getFee());
                    writer.newLine();
                    for (String stream : program.getAllowedStreams()) {
                        writer.write("Stream:" + stream);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving to file: " + filename, e);
        }
    }

    public void loadFromFile(String filename) {
        colleges.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            College currentCollege = null;
            Program currentProgram = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("College:")) {
                    String collegeName = line.substring(8).trim();
                    currentCollege = new College(collegeName);
                    colleges.add(currentCollege);
                }
                else if (line.startsWith("Program:") && currentCollege != null) {
                    String[] parts = line.substring(8).split(",");
                    String name = parts[0].trim();
                    int seats = Integer.parseInt(parts[1].trim());
                    int eligibility = Integer.parseInt(parts[2].trim());
                    double fee = Double.parseDouble(parts[3].trim());
                    currentProgram = new Program(name, seats, eligibility, fee);
                    currentCollege.addProgram(currentProgram);
                }
                else if (line.startsWith("Stream:") && currentProgram != null) {
                    String stream = line.substring(7).trim();
                    currentProgram.addAllowedStream(stream);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error loading from file: " + filename, e);
        }
    }
}







