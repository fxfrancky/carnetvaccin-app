package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.notification.NotificationDTO;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

public class NotificationView extends VerticalLayout {

    private Button backButton;
    private VerticalLayout notificationList;
    private List<NotificationDTO> notifications;
    private HomeView homeView;
    private boolean isVisible = true;

    public NotificationView(HomeView homeView) {
        this.homeView = homeView;

        // Back Button to Return to HomeView
        backButton = new Button("Back", click -> UI.getCurrent().setContent(homeView));
        backButton.setIcon(VaadinIcons.ARROW_LEFT);
        backButton.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        Label titleNotifination = new Label("List of Unread notifications"  + VaadinIcons.ENVELOPE.getHtml());

        titleNotifination.setStyleName(ValoTheme.LABEL_H2);
        titleNotifination.setContentMode(ContentMode.HTML);


        Label markAsReadTitle = new Label("Click on a notification to mark it as Read" );

//        markAsReadTitle.setStyleName(ValoTheme.LABEL_H4);
        markAsReadTitle.addStyleName(ValoTheme.LABEL_SMALL);
        markAsReadTitle.addStyleName("italic-text");
        markAsReadTitle.setContentMode(ContentMode.HTML);

        // Notification List
        notificationList = new VerticalLayout();
        notificationList.setSpacing(true);

        notifications = new ArrayList<>();
        loadNotifications(); // Fetch notifications
        addComponents(titleNotifination, markAsReadTitle, backButton, notificationList);
    }


    private void loadNotifications() {

        notifications = homeView.getNotificationList();

        notificationList.removeAllComponents();
        for (NotificationDTO notification : notifications) {
            Button notificationButton = new Button(notification.getMessage(), click -> markAsRead(notification));
            notificationButton.setStyleName(notification.isRead() ? "read" : "unread");
            notificationList.addComponent(notificationButton);
        }
    }

    private void markAsRead(NotificationDTO notification) {
        notification.setRead(true);
        homeView.markAsRead(notification);
        loadNotifications(); // Refresh UI
    }

}
