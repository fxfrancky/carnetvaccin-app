package com.carnetvaccin.app.frontend.utilisateur;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProfileForm extends FormLayout {


    private UtilisateurFacade userFacade;


    private AccessControl accessControl;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private DateField dateNaissance = new DateField("Birth Date");
    private TextField userName = new TextField("Username");
    private TextField password = new TextField("password");
    private CheckBox isAdmin = new CheckBox("Is Admin");
    private Button createButton = new Button("Create User");
    private Button deleteButton = new Button("Delete Account");

    public ProfileForm(UtilisateurFacade userFacade, AccessControl accessControl ) {
        this.userFacade = userFacade;
        this.accessControl = accessControl;
        UtilisateurDTO loggedInUser = new UtilisateurDTO();
        if(((CustomAccessControl)accessControl).isUserSignedIn()){
            loggedInUser =  ((CustomAccessControl)accessControl).getCurrentUser();
        }else {
            Notification.show("An error occur The user is not connected", Notification.Type.ERROR_MESSAGE);
        }
        setSizeUndefined();

        Label title = new Label("Profil Utilisateur");
        title.setStyleName(ValoTheme.LABEL_H3);
        addComponent(title);

        final VerticalLayout layout = new VerticalLayout();


        firstName.setValue(loggedInUser.getFirstName());
        firstName.setEnabled(false);
        layout.addComponent(firstName);

        lastName.setValue(loggedInUser.getLastName());
        lastName.setEnabled(false);
        layout.addComponent(lastName);


        email.setValue(loggedInUser.getEmail());
        email.setEnabled(false);
        layout.addComponent(email);
        LocalDate parsedAtServer = LocalDate.parse(loggedInUser.getDateNaissance(), DateTimeFormatter.ISO_DATE);
        dateNaissance.setValue(parsedAtServer);
        dateNaissance.setEnabled(false);
        layout.addComponent(dateNaissance);

        userName.setValue(loggedInUser.getUserName());
        userName.setEnabled(false);
        layout.addComponent(userName);

//        layout.addComponent(password);
        isAdmin.setValue(loggedInUser.isAdmin());
        isAdmin.setEnabled(false);
        layout.addComponent(isAdmin);

        addComponents(firstName,lastName,email,dateNaissance,userName,isAdmin);

        HorizontalLayout btn = new HorizontalLayout();
        btn.setSizeFull();
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
        deleteButton.addClickListener(e -> delete());
        btn.addComponent(deleteButton);
        addComponents(layout, btn);
    }

    private void delete(){

        showConfirmationDialog("Are you sure you want to delete your account ? You Will be disconnected", () -> {
                    UtilisateurDTO loggedUser = ((CustomAccessControl)accessControl).getCurrentUser();
            try {
                    userFacade.deleteUserAccount(loggedUser);
                     Notification.show("Account  Deleted");
                 } catch (CarnetException e) {
                    Notification.show("An error occurs while deletinG a user", Notification.Type.ERROR_MESSAGE);
              }
         });
    }


    /**
     * Confirmation Dialog
     * @param message
     * @param onConfirm
     */
    private void showConfirmationDialog(String message, Runnable onConfirm) {
        // Create a new Window as the confirmation dialog
        Window confirmationDialog = new Window("Confirmation");
        confirmationDialog.setModal(true);
        confirmationDialog.setResizable(false);
        confirmationDialog.setWidth("600px");

        // Create a vertical layout for the dialog's content
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setSpacing(true);

        // Add the message
        Label confirmationMessage = new Label(message);
        content.addComponent(confirmationMessage);

        // Add buttons
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button confirmButton = new Button("Yes");
        confirmButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        confirmButton.addClickListener(event -> {
            confirmationDialog.close(); // Close dialog
            onConfirm.run(); // Perform the action
        });

        Button cancelButton = new Button("No");
        cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
        cancelButton.addClickListener(event -> {
            confirmationDialog.close(); // Close dialog without action
        });

        buttons.addComponents(confirmButton, cancelButton);

        // Add components to the dialog's layout
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        confirmationDialog.setContent(content);

        // Show the dialog
        UI.getCurrent().addWindow(confirmationDialog);
    }
}
