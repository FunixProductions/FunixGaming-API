package fr.funixgaming.api.funixbot.service.resources;

import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.test.beans.JsonHelper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotAutoMessageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FunixBotAutoMessagesResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @MockitoBean
    private UserAuthClient userAuthClient;

    @Test
    void testAccessMessages() throws Exception {
        mockMvc.perform(get("/funixbot/automessages"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateAndPatch() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/automessages"))
                .andExpect(status().isUnauthorized());

        final FunixBotAutoMessageDTO funixBotAutoMessageDTO = new FunixBotAutoMessageDTO("testMessage", "Minecraft", false);
        MvcResult mvcResult = mockMvc.perform(post("/funixbot/automessages")
                .header("Authorization", "Bearer " + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(funixBotAutoMessageDTO))
        ).andExpect(status().isOk()).andReturn();

        final FunixBotAutoMessageDTO created = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotAutoMessageDTO.class);
        assertEquals(funixBotAutoMessageDTO.getMessage(), created.getMessage());
        created.setMessage("testMessage2");
        created.setGameName(null);

        mvcResult = mockMvc.perform(put("/funixbot/automessages")
                .header("Authorization", "Bearer " + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(created))
        ).andExpect(status().isOk()).andReturn();
        final FunixBotAutoMessageDTO updated = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotAutoMessageDTO.class);
        assertEquals(created.getMessage(), updated.getMessage());
        assertNull(updated.getGameName());
    }

    @Test
    void testCreateMessageWithNoMessage() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/automessages")
                .header("Authorization", "Bearer " + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(new FunixBotAutoMessageDTO()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateMessageWithNoAccess() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.USER);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);
        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/automessages")
                .header("Authorization", "Bearer " + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonHelper.toJson(new FunixBotAutoMessageDTO()))
        ).andExpect(status().isForbidden());
    }

}
