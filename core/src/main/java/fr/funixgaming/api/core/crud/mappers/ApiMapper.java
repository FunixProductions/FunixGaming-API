package fr.funixgaming.api.core.crud.mappers;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.crud.entities.ApiEntity;

/**
 * Example class impl
 *
 * @Mapper(componentModel = "spring")
 * public interface FunixBotCommandMapper extends ApiMapper<FunixBotCommand, FunixBotCommandDTO> {
 *     @Mapping(target = "uuid", source = "id")
 *     @Mapping(target = "id", ignore = true)
 *     FunixBotCommand toEntity(FunixBotCommandDTO dto);
 *
 *     @Mapping(target = "id", source = "uuid")
 *     FunixBotCommandDTO toDto(FunixBotCommand entity);
 *
 *     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
 *     void patch(FunixBotCommand request, @MappingTarget FunixBotCommand toPatch);
 * }
 *
 * @param <ENTITY> entity db
 * @param <DTO> dto api
 */
public interface ApiMapper<ENTITY extends ApiEntity, DTO extends ApiDTO> {
    ENTITY toEntity(DTO dto);
    DTO toDto(ENTITY entity);
    void patch(ENTITY request, ENTITY toPatch);
}
