package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.frontend.Page;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button.ClickEvent;

import javax.inject.Inject;

@CDIView(Page.LOGIN)
//@UIScoped
public class LoginView extends CustomComponent implements View, ClickListener {

    @Inject
    private UserInfo user;

    @Inject
    private UtilisateurFacade userFacade;

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    private TextField userNameField;
    private PasswordField passwordField;
    private Button loginButton;

    @Override
    public void enter(ViewChangeEvent event) {

        userNameField = new TextField("User Name");
        passwordField = new PasswordField("Password");
        loginButton = new Button("Login");
        loginButton.addClickListener(this);
        loginButton.setClickShortcut(KeyCode.ENTER);

        VerticalLayout layout = new VerticalLayout();
        setCompositionRoot(layout);
        layout.setSizeFull();

        layout.addComponent(userNameField);
        layout.addComponent(passwordField);
        layout.addComponent(loginButton);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        String userName = userNameField.getValue();
        String password = passwordField.getValue();

        UtilisateurDTO loginUser = userFacade.getUserByUserNameAndPassword(userName, password);
        if (loginUser == null) {
            new Notification("Wrong credentials", Notification.TYPE_ERROR_MESSAGE)
                    .show(getUI().getPage());
            return;
        }

        user.setUser(loginUser);

        navigationEvent.fire(new NavigationEvent(Page.HOME));
    }
}