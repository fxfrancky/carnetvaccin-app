package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-04-21T18:38:54")
@StaticMetamodel(Vaccin.class)
public class Vaccin_ { 

    public static volatile SingularAttribute<Vaccin, Long> vaccinId;
    public static volatile SingularAttribute<Vaccin, TypeVaccinEnum> typeVaccin;
    public static volatile SingularAttribute<Vaccin, Integer> nbrMonthsDose;
    public static volatile SingularAttribute<Vaccin, Integer> numDose;
    public static volatile SingularAttribute<Vaccin, String> vaccinDescription;

}