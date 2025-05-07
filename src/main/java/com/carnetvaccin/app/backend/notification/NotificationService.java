package com.carnetvaccin.app.backend.notification;

import com.carnetvaccin.app.backend.commons.AbstractService;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class NotificationService extends AbstractService<Notification> {

    @PersistenceContext(unitName="carnetvaccin-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationService() {
        super(Notification.class);
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }



}
