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

    public Optional<VaccinUtilisateur> findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) throws CarnetException {

        try {
            TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
            query.setParameter("vaccinId", vaccinId );
            query.setParameter("utilisateurId",utilisateurId);
            return Optional.ofNullable(query.getSingleResult()); // Use Optional.ofNullable

        } catch (Exception ex){
            throw new CarnetException("Error finding vaccinutilisateur by VaccinId and utilisateurId");
        }
    }



public List<VaccinUtilisateur> findAllVaccinUtilisateurByUserId(Long utilisateurId) throws CarnetException {
    TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findAllVaccinUtilisateurByUserId", VaccinUtilisateur.class);
    query.setParameter("utilisateurId",utilisateurId);

    try {
        return query.getResultList();
    } catch (Exception ex){
        throw new CarnetException("An error occurss");
    }
}

public List<VaccinUtilisateur> findrByTerms(String searchTerm, Long utilisateurId) throws CarnetException {
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
            System.out.println("************Lets verify our Entities");
            System.out.println("************Lets verify our Vaccin " + vaccinUtilisateur.getVaccin());
            System.out.println("************Lets verify our Vaccin Id " + vaccinUtilisateur.getVaccin().getVaccinId());
            System.out.println("************Lets verify our Vaccin TypeVaccin" + vaccinUtilisateur.getVaccin().getTypeVaccin());
            System.out.println("************Lets verify our Entities Utilisateur");
            System.out.println("************Lets verify our Entities " + vaccinUtilisateur.getUtilisateur());
            System.out.println("************Lets verify our Entities Utilisateur Id" + vaccinUtilisateur.getUtilisateur().getUtilisateurId()  );
            System.out.println("************Lets verify our Entities utilisateur UserName" + vaccinUtilisateur.getUtilisateur().getUserName());
            System.out.println("************Lets verify our Entities utilisateur FirstName" + vaccinUtilisateur.getUtilisateur().getFirstName());
            create(vaccinUtilisateur);
            em.flush();
        } catch (Exception e) {
            System.out.println(" --------------- An error occurs while saving a vaccin utilisateur  : " + e.getMessage());
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
}
