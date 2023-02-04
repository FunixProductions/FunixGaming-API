package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.TwitchEventSubListDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.user.TwitchUserDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchServerTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelEventType;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelSubscription;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.entities.TwitchEventSub;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.repositories.TwitchEventSubRepository;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.users.TwitchReferenceUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Service dedicated to the handling of register and removing streamer subscriptions events
 * Also used to check the valid status of the registered events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchEventSubRegistrationService {

    private final TwitchEventSubRepository eventSubRepository;
    private final TwitchReferenceUsersService twitchReferenceUsersService;
    private final TwitchEventSubReferenceService twitchEventSubReferenceService;
    private final TwitchServerTokenService twitchServerTokenService;

    private final Set<String> streamerIdsRemoving = new HashSet<>();

    public void createSubscription(final String streamerUsername) throws ApiBadRequestException {
        final Optional<TwitchEventSub> search = eventSubRepository.findTwitchEventSubByStreamerUsernameEqualsIgnoreCase(streamerUsername);
        if (search.isPresent()) {
            throw new ApiBadRequestException(String.format("Le streamer %s possède déjà son abonnement pour les twitch events sur la funix api.", streamerUsername));
        }

        final String streamerId = getUserIdFromUsername(streamerUsername);
        final List<ChannelSubscription> channelSubscriptions = getChannelSubscriptions(streamerId);
        createSubscriptionList(channelSubscriptions, streamerUsername);
    }

    public void removeSubscription(final String streamerUsername) throws ApiException {
        final Optional<TwitchEventSub> search = eventSubRepository.findTwitchEventSubByStreamerUsernameEqualsIgnoreCase(streamerUsername);

        if (search.isPresent()) {
            removeStreamerSubscriptions(streamerUsername, search.get());
        } else {
            throw new ApiNotFoundException(String.format("Le streamer %s ne possède pas d'abonnement pour les events twitch.", streamerUsername));
        }
    }

    @Async
    public void removeStreamerSubscriptions(final String streamerUsername, final TwitchEventSub eventSubLocal) {
        final String streamerId = getUserIdFromUsername(streamerUsername);

        if (this.streamerIdsRemoving.contains(streamerId)) {
            return;
        }
        this.streamerIdsRemoving.add(streamerId);

        try {
            for (final TwitchEventSubListDTO.TwitchEventSub sub : getActualEventsForStreamer(streamerId)) {
                try {
                    this.twitchEventSubReferenceService.deleteSubscription(sub.getId());
                    log.info("SUPPRESSION TWITCH EVENT Le streamer {} ne recevera plus de notifications pour l'event {}.", streamerId, sub.getType());
                } catch (ApiBadRequestException e) {
                    log.error("Une erreur est survenue lors de la suppression d'un event twitch pour le streamer {}. Event id {} Event type {}. Erreur: {}", streamerUsername, sub.getId(), sub.getType(), e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            this.streamerIdsRemoving.remove(streamerId);
            this.eventSubRepository.delete(eventSubLocal);
            Thread.currentThread().interrupt();
            throw new ApiException(String.format("Thread coupé pour la suppresstion des subscriptions pour le streamer %s.", streamerUsername), e);
        } catch (ApiException e) {
            log.error("Une erreur est survenue lors de la récupération des events twitch pour le streamer {}. Erreur: {}", streamerUsername, e.getMessage());
        }

        this.streamerIdsRemoving.remove(streamerId);
        this.eventSubRepository.delete(eventSubLocal);
        log.info("Streamer {} ne possède plus le hook des events twitch sur la funix api.", streamerUsername);
    }

    @Async
    public void createSubscriptionList(final List<ChannelSubscription> subscriptions, final String streamerUsername) {
        for (final ChannelSubscription channelSubscription : subscriptions) {
            try {
                this.twitchEventSubReferenceService.createSubscription(channelSubscription);
                Thread.sleep(400);
                log.info("Le streamer {} possède désormais le hook twitch event {} sur la funix api.", streamerUsername, channelSubscription.getType());
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                throw new ApiException(String.format("Thread tué lors de la création de subscription pour le streamer %s.", streamerUsername), interruptedException);
            } catch (ApiException apiException) {
                log.error("Impossible de créer le hook avec l'event de type {} pour le streamer {}", channelSubscription.getType(), streamerUsername);
            }
        }

        final TwitchEventSub eventSub = new TwitchEventSub();
        eventSub.setStreamerUsername(streamerUsername);
        eventSubRepository.save(eventSub);
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

    private List<ChannelSubscription> getChannelSubscriptions(final String streamerId) {
        try {
            final List<ChannelSubscription> subscriptions = new ArrayList<>();
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

    private String getUserIdFromUsername(final String username) {
        final TwitchDataResponseDTO<TwitchUserDTO> userList = this.twitchReferenceUsersService.getUsersByName(this.twitchServerTokenService.getAccessToken(), List.of(username));

        if (userList.getData().isEmpty()) {
            throw new ApiBadRequestException(String.format("Le streamer %s n'existe pas sur twitch.", username));
        } else {
            final TwitchUserDTO twitchUserDTO = userList.getData().get(0);
            return twitchUserDTO.getId();
        }
    }

}
