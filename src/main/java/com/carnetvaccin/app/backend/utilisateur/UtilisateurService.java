package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.api.utilisateur.UtilisateurMapper;
import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.UUID;

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
        return query.getSingleResult();

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
            throw new CarnetException("wrong credentials");
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


    /**
     * Delete A user Account
     * @param utilisateur
     * @throws CarnetException
     */
    public void deleteUserAccount(Utilisateur utilisateur) throws CarnetException{

        try{
            remove(utilisateur);
        } catch (Exception e) {
            throw new CarnetException("An error occurs while deleting a user ");
        }
    }


    /**
     *  Login a user
     * @param username
     * @param plainPassword
     * @return
     */
    public String loginUser(String username, String plainPassword) {
        try {
            Utilisateur user = getUserByUserName(username);
            if (user != null && user.getEncryptedPassword().equals(UtilisateurMapper.encryptPassword(plainPassword))){
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                update(user);
                return token;
            } else {
                throw new CarnetException("Invalid password");
            }
        } catch (NoResultException e) {
            throw new CarnetException("Invalid username or password");
        }
    }

    /**
     * Check if the username is already taken
     * @param userName
     * @return
     */
    public boolean isUserNameTaken(String userName) {
            return !em.createNamedQuery("Utilisateur.getUserByUserName", Utilisateur.class)
                    .setParameter("userName", userName)
                    .getResultList()
                    .isEmpty();
    }

    /**
     * Check if the email is already taken
     * @param email
     * @return
     */
    public boolean isEmailTaken(String email) {
        return !em.createNamedQuery("Utilisateur.getUserByEmail", Utilisateur.class)
                .setParameter("email", email)
                .getResultList()
                .isEmpty();
    }

    /**
     *  Validate User Token
     * @param token
     * @return
     */
    public Utilisateur validateToken(String token) {
        try {
            return em.createNamedQuery("Utilisateur.getUserByToken", Utilisateur.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Register a new user
     * @param userEntity
     * @return
     */
    public String registerUser(Utilisateur userEntity) {
        try {

            if (isUserNameTaken(userEntity.getUserName())) {
                throw new CarnetException("Username is already taken");
            }
            if (isEmailTaken(userEntity.getEmail())) {
                throw new CarnetException("Email is already taken. You may already have an account.");
            }
            create(userEntity);

            // Send email
//            emailService.sendEmail(email, "Welcome!", "Hi " + firstName + ", your registration was successful!");
            return "Registration Successful";
        } catch (Exception e) {
            throw new CarnetException("Error during registration: " + e.getMessage());
        }
    }


}
