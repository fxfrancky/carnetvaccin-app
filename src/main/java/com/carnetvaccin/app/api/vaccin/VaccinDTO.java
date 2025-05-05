package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.api.commons.BaseDto;
import com.carnetvaccin.app.backend.enums.TypeVaccinEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class VaccinDTO  extends BaseDto implements Serializable {

    private TypeVaccinEnum typeVaccin;

    private String vaccinDescription;

    private Integer numDose;

    private Integer nbrMonthsDose;


    public VaccinDTO(TypeVaccinEnum typeVaccin, String vaccinDescription, Integer numDose, Integer nbrMonthsDose) {
        this.typeVaccin = typeVaccin;
        this.vaccinDescription = vaccinDescription;
        this.numDose = numDose;
        this.nbrMonthsDose = nbrMonthsDose;
    }

    public VaccinDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VaccinDTO vaccinDTO = (VaccinDTO) o;
        return typeVaccin == vaccinDTO.typeVaccin && Objects.equals(numDose, vaccinDTO.numDose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typeVaccin, numDose);
    }
}
