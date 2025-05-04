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

import javax.annotation.PostConstruct;
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

//    private  HorizontalLayout btns;
    private VaccinUtilisateurDTO currentItem;
    private VaccinDTO currentVaccin;
    private ComboBox<VaccinDTO> vaccinComboBox;
    private List<VaccinDTO> vaccinDTOList;
    private ListDataProvider<VaccinDTO> vaccinDataProvider;
    private boolean isNew = false;
    private AccessControl accessControl;
    private UtilisateurDTO loggedInUser;

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

        // Init a binder
        binder = new Binder<>(VaccinUtilisateurDTO.class);

        loggedInUser = ((CustomAccessControl) accessControl).getCurrentUser();

        vaccinDTOList = vaccinFacade.findAllVaccin();
        if (vaccinDTOList == null) {
            vaccinDTOList = new ArrayList<>();
        }
        vaccinDataProvider = new ListDataProvider<>(vaccinDTOList);

    }

    private void createComponents() {
        dateVaccinationField = new DateField("Date de Vaccination");
        lieuVacctinationField = new TextField("Lieu de Vaccination");
        commentairesVaccinField= new TextArea("Commentaire");

        typeVaccinField = new TextField("Type de vaccin");
        descriptionVaccinField = new TextArea("Description du vaccin");
        vaccinComboBox = new ComboBox("Vaccin");
        numDoseField = new TextField("Numero Dose");
        numMonthDoseField = new TextField("Nombre de Mois");

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
    }


    private void configureComponents() {

        // Configure the ComboBox
        vaccinComboBox.setDataProvider(vaccinDataProvider);
        vaccinComboBox.setEmptySelectionAllowed(false);
        vaccinComboBox.setItemCaptionGenerator(vaccinDTO ->
                String.format(" Type: %s - Dose: (%s)",vaccinDTO.getTypeVaccin().name(), vaccinDTO.getNumDose())
        );
        vaccinComboBox.setValue(vaccinDataProvider.getItems().iterator().next()); // Select the first vaccin by default

        currentVaccin = vaccinDataProvider.getItems().iterator().next();
        System.out.println("Default current vaccin");
        typeVaccinField.setValue(currentVaccin.getTypeVaccin().name());
        descriptionVaccinField.setValue(currentVaccin.getVaccinDescription());
        numDoseField.setValue(String.valueOf(currentVaccin.getNumDose()));
        numMonthDoseField.setValue(String.valueOf(currentVaccin.getNumDose()));

        // Make vaccin fields read only
        typeVaccinField.setReadOnly(true);
        descriptionVaccinField.setReadOnly(true);
        numDoseField.setReadOnly(true);
        numMonthDoseField.setReadOnly(true);

        //add value change listener to the vaccinComboBox
        vaccinComboBox.addValueChangeListener(event -> {
            VaccinDTO selectedVaccin = (VaccinDTO) event.getValue();
            if (selectedVaccin != null) {
                currentVaccin = selectedVaccin;
                typeVaccinField.setValue(selectedVaccin.getTypeVaccin().name());
                descriptionVaccinField.setValue(selectedVaccin.getVaccinDescription());
                numDoseField.setValue(String.valueOf(selectedVaccin.getNumDose()));
                numMonthDoseField.setValue(String.valueOf(selectedVaccin.getNumDose()));
            } else {
                typeVaccinField.clear();
                descriptionVaccinField.clear();
                numDoseField.clear();
                numMonthDoseField.clear();
            }
        });

        saveButton.addClickListener(e -> save());
        cancelButton.addClickListener(e -> setVisible(false));


        // Bind Direct Fields
//        binder.bind(dateVaccinationField, VaccinUtilisateurDTO::getDateVaccination, VaccinUtilisateurDTO::setDateVaccination);
        binder.forField(dateVaccinationField)
                .asRequired("Date Vaccination is required")
                .bind(VaccinUtilisateurDTO::getDateVaccination, VaccinUtilisateurDTO::setDateVaccination);

//        binder.bind(lieuVacctinationField, VaccinUtilisateurDTO::getLieuVacctination, VaccinUtilisateurDTO::setLieuVacctination);
        binder.forField(lieuVacctinationField)
                .asRequired("Lieu Vaccination is required")
                .bind(VaccinUtilisateurDTO::getLieuVacctination, VaccinUtilisateurDTO::setLieuVacctination);

//        binder.bind(commentairesVaccinField, VaccinUtilisateurDTO::getCommentairesVaccin, VaccinUtilisateurDTO::setCommentairesVaccin);
        binder.forField(commentairesVaccinField)
                .asRequired("Commentaires Vaccination is required")
                .bind(VaccinUtilisateurDTO::getCommentairesVaccin, VaccinUtilisateurDTO::setCommentairesVaccin);


        if (vaccinDTOList!= null || !vaccinDTOList.isEmpty()){
            initData();
            System.out.println("************Issues with init ");
            for (VaccinDTO vaccinDTO: vaccinDTOList){
                System.out.println("********************* Vaccin Type : " + vaccinDTO.getTypeVaccin());
                System.out.println("********************* Vaccin Description : " + vaccinDTO.getVaccinDescription());
                System.out.println("********************* Vaccin NbrMonthDose : " + vaccinDTO.getNbrMonthsDose());
                System.out.println("********************* Vaccin NumDose : " + vaccinDTO.getNumDose());
            }
        }
        // Bind Nested Fields (Vaccin)
//        binder.bind(typeVaccinField,
//                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getTypeVaccin().name(),
//                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setTypeVaccin(TypeVaccinEnum.valueOf(value)));
//        binder.forField(typeVaccinField)
//                .bind("vaccinDTO.typeVaccin");

        // Bind Enum in Nested Class (Vaccin)
        binder.forField(typeVaccinField)
                .bind(
                        vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getTypeVaccin().name(),
                        (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setTypeVaccin(TypeVaccinEnum.valueOf(value))
                );

        binder.bind(numDoseField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getNumDose().toString(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setNumDose(Integer.valueOf(value)));
//        binder.forField(numDoseField)
//                .withConverter(new StringToIntegerConverter("Must enter a number"))
//                .bind("vaccinDTO.numDose");

        binder.bind(numMonthDoseField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getNbrMonthsDose().toString(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setNbrMonthsDose(Integer.valueOf(value)));

//        binder.forField(numMonthDoseField)
//                .withConverter(new StringToIntegerConverter("Must enter a number"))
//                .bind("vaccinDTO.nbrMonthsDose");

        binder.bind(descriptionVaccinField,
                vaccinUtilisateur -> vaccinUtilisateur.getVaccinDTO().getVaccinDescription(),
                (vaccinUtilisateur, value) -> vaccinUtilisateur.getVaccinDTO().setVaccinDescription(value));

//        binder.forField(numMonthDoseField)
//                .bind("vaccinDTO.vaccinDescription");

    }


    private void buildForm() {
        addComponent(vaccinComboBox);
        addComponent(typeVaccinField);
        addComponent(numDoseField);
        addComponent(numMonthDoseField);
        addComponent(descriptionVaccinField);
        addComponent(dateVaccinationField);
        addComponent(lieuVacctinationField);
        addComponent(commentairesVaccinField);
        HorizontalLayout btns = new HorizontalLayout();
        btns.setSpacing(true);
        btns.setMargin(true);
        btns.setSizeFull();
        saveButton.addStyleNames(ValoTheme.BUTTON_PRIMARY);
        cancelButton.addStyleNames(ValoTheme.BUTTON_DANGER);
        btns.addComponents(saveButton, cancelButton);
        addComponent(btns);
        setSpacing(true);
        setMargin(true);
    }

    public void editVaccinUtilisateur(VaccinUtilisateurDTO vaccinUtilisateur) {
        isNew = false;
        currentItem = vaccinUtilisateur;
//        currentItem.setVaccinDTO(currentVaccin);
//        currentItem.setUtilisateurDTO(loggedInUser);
//        System.out.println("*********Current Vaacin edit ********* " + currentVaccin.getTypeVaccin());
//        System.out.println("*********Current Vaacin edit Dose********* " + currentVaccin.getNumDose());
//
        if(binder.writeBeanIfValid(currentItem)){
            System.out.println("------------------ Data correct during edit -----------------");
            System.out.println("------------------ TypeVaccin : " + currentItem.getVaccinDTO().getTypeVaccin());
            System.out.println("------------------ Dose : " + currentItem.getVaccinDTO().getNumDose());
        }
//
        currentVaccin = currentItem.getVaccinDTO();
        vaccinComboBox.setValue(currentItem.getVaccinDTO());
       typeVaccinField.setValue(currentItem.getVaccinDTO().getTypeVaccin().name());
       descriptionVaccinField.setValue(currentItem.getVaccinDTO().getVaccinDescription());
       numDoseField.setValue(String.valueOf(currentItem.getVaccinDTO().getNumDose()));
       numMonthDoseField.setValue(String.valueOf(currentItem.getVaccinDTO().getNbrMonthsDose()));
       lieuVacctinationField.setValue(currentItem.getLieuVacctination());
       descriptionVaccinField.setValue(currentItem.getCommentairesVaccin());
       dateVaccinationField.setValue(currentItem.getDateVaccination());
        binder.setBean(currentItem);
        setVisible(true);
        vaccinComboBox.focus();
        System.out.println("************************* Data initialised for edit");
    }

    public void addNewVaccinUtilisateur() {
        isNew = true;
        VaccinUtilisateurDTO newVaccinUtilisateur = new VaccinUtilisateurDTO();
        //
        if(binder.writeBeanIfValid(currentItem)){
            System.out.println("------------------ Data correct during add -----------------");
            System.out.println("------------------ TypeVaccin : " + currentItem.getVaccinDTO().getTypeVaccin());
            System.out.println("------------------ Dose : " + currentItem.getVaccinDTO().getNumDose());
        }
        //newVaccinUtilisateur.setUser(userService.getLoggedInUser()); //set logged in user.
        currentItem = newVaccinUtilisateur;
        System.out.println("*********Current Vaacin add ********* " + currentVaccin.getTypeVaccin());
        System.out.println("*********Current Vaacin add Dose********* " + currentVaccin.getNumDose());


        typeVaccinField.setValue(currentVaccin.getTypeVaccin().name());
        descriptionVaccinField.setValue(currentVaccin.getVaccinDescription());
        numDoseField.setValue(String.valueOf(currentVaccin.getNumDose()));
        numMonthDoseField.setValue(String.valueOf(currentVaccin.getNbrMonthsDose()));
        currentItem.setVaccinDTO(currentVaccin);
        currentItem.setUtilisateurDTO(loggedInUser);
//        binder.setBean(currentItem);
        binder.setBean(currentItem);
        setVisible(true);
        vaccinComboBox.focus();
    }

    private void save() {
        try {
            binder.writeBean(currentItem); // Binder
            VaccinUtilisateurDTO savedVaccinUtilisateur = currentItem;
            if (isNew) {
                // For a new VaccinUtilisateur, set the user.
                savedVaccinUtilisateur.setUtilisateurDTO(loggedInUser); //set the logged in user
                savedVaccinUtilisateur.setVaccinDTO(currentVaccin);
                addNewVaccinUtilisateur();
                parentView.addVaccinUtilisateur(savedVaccinUtilisateur);
            } else {
                parentView.updateVaccinUtilisateur(savedVaccinUtilisateur);
            }
            setVisible(false);
            binder.removeBean(); // binder
        } catch (Exception e) {
            Notification.show("Error saving: " + e.getMessage(), ERROR_MESSAGE);
        }
    }



}
