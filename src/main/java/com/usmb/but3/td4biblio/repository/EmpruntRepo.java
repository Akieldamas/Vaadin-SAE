package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.usmb.but3.td4biblio.entity.Emprunt;

public interface EmpruntRepo extends JpaRepository<Emprunt, Integer> {
    
    List<Emprunt> findByUtilisateurId(Integer utilisateurId);
    List<Emprunt> findByDocumentId(Integer documentId);
    
    @Query("SELECT COUNT(e) FROM Emprunt e WHERE e.utilisateur.id = :utilisateurId AND e.dateRendu IS NULL")
    long countEmpruntsActifs(@Param("utilisateurId") Integer utilisateurId);

    @Query("SELECT e FROM Emprunt e WHERE " +
           "LOWER(e.utilisateur.nom) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.utilisateur.prenom) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
           "LOWER(e.document.titre) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<Emprunt> searchEmprunts(@Param("filter") String filter);
}