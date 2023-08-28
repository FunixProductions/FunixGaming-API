package fr.funixgaming.api.funixbot.service.services;

import com.funixproductions.core.test.beans.JsonHelper;
import fr.funixgaming.api.core.test.ResourceTestHandler;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotUserExperience;
import fr.funixgaming.api.funixbot.service.repositories.FunixBotUserExperienceRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TestFunixBotUserExperience extends ResourceTestHandler {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;

    private final FunixBotUserExperience first;
    private final FunixBotUserExperience second;
    private final FunixBotUserExperience third;
    private final FunixBotUserExperience fourth;

    @Autowired
    TestFunixBotUserExperience(MockMvc mockMvc,
                               JsonHelper jsonHelper,
                               FunixBotUserExperienceRepository repository) throws Exception {
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        repository.deleteAll();

        final FunixBotUserExperience first = new FunixBotUserExperience();
        first.setTwitchUserId("1");
        first.setLevel(100);
        first.setXp(900);
        first.setXpNextLevel(1000);
        first.setTwitchUsername(UUID.randomUUID().toString());
        first.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience second = new FunixBotUserExperience();
        second.setTwitchUserId("2");
        second.setLevel(100);
        second.setXp(200);
        second.setXpNextLevel(1000);
        second.setTwitchUsername(UUID.randomUUID().toString());
        second.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience third = new FunixBotUserExperience();
        third.setTwitchUserId("3");
        third.setLevel(90);
        third.setXp(200);
        third.setXpNextLevel(1000);
        third.setTwitchUsername(UUID.randomUUID().toString());
        third.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience fourth = new FunixBotUserExperience();
        fourth.setTwitchUserId("4");
        fourth.setLevel(10);
        fourth.setXp(200);
        fourth.setXpNextLevel(1000);
        fourth.setTwitchUsername(UUID.randomUUID().toString());
        fourth.setLastMessageDate(Date.from(Instant.now()));

        this.first = repository.save(first);
        this.second = repository.save(second);
        this.third = repository.save(third);
        this.fourth = repository.save(fourth);
    }

    @Test
    void testAccessAdminRouteWithSimpleUser() throws Exception {
        super.setupNormal();

        mockMvc.perform(post("/funixbot/user/exp")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessAdminRouteWithSimpleUserOneSlash() throws Exception {
        super.setupNormal();

        mockMvc.perform(post("/funixbot/user/exp/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessAdminRouteWithModeratorUser() throws Exception {
        super.setupModerator();

        final FunixBotUserExperienceDTO funixBotUserExperienceDTO = new FunixBotUserExperienceDTO();
        funixBotUserExperienceDTO.setTwitchUserId(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setXp(10);
        funixBotUserExperienceDTO.setLevel(10);
        funixBotUserExperienceDTO.setXpNextLevel(10);
        funixBotUserExperienceDTO.setTwitchUsername(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setLastMessageDateSeconds(10L);

        mockMvc.perform(post("/funixbot/user/exp")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(funixBotUserExperienceDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessAdminRouteWithAdminUser() throws Exception {
        super.setupAdmin();

        final FunixBotUserExperienceDTO funixBotUserExperienceDTO = new FunixBotUserExperienceDTO();
        funixBotUserExperienceDTO.setTwitchUserId(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setXp(10);
        funixBotUserExperienceDTO.setLevel(10);
        funixBotUserExperienceDTO.setXpNextLevel(10);
        funixBotUserExperienceDTO.setTwitchUsername(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setLastMessageDateSeconds(10L);

        mockMvc.perform(post("/funixbot/user/exp")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(funixBotUserExperienceDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testPostWithUserClassic() throws Exception {
        super.setupNormal();

        final FunixBotUserExperienceDTO funixBotUserExperienceDTO = new FunixBotUserExperienceDTO();
        funixBotUserExperienceDTO.setTwitchUserId(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setXp(10);
        funixBotUserExperienceDTO.setLevel(10);
        funixBotUserExperienceDTO.setXpNextLevel(10);
        funixBotUserExperienceDTO.setTwitchUsername(UUID.randomUUID().toString());
        funixBotUserExperienceDTO.setLastMessageDateSeconds(10L);

        mockMvc.perform(post("/funixbot/user/exp")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(funixBotUserExperienceDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetFirstRank() throws Exception {
        testGetFirstRankWithLevel(1);
    }

    @Test
    void testGetSecondRank() throws Exception {
        testGetFirstRankWithLevel(2);
    }

    @Test
    void testGetThirdRank() throws Exception {
        testGetFirstRankWithLevel(3);
    }

    @Test
    void testGetFourthRank() throws Exception {
        testGetFirstRankWithLevel(4);
    }

    private void testGetFirstRankWithLevel(final int rankLevel) throws Exception {
        super.setupNormal();

        final MvcResult result = mockMvc.perform(get("/funixbot/user/exp/rank?twitchUserId=" + rankLevel))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(rankLevel, rank);
    }

    @Test
    void testGetUnranked() throws Exception {
        super.setupNormal();

        final MvcResult result = mockMvc.perform(get("/funixbot/user/exp/rank?twitchUserId=ouiouiuiu"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(0, rank);
    }

}
