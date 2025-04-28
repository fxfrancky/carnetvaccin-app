package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.security.CustomAccessControl;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UIScoped
public class CarnetForm extends FormLayout {


    @Inject
    private UserInfo loggedInUser;

    @Inject
    private CustomAccessControl customAccessControl;

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


    public CarnetForm() {

        setMargin(true);
        setSpacing(true);

        final VerticalLayout layout = new VerticalLayout();

//        // create a header bar
//        HorizontalLayout headerBar = createHeader();
//        layout.addComponent(headerBar);

        updateVaccinUtilisateurList();
        Label title = new Label("Carnet de Vaccination");
        title.setStyleName(ValoTheme.LABEL_H2);
        layout.addComponent(title);

        // Add Search field
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
        HorizontalLayout toolbar = new HorizontalLayout(filtering);

        layout.addComponent(toolbar);

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

        layout.addComponents(vUtilisateurGrid);
        addComponents(layout);
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
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getVaccinDescription()).setCaption("Description du vaccin").setId("vaccinDescription");
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

}
