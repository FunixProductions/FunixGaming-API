package fr.funixgaming.api.server.user.mappers;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.server.user.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper extends ApiMapper<User, UserDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    UserDTO toDto(User entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(User request, @MappingTarget User toPatch);
}
