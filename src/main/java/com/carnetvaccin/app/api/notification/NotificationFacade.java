package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.notification.NotificationService;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class NotificationFacade extends AbstractFacade<Notification, NotificationDTO, NotificationService, NotificationMapper> {

    @Inject
    private NotificationMapper mapper;

    @Inject
    private NotificationService service;

    public NotificationFacade() {
        super(NotificationDTO.class, Notification.class);
    }

    @Override
    protected NotificationService getService() {
        return service;
    }

    @Override
    protected NotificationMapper getMapper() {
        return mapper;
    }
}
