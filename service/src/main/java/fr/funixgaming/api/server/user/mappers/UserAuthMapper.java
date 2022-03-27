package fr.funixgaming.api.server.user.mappers;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.server.user.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAuthMapper {
    UserDTO toDto(UserCreationDTO dto);

}
