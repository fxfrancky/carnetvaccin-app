package com.carnetvaccin.app.api.utilisateur;

import com.carnetvaccin.app.api.commons.AbstractFacade;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.utilisateur.UtilisateurService;

import javax.ejb.Stateless;
import javax.inject.Inject;

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

    public UtilisateurDTO findByEmail(String email){
        return mapper.toDto(getService().findUserByEmail(email));
    }
}
