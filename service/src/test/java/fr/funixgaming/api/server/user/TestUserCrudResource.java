package fr.funixgaming.api.server.user;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.entities.UserToken;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestUserCrudResource {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTestComponent userTestComponent;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    private final String route;
    private final String bearerToken;

    @Autowired
    TestUserCrudResource(MockMvc mockMvc,
                         JsonHelper jsonHelper,
                         UserTestComponent userTestComponent,
                         UserRepository userRepository,
                         UserTokenRepository userTokenRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/user";
        this.bearerToken = tokenDTO.getToken();
        this.userTestComponent = userTestComponent;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Test
    void testAccessUser() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessModo() throws Exception {
        final User user = userTestComponent.createModoAccount();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testPatch() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        final UserDTO user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk());

        final UserSecretsDTO requestChangePassword = new UserSecretsDTO();
        requestChangePassword.setPassword("newPassword");
        requestChangePassword.setId(user.getId());

        mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(requestChangePassword)))
                .andExpect(status().isOk());

        final Optional<User> search = userRepository.findByUuid(user.getId().toString());
        assertTrue(search.isPresent());
        assertEquals(requestChangePassword.getPassword(), search.get().getPassword());
    }

    @Test
    void testFindById() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        mockMvc.perform(get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveId() throws Exception {
        final UserSecretsDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        UserToken userToken = new UserToken();
        userToken.setUser(userRepository.findByUuid(user.getId().toString()).get());
        userToken.setToken("jshdlqkfjhslkh");
        userToken.setExpirationDate(Date.from(Instant.now().plusSeconds(10000)));
        userToken = this.userTokenRepository.save(userToken);

        mockMvc.perform(delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());

        assertFalse(this.userTokenRepository.findByUuid(userToken.getUuid().toString()).isPresent());
    }

    private UserDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
    }

    private UserSecretsDTO createUser() {
        final UserSecretsDTO userDTO = new UserSecretsDTO();

        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setRole(UserRole.USER);
        userDTO.setEmail("oui@ggmail.com");
        userDTO.setPassword("password");
        return userDTO;
    }
}
