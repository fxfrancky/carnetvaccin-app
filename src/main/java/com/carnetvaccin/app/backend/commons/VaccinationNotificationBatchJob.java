package com.carnetvaccin.app.backend.commons;

import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.notification.NotificationService;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import com.carnetvaccin.app.backend.vaccin.VaccinService;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateurService;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class VaccinationNotificationBatchJob {

    @Inject
    private UtilisateurService utilisateurService;

    @Inject
    private VaccinUtilisateurService vaccinUtilisateurService;

    @Inject
    private VaccinService vaccinService;

    @Inject
    private EmailService emailService;

    @Inject
    private NotificationService notificationService;

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false) // Runs daily at midnight
    public void checkVaccinationSchedules() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllActiveUtilisateurs();
        LocalDate today = LocalDate.now();

        for (Utilisateur utilisateur : utilisateurs) {
            LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
            long monthsOld = birthDate.until(today).toTotalMonths();
            List<VaccinUtilisateur> vaccinUtilisateurList = vaccinUtilisateurService.findAllVaccinUtilisateurByUserId(utilisateur.getUtilisateurId());

            addNotificationsToVaccinRSV(utilisateur,  vaccinUtilisateurList);

            addNotificationsToVaccinCOMBINE(utilisateur,  vaccinUtilisateurList);

            addNotificationsToVaccinCOMBINERORV(utilisateur,  vaccinUtilisateurList);

            addNotificationsToVaccinROTAROVIRUS(utilisateur, vaccinUtilisateurList);

            addNotificationsToVaccinPNEUMONOCOQUES(utilisateur, vaccinUtilisateurList);

            addNotificationsToVaccinMENINGOCOQUEB(utilisateur, vaccinUtilisateurList);

            addNotificationsToVaccinMENINGOCOQUEACWY(utilisateur, vaccinUtilisateurList);

        }
    }

    /**
     *  Add a new notification For a User and a Vaccin
     * @param utilisateur
     * @param vaccin
     * @param message
     */
    private void addNotification(Utilisateur utilisateur, Vaccin vaccin, String message) {
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setVaccin(vaccin);
        notification.setMessage(message);
        notification.setDateNotification(LocalDate.now());
        notification.setRead(false);

        notificationService.addNotification(notification);
        notificationService.notifyUI(notification);
        emailService.sendEmail(utilisateur.getEmail(), "Vaccination Reminder", message);
    }


    /**
     *  Add notification for Vaccin Type RSV
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinRSV(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.RSV)).collect(Collectors.toList());

        // Check the first dose for RSV
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.RSV.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.RSV.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 0) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of RSV vaccine is due!");
                }
            }
            //Check if we are 6 month and the first dose is still due
            if(monthsOld ==6){
                Optional<Vaccin> vaccinDose2 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.RSV.name(),2);
                if (vaccinDose2.isPresent()){
                        addNotification(utilisateur, vaccinDose2.get(),"Your second dose of RSV vaccine is due!");
                }

            }
        }

    }

    /**
     * Add notification for Vaccin Type COMBINE
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinCOMBINE(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.COMBINE)).collect(Collectors.toList());

        // Check the first dose for COMBINE
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.COMBINE.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.COMBINE.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 2) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of COMBINE vaccine is due!");
                }
            }
        }

        // Check the second dose for COMBINE
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.COMBINE.name(), 2)){
            Optional<Vaccin> vaccinDose2 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.COMBINE.name(),2);
            if (vaccinDose2.isPresent()){
                if (monthsOld == 4) {
                    addNotification(utilisateur, vaccinDose2.get(),"Your second dose of COMBINE vaccine is due!");
                }
            }
        }

        // Check the third dose for COMBINE
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.COMBINE.name(), 3)){
            Optional<Vaccin> vaccinDose3 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.COMBINE.name(),3);
            if (vaccinDose3.isPresent()){
                if (monthsOld == 11) {
                    addNotification(utilisateur, vaccinDose3.get(),"Your third dose of COMBINE vaccine is due!");
                }
            }
        }

    }


    /**
     * Add notification for Vaccin Type COMBINE RORV
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinCOMBINERORV(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.COMBINE_RORV)).collect(Collectors.toList());

        // Check the first dose for COMBINE
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.COMBINE_RORV.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.COMBINE_RORV.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 12) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of COMBINE RORV vaccine is due!");
                }
            }
        }

    }


    /**
     * Add notification for Vaccin Type ROTAROVIRUS
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinROTAROVIRUS(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.ROTAROVIRUS)).collect(Collectors.toList());

        // Check the first dose for ROTAROVIRUS
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.ROTAROVIRUS.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.ROTAROVIRUS.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 2) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of ROTAROVIRUS vaccine is due!");
                }
            }
        }

        // Check the second dose for ROTAROVIRUS
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.ROTAROVIRUS.name(), 2)){
            Optional<Vaccin> vaccinDose2 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.ROTAROVIRUS.name(),2);
            if (vaccinDose2.isPresent()){
                if (monthsOld == 3) {
                    addNotification(utilisateur, vaccinDose2.get(),"Your second dose of ROTAROVIRUS vaccine is due!");
                }
            }
        }

        // Check the third dose for ROTAROVIRUS
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.ROTAROVIRUS.name(), 3)){
            Optional<Vaccin> vaccinDose3 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.ROTAROVIRUS.name(),3);
            if (vaccinDose3.isPresent()){
                if (monthsOld == 4) {
                    addNotification(utilisateur, vaccinDose3.get(),"Your third dose of ROTAROVIRUS vaccine is due!");
                }
            }
        }

    }


    /**
     * Add notification for Vaccin Type PNEUMONOCOQUES
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinPNEUMONOCOQUES(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.PNEUMONOCOQUES)).collect(Collectors.toList());

        // Check the first dose for PNEUMONOCOQUES
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.PNEUMONOCOQUES.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.PNEUMONOCOQUES.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 2) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of PNEUMONOCOQUES vaccine is due!");
                }
            }
        }

        // Check the second dose for PNEUMONOCOQUES
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.PNEUMONOCOQUES.name(), 2)){
            Optional<Vaccin> vaccinDose2 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.PNEUMONOCOQUES.name(),2);
            if (vaccinDose2.isPresent()){
                if (monthsOld == 4) {
                    addNotification(utilisateur, vaccinDose2.get(),"Your second dose of PNEUMONOCOQUES vaccine is due!");
                }
            }
        }

        // Check the third dose for PNEUMONOCOQUES
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.PNEUMONOCOQUES.name(), 3)){
            Optional<Vaccin> vaccinDose3 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.PNEUMONOCOQUES.name(),3);
            if (vaccinDose3.isPresent()){
                if (monthsOld == 11) {
                    addNotification(utilisateur, vaccinDose3.get(),"Your third dose of PNEUMONOCOQUES vaccine is due!");
                }
            }
        }

    }


    /**
     * Add notification for Vaccin Type MENINGOCOQUE_B
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinMENINGOCOQUEB(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.MENINGOCOQUE_B)).collect(Collectors.toList());

        // Check the first dose for MENINGOCOQUE_B
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.MENINGOCOQUE_B.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.MENINGOCOQUE_B.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 3) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of MENINGOCOQUE_B vaccine is due!");
                }
            }
        }

        // Check the second dose for MENINGOCOQUE_B
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.MENINGOCOQUE_B.name(), 2)){
            Optional<Vaccin> vaccinDose2 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.MENINGOCOQUE_B.name(),2);
            if (vaccinDose2.isPresent()){
                if (monthsOld == 5) {
                    addNotification(utilisateur, vaccinDose2.get(),"Your second dose of MENINGOCOQUE_B vaccine is due!");
                }
            }
        }

        // Check the third dose for MENINGOCOQUE_B
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.MENINGOCOQUE_B.name(), 3)){
            Optional<Vaccin> vaccinDose3 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.MENINGOCOQUE_B.name(),3);
            if (vaccinDose3.isPresent()){
                if (monthsOld == 12) {
                    addNotification(utilisateur, vaccinDose3.get(),"Your third dose of MENINGOCOQUE_B vaccine is due!");
                }
            }
        }

    }

    /**
     * Add notification for Vaccin Type MENINGOCOQUE_ACWY
     * @param utilisateur
     * @param vaccinUtilisateurList
     */
    public void addNotificationsToVaccinMENINGOCOQUEACWY(Utilisateur utilisateur, List<VaccinUtilisateur> vaccinUtilisateurList){

        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(utilisateur.getDateNaissance());
        long monthsOld = birthDate.until(today).toTotalMonths();

        List<VaccinUtilisateur> vaccinUtilisateurRSVList = vaccinUtilisateurList.stream().filter(v -> v.getVaccin().getTypeVaccin().equals(TypeVaccinEnum.MENINGOCOQUE_ACWY)).collect(Collectors.toList());

        // Check the first dose for COMBINE
        if (!containsVaccin(vaccinUtilisateurRSVList, TypeVaccinEnum.MENINGOCOQUE_ACWY.name(), 1)){
            Optional<Vaccin> vaccinDose1 = vaccinService.findVaccinByTypeAndDose(TypeVaccinEnum.MENINGOCOQUE_ACWY.name(),1);
            if (vaccinDose1.isPresent()){
                if (monthsOld == 13) {
                    addNotification(utilisateur, vaccinDose1.get(),"Your first dose of MENINGOCOQUE ACWY vaccine is due!");
                }
            }
        }

    }


    /**
     * Check if the list contain this vaccin type for this dose
     * @param list
     * @param typeVaccin
     * @param numDose
     * @return
     */
    public boolean containsVaccin(List<VaccinUtilisateur> list, String typeVaccin, int numDose) {
        return list.stream()
                .map(VaccinUtilisateur::getVaccin)
                .anyMatch(v -> v.getTypeVaccin().name().equals(typeVaccin) && v.getNumDose() == numDose);
    }


}