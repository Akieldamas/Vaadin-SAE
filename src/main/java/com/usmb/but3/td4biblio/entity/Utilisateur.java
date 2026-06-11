package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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
}