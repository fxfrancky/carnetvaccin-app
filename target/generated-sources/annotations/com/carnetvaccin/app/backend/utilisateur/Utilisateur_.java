package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-04-21T18:38:54")
@StaticMetamodel(Utilisateur.class)
public class Utilisateur_ { 

    public static volatile SingularAttribute<Utilisateur, String> firstName;
    public static volatile SingularAttribute<Utilisateur, String> lastName;
    public static volatile SingularAttribute<Utilisateur, String> password;
    public static volatile ListAttribute<Utilisateur, Notification> notificationList;
    public static volatile SingularAttribute<Utilisateur, LocalDate> dateNaissance;
    public static volatile SingularAttribute<Utilisateur, Long> utilisateurId;
    public static volatile SingularAttribute<Utilisateur, String> userName;
    public static volatile ListAttribute<Utilisateur, VaccinUtilisateur> vaccinUtilisateurList;
    public static volatile SingularAttribute<Utilisateur, String> email;

}