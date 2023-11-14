package ed.service.messaging.utils;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String extractToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

}
