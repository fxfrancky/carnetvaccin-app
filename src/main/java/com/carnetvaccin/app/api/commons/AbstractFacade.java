package com.carnetvaccin.app.api.commons;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.commons.BaseEntity;

import javax.persistence.criteria.Predicate;
import java.util.List;

public abstract class AbstractFacade<Entity extends BaseEntity, DTO extends BaseDto, Service extends AbstractService<Entity>, Mapper extends AbstractMapper<Entity, DTO>> {

    protected Class<Entity> entityClass;
    protected Class<DTO> dtoClass;

    public AbstractFacade() {
    }

    protected abstract Service getService();

    protected abstract Mapper getMapper();

    public AbstractFacade(Class<DTO> dtoClass, Class<Entity> entityClass) {
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    public void create(DTO entityDTO){
        getService().create(getMapper().toEntity(entityDTO));
    }

    public void update(DTO entityDTO){
        getService().update(getMapper().toEntity(entityDTO));
    }

    public void remove(DTO entityDTO){
        getService().remove(getMapper().toEntity(entityDTO));
    }
    public DTO findById(Long id){
        return  getMapper().toDto(getService().findById(id));
    }

    List<DTO> findAll(){
        return getMapper().toDtoList(getService().findAll());
    }

    List<DTO> findAll(Predicate[] predicates){
        return getMapper().toDtoList(getService().findAll(predicates));
    }

    List<DTO> findAll(int offset, int limit){
        return getMapper().toDtoList(getService().findAll(offset,limit));
    }

    List<DTO> findAll(Predicate[] predicates, int offset, int limit){
        return  getMapper().toDtoList(getService().findAll(predicates,offset,limit));
    }

    public Long count(){
        return getService().count();
    }

}
