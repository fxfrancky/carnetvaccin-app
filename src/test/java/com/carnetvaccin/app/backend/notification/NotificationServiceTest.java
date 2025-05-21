package com.carnetvaccin.app.backend.notification;

import com.carnetvaccin.app.backend.AbstractServiceTest;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest extends AbstractServiceTest {

    private NotificationService service;

    // Create and persist Utilisateur
    Utilisateur utilisateur = new Utilisateur();
    Vaccin vaccin = new Vaccin();
    Notification notification = new Notification();


    @BeforeEach
    void initService() {
        service = new NotificationService();
        service.setEm(entityManager);

        entityManager.clear();

        startTransaction();

        // Create and persist Utilisateur
//        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        entityManager.merge(utilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        vaccin.setVaccinId(1L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);
        vaccin.setNbrMonthsDose(4);
        vaccin.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);
        entityManager.merge(vaccin);
        entityManager.getTransaction().commit();

        startTransaction();
        notification.setNotificationId(1L);
        notification.setMessage("New vaccine notification");
        notification.setRead(false);
        notification.setUtilisateur(utilisateur);
        notification.setVaccin(vaccin);

        entityManager.merge(notification);
        entityManager.getTransaction().commit();
    }

    @Test
    void testAddNotification_Success() throws CarnetException {

        startTransaction();
        service.addNotification(notification);
        entityManager.getTransaction().commit();
        assertNotNull(entityManager.find(Notification.class, notification.getNotificationId()));
    }

    @Test
    void testMarkAsRead_Success() throws CarnetException {

        startTransaction();
        service.addNotification(notification);
        entityManager.getTransaction().commit();

        startTransaction();
        notification.setRead(true);
        service.markAsRead(notification.getNotificationId());
        entityManager.merge(notification);
        entityManager.getTransaction().commit();

        List<Notification> notificationReads = service.findAll();
        assertEquals(1, notificationReads.size());
        assertFalse(notificationReads.isEmpty());
        Notification isReadNotif = entityManager.find(Notification.class, notification.getNotificationId());
        assertTrue(isReadNotif.isRead());
    }

    @Test
    void testGetUnreadNotifications_Success() throws CarnetException {
        startTransaction();
        // Create and persist Utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        entityManager.merge(utilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        Vaccin vaccin = new Vaccin();
        vaccin.setVaccinId(1L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);
        vaccin.setNbrMonthsDose(4);
        vaccin.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);
        entityManager.merge(vaccin);
        entityManager.getTransaction().commit();

        Notification notification1 = new Notification();
        notification1.setMessage("Unread 1");
        notification1.setRead(false);
        notification1.setUtilisateur(utilisateur);
        notification1.setVaccin(vaccin);

        Notification notification2 = new Notification();
        notification2.setMessage("Unread 2");
        notification2.setRead(false);
        notification2.setUtilisateur(utilisateur);
        notification2.setVaccin(vaccin);

        startTransaction();
        service.addNotification(notification1);
        service.addNotification(notification2);
        entityManager.getTransaction().commit();

        List<Notification> unreadNotifications = service.getUnreadNotifications();
        assertEquals(3, unreadNotifications.size());
    }

    @Test
    void testFindUnreadNotificationsByUserId_Success() throws CarnetException {

        Notification notification1 = new Notification();
        notification1.setMessage("New vaccine notification");
        notification1.setRead(false);
        notification1.setUtilisateur(utilisateur);
        notification1.setVaccin(vaccin);

        List<Notification> result = service.findUnreadNotificationsByUserId(1L);
        assertEquals(1, result.size());
        assertEquals("New vaccine notification", result.get(0).getMessage());
    }

    @Test
    void testGetUnreadMessageCount_Success() throws CarnetException {

        startTransaction();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");
        entityManager.merge(utilisateur);
        entityManager.getTransaction().commit();

        startTransaction();
        Vaccin vaccin = new Vaccin();
        vaccin.setVaccinId(1L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);
        vaccin.setNbrMonthsDose(4);
        vaccin.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);
        entityManager.merge(vaccin);
        entityManager.getTransaction().commit();

        Notification notification1 = new Notification();
        notification1.setMessage("Unread 1");
        notification1.setRead(false);
        notification1.setUtilisateur(utilisateur);
        notification1.setVaccin(vaccin);

        Notification notification2 = new Notification();
        notification2.setMessage("Unread 2");
        notification2.setRead(false);
        notification2.setUtilisateur(utilisateur);
        notification2.setVaccin(vaccin);

        Notification notification3 = new Notification();
        notification3.setMessage("Unread 3");
        notification3.setRead(false);
        notification3.setUtilisateur(utilisateur);
        notification3.setVaccin(vaccin);

        startTransaction();
        service.addNotification(notification1);
        entityManager.getTransaction().commit();

        startTransaction();
        service.addNotification(notification2);
        entityManager.getTransaction().commit();

        startTransaction();
        service.addNotification(notification3);
        entityManager.getTransaction().commit();

        int count = service.getUnreadMessageCount(utilisateur.getUtilisateurId());
        assertEquals(4, count);
    }

    @AfterEach
    void cleanup() {
        cleanDatabase(); // Clean the database after each test
    }
}
