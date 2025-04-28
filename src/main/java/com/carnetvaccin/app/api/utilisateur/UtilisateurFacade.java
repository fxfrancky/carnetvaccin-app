package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Logger;

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
//    public Utilisateur getUserByUserNameAndPassword(String userName, String password){
//    public UtilisateurDTO getUserByUserNameAndPassword(String userName, String password)
//    public UtilisateurDTO getUserById(Long IdUtilisateur)
//    public void saveUser(Utilisateur user)
//    public List<UtilisateurDTO> getAllUsers();
}
