package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
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
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchUserResourceTest {

    @MockBean
    private TwitchUsersClient twitchUsersClient;

    @MockBean
    private UserAuthClient userAuthClient;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupMocks() {
        when(twitchUsersClient.getUsersById(anyList())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchUsersClient.getUsersByName(anyList())).thenReturn(new TwitchDataResponseDTO<>());
        when(twitchUsersClient.isUserFollowingStreamer(anyString(), anyString())).thenReturn(new TwitchDataResponseDTO<>());

        final UserDTO userDTO = new UserDTO();
        userDTO.setRole(UserRole.MODERATOR);
        userDTO.setEmail("oui@gmail.com");
        userDTO.setId(UUID.randomUUID());
        userDTO.setCreatedAt(new Date());
        userDTO.setUsername("toto");

        when(userAuthClient.current(anyString())).thenReturn(userDTO);
    }

    @Test
    void testIsFollowing() throws Exception {
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
