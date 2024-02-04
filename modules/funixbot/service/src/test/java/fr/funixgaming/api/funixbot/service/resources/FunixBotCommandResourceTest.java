package fr.funixgaming.api.funixbot.service.resources;

import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.test.beans.JsonHelper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.funixbot.client.enums.FunixBotCommandType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FunixBotCommandResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @MockBean
    private UserAuthClient userAuthClient;

    @Test
    void testGetCommands() throws Exception {
        mockMvc.perform(get("/funixbot/command"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCommandNoAccess() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.USER);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/command"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/funixbot/command")
                .header("Authorization", "Bearer " + UUID.randomUUID()))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAndEditCommand() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        final String commandName = "tEst1";
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand(commandName);
        commandDTO.setMessage("testMessage");
        commandDTO.setType(FunixBotCommandType.FUN);

        MvcResult mvcResult = mockMvc.perform(post("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk()).andReturn();
        final FunixBotCommandDTO createdCommand = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotCommandDTO.class);
        assertEquals(commandName.toLowerCase(), createdCommand.getCommand());
        createdCommand.setCommand("test2patched");

        mvcResult = mockMvc.perform(patch("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(createdCommand)))
                .andExpect(status().isOk()).andReturn();
        final FunixBotCommandDTO editedCommand = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotCommandDTO.class);
        assertEquals(createdCommand.getCommand(), editedCommand.getCommand());
    }

    @Test
    void testCreateAndEditCommand2() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        final String commandName = "bonjourModo";
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand(commandName);
        commandDTO.setMessage("testMessage");
        commandDTO.setType(FunixBotCommandType.FUN);

        MvcResult mvcResult = mockMvc.perform(post("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk()).andReturn();
        final FunixBotCommandDTO createdCommand = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotCommandDTO.class);
        createdCommand.setMessage("test2patched");

        mvcResult = mockMvc.perform(patch("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(createdCommand)))
                .andExpect(status().isOk()).andReturn();
        final FunixBotCommandDTO editedCommand = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotCommandDTO.class);
        assertEquals(createdCommand.getMessage(), editedCommand.getMessage());
    }

    @Test
    void testCreateCommandWithCommandTooLong() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("testdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdgh");
        commandDTO.setMessage("testMessage");
        commandDTO.setType(FunixBotCommandType.FUN);

        handleBadRequest(commandDTO);
    }

    @Test
    void testCreateCommandWithMessageTooLong() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test");
        commandDTO.setMessage("testdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdghtestdfgldfskghdflkghdflskgjhdfslkgjhdflkgjhdskflhfghdgfhdgfhdfghdfghfdghfdgh");
        commandDTO.setType(FunixBotCommandType.FUN);

        handleBadRequest(commandDTO);
    }

    @Test
    void testCreateCommandNotAlphanumeric() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("test!");
        commandDTO.setMessage("testMessage");
        commandDTO.setType(FunixBotCommandType.FUN);

        handleBadRequest(commandDTO);

        commandDTO.setCommand("test@");
        handleBadRequest(commandDTO);
        commandDTO.setCommand("test#");
        handleBadRequest(commandDTO);
    }

    @Test
    void testCreateDuplicateCommands() throws Exception {
        final FunixBotCommandDTO commandDTO = new FunixBotCommandDTO();
        commandDTO.setCommand("testDupplicateCmd");
        commandDTO.setMessage("testMessage");
        commandDTO.setType(FunixBotCommandType.FUN);

        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isOk());

        commandDTO.setId(null);
        mockMvc.perform(post("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isBadRequest());
    }

    private void handleBadRequest(final FunixBotCommandDTO commandDTO) throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/command")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(commandDTO)))
                .andExpect(status().isBadRequest());
    }

}
