package AdminSetup.College;

import AdminSetup.Program.Program;

import java.util.ArrayList;
import java.util.List;
//
//public class College {
//    private final String name;
//    private final List<Program> programs = new ArrayList<>();
//
//    public College(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public List<Program> getPrograms() {
//        return programs;
//    }
//
//    public void addProgram(String name, int seatLimit) {
//        programs.add(new Program(name, seatLimit));
//    }
//
//    // Convert College to file format string
//    public String toFileFormat() {
//        String result = name;
//        for (Program p : programs) {
//            result += ";" + p.toFileFormat(); // Concatenate each program
//        }
//        return result;
//    }
//
//    // Load from a saved string line
//    public static College fromFileLine(String line) {
//        String[] parts = line.split(";");
//        College college = new College(parts[0]);
//        for (int i = 1; i < parts.length; i++) {
//            college.getPrograms().add(Program.fromFileLine(parts[i]));
//        }
//        return college;
//    }
//}
//...............................................................//
public class College {
    private String name;
    private ArrayList<Program> programs;
//
    public College(String name) {
        this.name = name;
        this.programs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrograms(ArrayList<Program> programs) {
        this.programs = programs;
    }

    public void addProgram(Program program) {
        this.programs.add(program);
    }

    public String toFileFormat() {
        String result = name;
        for (Program p : programs) {
            result += ";" + p.toFileFormat();
        }
        return result;
    }


    public static College fromFileLine(String line) {
        String[] parts = line.split(";");
        College college = new College(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            college.getPrograms().add(Program.fromFileLine(parts[i]));
        }
        return college;
    }

}
