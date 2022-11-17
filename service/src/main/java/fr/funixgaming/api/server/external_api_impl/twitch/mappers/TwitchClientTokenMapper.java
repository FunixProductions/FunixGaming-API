package fr.funixgaming.api.server.external_api_impl.twitch.mappers;

import fr.funixgaming.api.client.external_api_impl.twitch.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.external_api_impl.twitch.entities.TwitchClientToken;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TwitchClientTokenMapper extends ApiMapper<TwitchClientToken, TwitchClientTokenDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "OAuthCode", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    TwitchClientToken toEntity(TwitchClientTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    TwitchClientTokenDTO toDto(TwitchClientToken entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(TwitchClientToken request, @MappingTarget TwitchClientToken toPatch);
}
