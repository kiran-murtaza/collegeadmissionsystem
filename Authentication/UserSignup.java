package Authentication;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserSignup extends UserLoginManager {

    public String signUp(String firstName, String lastName, String password, String securityAnswer,
                         String cnic, LocalDate dob, Gender gender,
                         String phone, String email)  {

        if (password.isEmpty() || securityAnswer.isEmpty() ||
                cnic.isEmpty() || dob == null || gender == null ||
                phone.isEmpty() || email.isEmpty()) {
            return "All fields are required.";
        }

        if (firstName.contains(",") || lastName.contains(",") ||
                password.contains(",") || securityAnswer.contains(",") || cnic.contains(",") ||
                phone.contains(",") || email.contains(",")) {
            return "commas";
        }
        if (firstName.contains(" ") || lastName.contains(" ") ||
                password.contains(" ") || securityAnswer.contains(" ") || cnic.contains(" ") ||
                phone.contains(" ") || email.contains(" ")) {
            return "Field can not be empty";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters.";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }

        if (!password.matches(".*[!@#$%^&*()\\-+=<>?{}\\[\\]~].*")) {
            return "Password must contain at least one special character.";
        }

        if (securityAnswer.trim().matches("\\d+")) {
            return "Enter a valid pet name";
        }


        for (Users u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return "Email already exists.";
            }
        }

        if (!cnic.matches("\\d{5}-\\d{7}-\\d")) {
            return "Invalid CNIC format. Use XXXXX-XXXXXXX-X.";
        }

        if (ChronoUnit.YEARS.between(dob, LocalDate.now()) < 18) {
            return "You must be at least 18 years old to sign up.";
        }

        if (!phone.matches("^\\+\\d{10,15}$")) {
            return "Invalid phone number format. Include country code.";
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            return "Invalid email address.";
        }


        Users users1 = new Users( firstName, lastName, email, password,
                securityAnswer,  cnic,  dob,
                gender, phone
        );

        users.add(users1);
        saveUsers();
        return "Sign up successful!";
    }
}
