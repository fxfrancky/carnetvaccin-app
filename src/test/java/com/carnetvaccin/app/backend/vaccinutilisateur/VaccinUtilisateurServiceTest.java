package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VaccinUtilisateurServiceTest extends AbstractServiceTest {

    private VaccinUtilisateurService service;

    @BeforeEach
    void initService() {
        service = new VaccinUtilisateurService();
        service.setEm(entityManager);

        entityManager.clear();

        //      vaccinService.createVaccin(newVaccin);
        startTransaction();
        // Create and persist Utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        entityManager.merge(utilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        Vaccin vaccin = new Vaccin();
        vaccin.setVaccinId(2L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);
        vaccin.setNbrMonthsDose(4);
        vaccin.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);
        entityManager.merge(vaccin);
        entityManager.getTransaction().commit();

        // Create and persist VaccinUtilisateur with nested objects
        // Insert 10 VaccinUtilisateur entries
        for (int i = 1; i <= 10; i++) {
            VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
            vaccinUtilisateur.setVaccinUtilisateurId((long) i);
            vaccinUtilisateur.setUtilisateur(utilisateur);
            vaccinUtilisateur.setCommentairesVaccin("Test Entry " + i);
            if (!entityManager.contains(vaccinUtilisateur.getVaccin())) {
                vaccinUtilisateur.setVaccin(entityManager.merge(vaccin));
            }

            startTransaction();
            entityManager.merge(vaccinUtilisateur);
            entityManager.getTransaction().commit();
        }

    }



    @Test
    void testFindByUtilisaterIDAndVaccinId_Success() throws CarnetException {
        TypedQuery<VaccinUtilisateur> query = entityManager.createNamedQuery(
                "VaccinUtilisateur.findByUtilisaterIDAndVaccinId", VaccinUtilisateur.class);
        query.setParameter("vaccinId", 2L);
        query.setParameter("utilisateurId", 1L);
        List<VaccinUtilisateur> results = query.getResultList();

        assertNotNull(results);
        assertEquals(10, results.size());

    }

    @Test
    void testSaveVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
//        entityManager.getTransaction().begin();
        startTransaction();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        VaccinUtilisateur retrieved = entityManager.find(VaccinUtilisateur.class, vaccinUtilisateur.getVaccinUtilisateurId());
        assertNotNull(retrieved);
    }

    @Test
    void testUpdateVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
        startTransaction();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        vaccinUtilisateur.setCommentairesVaccin("Updated description");
        VaccinUtilisateur updated = service.updateVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        assertEquals("Updated description", updated.getCommentairesVaccin());
    }

    @Test
    void testDeleteVaccinUtilisateur() throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = new VaccinUtilisateur();
        startTransaction();
        service.saveVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        service.deleteVaccinUtilisateur(vaccinUtilisateur);
        entityManager.getTransaction().commit();

        assertNull(entityManager.find(VaccinUtilisateur.class, vaccinUtilisateur.getVaccinUtilisateurId()));
    }

    @AfterEach
    void cleanup() {
        cleanDatabase(); // Clean the database after each test
    }
}
