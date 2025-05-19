package Authentication;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminLogin {
    public ArrayList <Admins> adminsArrayList;

    public AdminLogin(){
        adminsArrayList= new ArrayList<>();
        transferData();
    }

    public boolean login(String email,String password){
        for (int i = 0; i <adminsArrayList.size() ; i++) {
            if(email.equals(adminsArrayList.get(i).getEmail()) && password.equals(adminsArrayList.get(i).getPassword()) ){
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
            FileWriter writer = new FileWriter(file,true);

            for (int i = 0; i <adminsArrayList.size() ; i++) {

                writer.write(adminsArrayList.get(i).getEmail()+ "," +
                        adminsArrayList.get(i).getPassword() + "\n");
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

                adminsArrayList.add(new Admins(parts[0], parts[1]));


            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }






}
