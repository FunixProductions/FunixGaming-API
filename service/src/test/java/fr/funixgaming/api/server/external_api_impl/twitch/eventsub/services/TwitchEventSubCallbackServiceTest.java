package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import com.google.gson.Gson;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.events.channel.TwitchEventChannelFollowDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.handler.TwitchEventSubHandlerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class TwitchEventSubCallbackServiceTest {

    @InjectMocks
    private TwitchEventSubCallbackService service;

    @Mock
    private TwitchEventSubHmacService hmacService;

    @Mock
    private TwitchEventSubHandlerService handlerService;

    private final Gson gson = new Gson();

    @Test
    void testNewWebhookNotification() {
        doNothing().when(hmacService).validEventMessage(any(), any());
        doNothing().when(handlerService).receiveNewNotification(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "10");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_NOTIFICATION);

        final TwitchEventChannelFollowDTO followDTO = new TwitchEventChannelFollowDTO();
        final TwitchBodyTestNotificationTest<TwitchEventChannelFollowDTO> test = new TwitchBodyTestNotificationTest<>(followDTO);
        try {
            service.handleNewWebhook(httpServletRequest, gson.toJson(test).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            fail("No throw here accepted", e);
        }

        try {
            service.handleNewWebhook(httpServletRequest, gson.toJson(test).getBytes(StandardCharsets.UTF_8));
            fail("Should fail because not able to manage twice event id");
        } catch (ApiBadRequestException ignored) {
        }
        service.cleanMessagesIds();
    }

    @Test
    void testNewWebhookVerification() throws ApiException {
        doNothing().when(hmacService).validEventMessage(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "11");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_VERIFICATION);

        final TwitchBodyTestVerificationTest verification = new TwitchBodyTestVerificationTest();
        final String response = service.handleNewWebhook(httpServletRequest, gson.toJson(verification).getBytes(StandardCharsets.UTF_8));
        assertEquals(verification.challenge, response);
    }

    @Test
    void testNewWebhookRevocation() throws ApiException {
        doNothing().when(hmacService).validEventMessage(any(), any());

        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, "12");
        httpServletRequest.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_REVOCATION);

        final String s = service.handleNewWebhook(httpServletRequest, "anything".getBytes(StandardCharsets.UTF_8));
        assertEquals("s", s);
    }

    @Test
    void missingMessageIdOrMessageType() {
        try {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();

            service.handleNewWebhook(request, "".getBytes());

            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());

            service.handleNewWebhook(request, "".getBytes());
            fail("should fail here");
        } catch (ApiBadRequestException ignored) {
        } catch (RuntimeException e) {
            fail(e);
        }
    }

    @Test
    void testMalformatedBodyNotification() {
        try {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_NOTIFICATION);

            service.handleNewWebhook(request, "".getBytes());

            fail("should fail here");
        } catch (ApiBadRequestException ignored) {
        } catch (RuntimeException e) {
            fail(e);
        }
    }

    @Test
    void testMalformatedBodyVerification() {
        try {
            doNothing().when(hmacService).validEventMessage(any(), any());
            doNothing().when(handlerService).receiveNewNotification(any(), any());
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_ID, UUID.randomUUID().toString());
            request.addHeader(TwitchEventSubCallbackService.TWITCH_MESSAGE_TYPE, TwitchEventSubCallbackService.MESSAGE_TYPE_VERIFICATION);

            service.handleNewWebhook(request, "".getBytes());

            fail("should fail here");
        } catch (ApiBadRequestException ignored) {
        } catch (RuntimeException e) {
            fail(e);
        }
    }

    @Getter
    private static class TwitchBodyTestVerificationTest {
        private final String challenge = UUID.randomUUID().toString();
    }

    @Getter
    @RequiredArgsConstructor
    private static class TwitchBodyTestNotificationTest<T> {

        private final SubscriptionTest subscription = new SubscriptionTest();

        private final T event;

        @Getter
        private static class SubscriptionTest {

            private final String id = UUID.randomUUID().toString();

            private final String type = UUID.randomUUID().toString();

        }

    }

}
