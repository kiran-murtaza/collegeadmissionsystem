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

//    public void saveToFile(String filename) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (College c : colleges) {
//                bw.write(c.toFileFormat());
//                bw.newLine();
//            }
//        }
//    }
//
//    public void loadFromFile(String filename) throws IOException {
//        colleges.clear();
//        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                colleges.add(College.fromFileLine(line));
//            }
//        }
//    }
public void saveToFile(String filename) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
        for (College college : colleges) {
            bw.write("COLLEGE," + college.getName());
            bw.newLine();

            for (Program program : college.getPrograms()) {
                bw.write("PROGRAM," + program.getName() + "," + program.getSeats() + "," + program.getEligibility());
                bw.newLine();
            }
            bw.newLine();
        }
    }
}
    public void loadFromFile(String filename) throws IOException {
        colleges.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            College currentCollege = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    currentCollege = null;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts[0].equalsIgnoreCase("COLLEGE")) {
                    String collegeName = parts[1];
                    currentCollege = new College(collegeName);
                    colleges.add(currentCollege);
                } else if (parts[0].equalsIgnoreCase("PROGRAM") && currentCollege != null) {
                    String programName = parts[1];
                    int seats = Integer.parseInt(parts[2]);
                    int eligibility = Integer.parseInt(parts[3]);
                    currentCollege.addProgram(new Program(programName, seats, eligibility));
                }
            }
        }
    }


}







