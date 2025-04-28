package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.HomeView;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.CDIView;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;

@CDIView(LoginView.LOGIN)
public class LoginView extends CustomComponent implements View, Button.ClickListener {

    public static final String LOGIN = "login";

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
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        VerticalLayout mainLayout = createMainLayout();
        Panel loginPanel = createCenterLoginPanel();
        VerticalLayout panelLayout = createInnerLoginLayout();

        // Header caption for the login form.
        Label caption = new Label("Please Login");
//        caption.addStyleName("login-caption");
        caption.addStyleName("h3");
        caption.setWidth("100%");

        // Username field.
        userNameField = new TextField("Username");
        userNameField.setWidth("100%");

        // Password field.
        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");

        loginButton = createLoginButton();

        // Assemble the form components.
        panelLayout.addComponents(caption, userNameField, passwordField, loginButton);
        loginPanel.setContent(panelLayout);

        // Center the login panel in the main layout.
        mainLayout.addComponent(loginPanel);
        mainLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);

        // Set the main layout as the content of the UI.
        setCompositionRoot(mainLayout);

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        String username = userNameField.getValue();
        String password = passwordField.getValue();
        UtilisateurDTO loginUser = null;
        try {
            loginUser = userFacade.getUserByUserNameAndPassword(username, password);
        } catch (CarnetException e) {
            Notification.show("Login failed. Please check your credentials.", Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (loginUser != null) {
            Notification.show("Login successful", Notification.Type.TRAY_NOTIFICATION);
        }

        user.setUser(loginUser);

        navigationEvent.fire(new NavigationEvent(HomeView.HOME));
    }


    /**
     * Create main layout
     *
     * @return
     */
    public VerticalLayout createMainLayout() {
        // Main layout that fills the browser window.
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
        // You can define a background style in your SCSS/CSS.
        mainLayout.addStyleName("login-background");
        return mainLayout;
    }

    /**
     * Create a centered login panel
     *
     * @return
     */
    public Panel createCenterLoginPanel() {
        Panel loginPanel = new Panel();
        loginPanel.setWidth("350px");
        loginPanel.setHeight("300px");
        loginPanel.addStyleName("login-panel");
        return loginPanel;
    }

    /**
     * Create Inner Layout
     * @return
     */
    public VerticalLayout createInnerLoginLayout() {
        VerticalLayout panelLayout = new VerticalLayout();
        panelLayout.setMargin(true);
        panelLayout.setSpacing(true);
        panelLayout.setWidth("100%");
        return panelLayout;
    }


    /**
     * Create Login Button
     * @return
     */
    public Button createLoginButton() {
        // Login button with primary style.
        Button loginButton = new Button("Login");
        loginButton.setWidth("100%");
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        // Set Enter key as a shortcut for login.
        loginButton.setClickShortcut(KeyCode.ENTER);

        // Add a click listener for the login button.
        loginButton.addClickListener(this);
        return loginButton;
    }

}