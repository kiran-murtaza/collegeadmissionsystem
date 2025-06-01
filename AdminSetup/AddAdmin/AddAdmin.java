package AdminSetup.AddAdmin;
import Authentication.AdminLogin;
import Authentication.Admins;
import Authentication.UserLoginManager;
import Authentication.Users;

import java.util.ArrayList;

public class AddAdmin extends AdminLogin {

    public String setAdmin(Admins currentAdmin,String email, String password){
        if (currentAdmin == null || !currentAdmin.isSuperAdmin()) {
            return "Only a super admin can add new admins.";
        }
        if (email.isEmpty() || password.isEmpty()) {
            return "All fields are required.";
        }

        if ( email.contains(",") || password.contains(",")) {
            return "Fields cannot contain commas.";
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            return "Invalid email address.";
        }

        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }

        for (Admins admins : adminsArrayList) {
            if (admins.getEmail().equals(email)) {
                return "email already exists.";
            }
        }

        Admins admins = new Admins(
                email, password
                // new admin is not super admin by default

        );

        adminsArrayList.add(admins);
        saveAdmin();
        return "new admin added";

    }

}
