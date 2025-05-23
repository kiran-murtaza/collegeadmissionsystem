package AdminSetup.Program;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;

import java.util.ArrayList;
import java.util.List;

public class ProgramManager {
    private List<Program> programList;


    public ProgramManager() {
        programList = new ArrayList<>();
    }

    public boolean addProgram(String name, int seats, int eligibility) {
        if (getProgramByName(name) == null) {
            programList.add(new Program(name, seats, eligibility));
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
//
}



    //....................................................................................//
//public class ProgramManager {
//    private final CollegeManager collegeManager;
//
//    public ProgramManager(CollegeManager collegeManager) {
//        this.collegeManager = collegeManager;
//    }
//
//    public boolean addProgram(String collegeName, String programName, int seatLimit) {
//        College college = collegeManager.getCollegeByName(collegeName);
//        if (college != null) {
//            for (Program p : college.getPrograms()) {
//                if (p.getName().equalsIgnoreCase(programName)) {
//                    return false; // Already exists
//                }
//            }
//            college.addProgram(programName, seatLimit);
//            return true;
//        }
//        return false;
//    }
//
//    public List<Program> getProgramsByCollege(String collegeName) {
//        College college = collegeManager.getCollegeByName(collegeName);
//        if (college != null) {
//            return college.getPrograms();
//        }
//        return new ArrayList<>();
//    }
//
//    public List<String> getProgramNames(String collegeName) {
//        List<String> names = new ArrayList<>();
//        for (Program p : getProgramsByCollege(collegeName)) {
//            names.add(p.getName());
//        }
//        return names;
//    }
//
//    public Program getProgram(String collegeName, String programName) {
//        for (Program p : getProgramsByCollege(collegeName)) {
//            if (p.getName().equalsIgnoreCase(programName)) {
//                return p;
//            }
//        }
//        return null;
//    }
//
//    public boolean setEligibility(String collegeName, String programName, int minScore) {
//        Program program = getProgram(collegeName, programName);
//        if (program != null) {
//            program.setEligibility(minScore);
//            return true;
//        }
//        return false;
//    }
//
//    public int getEligibility(String collegeName, String programName) {
//        Program program = getProgram(collegeName, programName);
//        if (program != null) {
//            return program.getEligibility();
//        }
//        return 0;
//    }
//}
