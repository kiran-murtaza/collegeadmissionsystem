package AdminSetup.College;

import AdminSetup.Program.Program;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollegeManager {
    private final ArrayList<College> colleges;
    private final String fileName = "colleges.txt";

    public CollegeManager() {
        colleges= new ArrayList<>();

    }

    public void addCollege(String name) {
        colleges.add(new College( name));
    }

    public boolean removeCollegeByName(String name) {
        return colleges.removeIf(c -> c.getName().equalsIgnoreCase(name));
    }

    public ArrayList<College> getAllColleges() {
        return colleges;
    }


    public College getCollegeByName(String collegeName) {
        for (College c : colleges) {
            if (c.getName().equalsIgnoreCase(collegeName)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<College> getCollegesByProgramName(String programName) {
        ArrayList<College> result = new ArrayList<>();
        for (College c : colleges) {
            for (Program p : c.getPrograms()) {
                if (p.getName().equalsIgnoreCase(programName)) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }


    public boolean updateCollegeProgramsByName(String collegeName, ArrayList<Program> newPrograms) {
        for (College c : colleges) {
            if (c.getName().equalsIgnoreCase(collegeName)) {
                c.setPrograms(newPrograms);
                return true;
            }
        }
        return false;
    }

public void saveToFile(String filename) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
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
    writer.close();
}

    public void loadFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        College currentCollege = null;
        Program currentProgram = null;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("College:")) {
                String collegeName = line.substring(8).trim();
                currentCollege = new College(collegeName);
                colleges.add(currentCollege);
            } else if (line.startsWith("Program:")) {
                String[] parts = line.substring(8).split(",");
                String name = parts[0].trim();
                int seats = Integer.parseInt(parts[1].trim());
                int eligibility = Integer.parseInt(parts[2].trim());
                double fee = Double.parseDouble(parts[3].trim());
                currentProgram = new Program(name, seats, eligibility, fee);
                currentCollege.addProgram(currentProgram);
            } else if (line.startsWith("Stream:")) {
                String stream = line.substring(7).trim();
                currentProgram.addAllowedStream(stream);
            }
        }
        reader.close();
    }



}







