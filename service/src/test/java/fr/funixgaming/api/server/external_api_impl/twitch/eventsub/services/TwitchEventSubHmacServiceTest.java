package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
class TwitchEventSubHmacServiceTest {

    @Autowired
    private TwitchEventSubHmacService hmacService;

    @Test
    @Order(1)
    void testValidTwitchCall() throws Exception {
        final File file = new File(TwitchEventSubHmacService.FILE_SECRET_NAME);
        file.delete();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_ID, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_TIMESTAMP, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_SIGNATURE, "sha256=" + encode("1010body"));

        hmacService.validEventMessage(request, "body");
    }

    @Test
    @Order(2)
    void testValidTwitchCallWithFile() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_ID, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_TIMESTAMP, "10");
        request.addHeader(TwitchEventSubHmacService.TWITCH_MESSAGE_SIGNATURE, "sha256=" + encode("1010body"));

        hmacService.validEventMessage(request, "body");
    }

    private String encode(final String data) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(hmacService.getKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);

        return new String(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

}
