package com.usmb.but3.td4biblio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Bibliotheque;

public interface BibliothequeRepo extends JpaRepository<Bibliotheque, Integer> {
    Optional<Bibliotheque> findByNom(String nom);
}