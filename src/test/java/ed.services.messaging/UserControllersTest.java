package ed.services.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import ed.service.messaging.MessagingApplication;
import ed.service.messaging.controllers.UserController;
import ed.service.messaging.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = MessagingApplication.class)
//@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Test
    public void testRegisterUser() throws Exception{

        for (int x = 0; x < 10 ; x++ ){
            UserDTO randomUser = TestUtil.createRandomUser();
            // When
            mockMvc.perform(MockMvcRequestBuilders.post("/api/1.0/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(randomUser)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

}
