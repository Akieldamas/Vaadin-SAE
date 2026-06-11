package com.usmb.but3.td4biblio.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.usmb.but3.td4biblio.entity.Livre;
import com.usmb.but3.td4biblio.entity.Utilisateur;

/**
 *  * Repository is an interface that provides access to data in a database
 *  
 */
public interface UtilisateurRepo extends JpaRepository<Utilisateur, Integer> {
    List<Utilisateur> findByRole(Integer roleId);
}