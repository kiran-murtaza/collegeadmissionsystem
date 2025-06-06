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
                    nullSafe(app.getApplicationId()),
                    nullSafe(app.getUsers() != null ? app.getUsers().getFirstName() : "Unknown"),
                    nullSafe(app.getAddress()),
                    nullSafe(app.getBoard10()),
                    nullSafe(app.getYear10()),
                    nullSafe(app.getPercent10()),
                    nullSafe(app.getStream10()),
                    nullSafe(app.getBoard12()),
                    nullSafe(app.getYear12()),
                    nullSafe(app.getPercent12()),
                    nullSafe(app.getStream12()),
                    nullSafe(app.getSelectedProgram() != null ? app.getSelectedProgram() : "N/A"),
                    nullSafe(app.getSelectedCollege() != null ? app.getSelectedCollege() : "N/A"),
                    nullSafe(app.getUsers().getEmail()),
                    nullSafe(app.getStatus() != null ? app.getStatus().name() : "UNKNOWN"),
                    nullSafe(app.getTestSchedule()),
                    nullSafe(app.getTestScore())
            );
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to avoid null values causing issues
    private static String nullSafe(String value) {
        return value == null ? "" : value;
    }



    public static boolean hasAppliedBefore(ArrayList<ApplicationFormData> existingApplications, ApplicationFormData newApp) {
        for (ApplicationFormData app : existingApplications) {
            boolean sameUser = app.getUsers().equals(newApp.getUsers());
            boolean sameProgram = app.getSelectedProgram() != null && newApp.getSelectedProgram() != null &&
                    app.getSelectedProgram().equalsIgnoreCase(newApp.getSelectedProgram());
            boolean sameCollege = app.getSelectedCollege() != null && newApp.getSelectedCollege() != null &&
                    app.getSelectedCollege().equalsIgnoreCase(newApp.getSelectedCollege());

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

                if (parts.length < 17) {
                    // Skip invalid lines
                    continue;
                }

                String applicationID = parts[0];
                String fullName = parts[1];  // You may lookup user by name/email if needed
                String address = parts[2];
                String board10 = parts[3];
                String year10 = parts[4];
                String percent10 = parts[5];
                String stream10 = parts[6];
                String board12 = parts[7];
                String year12 = parts[8];
                String percent12 = parts[9];
                String stream12 = parts[10];
                String selectedProgramName = parts[11];  // keep as String
                String selectedCollegeName = parts[12];  // keep as String
                String email = parts[13];
                String statusStr = parts[14];
                String testSchedule = parts[15];
                String testScore = parts[16];

                Applicant user = null;  // You may implement user lookup if needed

                ApplicationFormData app = new ApplicationFormData(
                        applicationID,
                        user,
                        address,
                        board10, year10, percent10, stream10,
                        board12, year12, percent12, stream12,
                        selectedProgramName,
                        selectedCollegeName,
                        email
                );

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

    public static void updateApplicationStatus(String applicationId, Status newStatus) {
        ArrayList<ApplicationFormData> allApps = loadAllApplications();

        for (ApplicationFormData app : allApps) {
            if (app.getApplicationId().equals(applicationId)) {
                app.setStatus(newStatus);
                break;
            }
        }

        // Now overwrite the file with updated list
        try (FileWriter writer = new FileWriter(APPLICATION_FILE, false)) { // overwrite
            for (ApplicationFormData app : allApps) {
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
                        app.getSelectedProgram() != null ? app.getSelectedProgram() : "N/A",
                        app.getSelectedCollege() != null ? app.getSelectedCollege() : "N/A",
                        app.getEmail(),
                        app.getStatus().name(),
                        app.getTestSchedule() != null ? app.getTestSchedule() : "null",
                        app.getTestScore() != null ? app.getTestScore() : "null"
                );
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating application status: " + e.getMessage());
        }
    }

    public static List<String> getAllApplicantIds() {
        List<String> ids = new ArrayList<>();
        try {
            ArrayList<ApplicationFormData> apps = loadAllApplications();
            for (ApplicationFormData app : apps) {
                ids.add(app.getApplicationId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

}
