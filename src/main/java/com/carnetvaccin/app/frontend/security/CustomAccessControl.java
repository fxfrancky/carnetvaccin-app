package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.frontend.utilisateur.UserInfo;
import com.vaadin.cdi.VaadinSessionScoped;
import com.vaadin.cdi.access.AccessControl;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptor;
import java.io.Serializable;

//@Alternative
//@ApplicationScoped
@Alternative
//@Default
//@Named
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
}
