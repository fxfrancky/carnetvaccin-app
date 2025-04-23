package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.commons.AbstractService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

public VaccinUtilisateur findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) {
    TypedQuery<VaccinUtilisateur> query = em.createNamedQuery("VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
    query.setParameter("vaccinId", vaccinId );
    query.setParameter("utilisateurId",utilisateurId);
    return query.getSingleResult();
}
}
