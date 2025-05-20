package com.carnetvaccin.app.api.notification;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class MessagesButton extends Button {

    private int unreadCount = 0;
    private Label countLabel;
    private CssLayout iconAndCount; // Use CssLayout
    private boolean messagesVisible = false;


    public MessagesButton() {
        setStyleName(ValoTheme.BUTTON_BORDERLESS);
        setIcon(VaadinIcons.BELL_O);
        countLabel = new Label(" (0)");
        countLabel.setStyleName(ValoTheme.LABEL_SMALL);

        // Use a CssLayout
        iconAndCount = new CssLayout();
        iconAndCount.addComponent(countLabel);
        iconAndCount.setStyleName("icon-and-count"); // Apply a style name for CSS
        iconAndCount.setHeight("100%");
        iconAndCount.setWidth("100%");

        // Set the button's caption to an empty string.
        setCaption("");

        //  Add a style name to the button to allow for custom styling of the icon and count.
        addStyleName("messages-button");

        addClickListener(e -> {
            messagesVisible = !messagesVisible; // Toggle visibility
            System.out.println("Notification container clicked. notificationsVisible: " + messagesVisible); //added

        });
    }

    public void setUnreadCount(int count) {
        this.unreadCount = count;
        countLabel.setValue(" (" + unreadCount + ") Unread Messages"); // Update the label's value

        if (unreadCount > 0) {
            iconAndCount.addStyleName("unread"); // Show the count
        } else {
            iconAndCount.removeStyleName("unread"); // Hide the count
        }
    }

    public CssLayout getIconAndCountLayout() {
        return iconAndCount;
    }
}