package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateurService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public VaccinUtilisateurDTO findByUtilisaterIDAndVaccinId(Long vaccinId, Long utilisateurId) throws CarnetException {

        Optional<VaccinUtilisateur> vaccinUtilisateur = getService().findByUtilisaterIDAndVaccinId(vaccinId, utilisateurId);
        return vaccinUtilisateur.map(vUtilisateur -> mapper.toDto(vUtilisateur)).orElse(null);
    }

    public List<VaccinUtilisateurDTO> findAllVaccinUtilisateur() {
        return mapper.toDtoList(getService().findAll());
    }

    public List<VaccinUtilisateurDTO> findAllVaccinUtilisateurByUserId(Long utilisateurId) throws CarnetException {
        return mapper.toDtoList(getService().findAllVaccinUtilisateurByUserId(utilisateurId));
    }

    public List<VaccinUtilisateurDTO> findrByTerms(String searchTerm, Long utilisateurId) throws CarnetException {
        return mapper.toDtoList(getService().findByTerms(searchTerm, utilisateurId));
    }

    public void saveVaccinUtilisateur(VaccinUtilisateurDTO vaccinUtilisateurDTO) throws CarnetException {
        getService().saveVaccinUtilisateur(mapper.toEntity(vaccinUtilisateurDTO));
    }

    public VaccinUtilisateurDTO updateVaccinUtilisateur(VaccinUtilisateurDTO vaccinUtilisateurDTO) throws CarnetException {
        VaccinUtilisateur vaccinUtilisateur = getService().updateVaccinUtilisateur(mapper.toEntity(vaccinUtilisateurDTO));
        return mapper.toDto(vaccinUtilisateur);
    }

    public void deleteVaccin(VaccinUtilisateurDTO vaccinUtilisateurDTO) throws CarnetException {
        getService().deleteVaccinUtilisateur(mapper.toEntity(vaccinUtilisateurDTO));
    }

    public void markAsSent(Long vaccinUtilisateurId) throws CarnetException {
        getService().markAsSent(vaccinUtilisateurId);
    }

    public List<VaccinUtilisateurDTO> findDueVaccinations(LocalDate dateVaccination) throws CarnetException {
        return mapper.toDtoList(getService().findDueVaccinations(dateVaccination));
    }

}