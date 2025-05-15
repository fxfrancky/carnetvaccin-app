package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
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


    public void initialize(UtilisateurDTO loggedInUser) {
        messagesButton = new MessagesButton();

        List<NotificationDTO> notifications = mapper.toDtoList(notificationService.findUnreadNotificationsByUserId(loggedInUser.getId()));
        messagesButton.setNotifications(notifications);
        messagesButton.setUnreadMessages(notifications.size());

        messagesButton.getNotificationBell().addClickListener(event -> messagesButton.toggleMenu());
    }

    public MessagesButton getMessagesButton() {
        return messagesButton;
    }
}
