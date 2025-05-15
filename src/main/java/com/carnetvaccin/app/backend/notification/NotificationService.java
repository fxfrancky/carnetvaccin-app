package com.carnetvaccin.app.backend.notification;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Stateless
@LocalBean
public class NotificationService extends AbstractService<Notification> {

    @PersistenceContext(unitName="carnetvaccin-PU")
    private EntityManager em;

    public NotificationService() {
        super(Notification.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    private final List<Consumer<Notification>> subscribers = new ArrayList<>();

    public void subscribeToUpdates(Consumer<Notification> listener) {
        subscribers.add(listener);
    }

    /**
     * Notify The Ui of any change
     * @param notification
     */
    public void notifyUI(Notification notification) {
        subscribers.forEach(subscriber -> subscriber.accept(notification));
    }


    /**
     * Add a new notification
     * @param notification
     */
    @Transactional
    public void addNotification(Notification notification) {
        try {
            create(notification);
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("Error adding notification: " + e.getMessage());
        }
    }

    /**
     * Mark Notification as Read
     * @param notificationId
     * @throws CarnetException
     */
    @Transactional
    public void markAsRead(Long notificationId) throws CarnetException {

        try {
            Query query = em.createNamedQuery("Notification.markAsRead");
            query.setParameter("notificationId", notificationId);
            query.executeUpdate();
            em.flush();
        } catch (Exception e) {
            throw new CarnetException("Error updating a notification " + e.getMessage());
        }
    }


    /**
     * find UnreadNotifications
     * @return
     * @throws CarnetException
     */
    public List<Notification> getUnreadNotifications()  throws CarnetException {
        TypedQuery<Notification> query = em.createNamedQuery("Notification.findUnreadNotifications", Notification.class);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw new CarnetException("An error occurs");
        }
    }


    /**
     * find UnreadNotifications By UserId
     * @param utilisateurId
     * @return
     * @throws CarnetException
     */
    public List<Notification> findUnreadNotificationsByUserId(Long utilisateurId) throws CarnetException {
        TypedQuery<Notification> query = em.createNamedQuery("Notification.findUnreadNotificationsByUserId", Notification.class);
        query.setParameter("utilisateurId", utilisateurId);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw new CarnetException("An error occurs");
        }
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
