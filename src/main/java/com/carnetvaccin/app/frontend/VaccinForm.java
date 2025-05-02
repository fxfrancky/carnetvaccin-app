package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;
import java.util.List;

public class VaccinForm extends FormLayout {


//    @Named("customAccessControl")
    private AccessControl accessControl;

    private VaccinFacade vaccinFacade;
    private VaccinUtilisateurFacade vaccinUtilisateurFacade;

    private VaccinUtilisateurDTO vaccinUtilisateurDTO;

    private TextField typeVaccinField = new TextField("Type de Vaccin");
    private TextField numDoseField = new TextField("Numero de Dose");
    private TextField numMonthDoseField = new TextField("Nombre de mois");
    private TextArea descriptionVaccinField = new TextArea("Description du Vaccin");

    private DateField dateVaccination = new DateField("Date de Vaccination");
    private TextField lieuVacctination = new TextField("Lieu de Vaccination");
    private TextArea commentairesVaccin = new TextArea("Commentaires");

    private HomeView homeView;

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private  HorizontalLayout btns;
    private UtilisateurDTO selectedUtilisateurDTO;
    private VaccinDTO selectedVaccinDTO;
    private List<VaccinDTO> vaccinDTOList;

    // Bind the form to the Vaccin
    private Binder<VaccinUtilisateurDTO> binder = new Binder<>(VaccinUtilisateurDTO.class);

    public VaccinForm(HomeView homeView, VaccinFacade vaccinFacade, VaccinUtilisateurFacade vaccinUtilisateurFacade, AccessControl accessControl) {
        this.homeView = homeView;
        this.vaccinFacade = vaccinFacade;
        this.vaccinUtilisateurFacade = vaccinUtilisateurFacade;
        this.accessControl = accessControl;
        setSizeUndefined();

        //  Add save and delete buttons
        btns = new HorizontalLayout(save, delete);

        binder.forField(typeVaccinField)
                .bind(vaccinUtilisateurDTO1 -> vaccinUtilisateurDTO.getVaccinDTO().getTypeVaccin().name(),
                        null);

        binder.forField(numDoseField)
                .bind(vaccinUtilisateurDTO1 -> vaccinUtilisateurDTO.getVaccinDTO().getNumDose().toString(),
                        null);

        binder.forField(numMonthDoseField)
                .bind(vaccinUtilisateurDTO1 -> vaccinUtilisateurDTO.getVaccinDTO().getNbrMonthsDose().toString(),
                        null);

        binder.forField(descriptionVaccinField)
                .bind(vaccinUtilisateurDTO1 -> vaccinUtilisateurDTO.getVaccinDTO().getVaccinDescription(),
                        null);

        binder.forField(dateVaccination)
                .bind(
                        VaccinUtilisateurDTO::getDateVaccination,
                        VaccinUtilisateurDTO::setDateVaccination);

        binder.forField(lieuVacctination)
                .bind(
                        VaccinUtilisateurDTO::getLieuVacctination,
                        VaccinUtilisateurDTO::setLieuVacctination);

        binder.forField(commentairesVaccin)
                .bind(
                        VaccinUtilisateurDTO::getCommentairesVaccin,
                        VaccinUtilisateurDTO::setCommentairesVaccin);

        save.setStyleName((ValoTheme.BUTTON_PRIMARY));
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());

        try {
            vaccinDTOList = vaccinFacade.findAllVaccin();
        } catch (CarnetException e) {
            Notification.show("Cannot retrieve the list of vaccins", Notification.Type.ERROR_MESSAGE);
        }
        // Create a list of items for selection
        VerticalLayout popupContent = new VerticalLayout();
        for (VaccinDTO vaccinDTO : vaccinDTOList) {
            Button vaccinButton = new Button(vaccinDTO.getTypeVaccin().name() + vaccinDTO.getNumDose(), event -> {
                typeVaccinField.setValue(vaccinDTO.getTypeVaccin().name());
                numDoseField.setValue(String.valueOf(vaccinDTO.getNumDose()));
                numMonthDoseField.setValue(String.valueOf(vaccinDTO.getNbrMonthsDose()));
                descriptionVaccinField.setValue(vaccinDTO.getVaccinDescription());
            });
            vaccinButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
            popupContent.addComponent(vaccinButton);
        }

        // Create PopupView
        PopupView popupView = new PopupView("Select Vaccin", popupContent);

        addComponents(popupView, typeVaccinField, numDoseField, numMonthDoseField, descriptionVaccinField, dateVaccination, lieuVacctination, commentairesVaccin, btns );
    }


    @PostConstruct
        public void init() {

        typeVaccinField.setEnabled(false);
        descriptionVaccinField.setEnabled(false);
        numDoseField.setEnabled(false);
        numMonthDoseField.setEnabled(false);

    }

public void setVaccinUtilisateurDTO(VaccinUtilisateurDTO vaccinUtilisateurDTO) {
    this.vaccinUtilisateurDTO = vaccinUtilisateurDTO;
    binder.setBean(vaccinUtilisateurDTO);

    delete.setVisible(vaccinUtilisateurDTO.isPersisted());
    setVisible(true);
}


    private void delete(){
            try {
                vaccinUtilisateurFacade.deleteVaccin(vaccinUtilisateurDTO);
            } catch (CarnetException e) {
                Notification.show("An error occurs while deletin vaccin", Notification.Type.ERROR_MESSAGE);
            }
            homeView.updateVaccinUtilisateurList();
            setVisible(false);
    }

        private void save(){
            try {
                if(((CustomAccessControl)accessControl).isUserSignedIn()){
                    UtilisateurDTO utilisateurDTO = ((CustomAccessControl)accessControl).getCurrentUser();
                    vaccinUtilisateurFacade.saveOrUpdate(vaccinUtilisateurDTO);
                }else {
                    System.out.println("********** An error occurs while saving vaccin . The user is not connected");
                    Notification.show("An error occurs while saving vaccin. The user is not connected", Notification.Type.ERROR_MESSAGE);
                }

            } catch (CarnetException e) {
                System.out.println("********** An error occurs while saving vaccin "+e.getMessage());
                Notification.show("An error occurs while saving vaccin", Notification.Type.ERROR_MESSAGE);
            }
            homeView.updateVaccinUtilisateurList();
            setVisible(false);
        }


}
