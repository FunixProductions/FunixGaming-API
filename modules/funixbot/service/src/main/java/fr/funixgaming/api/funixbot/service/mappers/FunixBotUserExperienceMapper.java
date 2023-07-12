package fr.funixgaming.api.funixbot.service.mappers;

import com.funixproductions.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotUserExperience;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FunixBotUserExperienceMapper extends ApiMapper<FunixBotUserExperience, FunixBotUserExperienceDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastMessageDate", expression = "java(java.util.Date.from(java.time.Instant.ofEpochSecond(dto.getLastMessageDateSeconds())))")
    FunixBotUserExperience toEntity(FunixBotUserExperienceDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "lastMessageDateSeconds", expression = "java(entity.getLastMessageDate().toInstant().getEpochSecond())")
    FunixBotUserExperienceDTO toDto(FunixBotUserExperience entity);
}
