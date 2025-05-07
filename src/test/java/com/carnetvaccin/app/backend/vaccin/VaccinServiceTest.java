package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class VaccinServiceTest extends AbstractServiceTest {

    @Inject
    private VaccinService vaccinService;

    @BeforeEach
    void initService() {
        vaccinService = new VaccinService();
        vaccinService.setEm(entityManager);

        // Initialize test data
        entityManager.getTransaction().begin();

        Vaccin vaccin1 = new Vaccin();
        vaccin1.setTypeVaccin(TypeVaccinEnum.RSV);
        vaccin1.setNumDose(1);
        entityManager.merge(vaccin1);

        Vaccin vaccin2 = new Vaccin();
        vaccin2.setTypeVaccin(TypeVaccinEnum.ROTAROVIRUS);
        vaccin2.setNumDose(2);
        entityManager.merge(vaccin2);

        entityManager.getTransaction().commit();
    }

    @Test
    void testFindVaccinByTypeAndDose_Success() throws CarnetException {
        Optional<Vaccin> result = vaccinService.findVaccinByTypeAndDose("RSV", 1);
        assertTrue(result.isPresent());
        assertEquals(TypeVaccinEnum.RSV, result.get().getTypeVaccin());

        Optional<Vaccin> result2 = vaccinService.findVaccinByTypeAndDose("ROTAROVIRUS", 2);
        assertTrue(result2.isPresent());
        assertEquals(TypeVaccinEnum.ROTAROVIRUS, result2.get().getTypeVaccin());
    }

    @Test
    @Transactional
    void testCreateVaccin_Success() throws CarnetException {
        Vaccin newVaccin = new Vaccin();
        newVaccin.setTypeVaccin(TypeVaccinEnum.RSV);
        newVaccin.setNumDose(1);

//      vaccinService.createVaccin(newVaccin);
        entityManager.getTransaction().begin();
        vaccinService.createVaccin(newVaccin);
        entityManager.merge(newVaccin);
        entityManager.getTransaction().commit();

        Vaccin retrieved = entityManager.find(Vaccin.class, newVaccin.getVaccinId());
        assertNotNull(retrieved);
        assertEquals(TypeVaccinEnum.RSV, retrieved.getTypeVaccin());
    }

    @Test
    void testFindAllVaccin_Success() throws CarnetException {
        List<Vaccin> vaccines = vaccinService.findAllVaccin();
        assertEquals(2, vaccines.size()); // Expecting 2 vaccines from @BeforeEach setup
    }
}
