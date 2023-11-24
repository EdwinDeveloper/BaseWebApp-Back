package ed.service.messaging.security.TFA;

import ed.service.messaging.exception.CustomHttpException;
import org.springframework.stereotype.Service;
import ed.service.messaging.entity.jpa.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorException;

@Service
public class TFAService {

    private static final String TFA_ISSUER = "OPM SPEI";
    private static final int TFA_DIGITS = 6;
    private static final int TFA_PERIOD = 30;
    private static final String TFA_ALGORITHM = "HmacSHA1";

    public static GoogleAuthenticator newGoogleAuthenticator() {
        return new GoogleAuthenticator();
    }

    public static void validateUserOtp(User user, String otp) throws CustomHttpException {

        if (otp == null || otp.isEmpty()) {
            throw new CustomHttpException(400, "Sin código OTP");
        }

        GoogleAuthenticator googleAuthenticator = newGoogleAuthenticator();
        try {
            if (!googleAuthenticator.authorize(user.getTfa(), Integer.parseInt(otp))) {
                throw new CustomHttpException(400, "Código OTP inválido");
            }
        } catch (GoogleAuthenticatorException e) {
            throw new CustomHttpException(500, "Error verificando el código OTP");
        }
    }

}
