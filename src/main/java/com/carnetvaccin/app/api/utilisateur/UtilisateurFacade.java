package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur,UtilisateurDTO, UtilisateurService,UtilisateurMapper> {

    @Inject
    private UtilisateurMapper mapper;

    @Inject
    private UtilisateurService service;

    public UtilisateurFacade() {
        super(UtilisateurDTO.class, Utilisateur.class);
    }

    @Override
    protected UtilisateurService getService() {
        return service;
    }

    @Override
    protected UtilisateurMapper getMapper() {
        return mapper;
    }

    public UtilisateurDTO getUserByEmail(String email){
        Optional<Utilisateur> utilisateur = getService().getUserByEmail(email);
        if (utilisateur.isPresent()){
            Utilisateur foundUser = utilisateur.get();
            return mapper.toDto(foundUser);
        } else {
            return null;
        }
    }

    public UtilisateurDTO getUserByUserName(String userName){
            Optional<Utilisateur> utilisateur = getService().getUserByUserName(userName);
            if (utilisateur.isPresent()){
                Utilisateur foundUser = utilisateur.get();
                return mapper.toDto(foundUser);
            } else {
                return null;
            }
    }

    public void deleteUserAccount(UtilisateurDTO utilisateurDTO) throws CarnetException {
         getService().deleteUserAccount(mapper.toEntity(utilisateurDTO));
    }

    //    Login a user
public UtilisateurDTO loginUser(String username, String plainPassword) {
    return mapper.toDto(getService().loginUser(username,plainPassword));
}
//    Register a new User
    @Transactional
    public UtilisateurDTO registerUtilisateur(UtilisateurDTO utilisateurDTO){
        validateFields(utilisateurDTO);
        Utilisateur utilisateur = mapper.toEntity(utilisateurDTO);
        utilisateur.setEncryptedPassword(hashPassword(utilisateurDTO.getPlainPassword()));
        utilisateur.setToken(UUID.randomUUID().toString());
        return mapper.toDto(getService().registerUser(utilisateur));
    }

    private void validateFields(UtilisateurDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().length() < 3) {
            throw new CarnetException("Invalid first name.");
        }
        if (dto.getLastName() == null || dto.getLastName().length() < 3) {
            throw new CarnetException("Invalid last name.");
        }
        if (dto.getDateNaissance() == null || dto.getDateNaissance().isEmpty()) {
            throw new CarnetException("Date of birth cannot be empty.");
        }
        if (dto.getUserName() == null || dto.getUserName().length() < 6) {
            throw new CarnetException("Invalid username. It must be at least 6 characters.");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new CarnetException("Invalid email address.");
        }
        if (dto.getPlainPassword() == null || dto.getPlainPassword().length() < 6 || !dto.getPlainPassword().matches(".*[!@#$%^&*].*")) {
            throw new CarnetException("Invalid password. It must be at least 6 characters long and contain a special character.");
        }
    }
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
//
//    public boolean checkPassword(String password, String storedHash) {
//        try {
//            return BCrypt.checkpw(password, storedHash);
//        } catch (Exception e) {
//            log.error("Error checking password: {}", e.getMessage());
//            e.printStackTrace();
//            throw new CarnetException("Error checking password: in UserFacade");
//        }
//    }

    @Transactional
    public UtilisateurDTO loginWithToken(String token) {
        Optional<Utilisateur> user = getService().findByToken(token);
        return user.map(utilisateur -> mapper.toDto(utilisateur)).orElse(null);
    }

//    @Transactional
//    public UtilisateurDTO getCurrentUser(String userName) {
//        Optional<Utilisateur> user = getService().getUserByUserName(userName);
//        return user.map(utilisateur -> mapper.toDto(utilisateur)).orElse(null);
//    }
}
