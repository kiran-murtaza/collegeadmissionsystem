package Authentication;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import Applicant.

public class UserLoginManager {

    private final String CREDENTIALS_FILE = "UserCredentials.txt";
    private ArrayList<Users> users;

    public UserLoginManager() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    // Load users from file - all are Applicants here
    private void loadUsersFromFile() {
        try {
            File file = new File(CREDENTIALS_FILE);
            if (!file.exists()) {
                return; // no users yet
            }

            Scanner scanner = new Scanner(file);
            int maxId = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                // Assuming file order: firstName,lastName,email,password,securityAnswer,cnic,dob,gender,phone,userID
                Applicant applicant = new Applicant(
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                        LocalDate.parse(parts[6]), Gender.valueOf(parts[7]), parts[8], parts[9]
                );

                users.add(applicant);

                // Track max ID to continue id count if needed
                String userID = parts[9];
                int idNum = Integer.parseInt(userID.substring(4)); // assuming "USER" prefix
                if (idNum >= maxId) {
                    maxId = idNum + 1;
                }
            }

            Users.idCounter = maxId;
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save current users list to file
    public void saveUsersToFile() {
        try {
            File file = new File(CREDENTIALS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file, false);

            for (Users user : users) {
                writer.write(String.join(",",
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getSecurityAnswer(),
                        user.getCnic(),
                        user.getDateOfBirth().toString(),
                        user.getGender().name(),
                        user.getPhone(),
                        user.getUserID()
                ) + "\n");
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login by email and password, return the user if found, else null
    public Users login(String email, String password) {
        for (Users user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Password reset by email and security answer
    public String resetPassword(String email, String newPassword, String securityAnswer) {
        for (Users user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                if (user.getSecurityAnswer().equalsIgnoreCase(securityAnswer)) {
                    user.setPassword(newPassword);
                    saveUsersToFile();
                    return "Password reset successful!";
                } else {
                    return "Incorrect security answer.";
                }
            }
        }
        return "Email not found.";
    }

    // Add new user (Applicant)
    public void addUser(Applicant applicant) {
        users.add(applicant);
        saveUsersToFile();
    }

}
