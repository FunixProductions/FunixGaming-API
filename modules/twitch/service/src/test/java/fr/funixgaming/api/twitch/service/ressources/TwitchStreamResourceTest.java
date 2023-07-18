package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchStreamResourceTest {

    @MockBean
    private TwitchStreamsClient twitchStreamsClient;

    @MockBean
    private UserAuthClient userAuthClient;

    @MockBean
    private TwitchChatClient twitchChatClient;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupMocks() {
        when(twitchStreamsClient.getStreams(anyString())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchChatClient.getChannelChatters(anyInt(), anyString(), anyString())).thenReturn(new TwitchDataResponseDTO<>());

        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setEmail("oui@gmail.com");
        userDTO.setId(UUID.randomUUID());
        userDTO.setCreatedAt(new Date());
        userDTO.setUsername("toto");

        when(userAuthClient.current(anyString())).thenReturn(userDTO);
    }

    @Test
    void testFetch() throws Exception {
        mockMvc.perform(get("/twitch/stream"))
                .andExpect(status().isOk());
    }

}
