package com.carnetvaccin.app.frontend.utilisateur;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

@CDIView(CreateUserView.USER)
@RolesAllowed(Role.ADMIN)
public class CreateUserView extends CustomComponent implements View {

    public static final String USER = "user";
    @Inject
    private UtilisateurFacade userFacade;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private DateField dateNaissance = new DateField("Birth Date");
    private TextField userName = new TextField("Username");
    private TextField password = new TextField("password");
    private CheckBox  isAdmin = new CheckBox("Is Admin");
    private Button createButton = new Button("Create User");
    private Button deleteButton = new Button("Delete User");
//    private ConfirmDialog dialog = new ConfirmDialog();
    private Binder<UtilisateurDTO> binder = new Binder<>(UtilisateurDTO.class);



    @Override
    public void enter(ViewChangeEvent event) {
        final VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("Register new User"));

        layout.addComponent(firstName);
        layout.addComponent(lastName);
        layout.addComponent(email);
        layout.addComponent(dateNaissance);
        layout.addComponent(userName);
        layout.addComponent(password);
        layout.addComponent(isAdmin);


//        Binder for First Name
        binder.forField(firstName).withValidator((value, context) -> {
            if (value.isEmpty()) {
                return ValidationResult.error("First Name cannot be empty");
            }else if (value.length() < 3) {
                return ValidationResult.error("First Name must be at least 3 characters long");
            } else if (!value.matches("^[a-zA-ZÀ-ÿ'-]+$")) {
                return ValidationResult.error("First name can only contain alphanumeric characters and some special characters");
            }
            return ValidationResult.ok();
        }).bind("firstName");


//        Binder for Last Name
        binder.forField(lastName).withValidator((value, context) -> {
            if (value.isEmpty()) {
                return ValidationResult.error("Last Name cannot be empty");
            }else if (value.length() < 3) {
                return ValidationResult.error("Last must be at least 3 characters long");
            } else if (!value.matches("^[a-zA-ZÀ-ÿ'-]+$")) {
                return ValidationResult.error("Last name can only contain alphanumeric characters and some special characters");
            }
            return ValidationResult.ok();
        }).bind("lastName");


        //        Bind for Email
        binder.forField(email).withValidator((value, context) -> {
            if (value.isEmpty()) {
                return ValidationResult.error("Email cannot be empty");
            }

            if (userFacade.getUserByEmail(value) != null) {
                return ValidationResult.error("Email is taken");
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

            if (value.matches(emailRegex)){
                return ValidationResult.ok();
            }else {
                return ValidationResult.error("Invalid email address");
            }
        }).bind("email");


//        Bind for User Name
        binder.forField(userName).withValidator((value, context) -> {
            if (value.isEmpty()) {
                return ValidationResult.error("Username cannot be empty");
            }else if (value.length() < 5) {
                return ValidationResult.error("Username must be at least 5 characters long");
            } else if (!value.matches("^[a-zA-Z0-9_]+$")) {
                return ValidationResult.error("Username can only contain alphanumeric characters and underscores");
            }
            if (userFacade.getUserByUserName(value) != null) {
                return ValidationResult.error("Username is taken");
            }
            return ValidationResult.ok();
        }).bind("userName");

        //  Bind for Password
        binder.forField(password).withValidator((value, context) -> {
            if (value.isEmpty()) {
                return ValidationResult.error("Password cannot be empty");
            }else if (value.length() < 6) {
                return ValidationResult.error("Password must be at least 6 characters long");
            } else if (!value.matches("^[a-zA-ZÀ-ÿ'-]+$")) {
                return ValidationResult.error("Password can only contain alphanumeric characters and underscores");
            }
            return ValidationResult.ok();
        }).bind("password");

        // Add Listener to User Creation
        createButton.addClickListener(e -> createUser());
        createButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        createButton.setClickShortcut(KeyCode.ENTER);

        // Add a Listener to User Deletion
//        deleteButton.addClickListener(e -> dialog.open());

        //   COnfirmation dialog
//        dialog.setHeader("Delete a User");
//        dialog.setText("Do You Really wanna delete the user ?");
//
//        dialog.setCancelable(true);
//        dialog.setConfirmText("Delete User");
////        dialog.setConfirmButtonTheme(ValoTheme.BUTTON_DANGER);
//        dialog.addConfirmListener(e -> deleteUser());

    }

    /**
     * Create a User
     */
    private void createUser(){
        UtilisateurDTO user = binder.getBean();
        if(user != null){
            try {
                binder.writeBean(user);
                userFacade.create(user);
                binder.setBean(new UtilisateurDTO("","", "", "",null, null,"","",false));
            }catch (ValidationException e){
                Notification.show("An error occurs" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Delete User
     */
    private void deleteUser(){
        UtilisateurDTO user = binder.getBean();
        if(user != null){
            try {
                binder.writeBean(user);
                userFacade.remove(user);
                binder.setBean(new UtilisateurDTO("","", "", "",null, null,"","",false));
            }catch (ValidationException e){
                Notification.show("An error occurs" + e.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }
    }
}
