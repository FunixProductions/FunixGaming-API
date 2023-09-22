package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.clients.UserAuthClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.api.user.client.enums.UserRole;
import com.funixproductions.core.crud.dtos.PageDTO;
import fr.funixgaming.api.twitch.service.services.FunixGamingInformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
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

    @MockBean
    private InternalUserCrudClient internalUserCrudClient;

    @Autowired
    private FunixGamingInformationService funixGamingInformationService;

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

        final PageDTO<UserDTO> pageDTO = new PageDTO<>();
        pageDTO.setActualPage(0);
        pageDTO.setTotalPages(1);
        pageDTO.setContent(List.of(userDTO));
        pageDTO.setTotalElementsThisPage(1);
        pageDTO.setTotalElementsDatabase(1L);
        when(internalUserCrudClient.getAll(any(), any(), any(), any())).thenReturn(pageDTO);
        funixGamingInformationService.fetchUserInfos();
    }

    @Test
    void testFetch() throws Exception {
        mockMvc.perform(get("/twitch/stream"))
                .andExpect(status().isOk());
    }

}
