package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FunixBotTestUserXp {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;

    private final String route;
    private final String bearerToken;

    @Autowired
    public FunixBotTestUserXp(MockMvc mockMvc,
                              JsonHelper jsonHelper,
                              UserTestComponent userTestComponent,
                              FunixBotUserExperienceRepository userExperienceRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/funixbot/user/xp";
        this.bearerToken = tokenDTO.getToken();

        userExperienceRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Set<FunixBotUserExperienceDTO> users = new HashSet<>();
        users.addAll(jsonHelper.fromJson(result.getResponse().getContentAsString(), users.getClass()));
    }

    @Test
    public void testCreate() throws Exception {
        final FunixBotUserExperienceDTO user = createBaseDto();

        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();

        final FunixBotUserExperienceDTO command = getResponse(result);
    }

    @Test
    public void testPatch() throws Exception {
        final FunixBotUserExperienceDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotUserExperienceDTO command = getResponse(result);

        command.setUsername("sdkjfhsdkjh");
        result = mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(command)))
                .andExpect(status().isOk())
                .andReturn();

        command = getResponse(result);
    }

    @Test
    public void testFindById() throws Exception {
        final FunixBotUserExperienceDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotUserExperienceDTO command = getResponse(result);

        result = mockMvc.perform(get(route + "/" + command.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();
        command = getResponse(result);
    }

    @Test
    public void testRemoveId() throws Exception {
        final FunixBotUserExperienceDTO user = createBaseDto();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotUserExperienceDTO command = getResponse(result);

        mockMvc.perform(delete(route + "?id=" + command.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    private FunixBotUserExperienceDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), FunixBotUserExperienceDTO.class);
    }

    private FunixBotUserExperienceDTO createBaseDto() {
        final FunixBotUserExperienceDTO user = new FunixBotUserExperienceDTO();

        user.setUserId("id");
        user.setUsername("name");
        user.setXp(10);
        user.setLevel(1);
        user.setLastMessageDate(Date.from(Instant.now()));
        user.setXpNextLevel(100);
        return user;
    }

}
