package AdminSetup.College;

import AdminSetup.Program.Program;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollegeManager {
    private final ArrayList<College> colleges;

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
}






//example
//


//ArrayList<String> medPrograms = new ArrayList<>();
//medPrograms.add("Medical");
//
//ArrayList<String> engPrograms = new ArrayList<>();
//engPrograms.add("Engineering");
//
//CollegeManager collegeManager = new CollegeManager();
//collegeManager.addCollege("C001", "Dow Medical College", medPrograms);
//collegeManager.addCollege("C002", "NED University", engPrograms);
//
//// To get colleges offering "Medical"
//ArrayList<College> medicalColleges = collegeManager.getCollegesByProgram("Medical");
//for (College c : medicalColleges) {
//        System.out.println(c.getName());
//        }





//...........................................//
//import java.io.*;
//        import java.util.ArrayList;
//import java.util.List;
//
//public class CollegeManager {
//    private final List<College> colleges = new ArrayList<>();
//
//    public void addCollege(String name) {
//        if (getCollegeByName(name) == null) {
//            colleges.add(new College(name));
//        }
//    }
//
//    public void addProgramToCollege(String collegeName, String programName, int seatLimit) {
//        College college = getCollegeByName(collegeName);
//        if (college != null) {
//            college.addProgram(programName, seatLimit);
//        }
//    }
//
//    public List<College> getAllColleges() {
//        return colleges;
//    }
//
//    public College getCollegeByName(String name) {
//        for (College c : colleges) {
//            if (c.getName().equalsIgnoreCase(name)) return c;
//        }
//        return null;
//    }
//
//    // Save colleges and their programs to file
//    public void saveToFile(String filename) throws IOException {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
//            for (College c : colleges) {
//                bw.write(c.toFileFormat());
//                bw.newLine();
//            }
//        }
//    }
//
//    // Load from file
//    public void loadFromFile(String filename) throws IOException {
//        colleges.clear();
//        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                colleges.add(College.fromFileLine(line));
//            }
//        }
//    }
//
//    // For dropdown: get list of college names
//    public List<String> getCollegeNames() {
//        List<String> names = new ArrayList<>();
//        for (College c : colleges) {
//            names.add(c.getName());
//        }
//        return names;
//    }
//
//    // For dropdown: get program names by college
//    public List<String> getProgramNames(String collegeName) {
//        College c = getCollegeByName(collegeName);
//        List<String> names = new ArrayList<>();
//        if (c != null) {
//            for (Program p : c.getPrograms()) {
//                names.add(p.getName());
//            }
//        }
//        return names;
//    }
//}

