package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur,UtilisateurDTO, UtilisateurService,UtilisateurMapper> {

    @Inject
    private UtilisateurMapper mapper;

    @Inject
    private UtilisateurService service;

    @Inject
    private UserInfo userInfo;

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

    @Override
    protected Logger getLogger() {
        return Logger.getLogger(this.getClass().getSimpleName());
    }

    public UtilisateurDTO getUserByEmail(String email){
            return mapper.toDto(getService().getUserByEmail(email));
    }

    public UtilisateurDTO getUserByUserNameAndPassword(String userName, String password) throws CarnetException {
            return  mapper.toDto(getService().getUserByUserNameAndPassword(userName,password));
    }

    public UtilisateurDTO getUserByUserName(String userName){
            return mapper.toDto(getService().getUserByUserName(userName));

    }

    public void deleteUserAccount(UtilisateurDTO utilisateurDTO) throws CarnetException {
         getService().deleteUserAccount(mapper.toEntity(utilisateurDTO));
    }

    public UtilisateurDTO validateToken(String token){
        return mapper.toDto(getService().validateToken(token));
    }

    //    Login a user
public UtilisateurDTO loginUser(String username, String plainPassword) {
//        String token = getService().loginUser(username,plainPassword);
        UtilisateurDTO utilisateurDTO = mapper.toDto(getService().loginUser(username,plainPassword));
        if (utilisateurDTO != null){
            UserInfo userInfo1 = new UserInfo();
            userInfo.setUser(utilisateurDTO);
            userInfo.setCurrentToken(utilisateurDTO.getToken());
        }
        return utilisateurDTO;
}
//    Register a new User
    public String registerUtilisateur(UtilisateurDTO utilisateurDTO){
        validateFields(utilisateurDTO);
        return getService().registerUser(mapper.toEntity(utilisateurDTO));
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
//    public Utilisateur getUserByUserNameAndPassword(String userName, String password){
//    public UtilisateurDTO getUserByUserNameAndPassword(String userName, String password)
//    public UtilisateurDTO getUserById(Long IdUtilisateur)
//    public void saveUser(Utilisateur user)
//    public List<UtilisateurDTO> getAllUsers();
}
