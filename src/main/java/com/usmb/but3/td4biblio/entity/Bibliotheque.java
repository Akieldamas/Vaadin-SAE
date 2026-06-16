package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "bibliotheque")
public class Bibliotheque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String adresse;

    @Column(name = "horaire_ouverture")
    private LocalTime horaireOuverture;

    @Column(name = "horaire_fermeture")
    private LocalTime horaireFermeture;
}