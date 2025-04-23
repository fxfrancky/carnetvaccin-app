package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.api.commons.BaseDto;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class VaccinUtilisateurDTO extends BaseDto implements Serializable {

    private VaccinDTO vaccinDTO;

    private UtilisateurDTO utilisateurDTO;

    private LocalDate dateVaccination;

    private String lieuVacctination;

    private String commentairesVaccin;


    public VaccinUtilisateurDTO(VaccinDTO vaccinDTO, UtilisateurDTO utilisateurDTO, LocalDate dateVaccination, String lieuVacctination, String commentairesVaccin) {
        this.vaccinDTO = vaccinDTO;
        this.utilisateurDTO = utilisateurDTO;
        this.dateVaccination = dateVaccination;
        this.lieuVacctination = lieuVacctination;
        this.commentairesVaccin = commentairesVaccin;
    }

    public VaccinUtilisateurDTO() {
    }
}
