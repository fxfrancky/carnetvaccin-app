package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class VaccinService  extends AbstractService<Vaccin> {

    @PersistenceContext(unitName="carnetvaccin-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VaccinService() {
        super(Vaccin.class);
    }


    public Vaccin findVaccinByTypeAndDose(String typeVaccin, Integer numDose) {
        TypedQuery<Vaccin> query = em.createNamedQuery("Vaccin.findVaccinByTypeAndDose", Vaccin.class);
        query.setParameter("typeVaccin", TypeVaccinEnum.valueOf(typeVaccin) );
        query.setParameter("numDose",numDose);
        return query.getSingleResult();
    }
}
