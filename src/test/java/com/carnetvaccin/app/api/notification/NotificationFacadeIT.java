package com.carnetvaccin.app.api.notification;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.vaccin.VaccinDTO;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.notification.NotificationService;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationFacadeIT {

    @InjectMocks
    private NotificationFacade notificationFacade;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper mapper;

    private Notification notification = new Notification();

    private NotificationDTO notificationDTO = new NotificationDTO();

    private Utilisateur utilisateur = new Utilisateur();
    private Vaccin vaccin = new Vaccin();

    private UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
    private VaccinDTO vaccinDTO = new VaccinDTO();

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this); // Initialize Mockito mocks

        utilisateur.setUtilisateurId(1L); // Example ID
        utilisateur.setFirstName("Test User");
        utilisateur.setUserName("Franck");
        utilisateur.setEmail("fxfre@yahoo.com");

        vaccin.setVaccinId(1L); // Example ID
        vaccin.setVaccinDescription("COVID-19 Vaccine");
        vaccin.setNumDose(2);
        vaccin.setNbrMonthsDose(4);
        vaccin.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);

        notification.setNotificationId(1L);
        notification.setMessage("New vaccine notification");
        notification.setRead(false);
        notification.setUtilisateur(utilisateur);
        notification.setVaccin(vaccin);

        utilisateurDTO.setId(1L); // Example ID
        utilisateurDTO.setFirstName("Test User");
        utilisateurDTO.setUserName("Franck");
        utilisateurDTO.setEmail("fxfre@yahoo.com");

        vaccinDTO.setId(1L); // Example ID
        vaccinDTO.setVaccinDescription("COVID-19 Vaccine");
        vaccinDTO.setNumDose(2);
        vaccinDTO.setNbrMonthsDose(4);
        vaccinDTO.setTypeVaccin(TypeVaccinEnum.MENINGOCOQUE_ACWY);

        notificationDTO.setId(1L);
        notificationDTO.setMessage("New vaccine notification");
        notificationDTO.setRead(false);
        notificationDTO.setUtilisateurDTO(utilisateurDTO);
        notificationDTO.setVaccinDTO(vaccinDTO);

        when(mapper.toDto(notification)).thenReturn(notificationDTO);
        when(mapper.toEntity(notificationDTO)).thenReturn(notification);

        when(mapper.toDtoList(Arrays.asList(notification))).thenReturn(Arrays.asList(notificationDTO));
        when(mapper.toEntityList(Arrays.asList(notificationDTO))).thenReturn(Arrays.asList(notification));
    }


    @Test
    void testAddNotification_Success() throws CarnetException {
        doNothing().when(notificationService).addNotification(any(Notification.class));
        when(mapper.toEntity(notificationDTO)).thenReturn(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Test notification");

        notificationFacade.addNotification(notificationDTO);

        when(notificationService.getUnreadNotifications()).thenReturn(Arrays.asList(notification));


        List<NotificationDTO> notifications = notificationFacade.getUnreadNotifications();
        assertFalse(notifications.isEmpty());
        assertEquals("New vaccine notification", notifications.get(0).getMessage());
//        verify(notificationService, times(1)).addNotification(any(Notification.class));

    }

    @Test
    void testMarkNotificationAsRead_Success() throws CarnetException {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("Mark as read test");
        notificationDTO.setRead(true);

        notification.setRead(true);
        doNothing().when(notificationService).addNotification(any(Notification.class));
        when(mapper.toEntity(notificationDTO)).thenReturn(notification);
        when(mapper.toDto(notification)).thenReturn(notificationDTO);


        notificationFacade.addNotification(notificationDTO);

        when(notificationService.getUnreadNotifications()).thenReturn(Arrays.asList(notification));
        List<NotificationDTO> notifications = notificationFacade.getUnreadNotifications();
        assertFalse(notifications.isEmpty());

        Long notificationId = notifications.get(0).getId();
        notificationFacade.markAsRead(notificationId);

        List<NotificationDTO> updatedNotifications = notificationFacade.getUnreadNotifications();
//        assertTrue(updatedNotifications.isEmpty());
    }

    @Test
    void testFindUnreadNotificationsByUserId_Success() throws CarnetException {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("New vaccine notification");
        notificationDTO.setId(1L);
        doNothing().when(notificationService).addNotification(any(Notification.class));
        when(mapper.toEntity(notificationDTO)).thenReturn(notification);
        when(mapper.toDto(notification)).thenReturn(notificationDTO);

        notificationFacade.addNotification(notificationDTO);

        when(notificationService.findUnreadNotificationsByUserId(anyLong())).thenReturn(Arrays.asList(notification));

        List<NotificationDTO> userNotifications = notificationFacade.findUnreadNotificationsByUserId(1L);
        assertEquals(1, userNotifications.size());
        assertEquals("New vaccine notification", userNotifications.get(0).getMessage());
    }

    @Test
    void testGetUnreadMessageCount_Success() throws CarnetException {
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setMessage("Unread message 1");
        notificationDTO1.setId(2L);

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setMessage("Unread message 2");
        notificationDTO2.setId(2L);

        doNothing().when(notificationService).addNotification(any(Notification.class));
        when(mapper.toEntity(notificationDTO)).thenReturn(notification);
        when(mapper.toDto(notification)).thenReturn(notificationDTO);
        when(notificationService.getUnreadMessageCount(anyLong())).thenReturn(2);


        notificationFacade.addNotification(notificationDTO1);
        notificationFacade.addNotification(notificationDTO2);

        int unreadCount = notificationFacade.getUnreadMessageCount(2L);
        assertEquals(2, unreadCount);
    }
}
