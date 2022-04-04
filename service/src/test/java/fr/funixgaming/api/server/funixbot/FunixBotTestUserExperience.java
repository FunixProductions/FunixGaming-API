package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.CrudTestImpl;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
public class FunixBotTestUserExperience {

    private final MockMvc mockMvc;
    private final CrudTestImpl<FunixBotUserExperienceDTO> crudTest;

    @Autowired
    public FunixBotTestUserExperience(MockMvc mockMvc,
                                      UserTestComponent userTestComponent,
                                      FunixBotUserExperienceRepository funixBotUserExperienceRepository,
                                      UserRepository userRepository,
                                      UserTokenRepository userTokenRepository) throws Exception {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        funixBotUserExperienceRepository.deleteAll();

        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());

        this.mockMvc = mockMvc;
        this.crudTest = new CrudTestImpl<>(mockMvc, "/funixbot/user/xp", tokenDTO.getToken());
    }

    @Test
    public void testGetAll() throws Exception {
        crudTest.testGet();
    }

    @Test
    public void testCreate() throws Exception {
        final FunixBotUserExperienceDTO userExperienceDTO = new FunixBotUserExperienceDTO();

        userExperienceDTO.setUserId("oui");
        userExperienceDTO.setUsername("oui");
        userExperienceDTO.setLevel(10);
        userExperienceDTO.setXp(10);
        userExperienceDTO.setXpNextLevel(100);
        userExperienceDTO.setLastMessageDate(Date.from(Instant.now()));

        crudTest.create(userExperienceDTO);
    }

    @Test
    public void testPatch() throws Exception {
        final FunixBotUserExperienceDTO userExperienceDTO = new FunixBotUserExperienceDTO();

        userExperienceDTO.setUserId("oui");
        userExperienceDTO.setUsername("oui");
        userExperienceDTO.setLevel(10);
        userExperienceDTO.setXp(10);
        userExperienceDTO.setXpNextLevel(100);
        userExperienceDTO.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperienceDTO result = crudTest.create(userExperienceDTO);
        result.setUserId("sdkjfhsdkjh");

        crudTest.update(result);
    }

    @Test
    public void testFindById() throws Exception {
        final FunixBotUserExperienceDTO userExperienceDTO = new FunixBotUserExperienceDTO();

        userExperienceDTO.setUserId("oui");
        userExperienceDTO.setUsername("oui");
        userExperienceDTO.setLevel(10);
        userExperienceDTO.setXp(10);
        userExperienceDTO.setXpNextLevel(100);
        userExperienceDTO.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperienceDTO result = crudTest.create(userExperienceDTO);
        crudTest.findById(result.getId().toString());
    }

    @Test
    public void testRemoveId() throws Exception {
        final FunixBotUserExperienceDTO userExperienceDTO = new FunixBotUserExperienceDTO();

        userExperienceDTO.setUserId("oui");
        userExperienceDTO.setUsername("oui");
        userExperienceDTO.setLevel(10);
        userExperienceDTO.setXp(10);
        userExperienceDTO.setXpNextLevel(100);
        userExperienceDTO.setLastMessageDate(Date.from(Instant.now()));

        final FunixBotUserExperienceDTO result = crudTest.create(userExperienceDTO);
        crudTest.deleteObj(result.getId().toString());
    }

}
