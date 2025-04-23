package com.carnetvaccin.app.api.vaccinutilisateur;

import com.carnetvaccin.app.api.commons.AbstractMapper;
import com.carnetvaccin.app.api.utilisateur.UtilisateurMapper;
import com.carnetvaccin.app.api.vaccin.VaccinMapper;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class VaccinUtilisateurMapper extends AbstractMapper<VaccinUtilisateur, VaccinUtilisateurDTO> {

    @Inject
    private UtilisateurMapper utilisateurMapper;

    @Inject
    private VaccinMapper vaccinMapper;

    public VaccinUtilisateurMapper() {
        super(VaccinUtilisateur.class, VaccinUtilisateurDTO.class);
    }

    @Override
    public VaccinUtilisateurDTO toDto(VaccinUtilisateur vUEntity) {
        VaccinUtilisateurDTO vUDTO = new VaccinUtilisateurDTO();
        vUDTO.setId(vUEntity.getVaccinUtilisateurId());
        vUDTO.setVaccinDTO(vaccinMapper.toDto(vUEntity.getVaccin()));
        vUDTO.setUtilisateurDTO(utilisateurMapper.toDto(vUEntity.getUtilisateur()));
        vUDTO.setDateVaccination(vUEntity.getDateVaccination());
        vUDTO.setLieuVacctination(vUEntity.getLieuVacctination());
        vUDTO.setCommentairesVaccin(vUEntity.getCommentairesVaccin());
        return vUDTO;
    }

    @Override
    public VaccinUtilisateur toEntity(VaccinUtilisateurDTO vUDTO) {
        VaccinUtilisateur vUEntity = new VaccinUtilisateur();
        vUEntity.setVaccinUtilisateurId(vUDTO.getId());
        vUEntity.setVaccin(vaccinMapper.toEntity(vUDTO.getVaccinDTO()));
        vUEntity.setUtilisateur(utilisateurMapper.toEntity(vUDTO.getUtilisateurDTO()));
        vUEntity.setDateVaccination(vUDTO.getDateVaccination());
        vUEntity.setLieuVacctination(vUDTO.getLieuVacctination());
        vUEntity.setCommentairesVaccin(vUDTO.getCommentairesVaccin());
        return vUEntity;
    }

    @Override
    public List<VaccinUtilisateur> toEntityList(List<VaccinUtilisateurDTO> vaccinUtilisateurDTOS) {

        List<VaccinUtilisateur> vaccinUtilisateurList;
        if (CollectionUtils.isEmpty(vaccinUtilisateurDTOS)){
            vaccinUtilisateurList = new ArrayList<>();
        } else {
            vaccinUtilisateurList = vaccinUtilisateurDTOS.stream()
                    .map(this::toEntity).collect(Collectors.toList());
        }
        return vaccinUtilisateurList;
    }

    @Override
    public List<VaccinUtilisateurDTO> toDtoList(List<VaccinUtilisateur> vaccinUtilisateurs) {

        List<VaccinUtilisateurDTO> vaccinUtilisateurDTOList;
        if (CollectionUtils.isEmpty(vaccinUtilisateurs)){
            vaccinUtilisateurDTOList = new ArrayList<>();
        }else {
            vaccinUtilisateurDTOList = vaccinUtilisateurs.stream()
                    .map(this::toDto).collect(Collectors.toList());
        }


        return vaccinUtilisateurDTOList;
    }


}
