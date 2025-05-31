package Applicant;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;
import AdminSetup.Program.Program;
import AdminSetup.Program.ProgramManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantManager {

    private static final String APPLICATION_FILE = "all_applications.txt";


    public static void saveToFile(ApplicationFormData app) {
        try (FileWriter writer = new FileWriter(APPLICATION_FILE, true)) {
            String line = String.join(",",
                    app.getApplicationId(),
                    app.getUsers() != null ? app.getUsers().getFirstName() : "Unknown",
                    app.getAddress(),
                    app.getBoard10(),
                    app.getYear10(),
                    app.getPercent10(),
                    app.getStream10(),
                    app.getBoard12(),
                    app.getYear12(),
                    app.getPercent12(),
                    app.getStream12(),
                    app.getSelectedProgram() != null ? app.getSelectedProgram().getName() : "N/A",
                    app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A",
                    app.getEmail(),
                    app.getStatus() != null ? app.getStatus().name() : "UNKNOWN",
                    app.getTestSchedule() != null ? app.getTestSchedule() : "null",
                    app.getTestScore() != null ? app.getTestScore() : "null"
            );
            writer.write(line + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


    public static boolean hasAppliedBefore(ArrayList<ApplicationFormData> existingApplications, ApplicationFormData newApp) {
        for (ApplicationFormData app : existingApplications) {
            boolean sameUser = app.getUsers().equals(newApp.getUsers());
            boolean sameProgram = app.getSelectedProgram() != null && newApp.getSelectedProgram() != null &&
                    app.getSelectedProgram().getName().equalsIgnoreCase(newApp.getSelectedProgram().getName());
            boolean sameCollege = app.getSelectedCollege() != null && newApp.getSelectedCollege() != null &&
                    app.getSelectedCollege().getName().equalsIgnoreCase(newApp.getSelectedCollege().getName());

            if (sameUser && (sameProgram || sameCollege)) {
                return true;
            }
        }
        return false;
    }


    public static ArrayList<ApplicationFormData> loadAllApplications() {
        ArrayList<ApplicationFormData> applications = new ArrayList<>();
        File file = new File(APPLICATION_FILE);

        if (!file.exists()) return applications;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

//                if (parts.length < 17) continue;

                String applicationID = parts[0];
                String fullName = parts[1];  // Can be used to lookup User if needed
                String address = parts[2];
                String board10 = parts[3];
                String year10 = parts[4];
                String percent10 = parts[5];
                String stream10 = parts[6];
                String board12 = parts[7];
                String year12 = parts[8];
                String percent12 = parts[9];
                String stream12 = parts[10];
                String selectedProgramName = parts[11];
                String selectedCollegeName = parts[12];
                String email = parts[13];
                String statusStr = parts[14];
                String testSchedule = parts[15];
                String testScore = parts[16];

                Program program = null;
                if (!"N/A".equalsIgnoreCase(selectedProgramName)) {
                    program = new ProgramManager().getProgramByName(selectedProgramName);
                }
                College college = new CollegeManager().getCollegeByName(selectedCollegeName);


                Applicant user= null;

                ApplicationFormData app = new ApplicationFormData(
                        applicationID,
                        user,
                        address,
                        board10, year10, percent10, stream10,
                        board12, year12, percent12, stream12,
                        program, college,email
                );

                app.setEmail(email);
                app.setTestSchedule(testSchedule);
                app.setTestScore(testScore);

                try {
                    app.setStatus(Status.valueOf(statusStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    app.setStatus(Status.SUBMITTED);  // default fallback
                }

                applications.add(app);
            }
        } catch (IOException e) {
            System.out.println("Error reading application file: " + e.getMessage());
        }

        return applications;
    }
    public static ArrayList<ApplicationFormData> getApplicationsByUserEmail(String email) {
        ArrayList<ApplicationFormData> allApps = loadAllApplications();
        ArrayList<ApplicationFormData> userApps = new ArrayList<>();

        for (ApplicationFormData app : allApps) {
            if (app.getEmail() != null && app.getEmail().equalsIgnoreCase(email)) {
                userApps.add(app);
            }
        }

        return userApps;
    }
}
