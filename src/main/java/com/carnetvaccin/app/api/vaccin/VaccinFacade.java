package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import com.carnetvaccin.app.backend.vaccin.VaccinService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class VaccinFacade extends AbstractFacade<Vaccin,VaccinDTO, VaccinService,VaccinMapper> {

    @Inject
    private VaccinMapper mapper;

    @Inject
    private VaccinService service;

    @Inject
    private VaccinUtilisateurFacade vaccinUtilisateurFacade;

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


    public List<VaccinDTO> findDueVaccinationsForUser(LocalDate today, UtilisateurDTO utilisateur) {
        //get the user's age.
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long ageInMonths = birthDate.until(today).toTotalMonths();


        //get all the user's vaccinations.
        List<VaccinUtilisateurDTO> userVaccinations = vaccinUtilisateurFacade.findAllVaccinUtilisateurByUserId(utilisateur.getId()) ;

        //get all available vaccines.
        List<VaccinDTO> allVaccins = mapper.toDtoList(service.findAllVaccin());

        List<VaccinDTO> dueVaccinations = new ArrayList<>();


        for (VaccinDTO vaccin : allVaccins) {
            // Check if the user's age is greater or equal to the vaccine's required age
            if (ageInMonths >= vaccin.getNbrMonthsDose()) {
                boolean hasVaccination = false;
                for (VaccinUtilisateurDTO userVaccination : userVaccinations) {
                    // Check if the user has already received this vaccine and notification is sent
                    if (userVaccination.getVaccinDTO().getTypeVaccin().name().equals(vaccin.getTypeVaccin().name()) && userVaccination.isNotificationSent()) {
                        hasVaccination = true;
                        break;
                    }
                }
                if (!hasVaccination) {
                    dueVaccinations.add(vaccin);
                }
            }
        }
        return dueVaccinations;
    }
}
