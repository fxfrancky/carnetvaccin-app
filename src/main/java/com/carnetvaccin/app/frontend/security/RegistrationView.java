package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;
import java.time.LocalDate;

@CDIView(RegistrationView.NAME)
public class RegistrationView extends VerticalLayout implements View {

    public static final String NAME = "register";

    @Inject
    private UtilisateurFacade userService;

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private UI ui;

    public RegistrationView() {
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout registrationForm = new VerticalLayout();
        registrationForm.setWidth("700px"); // Set fixed width for better styling
        registrationForm.setSpacing(true);
        registrationForm.setMargin(true);
        registrationForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        // Title
        Label title = new Label("Register a New Account  "+ VaadinIcons.FORM.getHtml());
        title.setContentMode(ContentMode.HTML);
        title.setStyleName(ValoTheme.LABEL_H2);


        // First Name
        TextField firstNameField = new TextField("First Name");
        firstNameField.setIcon(VaadinIcons.USER);
        firstNameField.addStyleName(ValoTheme.TEXTFIELD_SMALL);


        Label firstNameHelp = new Label("First name must be at least 3 characters.");
        firstNameHelp.setStyleName(ValoTheme.LABEL_SMALL);
        firstNameHelp.addStyleName(ValoTheme.LABEL_LIGHT);
        firstNameHelp.setStyleName("color: blue;");

        // Last Name
        TextField lastNameField = new TextField("Last Name");
        lastNameField.setIcon(VaadinIcons.USER_CARD);
        lastNameField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        Label lastNameHelp = new Label("Last name must be at least 3 characters.");
        lastNameHelp.setStyleName(ValoTheme.LABEL_SMALL);
        lastNameHelp.addStyleName(ValoTheme.LABEL_LIGHT);
        lastNameHelp.setStyleName("color: blue;");

        // Username
        TextField usernameField = new TextField("Username");
        usernameField.setIcon(VaadinIcons.USER);
        usernameField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        Label usernameHelp = new Label("Username must be at least 6 characters.");
        usernameHelp.setStyleName(ValoTheme.LABEL_SMALL);
        usernameHelp.addStyleName(ValoTheme.LABEL_LIGHT);
        usernameHelp.setStyleName("color: blue;");

        // Email
        TextField emailField = new TextField("Email");
        emailField.setIcon(VaadinIcons.ENVELOPE);
        emailField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        Label emailHelp = new Label("Enter a valid email address.");
        emailHelp.setStyleName(ValoTheme.LABEL_SMALL);
        emailHelp.addStyleName(ValoTheme.LABEL_LIGHT);
        emailHelp.setStyleName("color: blue;");

        // Password
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setIcon(VaadinIcons.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        Label passwordHelp = new Label("Password must be at least 6 characters and contain one special character (!, @, #, etc.).");
        passwordHelp.setStyleName(ValoTheme.LABEL_SMALL);
        passwordHelp.addStyleName(ValoTheme.LABEL_LIGHT);
        passwordHelp.setStyleName("color: blue;");

        // Confirm Password
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setIcon(VaadinIcons.LOCK);
        confirmPasswordField.addStyleName(ValoTheme.TEXTFIELD_SMALL);


        // Date of Birth
        DateField dateOfBirthField = new DateField("Date of Birth");
        dateOfBirthField.setIcon(VaadinIcons.CALENDAR);
        dateOfBirthField.setRangeEnd(LocalDate.now());

        // Is User an admin
        CheckBox isUserAdmin = new CheckBox("Is User Admin ?");

        // Register Button
        Button registerButton = new Button("Register ", VaadinIcons.FORM);
        registerButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        // Register Button Logic
        registerButton.addClickListener(event -> {
            try {
                validateRegistrationForm(firstNameField, lastNameField, usernameField, emailField, passwordField, confirmPasswordField, dateOfBirthField);
                UtilisateurDTO utilisateurDTO= new UtilisateurDTO();
                utilisateurDTO.setFirstName(firstNameField.getValue());
                utilisateurDTO.setLastName(lastNameField.getValue());
                utilisateurDTO.setEmail(emailField.getValue());
                utilisateurDTO.setDateNaissance(dateOfBirthField.getValue().toString());
                utilisateurDTO.setUserName(usernameField.getValue());
                utilisateurDTO.setPlainPassword(passwordField.getValue());
                utilisateurDTO.setAdmin(isUserAdmin.getValue());

                userService.registerUtilisateur(utilisateurDTO);
                Notification.show("Registration successful! Please login.", Notification.Type.HUMANIZED_MESSAGE);
                ui.getNavigator().navigateTo(LoginView.NAME);
            } catch (CarnetException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        // Add all components to the form layout
        registrationForm.addComponents(
                title,
                firstNameField,
                lastNameField,
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                dateOfBirthField,
                isUserAdmin,
                registerButton
        );

        // Center the form layout on the screen
        VerticalLayout centeredLayout = new VerticalLayout(registrationForm);
        centeredLayout.setSizeFull();
        centeredLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        addComponent(centeredLayout);
    }

    private void validateRegistrationForm(
            TextField firstNameField,
            TextField lastNameField,
            TextField usernameField,
            TextField emailField,
            PasswordField passwordField,
            PasswordField confirmPasswordField,
            DateField dateOfBirthField
    ) {
        if (firstNameField.getValue().isEmpty() || firstNameField.getValue().length() < 3) {
            throw new CarnetException("Invalid first name. It must be at least 3 characters.");
        }

        if (lastNameField.getValue().isEmpty() || lastNameField.getValue().length() < 3) {
            throw new CarnetException("Invalid last name. It must be at least 3 characters.");
        }

        if (usernameField.getValue().isEmpty() || usernameField.getValue().length() < 6) {
            throw new CarnetException("Invalid username. It must be at least 6 characters.");
        }

        if (emailField.getValue().isEmpty() || !emailField.getValue().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new CarnetException("Invalid email address.");
        }

        if (passwordField.getValue().isEmpty() || passwordField.getValue().length() < 6 || !passwordField.getValue().matches(".*[!@#$%^&*].*")) {
            throw new CarnetException("Invalid password. It must be at least 6 characters long and contain one special character.");
        }

        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            throw new CarnetException("Passwords do not match.");
        }

        if (dateOfBirthField.getValue() == null) {
            throw new CarnetException("Date of birth cannot be empty.");
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // Logic executed when the view is entered
    }
}
