package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramManager {
    private List<Program> programList;
    private final String fileName = "programs.txt";

    public ProgramManager() {
        programList = new ArrayList<>();
    }
    public boolean addProgram(String name, int seats, int eligibility) {
        for (Program p : programList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        Program newProgram = new Program(name, seats, eligibility);
        programList.add(newProgram);
        try {
            saveToFile(fileName);
        } catch (IOException e) {
            System.out.println("Could not save program: " + e.getMessage());
        }
        return true;
    }

    public List<Program> getAllPrograms() {
        return programList;
    }

    public void removeProgram(String name) {
        programList.removeIf(p -> p.getName().equalsIgnoreCase(name));
        try {
            saveToFile(fileName);
        } catch (IOException e) {
            System.out.println("Could not update file: " + e.getMessage());
        }
    }


     public ArrayList<Program> getProgramsByStream(String stream) {
        ArrayList<Program> filtered = new ArrayList<>();
        for (Program p : programList) {
            if (p.isStreamAllowed(stream)) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    public Program getProgramByName(String name) {
        for (Program p : programList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Program p : programList) {
                bw.write(p.getName() + "," + p.getSeats() + "," + p.getEligibility() + "," + String.join("|", p.getAllowedStreams()));
                bw.newLine();
            }
        }
    }
    public void loadFromFile(String filename) throws IOException {
        programList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int seats = Integer.parseInt(parts[1]);
                int eligibility = Integer.parseInt(parts[2]);
                Program p = new Program(name, seats, eligibility);
                if (parts.length > 3) {
                    String[] streams = parts[3].split("\\|");
                    p.setAllowedStreams(new ArrayList<>(Arrays.asList(streams)));
                }
                programList.add(p);
            }
        }
    }


}
//class ProgramManager {
//    private List<Program> programList;
//    private final String fileName = "programs.txt";
//
//    public ProgramManager() {
//        programList = new ArrayList<>();
//        try {
//            loadFromFile(fileName);
//        } catch (IOException e) {
//            System.out.println("Could not load programs: " + e.getMessage());
//        }
//    }
//
//    public boolean addProgram(String name, int seats, int eligibility) {
//        for (Program p : programList) {
//            if (p.getName().equalsIgnoreCase(name)) {
//                return false;
//            }
//        }
//        Program newProgram = new Program(name, seats, eligibility);
//        programList.add(newProgram);
//        try {
//            saveToFile(fileName);
//        } catch (IOException e) {
//            System.out.println("Could not save program: " + e.getMessage());
//        }
//        return true;
//    }
//
//    public List<Program> getAllPrograms() {
//        return programList;
//    }
//
//    public void removeProgram(String name) {
//        programList.removeIf(p -> p.getName().equalsIgnoreCase(name));
//        try {
//            saveToFile(fileName);
//        } catch (IOException e) {
//            System.out.println("Could not update file: " + e.getMessage());
//        }
//    }
//
//    public ArrayList<Program> getProgramsByStream(String stream) {
//        ArrayList<Program> filtered = new ArrayList<>();
//        for (Program p : programList) {
//            if (p.isStreamAllowed(stream)) {
//                filtered.add(p);
//            }
//        }
//        return filtered;
//    }
//
//    public Program getProgramByName(String name) {
//        for (Program p : programList) {
//            if (p.getName().equalsIgnoreCase(name)) {
//                return p;
//            }
//        }
//        return null;
//    }
//
//    public void saveToFile(String filename) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (Program p : programList) {
//                bw.write(p.getName() + "," + p.getSeats() + "," + p.getEligibility() + "," + String.join("|", p.getAllowedStreams()));
//                bw.newLine();
//            }
//        }
//    }
//
//    public void loadFromFile(String filename) throws IOException {
//        programList.clear();
//        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                String name = parts[0];
//                int seats = Integer.parseInt(parts[1]);
//                int eligibility = Integer.parseInt(parts[2]);
//                Program p = new Program(name, seats, eligibility);
//                if (parts.length > 3) {
//                    String[] streams = parts[3].split("\\|");
//                    p.setAllowedStreams(new ArrayList<>(Arrays.asList(streams)));
//                }
//                programList.add(p);
//            }
//        }
//    }
//}

