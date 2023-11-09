package ed.service.messaging.controllers;

import ed.service.messaging.dto.UserDTO;
import ed.service.messaging.utils.PasswordUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/1.0/user")
public class UserController extends AbstractController{

    @PostMapping(value = "/create")
    public Map<String, Object> createUser(@RequestBody UserDTO userDTO){

        if(PasswordUtil.isPasswordValid(userDTO.getPassword())){
            return badRequest("Invalid password, it needs contains 12 caracter's, one uppercase, a lower case and a special caracter");
        }

        return ok("todo bien");

    }

}
