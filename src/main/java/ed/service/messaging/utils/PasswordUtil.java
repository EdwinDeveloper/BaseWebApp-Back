package ed.service.messaging.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {

    public static boolean isPasswordValid(String newPassword){
        String pattern = "(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{12,}";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(newPassword);
        return matcher.matches();
    }

}
