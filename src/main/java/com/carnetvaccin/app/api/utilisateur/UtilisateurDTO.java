package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.BaseDto;
import com.carnetvaccin.app.api.notification.NotificationDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UtilisateurDTO extends BaseDto implements Serializable {


    private String firstName;

    private String lastName;

    private String email;

    private String dateNaissance;

    private String userName;

    private String plainPassword;

    private String token;

    private List<VaccinUtilisateurDTO> vaccinUtilisateurDTOList;

    private List<NotificationDTO> notificationDTOList;

    private boolean isAdmin;

    public UtilisateurDTO(String firstName, String lastName, String email, String dateNaissance, List<VaccinUtilisateurDTO> vaccinUtilisateurDTOList, List<NotificationDTO> notificationDTOList, String userName, String plainPassword, Boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.vaccinUtilisateurDTOList = vaccinUtilisateurDTOList;
        this.notificationDTOList = notificationDTOList;
        this.userName = userName;
        this.plainPassword = plainPassword;
        this.isAdmin = isAdmin;
    }

    public UtilisateurDTO() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


}
