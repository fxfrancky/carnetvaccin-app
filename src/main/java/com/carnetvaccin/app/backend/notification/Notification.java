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
public class Notification extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @JoinColumn(name = "utilisateur_id")
    @ManyToOne
    private Utilisateur utilisateur;

    @JoinColumn(name = "vaccin_id")
    @ManyToOne
    private Vaccin vaccin;

    @Column(name = "date_notification")
    private LocalDate dateNotification;

    @Column(name = "message")
    private String message;

    public Notification() {
    }

    public Notification(String message, LocalDate dateNotification, Vaccin vaccin, Utilisateur utilisateur, Long notificationId) {
        this.message = message;
        this.dateNotification = dateNotification;
        this.vaccin = vaccin;
        this.utilisateur = utilisateur;
        this.notificationId = notificationId;
    }

    public Notification(Utilisateur utilisateur, Vaccin vaccin, LocalDate dateNotification, String message) {
        this.utilisateur = utilisateur;
        this.vaccin = vaccin;
        this.dateNotification = dateNotification;
        this.message = message;
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
