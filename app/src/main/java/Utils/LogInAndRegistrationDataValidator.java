package Utils;

import java.util.regex.Pattern;
public class LogInAndRegistrationDataValidator {

    //password pattern skeleton
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    // A placeholder username validation check
    public boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return EMAIL_PATTERN.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    //password validation
    public boolean isPasswordValid(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        else return PASSWORD_PATTERN.matcher(password).matches();
    }

    // compare the 2 passwords upon registration
    public boolean doPasswordsCoincide(String password1, String password2){
        return password1.equals(password2);
    }

}
