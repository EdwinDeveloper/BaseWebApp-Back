package ed.service.messaging.controllers;

import ed.service.messaging.config.SpringApplicationContext;
import ed.service.messaging.dto.UserDTO;
import ed.service.messaging.entity.jpa.User;
import ed.service.messaging.repository.UserRepository;
import ed.service.messaging.services.EncryptionService;
import ed.service.messaging.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Use to create the user record
     */
    @PostMapping(value = "/create")
    public Map<String, Object> createUser(@RequestBody UserDTO userDTO){

        if(userDTO.getFirstName() == null){
            return badRequest("firstName is required");
        }

        if(userDTO.getLastNameFirst() == null){
            return badRequest("lastNameFirst is required");
        }

        if(userDTO.getLastNameSecond() == null){
            return badRequest("lastNameSecond is required");
        }

        if(userDTO.getEmail() == null){
            return badRequest("email is required");
        }

        if(userDTO.getPassword() == null){
            return badRequest("Password is required");
        }

        if(!PasswordUtil.isPasswordValid(userDTO.getPassword())){
            return badRequest("Invalid password, it needs contains 12 caracter's, one uppercase, a lower case and a special caracter");
        }

        Optional<User> userExist = userRepository.findByEmail(userDTO.getEmail());

        if(userExist.isPresent()){
            return badRequest("Invalid data, email already in the system");
        }

        User user = new User(userDTO);

        userRepository.save(user);

        return ok("User saved");

    }

}
