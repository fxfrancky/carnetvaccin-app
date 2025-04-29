package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.AbstractMapper;
import com.carnetvaccin.app.api.notification.NotificationMapper;
import com.carnetvaccin.app.api.vaccinutilisateur.VaccinUtilisateurMapper;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class UtilisateurMapper extends AbstractMapper<Utilisateur, UtilisateurDTO> {

    @Inject
    private VaccinUtilisateurMapper vaccinUtilisateurMapper;

    @Inject
    private NotificationMapper notificationMapper;

    public UtilisateurMapper() {
        super(Utilisateur.class, UtilisateurDTO.class);
    }

    @Override
    public UtilisateurDTO toDto(Utilisateur uEntity) {
        UtilisateurDTO uDTO = new UtilisateurDTO();
        uDTO.setId(uEntity.getUtilisateurId());
        uDTO.setFirstName(uEntity.getFirstName());
        uDTO.setLastName(uEntity.getLastName());
        uDTO.setEmail(uEntity.getEmail());
        uDTO.setDateNaissance(uEntity.getDateNaissance());
        uDTO.setPlainPassword(null); // we dont return a password
        uDTO.setUserName(uEntity.getUserName());
//        uDTO.setVaccinUtilisateurDTOList(vaccinUtilisateurMapper.toDtoList(uEntity.getVaccinUtilisateurList()));
//        uDTO.setNotificationDTOList(notificationMapper.toDtoList(uEntity.getNotificationList()));
        uDTO.setAdmin(uEntity.isAdmin());
        return uDTO;
    }

    @Override
    public Utilisateur toEntity(UtilisateurDTO uDTO) {
        Utilisateur uEntity = new Utilisateur();
        uEntity.setUtilisateurId(uDTO.getId());
        uEntity.setFirstName(uDTO.getFirstName());
        uEntity.setLastName(uDTO.getLastName());
        uEntity.setEmail(uDTO.getEmail());
        uEntity.setDateNaissance(uDTO.getDateNaissance());
        uEntity.setEncryptedPassword(encryptPassword(uDTO.getPlainPassword()));
        uEntity.setUserName(uDTO.getUserName());
        uEntity.setVaccinUtilisateurList(vaccinUtilisateurMapper.toEntityList(uDTO.getVaccinUtilisateurDTOList()));
        uEntity.setNotificationList(notificationMapper.toEntityList(uDTO.getNotificationDTOList()));
        uEntity.setAdmin(uDTO.isAdmin());
        return uEntity;
    }


    @Override
    public List<Utilisateur> toEntityList(List<UtilisateurDTO> utilisateurDTOList) {

        List<Utilisateur> utilisateurList;
        if (CollectionUtils.isEmpty(utilisateurDTOList)){
            utilisateurList = new ArrayList<>();
        } else {
            utilisateurList = utilisateurDTOList.stream()
                    .map(this::toEntity).collect(Collectors.toList());
        }
        return utilisateurList;
    }

    @Override
    public List<UtilisateurDTO> toDtoList(List<Utilisateur> utilisateurEntityList) {

        List<UtilisateurDTO> utilisateurDTOList;
        if (CollectionUtils.isEmpty(utilisateurEntityList)){
            utilisateurDTOList = new ArrayList<>();
        } else {
            utilisateurDTOList = utilisateurEntityList.stream()
                    .map(this::toDto).collect(Collectors.toList());
        }
        return utilisateurDTOList;
    }


    public static String encryptPassword(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }

}
