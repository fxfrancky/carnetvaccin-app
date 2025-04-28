package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateurService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

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

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(this.getClass().getSimpleName());
    }
    public VaccinUtilisateurDTO findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) throws CarnetException {
        return mapper.toDto(getService().findByUtilisaterIDAndVaccinId(vaccinId,utilisateurId));
    }

    public List<VaccinUtilisateurDTO> findAllVaccinUtilisateur() {
        return mapper.toDtoList(getService().findAll());
    }

    public List<VaccinUtilisateurDTO> findAllVaccinUtilisateurByUserId(Long utilisateurId) throws CarnetException {
        return mapper.toDtoList(getService().findAllVaccinUtilisateurByUserId(utilisateurId));
    }

    public List<VaccinUtilisateurDTO> findrByTerms(String searchTerm, Long utilisateurId) throws CarnetException {
        return mapper.toDtoList(getService().findrByTerms(searchTerm,utilisateurId));
    }

    public void saveOrUpdate(VaccinUtilisateurDTO vaccinUtilisateurDTO) throws CarnetException{
        getService().saveOrUpdate(mapper.toEntity(vaccinUtilisateurDTO));
    }

    public void deleteVaccin(VaccinUtilisateurDTO vaccinUtilisateurDTO) throws CarnetException{
        getService().deleteVaccin(mapper.toEntity(vaccinUtilisateurDTO));
    }
}
