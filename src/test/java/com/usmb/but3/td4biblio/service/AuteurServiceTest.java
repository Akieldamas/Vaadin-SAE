package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Auteur;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class AuteurServiceTest {

    @Autowired
    private AuteurService auteurService;

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteurById1() {
        Auteur auteur = auteurService.getAuteurById(1);
        assertThat(auteur).isNotNull();
        assertThat(auteur.getNom()).isEqualTo("Orwell");
        assertThat(auteur.getPrenom()).isEqualTo("George");

    }

    @Test
    void testGetAuteurById_idInexistant() {
        assertThat(auteurService.getAuteurById(99999)).isNull();
    }

    @Test
    @Transactional
    void testSaveAuteur() {
        Auteur auteur = buildAuteur("Flaubert", "Gustave", "Française",
                LocalDate.of(1821, 12, 12), LocalDate.of(1880, 5, 8));
        Auteur saved = auteurService.saveAuteur(auteur);
        Auteur found = auteurService.getAuteurById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Flaubert");
        assertThat(found.getPrenom()).isEqualTo("Gustave");
        assertThat(found.getNationalite()).isEqualTo("Française");
        assertThat(found.getDateNaissance()).isEqualTo(LocalDate.of(1821, 12, 12));
        assertThat(found.getDateDeces()).isEqualTo(LocalDate.of(1880, 5, 8));
    }

    @Test
    @Transactional
    void testUpdateAuteur() {
        Auteur auteur = buildAuteur("AvantUpdate", "Prenom", "Française", null, null);
        Auteur saved = auteurService.saveAuteur(auteur);
        saved.setNom("ApresUpdate");
        auteurService.updateAuteur(saved);
        assertThat(auteurService.getAuteurById(saved.getId()).getNom()).isEqualTo("ApresUpdate");
    }

    @Test
    @Transactional
    void testDeleteAuteurById() {
        Auteur auteur = buildAuteur("ASupprimer", "Test", null, null, null);
        int id = auteurService.saveAuteur(auteur).getId();
        auteurService.deleteAuteurById(id);
        assertThat(auteurService.getAuteurById(id)).isNull();
    }

    @Test
    void testGetAuteursByNom_Orwell_retourneUnSeul() {
        List<Auteur> auteurs = auteurService.getAuteursByNom("Orwell");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNom_inexistantRetourneVide() {
        assertThat(auteurService.getAuteursByNom("NomInexistant")).isEmpty();
    }

    @Test
    void testGetAuteursByNomAndPrenom_OrwellGeorge_retourne1() {
        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Orwell", "George");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingIncorrectRetourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Orwell", "Albert");
        assertThat(auteurs).isEmpty();
    }
    
    @Test
    void testGetAuteursByNomLike_inexistantRetourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomLikeAndPrenomLike("ZZZZ", "YYYY");
        assertThat(auteurs).isEmpty();
    }

    @Test
    void testGetByNomContainingIgnoreCase_orwell_minuscules() {
        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("orwell");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
    }

    @Test
    void testGetByNomContainingIgnoreCase_ORWELL_majuscules() {
        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("ORWELL");
        assertThat(auteurs).hasSize(1);
    }

    @Test
    void testGetAuteursByNomStartWith_XXX_retourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomStartWithIgnoreCase("XXX");
        assertThat(auteurs).isEmpty();
    }

    private Auteur buildAuteur(String nom, String prenom, String nationalite,
                                LocalDate naissance, LocalDate deces) {
        Auteur auteur = new Auteur();
        auteur.setNom(nom);
        auteur.setPrenom(prenom);
        auteur.setNationalite(nationalite);
        auteur.setDateNaissance(naissance);
        auteur.setDateDeces(deces);
        return auteur;
    }
}