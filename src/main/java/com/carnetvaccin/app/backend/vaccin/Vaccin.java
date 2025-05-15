package com.carnetvaccin.app.backend.vaccin;

import com.carnetvaccin.app.backend.commons.BaseEntity;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "vaccin")
@Getter
@Setter
@NamedQuery(name = "Vaccin.findVaccinByTypeAndDose", query = "from Vaccin v where v.typeVaccin = :typeVaccin and v.numDose = :numDose")
public class Vaccin extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccin_id")
    private Long vaccinId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaccin_type")
    private TypeVaccinEnum typeVaccin;

    @Column(name = "vaccin_description"  , nullable = false)
    private String vaccinDescription;

    @Column(name = "num_dose"  , nullable = false)
    private Integer numDose;

    @Column(name = "nbre_months_dose"  , nullable = false)
    private Integer nbrMonthsDose;

    public Vaccin() {
    }

    public Vaccin(Long vaccinId, TypeVaccinEnum typeVaccin, String vaccinDescription, Integer numDose, Integer nbrMonthsDose) {
        this.vaccinId = vaccinId;
        this.typeVaccin = typeVaccin;
        this.vaccinDescription = vaccinDescription;
        this.numDose = numDose;
        this.nbrMonthsDose = nbrMonthsDose;
    }


    public Vaccin(TypeVaccinEnum typeVaccin, String vaccinDescription, Integer numDose, Integer nbrMonthsDose) {
        this.typeVaccin = typeVaccin;
        this.vaccinDescription = vaccinDescription;
        this.numDose = numDose;
        this.nbrMonthsDose = nbrMonthsDose;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vaccin vaccin = (Vaccin) o;
        return Objects.equals(vaccinId, vaccin.vaccinId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vaccinId);
    }
}
