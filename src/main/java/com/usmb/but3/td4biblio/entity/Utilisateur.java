package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 *  * Une classe entité qui représente une table de la base de données
 *  
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "utilisateur")

public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    private String prenom;
    private String nom;
    private String adresse;
    private String email;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_fin_abonnement")
    private LocalDate dateFinAbonnement;

    @Column(name = "numero_carte", length = 10)
    private String numeroCarte;

    @ManyToOne
    @JoinColumn(name = "role_utilisateur_id", nullable = false)
    private RoleUtilisateur roleUtilisateur;

    public Integer getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateFinAbonnement() {
        return dateFinAbonnement;
    }

    public void setDateFinAbonnement(LocalDate dateFinAbonnement) {
        this.dateFinAbonnement = dateFinAbonnement;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}