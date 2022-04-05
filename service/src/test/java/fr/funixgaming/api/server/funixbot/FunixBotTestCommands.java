package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.CrudTestImpl;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotCommandRepository;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class FunixBotTestCommands {

    private final MockMvc mockMvc;
    private final CrudTestImpl<FunixBotCommandDTO> crudTest;

    @Autowired
    public FunixBotTestCommands(MockMvc mockMvc,
                                      UserTestComponent userTestComponent,
                                      FunixBotCommandRepository commandRepository,
                                      UserRepository userRepository,
                                      UserTokenRepository userTokenRepository) throws Exception {
        userTokenRepository.deleteAll();
        userRepository.deleteAll();
        commandRepository.deleteAll();

        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());

        this.mockMvc = mockMvc;
        this.crudTest = new CrudTestImpl<>(mockMvc, "/funixbot/commands", tokenDTO.getToken());
    }

    @Test
    public void testGetAll() throws Exception {
        crudTest.testGet();
    }

    @Test
    public void testCreate() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        crudTest.create(commandDTO);
    }

    @Test
    public void testPatch() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        final FunixBotCommandDTO result = crudTest.create(commandDTO);
        result.setMessage("sdkjfhsdkjh");

        crudTest.update(result);
    }

    @Test
    public void testFindById() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        final FunixBotCommandDTO result = crudTest.create(commandDTO);
        crudTest.findById(result.getId().toString());
    }

    @Test
    public void testRemoveId() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        final FunixBotCommandDTO result = crudTest.create(commandDTO);
        crudTest.deleteObj(result.getId().toString());
    }
}
