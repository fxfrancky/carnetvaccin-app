package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurServiceTest  extends AbstractServiceTest {

    private UtilisateurService service;

    @BeforeEach
    void initService() {
        service = new UtilisateurService();
        service.setEm(entityManager);

        entityManager.clear();

        startTransaction();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L);
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        utilisateur.setEncryptedPassword(service.hashPassword("Password1!")); // Hash password
        entityManager.merge(utilisateur);
        entityManager.getTransaction().commit();
    }

    @Test
    void testGetUserByEmail_Success() {
        Optional<Utilisateur> user = service.getUserByEmail("fxfre@yahoo.com");
        assertTrue(user.isPresent());
        assertEquals("Franck", user.get().getUserName());
    }

    @Test
    void testGetUserByUserName_Success() {
        Optional<Utilisateur> user = service.getUserByUserName("Franck");
        assertTrue(user.isPresent());
        assertEquals("fxfre@yahoo.com", user.get().getEmail());
    }

    @Test
    void testIsUserNameTaken_Success() {
        assertTrue(service.isUserNameTaken("Franck"));
    }

    @Test
    void testIsEmailTaken_Success() {
        assertTrue(service.isEmailTaken("fxfre@yahoo.com"));
    }

    @Test
    void testRegisterUser_Success() {
        Utilisateur newUser = new Utilisateur();
        newUser.setUserName("JohnDoe");
        newUser.setEmail("johndoe@example.com");
        newUser.setEncryptedPassword(service.hashPassword("Password1!"));

        startTransaction();
        Utilisateur registeredUser = service.registerUser(newUser);
        entityManager.getTransaction().commit();
        assertNotNull(entityManager.find(Utilisateur.class, registeredUser.getUtilisateurId()));
    }

    @Test
    void testLoginUser_Success() {
        Utilisateur loggedInUser = service.loginUser("Franck", "Password1!");
        assertNotNull(loggedInUser);
        assertNotNull(loggedInUser.getToken());
    }

    @Test
    void testLoginUser_Failure() {
        Utilisateur loggedInUser = service.loginUser("Franck", "WrongPassword1!");
        assertNull(loggedInUser); // Invalid password should return null
    }

    @Test
    void testFindByToken_Success() {
        Utilisateur loggedInUser;
        startTransaction();
            loggedInUser = service.loginUser("Franck", "Password1!");
        entityManager.getTransaction().commit();
        assertNotNull(loggedInUser);

        Optional<Utilisateur> userByToken = service.findByToken(loggedInUser.getToken());
        assertTrue(userByToken.isPresent());
        assertEquals("Franck", userByToken.get().getUserName());
    }

    @Test
    void testDeleteUserAccount_Success() {
        Utilisateur user = entityManager.find(Utilisateur.class, 1L);
        assertNotNull(user);

        startTransaction();
        service.deleteUserAccount(user);
        entityManager.getTransaction().commit();
        assertNull(service.getUserByUserName(user.getUserName()).orElse(null));
        assertNull(service.getUserByEmail(user.getEmail()).orElse(null));
//        assertNull(service.getUserByEmail(user.getEmail()));
//        assertNull(entityManager.find(Utilisateur.class, 1L)); // User should be deleted
    }

    @AfterEach
    void cleanup() {
        cleanDatabase(); // Clean the database after each test
    }
}
