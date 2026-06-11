package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
}