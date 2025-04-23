package com.carnetvaccin.app.backend.commons;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public abstract class AbstractService<T> {

    private final Class<T> entityClass;

    protected AbstractService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Create an object
     * @param entity
     */
    public void create(T entity){
        getEntityManager().persist(entity);
    }

    /**
     * Update a single Object
     * @param entity
     */
    public void update(T entity){
        getEntityManager().merge(entity);
    }

    public void remove(Predicate[] predicates){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(entityClass);
        Root<T> root = criteriaDelete.from(entityClass);
        criteriaDelete.where(predicates);
        getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * Find a single Object
     * @param id
     * @return
     */
    public T findById(Object id){
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Find All entities
     * @return
     */
    public List<T> findAll(){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = getEntityManager().createQuery(all);
        return allQuery.getResultList();
    }

    /**
     * Select By Predicates to filter
     * @param predicates
     * @return
     */
    public List<T> findAll(Predicate[] predicates){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry).where(predicates);
        TypedQuery<T> allQuery = getEntityManager().createQuery(all);
        return allQuery.getResultList();
    }

     /**
     * FindAll with pagination
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findAll(int offset, int limit){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = getEntityManager().createQuery(all);
        allQuery.setFirstResult(offset);
        allQuery.setMaxResults(limit);
        return allQuery.getResultList();
    }

    /**
     * FindAll with pagination and predicates
     * @param predicates
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findAll(Predicate[] predicates, int offset, int limit){
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry).where(predicates);
        TypedQuery<T> allQuery = getEntityManager().createQuery(all);
        allQuery.setFirstResult(offset);
        allQuery.setMaxResults(limit);
        return allQuery.getResultList();
    }
    /**
     * count entities
     * @return
     */
    public Long count() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> entityRoot = cq.from(entityClass);
        cq.select(cb.count(entityRoot));
        return getEntityManager().createQuery(cq).getSingleResult();
    }
}
