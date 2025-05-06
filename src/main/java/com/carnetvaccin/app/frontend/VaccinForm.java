package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

@Slf4j
public class VaccinForm extends FormLayout {

    private VaccinFacade vaccinFacade;
    private TextField typeVaccinField;
    private TextField numDoseField;
    private TextField numMonthDoseField ;
    private TextArea descriptionVaccinField;

    private DateField dateVaccinationField;
    private TextField lieuVacctinationField;
    private TextArea commentairesVaccinField;
    // Create Binder
    private Binder<VaccinUtilisateurDTO> binder;

    private HomeView parentView;

    private Button saveButton;
    private Button cancelButton;

    private VaccinUtilisateurDTO currentItem;
    private VaccinDTO currentVaccin;
    private List<VaccinDTO> vaccinDTOList;
    private boolean isNew = false;
    private AccessControl accessControl;
    private UtilisateurDTO loggedInUser;
    private ComboBox<VaccinDTO> comboBox;
    private ListDataProvider<VaccinDTO> vaccinDataProvider;

    public VaccinForm(HomeView parentView, AccessControl accessControl, VaccinFacade vaccinFacade) {
        this.parentView = parentView;
        this.vaccinFacade = vaccinFacade;
        this.accessControl = accessControl;

        initData(); // Initialize the data.
        createComponents();
        configureComponents();
        buildForm();
    }


    @PostConstruct
    public void initData() {
        loggedInUser = ((CustomAccessControl) accessControl).getCurrentUser();
    }

    private void createComponents() {
        dateVaccinationField = new DateField("Date de Vaccination");
        dateVaccinationField.setRangeEnd(LocalDate.now());
        lieuVacctinationField = new TextField("Lieu de Vaccination");
        commentairesVaccinField= new TextArea("Commentaire");

        typeVaccinField = new TextField("Type de vaccin");
        descriptionVaccinField = new TextArea("Description du vaccin");
//        vaccinComboBox = new ComboBox("Vaccin");
        numDoseField = new TextField("Numero Dose");
        numMonthDoseField = new TextField("Nombre de Mois");

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        // Init a binder
        binder = new Binder<>(VaccinUtilisateurDTO.class);
    }


    private void configureComponents() {

        vaccinDTOList = vaccinFacade.findAllVaccin();
        if (CollectionUtils.isEmpty(vaccinDTOList)) {
            vaccinDTOList = new ArrayList<>();
        }
        vaccinDataProvider = new ListDataProvider<>(vaccinDTOList);

        comboBox = new ComboBox<VaccinDTO>();
        comboBox.setCaption("Select a vaccin");
        comboBox.setEmptySelectionAllowed(false);
        comboBox.setDataProvider(vaccinDataProvider);
        if(vaccinDataProvider.getItems().iterator().hasNext()) { //check if the dataProvider has any items
            comboBox.setValue(null);
            currentVaccin = vaccinDTOList.get(0);
            comboBox.setValue(currentVaccin); // Select the first vaccin by default
            updateVaccinFields(currentVaccin);
        }

        comboBox.setItemCaptionGenerator(vaccinDTO ->
                String.format("%s-%s",vaccinDTO.getTypeVaccin().name(), vaccinDTO.getNumDose())
        );
        comboBox.addValueChangeListener(e -> {
            VaccinDTO selectedVaccin = e.getValue();
            if(selectedVaccin!= null) {
                updateVaccinFields(selectedVaccin);
                vaccinDataProvider.refreshAll();
            } else {
                log.info("comboBox No value selected");
            }
        });

        // Make vaccin fields read only
        typeVaccinField.setReadOnly(true);
        descriptionVaccinField.setReadOnly(true);
        numDoseField.setReadOnly(true);
        numMonthDoseField.setReadOnly(true);

        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> {
            setVisible(false);
            parentView.setGridFullWidth();
        });

        // Bind Direct Fields
        binder.forField(dateVaccinationField)
                .bind(VaccinUtilisateurDTO::getDateVaccination, VaccinUtilisateurDTO::setDateVaccination);

        binder.forField(lieuVacctinationField)
                .bind(VaccinUtilisateurDTO::getLieuVacctination, VaccinUtilisateurDTO::setLieuVacctination);

        binder.forField(commentairesVaccinField)
                .bind(VaccinUtilisateurDTO::getCommentairesVaccin, VaccinUtilisateurDTO::setCommentairesVaccin);

        // Bind Nested Fields (Vaccin)
        binder.bind(typeVaccinField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getTypeVaccin().name(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setTypeVaccin(TypeVaccinEnum.valueOf(value)));

        binder.bind(numDoseField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getNumDose().toString(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setNumDose(Integer.valueOf(value)));


        binder.bind(numMonthDoseField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getNbrMonthsDose().toString(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setNbrMonthsDose(Integer.valueOf(value)));

        binder.bind(descriptionVaccinField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getVaccinDescription(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setVaccinDescription(value));

    }

    public void updateVaccinFields(VaccinDTO selectedVaccin) {

        if(currentItem == null){
            currentItem = new VaccinUtilisateurDTO();
            currentItem.setVaccinDTO(selectedVaccin);
            currentItem.setUtilisateurDTO(loggedInUser);
        }
        currentVaccin = selectedVaccin;
        typeVaccinField.setValue(selectedVaccin.getTypeVaccin().name());
        descriptionVaccinField.setValue(selectedVaccin.getVaccinDescription());
        numDoseField.setValue(String.valueOf(selectedVaccin.getNumDose()));
        numMonthDoseField.setValue(String.valueOf(selectedVaccin.getNbrMonthsDose()));
    }

    private void buildForm() {
        addComponents(comboBox ,typeVaccinField, numDoseField, numMonthDoseField, descriptionVaccinField, dateVaccinationField, lieuVacctinationField, commentairesVaccinField);
        HorizontalLayout btns = new HorizontalLayout();
        btns.setSpacing(true);
        btns.setMargin(true);
        btns.setSizeFull();
        saveButton.addStyleNames(ValoTheme.BUTTON_PRIMARY);
        cancelButton.addStyleNames(ValoTheme.BUTTON_DANGER);
        btns.addComponents(saveButton, cancelButton);
        btns.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(btns);
        setSpacing(true);
        setMargin(true);
    }

    public void editVaccinUtilisateur(VaccinUtilisateurDTO vaccinUtilisateur) {
        isNew = false;
        currentItem = vaccinUtilisateur;
        binder.setBean(currentItem);
        setVisible(true);
        parentView.showForm();
    }

    public void addNewVaccinUtilisateur() {
        isNew = true;
        VaccinUtilisateurDTO newVaccinUtilisateur = new VaccinUtilisateurDTO();
        currentItem = newVaccinUtilisateur;
        currentItem.setVaccinDTO(currentVaccin);
        currentItem.setUtilisateurDTO(loggedInUser);
        setVisible(true);
        binder.setBean(currentItem);
        parentView.showForm();
    }

    private void save() {
        try {
            binder.writeBean(currentItem); // Binder
            if (isNew) {
                // For a new VaccinUtilisateur, set the user.
                currentItem.setUtilisateurDTO(loggedInUser); //set the logged in user
                currentItem.setVaccinDTO(currentVaccin);
                parentView.addVaccinUtilisateur(currentItem);
            } else {
                parentView.updateVaccinUtilisateur(currentItem);
            }
            parentView.setGridFullWidth();
            setVisible(false);
            binder.removeBean(); // binder
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage(), ERROR_MESSAGE);
        }
    }



}
