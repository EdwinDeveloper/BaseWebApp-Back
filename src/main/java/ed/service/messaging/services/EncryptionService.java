package ed.service.messaging.services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    public StandardPBEStringEncryptor encryptor;

    public EncryptionService(@Value("ed.service.messagging.security.encryptionKey") String secretKey ){
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(secretKey);
        encryptor.setAlgorithm("PBEWithMD5AndDES");
    }

    public String encrypt(String data){
        return encryptor.encrypt(data);
    }

    public String decrypt(String encryptedData){
        try{
            return encryptor.decrypt(encryptedData);
        }catch (EncryptionOperationNotPossibleException e){
            return null;
        }
    }

}
