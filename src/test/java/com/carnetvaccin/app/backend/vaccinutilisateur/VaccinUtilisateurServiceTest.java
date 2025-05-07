package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VaccinUtilisateurServiceTest extends AbstractServiceTest {

    @Inject
    private VaccinUtilisateurService service;

    @BeforeEach
    void initService() {
        service = new VaccinUtilisateurService();
        service.setEm(entityManager);

        entityManager.getTransaction().begin();

        // Create and persist Utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        entityManager.merge(utilisateur);

        // Create and persist Vaccin
        Vaccin vaccin = new Vaccin();
        vaccin.setVaccinId(1L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(1);
        vaccin.setNbrMonthsDose(1);
        vaccin.setTypeVaccin(TypeVaccinEnum.RSV);
        entityManager.merge(vaccin);

        // Create and persist VaccinUtilisateur with nested objects
        // Insert 10 VaccinUtilisateur entries
        for (int i = 1; i <= 10; i++) {
            VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
            vaccinUtilisateur.setVaccinUtilisateurId((long) i);
            vaccinUtilisateur.setUtilisateur(utilisateur);
            vaccinUtilisateur.setVaccin(vaccin);
            vaccinUtilisateur.setCommentairesVaccin("Test Entry " + i);
            entityManager.merge(vaccinUtilisateur);
        }

        entityManager.getTransaction().commit();
    }

    @Test
    void testFindByUtilisaterIDAndVaccinId_Success() throws CarnetException {
        TypedQuery<VaccinUtilisateur> query = entityManager.createNamedQuery(
                "VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
        query.setParameter("vaccinId", 1L);
        query.setParameter("utilisateurId", 1L);
        List<VaccinUtilisateur> results = query.getResultList();

        assertNotNull(results);
        assertEquals(10, results.size());

    }

    @Test
    void testSaveVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
        entityManager.getTransaction().begin();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        VaccinUtilisateur retrieved = entityManager.find(VaccinUtilisateur.class, vaccinUtilisateur.getVaccinUtilisateurId());
        assertNotNull(retrieved);
    }

    @Test
    void testUpdateVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
        entityManager.getTransaction().begin();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        vaccinUtilisateur.setCommentairesVaccin("Updated description");
        VaccinUtilisateur updated = service.updateVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        assertEquals("Updated description", updated.getCommentairesVaccin());
    }

    @Test
    void testDeleteVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
        entityManager.getTransaction().begin();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        service.deleteVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        assertNull(entityManager.find(VaccinUtilisateur.class, vaccinUtilisateur.getVaccinUtilisateurId()));
    }
}
