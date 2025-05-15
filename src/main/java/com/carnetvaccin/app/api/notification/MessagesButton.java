package com.carnetvaccin.app.api.notification;


import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;

import java.util.List;

public class MessagesButton extends CssLayout {

    private final Button bellButton;
    private final Label notificationBadge;
    private int unreadMessages = 0;
    private final MenuBar notificationMenu;

    public MessagesButton() {
        bellButton = new Button(FontAwesome.BELL);
        notificationBadge = new Label("0");
        notificationBadge.setStyleName("badge");
        notificationMenu = new MenuBar();
        notificationMenu.setVisible(false);

        addStyleName("messages-button");
        addComponent(bellButton);
        addComponent(notificationBadge);
        addComponent(notificationMenu);
        updateNotificationCount();

        bellButton.addClickListener(event -> toggleMenu());
    }

    public void setUnreadMessages(int count) {
        this.unreadMessages = count;
        updateNotificationCount();
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        notificationMenu.removeItems();
        for (NotificationDTO notificationDTO : notifications) {
            notificationMenu.addItem(notificationDTO.getVaccinDTO().getTypeVaccin().name() + "-" + notificationDTO.getVaccinDTO().getNumDose(), selectedItem -> notificationDTO.setRead(true));
        }
        notificationBadge.setValue(String.valueOf(notifications.size()));
    }

    public void toggleMenu() {
        notificationMenu.setVisible(!notificationMenu.isVisible());
    }

    private void updateNotificationCount() {
        notificationBadge.setValue(String.valueOf(unreadMessages));
        notificationBadge.setVisible(unreadMessages > 0);
    }

    private void markAsRead() {
        setUnreadMessages(0);
        notificationBadge.setVisible(false);
    }

    public Button getNotificationBell() {
        return bellButton;
    }
}
