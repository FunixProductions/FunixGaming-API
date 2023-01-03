package fr.funixgaming.api.server.user;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestUserAuthResourceAuth {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserTestComponent userTestComponent;

    @Autowired
    private JsonHelper jsonHelper;

    @Test
    void testRegisterSuccess() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("oui");
        creationDTO.setPasswordConfirmation("oui");

        MvcResult mvcResult = this.mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO userDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);
        assertEquals(creationDTO.getUsername(), userDTO.getUsername());
        assertEquals(creationDTO.getEmail(), userDTO.getEmail());
        assertEquals(UserRole.USER, userDTO.getRole());
    }

    @Test
    void testRegisterFailPasswordsMismatch() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("oui2");
        creationDTO.setPasswordConfirmation("oui");

        this.mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUsernameTaken() throws Exception {
        final UserCreationDTO creationDTO = new UserCreationDTO();
        creationDTO.setEmail(UUID.randomUUID() + "@gmail.com");
        creationDTO.setUsername(UUID.randomUUID().toString());
        creationDTO.setPassword("oui");
        creationDTO.setPasswordConfirmation("oui");

        this.mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isOk());

        this.mockMvc.perform(post("/user/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(creationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginSuccess() throws Exception {
        final User account = userTestComponent.createBasicUser();

        final UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setPassword(account.getPassword());
        loginDTO.setStayConnected(false);

        MvcResult mvcResult = this.mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO tokenDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertEquals(tokenDTO.getUser().getId(), account.getUuid());
        assertNotNull(tokenDTO.getToken());
        assertNotNull(tokenDTO.getExpirationDate());
    }

    @Test
    void testLoginSuccessNoExpiration() throws Exception {
        final User account = userTestComponent.createBasicUser();

        final UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setPassword(account.getPassword());
        loginDTO.setStayConnected(true);

        MvcResult mvcResult = this.mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO tokenDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);
        assertEquals(tokenDTO.getUser().getId(), account.getUuid());
        assertNotNull(tokenDTO.getToken());
        assertNull(tokenDTO.getExpirationDate());
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        final UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUsername("JENEXISTEPAS");
        loginDTO.setPassword("CEMOTDEPASSENONPLUS");

        this.mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCurrentUser() throws Exception {
        final User account = userTestComponent.createBasicUser();

        final UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(account.getUsername());
        loginDTO.setPassword(account.getPassword());
        loginDTO.setStayConnected(true);

        MvcResult mvcResult = this.mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserTokenDTO tokenDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserTokenDTO.class);

        mvcResult = this.mockMvc.perform(get("/user/auth/current")
                .header("Authorization", "Bearer " + tokenDTO.getToken())
        ).andExpect(status().isOk()).andReturn();
        final UserDTO userDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertEquals(tokenDTO.getUser().getUsername(), userDTO.getUsername());
        assertEquals(tokenDTO.getUser().getEmail(), userDTO.getEmail());
        assertEquals(tokenDTO.getUser().getRole(), userDTO.getRole());
        assertEquals(tokenDTO.getUser().getId(), userDTO.getId());
    }

    @Test
    void testFailGetCurrentUserNoAuth() throws Exception {
        this.mockMvc.perform(get("/user/auth/current")).andExpect(status().isUnauthorized());
    }

    @Test
    void testFailGetCurrentUserBadAuth() throws Exception {
        this.mockMvc.perform(get("/user/auth/current")
                .header("Authorization", "Bearer " + "BADTOKEN"))
                .andExpect(status().isUnauthorized());
    }

}
