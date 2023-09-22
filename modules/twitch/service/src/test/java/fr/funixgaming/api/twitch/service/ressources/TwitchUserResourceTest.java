package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelClient;
import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
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
import org.springframework.http.HttpHeaders;
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
class TwitchUserResourceTest {

    @MockBean
    private TwitchUsersClient twitchUsersClient;

    @MockBean
    private TwitchChannelClient twitchChannelClient;

    @MockBean
    private UserAuthClient userAuthClient;

    @MockBean
    private InternalUserCrudClient internalUserCrudClient;

    @MockBean
    private TwitchStreamsClient twitchStreamsClient;

    @MockBean
    private TwitchChatClient twitchChatClient;

    @Autowired
    private FunixGamingInformationService funixGamingInformationService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupMocks() {
        when(twitchUsersClient.getUsersById(anyList())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchUsersClient.getUsersByName(anyList())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchChannelClient.getChannelFollowers(anyString(), anyString(), anyString(), anyString())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchChatClient.getChannelChatters(anyInt(), anyString(), anyString())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchStreamsClient.getStreams(anyString())).thenReturn(new TwitchDataResponseDTO<>());

        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setEmail("oui@gmail.com");
        userDTO.setId(UUID.randomUUID());
        userDTO.setCreatedAt(new Date());
        userDTO.setUsername("funix");

        when(userAuthClient.current(anyString())).thenReturn(userDTO);

        final PageDTO<UserDTO> pageDTO = new PageDTO<>();
        pageDTO.setActualPage(0);
        pageDTO.setTotalPages(1);
        pageDTO.setContent(List.of(userDTO));
        pageDTO.setTotalElementsThisPage(1);
        pageDTO.setTotalElementsDatabase(1L);
        when(internalUserCrudClient.getAll(any(), any(), any(), any())).thenReturn(pageDTO);
    }

    @Test
    void testIsFollowing() throws Exception {
        funixGamingInformationService.fetchUserInfos();
        mockMvc.perform(get("/twitch/user/isFollowing?user_id=123&streamer_id=456")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUsersByName() throws Exception {
        mockMvc.perform(get("/twitch/user/usersByName?login=funix")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUsersById() throws Exception {
        mockMvc.perform(get("/twitch/user/usersById?id=1354")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                .andExpect(status().isOk());
    }

}
