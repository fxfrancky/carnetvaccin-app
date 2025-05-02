package com.carnetvaccin.app.api.commons;

import com.carnetvaccin.app.backend.commons.BaseEntity;

import java.util.List;

public abstract class AbstractMapper<Entity extends BaseEntity, DTO extends BaseDto> {

    private final Class<Entity> entity;

    private final Class<DTO> dto;

    public AbstractMapper(Class<Entity> entity, Class<DTO> dto) {
        this.entity = entity;
        this.dto = dto;
    }

    public abstract DTO toDto(Entity entity);

    public abstract Entity toEntity(DTO dto);

    public abstract List<Entity> toEntityList(List<DTO> dtoList);
    public abstract List<DTO> toDtoList(List<Entity> entityList);
}
