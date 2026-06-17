package com.usmb.but3.td4biblio.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Utilisateur;

/**
 *  * Repository is an interface that provides access to data in a database
 *  
 */
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Integer> {
    List<Utilisateur> findByRoleUtilisateurId(Integer roleUtilisateurId);
    List<Utilisateur> findByRoleUtilisateurIdAndDateFinAbonnementBefore(Integer roleUtilisateurId, LocalDate date);
    List<Utilisateur> findByNomContainingIgnoreCaseAndNumeroCarteContainingIgnoreCaseAndRoleUtilisateurId(String nom, String numeroCarte, Integer id);
    List<Utilisateur> findByNomContainingIgnoreCaseAndNumeroCarteContainingIgnoreCaseAndRoleUtilisateurIdAndDateFinAbonnementBefore(String nom, String numeroCarte, Integer id, LocalDate date);
    Utilisateur getUtilisateurByLoginAndMotDePasse(String login, String motDePasse);


    @Query("SELECT u FROM Utilisateur u WHERE u.roleUtilisateur.id <> 1")
    List<Utilisateur> findUtilisateursAutorises();
}