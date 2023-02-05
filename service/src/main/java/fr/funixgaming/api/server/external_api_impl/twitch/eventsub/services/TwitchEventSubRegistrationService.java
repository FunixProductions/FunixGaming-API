package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import com.google.common.base.Strings;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.TwitchEventSubListDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.user.TwitchUserDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchServerTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.TwitchSubscription;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelSubscription;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.users.TwitchReferenceUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service dedicated to the handling of register and removing streamer subscriptions events
 * Also used to check the valid status of the registered events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchEventSubRegistrationService {

    public static final String TWITCH_SUBSCRIPTION_ACTIVE_STATUS = "enabled";

    private final TwitchReferenceUsersService twitchReferenceUsersService;
    private final TwitchEventSubReferenceService twitchEventSubReferenceService;
    private final TwitchServerTokenService twitchServerTokenService;

    private final Set<String> streamerIdsCreating = new HashSet<>();
    private final Set<String> streamerIdsRemoving = new HashSet<>();

    /**
     * Method called by the ressource to create the event list subscriptions for a streamer
     * Calls inside an async function who process the task (high CPU demand due to delay to not spamm twitch api)
     * @param streamerUsername streamer name like funixgaming to create his subscriptions
     * @throws ApiBadRequestException when error
     */
    public void createSubscription(final String streamerUsername) throws ApiBadRequestException {
        final String streamerId = getUserIdFromUsername(streamerUsername);
        final List<TwitchSubscription> subscriptions = new ArrayList<>();

        subscriptions.addAll(generateChannelSubscriptions(streamerId));

        createSubscriptions(subscriptions, streamerUsername, streamerId);
    }

    /**
     * Method called by the ressource to remove a subscription list for a streamer
     * Calls inside an async function who process the task (high CPU demand due to delay to not spamm twitch api)
     * @param streamerUsername streamer name like funixgaming to remove his subscriptions
     * @throws ApiBadRequestException when error
     */
    public void removeSubscription(final String streamerUsername) throws ApiBadRequestException {
        final String streamerId = getUserIdFromUsername(streamerUsername);

        removeStreamerSubscriptions(streamerUsername, streamerId);
    }

    /**
     * Async method to remove all the streamer subscriptions from twitch
     * @param streamerUsername streamer username used for logging
     * @param streamerId streamer id
     */
    @Async
    public void removeStreamerSubscriptions(final String streamerUsername, final String streamerId) {
        if (this.streamerIdsRemoving.contains(streamerId)) {
            return;
        }
        this.streamerIdsRemoving.add(streamerId);

        try {
            for (final TwitchEventSubListDTO.TwitchEventSub sub : getActualEventsForStreamer(streamerId)) {
                deleteSubscriptionRequest(sub, streamerId, streamerUsername);
            }
        } catch (InterruptedException e) {
            this.streamerIdsRemoving.remove(streamerId);
            Thread.currentThread().interrupt();
            throw new ApiException(String.format("Thread coupé pendant la récupération des twitch events pour le streamer %s.", streamerUsername), e);
        } catch (ApiException e) {
            log.error("Une erreur est survenue lors de la récupération des events twitch pour le streamer {}. Erreur: {}", streamerUsername, e.getMessage());
        }

        this.streamerIdsRemoving.remove(streamerId);
        log.info("Streamer {} ne possède plus le hook des events twitch sur la funix api.", streamerUsername);
    }

    /**
     * Async method to run the whole subscriptions creation listed in the enums defined here: <br>
     * Enums: {@link fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums}<br>
     * It will check if the subscriptions you create are not aleready activated.
     * @param subscriptions subscriptions you want to create
     * @param streamerUsername streamer twitch username used for logging
     * @param streamerId streamer twitch id
     */
    @Async
    public void createSubscriptions(final List<TwitchSubscription> subscriptions, final String streamerUsername, final String streamerId) {
        if (this.streamerIdsCreating.contains(streamerId)) {
            return;
        }
        this.streamerIdsCreating.add(streamerId);

        try {
            final List<TwitchEventSubListDTO.TwitchEventSub> activeSubs = getActualEventsForStreamer(streamerId);
            TwitchEventSubListDTO.TwitchEventSub actualActiveSub;

            for (final TwitchSubscription subscription : subscriptions) {
                actualActiveSub = isNewSubscriptionIsAlreadyActivated(subscription, activeSubs);

                if (actualActiveSub != null && !Strings.isNullOrEmpty(actualActiveSub.getStatus()) &&
                        !actualActiveSub.getStatus().equals(TWITCH_SUBSCRIPTION_ACTIVE_STATUS)) {
                    deleteSubscriptionRequest(actualActiveSub, streamerId, streamerUsername);
                }
                createNewSubscriptionRequest(subscription, streamerUsername);
            }

        } catch (InterruptedException interruptedException) {
            this.streamerIdsCreating.remove(streamerId);
            Thread.currentThread().interrupt();
            throw new ApiException("Thread coupé lors de la récupération des events streamer twitch avant de lui subscribe.");
        }
        this.streamerIdsCreating.remove(streamerId);
    }

    @Nullable
    private TwitchEventSubListDTO.TwitchEventSub isNewSubscriptionIsAlreadyActivated(final TwitchSubscription newSub,
                                                                                     final List<TwitchEventSubListDTO.TwitchEventSub> activeSubs) {
        for (final TwitchEventSubListDTO.TwitchEventSub activeSub : activeSubs) {
            if (!Strings.isNullOrEmpty(activeSub.getType()) && activeSub.getType().equals(newSub.getType())) {
                return activeSub;
            }
        }

        return null;
    }

    private void createNewSubscriptionRequest(final TwitchSubscription subscription, final String streamerUsername) {
        try {
            this.twitchEventSubReferenceService.createSubscription(subscription);
            Thread.sleep(400);
            log.info("CREATION TWITCH EVENT Le streamer {} possède désormais le hook twitch event {} sur la funix api.", streamerUsername, subscription.getType());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new ApiException(String.format("Thread tué lors de la création de subscription pour le streamer %s.", streamerUsername), interruptedException);
        } catch (ApiException apiException) {
            log.error("Impossible de créer le hook avec l'event de type {} pour le streamer {}", subscription.getType(), streamerUsername);
        }
    }

    private void deleteSubscriptionRequest(final TwitchEventSubListDTO.TwitchEventSub sub, final String streamerId, final String streamerUsername) {
        try {
            this.twitchEventSubReferenceService.deleteSubscription(sub.getId());
            Thread.sleep(400);
            log.info("SUPPRESSION TWITCH EVENT Le streamer {} ne recevera plus de notifications pour l'event {}.", streamerId, sub.getType());
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new ApiException(String.format("Thread tué lors de la suppression d'une subscription type %s pour le streamer %s.", sub.getType(), streamerUsername), interruptedException);
        } catch (ApiBadRequestException e) {
            log.error("Une erreur est survenue lors de la suppression d'un event twitch pour le streamer {}. Event id {} Event type {}. Erreur: {}", streamerUsername, sub.getId(), sub.getType(), e.getMessage());
        }
    }

    private List<TwitchEventSubListDTO.TwitchEventSub> getActualEventsForStreamer(final String streamerId) throws InterruptedException, ApiException {
        final List<TwitchEventSubListDTO.TwitchEventSub> toSend = new ArrayList<>();

        TwitchEventSubListDTO subs = this.twitchEventSubReferenceService.getSubscriptions(null, null, streamerId, null);
        boolean continueLoop = true;
        String pagination;

        while (continueLoop) {
            toSend.addAll(subs.getData());

            pagination = subs.hasPagination();
            if (pagination == null) {
                continueLoop = false;
            } else {
                Thread.sleep(400);
                subs = this.twitchEventSubReferenceService.getSubscriptions(null, null, streamerId, pagination);
            }
        }
        return toSend;
    }

    private String getUserIdFromUsername(final String username) {
        final TwitchDataResponseDTO<TwitchUserDTO> userList = this.twitchReferenceUsersService.getUsersByName(this.twitchServerTokenService.getAccessToken(), List.of(username));

        if (userList.getData().isEmpty()) {
            throw new ApiBadRequestException(String.format("Le streamer %s n'existe pas sur twitch.", username));
        } else {
            final TwitchUserDTO twitchUserDTO = userList.getData().get(0);
            return twitchUserDTO.getId();
        }
    }

    private List<TwitchSubscription> generateChannelSubscriptions(final String streamerId) {
        try {
            final List<TwitchSubscription> subscriptions = new ArrayList<>();

            for (ChannelEventType eventType : ChannelEventType.values()) {
                final Constructor<? extends ChannelSubscription> constructor = eventType.getClazz().getConstructor(eventType.getClazz());
                final ChannelSubscription channelSubscription = constructor.newInstance(streamerId);
                subscriptions.add(channelSubscription);
            }

            return subscriptions;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ApiException(String.format("Erreur interne lors de la création de twitch events pour le streamer %s.", streamerId), e);
        }
    }

}
