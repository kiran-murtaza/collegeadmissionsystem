package Authentication;

public class Admins {
    String email;
    String password;

    public Admins(String email, String password){
        this.email=email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
