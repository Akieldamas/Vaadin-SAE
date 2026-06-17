package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Editeur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * Seed éditeurs :
 *  1 Gallimard       5 rue Gaston-Gallimard, 75007 Paris
 *  2 Le Seuil        25 bd Romain-Rolland, 75014 Paris
 *  3 J'ai lu         87 quai Panhard, 75013 Paris
 *  4 Universal Music 20 rue des Fossés-Saint-Jacques…
 *  5 Warner Bros.    4000 Warner Blvd, Burbank CA
 *  6 Sony Pictures   10202 W Washington Blvd…
 *  7 Actes Sud       Le Méjan, Place Nina-Berberova, 13200 Arles
 */
@SpringBootTest
public class EditeurServiceTest {

    @Autowired
    private EditeurService editeurService;

    // =========================================================================
    // getAllEditeurs
    // =========================================================================

    @Test
    void testGetAllEditeurs_retourne7Editeurs() {
        List<Editeur> editeurs = editeurService.getAllEditeurs();
        assertThat(editeurs).hasSizeGreaterThanOrEqualTo(7);
    }

    @Test
    void testGetAllEditeurs_premierEstGallimard() {
        List<Editeur> editeurs = editeurService.getAllEditeurs();
        assertThat(editeurs.get(0).getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetAllEditeurs_triParIdAscendant() {
        List<Editeur> editeurs = editeurService.getAllEditeurs();
        for (int i = 0; i < editeurs.size() - 1; i++) {
            assertThat(editeurs.get(i).getId()).isLessThan(editeurs.get(i + 1).getId());
        }
    }

    @Test
    void testGetAllEditeurs_tousOntUnNom() {
        editeurService.getAllEditeurs().forEach(e ->
                assertThat(e.getNom()).isNotBlank());
    }

    // =========================================================================
    // getEditeurById
    // =========================================================================

    @Test
    void testGetEditeurById1_estGallimard() {
        Editeur e = editeurService.getEditeurById(1);
        assertThat(e).isNotNull();
        assertThat(e.getNom()).isEqualTo("Gallimard");
        assertThat(e.getAdresse()).isEqualTo("5 rue Gaston-Gallimard, 75007 Paris");
        assertThat(e.getLienSiteWeb()).isEqualTo("https://www.gallimard.fr");
    }

    @Test
    void testGetEditeurById3_estJaiLu() {
        Editeur e = editeurService.getEditeurById(3);
        assertThat(e.getNom()).isEqualTo("J'ai lu");
    }

    @Test
    void testGetEditeurById5_estWarnerBros() {
        Editeur e = editeurService.getEditeurById(5);
        assertThat(e.getNom()).isEqualTo("Warner Bros.");
    }

    @Test
    void testGetEditeurById7_estActesSud() {
        Editeur e = editeurService.getEditeurById(7);
        assertThat(e.getNom()).isEqualTo("Actes Sud");
    }

    @Test
    void testGetEditeurById_idInexistantRetourneNull() {
        assertThat(editeurService.getEditeurById(99999)).isNull();
    }

    // =========================================================================
    // getEditeurByNom
    // =========================================================================

    @Test
    void testGetEditeurByNom_Gallimard_retourneGallimard() {
        Editeur e = editeurService.getByNom("Gallimard");
        assertThat(e).isNotNull();
        assertThat(e.getId()).isEqualTo(1);
    }

    @Test
    void testGetEditeurByNom_inexistantRetourneNull() {
        assertThat(editeurService.getByNom("EditeurInexistant999")).isNull();
    }

    // =========================================================================
    // saveEditeur
    // =========================================================================

    @Test
    @Transactional
    void testSaveEditeur_idGenereApres7() {
        Editeur e = buildEditeur("Nouvel Editeur");
        Editeur saved = editeurService.saveEditeur(e);
        assertThat(saved.getId()).isNotNull().isGreaterThan(7);
    }

    @Test
    @Transactional
    void testSaveEditeur_tousChampsPersistents() {
        Editeur e = buildEditeur("Editeur Complet");
        e.setAdresse("1 rue des Tests, 75001 Paris");
        e.setLienSiteWeb("https://editeur-test.fr");
        e.setLienWikipedia("https://fr.wikipedia.org/wiki/Test");
        Editeur saved = editeurService.saveEditeur(e);
        Editeur found = editeurService.getEditeurById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Editeur Complet");
        assertThat(found.getAdresse()).isEqualTo("1 rue des Tests, 75001 Paris");
        assertThat(found.getLienSiteWeb()).isEqualTo("https://editeur-test.fr");
    }

    // =========================================================================
    // updateEditeur
    // =========================================================================

    @Test
    @Transactional
    void testUpdateEditeur_nomMisAJour() {
        Editeur e = buildEditeur("AncienNom");
        Editeur saved = editeurService.saveEditeur(e);
        saved.setNom("NouveauNom");
        editeurService.updateEditeur(saved);
        assertThat(editeurService.getEditeurById(saved.getId()).getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testUpdateEditeur_adresseMiseAJour() {
        Editeur e = buildEditeur("EditeurAdresse");
        e.setAdresse("Ancienne Adresse");
        Editeur saved = editeurService.saveEditeur(e);
        saved.setAdresse("Nouvelle Adresse");
        editeurService.updateEditeur(saved);
        assertThat(editeurService.getEditeurById(saved.getId()).getAdresse()).isEqualTo("Nouvelle Adresse");
    }

    // =========================================================================
    // deleteEditeurById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteEditeurById_editeurPlusPresent() {
        Integer id = editeurService.saveEditeur(buildEditeur("ASupprimer")).getId();
        editeurService.deleteEditeurById(id);
        assertThat(editeurService.getEditeurById(id)).isNull();
    }

    // =========================================================================
    // getByNomContainingIgnoreCase
    // =========================================================================

    @Test
    void testGetByNomContainingIgnoreCase_gall_trouveGallimard() {
        List<Editeur> editeurs = editeurService.getByNomContainingIgnoreCase("gall");
        assertThat(editeurs).hasSize(1);
        assertThat(editeurs.get(0).getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetByNomContainingIgnoreCase_Pictures_trouveSony() {
        List<Editeur> editeurs = editeurService.getByNomContainingIgnoreCase("Pictures");
        assertThat(editeurs).isNotEmpty();
        assertThat(editeurs).anySatisfy(e -> assertThat(e.getNom()).contains("Sony"));
    }

    @Test
    void testGetByNomContainingIgnoreCase_inexistantRetourneVide() {
        assertThat(editeurService.getByNomContainingIgnoreCase("ZZZINEXISTANT")).isEmpty();
    }

    // =========================================================================
    // getAuteursByNomStartWithIgnoreCase (méthode nommée pour Editeur)
    // =========================================================================

    @Test
    void testGetEditeursByNomStartWith_Ga_trouveGallimard() {
        List<Editeur> editeurs = editeurService.getAuteursByNomStartWithIgnoreCase("Ga");
        assertThat(editeurs).isNotEmpty();
        assertThat(editeurs).allSatisfy(e ->
                assertThat(e.getNom()).startsWithIgnoringCase("Ga"));
    }

    @Test
    void testGetEditeursByNomStartWith_So_trouveSony() {
        List<Editeur> editeurs = editeurService.getAuteursByNomStartWithIgnoreCase("So");
        assertThat(editeurs).isNotEmpty();
        assertThat(editeurs).anySatisfy(e -> assertThat(e.getNom()).contains("Sony"));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Editeur buildEditeur(String nom) {
        Editeur e = new Editeur();
        e.setNom(nom);
        e.setAdresse("1 rue de l'Edition");
        e.setLienSiteWeb("https://editeur.fr");
        return e;
    }
}