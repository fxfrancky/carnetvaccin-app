package com.carnetvaccin.app.backend.notification;

import javax.inject.Inject;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class NotificationListener {

    @Inject
    private NotificationService notificationService;

    @PostPersist
    @PostUpdate
    public void onNotificationChange(Notification notification) {
        notificationService.notifyUI(notification);
    }
}
