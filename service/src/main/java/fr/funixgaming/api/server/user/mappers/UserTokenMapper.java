package fr.funixgaming.api.server.user.mappers;


import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.server.user.entities.UserToken;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserTokenMapper extends ApiMapper<UserToken, UserTokenDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    UserToken toEntity(UserTokenDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    UserTokenDTO toDto(UserToken entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(UserToken request, @MappingTarget UserToken toPatch);
}
