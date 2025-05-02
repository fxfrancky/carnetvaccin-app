package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.HomeView;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;

@CDIView(LoginView.NAME)
public class LoginView extends VerticalLayout implements View {

    public static final String NAME = "login";

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private UI ui;

    @Inject
    private UtilisateurFacade userFacade;

    @Inject
    private AccessControl accessControl;

    // Fields
    private TextField usernameField;
    private PasswordField passwordField;


    @Override
    public void enter(ViewChangeEvent event) {
        buildView();
        setSizeFull();
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
    }

    private void buildView() {
        removeAllComponents();
        VerticalLayout mainLayout = createMainLayout();
        Panel loginPanel = createCenterLoginPanel();
        VerticalLayout loginForm = createInnerLoginLayout();

        // Title
        Label title = new Label("Login");
        title.setStyleName(ValoTheme.LABEL_H1);

        // Username field
        usernameField = new TextField("Username");
        usernameField.setPlaceholder("Enter your username");
        usernameField.setWidth("100%");
        usernameField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//        usernameField.addStyleName(ValoTheme.TEXTFIELD_LARGE);

        // Password field
        passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Enter your password");
        passwordField.setWidth("100%");
//        passwordField.addStyleName(ValoTheme.TEXTFIELD_LARGE);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//
//        usernameField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//        passwordField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        // Login button
        Button loginButton = createLoginButton();
        loginButton.addClickListener(this::login);

        // Register link
        HorizontalLayout registerLayout = new HorizontalLayout();
        registerLayout.setSpacing(true);
        Label registerPrompt = new Label("Don't have an account yet ?");
        Button registerButton = new Button("Click Here to Register", event -> {
            ui.getNavigator().navigateTo(RegistrationView.NAME);

        });
        registerButton.setStyleName(ValoTheme.LINK_SMALL);//link
//        registerButton.setStyleName(ValoTheme.LINK_SMALL);//link
        registerLayout.addComponents(registerPrompt, registerButton);

        loginForm.addComponents(title, usernameField, passwordField, loginButton, registerLayout);
        loginForm.addStyleName("login-form-container");
        loginPanel.setContent(loginForm);

        mainLayout.addComponent(loginPanel);
        mainLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
        addComponent(mainLayout);

        // Check for token in URL
        String token = Page.getCurrent().getUriFragment();
        if (token != null && token.startsWith("token=")) {
            String actualToken = token.substring(6); // Remove "token="
            if (((CustomAccessControl) accessControl).signInWithToken(actualToken)) {
                ui.getNavigator().navigateTo(HomeView.NAME);
            } else {
                Notification.show("Invalid token. Please log in.", Notification.Type.HUMANIZED_MESSAGE);
            }
        }
    }

    /**
     * Create main layout
     *
     * @return
     */
    public VerticalLayout createMainLayout() {
        // Main layout that fills the browser window.
//        VerticalLayout mainLayout = new VerticalLayout();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
//        mainLayout.setStyleName("login-view");
//        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);
        // Center the whole layout.
//        mainLayout.addComponent(new Label("<div style='width:100%; height: 100%; display:flex; justify-content:center; align-items:center;'></div>", ContentMode.HTML));


//        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        // You can define a background style in your SCSS/CSS.
//        mainLayout.addStyleName("login-background");
        return mainLayout;
    }

    /**
     * Create a centered login panel
     *
     * @return
     */
    public Panel createCenterLoginPanel() {
        Panel loginPanel = new Panel();
        loginPanel.setWidth("500px");
        loginPanel.setHeight("500px");
        loginPanel.addStyleName(ValoTheme.PANEL_WELL);
        return loginPanel;
    }

    public VerticalLayout createInnerLoginLayout() {
        VerticalLayout panelLayout = new VerticalLayout();
        panelLayout.setMargin(true);
        panelLayout.setSpacing(true);
        panelLayout.setWidth("100%");
        return panelLayout;
    }

    public Button createLoginButton() {
        // Login button with primary style.
        Button loginButton = new Button("Login");
        loginButton.setWidth("100%");
        loginButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        // Set Enter key as a shortcut for login.
        loginButton.setClickShortcut(KeyCode.ENTER);
        // Add a click listener for the login button.
        loginButton.addClickListener(this::login);
        return loginButton;
    }


    private void login(Button.ClickEvent event) {
        {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            try {
                if (((CustomAccessControl) accessControl).signIn(username, password)) {
                    UI currentUI = UI.getCurrent();
                    if (currentUI != null) {
                        ui.getNavigator().navigateTo(HomeView.NAME);
                    }
                } else {
                    Notification.show("Invalid username or password", Notification.Type.ERROR_MESSAGE);
                    passwordField.clear();
                }
            } catch (CarnetException e) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                passwordField.clear();
            }

        }
    }
}