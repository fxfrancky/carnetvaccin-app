package com.carnetvaccin.app.frontend;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CDIView(Page.HOME)
@RolesAllowed(Role.User)
public class HomeView extends CustomComponent implements View {

    @Inject
    private VaccinUtilisateurFacade vUtilisateurFacade;

    @Inject
    private VaccinFacade vFacade;

    @Inject
    private UtilisateurFacade utilisateurFacade;

    private Grid<VaccinUtilisateurDTO> vUtilisateurGrid = new Grid<>(VaccinUtilisateurDTO.class);

    @Override
    public void enter(ViewChangeEvent event) {

        final VerticalLayout layout = new VerticalLayout();
        updateVaccinUtilisateurList();
        Label title = new Label("Carnet de Vaccination");
        title.setStyleName("h1");
        layout.addComponent(title);
        layout.addComponents(vUtilisateurGrid);
//        setContent(layout);
    }



    public void updateVaccinUtilisateurList(){
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("fxfrancky@gmail.com");
        utilisateurDTO.setPassword("Password1");
        utilisateurDTO.setFirstName("Franck");
        utilisateurDTO.setLastName("Owen");
        utilisateurDTO.setUserName("fowwen");
        utilisateurDTO.setAdmin(true);
        utilisateurDTO.setDateNaissance(LocalDate.now());

        utilisateurFacade.create(utilisateurDTO);

        UtilisateurDTO utilisateurDTOCreated = utilisateurFacade.getUserByEmail(utilisateurDTO.getEmail());


        UtilisateurDTO utilisateurDTO2 = new UtilisateurDTO();
        utilisateurDTO2.setEmail("fxfrancky@yahoo.com");
        utilisateurDTO2.setPassword("Password2");
        utilisateurDTO2.setFirstName("Franck");
        utilisateurDTO2.setLastName("Owen");
        utilisateurDTO2.setUserName("fowwen2");
        utilisateurDTO2.setAdmin(false);
        utilisateurDTO2.setDateNaissance(LocalDate.now());

        utilisateurFacade.create(utilisateurDTO2);

        UtilisateurDTO utilisateurDTOCreated2 = utilisateurFacade.getUserByEmail(utilisateurDTO2.getEmail());

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
        vUtilisateurDTO.setCommentairesVaccin("Premier Vaccin Combiné");
        vUtilisateurDTO.setDateVaccination(LocalDate.now());
        vUtilisateurDTO.setLieuVacctination("Montigny Les Metz");


        VaccinUtilisateurDTO vUtilisateurDTO2 = new VaccinUtilisateurDTO();
        vUtilisateurDTO2.setUtilisateurDTO(utilisateurDTOCreated2);
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
