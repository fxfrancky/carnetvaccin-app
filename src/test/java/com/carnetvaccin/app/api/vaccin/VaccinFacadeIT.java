package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import com.carnetvaccin.app.backend.vaccin.VaccinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class VaccinFacadeIT {
    @InjectMocks
    private VaccinFacade facade;

    @Mock
    private VaccinService service;

    @Mock
    private VaccinMapper mapper;

    private Vaccin vaccin;
    private VaccinDTO vaccinDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock test data
        vaccin = new Vaccin();
        vaccin.setVaccinId(1L);
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);

        vaccinDTO = new VaccinDTO();
        vaccinDTO.setId(1L);
        vaccinDTO.setVaccinDescription("COVID-19 Vaccine");
        vaccinDTO.setNumDose(2);

        when(mapper.toDto(vaccin)).thenReturn(vaccinDTO);
        when(mapper.toEntity(vaccinDTO)).thenReturn(vaccin);

        when(mapper.toDtoList(Arrays.asList(vaccin))).thenReturn(Arrays.asList(vaccinDTO));
        when(mapper.toEntityList(Arrays.asList(vaccinDTO))).thenReturn(Arrays.asList(vaccin));
    }

    @Test
    void testFindVaccinByTypeAndDose_Success() throws CarnetException {
        when(service.findVaccinByTypeAndDose("COVID-19", 2)).thenReturn(Optional.of(vaccin));

        VaccinDTO result = facade.findVaccinByTypeAndDose("COVID-19", 2);
        assertNotNull(result);
        assertEquals("COVID-19 Vaccine", result.getVaccinDescription());

        verify(service, times(1)).findVaccinByTypeAndDose("COVID-19", 2);
    }

    @Test
    void testCreateVaccin_Success() throws CarnetException {
        doNothing().when(service).createVaccin(any(Vaccin.class));

        facade.createVaccin(vaccinDTO);

        verify(service, times(1)).createVaccin(any(Vaccin.class));
    }

    @Test
    void testFindAllVaccin_Success() throws CarnetException {

        when(service.findAllVaccin()).thenReturn(Arrays.asList(vaccin));

        List<VaccinDTO> result = facade.findAllVaccin();
        assertEquals(1, result.size());

        verify(service, times(1)).findAllVaccin();
    }
}
