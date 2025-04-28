package com.carnetvaccin.app.frontend.security;

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

    @Override
    public boolean isUserSignedIn() {
        return userInfo.getUser() != null;
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

    public void logout(){
        userInfo.setUser(null);

        // Invalidate the underlying HTTP session
        VaadinSession currentVaadinSession = VaadinSession.getCurrent();
        if (currentVaadinSession != null && currentVaadinSession.getSession()!= null) {
            currentVaadinSession.getSession().invalidate();
        }
        currentVaadinSession.close();

//        Page.getCurrent().reload();
    }
}
