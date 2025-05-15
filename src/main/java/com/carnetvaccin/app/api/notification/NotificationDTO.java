package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.commons.BaseDto;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class NotificationDTO extends BaseDto implements Serializable {

    private UtilisateurDTO utilisateurDTO;

    private VaccinDTO vaccinDTO;

    private LocalDate dateNotification;

    private String message;

    private boolean isRead;

    public NotificationDTO(UtilisateurDTO utilisateurDTO, VaccinDTO vaccinDTO, LocalDate dateNotification, String message, boolean isRead) {
        this.utilisateurDTO = utilisateurDTO;
        this.vaccinDTO = vaccinDTO;
        this.dateNotification = dateNotification;
        this.message = message;
        this.isRead = isRead;
    }

    public NotificationDTO() {
    }


}
