package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import com.carnetvaccin.app.backend.vaccin.VaccinService;

import javax.ejb.Stateless;
import javax.inject.Inject;

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

    public VaccinDTO findVaccinByTypeAndDose(String typeVaccin, Integer numDose){
        return mapper.toDto(getService().findVaccinByTypeAndDose(typeVaccin,numDose));
    }
}
