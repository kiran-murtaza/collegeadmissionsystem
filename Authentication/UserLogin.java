package Authentication;

import Authentication.LoginFrame;
import Authentication.UserLoginManager;
import Authentication.Users;

import java.util.ArrayList;

public class UserLogin extends UserLoginManager {
    public UserLogin(){
        transferData();
    }

    public Users login(String email, String pass){
        for (int i = 0; i < users.size(); i++) {
            if(email.equals(users.get(i).getEmail()) && pass.equals(users.get(i).getPassword()) ){
                return users.get(i);
            }
        }
        return null;
    }


    public static void main(String[] args) {
        new LoginFrame();
    }
}
