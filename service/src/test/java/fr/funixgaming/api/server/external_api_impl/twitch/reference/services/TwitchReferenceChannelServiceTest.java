package fr.funixgaming.api.server.external_api_impl.twitch.reference.services;

import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.channel.TwitchReferenceChannelClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.channel.TwitchReferenceChannelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TwitchReferenceChannelServiceTest {

    @MockBean
    private TwitchReferenceChannelClient client;

    @Autowired
    private TwitchReferenceChannelService service;

    @Test
    void testGetChannelInfo() {
        when(client.getChannelInformation(anyString(), anyList())).thenReturn(new TwitchDataResponseDTO<>());
        assertNotNull(service.getChannelInformation("token", new ArrayList<>()));
    }

    @Test
    void testGetChannelVips() {
        when(client.getChannelVips(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyList()
        ));
        assertNotNull(service.getChannelVips("token", "id", null, null, null));
    }

    @Test
    void updateChannelTest() {
        doNothing().when(client).updateChannelInformation(anyString(), anyString(), any(TwitchChannelUpdateDTO.class));
        assertDoesNotThrow(() -> service.updateChannelInformation("token", "id", new TwitchChannelUpdateDTO()));
    }

}
