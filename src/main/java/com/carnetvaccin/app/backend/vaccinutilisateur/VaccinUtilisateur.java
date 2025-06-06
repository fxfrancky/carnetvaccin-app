package com.carnetvaccin.app.backend.vaccinutilisateur;

import com.carnetvaccin.app.backend.commons.BaseEntity;
import com.carnetvaccin.app.backend.utilisateur.Utilisateur;
import com.carnetvaccin.app.backend.vaccin.Vaccin;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "vaccin_utilisateur")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "VaccinUtilisateur.findByUtilisaterIDAndVaccinId", query = "from VaccinUtilisateur v where v.vaccin.vaccinId = :vaccinId  and v.utilisateur.utilisateurId = :utilisateurId"),
        @NamedQuery(name = "VaccinUtilisateur.findAllVaccinUtilisateurByUserId", query = "from VaccinUtilisateur v where v.utilisateur.utilisateurId = :utilisateurId order by v.dateVaccination desc"),
        @NamedQuery(name = "VaccinUtilisateur.findrByTerms", query = "from VaccinUtilisateur v WHERE v.utilisateur.utilisateurId = :utilisateurId AND (lower(v.commentairesVaccin) LIKE :term OR  lower(v.lieuVacctination) LIKE :term" +
                            " OR  lower(v.vaccin.vaccinDescription) LIKE :term  OR  lower(v.vaccin.typeVaccin) LIKE :term )"),
        @NamedQuery(name = "VaccinUtilisateur.notificationSent", query = "UPDATE VaccinUtilisateur v SET v.notificationSent = true where v.vaccinUtilisateurId = :vaccinUtilisateurId "),

})
public class VaccinUtilisateur extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccin_utilisateur_id")
    private Long vaccinUtilisateurId;

    @ManyToOne
    @JoinColumn(name = "vaccin_id")
    private Vaccin vaccin;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Column(name = "date_vaccination")
    private LocalDate dateVaccination;

    @Column(name = "lieu_vaccination")
    private String lieuVacctination;

    @Column(name = "commentaires_vaccin")
    private String commentairesVaccin;

    @Column(name ="notification_sent" )
    private boolean notificationSent = false;

    public VaccinUtilisateur() {
    }

    public VaccinUtilisateur(Long vaccinUtilisateurId, Vaccin vaccin, Utilisateur utilisateur, LocalDate dateVaccination, String lieuVacctination, String commentairesVaccin, boolean notificationSent) {
        this.vaccinUtilisateurId = vaccinUtilisateurId;
        this.vaccin = vaccin;
        this.utilisateur = utilisateur;
        this.dateVaccination = dateVaccination;
        this.lieuVacctination = lieuVacctination;
        this.commentairesVaccin = commentairesVaccin;
        this.notificationSent = notificationSent;
    }

    public VaccinUtilisateur(boolean notificationSent, String commentairesVaccin, String lieuVacctination, LocalDate dateVaccination, Utilisateur utilisateur, Vaccin vaccin) {
        this.notificationSent = notificationSent;
        this.commentairesVaccin = commentairesVaccin;
        this.lieuVacctination = lieuVacctination;
        this.dateVaccination = dateVaccination;
        this.utilisateur = utilisateur;
        this.vaccin = vaccin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VaccinUtilisateur that = (VaccinUtilisateur) o;
        return Objects.equals(vaccinUtilisateurId, that.vaccinUtilisateurId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vaccinUtilisateurId);
    }

    public boolean isPersisted() {
        return vaccinUtilisateurId != null;
    }
}
