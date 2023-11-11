package ed.service.messaging.security.encrypt;

import java.security.SecureRandom;

public class EncryptorHandler {

    public static String createAESKey(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] aesKeyBytes = new byte[32];
        secureRandom.nextBytes(aesKeyBytes);
        StringBuilder aesKeyHex = new StringBuilder();
        for(byte b: aesKeyBytes){
            aesKeyHex.append(String.format("%02x", b));
        }
        return aesKeyHex.toString();
    }

}
