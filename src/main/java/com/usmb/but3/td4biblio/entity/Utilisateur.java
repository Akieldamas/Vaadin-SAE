package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Une classe entité qui représente une table de la base de données
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "utilisateur")

public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idRole;
    private String nom;
    private String prenom;
    private String email;
    private String numeroCarte;
    private LocalDate dateFinAbonnement;
}



