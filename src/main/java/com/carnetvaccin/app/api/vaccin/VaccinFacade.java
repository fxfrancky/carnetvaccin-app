package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import com.carnetvaccin.app.backend.vaccin.VaccinService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
public class VaccinFacade extends AbstractFacade<Vaccin,VaccinDTO, VaccinService,VaccinMapper> {

    @Inject
    private VaccinMapper mapper;

    @Inject
    private VaccinService service;

    public VaccinFacade() {
        super(VaccinDTO.class, Vaccin.class);
    }

    @Override
    protected VaccinService getService() {
        return service;
    }

    @Override
    protected VaccinMapper getMapper() {
        return mapper;
    }

    public VaccinDTO findVaccinByTypeAndDose(String typeVaccin, Integer numDose) throws CarnetException {
        Optional<Vaccin> vaccin = getService().findVaccinByTypeAndDose(typeVaccin,numDose);
        return vaccin.map(vacc -> mapper.toDto(vacc)).orElse(null);
    }

    public void createVaccin(VaccinDTO vaccinDTO) throws CarnetException {
        getService().createVaccin(mapper.toEntity(vaccinDTO));
    }

    public List<VaccinDTO> findAllVaccin() throws CarnetException{
         List<Vaccin> vaccinList = getService().findAllVaccin();
        return mapper.toDtoList(vaccinList);
    }
}
