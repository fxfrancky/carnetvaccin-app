package com.carnetvaccin.app.api.commons;

import com.carnetvaccin.app.api.notification.NotificationDTO;
import com.carnetvaccin.app.api.notification.NotificationFacade;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.api.vaccin.VaccinFacade;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurDTO;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurFacade;
import com.carnetvaccin.app.backend.commons.EmailService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Startup
public class VaccinationNotificationBatchJob {

    @Inject
    private UtilisateurFacade utilisateurFacade;

    @Inject
    private VaccinUtilisateurFacade vaccinUtilisateurFacade;

    @Inject
    private VaccinFacade vaccinFacade;

    @Inject
    private EmailService emailService;

    @Inject
    private NotificationFacade notificationFacade;

    private static final Logger logger = LoggerFactory.getLogger(VaccinationNotificationBatchJob.class);
    private ScheduledExecutorService scheduler;

//    /**
//     *  Add a new notification For a User and a Vaccin
//     * @param utilisateur
//     * @param vaccin
//     * @param message
//     */
    private void addNotification(UtilisateurDTO utilisateur, VaccinDTO vaccin, String message) {
        NotificationDTO notification = new NotificationDTO();
        notification.setUtilisateurDTO(utilisateur);
        notification.setVaccinDTO(vaccin);
        notification.setMessage(message);
        notification.setDateNotification(LocalDate.now());
        notification.setRead(false);

        notificationFacade.addNotification(notification);
        emailService.sendEmail(utilisateur.getEmail(), "Vaccination Reminder", message);
    }


@PostConstruct
public void startNotificationScheduler() {
    scheduler = Executors.newSingleThreadScheduledExecutor();
    // Run the check daily at 08:00 AM
    long initialDelay = calculateInitialDelay();
    scheduler.scheduleAtFixedRate(this::sendVaccinationNotifications, initialDelay,5 * 60 * 1000, TimeUnit.MILLISECONDS);
    logger.info("Vaccination notification scheduler started.");
}

@PreDestroy
public void stopNotificationScheduler() {
    if (scheduler != null) {
        scheduler.shutdownNow();
        logger.info("Vaccination notification scheduler stopped.");
    }
}

private long calculateInitialDelay() {
    // Calculate the delay until the next 8:00 AM
    java.util.Calendar now = java.util.Calendar.getInstance();
    java.util.Calendar nextRun = java.util.Calendar.getInstance();
    nextRun.set(java.util.Calendar.HOUR_OF_DAY, 8);
    nextRun.set(java.util.Calendar.MINUTE, 0);
    nextRun.set(java.util.Calendar.SECOND, 0);
    nextRun.set(java.util.Calendar.MILLISECOND, 0);

    if (now.after(nextRun)) {
        nextRun.add(java.util.Calendar.DAY_OF_MONTH, 1);
    }
    return nextRun.getTimeInMillis() - now.getTimeInMillis();
}

private void sendVaccinationNotifications() {
    logger.info("Checking for due vaccinations...");
    LocalDate today = LocalDate.now();
    List<UtilisateurDTO> users = utilisateurFacade.findAllUtilisateurs();

    for (UtilisateurDTO user : users) {
        List<VaccinDTO> dueVaccinations = vaccinFacade.findDueVaccinationsForUser(today, user);
        for (VaccinDTO vaccin : dueVaccinations) {
            String message = "Reminder: Your vaccination (" + vaccin.getTypeVaccin().name() + ", Dose " + vaccin.getNumDose() + ") is due on " + today + "."; //date

            try {
                //send notification
                addNotification(user, vaccin, message);

                // Also send an email
                emailService.sendEmail(user.getEmail(), "Vaccination Reminder", message);

                // Find the VaccinUtilisateur and mark it as sent.
                List<VaccinUtilisateurDTO> userVaccinations = vaccinUtilisateurFacade.findAllVaccinUtilisateurByUserId(user.getId());
                for (VaccinUtilisateurDTO userVaccination : userVaccinations) {
                    if (userVaccination.getVaccinDTO().getTypeVaccin().name().equals(vaccin.getTypeVaccin().name())) {
                        vaccinUtilisateurFacade.markAsSent(userVaccination.getId());
                        break; // Assuming only one entry per vaccine type
                    }
                }
                logger.info("Notification sent to user: " + user.getUserName() + " for vaccination: " + vaccin.getTypeVaccin().name());
            } catch (Exception e) {
                logger.error("Failed to send notification for user: " + user.getUserName() + " and vaccine: " + vaccin.getTypeVaccin().name(), e);
                throw new CarnetException("Failed to send notification for user: " + user.getUserName() + " and vaccine: " + vaccin.getTypeVaccin().name() + e.getMessage());
            }
        }
    }
}
}