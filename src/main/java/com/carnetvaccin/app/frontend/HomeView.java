package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.notification.MessagesButton;
import com.carnetvaccin.app.api.notification.NotificationFacade;
import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;
import static com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE;


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
    private NotificationFacade notificationFacade;

    @Inject
    private VaccinFacade vFacade;

    @Inject
    private UtilisateurFacade utilisateurFacade;

    private Grid<VaccinUtilisateurDTO> vUtilisateurGrid = new Grid<VaccinUtilisateurDTO>("Historique des Vaccins");

    private Button addVaccinUtilisateurButton;

    private TextField searchField;

    private Label title;

    private CssLayout filtering;

    private Button clearFilterTextBtn;

    // Add an extra header row to hold the filtering components.
    HeaderRow filterRow = vUtilisateurGrid.appendHeaderRow();

    private TabSheet mainTabSheet; // First TabSheet

    private VerticalLayout vaccinUtilisateurTab;

    private Button logoutButton;

    private VaccinForm vUtilisateurForm;
    //
    private ProfileForm profileForm;

    private HorizontalLayout headerLayout;

    private HorizontalLayout mainContentLayout; // main layout to hold grid and form
    private HorizontalLayout searchAndAddLayout;

    private ListDataProvider<VaccinUtilisateurDTO> vaccinUtilisateurDataProvider;
    private List<VaccinUtilisateurDTO> vaccinUtilisateurList;

    private UtilisateurDTO loggedInUser;

    private List<VaccinDTO> vaccinDTOList;

    public HomeView() {

    }

    private void buildView() {
        removeAllComponents();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(headerLayout);
        mainLayout.addComponent(title);
        mainLayout.addComponent(mainTabSheet);
        addComponent(mainLayout);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //   Only allow authenticated users
        if (!((CustomAccessControl) accessControl).isUserSignedIn()
                || !((CustomAccessControl) accessControl).isUserInRole(Role.User)) {
            ui.getNavigator().navigateTo(LoginView.NAME);
        }
    }


    @PostConstruct
    private void init() {
        loggedInUser = ((CustomAccessControl) accessControl).getCurrentUser();
        setupVaccinUtilisateurData();
        createComponents();
        configureComponents(); //call configureComponents
        buildView();
    }

    private void setupVaccinUtilisateurData() {
        vaccinUtilisateurList = vUtilisateurFacade.findAllVaccinUtilisateurByUserId(loggedInUser.getId());
        if (vaccinUtilisateurList == null || vaccinUtilisateurList.isEmpty()) {
            vaccinUtilisateurList = new ArrayList<>();
        }
        vaccinUtilisateurDataProvider = new ListDataProvider<>(vaccinUtilisateurList);
        vaccinDTOList = vFacade.findAllVaccin();
    }

    //    Update List of VaccinUtilisateur
    public void updateVaccinUtilisateurDTOList() {
        vaccinUtilisateurList = vUtilisateurFacade.findrByTerms(searchField.getValue(), loggedInUser.getId());
        if (vaccinUtilisateurList == null || vaccinUtilisateurList.isEmpty()) {
            vaccinUtilisateurList = new ArrayList<>();
        }
        // Update in the Grid
        vaccinUtilisateurDataProvider.getItems().clear();
        vaccinUtilisateurDataProvider.getItems().addAll(vaccinUtilisateurList);
        vaccinUtilisateurDataProvider.refreshAll();
    }

    private void createComponents() {
        // create logout button
        logoutButton = new Button("Logout", VaadinIcons.SIGN_OUT);
        // create header
        headerLayout = createHeader();
        // Add Title
        title = new Label(VaadinIcons.BOOK.getHtml() + "  Carnet de Vaccination  " + VaadinIcons.BOOK.getHtml());

        //  VaccinUtilisateur Tab Components
        vaccinUtilisateurTab = new VerticalLayout();

        // Main TabSheet
        mainTabSheet = new TabSheet();
        // Main TabSheet Content
        mainContentLayout = new HorizontalLayout(); // Initialize the layout
        searchAndAddLayout = new HorizontalLayout();

        addVaccinUtilisateurButton = new Button("Add new Vaccin", VaadinIcons.PLUS);

        vUtilisateurForm = new VaccinForm(this, accessControl, vFacade); // Pass HomeView instance

        profileForm = new ProfileForm(utilisateurFacade, accessControl, ui);

        // Search Field
        searchField = new TextField();

        // Button to clear the filter
        clearFilterTextBtn = new Button(VaadinIcons.CLOSE);

        // Filtering Layout
        filtering = new CssLayout();
    }


    private void configureComponents() {
        // configure Logout Button
        logoutButton.addClickListener(this::logout);
        logoutButton.addStyleName(ValoTheme.BUTTON_DANGER);

        // configure title
        title.setContentMode(ContentMode.HTML);
        title.setStyleName(ValoTheme.LABEL_H2);

        // Add Vaccin Button
        addVaccinUtilisateurButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

        // configure VaccinUtilisateurDataGrid
        configureVaccinUtilisateurDataGrid();

        // Add filtering
        searchField.setPlaceholder("Search (Description vaccin, commentaire, Lieu,  type vaccin)...");
        searchField.setWidth("100%");
        searchField.addValueChangeListener(e -> updateVaccinUtilisateurDTOList());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(evnt -> {
            String searchTerm = evnt.getValue();
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                // Clear filter if search term is empty
                vaccinUtilisateurDataProvider.clearFilters();
            } else {
                final String lowerCaseSearchTerm = searchTerm.toLowerCase();
                vaccinUtilisateurDataProvider.setFilter(vaccinUtilisateurDTO ->
                        vaccinUtilisateurDTO.getCommentairesVaccin().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getVaccinDTO().getVaccinDescription().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getVaccinDTO().getTypeVaccin().name().toLowerCase().contains(lowerCaseSearchTerm) ||
                                vaccinUtilisateurDTO.getLieuVacctination().toLowerCase().contains(lowerCaseSearchTerm)
                );
            }
        });

        // Clear Button Filter
        clearFilterTextBtn.setDescription("Clear the current filter");
        clearFilterTextBtn.addClickListener(e -> {
            searchField.clear();
        });

        // Add filtering
        CssLayout filtering = new CssLayout();
        filtering.addComponents(searchField, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        // ToolBar
        searchAndAddLayout.addComponents(filtering, addVaccinUtilisateurButton);
        searchAndAddLayout.setSpacing(true);
        searchAndAddLayout.setMargin(true);
        searchAndAddLayout.setSizeFull();
        searchAndAddLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        // configure vaccinUtilisateurTab
        vaccinUtilisateurTab.addComponents(searchAndAddLayout);
        vaccinUtilisateurTab.addComponents(mainContentLayout);
        vaccinUtilisateurTab.setSizeFull();
        vaccinUtilisateurTab.setSpacing(true);
        vaccinUtilisateurTab.setMargin(true);

        mainContentLayout.addComponents(vUtilisateurGrid, vUtilisateurForm);

        addVaccinUtilisateurButton.addClickListener(e -> {
            vUtilisateurForm.addNewVaccinUtilisateur();
            showForm(); //show form
        });
        vUtilisateurForm.setVisible(false);

        vUtilisateurForm.setWidth("20%");
        vUtilisateurGrid.setWidth("80%");

        mainContentLayout.setExpandRatio(vUtilisateurGrid, 80);
        mainContentLayout.setExpandRatio(vUtilisateurForm, 20);
        mainContentLayout.setSizeFull();
//        mainContentLayout.setSpacing(true);

        setGridFullWidth();

        // Main TabSheet
        mainTabSheet.setStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        mainTabSheet.addTab(vaccinUtilisateurTab, "Carnet de vaccination", VaadinIcons.CALENDAR);
        mainTabSheet.addTab(profileForm, "Mon Profil", VaadinIcons.USER);
        mainTabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);  // Adds borders
        mainTabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
    }

    private void configureVaccinUtilisateurDataGrid() {

        // Configure VaccinUtilisateur Grid
        vUtilisateurGrid.setDataProvider(vaccinUtilisateurDataProvider);

        vUtilisateurGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        // Handle selection change in Grid
        vUtilisateurGrid.asSingleSelect().addValueChangeListener(event -> {
            VaccinUtilisateurDTO selectedVaccinutilisateur = event.getValue();
            if (selectedVaccinutilisateur != null) {
                vUtilisateurForm.editVaccinUtilisateur(selectedVaccinutilisateur);
                vUtilisateurForm.setCurrentVaccin(selectedVaccinutilisateur.getVaccinDTO());
                vUtilisateurForm.getComboBox().setValue(selectedVaccinutilisateur.getVaccinDTO());
                vaccinUtilisateurDataProvider.refreshAll();
                showForm();
            } else {
                vUtilisateurForm.setVisible(false);
                setGridFullWidth();
            }
        });

        vUtilisateurGrid.removeAllColumns();
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin()).setCaption("Type Vaccin").setId("typeVaccin");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getVaccinDescription()).setCaption("Description du vaccin").setId("vaccinDescription").setWidth(1300);
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getNumDose()).setCaption("Dose").setId("numDose");

        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getDateVaccination).setCaption("Date Vaccination").setId("dateVaccination").setSortable(true);
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getCommentairesVaccin).setCaption("Commentaire vaccination").setId("commentairesVaccin");
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getLieuVacctination).setCaption("Lieu Vaccination").setId("lieuVacctination");
//        vUtilisateurGrid.setSizeFull();
        filterRow.getCell("typeVaccin").setComponent(getvaccinTypesFilters());

    }

    private void logout(Button.ClickEvent event) {
        ((CustomAccessControl) accessControl).signOut();
        ui.getNavigator().navigateTo(LoginView.NAME);
    }

    /**
     * Create a Header for our page
     *
     * @return
     */
    public HorizontalLayout createHeader() {
        // --- Header Bar ---
        HorizontalLayout headerBar = new HorizontalLayout();
        headerBar.setWidth("100%");
        headerBar.setMargin(true);
        headerBar.setSpacing(true);
        // Show current user information
        String currentUser = loggedInUser.getFirstName() + "  -  " + loggedInUser.getLastName();
        Label userInfoLabel = new Label(" Welcome   " + currentUser);
        userInfoLabel.addStyleName(ValoTheme.LABEL_COLORED);
        userInfoLabel.addStyleName(ValoTheme.LABEL_BOLD);

        // Add Notification Ring
        notificationFacade.initialize(loggedInUser);
        MessagesButton msgBtn = notificationFacade.getMessagesButton();

        // Add the user info and logout button to the header bar.
        headerBar.addComponents(userInfoLabel, msgBtn, logoutButton);
        headerBar.setComponentAlignment(msgBtn, Alignment.MIDDLE_CENTER);

//        headerBar.addComponents(userInfoLabel, logoutButton);

        headerBar.setComponentAlignment(logoutButton, Alignment.MIDDLE_RIGHT);
        headerBar.setExpandRatio(userInfoLabel, 1);
        return headerBar;
    }

    // Method to add new VaccinUtilisateur to the Grid and database
    public void addVaccinUtilisateur(VaccinUtilisateurDTO newVaccinUtilisateur) {
        try {
            // Persist the new entity to the database
            vUtilisateurFacade.saveVaccinUtilisateur(newVaccinUtilisateur);
            // Add to the Grid
            vaccinUtilisateurList.add(newVaccinUtilisateur);
            vaccinUtilisateurDataProvider.getItems().add(newVaccinUtilisateur); // Use the data provider
            vaccinUtilisateurDataProvider.refreshAll();
            filterRow.getCell("typeVaccin").setComponent(getvaccinTypesFilters());
            Notification.show("Vaccin Utilisateur added successfully", HUMANIZED_MESSAGE);
        } catch (CarnetException e) {
            e.printStackTrace();
            Notification.show("Error adding Vaccin Utilisateur: ", ERROR_MESSAGE);
        }
    }


    // Method to update VaccinUtilisateur in the Grid and database
    public void updateVaccinUtilisateur(VaccinUtilisateurDTO updatedVaccinUtilisateur) {
        try {
            // Merge the updated entity to the database
            vUtilisateurFacade.updateVaccinUtilisateur(updatedVaccinUtilisateur);
            // Update in the Grid
            vaccinUtilisateurDataProvider.getItems().clear();
            vaccinUtilisateurDataProvider.getItems().addAll(vaccinUtilisateurList);
            vaccinUtilisateurDataProvider.refreshAll();
            Notification.show("Vaccin Utilisateur updated successfully", HUMANIZED_MESSAGE);
        } catch (CarnetException e) {
            e.printStackTrace();
            Notification.show("Error updating Vaccin Utilisateur: ", ERROR_MESSAGE);
        }
    }


    /**
     * Get Vaccin Types Filter
     *
     * @return
     */
    public ComboBox<String> getvaccinTypesFilters() {
        // Create a ComboBox to filter the "Last Name" column.
        ComboBox<String> vaccinTypesFilter = new ComboBox<>();
        vaccinTypesFilter.setPlaceholder("Select Type Vaccin");
        vaccinTypesFilter.setWidth("150px");
        vaccinTypesFilter.setSizeFull();

        updateVaccinUtilisateurDTOList();
        // Populate the ComboBox with distinct last names from the data.
        List<String> distinctVaccinTypes = vaccinUtilisateurList.stream()
                .map(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin().name())
                .distinct()
                .collect(Collectors.toList());
        vaccinTypesFilter.setItems(distinctVaccinTypes);

        // Add a listener to filter the grid upon selecting a value.
        vaccinTypesFilter.addValueChangeListener(event -> {
            String selectedValue = event.getValue();
            if (selectedValue == null || selectedValue.isEmpty()) {
                // No selection: clear the filter.
                vaccinUtilisateurDataProvider.clearFilters();
            } else {
                // Set the filter to display only rows with a matching last name.
                vaccinUtilisateurDataProvider.setFilter(VaccinUtilisateurDTO::getVaccinDTO,
                        vaccinDTO -> vaccinDTO.getTypeVaccin().name().equals(selectedValue));
            }
        });
        return vaccinTypesFilter;
    }

    public void showForm(){
        vUtilisateurForm.setVisible(true);
        mainContentLayout.setExpandRatio(vUtilisateurForm, 20);
        mainContentLayout.setExpandRatio(vUtilisateurGrid, 80);
        vUtilisateurGrid.setWidth("100%");

    }

    public void setGridFullWidth() {
        vUtilisateurGrid.setWidth("100%");
        mainContentLayout.setExpandRatio(vUtilisateurGrid, 100);
        mainContentLayout.setExpandRatio(vUtilisateurForm, 0);
        // Ensure the form is not taking up any space
        vUtilisateurForm.setWidth("0%");
    }
}
