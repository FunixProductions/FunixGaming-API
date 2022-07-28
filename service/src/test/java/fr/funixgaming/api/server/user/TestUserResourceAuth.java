package fr.funixgaming.api.server.user;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import fr.funixgaming.api.server.utils.JsonHelper;
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
    private final UserTestComponent userTestComponent;
    private final JsonHelper jsonHelper;

    @Autowired
    public TestUserResourceAuth(MockMvc mockMvc,
                                UserRepository userRepository,
                                UserTokenRepository userTokenRepository,
                                UserTestComponent userTestComponent,
                                JsonHelper jsonHelper) {
        this.mockMvc = mockMvc;
        this.userTestComponent = userTestComponent;
        this.jsonHelper = jsonHelper;

        userTokenRepository.deleteAll();
        userRepository.deleteAll();
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
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO userDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
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
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterUsernameTaken() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail("test@gmail.com");
        creationDTO.setUsername("test");
        creationDTO.setPassword("oui");
        creationDTO.setPasswordConfirmation("oui");

        this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        final UserDTO account = userTestComponent.createAccount();

        final UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setPassword("passwordoui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO tokenDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertEquals(tokenDTO.getUser(), account);
        assertNotNull(tokenDTO.getToken());
        assertNotNull(tokenDTO.getExpirationDate());
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        final UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUsername("JENEXISTEPAS");
        loginDTO.setPassword("CEMOTDEPASSENONPLUS");

        this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isBadRequest());
    }

}
