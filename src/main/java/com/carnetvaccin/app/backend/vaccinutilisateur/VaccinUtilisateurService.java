package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

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

public VaccinUtilisateur findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) throws CarnetException {
    TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
    query.setParameter("vaccinId", vaccinId );
    query.setParameter("utilisateurId",utilisateurId);
//    return query.getResultStream().findFirst().orElse(null);
    try {
        return query.getSingleResult();
    } catch (Exception ex){
        throw new CarnetException("An error occurss",ex);
    }
}

public List<VaccinUtilisateur> findAllVaccinUtilisateurByUserId(Long utilisateurId) throws CarnetException {
    TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findAllVaccinUtilisateurByUserId", VaccinUtilisateur.class);
    query.setParameter("utilisateurId",utilisateurId);

    try {
        return query.getResultList();
    } catch (Exception ex){
        throw new CarnetException("An error occurss",ex);
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
        throw new CarnetException("An error occurss",ex);
    }
    return vaccinUtilisateurList;
}

    /**
     *  Save Or Update
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    public void saveOrUpdate(VaccinUtilisateur vaccinUtilisateur) throws CarnetException{

        try {
            if(vaccinUtilisateur.isPersisted()){ // then update
                update(vaccinUtilisateur);
            }else { // save
                create(vaccinUtilisateur);
            }
        } catch (Exception e) {
            throw new CarnetException("An error occurs while saving a vaccin ", e);
        }
    }

    /**
     * Delete Vaccin
     * @param vaccinUtilisateur
     * @throws CarnetException
     */
    public void deleteVaccin(VaccinUtilisateur vaccinUtilisateur) throws CarnetException{

        try{
            remove(vaccinUtilisateur);
        } catch (Exception e) {
            throw new CarnetException("An error occurs while deleting a vaccin ", e);
        }
    }

}
