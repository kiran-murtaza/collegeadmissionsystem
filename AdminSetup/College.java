package AdminSetup;

import java.util.ArrayList;
//
public class College {
    private String name;
    private ArrayList<Program> programs;

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
}
