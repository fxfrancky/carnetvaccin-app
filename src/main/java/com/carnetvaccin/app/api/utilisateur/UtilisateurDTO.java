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

    private List<String> roles;

    private boolean isAdmin;

    private boolean isActive = true;

    public UtilisateurDTO(String firstName, String lastName, String email, String dateNaissance, String userName, String plainPassword, String token, List<VaccinUtilisateurDTO> vaccinUtilisateurDTOList, List<NotificationDTO> notificationDTOList, List<String> roles, boolean isAdmin, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.userName = userName;
        this.plainPassword = plainPassword;
        this.token = token;
        this.vaccinUtilisateurDTOList = vaccinUtilisateurDTOList;
        this.notificationDTOList = notificationDTOList;
        this.roles = roles;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
    }

    public UtilisateurDTO() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public void addRole(String role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }
}