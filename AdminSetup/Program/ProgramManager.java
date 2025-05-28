package AdminSetup.Program;


import java.util.ArrayList;
import java.util.List;

public class ProgramManager {
    private List<Program> programList;

    public ProgramManager() {
        programList = new ArrayList<>();
    }
    public boolean addProgram(String name, int seats, int eligibility , double fees) {
        for (Program p : programList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        Program newProgram = new Program(name, seats, eligibility, fees);
        programList.add(newProgram);

        return true;
    }

    public List<Program> getAllPrograms() {
        return programList;
    }

    public void removeProgram(String name) {
        programList.removeIf(p -> p.getName().equalsIgnoreCase(name));

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

    public void addProgram(Program program) {
        programList.add(program);
    }

}


