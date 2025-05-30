package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class UtilisateurService extends AbstractService<Utilisateur> {

    @PersistenceContext(unitName = "carnetvaccin-PU")
    private EntityManager em;

    private static final Logger logger = Logger.getLogger(UtilisateurService.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurService() {
        super(Utilisateur.class);
    }

    /**
     * Get User By Email
     *
     * @param email
     * @return
     */
    public Optional<Utilisateur> getUserByEmail(String email) {
        try {
            TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByEmail", Utilisateur.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding user by email", e);
            return Optional.empty();
        }
    }

    /**
     * Find all active Utilisateurs
     * @return
     */
    public List<Utilisateur> getAllActiveUtilisateurs() {
        try {
            TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getAllActiveUtilisateurs", Utilisateur.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new CarnetException("Error finding active users" + e.getMessage());
        }
    }

    /**
     * Get User By UserName
     *
     * @param userName
     * @return
     */
    public Optional<Utilisateur> getUserByUserName(String userName) {
        try {
            TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByUserName", Utilisateur.class);
            query.setParameter("userName", userName);
            return query.getResultStream().findFirst(); // Use Optional.ofNullable
        } catch (Exception e) {
            throw new CarnetException("Error finding user by userName " + e.getMessage());
        }
    }


    /**
     * Delete A user Account
     *
     * @param utilisateur
     * @throws CarnetException
     */
    @Transactional
    public void deleteUserAccount(Utilisateur utilisateur) throws CarnetException {

        try {
            Query query = em.createNamedQuery("Utilisateur.deleteUtilisateur");
            query.setParameter("userName", utilisateur.getUserName());
            query.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("Error updating user by username " + e.getMessage());
        }

    }


    /**
     * Login a user
     *
     * @param username
     * @param plainPassword
     * @return
     */
    @Transactional
    public Utilisateur loginUser(String username, String plainPassword) {
        try {
            Optional<Utilisateur> user = getUserByUserName(username);
            if (user.isPresent()) {
                Utilisateur foundUser = user.get();
                if (checkPassword(plainPassword, foundUser.getEncryptedPassword())) {
                    String token = UUID.randomUUID().toString();
                    foundUser.setToken(token);
                    Utilisateur savedUser = update(foundUser);
                    return foundUser;
                }
            }
            return null;
        } catch (NoResultException e) {
            throw new CarnetException("Invalid username or password");
        } catch (Exception e) {
            throw new CarnetException(e.getMessage());
        }
    }

    /**
     * Check if the username is already taken
     * @param userName
     * @return
     */
    public boolean isUserNameTaken(String userName) {
        try{
            return !em.createNamedQuery("Utilisateur.getUserByUserName", Utilisateur.class)
                    .setParameter("userName", userName)
                    .getResultList()
                    .isEmpty();
        }catch (Exception ex){
            throw new CarnetException("An error occur trying to get a user by userName");
        }

    }

    /**
     * Check if the email is already taken
     * @param email
     * @return
     */
    public boolean isEmailTaken(String email) {
        try{
            return !em.createNamedQuery("Utilisateur.getUserByEmail", Utilisateur.class)
                    .setParameter("email", email)
                    .getResultList()
                    .isEmpty();
        }catch (Exception ex){
            throw new CarnetException("An error occur trying to get a user by email");
        }

    }


    /**
     *  Validate User Token
     * @param token
     * @return
     */
    public Optional<Utilisateur> findByToken(String token) throws CarnetException {

        try {
            TypedQuery<Utilisateur> query = em.createNamedQuery("Utilisateur.getUserByToken", Utilisateur.class);
            query.setParameter("token", token);
            return Optional.ofNullable(query.getSingleResult()); // Use Optional.ofNullable
        } catch (Exception ex){
            throw new CarnetException("A Issue Occurs validating Token");
        }
    }


    /**
     * Register a new user
     * @param userEntity
     * @return
     */
    @Transactional
    public Utilisateur registerUser(Utilisateur userEntity) {
        try {

            if (isUserNameTaken(userEntity.getUserName())) {
                throw new CarnetException("Username is already taken");
            }
            if (isEmailTaken(userEntity.getEmail())) {
                throw new CarnetException("Email is already taken. You may already have an account.");
            }
            userEntity.addRole(Role.User);
            if(userEntity.isAdmin()){
                userEntity.addRole(Role.ADMIN);
            }
            create(userEntity);
            em.flush();
            // Send email
//            emailService.sendEmail(email, "Welcome!", "Hi " + firstName + ", your registration was successful!");
            return userEntity;
        } catch (Exception e) {
            throw new CarnetException("Error during registration: " + e.getMessage());
        }
    }

    /**
     * To hash a password
     * @param password
     * @return
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Compare PlainPassword to Hashpassword
     * @param password
     * @param storedHash
     * @return
     */
    public boolean checkPassword(String password, String storedHash) {
        try {
            return BCrypt.checkpw(password, storedHash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CarnetException("Error checking password: in password Service ");
        }
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
