package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.RoleUtilisateur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class RoleUtilisateurServiceTest {

    @Autowired
    private RoleUtilisateurService roleUtilisateurService;

    @Test
    void testGetAllRoles_retourne2Roles() {
        List<RoleUtilisateur> roles = roleUtilisateurService.getAllRoles();
        assertThat(roles).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetRoleById1_estBibliothecaire() {
        RoleUtilisateur role = roleUtilisateurService.getRoleById(1);
        assertThat(role).isNotNull();
        assertThat(role.getLibelle()).isEqualTo("Bibliothécaire");
    }

    @Test
    void testGetRoleById_idInexistantRetourneNull() {
        assertThat(roleUtilisateurService.getRoleById(99999)).isNull();
    }

    @Test
    @Transactional
    void testSaveRole() {
        RoleUtilisateur r = new RoleUtilisateur();
        r.setLibelle("Stagiaire");
        RoleUtilisateur saved = roleUtilisateurService.saveRole(r);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLibelle()).isEqualTo("Stagiaire");
    }

    @Test
    @Transactional
    void testUpdateRole_libelleMisAJour() {
        RoleUtilisateur r = new RoleUtilisateur();
        r.setLibelle("RoleAvant");
        RoleUtilisateur saved = roleUtilisateurService.saveRole(r);
        saved.setLibelle("RoleApres");
        roleUtilisateurService.updateRole(saved);
        assertThat(roleUtilisateurService.getRoleById(saved.getId()).getLibelle()).isEqualTo("RoleApres");
    }

    @Test
    @Transactional
    void testDeleteRoleById() {
        RoleUtilisateur r = new RoleUtilisateur();
        r.setLibelle("RoleASupprimer");
        Integer id = roleUtilisateurService.saveRole(r).getId();
        roleUtilisateurService.deleteRoleById(id);
        assertThat(roleUtilisateurService.getRoleById(id)).isNull();
    }
}