package Applicant;

import AdminSetup.College.College;
import AdminSetup.College.CollegeManager;
import AdminSetup.Program.Program;
import AdminSetup.Program.ProgramManager;
import Authentication.Gender;
import Authentication.Users;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantManager {


    public static void saveToFile(ApplicationFormData app, File file) {
        try (FileWriter writer = new FileWriter(file, true)) {
            String line = String.join(",",
                    app.getApplicationId(),
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
                    app.getSelectedCollege() != null ? app.getSelectedCollege().getName() : "N/A"
            );
            writer.write(line + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
//
    public static boolean hasAppliedBefore(List<ApplicationFormData> existingApplications, ApplicationFormData newApp) {
        for (ApplicationFormData app : existingApplications) {
            if (app.getUsers().equals(newApp.getUsers()) &&
                    app.getSelectedProgram().getName().equalsIgnoreCase(newApp.getSelectedProgram().getName())) {
                return true;
            }
        }
        return false;
    }




    public static ArrayList<ApplicationFormData> loadApplicantsFromFile(Applicant applicant,File file) throws FileNotFoundException {
        ArrayList<ApplicationFormData> applicantData = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            File file1 = new File(file.getName());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                String applicationID = parts[0];
                String address = parts[1];
                String board10 =parts[2];
                String year10= parts[3];
                String percent10=parts[4];
                String stream10=parts[5];
                String board12=parts[6];
                String year12=parts[7];
                String percent12=parts[8];
                String stream12=parts[9];
                String selectedProgram=parts[10];
                String selectedCollege=parts[11];

                ProgramManager programManager = new ProgramManager();
                CollegeManager collegeManager = new CollegeManager();

                Program program= programManager.getProgramByName(selectedProgram);
                College college= collegeManager.getCollegeByName(selectedCollege);

                ApplicationFormData applicationFormData = new ApplicationFormData(
                        applicationID,applicant,address,board10,year10,percent10,stream10,
                        board12,year12,percent12,stream12,program,college)
                        ;

//                applicant.setSelectedCollege(selectedCollege);
//                applicant.setSelectedProgram(selectedProgram);
//                applicant.setApplicationStatus(appStatus);
//                applicant.setFeePaid(feePaid);
//                applicant.setScholarshipApplied(scholarshipApplied);
//                applicant.setAnnualIncome(annualIncome);

                applicantData.add(applicationFormData);

            }
        }
        return applicantData;
    }

}


