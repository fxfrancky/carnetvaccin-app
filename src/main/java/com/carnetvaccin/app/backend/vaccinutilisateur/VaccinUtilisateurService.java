package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Stateless
@LocalBean
public class VaccinUtilisateurService extends AbstractService<VaccinUtilisateur> {

    @PersistenceContext(unitName="carnetvaccin-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VaccinUtilisateurService() {
        super(VaccinUtilisateur.class);
    }

    /**
     * Find VaccinUtilisateur
     * @param vaccinId
     * @param utilisateurId
     * @return
     * @throws CarnetException
     */
    public Optional<VaccinUtilisateur> findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) throws CarnetException {

        try {
            TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
            query.setParameter("vaccinId", vaccinId );
            query.setParameter("utilisateurId",utilisateurId);
            return query.getResultStream().findFirst();
        } catch (Exception ex){
            throw new CarnetException("Error finding vaccinutilisateur by VaccinId and utilisateurId");
        }
    }


    /**
     * find All VaccinUtilisateur By UserId
     * @param utilisateurId
     * @return
     * @throws CarnetException
     */
    public List<VaccinUtilisateur> findAllVaccinUtilisateurByUserId(Long utilisateurId) throws CarnetException {
    TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findAllVaccinUtilisateurByUserId", VaccinUtilisateur.class);
    query.setParameter("utilisateurId",utilisateurId);
    try {
        return query.getResultList();
    } catch (Exception ex){
        throw new CarnetException("An error occurs");
    }
}

    /**
     * Find by terms
     * @param searchTerm
     * @param utilisateurId
     * @return
     * @throws CarnetException
     */
    public List<VaccinUtilisateur> findByTerms(String searchTerm, Long utilisateurId) throws CarnetException {
    List<VaccinUtilisateur> vaccinUtilisateurList = new ArrayList<>();
    try {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // Return all vaccinUtilisateur if no search term provided
            vaccinUtilisateurList = findAllVaccinUtilisateurByUserId(utilisateurId);
            return vaccinUtilisateurList;
        }
        // Prepare the search term for a LIKE query.
        String term = "%" + searchTerm.toLowerCase() + "%";
        // Execute the NamedQuery
        TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findrByTerms", VaccinUtilisateur.class);
        query.setParameter("term", term);
        query.setParameter("utilisateurId",utilisateurId);
        vaccinUtilisateurList = query.getResultList();
//            return query.getResultList();
    } catch (Exception ex) {
        throw new CarnetException("An error occurs");
    }
    return vaccinUtilisateurList;
}

    /**
     *  Save Or Update
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    @Transactional
    public void saveOrUpdate(VaccinUtilisateur vaccinUtilisateur) throws CarnetException{

        try {
            if(vaccinUtilisateur.isPersisted()){ // then update
                update(vaccinUtilisateur);
            }else { // save
                create(vaccinUtilisateur);
            }
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("An error occurs while saving a vaccin " + e.getMessage());
        }
    }

    /**
     * Create Vaccin Utilisateur
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    @Transactional
    public void saveVaccinUtilisateur(VaccinUtilisateur vaccinUtilisateur) throws CarnetException{
        try{
            create(vaccinUtilisateur);
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("An error occurs while saving a vaccin utilisateur");
        }
    }

    /**
     * update a vaccin utilisateur
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    @Transactional
    public VaccinUtilisateur updateVaccinUtilisateur(VaccinUtilisateur vaccinUtilisateur) throws CarnetException {
        try {
            VaccinUtilisateur vaccin = update(vaccinUtilisateur);
            em.flush();
            return vaccin;
        } catch (Exception ex) {
            throw new CarnetException("An error occurs while updating a vaccin");
        }
    }

    /**
     * Create Vaccin Utilisateur
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    @Transactional
    public void deleteVaccinUtilisateur(VaccinUtilisateur vaccinUtilisateur) throws CarnetException{

        try{
            remove(vaccinUtilisateur);
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("An error occurs while saving a vaccin utilisateur");
        }
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
