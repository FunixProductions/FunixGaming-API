package fr.funixgaming.api.server.funixbot.mappers.user;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUserExperience;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FunixBotExperienceMapper extends ApiMapper<FunixBotUserExperience, FunixBotUserExperienceDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    FunixBotUserExperience toEntity(FunixBotUserExperienceDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "user.id", source = "user.uuid")
    FunixBotUserExperienceDTO toDto(FunixBotUserExperience entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(FunixBotUserExperience request, @MappingTarget FunixBotUserExperience toPatch);
}
