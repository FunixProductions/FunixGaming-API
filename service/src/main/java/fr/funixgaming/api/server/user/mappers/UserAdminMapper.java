package fr.funixgaming.api.server.user.mappers;

import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.user.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserAdminMapper extends ApiMapper<User, UserAdminDTO> {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tokens", ignore = true)
    @Mapping(target = "banned", ignore = true)
    @Override
    User toEntity(UserAdminDTO dto);

    @Mapping(target = "id", source = "uuid")
    @Override
    UserAdminDTO toDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    @Override
    void patch(User request, @MappingTarget User toPatch);
}
