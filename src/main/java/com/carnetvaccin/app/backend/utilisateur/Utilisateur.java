package com.carnetvaccin.app.backend.utilisateur;

import com.carnetvaccin.app.backend.commons.BaseEntity;
import com.carnetvaccin.app.backend.notification.Notification;
import com.carnetvaccin.app.backend.vaccinutilisateur.VaccinUtilisateur;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NamedQueries({
   @NamedQuery(name = "Utilisateur.getUserByEmail", query = "from Utilisateur u where u.email = :email"),
   @NamedQuery(name = "Utilisateur.getUserByUserName", query = "from Utilisateur u where u.userName = :userName"),
   @NamedQuery(name = "Utilisateur.getUserByToken", query = "from Utilisateur u where u.token = :token"),
   @NamedQuery(name = "Utilisateur.getUserByUserNameAndPassword", query = "from Utilisateur u where u.userName = :userName and u.encryptedPassword = :encryptedPassword")

})
public class Utilisateur extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @Column(name = "first_name")
//    @NotNull
    private String firstName;

    @Column(name = "last_name")
//    @NotNull
    private String lastName;

    @Column(name = "email", unique = true)
//    @NotNull
    @Email
    private String email;

    @Column(name = "date_naiss")
//    @NotNull
    private String dateNaissance;

    @Column(name = "user_name" , unique = true)
//    @NotNull
    private String userName;

//    @NotNull
    @Column(name = "password")
    private String encryptedPassword;

    @Column(name = "is_admin")
    private boolean isAdmin = false;

    @Column(name = "token")
    private String token; // Added for Bearer Token

    // No need for @Transient, roles should be persisted.  Simplest approach is a comma-separated string.
    @Column(name = "roles")
    private String roles;  // Store roles as a comma-separated string

    @OneToMany(mappedBy = "utilisateur")
    private List<VaccinUtilisateur> vaccinUtilisateurList;

    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notificationList;

    public Utilisateur() {
        this.roles = ""; // Initialize to empty string

    }

    public Utilisateur(List<Notification> notificationList, Long utilisateurId, String firstName, String lastName, String email, String dateNaissance, List<VaccinUtilisateur> vaccinUtilisateurList, String userName, String password, Boolean isAdmin) {
        this.notificationList = notificationList;
        this.utilisateurId = utilisateurId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.vaccinUtilisateurList = vaccinUtilisateurList;
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.isAdmin = isAdmin;

    }

    public Utilisateur(String firstName, String lastName, String email, String dateNaissance, List<VaccinUtilisateur> vaccinUtilisateurList, List<Notification> notificationList, String userName, String encryptedPassword, Boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.vaccinUtilisateurList = vaccinUtilisateurList;
        this.notificationList = notificationList;
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.isAdmin = isAdmin;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(utilisateurId, that.utilisateurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilisateurId, email);
    }

    public List<String> getRoles() {
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(roles.split(","));
    }

    public void setRoles(List<String> roles) {
        if (roles == null || roles.isEmpty()) {
            this.roles = "";
        } else {
            this.roles = String.join(",", roles);
        }
    }

    public void addRole(String role) {
        if (this.roles == null || this.roles.isEmpty()) {
            this.roles = role;
        }
        else if (!this.roles.contains(role)) {
            this.roles += "," + role;
        }
    }
}
