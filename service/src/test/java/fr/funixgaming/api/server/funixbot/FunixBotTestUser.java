package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserRepository;
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

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FunixBotTestUser {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTestComponent userTestComponent;

    private final String route;
    private final String bearerToken;

    @Autowired
    public FunixBotTestUser(MockMvc mockMvc,
                            JsonHelper jsonHelper,
                            UserTestComponent userTestComponent,
                            FunixBotUserRepository userExperienceRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/funixbot/user/";
        this.bearerToken = tokenDTO.getToken();
        this.userTestComponent = userTestComponent;

        userExperienceRepository.deleteAll();
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
    public void testCreate() throws Exception {
        final FunixBotUserDTO user = createBaseDto();

        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();

        final FunixBotUserDTO response = getResponse(result);
        compare(user, response);
    }

    @Test
    public void testGetAll() throws Exception {
        FunixBotUserDTO userCreation = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userCreation)))
                .andExpect(status().isOk())
                .andReturn();
        userCreation = getResponse(result);

        result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Set<FunixBotUserDTO> users = new HashSet<>();
        users.addAll(jsonHelper.fromJson(result.getResponse().getContentAsString(), users.getClass()));

        assertEquals(1, users.size());
        for (final FunixBotUserDTO res : users) {
            compare(userCreation, res);
        }
    }

    @Test
    public void testPatch() throws Exception {
        FunixBotUserDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        user.getUserExperience().setLevel(1000);

        result = mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();

        final FunixBotUserDTO userPatch = getResponse(result);
        compare(user, userPatch);
    }

    @Test
    public void testFindById() throws Exception {
        FunixBotUserDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        user = getResponse(result);

        result = mockMvc.perform(get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final FunixBotUserDTO userGet = getResponse(result);
        compare(user, userGet);
    }

    @Test
    public void testRemoveId() throws Exception {
        FunixBotUserDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        user = getResponse(result);

        mockMvc.perform(delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());

        result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Set<FunixBotUserDTO> users = new HashSet<>();
        users.addAll(jsonHelper.fromJson(result.getResponse().getContentAsString(), users.getClass()));

        assertEquals(0, users.size());
    }

    private FunixBotUserDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), FunixBotUserDTO.class);
    }

    private FunixBotUserDTO createBaseDto() {
        final FunixBotUserDTO user = new FunixBotUserDTO();
        final FunixBotUserExperienceDTO experienceDTO = new FunixBotUserExperienceDTO();

        user.setUserId("id");
        user.setUsername("name");
        user.setLastMessageDateSeconds(Instant.now().getEpochSecond());

        experienceDTO.setXp(10);
        experienceDTO.setLevel(1);
        experienceDTO.setXpNextLevel(100);
        user.setUserExperience(experienceDTO);
        return user;
    }

    private void compare(final FunixBotUserDTO base, final FunixBotUserDTO toCompare) {
        assertEquals(base.getUserId(), toCompare.getUserId());
        assertEquals(base.getUsername(), toCompare.getUsername());
        assertNotNull(toCompare.getUserExperience());
        assertEquals(base.getLastMessageDateSeconds(), toCompare.getLastMessageDateSeconds());

        final FunixBotUserExperienceDTO userExp = base.getUserExperience();
        final FunixBotUserExperienceDTO experienceDTO = toCompare.getUserExperience();
        assertEquals(userExp.getXp(), experienceDTO.getXp());
        assertEquals(userExp.getXpNextLevel(), experienceDTO.getXpNextLevel());
        assertEquals(userExp.getLevel(), experienceDTO.getLevel());
    }

}
