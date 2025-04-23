package com.carnetvaccin.app;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@CDIUI("")
@Push
public class MainUI extends UI {

    @Inject
    private VaccinUtilisateurFacade vUtilisateurFacade;

    @Inject
    private VaccinFacade vFacade;

    @Inject
    private UtilisateurFacade utilisateurFacade;

    private Grid<VaccinUtilisateurDTO> vUtilisateurGrid = new Grid<>(VaccinUtilisateurDTO.class);



    @Override
    protected void init(VaadinRequest vaadinRequest) {
//        dataService.printPopularProgrammingLanguages();

        final VerticalLayout layout = new VerticalLayout();
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        Button button = new Button("Click Me");
//        button.addClickListener(e -> {
//            layout.addComponent(new Label( serviceHello.sayHello(name.getValue())));
//        });
//
//        layout.addComponents(name, button);
//        updateProgrammingLanguagelist();
        updateVaccinUtilisateurList();
        Label title = new Label("Carnet de Vaccination");
        title.setStyleName("h1");
        layout.addComponent(title);
        layout.addComponents(vUtilisateurGrid);
        setContent(layout);

    }

    public void updateVaccinUtilisateurList(){
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("fxfrancky@gmail.com");
        utilisateurDTO.setPassword("Password1");
        utilisateurDTO.setFirstName("Franck");
        utilisateurDTO.setLastName("Owen");
        utilisateurDTO.setUserName("fowwen");
        utilisateurDTO.setDateNaissance(LocalDate.now());

        utilisateurFacade.create(utilisateurDTO);

        UtilisateurDTO utilisateurDTOCreated = utilisateurFacade.findByEmail(utilisateurDTO.getEmail());

        VaccinDTO vaccinDTO = new VaccinDTO();
        vaccinDTO.setTypeVaccin(TypeVaccinEnum.COMBINE);
        vaccinDTO.setNumDose(1);
        vaccinDTO.setNbrMonthsDose(0);
        vaccinDTO.setVaccinDescription("Vaccin combiné Description");

        vFacade.create(vaccinDTO);
        VaccinDTO vaccinDTOCreated = vFacade.findVaccinByTypeAndDose(vaccinDTO.getTypeVaccin().name(),vaccinDTO.getNumDose());

        VaccinDTO vaccinDTO2 = new VaccinDTO();
        vaccinDTO2.setTypeVaccin(TypeVaccinEnum.COMBINE);
        vaccinDTO2.setNumDose(2);
        vaccinDTO2.setNbrMonthsDose(4);
        vaccinDTO2.setVaccinDescription("Vaccin combiné Description");

        vFacade.create(vaccinDTO2);
        VaccinDTO vaccinDTOCreated2 = vFacade.findVaccinByTypeAndDose(vaccinDTO2.getTypeVaccin().name(),vaccinDTO2.getNumDose());



        VaccinUtilisateurDTO vUtilisateurDTO = new VaccinUtilisateurDTO();
        vUtilisateurDTO.setUtilisateurDTO(utilisateurDTOCreated);
        vUtilisateurDTO.setVaccinDTO(vaccinDTOCreated);
//        vUtilisateurDTO.setId(v);
        vUtilisateurDTO.setCommentairesVaccin("Premier Vaccin Combiné");
        vUtilisateurDTO.setDateVaccination(LocalDate.now());
        vUtilisateurDTO.setLieuVacctination("Montigny Les Metz");


        VaccinUtilisateurDTO vUtilisateurDTO2 = new VaccinUtilisateurDTO();
        vUtilisateurDTO2.setUtilisateurDTO(utilisateurDTOCreated);
        vUtilisateurDTO2.setVaccinDTO(vaccinDTOCreated2);
        vUtilisateurDTO2.setCommentairesVaccin("Deuxième Vaccin Combiné");
        vUtilisateurDTO2.setDateVaccination(LocalDate.now());
        vUtilisateurDTO2.setLieuVacctination("Metz");

        vUtilisateurFacade.create(vUtilisateurDTO);
        VaccinUtilisateurDTO vaccinUtilisateurDTOCreated  = vUtilisateurFacade.findByUtilisaterIDAndVaccinId(vaccinDTOCreated.getId(),utilisateurDTOCreated.getId());

        vUtilisateurFacade.create(vUtilisateurDTO2);
        VaccinUtilisateurDTO vaccinUtilisateurDTOCreated2  = vUtilisateurFacade.findByUtilisaterIDAndVaccinId(vaccinDTOCreated2.getId(),utilisateurDTOCreated.getId());

        List<VaccinUtilisateurDTO> vUtilisateursDTOList = new ArrayList<>();
        vUtilisateursDTOList.add(vaccinUtilisateurDTOCreated);
        vUtilisateursDTOList.add(vaccinUtilisateurDTOCreated2);

        /**
         *
         *     List<Order> allOrders = (List<Order>) orderRepository.findAll();
         *     grid.setItems(allOrders);
         *     grid.setColumnOrder("id", "login", "dateOfOrder");
         *     grid.removeColumn("food");
         */

        /**
         *
         *
         *    private VaccinDTO vaccinDTO;
         *
         *     private UtilisateurDTO utilisateurDTO;
         *
         *     private LocalDate dateVaccination;
         *
         *     private String lieuVacctination;
         *
         *     private String commentairesVaccin;
         */
        vUtilisateurGrid.setItems(vUtilisateursDTOList);
        vUtilisateurGrid.removeAllColumns();
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getId).setCaption("Identifiant");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getUtilisateurDTO().getId()).setCaption("Utilisateur ID");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getTypeVaccin()).setCaption("Type Vaccin");
        vUtilisateurGrid.addColumn(vUtilisateur -> vUtilisateur.getVaccinDTO().getNumDose()).setCaption("Dose");

        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getDateVaccination).setCaption("Date Vaccination");
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getCommentairesVaccin).setCaption("Commentaire vaccination");
        vUtilisateurGrid.addColumn(VaccinUtilisateurDTO::getLieuVacctination).setCaption("Lieu Vaccination");
        vUtilisateurGrid.setWidthFull();

    }


}
