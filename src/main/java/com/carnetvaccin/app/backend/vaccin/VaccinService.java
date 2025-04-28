package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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


    public Vaccin findVaccinByTypeAndDose(String typeVaccin, Integer numDose) throws CarnetException {
        TypedQuery<Vaccin> query = em.createNamedQuery("Vaccin.findVaccinByTypeAndDose", Vaccin.class);
        query.setParameter("typeVaccin", TypeVaccinEnum.valueOf(typeVaccin) );
        query.setParameter("numDose",numDose);
        try {
            return query.getSingleResult();
//            return query.getResultStream().findFirst().orElse(null);
    } catch (Exception ex) {
        throw new CarnetException("An error occurss",ex);
    }

    }

    /**
     * Create a vaccin
     * @param vaccin
     * @throws CarnetException
     */
    public void createVaccin(Vaccin vaccin) throws CarnetException {
        try {
            create(vaccin);
        } catch (Exception ex) {
            throw new CarnetException("An error occurs while creating a vaccin",ex);
        }

    }

    /**
     * Find All vaccin
     * @return
     * @throws CarnetException
     */
    public List<Vaccin> findAllVaccin() throws CarnetException {

        try{
            return super.findAll();
        } catch (Exception ex) {
            throw new CarnetException("An error occurs while creating a vaccin",ex);
        }
    }
}
