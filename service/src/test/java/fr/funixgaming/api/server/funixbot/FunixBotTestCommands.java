package fr.funixgaming.api.server.funixbot;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotCommandRepository;
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

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FunixBotTestCommands {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;

    private final String route;
    private final String bearerToken;

    @Autowired
    public FunixBotTestCommands(MockMvc mockMvc,
                                JsonHelper jsonHelper,
                                UserTestComponent userTestComponent,
                                FunixBotCommandRepository commandRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createModoAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/funixbot/command";
        this.bearerToken = tokenDTO.getToken();

        commandRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Set<FunixBotCommandDTO> commands = new HashSet<>();
        commands.addAll(jsonHelper.fromJson(result.getResponse().getContentAsString(), commands.getClass()));
    }

    @Test
    public void testCreate() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final FunixBotCommandDTO command = getResponse(result);
    }

    @Test
    public void testPatch() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotCommandDTO command = getResponse(result);

        command.setMessage("sdkjfhsdkjh");
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
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotCommandDTO command = getResponse(result);

        result = mockMvc.perform(get(route + "/" + command.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();
        command = getResponse(result);
    }

    @Test
    public void testRemoveId() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("oui");

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk())
                .andReturn();
        FunixBotCommandDTO command = getResponse(result);

        mockMvc.perform(delete(route + "?id=" + command.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }

    private FunixBotCommandDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), FunixBotCommandDTO.class);
    }
}
