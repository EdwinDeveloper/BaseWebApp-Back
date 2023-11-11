package ed.service.messaging.security.TFA;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthQRCodeProvider{

    public String getMimeType() {
        return "image/png";
    }

    public static String generateQRCodeDataUrl(GoogleAuthenticatorKey key) throws WriterException, IOException {
        String otpAuthURL = generateOtpAuthURL(key);
        String imageBase64 = generateQRCodeBase64(otpAuthURL);
        return "data:image/png;base64," + imageBase64;
    }

    private static String generateOtpAuthURL(GoogleAuthenticatorKey key) {
        return "otpauth://totp/"
                + URLEncoder.encode("messaging", StandardCharsets.UTF_8)
                + ":" + URLEncoder.encode("messaging", StandardCharsets.UTF_8)
                + "?secret=" + key.getKey();
    }

    private static String generateQRCodeBase64(String text) throws WriterException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        writeToStream(bitMatrix, "PNG", outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static void writeToStream(BitMatrix bitMatrix, String format, ByteArrayOutputStream outputStream) throws IOException {
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
    }

}
