package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.commons.AbstractService;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import com.carnetvaccin.app.backend.exceptions.CarnetException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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


    /**
     * Find Vaccin By Type and Dose
     * @param typeVaccin
     * @param numDose
     * @return
     * @throws CarnetException
     */
    public Optional<Vaccin> findVaccinByTypeAndDose(String typeVaccin, Integer numDose) throws CarnetException {

        try {
            TypedQuery<Vaccin> query = em.createNamedQuery("Vaccin.findVaccinByTypeAndDose", Vaccin.class);
            query.setParameter("typeVaccin", TypeVaccinEnum.valueOf(typeVaccin) );
            query.setParameter("numDose",numDose);
            return query.getResultStream().findFirst();
        } catch (Exception ex){
            throw new CarnetException("Error finding vaccin by dose and typeVaccin");
        }
    }
    /**
     * Create a vaccin
     * @param vaccin
     * @throws CarnetException
     */
    @Transactional
    public void createVaccin(Vaccin vaccin) throws CarnetException {
        try {
            create(vaccin);
            em.flush();
        } catch (Exception ex) {
            throw new CarnetException("An error occurs while creating a vaccin");
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
            throw new CarnetException("An error occurs while creating a vaccin");
        }
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }
}
