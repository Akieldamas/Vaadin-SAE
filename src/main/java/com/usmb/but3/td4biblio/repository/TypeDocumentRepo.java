package com.usmb.but3.td4biblio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.TypeDocument;

public interface TypeDocumentRepo extends JpaRepository<TypeDocument, Integer> {
    Optional<TypeDocument> findByNom(String nom);
}