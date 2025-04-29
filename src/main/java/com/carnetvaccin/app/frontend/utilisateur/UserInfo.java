package com.carnetvaccin.app.frontend.utilisateur;

import com.carnetvaccin.app.api.roles.Role;
import com.carnetvaccin.app.api.utilisateur.UtilisateurDTO;
import lombok.Getter;
import lombok.Setter;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Singleton
public class UserInfo implements Serializable {

    @Getter
    private UtilisateurDTO user;

    @Getter
    @Setter
    private String currentToken;

    private List<String> roles = new ArrayList<String>();

    public UserInfo() {
    }

    public UserInfo(UtilisateurDTO user) {
        this.user = user;
    }

    public String getName(){
        if(user == null){
            return "anonymous user";
        } else {
            return user.getFirstName() + "  -  " + user.getLastName();
        }
    }

    public void setUser(UtilisateurDTO user) {
        this.user = user;
        roles.clear();
        if (user != null){
            roles.add(Role.User);
            if (user.isAdmin()){
                roles.add(Role.ADMIN);
            }
        }
    }

    public List<String> getRoles() {
        return roles;
    }
}
