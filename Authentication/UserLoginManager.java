package Authentication;
import ApplicationForm.Gender;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class UserLoginManager{
    public ArrayList<Users> users;
    private final String Credentials ="UserCredentials.txt";

    public UserLoginManager(){
        users=new ArrayList<>();
        transferData();
    }

    public void saveUsers (){
        try{
            File file = new File(Credentials);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,false);

            for (int i = 0; i <users.size() ; i++) {
                Users users1= users.get(i);
                writer.write(
                        users1.getFirstName() + "," + users1.getLastName() + "," + users1.getEmail() + "," + users1.getPassword() + "," +
                                users1.getSecurityAnswer() + "," + users1.getCnic() + "," + users1.getDateOfBirth() +
                                "," + users1.getGender() + "," + users1.getPhone() + "," + users1.getUserID() + "\n"
                );

            }
            writer.close();
        }

         catch (Exception e){
            e.printStackTrace();
        }
    }

    public void transferData() {
        int maxId =0;
        try {
            File file = new File(Credentials);
            if (!file.exists()) {
                return;
            }

            Scanner readFile = new Scanner(file);
            while (readFile.hasNextLine()) {
                String line = readFile.nextLine();
                String[] parts = line.split(",");
                Users users1 = new Users(
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],
                        LocalDate.parse(parts[6]), Gender.valueOf(parts[7]), parts[8], parts[9]
                );
                users.add(users1);

                /*
                Code below is for making sure that id count is continued each time code is run.
                It should not start from 0 each time program is run
                 */

                String userID = parts[9];
                int idNum = Integer.parseInt(userID.substring(4));
                if (idNum >= maxId) {
                    maxId = idNum + 1;
                }

            }
            Users.idCounter=maxId;
            readFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String forgetPassword(String email, String newPassword,String securityAnswer) {
        for (int i = 0; i < users.size(); i++) {
            Users users1 = users.get(i);
            if (users1.getEmail().equals(email)) {
                if (users1.getSecurityAnswer().equals(securityAnswer)) {
                    users1.setPassword(newPassword);
                    saveUsers();
                    return "Password reset successful!";
                }
                else {
                    return "Incorrect security answer.";
                }
            }
        }
        return "Email not found.";
    }



}
