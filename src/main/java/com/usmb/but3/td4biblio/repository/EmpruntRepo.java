package com.usmb.but3.td4biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.EmpruntId;

@Repository
public interface EmpruntRepo extends JpaRepository<Emprunt, EmpruntId> {
    
    // Retrouver les emprunts d'un utilisateur via son ID
    List<Emprunt> findByUtilisateurId(Integer utilisateurId);
    
    // Retrouver l'historique des emprunts d'un document via son ID
    List<Emprunt> findByDocumentId(Integer documentId);
}