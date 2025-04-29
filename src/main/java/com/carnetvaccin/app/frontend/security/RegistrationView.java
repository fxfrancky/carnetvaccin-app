package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;
import java.time.LocalDate;

@CDIView(RegistrationView.REGISTER)
public class RegistrationView extends VerticalLayout implements View {

    public static final String REGISTER = "register";

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    @Inject
    private UtilisateurFacade userService;

    public RegistrationView() {
//        setSizeFull(); // Ensure the view fills the entire screen
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        VerticalLayout registrationForm = new VerticalLayout();
        registrationForm.setWidth("400px"); // Set fixed width for better styling
        registrationForm.setSpacing(true);
        registrationForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
//        registrationForm.setSizeFull();

        // Title
        Label title = new Label("Register a New Account");
        title.setStyleName(ValoTheme.LABEL_H1);

        // First Name
        TextField firstNameField = new TextField("First Name");
        Label firstNameHelp = new Label("First name must be at least 3 characters.");
        firstNameHelp.setStyleName("help-text"); // Optional: Apply CSS styling for help text

        // Last Name
        TextField lastNameField = new TextField("Last Name");
        Label lastNameHelp = new Label("Last name must be at least 3 characters.");

        // Username
        TextField usernameField = new TextField("Username");
        Label usernameHelp = new Label("Username must be at least 6 characters.");

        // Email
        TextField emailField = new TextField("Email");
        Label emailHelp = new Label("Enter a valid email address.");

        // Password
        PasswordField passwordField = new PasswordField("Password");
        Label passwordHelp = new Label("Password must be at least 6 characters and contain one special character (!, @, #, etc.).");

        // Confirm Password
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        // Date of Birth
        DateField dateOfBirthField = new DateField("Date of Birth");
        dateOfBirthField.setRangeEnd(LocalDate.now());

        // Is User an admin
        CheckBox isUserAdmin = new CheckBox("Is User Admin ?");

        // Register Button
        Button registerButton = new Button("Register");
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
                Notification.show("Registration successful! Please login.", Notification.Type.ASSISTIVE_NOTIFICATION);
                navigationEvent.fire(new NavigationEvent(LoginView.LOGIN)); // Navigate to the login page
            } catch (CarnetException e) {
                Notification.show(e.getMessage(), Notification.Type.WARNING_MESSAGE);
            }
        });

        // Add all components to the form layout
        registrationForm.addComponents(
                title,
                firstNameField, firstNameHelp,
                lastNameField, lastNameHelp,
                usernameField, usernameHelp,
                emailField, emailHelp,
                passwordField, passwordHelp,
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
