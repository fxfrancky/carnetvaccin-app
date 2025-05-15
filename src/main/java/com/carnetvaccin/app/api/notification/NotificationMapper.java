package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.commons.AbstractMapper;
import com.carnetvaccin.app.api.utilisateur.UtilisateurMapper;
import com.carnetvaccin.app.api.vaccin.VaccinMapper;
import com.carnetvaccin.app.backend.notification.Notification;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class NotificationMapper extends AbstractMapper<Notification, NotificationDTO> {

    @Inject
    private UtilisateurMapper utilisateurMapper;

    @Inject
    private VaccinMapper vaccinMapper;

    public NotificationMapper() {
        super(Notification.class, NotificationDTO.class);
    }

    @Override
    public NotificationDTO toDto(Notification notificationEntity) {
        if (notificationEntity == null){
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationEntity.getNotificationId());
        notificationDTO.setDateNotification(notificationEntity.getDateNotification());
        notificationDTO.setMessage(notificationEntity.getMessage());
        notificationDTO.setRead(notificationEntity.isRead());
        notificationDTO.setUtilisateurDTO(utilisateurMapper.toDto(notificationEntity.getUtilisateur()));
        notificationDTO.setVaccinDTO(vaccinMapper.toDto(notificationEntity.getVaccin()));
        return notificationDTO;
    }

    @Override
    public Notification toEntity(NotificationDTO notificationDTO) {
        
        if(notificationDTO == null){
            return null;
        }
        Notification notificationEntity = new Notification();
        notificationEntity.setNotificationId(notificationDTO.getId());
        notificationEntity.setDateNotification(notificationDTO.getDateNotification());
        notificationEntity.setMessage(notificationDTO.getMessage());
        notificationEntity.setRead(notificationDTO.isRead());
        notificationEntity.setUtilisateur(utilisateurMapper.toEntity(notificationDTO.getUtilisateurDTO()));
        notificationEntity.setVaccin(vaccinMapper.toEntity(notificationDTO.getVaccinDTO()));
        return notificationEntity;
    }

    @Override
    public List<Notification> toEntityList(List<NotificationDTO> notificationDTOS) {
        List<Notification> notificationList;
        if (CollectionUtils.isEmpty(notificationDTOS)){
            notificationList = new ArrayList<>();
        } else {
            notificationList = notificationDTOS.stream()
                    .map(this::toEntity).collect(Collectors.toList());
        }
        return notificationList;
    }

    @Override
    public List<NotificationDTO> toDtoList(List<Notification> notificationsList) {
        List<NotificationDTO> notificationDTOSList = notificationsList.stream()
                .map(this::toDto).collect(Collectors.toList());
        return notificationDTOSList;
    }
}
