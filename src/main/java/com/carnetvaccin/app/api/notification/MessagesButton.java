package com.carnetvaccin.app.api.notification;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class MessagesButton extends Button {

    private int unreadCount = 0;
    private VerticalLayout messagesLayout;
    private Window messagesWindow;
    private Label countLabel;

    public MessagesButton() {
        setStyleName(ValoTheme.BUTTON_BORDERLESS);
        setIcon(VaadinIcons.BELL_O); // Use VaadinIcons.BELL_O for messagesButton
        //Removed the old icon
        countLabel = new Label(" (0)");
        countLabel.setStyleName(ValoTheme.LABEL_SMALL);

        VerticalLayout iconAndCount = new VerticalLayout();
        iconAndCount.addComponents(countLabel); // Only add the label
        iconAndCount.setSpacing(false);
        iconAndCount.setMargin(false);
        iconAndCount.setHeight("100%");
        iconAndCount.setWidth("100%");
        iconAndCount.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setCaption("Messages");

        messagesLayout = new VerticalLayout();
        messagesLayout.setMargin(true);
        messagesLayout.setSpacing(true);

        messagesWindow = new Window("Messages");
        messagesWindow.setClosable(true);
        messagesWindow.setModal(false);
        messagesWindow.setWidth("300px");
        messagesWindow.setHeightUndefined();
        messagesWindow.setContent(messagesLayout);

        addClickListener(e -> {
            showMessages();
            if (messagesWindow != null && messagesWindow.getParent() == null) {
                UI.getCurrent().addWindow(messagesWindow);
            } else if (messagesWindow != null) {
                messagesWindow.close();
            }
        });
    }

    public void setUnreadCount(int count) {
        this.unreadCount = count;
        countLabel.setValue(" (" + unreadCount + ")"); // Update the label's value

        if (unreadCount > 0) {
            addStyleName("unread"); // Apply the "unread" style
        } else {
            removeStyleName("unread");
        }
    }

    private void showMessages() {
        messagesLayout.removeAllComponents();
        if (unreadCount == 0) {
            messagesLayout.addComponent(new Label("No new messages"));
        } else {
            for (int i = 0; i < unreadCount; i++) {
                messagesLayout.addComponent(new Label("Message " + (i + 1) + " content..."));
            }
        }
    }
}