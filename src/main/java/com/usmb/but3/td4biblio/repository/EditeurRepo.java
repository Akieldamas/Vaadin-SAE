package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Editeur;

public interface EditeurRepo extends JpaRepository<Editeur, Integer> {
    List<Editeur> findByNomEditeur(String nom_editeur);
    List<Editeur> findByNomEditeurContainingIgnoreCase(String filter);

}