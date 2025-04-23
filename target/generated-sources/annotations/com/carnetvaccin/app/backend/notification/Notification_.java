package com.carnetvaccin.app.backend.notification;

import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-04-21T18:38:54")
@StaticMetamodel(Notification.class)
public class Notification_ { 

    public static volatile SingularAttribute<Notification, Vaccin> vaccin;
    public static volatile SingularAttribute<Notification, Utilisateur> utilisateur;
    public static volatile SingularAttribute<Notification, Long> notificationId;
    public static volatile SingularAttribute<Notification, String> message;
    public static volatile SingularAttribute<Notification, LocalDate> dateNotification;

}