package com.usmb.but3.td4biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Editeur;

public interface EditeurRepo extends JpaRepository<Editeur, Integer> {
    Editeur findByNom(String nom);
}