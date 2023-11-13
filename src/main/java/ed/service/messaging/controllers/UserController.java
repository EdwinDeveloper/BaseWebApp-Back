package ed.service.messaging.controllers;

import com.google.zxing.WriterException;
import ed.service.messaging.config.SpringApplicationContext;
import ed.service.messaging.dto.LoginDTO;
import ed.service.messaging.dto.UserDTO;
import ed.service.messaging.entity.jpa.User;
import ed.service.messaging.repository.UserRepository;
import ed.service.messaging.security.TFA.AuthQRCodeProvider;
import ed.service.messaging.security.TFA.TFAService;
import ed.service.messaging.services.EncryptionService;
import ed.service.messaging.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/1.0/user")
public class UserController extends AbstractController{

    private EncryptionService encryptionService;

    private void injectDependencies() {
        if (encryptionService == null) {
            encryptionService = SpringApplicationContext.getBean(EncryptionService.class);
        }
    }

    private UserRepository userRepository;
    private AuthQRCodeProvider authQRCodeProvider;
    private TFAService tfaService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserRepository userRepository,
            AuthQRCodeProvider authQRCodeProvider,
            TFAService tfaService,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.authQRCodeProvider = authQRCodeProvider;
        this.tfaService = tfaService;
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Use to verify the credentials
     */
    @PostMapping(value = "/verifyCredentials")
    public Map<String, Object> verifyCredentials(@RequestBody LoginDTO loginDTO) throws IOException, WriterException {

        List<String> errorValidation = new ArrayList<>();

        if(loginDTO.getEmail() == null){
            errorValidation.add("email is required");
        }

        if(loginDTO.getPassword() == null){
            errorValidation.add("password is required");
        }

        if(!errorValidation.isEmpty()){
            return badRequest(errorValidation);
        }

        Map<String, Object> response = new HashMap<>();

        Optional<User> userExist = userRepository.findByEmail(loginDTO.getEmail());

        if(!userExist.isPresent()){
            return badRequest("User not exists");
        }

        if(userExist.get().getTfa() == null){
            String TFAKey = TFAService.newGoogleAuthenticator().createCredentials().getKey();
            userExist.get().setTfa(TFAKey);
            userRepository.save(userExist.get());
            String imageQR = AuthQRCodeProvider.generateQRCodeDataUrl(TFAKey);
            response.put("tfaQR", imageQR);
        }

        response.put("verified", true);

        return ok(response);

    }

    /**
     * Use to create the user record
     */
    @PostMapping(value = "/create")
    public Map<String, Object> createUser(@RequestBody UserDTO userDTO) throws IOException, WriterException {

        List<String> errorValidation = new ArrayList<>();

        if(userDTO.getFirstName() == null){
            errorValidation.add("firstName is required");
        }

        if(userDTO.getLastNameFirst() == null){
            errorValidation.add("lastNameFirst is required");
        }

        if(userDTO.getLastNameSecond() == null){
            errorValidation.add("lastNameSecond is required");
        }

        if(userDTO.getEmail() == null){
            errorValidation.add("email is required");
        }

        if(userDTO.getPassword() == null){
            errorValidation.add("Password is required");
        }

        if(!errorValidation.isEmpty()){
            return badRequest(errorValidation);
        }

        if(!PasswordUtil.isPasswordValid(userDTO.getPassword())){
            return badRequest("Invalid password, it needs contains 12 caracter's, one uppercase, a lower case and a special caracter");
        }

        Optional<User> userExist = userRepository.findByEmail(userDTO.getEmail());

        if(userExist.isPresent()){
            return badRequest("Invalid data, email already in the system");
        }

        User user = new User(userDTO, encodePassword(userDTO.getPassword()));

        userRepository.save(user);

        return ok("User Created");

    }

}
