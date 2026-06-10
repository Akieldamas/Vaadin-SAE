package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Une classe entité qui représente une table de la base de données
 */


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "editeur")

public class Editeur {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 private String nom_editeur;
 private String adresse;
 private String lien_site_web;
 private String lien_wikipedia;

 
}
