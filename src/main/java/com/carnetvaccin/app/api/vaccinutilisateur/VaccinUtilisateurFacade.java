package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateurService;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class VaccinUtilisateurFacade extends AbstractFacade<VaccinUtilisateur,VaccinUtilisateurDTO, VaccinUtilisateurService,VaccinUtilisateurMapper> {


    @Inject
    private VaccinUtilisateurMapper mapper;

    @Inject
    private VaccinUtilisateurService service;

    public VaccinUtilisateurFacade() {
        super(VaccinUtilisateurDTO.class, VaccinUtilisateur.class);
    }

    @Override
    protected VaccinUtilisateurService getService() {
        return service;
    }

    @Override
    protected VaccinUtilisateurMapper getMapper() {
        return mapper;
    }

    public VaccinUtilisateurDTO findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) {
        return mapper.toDto(getService().findByUtilisaterIDAndVaccinId(vaccinId,utilisateurId));
    }
}
