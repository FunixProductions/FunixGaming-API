package fr.funixgaming.api.server.user;

import com.google.gson.Gson;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserResourceAuth {

    private final MockMvc mockMvc;
    private final Gson gson;
    private final UserRepository userRepository;

    @Autowired
    public TestUserResourceAuth(MockMvc mockMvc,
                                UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.gson = new Gson();

        this.userRepository.deleteAll();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("oui");
        creationDTO.setPasswordConfirmation("oui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO userDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(creationDTO.getUsername(), userDTO.getUsername());
        assertEquals(creationDTO.getEmail(), userDTO.getEmail());
        assertEquals(userDTO.getRole(), UserRole.USER);
    }

    @Test
    public void testRegisterFailPasswordsMismatch() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("oui2");
        creationDTO.setPasswordConfirmation("oui");

        this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(creationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final UserDTO account = createAccount();
        final UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUsername(account.getUsername());
        loginDTO.setPassword("oui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO tokenDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertEquals(tokenDTO.getUser(), account);
        assertNotNull(tokenDTO.getToken());
        assertNotNull(tokenDTO.getExpirationDate());
    }

    private UserDTO createAccount() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("oui");
        creationDTO.setPasswordConfirmation("oui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        return gson.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
    }

}
