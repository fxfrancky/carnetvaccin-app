package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
     * Get User By Email
     * @param email
     * @return
     */
    public Utilisateur getUserByEmail(String email){
        TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByEmail", Utilisateur.class);
        query.setParameter("email",email);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex){
            return null;
        }

    }

    /**
     * Get User By UserName and Password
     * @param userName
     * @param password
     * @return
     */
    public Utilisateur getUserByUserNameAndPassword(String userName, String password) throws CarnetException {
        TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByUserNameAndPassword", Utilisateur.class);
        query.setParameter("userName",userName);
        query.setParameter("password",password);
        try {
            return query.getSingleResult();
        } catch (Exception ex){
            throw new CarnetException("wrong credentials",ex);
        }
    }


    /**
     * Get User By UserName
     * @param userName
     * @return
     */
    public Utilisateur getUserByUserName(String userName){
        TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByUserName", Utilisateur.class);
        query.setParameter("userName",userName);
        return query.getSingleResult();
    }
    public void login(){}

    public void logout(){}

    public void Register(){}
}
