package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.backend.commons.AbstractService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class UtilisateurService extends AbstractService<Utilisateur> {

    @PersistenceContext(unitName="carnetvaccin-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurService() {
        super(Utilisateur.class);
    }

    /**
     * Find User By Email
     * @param email
     * @return
     */
    public Utilisateur findUserByEmail(String email){
        TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.findUserByEmail", Utilisateur.class);
        query.setParameter("email",email);
        return query.getSingleResult();
    }

    public void login(){}

    public void logout(){}

    public void Register(){}
}
