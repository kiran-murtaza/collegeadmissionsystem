package AdminSetup;

import java.util.ArrayList;
import java.util.List;

public class ProgramManager {
    private List<Program> programList;

    public ProgramManager() {
        programList = new ArrayList<>();
    }

    public void addProgram(String name, int seats, int eligibility) {
        programList.add(new Program(name, seats, eligibility));
    }

    public List<Program> getAllPrograms() {
        return programList;
    }

    public void removeProgram(String name) {
        programList.removeIf(p -> p.getName().equalsIgnoreCase(name));
    }

    public Program getProgramByName(String name) {
        for (Program p : programList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

}
