package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.notification.NotificationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class NotificationFacade extends AbstractFacade<Notification, NotificationDTO, NotificationService, NotificationMapper> {

    private MessagesButton bellBtn;

    @Inject
    private NotificationMapper mapper;

    @Inject
    private NotificationService notificationService;

    public NotificationFacade() {
        super(NotificationDTO.class, Notification.class);
    }

    @Override
    protected NotificationService getService() {
        return notificationService;
    }

    @Override
    protected NotificationMapper getMapper() {
        return mapper;
    }

    private MessagesButton messagesButton;

    public void addNotification(NotificationDTO notification) throws CarnetException{
        getService().addNotification(mapper.toEntity(notification));
    }

    public void markAsRead(Long notificationId) throws CarnetException {
        getService().markAsRead(notificationId);
    }

    public List<NotificationDTO> getUnreadNotifications()  throws CarnetException {
        return mapper.toDtoList(getService().getUnreadNotifications());

    }

    public List<NotificationDTO> findUnreadNotificationsByUserId(Long utilisateurId) throws CarnetException {
        return mapper.toDtoList(getService().findUnreadNotificationsByUserId(utilisateurId));
    }


}
