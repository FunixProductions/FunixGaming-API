package fr.funixgaming.api.server.funixbot.mappers;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUser;
import fr.funixgaming.api.server.funixbot.mappers.user.FunixBotUserExperienceMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        FunixBotUserExperienceMapper.class
})
public interface FunixBotUserMapper extends ApiMapper<FunixBotUser, FunixBotUserDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastMessageDate", expression = "java(java.util.Date.from(java.time.Instant.ofEpochSecond(dto.getLastMessageDateSeconds())))")
    FunixBotUser toEntity(FunixBotUserDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "lastMessageDateSeconds", expression = "java(entity.getLastMessageDate().toInstant().getEpochSecond())")
    FunixBotUserDTO toDto(FunixBotUser entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(FunixBotUser request, @MappingTarget FunixBotUser toPatch);
}
