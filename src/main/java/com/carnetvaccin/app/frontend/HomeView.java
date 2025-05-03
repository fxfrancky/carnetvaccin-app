package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.carnetvaccin.app.frontend.security.LoginView;
import com.carnetvaccin.app.frontend.utilisateur.ProfileForm;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.collections4.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CDIView(HomeView.NAME)
public class HomeView extends VerticalLayout implements View {

    public static final String NAME = "home";

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private UI ui;


    @Inject
    private AccessControl accessControl;

    @Inject
    private VaccinUtilisateurFacade vUtilisateurFacade;

    @Inject
    private VaccinFacade vFacade;

    @Inject
    private UtilisateurFacade utilisateurFacade;

    private Grid<VaccinUtilisateurDTO> vUtilisateurGrid = new Grid<>(VaccinUtilisateurDTO.class);


    TextField searchField = new TextField();

    // Add an extra header row to hold the filtering components.
    HeaderRow filterRow = vUtilisateurGrid.appendHeaderRow();

    private TabSheet tabSheet; // First TabSheet


    List<VaccinDTO> vaccinDTOList;

    private VaccinForm vaccinForm;
//
    private ProfileForm profileForm;

    @Inject
    public HomeView(){

        setMargin(true);
        setSpacing(true);
        setSizeFull(); // Occupy full screen space

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

//        Only allow authenticated users
     if (!((CustomAccessControl) accessControl).isUserSignedIn()
             || !((CustomAccessControl) accessControl).isUserInRole(Role.User)) {
         ui.getNavigator().navigateTo(LoginView.NAME);
        }
        init(); // Only build the view if the user is signed in
    }


//    @PostConstruct
    private void init(){
        removeAllComponents();
        updateVaccinUtilisateurList();
        // Add header
        addComponent(createHeader());
        // Add Title
        Label title = new Label(VaadinIcons.BOOK.getHtml() + "  Carnet de Vaccination  "+ VaadinIcons.BOOK.getHtml());
        title.setContentMode(ContentMode.HTML);
        title.setStyleName(ValoTheme.LABEL_H2);



        addComponent(title);

        // Initialize TabSheet 1
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.setStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);

        vaccinForm = new VaccinForm(this,vFacade,vUtilisateurFacade, accessControl);

        profileForm = new ProfileForm(utilisateurFacade, accessControl, ui);
        tabSheet.addTab(carnetDataGridTab(), "Carnet de vaccination", VaadinIcons.CALENDAR );
        tabSheet.addTab(profileForm, "Mon Profil", VaadinIcons.USER);
//        tabSheet.addTab(new VerticalLayout(new Label("Content for the second tab")), "Mon Profil");
        tabSheet.setStyleName("background-color: #f0f0f0; border-radius: 5px;");
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);  // Adds borders
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        // Add TabSheets to the layout
        addComponents(tabSheet);
        setExpandRatio(tabSheet, 1); // Let the first TabSheet expand

    }

    /**
     * Create DataGrid
     * @return
     */
    private VerticalLayout carnetDataGridTab() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

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
        Button addVaccinUtilisateurBtn = new Button("Add new Vaccin", VaadinIcons.PLUS);
        // Apply custom green color using inline styles
//        addVaccinUtilisateurBtn.addStyleName("green-primary-button");
        addVaccinUtilisateurBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        addVaccinUtilisateurBtn.setStyleName("background-color: green !important; color: white !important;");        // Apply custom green color using CSS class

        addVaccinUtilisateurBtn.addClickListener(e -> {
            vUtilisateurGrid.asSingleSelect().clear();
            VaccinDTO vaccinDTO = new VaccinDTO();
            if(CollectionUtils.isNotEmpty(vaccinDTOList)) {
                vaccinDTO = vaccinDTOList.get(0);
            } else {
                vaccinDTO.setId(1L);
                vaccinDTO.setTypeVaccin(TypeVaccinEnum.RSV);
                vaccinDTO.setNbrMonthsDose(0);
                vaccinDTO.setNumDose(1);
                vaccinDTO.setVaccinDescription("2, 1745945986976, 0, 1, 'RSV', NULL, ' RSV (virus respiratoire syncytial) : administration d�un traitement préventif (produit d immunisation passive) qui prot�ge contre la bronchiolite, de préférence avant la sortie de la maternité, en pé-riode de haute circulation du virus, de septembre � février. ");
            }
                VaccinUtilisateurDTO vaccinUtilisateurDTO = new VaccinUtilisateurDTO();
                vaccinUtilisateurDTO.setVaccinDTO(vaccinDTO);
            UtilisateurDTO loggedInUser = ((CustomAccessControl)accessControl).getCurrentUser();
                vaccinUtilisateurDTO.setUtilisateurDTO(loggedInUser);
                vaccinForm.setVaccinUtilisateurDTO(vaccinUtilisateurDTO);
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addVaccinUtilisateurBtn);
        toolbar.setSizeFull();
        toolbar.setMargin(true);
        toolbar.setSpacing(true);
        layout.addComponent(toolbar);
//
        HorizontalLayout main = new HorizontalLayout(vUtilisateurGrid, vaccinForm);
        main.setSizeFull();
        vUtilisateurGrid.setSizeFull();
        main.setExpandRatio(vUtilisateurGrid, 1);
        layout.addComponents(main);


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
            if (((CustomAccessControl)accessControl).isUserSignedIn()){
                UtilisateurDTO loggedInUser = ((CustomAccessControl)accessControl).getCurrentUser();
                vUtilisateursDTOList = vUtilisateurFacade.findrByTerms(searchField.getValue(), loggedInUser.getId());
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
        // Show current user information
        String currentUser = ((CustomAccessControl)accessControl).getPrincipalName();
        Label userInfoLabel = new Label(" Logged in as :   " + currentUser);
        userInfoLabel.addStyleName(ValoTheme.LABEL_COLORED);
        userInfoLabel.addStyleName(ValoTheme.LABEL_BOLD);


        Button logoutButton = new Button("Logout", VaadinIcons.SIGN_OUT);
        logoutButton.addClickListener(event -> {
            ((CustomAccessControl) accessControl).signOut();

            ui.getNavigator().navigateTo(LoginView.NAME);

        });

        logoutButton.addStyleName(ValoTheme.BUTTON_DANGER);

        // Add the user info and logout button to the header bar.
        headerBar.addComponents(userInfoLabel, logoutButton);
        headerBar.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        headerBar.setExpandRatio(userInfoLabel, 1);
        return headerBar;
    }


}
