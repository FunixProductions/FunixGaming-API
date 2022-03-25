package fr.funixgaming.api.core.mappers;

import fr.funixgaming.api.core.dtos.ApiDTO;
import fr.funixgaming.api.core.entities.ApiEntity;

public interface ApiMapper<ENTITY extends ApiEntity, DTO extends ApiDTO> {
    ENTITY toEntity(DTO dto);
    DTO toDto(ENTITY entity);
    void patch(ENTITY request, ENTITY toPatch);
}
