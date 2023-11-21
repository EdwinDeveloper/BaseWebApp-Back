package ed.service.messaging.controllers;

import com.google.zxing.WriterException;
import ed.service.messaging.config.SpringApplicationContext;
import ed.service.messaging.dto.LoginDTO;
import ed.service.messaging.dto.UserDTO;
import ed.service.messaging.dto.UserDTOR;
import ed.service.messaging.entity.jpa.User;
import ed.service.messaging.repository.UserRepository;
import ed.service.messaging.services.EncryptionService;
import ed.service.messaging.services.JWTService;
import ed.service.messaging.utils.PasswordUtil;
import ed.service.messaging.utils.TokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
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

    private final AuthenticationManager authenticationManager;

    private JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JWTService jwtService
    ){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean validateCurrentPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> health(){
        return ok("Service ok");
    }

    /**
     * Use to verify the credentials
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
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

        if(!validateCurrentPassword(loginDTO.getPassword(), userExist.get().getPassword())){
            return badRequest("Invalid password");
        }

        String jwt = jwtService.generateToken(userExist.get());

        response.put("verified", true);
        response.put("token", jwt);
        //response.put("token", jwt);

        return ok(response);

    }

    /**
     * Use to create the user record
     */
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> createUser(
            @RequestBody UserDTO userDTO
    ) throws IOException, WriterException {

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

    /**
     * Use to get all the users in system
     */
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getUsers(){

        Iterable<User> users = userRepository.findAll();

        List<UserDTOR> usersList = new ArrayList<>();

        users.forEach(user -> {
            UserDTOR userDTOR = new UserDTOR(user);
            usersList.add(userDTOR);
        });

        return ok(usersList);
    }

    /**
     * Use to delete one user by the id
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteUser(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        Claims extracted = jwtService.extractClaims(TokenUtil.extractToken(authorizationHeader));

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            return badRequest("User is not in the system");
        }

        if(extracted.getSubject().equals(user.get().getEmail())){
            return badRequest("You can't delete your own account");
        }

        userRepository.deleteById(id);

        return ok(204, "user deleted");
    }

    /**
     * Use to delete one user by the id
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateUser(
            @RequestBody UserDTOR userDTOR
    ) {

        Optional<User> user = userRepository.findById(userDTOR.getId());

        if(!user.isPresent()){
            return badRequest("User id is not valid or doesn't exist");
        }

        User userDB = user.get();
        List<String> errorValidation = new ArrayList<>();

        if(userDTOR.getEmail() == null){
            errorValidation.add("email is required");
        }

        if(userDTOR.getFirstName() == null){
            errorValidation.add("firstName is required");
        }

        if(userDTOR.getLastNameFirst() == null){
            errorValidation.add("lastNameFirst is required");
        }

        if(userDTOR.getLastNameSecond() == null){
            errorValidation.add("lastNameSecond is required");
        }

        if(!errorValidation.isEmpty()){
            return badRequest(errorValidation);
        }

        userDB.setFirstName(userDTOR.getFirstName());
        userDB.setLastNameFirst(userDTOR.getLastNameFirst());
        userDB.setLastNameSecond(userDTOR.getLastNameSecond());

        User userUpdated = userRepository.save(userDB);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "user update");
        response.put("user", userUpdated);

        return ok( response);
    }

}
