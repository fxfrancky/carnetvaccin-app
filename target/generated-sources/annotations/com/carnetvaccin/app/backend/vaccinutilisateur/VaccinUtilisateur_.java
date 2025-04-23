package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-04-21T18:38:54")
@StaticMetamodel(VaccinUtilisateur.class)
public class VaccinUtilisateur_ { 

    public static volatile SingularAttribute<VaccinUtilisateur, LocalDate> dateVaccination;
    public static volatile SingularAttribute<VaccinUtilisateur, Vaccin> vaccin;
    public static volatile SingularAttribute<VaccinUtilisateur, String> lieuVacctination;
    public static volatile SingularAttribute<VaccinUtilisateur, Long> vaccinUtilisateurId;
    public static volatile SingularAttribute<VaccinUtilisateur, Utilisateur> utilisateur;
    public static volatile SingularAttribute<VaccinUtilisateur, String> commentairesVaccin;

}