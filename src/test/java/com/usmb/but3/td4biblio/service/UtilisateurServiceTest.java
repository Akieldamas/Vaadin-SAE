package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Utilisateur;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class UtilisateurServiceTest {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private RoleUtilisateurService roleUtilisateurService;

    // =========================================================================
    // getAllUtilisateurs
    // =========================================================================

    @Test
    void testGetAllUtilisateurs_retourne8Utilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        assertThat(utilisateurs).hasSizeGreaterThanOrEqualTo(8);
    }

    @Test
    void testGetAllUtilisateurs_premierEstMdurand() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        assertThat(utilisateurs.get(0).getLogin()).isEqualTo("mdurand");
        assertThat(utilisateurs.get(0).getNom()).isEqualTo("Durand");
    }

    @Test
    void testGetAllUtilisateurs_triParIdAscendant() {
        List<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
        for (int i = 0; i < utilisateurs.size() - 1; i++) {
            assertThat(utilisateurs.get(i).getId()).isLessThan(utilisateurs.get(i + 1).getId());
        }
    }

    @Test
    void testGetAllUtilisateurs_chaqueUtilisateurAUnRole() {
        utilisateurService.getAllUtilisateurs().forEach(u ->
                assertThat(u.getRoleUtilisateur()).isNotNull());
    }

    // =========================================================================
    // getUtilisateurById
    // =========================================================================

    @Test
    void testGetUtilisateurById1_estMdurand() {
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        assertThat(u).isNotNull();
        assertThat(u.getLogin()).isEqualTo("mdurand");
        assertThat(u.getNom()).isEqualTo("Durand");
        assertThat(u.getPrenom()).isEqualTo("Marie");
        assertThat(u.getRoleUtilisateur().getId()).isEqualTo(1); // Bibliothécaire
        assertThat(u.getDateFinAbonnement()).isNull();            // Pas d'abonnement
    }

    @Test
    void testGetUtilisateurById3_estAliceMoreau() {
        Utilisateur u = utilisateurService.getUtilisateurById(3);
        assertThat(u.getLogin()).isEqualTo("amoreau");
        assertThat(u.getNom()).isEqualTo("Moreau");
        assertThat(u.getPrenom()).isEqualTo("Alice");
        assertThat(u.getRoleUtilisateur().getId()).isEqualTo(2); // Emprunteur
        assertThat(u.getNumeroCarte()).isEqualTo("EMP0000001");
        assertThat(u.getDateFinAbonnement()).isEqualTo(LocalDate.of(2026, 12, 31));
    }

    @Test
    void testGetUtilisateurById8_estRomainTremblay() {
        Utilisateur u = utilisateurService.getUtilisateurById(8);
        assertThat(u.getLogin()).isEqualTo("rtremblay");
        assertThat(u.getNom()).isEqualTo("Tremblay");
        assertThat(u.getNumeroCarte()).isEqualTo("EMP0000006");
    }

    @Test
    void testGetUtilisateurById_idInexistantRetourneNull() {
        assertThat(utilisateurService.getUtilisateurById(99999)).isNull();
    }

    // =========================================================================
    // saveUtilisateur
    // =========================================================================

    @Test
    @Transactional
    void testSaveUtilisateur_idGenereApres8() {
        Utilisateur u = buildUtilisateur("nouveauLogin", "Nouveau", "Test");
        Utilisateur saved = utilisateurService.saveUtilisateur(u);
        assertThat(saved.getId()).isNotNull().isGreaterThan(8);
    }

    @Test
    @Transactional
    void testSaveUtilisateur_tousChampsPersistents() {
        Utilisateur u = buildUtilisateur("loginPersist", "Dupont", "Jean");
        Utilisateur saved = utilisateurService.saveUtilisateur(u);
        Utilisateur found = utilisateurService.getUtilisateurById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Dupont");
        assertThat(found.getPrenom()).isEqualTo("Jean");
        assertThat(found.getLogin()).isEqualTo("loginPersist");
        assertThat(found.getEmail()).isEqualTo("loginPersist@test.com");
        assertThat(found.getNumeroCarte()).isEqualTo("TESTCARTE");
        assertThat(found.getRoleUtilisateur().getId()).isEqualTo(2);
    }

    // =========================================================================
    // updateUtilisateur
    // =========================================================================

    @Test
    @Transactional
    void testUpdateUtilisateur_nomMisAJour() {
        Utilisateur u = buildUtilisateur("loginUpdate", "AncienNom", "Prenom");
        Utilisateur saved = utilisateurService.saveUtilisateur(u);
        saved.setNom("NouveauNom");
        utilisateurService.updateUtilisateur(saved);
        assertThat(utilisateurService.getUtilisateurById(saved.getId()).getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testUpdateUtilisateur_emailMisAJour() {
        Utilisateur u = buildUtilisateur("loginEmail", "NomEmail", "Prenom");
        Utilisateur saved = utilisateurService.saveUtilisateur(u);
        saved.setEmail("nouveau@email.com");
        utilisateurService.updateUtilisateur(saved);
        assertThat(utilisateurService.getUtilisateurById(saved.getId()).getEmail()).isEqualTo("nouveau@email.com");
    }

    @Test
    @Transactional
    void testUpdateUtilisateur_dateFinAbonnementMiseAJour() {
        Utilisateur u = buildUtilisateur("loginDate", "NomDate", "Prenom");
        Utilisateur saved = utilisateurService.saveUtilisateur(u);
        LocalDate nouvelleDate = LocalDate.of(2028, 6, 30);
        saved.setDateFinAbonnement(nouvelleDate);
        utilisateurService.updateUtilisateur(saved);
        assertThat(utilisateurService.getUtilisateurById(saved.getId()).getDateFinAbonnement())
                .isEqualTo(nouvelleDate);
    }

    // =========================================================================
    // deleteUtilisateurById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteUtilisateurById_utilisateurPlusPresent() {
        Integer id = utilisateurService.saveUtilisateur(
                buildUtilisateur("loginDelete", "ASupprimer", "Test")).getId();
        utilisateurService.deleteUtilisateurById(id);
        assertThat(utilisateurService.getUtilisateurById(id)).isNull();
    }

    // =========================================================================
    // getUtilisateursByRole
    // =========================================================================

    @Test
    void testGetUtilisateursByRole1_retourne2Bibliothecaires() {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateursByRole(1);
        assertThat(utilisateurs).hasSize(2);
        assertThat(utilisateurs).allSatisfy(u ->
                assertThat(u.getRoleUtilisateur().getId()).isEqualTo(1));
        assertThat(utilisateurs).extracting(Utilisateur::getLogin)
                .containsExactlyInAnyOrder("mdurand", "jpires");
    }

    @Test
    void testGetUtilisateursByRole2_retourne6Emprunteurs() {
        List<Utilisateur> utilisateurs = utilisateurService.getUtilisateursByRole(2);
        assertThat(utilisateurs).hasSize(6);
        assertThat(utilisateurs).allSatisfy(u ->
                assertThat(u.getRoleUtilisateur().getId()).isEqualTo(2));
    }

    @Test
    void testGetUtilisateursByRole_roleInexistantRetourneVide() {
        assertThat(utilisateurService.getUtilisateursByRole(99999)).isEmpty();
    }

    // =========================================================================
    // getByNomOrNumeroCarte
    // =========================================================================

    @Test
    void testGetByNomOrNumeroCarte_Moreau_trouveAlice() {
        List<Utilisateur> utilisateurs = utilisateurService.getByNomOrNumeroCarte("Moreau", "");
        assertThat(utilisateurs).isNotEmpty();
        assertThat(utilisateurs).anySatisfy(u -> assertThat(u.getNom()).isEqualTo("Moreau"));
    }

    @Test
    void testGetByNomOrNumeroCarte_carteEMP1_trouveAlice() {
        List<Utilisateur> utilisateurs = utilisateurService.getByNomOrNumeroCarte("", "EMP0000001");
        assertThat(utilisateurs).isNotEmpty();
        assertThat(utilisateurs.get(0).getNumeroCarte()).isEqualTo("EMP0000001");
    }

    @Test
    void testGetByNomOrNumeroCarte_filtreParRole2Seulement() {
        // la méthode filtre sur roleUtilisateurId=2 → bibliothécaires exclus
        List<Utilisateur> utilisateurs = utilisateurService.getByNomOrNumeroCarte("Durand", "");
        // Durand est bibliothécaire (role=1) → ne devrait PAS apparaître
        assertThat(utilisateurs).isEmpty();
    }


    // =========================================================================
    // Helpers
    // =========================================================================

    private Utilisateur buildUtilisateur(String login, String nom, String prenom) {
        Utilisateur u = new Utilisateur();
        u.setLogin(login);
        u.setMotDePasse("mdp1234");
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(login + "@test.com");
        u.setAdresse("1 rue des Tests");
        u.setNumeroCarte("TESTCARTE");
        u.setDateNaissance(LocalDate.of(1990, 6, 15));
        u.setDateFinAbonnement(LocalDate.of(2027, 12, 31));
        u.setDureeEmpruntMax(3);
        u.setMaxEmprunts(5);
        u.setRoleUtilisateur(roleUtilisateurService.getRoleById(2));
        return u;
    }
}