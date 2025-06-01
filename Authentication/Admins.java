package Authentication;

public class Admins {
    String email;
    String password;
    private boolean isSuperAdmin;  // new flag


    public Admins(String email, String password) {
        this.email = email;
        this.password = password;
        this.isSuperAdmin = false;
    }

    // getters and setters
    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.isSuperAdmin = superAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
