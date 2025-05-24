package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;


import java.util.ArrayList;
import java.util.List;

public class ProgramManager {
    private List<Program> programList;
    private CollegeManager collegeManager;

    public ProgramManager() {
        programList = new ArrayList<>();
    }
    public boolean addProgram(String name, int seats, int eligibilty) {
        College college = collegeManager.getCollegeByName(name);
        if (college != null) {
            for (Program p : college.getPrograms()) {
                if (p.getName().equalsIgnoreCase(name)) {
                    return false;
                }
            }
            Program newProgram = new Program(name, seats,eligibilty);
            college.addProgram(newProgram);
            return true;
        }
        return false;
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



}

