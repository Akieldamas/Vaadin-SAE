package com.usmb.but3.td4biblio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.GenreDocument;

public interface GenreDocumentRepo extends JpaRepository<GenreDocument, Integer> {
    Optional<GenreDocument> findByNom(String nom);
}