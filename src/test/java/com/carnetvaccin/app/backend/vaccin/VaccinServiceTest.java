package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VaccinServiceTest extends AbstractServiceTest {

//    @Inject
    private VaccinService service;

    @BeforeEach
    void initService() {
        service = new VaccinService();
        service.setEm(entityManager);
        // Initialize test data
        entityManager.clear();
        entityManager.getTransaction().begin();

        Vaccin vaccin1 = new Vaccin();
        vaccin1.setTypeVaccin(TypeVaccinEnum.RSV);
        vaccin1.setNumDose(1);
        vaccin1.setVaccinId(1L);
        entityManager.merge(vaccin1);


        Vaccin vaccin2 = new Vaccin();
        vaccin2.setTypeVaccin(TypeVaccinEnum.COMBINE_RORV);
        vaccin2.setNumDose(2);
        vaccin2.setVaccinId(2L);
        entityManager.merge(vaccin2);

        entityManager.getTransaction().commit();
    }


    @Test
    void testFindVaccinByTypeAndDose_Success() throws CarnetException {
        Optional<Vaccin> result = service.findVaccinByTypeAndDose("RSV", 1);
        assertTrue(result.isPresent());
        assertEquals(TypeVaccinEnum.RSV, result.get().getTypeVaccin());

        Optional<Vaccin> result2 = service.findVaccinByTypeAndDose("COMBINE_RORV", 2);
        assertTrue(result2.isPresent());
        assertEquals(TypeVaccinEnum.COMBINE_RORV, result2.get().getTypeVaccin());
    }

    @Test
    @Transactional
    void testCreateVaccin_Success() throws CarnetException {
        Vaccin newVaccin = new Vaccin();
        newVaccin.setTypeVaccin(TypeVaccinEnum.COMBINE_RORV);
        newVaccin.setNumDose(1);

//      vaccinService.createVaccin(newVaccin);
        entityManager.getTransaction().begin();
        service.createVaccin(newVaccin);
        entityManager.merge(newVaccin);
        entityManager.getTransaction().commit();

        Vaccin retrieved = entityManager.find(Vaccin.class, newVaccin.getVaccinId());
        assertNotNull(retrieved);
        assertEquals(TypeVaccinEnum.COMBINE_RORV, retrieved.getTypeVaccin());
    }

    @Test
    void testFindAllVaccin_Success() throws CarnetException {
        List<Vaccin> vaccines = service.findAllVaccin();
        assertEquals(2, vaccines.size()); // Expecting 2 vaccines from @BeforeEach setup
    }

    @AfterEach
    void cleanup() {
        cleanDatabase(); // Clean the database after each test
    }
}
