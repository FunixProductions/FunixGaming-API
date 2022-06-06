package fr.funixgaming.api.server.funixbot;

import com.google.common.reflect.TypeToken;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.server.funixbot.entities.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestFunixBotUserExperience {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final String route;
    private final String bearerToken;

    private final FunixBotUserExperience first;
    private final FunixBotUserExperience second;
    private final FunixBotUserExperience third;
    private final FunixBotUserExperience fourth;

    @Autowired
    public TestFunixBotUserExperience(MockMvc mockMvc,
                                      JsonHelper jsonHelper,
                                      UserTestComponent userTestComponent,
                                      FunixBotUserExperienceRepository repository) throws Exception {
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/funixbot/user/exp/";
        this.bearerToken = userTestComponent.loginUser(userTestComponent.createModoAccount()).getToken();

        repository.deleteAll();

        final FunixBotUserExperience first = new FunixBotUserExperience();
        first.setTwitchUserId("1");
        first.setLevel(100);
        first.setXp(900);
        first.setXpNextLevel(1000);
        first.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience second = new FunixBotUserExperience();
        second.setTwitchUserId("2");
        second.setLevel(100);
        second.setXp(200);
        second.setXpNextLevel(1000);
        second.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience third = new FunixBotUserExperience();
        third.setTwitchUserId("3");
        third.setLevel(90);
        third.setXp(200);
        third.setXpNextLevel(1000);
        third.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperience fourth = new FunixBotUserExperience();
        fourth.setTwitchUserId("4");
        fourth.setLevel(10);
        fourth.setXp(200);
        fourth.setXpNextLevel(1000);
        fourth.setLastMessageDate(Date.from(Instant.now()));

        this.first = repository.save(first);
        this.second = repository.save(second);
        this.third = repository.save(third);
        this.fourth = repository.save(fourth);
    }

    @Test
    public void testGetAll() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();


        final List<FunixBotUserExperienceDTO> list = jsonHelper.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<FunixBotUserExperienceDTO>>() {}.getType());
        assertEquals(4, list.size());
        assertEquals(first.getTwitchUserId(), list.get(0).getTwitchUserId());
        assertEquals(second.getTwitchUserId(), list.get(1).getTwitchUserId());
        assertEquals(third.getTwitchUserId(), list.get(2).getTwitchUserId());
        assertEquals(fourth.getTwitchUserId(), list.get(3).getTwitchUserId());
    }

    @Test
    public void testGetAllPagination() throws Exception {
        MvcResult result = mockMvc.perform(get(route + "?page=0&elemsPerPage=2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();


        List<FunixBotUserExperienceDTO> list = jsonHelper.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<FunixBotUserExperienceDTO>>() {}.getType());
        assertEquals(2, list.size());
        assertEquals(first.getTwitchUserId(), list.get(0).getTwitchUserId());
        assertEquals(second.getTwitchUserId(), list.get(1).getTwitchUserId());

        result = mockMvc.perform(get(route + "?page=1&elemsPerPage=2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();


        list = jsonHelper.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<FunixBotUserExperienceDTO>>() {}.getType());
        assertEquals(2, list.size());
        assertEquals(third.getTwitchUserId(), list.get(0).getTwitchUserId());
        assertEquals(fourth.getTwitchUserId(), list.get(1).getTwitchUserId());
    }

    @Test
    public void testGetFirstRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "s/rank?twitchUserId=1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(1, rank);
    }

    @Test
    public void testGetSecondRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "s/rank?twitchUserId=2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(2, rank);
    }

    @Test
    public void testGetThirdRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "s/rank?twitchUserId=3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(3, rank);
    }

    @Test
    public void testGetFourthRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "s/rank?twitchUserId=4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(4, rank);
    }

    @Test
    public void testGetUnranked() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "s/rank?twitchUserId=ouiouiuiu")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(0, rank);
    }

}
