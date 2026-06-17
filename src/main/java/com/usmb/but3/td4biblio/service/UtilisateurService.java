package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * La couche Service pour la gestion des utilisateurs.
 * Elle interagit avec la couche Repository pour accéder aux données.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UtilisateurService {

    private final UtilisateurRepo utilisateurRepo;

    public List<Utilisateur> getAllUtilisateurs() {
        // Retourne la liste triée par ID ascendant comme pour les livres et auteurs
        return utilisateurRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Utilisateur getUtilisateurById(Integer id) {
        Optional<Utilisateur> optionalUtilisateur = utilisateurRepo.findById(id);
        log.debug("id: {}", id);
        if (optionalUtilisateur.isPresent()) {
            return optionalUtilisateur.get();
        }
        log.error("Utilisateur with id: {} doesn't exist", id);
        return null;
    }

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepo.save(utilisateur);
    }

    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepo.save(utilisateur);
    }

    public void deleteUtilisateurById(Integer id) {
        utilisateurRepo.deleteById(id);
    }

    public List<Utilisateur> getUtilisateursByRole(Integer roleUtilisateurId) {
        // Permet de filtrer par ID de rôle pour la vue
        return utilisateurRepo.findByRoleUtilisateurId(roleUtilisateurId);
    }
    public List<Utilisateur> getUtilisateursByRoleWithDate(Integer roleUtilisateurId) {
        // Permet de filtrer par ID de rôle pour la vue
        return utilisateurRepo.findByRoleUtilisateurIdAndDateFinAbonnementBefore(roleUtilisateurId, LocalDate.now());
    }

    public List<Utilisateur> getByNomOrNumeroCarte(String nom, String numeroCarte) {
       return utilisateurRepo.findByNomContainingIgnoreCaseAndNumeroCarteContainingIgnoreCaseAndRoleUtilisateurId(nom, numeroCarte, 2);
    }

    public List<Utilisateur> getByNomOrNumeroCarteWithDate(String nom, String numeroCarte) {
        return utilisateurRepo.findByNomContainingIgnoreCaseAndNumeroCarteContainingIgnoreCaseAndRoleUtilisateurIdAndDateFinAbonnementBefore(nom, numeroCarte, 2, LocalDate.now());
     }
    public Utilisateur getUtilisateurByLogin(String login) {
        // Permet de filtrer par ID de rôle pour la vue
        return utilisateurRepo.getUtilisateurByLogin(login);
    }

    public List<Utilisateur> getUtilisateursAutorises() {
        return utilisateurRepo.findUtilisateursAutorises();
    }
}