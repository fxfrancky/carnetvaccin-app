package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UtilisateurFacadeIT {
    @InjectMocks
    private UtilisateurFacade facade;

    @Mock
    private UtilisateurService service;

    @Mock
    private UtilisateurMapper mapper;

    private Utilisateur utilisateur;
    private UtilisateurDTO utilisateurDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock test data
        utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L);
        utilisateur.setFirstName("John");
        utilisateur.setLastName("Doe");
        utilisateur.setUserName("johndoe");
        utilisateur.setEmail("johndoe@example.com");
        utilisateur.setEncryptedPassword("hashedPassword123");
        utilisateur.setToken(UUID.randomUUID().toString());

        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(1L);
        utilisateurDTO.setFirstName("John");
        utilisateurDTO.setLastName("Doe");
        utilisateurDTO.setUserName("johndoe");
        utilisateurDTO.setEmail("johndoe@example.com");
        utilisateurDTO.setDateNaissance("11/03/1990");
        utilisateurDTO.setPlainPassword("password!123");

        when(mapper.toDto(utilisateur)).thenReturn(utilisateurDTO);
        when(mapper.toEntity(utilisateurDTO)).thenReturn(utilisateur);
    }

    @Test
    void testGetUserByEmail_Success() {
        when(service.getUserByEmail("johndoe@example.com")).thenReturn(Optional.of(utilisateur));

        UtilisateurDTO result = facade.getUserByEmail("johndoe@example.com");
        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(service, times(1)).getUserByEmail("johndoe@example.com");
    }

    @Test
    void testGetUserByUserName_Success() {
        when(service.getUserByUserName("johndoe")).thenReturn(Optional.of(utilisateur));

        UtilisateurDTO result = facade.getUserByUserName("johndoe");
        assertNotNull(result);
        assertEquals("Doe", result.getLastName());

        verify(service, times(1)).getUserByUserName("johndoe");
    }

    @Test
    void testRegisterUtilisateur_Success() {
        when(service.registerUser(any(Utilisateur.class))).thenReturn(utilisateur);

        UtilisateurDTO result = facade.registerUtilisateur(utilisateurDTO);
        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(service, times(1)).registerUser(any(Utilisateur.class));
    }

    @Test
    void testLoginUser_Success() {
        when(service.loginUser("johndoe", "password!123")).thenReturn(utilisateur);

        UtilisateurDTO result = facade.loginUser("johndoe", "password!123");
        assertNotNull(result);
        assertEquals("johndoe@example.com", result.getEmail());

        verify(service, times(1)).loginUser("johndoe", "password!123");
    }

    @Test
    void testLoginWithToken_Success() {
        when(service.findByToken(utilisateur.getToken())).thenReturn(Optional.of(utilisateur));

        UtilisateurDTO result = facade.loginWithToken(utilisateur.getToken());
        assertNotNull(result);
        assertEquals("John", result.getFirstName());

        verify(service, times(1)).findByToken(utilisateur.getToken());
    }

    @Test
    void testDeleteUserAccount_Success() throws CarnetException {
        doNothing().when(service).deleteUserAccount(any(Utilisateur.class));

        facade.deleteUserAccount(utilisateurDTO);

        verify(service, times(1)).deleteUserAccount(any(Utilisateur.class));
    }
}
