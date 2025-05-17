package com.carnetvaccin.app.backend.notification;

import com.carnetvaccin.app.backend.commons.BaseEntity;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Notification.findUnreadNotificationsByUserId", query = "FROM Notification n WHERE n.utilisateur.utilisateurId = :utilisateurId AND n.isRead = false ORDER BY n.dateNotification DESC"),
        @NamedQuery(name = "Notification.findUnreadNotifications", query = "FROM Notification n WHERE n.isRead = false ORDER BY n.dateNotification DESC"),
        @NamedQuery(name = "Notification.markAsRead", query = "UPDATE Notification n SET n.isRead = true where n.notificationId = :notificationId "),
})
public class Notification extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long notificationId;

    @JoinColumn(name = "utilisateur_id", nullable = false)
    @ManyToOne
    private Utilisateur utilisateur;

    @JoinColumn(name = "vaccin_id", nullable = false)
    @ManyToOne
    private Vaccin vaccin;

    @Column(name = "date_notification", nullable = false)
    private LocalDate dateNotification;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_read")
    private boolean isRead = false;


    public Notification() {
    }

    public Notification(Long notificationId, Utilisateur utilisateur, Vaccin vaccin, LocalDate dateNotification, String message, boolean isRead) {
        this.notificationId = notificationId;
        this.utilisateur = utilisateur;
        this.vaccin = vaccin;
        this.dateNotification = dateNotification;
        this.message = message;
        this.isRead = isRead;
    }

    public Notification(Utilisateur utilisateur, Vaccin vaccin, LocalDate dateNotification, String message, boolean isRead) {
        this.utilisateur = utilisateur;
        this.vaccin = vaccin;
        this.dateNotification = dateNotification;
        this.message = message;
        this.isRead = isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(notificationId);
    }
}
