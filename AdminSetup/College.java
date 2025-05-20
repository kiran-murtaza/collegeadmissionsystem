package AdminSetup;


import java.util.ArrayList;

public class College {
    private String id;
    private String name;
    private ArrayList<String> programNames; // Using ArrayList

    public College(String id, String name, ArrayList<String> programNames) {
        this.id = id;
        this.name = name;
        this.programNames = programNames;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getProgramNames() {
        return programNames;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgramNames(ArrayList<String> programNames) {
        this.programNames = programNames;
    }

    @Override
    public String toString() {
        return name + " (Programs: " + programNames + ")";
    }
}

