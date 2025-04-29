package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.carnetvaccin.app.frontend.security.LoginView;
import com.carnetvaccin.app.frontend.utilisateur.ProfileForm;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.CDIView;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CDIView(HomeView.HOME)
@RolesAllowed(Role.User)
public class HomeView extends VerticalLayout implements View {

    public static final String HOME = "home";

    @Inject
    private UserInfo loggedInUser;

    @Inject
    private CustomAccessControl customAccessControl;

//    @Inject
    private final VaccinUtilisateurFacade vUtilisateurFacade;

    @Inject
    private VaccinFacade vFacade;

    @Inject
    private UtilisateurFacade utilisateurFacade;

    private Grid<VaccinUtilisateurDTO> vUtilisateurGrid = new Grid<>(VaccinUtilisateurDTO.class);

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    TextField searchField = new TextField();

    // Add an extra header row to hold the filtering components.
    HeaderRow filterRow = vUtilisateurGrid.appendHeaderRow();

    private TabSheet tabSheet; // First TabSheet


    List<VaccinDTO> vaccinDTOList;

//    @Inject
    private VaccinForm vaccinForm;
//
    private ProfileForm profileForm;

    public HomeView(VaccinUtilisateurFacade vUtilisateurFacade){
        this.vUtilisateurFacade = vUtilisateurFacade;
        setMargin(true);
        setSpacing(true);
        setSizeFull(); // Occupy full screen space
        System.out.println("Current Logged In User :" +loggedInUser);
        System.out.println("Current Logged In User Name :" +loggedInUser.getName());
        System.out.println("Current Logged In User Token:" +loggedInUser.getCurrentToken());
        System.out.println("Current Logged In User Roles :" +loggedInUser.getRoles());
        System.out.println("Current Logged In User Id :" +loggedInUser.getUser().getId());
        System.out.println("Current Logged In User Email :" +loggedInUser.getUser().getEmail());
        System.out.println("Current Logged In User UserName :" +loggedInUser.getUser().getUserName());
        System.out.println("Current Logged In User :" +loggedInUser);
        System.out.println("Current Logged In User :" +loggedInUser);
        System.out.println("Current Logged In User :" +loggedInUser);
    }


    @PostConstruct
    private void init(){

        updateVaccinUtilisateurList();
        // Add header
        addComponent(createHeader());
        // Add Title
        Label title = new Label("Carnet de Vaccination");
        title.setStyleName(ValoTheme.LABEL_H2);
        addComponent(title);

        // Initialize TabSheet 1
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();


        vaccinForm = new VaccinForm(this,vFacade,vUtilisateurFacade, loggedInUser);

        profileForm = new ProfileForm(utilisateurFacade,loggedInUser);

        tabSheet.addTab(carnetDataGridTab(), "Carnet de vaccination");
        tabSheet.addTab(profileForm, "Mon Profil");
//        tabSheet.addTab(new VerticalLayout(new Label("Content for the second tab")), "Mon Profil");


        // Add TabSheets to the layout
        addComponents(tabSheet);
        setExpandRatio(tabSheet, 1); // Let the first TabSheet expand





//        Initialize the calendar vaccin



        /**
         //     * Init Calendrier Nationale de Vaccination
         //     */
////    @PostConstruct
//    public void initCalendrierVaccination(){
//        List<VaccinDTO> vaccinDTOList = new ArrayList<>();
//
////    RSV 1 (0 mois Naissance)
//      VaccinDTO rsv1 = new VaccinDTO();
//      rsv1.setVaccinDescription(" RSV (virus respiratoire syncytial) : administration d’un traitementpréventif (produit d’immunisation passive) qui protège contre labronchiolite, de préférence avant la sortie de la maternité, en pé-riode de haute circulation du virus, de septembre à février. ");
//      rsv1.setTypeVaccin(TypeVaccinEnum.RSV);
//      rsv1.setNumDose(1);
//      rsv1.setNbrMonthsDose(0);
//
//      vaccinDTOList.add(rsv1);
//
////        RSV 2 (6 mois) Rappel si on ne l a pas pris a la naissance
//        VaccinDTO rsv2 = new VaccinDTO();
//        rsv2.setVaccinDescription(" RSV : pour les nourrissons de moins de 6 mois ne l’ayant pasreçu avant la sortie de la maternité, administration du traitementpréventif (produit d’immunisation passive) qui protège contre labronchiolite, en période de haute circulation du virus, de sep-tembre à février. ");
//        rsv2.setTypeVaccin(TypeVaccinEnum.RSV);
//        rsv2.setNumDose(2);
//        rsv2.setNbrMonthsDose(6);
//
//        vaccinDTOList.add(rsv2);
//
////        COMBINE 1 (2 mois)
//        VaccinDTO combine1 = new VaccinDTO();
//        combine1.setVaccinDescription(" 1ère dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus Inﬂuenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ");
//        combine1.setTypeVaccin(TypeVaccinEnum.COMBINE);
//        combine1.setNumDose(1);
//        combine1.setNbrMonthsDose(2);
//
//        vaccinDTOList.add(combine1);
//
////        COMBINE 2 (4 mois)
//        VaccinDTO combine2 = new VaccinDTO();
//        combine2.setVaccinDescription(" 2ème dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus Inﬂuenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ");
//        combine2.setTypeVaccin(TypeVaccinEnum.COMBINE);
//        combine2.setNumDose(2);
//        combine2.setNbrMonthsDose(4);
//
//        vaccinDTOList.add(combine2);
//
////        COMBINE 3 (11 mois)
//        VaccinDTO combine3 = new VaccinDTO();
//        combine3.setVaccinDescription(" 3ème dose du vaccin combiné (D, T, aP, Hib, IPV, Hep B) quiprotège contre :- la diphtérie,- le tétanos,- la coqueluche,- les infections invasives à Haemophilus Inﬂuenzae de type b(méningite, épiglottite et arthrite),- la poliomyélite,- l’hépatite B. ");
//        combine3.setTypeVaccin(TypeVaccinEnum.COMBINE);
//        combine3.setNumDose(3);
//        combine3.setNbrMonthsDose(11);
//
//        vaccinDTOList.add(combine3);
//
////        COMBINE_ROV 1 (12 mois)
//        VaccinDTO combinerov1 = new VaccinDTO();
//        combinerov1.setVaccinDescription(" 1ère dose du vaccin combiné (RORV) qui protège contre :- la rougeole,- les oreillons,- la rubéole,- la varicelle. ");
//        combinerov1.setTypeVaccin(TypeVaccinEnum.COMBINE_RORV);
//        combinerov1.setNumDose(1);
//        combinerov1.setNbrMonthsDose(12);
//
//        vaccinDTOList.add(combinerov1);
//
////        ROTAROVIRUS 1 (2 mois)
//        VaccinDTO rotarovirus1 = new VaccinDTO();
//        rotarovirus1.setVaccinDescription(" Rotavirus (1ère dose) : Vaccination contre la gastro-entérite à ro-tavirus. ");
//        rotarovirus1.setTypeVaccin(TypeVaccinEnum.ROTAROVIRUS);
//        rotarovirus1.setNumDose(1);
//        rotarovirus1.setNbrMonthsDose(2);
//
//        vaccinDTOList.add(rotarovirus1);
//
////        ROTAROVIRUS 2 (3 mois)
//
//        VaccinDTO rotarovirus2 = new VaccinDTO();
//        rotarovirus2.setVaccinDescription(" Rotavirus (2 ième dose) : Vaccination contre la gastro-entérite à ro-tavirus. ");
//        rotarovirus2.setTypeVaccin(TypeVaccinEnum.ROTAROVIRUS);
//        rotarovirus2.setNumDose(2);
//        rotarovirus2.setNbrMonthsDose(3);
//
//        vaccinDTOList.add(rotarovirus2);
//
////        ROTAROVIRUS 3 (4 mois)
//
//        VaccinDTO rotarovirus3 = new VaccinDTO();
//        rotarovirus3.setVaccinDescription(" Rotavirus (3 ième dose) : Vaccination contre la gastro-entérite à ro-tavirus. ");
//        rotarovirus3.setTypeVaccin(TypeVaccinEnum.ROTAROVIRUS);
//        rotarovirus3.setNumDose(3);
//        rotarovirus3.setNbrMonthsDose(4);
//
//        vaccinDTOList.add(rotarovirus3);
//
////        PNEUMONOCOQUES 1 (2 mois)
//        VaccinDTO pneumonocoque1 = new VaccinDTO();
//        pneumonocoque1.setVaccinDescription(" Pneumocoques (1ère dose) : vaccination contre les infections in-vasives à pneumocoques. ");
//        pneumonocoque1.setTypeVaccin(TypeVaccinEnum.PNEUMONOCOQUES);
//        pneumonocoque1.setNumDose(1);
//        pneumonocoque1.setNbrMonthsDose(2);
//
//        vaccinDTOList.add(pneumonocoque1);
//
////        PNEUMONOCOQUES 2 (4 mois)
//        VaccinDTO pneumonocoque2 = new VaccinDTO();
//        pneumonocoque2.setVaccinDescription(" Pneumocoques (2ième dose) : vaccination contre les infections in-vasives à pneumocoques. ");
//        pneumonocoque2.setTypeVaccin(TypeVaccinEnum.PNEUMONOCOQUES);
//        pneumonocoque2.setNumDose(2);
//        pneumonocoque2.setNbrMonthsDose(4);
//
//        vaccinDTOList.add(pneumonocoque2);
//
////        PNEUMONOCOQUES 3 (11 mois)
//        VaccinDTO pneumonocoque3 = new VaccinDTO();
//        pneumonocoque3.setVaccinDescription(" Pneumocoques (3ième dose) : vaccination contre les infections in-vasives à pneumocoques. ");
//        pneumonocoque3.setTypeVaccin(TypeVaccinEnum.PNEUMONOCOQUES);
//        pneumonocoque3.setNumDose(3);
//        pneumonocoque3.setNbrMonthsDose(11);
//
//        vaccinDTOList.add(pneumonocoque3);
//
//
////        MENINGOCOQUE_B 1 (3 mois)
//        VaccinDTO meningocoqueb1 = new VaccinDTO();
//        meningocoqueb1.setVaccinDescription(" Méningocoque B (1 ère dose) : vaccination contre les infections invasives à méningocoque B. ");
//        meningocoqueb1.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_B);
//        meningocoqueb1.setNumDose(1);
//        meningocoqueb1.setNbrMonthsDose(3);
//
//        vaccinDTOList.add(meningocoqueb1);
//
////        MENINGOCOQUE_B 2 (5 mois)
//        VaccinDTO meningocoqueb2 = new VaccinDTO();
//        meningocoqueb2.setVaccinDescription(" Méningocoque B (2 ième dose) : vaccination contre les infections invasives à méningocoque B. ");
//        meningocoqueb2.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_B);
//        meningocoqueb2.setNumDose(2);
//        meningocoqueb2.setNbrMonthsDose(5);
//
//        vaccinDTOList.add(meningocoqueb2);
//
////        MENINGOCOQUE_B 3 (12 mois)
//        VaccinDTO meningocoqueb3 = new VaccinDTO();
//        meningocoqueb3.setVaccinDescription(" Méningocoque B (3 ième dose) : vaccination contre les infections invasives à méningocoque B. ");
//        meningocoqueb3.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_B);
//        meningocoqueb3.setNumDose(3);
//        meningocoqueb3.setNbrMonthsDose(12);
//
//        vaccinDTOList.add(meningocoqueb3);
//
////        MENINGOCOQUE_ACWY 1 (13 mois)
//        VaccinDTO meningocoque_acwy1 = new VaccinDTO();
//        meningocoque_acwy1.setVaccinDescription(" Méningocoques ACWY (1ère dose) : vaccination contre les infections invasives à méningocoques A, C, W et Y. ");
//        meningocoque_acwy1.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);
//        meningocoque_acwy1.setNumDose(1);
//        meningocoque_acwy1.setNbrMonthsDose(13);
//
//        vaccinDTOList.add(meningocoque_acwy1);
//
//        try {
//            for (VaccinDTO vaccinDTO: vaccinDTOList){
////                if (vFacade.findVaccinByTypeAndDose(vaccinDTO.getTypeVaccin().name(),vaccinDTO.getNumDose())==null){
//                    vFacade.createVaccin(vaccinDTO);
////                }
//            }
//        } catch (CarnetException e) {
//            Notification.show("An error occurs loading vaccin", Notification.Type.ERROR_MESSAGE);
//        }
//        for (VaccinDTO vaccinDTO: vaccinDTOList){
//           if (vFacade.findVaccinByTypeAndDose(vaccinDTO.getTypeVaccin().name(),vaccinDTO.getNumDose())!=null){
//                vFacade.create(vaccinDTO);
//           }
//        }

//        //    RSV,
        ////    COMBINE,
        ////    ROTAROVIRUS,
        ////    PNEUMONOCOQUES,
        ////    MENINGOCOQUE_B,
        ////    COMBINE_RORV,
        ////    MENINGOCOQUE_ACWY
        ////}
//    }


    }

    /**
     * Create DataGrid
     * @return
     */
    private VerticalLayout carnetDataGridTab() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        // Create a search field that will be used to filter multiple columns

        searchField.setPlaceholder("Search (Description vaccin, commentaire, Lieu,  type vaccin)...");
        searchField.setWidth("100%");
        searchField.addValueChangeListener(e -> updateVaccinUtilisateurList());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

//      // Button to clear the filter
        Button clearFilterTextBtn = new Button(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> {
            searchField.clear();
        });

        CssLayout filtering = new CssLayout();
        filtering.addComponents(searchField, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);


        // Add value change listener to filter the grid based on the search field's text.
        searchField.addValueChangeListener(evnt -> {
            String searchTerm = evnt.getValue();
            ListDataProvider<VaccinUtilisateurDTO> dataProvider = (ListDataProvider<VaccinUtilisateurDTO>) vUtilisateurGrid.getDataProvider();
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                // Clear filter if search term is empty
                dataProvider.clearFilters();
            } else {
                final String lowerCaseSearchTerm = searchTerm.toLowerCase();
                dataProvider.setFilter(vaccinUtilisateurDTO ->
                        vaccinUtilisateurDTO.getCommentairesVaccin().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getVaccinDTO().getVaccinDescription().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getVaccinDTO().getTypeVaccin().name().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getLieuVacctination().toLowerCase().contains(lowerCaseSearchTerm)
                );
            }
        });

//      Add a new Button to save vaccin
        Button addVaccinUtilisateurBtn = new Button("Add new Vaccin");
        addVaccinUtilisateurBtn.addClickListener(e -> {
            vUtilisateurGrid.asSingleSelect().clear();
            VaccinDTO vaccinDTO = new VaccinDTO();
            vaccinDTO = vaccinDTOList.get(0);
            VaccinUtilisateurDTO vaccinUtilisateurDTO = new VaccinUtilisateurDTO();
            vaccinUtilisateurDTO.setVaccinDTO(vaccinDTO);
            vaccinUtilisateurDTO.setUtilisateurDTO(new UtilisateurDTO());
            vaccinForm.setVaccinUtilisateurDTO(vaccinUtilisateurDTO);

        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addVaccinUtilisateurBtn);
        toolbar.setSizeFull();
        layout.addComponent(toolbar);
//
        HorizontalLayout main = new HorizontalLayout(vUtilisateurGrid, vaccinForm);
        main.setSizeFull();
        vUtilisateurGrid.setSizeFull();
        main.setExpandRatio(vUtilisateurGrid, 1);
        layout.addComponents(main);
//        layout.addComponent(vUtilisateurGrid);

        vaccinForm.setVisible(false);
        vUtilisateurGrid.asSingleSelect().addValueChangeListener(e ->{
            if(e.getValue() == null){
                vaccinForm.setVisible(false);
            }else {
                vaccinForm.setVaccinUtilisateurDTO(e.getValue());
            }
        });

        return layout;
    }

    /**
     * Update the List of VaccinUtilisateur for the Grid
     */
    public void updateVaccinUtilisateurList(){

        List<VaccinUtilisateurDTO> vUtilisateursDTOList = getVaccinUtilisateurDTOList();
        vUtilisateurGrid.setItems(vUtilisateursDTOList);
        vUtilisateurGrid.removeAllColumns();
//        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getId).setCaption("Identifiant").setId("id");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin()).setCaption("Type Vaccin").setId("typeVaccin");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getVaccinDescription()).setCaption("Description du vaccin").setId("vaccinDescription").setWidth(1500);
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getNumDose()).setCaption("Dose").setId("numDose");

        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getDateVaccination).setCaption("Date Vaccination").setId("dateVaccination").setSortable(true);
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getCommentairesVaccin).setCaption("Commentaire vaccination").setId("commentairesVaccin");
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getLieuVacctination).setCaption("Lieu Vaccination").setId("lieuVacctination");
        vUtilisateurGrid.setWidthFull();

        filterRow.getCell("typeVaccin").setComponent(getvaccinTypesFilters());

    }

    //    Get the List of VaccinUtilisateur
    public List<VaccinUtilisateurDTO> getVaccinUtilisateurDTOList(){
        List<VaccinUtilisateurDTO> vUtilisateursDTOList = new ArrayList<>();
        try {
            if (customAccessControl.isUserSignedIn() && loggedInUser.getUser().getId() != null){
                vUtilisateursDTOList = vUtilisateurFacade.findrByTerms(searchField.getValue(), loggedInUser.getUser().getId());
            }
        } catch (CarnetException e) {
            Notification.show("An error occurs while retrieving List Vaccin", Notification.Type.ERROR_MESSAGE);
        }
        return vUtilisateursDTOList;
    }

    /**
     * Add Type Vaccin Filter to the Grid
     * @param grid
     * @param vaccinUtilisateurDTOList
     */
    public void addTypeVaccinFilter(Grid<VaccinUtilisateurDTO> grid, List<VaccinUtilisateurDTO> vaccinUtilisateurDTOList){

        // Add an extra header row to hold the filtering components.
        HeaderRow filterRow = grid.appendHeaderRow();

        // Create a ComboBox to filter the "Last Name" column.
        ComboBox<String> vaccinTypesFilter = new ComboBox<>();
        vaccinTypesFilter.setPlaceholder("Select Type Vaccin");
        vaccinTypesFilter.setWidth("100%");

        // Populate the ComboBox with distinct last names from the data.
        List<String> distinctVaccinTypes = vaccinUtilisateurDTOList.stream()
                .map(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin().name())
                .distinct()
                .collect(Collectors.toList());
        vaccinTypesFilter.setItems(distinctVaccinTypes);

        // Add a listener to filter the grid upon selecting a value.
        vaccinTypesFilter.addValueChangeListener(event -> {
            ListDataProvider<VaccinUtilisateurDTO> dataProvider =
                    (ListDataProvider<VaccinUtilisateurDTO>) grid.getDataProvider();
            String selectedValue = event.getValue();
            if (selectedValue == null || selectedValue.isEmpty()) {
                // No selection: clear the filter.
                dataProvider.clearFilters();
            } else {
                // Set the filter to display only rows with a matching last name.
                dataProvider.setFilter(VaccinUtilisateurDTO::getVaccinDTO,
                        vaccinDTO -> vaccinDTO.getTypeVaccin().name().equals(selectedValue));
            }
        });

        // Place the ComboBox into the header cell for the "Last Name" column.
        filterRow.getCell("typeVaccin").setComponent(vaccinTypesFilter);

    }

    /**
     * Get Vaccin Types Filter
     * @return
     */
    public ComboBox<String> getvaccinTypesFilters(){
        // Create a ComboBox to filter the "Last Name" column.
        ComboBox<String> vaccinTypesFilter = new ComboBox<>();
        vaccinTypesFilter.setPlaceholder("Select Type Vaccin");
        vaccinTypesFilter.setWidth("100%");

        // Populate the ComboBox with distinct last names from the data.
        List<String> distinctVaccinTypes = getVaccinUtilisateurDTOList().stream()
                .map(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin().name())
                .distinct()
                .collect(Collectors.toList());
        vaccinTypesFilter.setItems(distinctVaccinTypes);

        // Add a listener to filter the grid upon selecting a value.
        vaccinTypesFilter.addValueChangeListener(event -> {
            ListDataProvider<VaccinUtilisateurDTO> dataProvider =
                    (ListDataProvider<VaccinUtilisateurDTO>) vUtilisateurGrid.getDataProvider();
            String selectedValue = event.getValue();
            if (selectedValue == null || selectedValue.isEmpty()) {
                // No selection: clear the filter.
                dataProvider.clearFilters();
            } else {
                // Set the filter to display only rows with a matching last name.
                dataProvider.setFilter(VaccinUtilisateurDTO::getVaccinDTO,
                        vaccinDTO -> vaccinDTO.getTypeVaccin().name().equals(selectedValue));
            }
        });
        return vaccinTypesFilter;
    }

    /**
     * Create a Header for our page
     * @return
     */
    public HorizontalLayout createHeader(){
        // --- Header Bar ---
        HorizontalLayout headerBar = new HorizontalLayout();
        headerBar.setWidth("100%");
        headerBar.setMargin(true);
        headerBar.setSpacing(true);
//        headerBar.setStyleName("header");

        // Show current user information
        String currentUser = loggedInUser.getName();
        Label userInfoLabel = new Label(" Logged in as :   " + currentUser);
        userInfoLabel.addStyleName(ValoTheme.LABEL_COLORED);
        userInfoLabel.addStyleName(ValoTheme.LABEL_BOLD);
        userInfoLabel.addStyleName(ValoTheme.LABEL_H3);

        // Logout button with placeholder logout logic.
        Button logoutButton = new Button("Logout", event -> {
            customAccessControl.logout();
            navigationEvent.fire(new NavigationEvent(LoginView.LOGIN));
        });
        logoutButton.addStyleName(ValoTheme.BUTTON_DANGER);

        // Add the user info and logout button to the header bar.
        headerBar.addComponents(userInfoLabel, logoutButton);
        headerBar.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        headerBar.setExpandRatio(userInfoLabel, 1);
        return headerBar;
    }


}
