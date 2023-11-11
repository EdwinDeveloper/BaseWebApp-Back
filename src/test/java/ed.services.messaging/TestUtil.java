package ed.services.messaging;

import ed.service.messaging.dto.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class TestUtil {

    public static UserDTO createRandomUser() {
        UserDTO user = new UserDTO();
        user.setFirstName(generateRandomString());
        user.setLastNameFirst(generateRandomString());
        user.setLastNameSecond(generateRandomString());
        user.setEmail(generateRandomEmail());
        user.setPassword(generateRandomPassword());
        return user;
    }

    private static String generateRandomString() {
        Random random = new Random();
        int randomNumberInRange = random.nextInt(11) + 10;
        return RandomStringUtils.randomAlphabetic(randomNumberInRange);
    }

    private static String generateRandomEmail() {
        return "edwindeveloper@outlook.com";
    }

    private static String generateRandomPassword() {
        return "123ERFM022*fmd";
    }

}
