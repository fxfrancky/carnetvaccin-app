package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;



public class VaccinUtilisateurFacadeIT {

    @InjectMocks
    private VaccinUtilisateurFacade facade;

    @Mock
    private VaccinUtilisateurService service;

    @Mock
    private VaccinUtilisateurMapper mapper;

    private VaccinUtilisateur vaccinUtilisateur;
    private VaccinUtilisateurDTO vaccinUtilisateurDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito mocks

        // Mock test data
        vaccinUtilisateur = new VaccinUtilisateur();
        vaccinUtilisateur.setVaccinUtilisateurId(1L);
        vaccinUtilisateur.setCommentairesVaccin("Test Entry");

        vaccinUtilisateurDTO = new VaccinUtilisateurDTO();
        vaccinUtilisateurDTO.setId(1L);
        vaccinUtilisateurDTO.setCommentairesVaccin("Test Entry");

        when(mapper.toDto(vaccinUtilisateur)).thenReturn(vaccinUtilisateurDTO);
        when(mapper.toEntity(vaccinUtilisateurDTO)).thenReturn(vaccinUtilisateur);
    }

    @Test
    void testFindByUtilisaterIDAndVaccinId_Success() throws Exception {
        when(service.findByUtilisaterIDAndVaccinId(1L, 1L)).thenReturn(Optional.of(vaccinUtilisateur));

        VaccinUtilisateurDTO result = facade.findByUtilisaterIDAndVaccinId(1L, 1L);
        assertNotNull(result);
        assertEquals("Test Entry", result.getCommentairesVaccin());

        verify(service, times(1)).findByUtilisaterIDAndVaccinId(1L, 1L);
    }

    @Test
    void testSaveVaccinUtilisateur_Success() throws Exception {
        doNothing().when(service).saveVaccinUtilisateur(any(VaccinUtilisateur.class));

        facade.saveVaccinUtilisateur(vaccinUtilisateurDTO);

        verify(service, times(1)).saveVaccinUtilisateur(any(VaccinUtilisateur.class));
    }

    @Test
    void testUpdateVaccinUtilisateur_Success() throws Exception {
        when(service.updateVaccinUtilisateur(any(VaccinUtilisateur.class))).thenReturn(vaccinUtilisateur);

        VaccinUtilisateurDTO result = facade.updateVaccinUtilisateur(vaccinUtilisateurDTO);
        assertEquals("Test Entry", result.getCommentairesVaccin());

        verify(service, times(1)).updateVaccinUtilisateur(any(VaccinUtilisateur.class));
    }

    @Test
    void testDeleteVaccinUtilisateur_Success() throws Exception {
        doNothing().when(service).deleteVaccinUtilisateur(any(VaccinUtilisateur.class));

        facade.deleteVaccin(vaccinUtilisateurDTO);

        verify(service, times(1)).deleteVaccinUtilisateur(any(VaccinUtilisateur.class));
    }
}
