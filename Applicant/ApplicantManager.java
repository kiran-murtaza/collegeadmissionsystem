package Applicant;

import Authentication.Gender;
import Authentication.Users;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantManager {


    public static void saveToFile(ApplicationFormData app, File file) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("applications.txt", true);
            writer.write(app.toString() + "\n");
            writer.close();

        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean hasAppliedBefore(List<ApplicationFormData> existingApplications, ApplicationFormData newApp) {
        for (ApplicationFormData app : existingApplications) {
            if (app.getUsers().equals(newApp.getUsers()) &&
                    app.getSelectedProgram().getName().equalsIgnoreCase(newApp.getSelectedProgram().getName())) {
                return true;
            }
        }
        return false;
    }




    public ArrayList<Applicant> loadApplicantsFromFile(File file) throws FileNotFoundException {
        ArrayList<Applicant> applicants = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            File file1 = new File(file.getName());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                String userID = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                String email = parts[3];
                String cnic = parts[4];
                String phone = parts[5];
                LocalDate dob = LocalDate.parse(parts[6]);
                Gender gender = Gender.valueOf(parts[7]);
                String selectedCollege = parts[8];
                String selectedProgram = parts[9];
//                ApplicationStatus appStatus = ApplicationStatus.valueOf(parts[10]);
                boolean feePaid = Boolean.parseBoolean(parts[11]);
                boolean scholarshipApplied = Boolean.parseBoolean(parts[12]);
                double annualIncome = Double.parseDouble(parts[13]);
                String address = parts[14];
                Applicant applicant = new Applicant(
                        firstName, lastName, email, "", cnic, phone, dob, gender, "", address
                );

//                applicant.setSelectedCollege(selectedCollege);
//                applicant.setSelectedProgram(selectedProgram);
//                applicant.setApplicationStatus(appStatus);
//                applicant.setFeePaid(feePaid);
//                applicant.setScholarshipApplied(scholarshipApplied);
//                applicant.setAnnualIncome(annualIncome);

                applicants.add(applicant);
            }
        }
        return applicants;
    }

}


