package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import com.carnetvaccin.app.api.utilisateur.UtilisateurFacade;
import com.carnetvaccin.app.backend.exceptions.CarnetException;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;


@Slf4j
@ApplicationScoped
@Alternative
public class CustomAccessControl extends AccessControl implements Serializable {



    private static final String SESSION_USER_KEY = "currentUser";

    @Inject
    private UtilisateurFacade utilisateurFacade;

    public CustomAccessControl() {
    }

    @Override
    public boolean isUserSignedIn() {
        if (VaadinSession.getCurrent() == null) {
            return false;
        }
        return VaadinSession.getCurrent().getAttribute(SESSION_USER_KEY) != null;
    }



    @Override
    public boolean isUserInRole(String role) {
        if (VaadinSession.getCurrent() == null) {
            return false;
        }
        if (!isUserSignedIn()) {
            return false;
        }
        UtilisateurDTO user = getCurrentUser();
        if (user == null) {
            return false;
        }
        return user.getRoles().contains(role);
    }

    @Override
    public boolean isUserInSomeRole(String... roles) {
        if (VaadinSession.getCurrent() == null) {
            return false;
        }
        for (String role : roles) {
            if (isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String getPrincipalName() {
        if (isUserSignedIn()) {
            return getCurrentUser().getFirstName() + "  -  " + getCurrentUser().getLastName();
        } else {
            return "anonymous user";
        }
    }

    /**
     * Sign In User
     * @param username
     * @param password
     * @return
     */
    public boolean signIn(String username, String password) {

        try {
            // Authenticate the user and assign a bearer token
            UtilisateurDTO foundUser = utilisateurFacade.loginUser(username, password);
            if (foundUser != null) {
                if (VaadinSession.getCurrent() != null) {
                    VaadinSession.getCurrent().setAttribute(SESSION_USER_KEY,foundUser);
                    HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
                    request.setAttribute(username,password);
                    log.info("User " + username + " logged in successfully via HttpServletRequest.login()");

                    VaadinSession.getCurrent().setAttribute(SESSION_USER_KEY, foundUser);

                    return true;

                }
            } else {
               log.warn("---------------------------  User Not even found but in Vaadin : is null  ---------------------------------------------------------");
            }
            return false;
        }catch (CarnetException e) {
            log.warn("Invalid Credentials !  **************" + e.getMessage());
            throw new CarnetException("Invalid Credentials ! ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UtilisateurDTO getCurrentUser() {

        if (VaadinSession.getCurrent() == null) {
            return null;
        }
        return (UtilisateurDTO) VaadinSession.getCurrent().getAttribute(SESSION_USER_KEY);
    }

    public UtilisateurDTO getLoggedInUser() {
        if (VaadinSession.getCurrent() == null) {
            return null;
        }
        return (UtilisateurDTO) VaadinSession.getCurrent().getAttribute(SESSION_USER_KEY);
    }

    @Transactional
    public boolean signInWithToken(String token) {
        UtilisateurDTO userDTO = utilisateurFacade.loginWithToken(token);
        if (userDTO != null) {
            if (VaadinSession.getCurrent() != null) {
                VaadinSession.getCurrent().setAttribute(SESSION_USER_KEY, userDTO);
                return true;
            }
        }
        return false;
    }

    public void signOut() {
        if (VaadinSession.getCurrent() != null) {
            VaadinSession.getCurrent().setAttribute(SESSION_USER_KEY, null);
            HttpServletRequest request = (HttpServletRequest) VaadinService.getCurrentRequest();
            if (request != null) {
                try {
                    request.logout();
                    log.info("User logged out successfully via HttpServletRequest.logout()");
                } catch (Exception e) {
                    log.warn("ServletException during request.logout(): " + e.getMessage());
                    throw new CarnetException("ServletException during request.logout():");
                }
            }
        }
    }

    @Alternative
    public AccessControl accessControl() {
        return this;
    }
}
