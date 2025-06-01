package Authentication;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminLogin {
    public ArrayList <Admins> adminsArrayList;
    private static Admins currentAdmin;

    public static void setCurrentAdmin(Admins admin) {
        currentAdmin = admin;
    }

    public static Admins getCurrentAdmin() {
        return currentAdmin;
    }

    public AdminLogin(){
        adminsArrayList= new ArrayList<>();
        transferData();
    }

    public boolean login(String email,String password){
        for (Admins admin : adminsArrayList) {
            if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
                setCurrentAdmin(admin);
                return true;
            }
        }
        return false;
    }


    public void saveAdmin (){
        try{
            File file = new File("AdminCredentials.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file,false);
            for (int i = 0; i <adminsArrayList.size() ; i++) {

                writer.write(adminsArrayList.get(i).getEmail()+ "," +
                        adminsArrayList.get(i).getPassword() + "," +adminsArrayList.get(i).isSuperAdmin()  +"\n" );
            }
            writer.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void transferData () {
        try{
            File file = new File("AdminCredentials.txt");
            if(!file.exists()){
                return;
            }

            Scanner readFile = new Scanner(file);
            while (readFile.hasNextLine()){
                String line = readFile.nextLine();
                String [] parts = line.split(",");

                Admins admin = new Admins(parts[0], parts[1]);

                if (parts.length >= 3) {
                    admin.setSuperAdmin(Boolean.parseBoolean(parts[2]));
                }

                adminsArrayList.add(admin);

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        if (adminsArrayList.isEmpty()) {
            Admins superAdmin = new Admins("superadmin@gmail.com", "superpassword");
            superAdmin.setSuperAdmin(true);
            adminsArrayList.add(superAdmin);
            saveAdmin();
            System.out.println("Default super admin created.");
        }
    }






}
