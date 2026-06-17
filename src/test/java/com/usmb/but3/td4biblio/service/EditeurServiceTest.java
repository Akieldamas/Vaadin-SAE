package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class EditeurServiceTest {

    @Autowired
    private EditeurService editeurService;

    @Test
    void testGetAllEditeurs() {
        List<Editeur> editeurs = editeurService.getAllEditeurs();
        assertThat(editeurs).isNotEmpty();
    }

    @Test
    void testGetAllEditeurs_premierEstGallimard() {
        List<Editeur> editeurs = editeurService.getAllEditeurs();
        assertThat(editeurs.get(0).getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetEditeurById1_estGallimard() {
        Editeur e = editeurService.getEditeurById(1);
        assertThat(e).isNotNull();
        assertThat(e.getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetEditeurById_idInexistantRetourneNull() {
        assertThat(editeurService.getEditeurById(99999)).isNull();
    }

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

    @Test
    @Transactional
    void testSaveEditeur() {
        Editeur e = buildEditeur("Nouvel Editeur");
        Editeur saved = editeurService.saveEditeur(e);
        Editeur found = editeurService.getEditeurById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Nouvel Editeur");
    }

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
    void testDeleteEditeurById() {
        Integer id = editeurService.saveEditeur(buildEditeur("ASupprimer")).getId();
        editeurService.deleteEditeurById(id);
        assertThat(editeurService.getEditeurById(id)).isNull();
    }

    @Test
    void testGetByNomContainingIgnoreCase() {
        List<Editeur> editeurs = editeurService.getEditeursByNomContainingIgnoreCase("gall");
        assertThat(editeurs).hasSize(1);
        assertThat(editeurs.get(0).getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetByNomContainingIgnoreCase_inexistantRetourneVide() {
        assertThat(editeurService.getEditeursByNomContainingIgnoreCase("ZZZINEXISTANT")).isEmpty();
    }
    @Test
    void testGetEditeursByNom() {
        List<Editeur> editeurs = editeurService.getEditeursByNomContainingIgnoreCase("Ga");
        assertThat(editeurs).isNotEmpty();
        assertThat(editeurs).allSatisfy(e ->
                assertThat(e.getNom()).startsWithIgnoringCase("Ga"));
    }

    @Test
    void testGetEditeursByNomStartWith_So_trouveSony() {
        List<Editeur> editeurs = editeurService.getEditeursByNomContainingIgnoreCase("So");
        assertThat(editeurs).isNotEmpty();
        assertThat(editeurs).anySatisfy(e -> assertThat(e.getNom()).contains("Sony"));
    }

    private Editeur buildEditeur(String nom) {
        Editeur e = new Editeur();
        e.setNom(nom);
        e.setAdresse("1 rue de l'Edition");
        e.setLienSiteWeb("https://editeur.fr");
        return e;
    }
}