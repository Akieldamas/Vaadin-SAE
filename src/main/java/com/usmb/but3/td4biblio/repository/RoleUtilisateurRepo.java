package com.usmb.but3.td4biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;

public interface RoleUtilisateurRepo extends JpaRepository<RoleUtilisateur, Integer> {
    RoleUtilisateur findByLibelle(String libelle);
}