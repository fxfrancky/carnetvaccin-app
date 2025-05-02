package com.carnetvaccin.app.api.vaccin;

import com.carnetvaccin.app.api.commons.AbstractMapper;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class VaccinMapper extends AbstractMapper<Vaccin, VaccinDTO> {

    public VaccinMapper() {
        super(Vaccin.class, VaccinDTO.class);
    }

    @Override
    public VaccinDTO toDto(Vaccin vaccinEntity) {
        if (vaccinEntity == null){
            return null;
        }

        VaccinDTO vaccinDTO = new VaccinDTO();
        if(vaccinDTO.getId() != null){
            vaccinDTO.setId(vaccinEntity.getVaccinId());
        }
        vaccinDTO.setTypeVaccin(vaccinEntity.getTypeVaccin());
        vaccinDTO.setVaccinDescription(vaccinEntity.getVaccinDescription());
        vaccinDTO.setNumDose(vaccinEntity.getNumDose());
        vaccinDTO.setNbrMonthsDose(vaccinEntity.getNbrMonthsDose());
        return vaccinDTO;
    }

    @Override
    public Vaccin toEntity(VaccinDTO vaccinDto) {
        if( vaccinDto == null){
            return null;
        }

       Vaccin vaccinEntity = new Vaccin();
       if(vaccinDto.getId()!=null){
           vaccinEntity.setVaccinId(vaccinDto.getId());
       }
       vaccinEntity.setTypeVaccin(vaccinDto.getTypeVaccin());
       vaccinEntity.setVaccinDescription(vaccinDto.getVaccinDescription());
       vaccinEntity.setNumDose(vaccinDto.getNumDose());
       vaccinEntity.setNbrMonthsDose(vaccinDto.getNbrMonthsDose());
        return vaccinEntity;
    }

    @Override
    public List<Vaccin> toEntityList(List<VaccinDTO> vaccinDTOSList) {
        List<Vaccin> vaccinEntityList;

        if (CollectionUtils.isEmpty(vaccinDTOSList)){
            vaccinEntityList = new ArrayList<>();
        } else {
            vaccinEntityList = vaccinDTOSList.stream()
                    .map(this::toEntity).collect(Collectors.toList());
        }

        return vaccinEntityList;
    }

    @Override
    public List<VaccinDTO> toDtoList(List<Vaccin> vaccinEntityList) {
        List<VaccinDTO> vaccinDTOSList;

        if(CollectionUtils.isEmpty(vaccinEntityList)){
            vaccinDTOSList = new ArrayList<>();
        } else {
            vaccinDTOSList = vaccinEntityList.stream()
                    .map(this::toDto).collect(Collectors.toList());
        }
        return vaccinDTOSList;
    }

}
