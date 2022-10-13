package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.server.beans.JsonHelper;
import fr.funixgaming.api.server.funixbot.entities.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestFunixBotUserExperience {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final String route;

    private final FunixBotUserExperience first;
    private final FunixBotUserExperience second;
    private final FunixBotUserExperience third;
    private final FunixBotUserExperience fourth;

    @Autowired
    public TestFunixBotUserExperience(MockMvc mockMvc,
                                      JsonHelper jsonHelper,
                                      FunixBotUserExperienceRepository repository) throws Exception {
        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/funixbot/user/exp/";

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
    public void testGetFirstRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "rank?twitchUserId=1"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(1, rank);
    }

    @Test
    public void testGetSecondRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "rank?twitchUserId=2"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(2, rank);
    }

    @Test
    public void testGetThirdRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "rank?twitchUserId=3"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(3, rank);
    }

    @Test
    public void testGetFourthRank() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "rank?twitchUserId=4"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(4, rank);
    }

    @Test
    public void testGetUnranked() throws Exception {
        final MvcResult result = mockMvc.perform(get(route + "rank?twitchUserId=ouiouiuiu"))
                .andExpect(status().isOk())
                .andReturn();

        final Integer rank = jsonHelper.fromJson(result.getResponse().getContentAsString(), Integer.class);
        assertEquals(0, rank);
    }

}
