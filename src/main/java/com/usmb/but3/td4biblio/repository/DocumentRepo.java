package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import com.usmb.but3.td4biblio.entity.Document;

/**
 *  * Repository is an interface that provides access to data in a database
 *  
 */
public interface DocumentRepo extends JpaRepository<Document, Integer> {

    // Exemples de requêtes utiles basées sur tes clés étrangères
    List<Document> findByAuteurId(Integer auteurId);

    List<Document> findByEditeurId(Integer editeurId);

    List<Document> findByCodeEmplacement(String codeEmplacement);

    List<Document> findByTitreContainingIgnoreCase(String filter);

    @Query("SELECT d FROM Document d WHERE d.id NOT IN (SELECT e.document.id FROM Emprunt e)")
    List<Document> findDocumentsDisponibles();

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.genres WHERE d.id = :id")
    Optional<Document> findByIdWithGenres(@Param("id") Integer id);
}