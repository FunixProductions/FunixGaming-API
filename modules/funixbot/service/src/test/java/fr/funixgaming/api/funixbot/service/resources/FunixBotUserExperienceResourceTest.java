package fr.funixgaming.api.funixbot.service.resources;

import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.test.beans.JsonHelper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotUserExperienceDTO;
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
class FunixBotUserExperienceResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonHelper jsonHelper;

    @MockBean
    private UserAuthClient userAuthClient;

    @Test
    void testGetExp() throws Exception {
        mockMvc.perform(get("/funixbot/user/exp"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateNoPermissions() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.USER);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);

        when(userAuthClient.current(any())).thenReturn(userDTO);

        mockMvc.perform(post("/funixbot/user/exp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(new FunixBotUserExperienceDTO())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/funixbot/user/exp")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(new FunixBotUserExperienceDTO())))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch("/funixbot/user/exp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(new FunixBotUserExperienceDTO())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(patch("/funixbot/user/exp")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(new FunixBotUserExperienceDTO())))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAndUpdateExp() throws Exception {
        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setUsername(UUID.randomUUID().toString());
        userDTO.setEmail(UUID.randomUUID().toString());
        userDTO.setId(UUID.randomUUID());
        userDTO.setValid(true);

        when(userAuthClient.current(any())).thenReturn(userDTO);

        final FunixBotUserExperienceDTO experienceDTO = new FunixBotUserExperienceDTO();
        experienceDTO.setXp(10);
        experienceDTO.setLevel(1);
        experienceDTO.setLastMessageDateSeconds(1L);
        experienceDTO.setTwitchUserId("qsd");
        experienceDTO.setXpNextLevel(100);

        MvcResult mvcResult = this.mockMvc.perform(post("/funixbot/user/exp")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(experienceDTO)))
                .andExpect(status().isOk())
                .andReturn();
        final FunixBotUserExperienceDTO createdDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotUserExperienceDTO.class);
        assertEquals(experienceDTO.getXp(), createdDTO.getXp());
        createdDTO.setLevel(10);

        mvcResult = this.mockMvc.perform(patch("/funixbot/user/exp")
                        .header("Authorization", "Bearer " + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(createdDTO)))
                .andExpect(status().isOk()).andReturn();
        final FunixBotUserExperienceDTO updatedDTO = jsonHelper.fromJson(mvcResult.getResponse().getContentAsString(), FunixBotUserExperienceDTO.class);
        assertEquals(createdDTO.getLevel(), updatedDTO.getLevel());
    }

}
