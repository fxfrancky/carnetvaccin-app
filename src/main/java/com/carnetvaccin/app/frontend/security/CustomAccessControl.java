package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.server.VaadinSession;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import java.io.Serializable;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class CustomAccessControl extends AccessControl implements Serializable {

    @Inject
    private UserInfo userInfo;

    private UtilisateurFacade utilisateurFacade;

    public String getCurrentToken(){
        String token = null;
        if (userInfo!= null && userInfo.getCurrentToken()!=null){
            token = userInfo.getCurrentToken();
        }
        return token;
    }

    @Override
    public boolean isUserSignedIn() {
        // Check if the current token is valid and associated with a user
        return getCurrentToken() != null && utilisateurFacade.validateToken(getCurrentToken()) != null;
//        return userInfo.getUser() != null;
    }

    @Override
    public boolean isUserInRole(String role) {
        if (isUserSignedIn()){
            for (String userRole: userInfo.getRoles()){
                if (role.equals(userRole)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getPrincipalName() {
        if (isUserSignedIn()){
            return userInfo.getName();
        }
        return null;
    }

    public void signIn(String username, String plainPassword) {
        try {
            // Authenticate the user and assign a bearer token

                if (userInfo == null){
//                    userInfo = new UserInfo();
                    throw new CarnetException("Oups Customer Info its actually Null ! ");
                }
//                this.userInfo.setCurrentToken(utilisateurFacade.loginUser(username, plainPassword));
//                this.userInfo.setUser(utilisateurFacade.getUserByUserName(username));
//            }

        } catch (Exception e) {
            throw new CarnetException("Invalid Credentials ! ");
        }
    }

    public void logout(){
        userInfo.setUser(null);
        userInfo.setCurrentToken(null);

        // Invalidate the underlying HTTP session
        VaadinSession currentVaadinSession = VaadinSession.getCurrent();
        if (currentVaadinSession != null && currentVaadinSession.getSession()!= null) {
            currentVaadinSession.getSession().invalidate();
        }
        currentVaadinSession.close();

//        Page.getCurrent().reload();
    }
}
