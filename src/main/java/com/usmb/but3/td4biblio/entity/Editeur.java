package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "editeur")
public class Editeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String adresse;

    @Column(name = "lien_site_web")
    private String lienSiteWeb;

    @Column(name = "lien_wikipedia")
    private String lienWikipedia;
}