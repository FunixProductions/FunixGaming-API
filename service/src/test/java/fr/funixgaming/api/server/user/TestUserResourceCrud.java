package fr.funixgaming.api.server.user;

import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotCommandRepository;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserResourceCrud {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTestComponent userTestComponent;

    private final String route;
    private final String bearerToken;

    @Autowired
    public TestUserResourceCrud(MockMvc mockMvc,
                                JsonHelper jsonHelper,
                                UserTestComponent userTestComponent,
                                FunixBotCommandRepository commandRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/user/";
        this.bearerToken = tokenDTO.getToken();
        this.userTestComponent = userTestComponent;

        commandRepository.deleteAll();
    }

    @Test
    public void testAccessUser() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAccessModo() throws Exception {
        final User user = userTestComponent.createModoAccount();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Set<UserDTO> user = new HashSet<>();
        user.addAll(jsonHelper.fromJson(result.getResponse().getContentAsString(), user.getClass()));
    }

    @Test
    public void testCreate() throws Exception {
        final UserAdminDTO userDTO = createUser();

        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO user = getResponse(result);
    }

    @Test
    public void testPatch() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        result = mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();

        user = getResponse(result);
    }

    @Test
    public void testFindById() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        result = mockMvc.perform(get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();
        user = getResponse(result);
    }

    @Test
    public void testRemoveId() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        mockMvc.perform(delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    private UserDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
    }

    private UserAdminDTO createUser() {
        final UserAdminDTO userDTO = new UserAdminDTO();

        userDTO.setUsername("OUI");
        userDTO.setRole(UserRole.USER);
        userDTO.setEmail("oui@ggmail.com");
        userDTO.setPassword("password");
        return userDTO;
    }
}
