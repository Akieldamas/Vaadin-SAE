package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.EmpruntId;
import com.usmb.but3.td4biblio.entity.Utilisateur;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class EmpruntServiceTest {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Test
    void testGetAllEmprunts_retourne18Emprunts() {
        List<Emprunt> emprunts = empruntService.getAllEmprunts();
        assertThat(emprunts).hasSizeGreaterThanOrEqualTo(18);
    }

    @Test
    void testGetAllEmprunts_chaqueEmpruntAUnUtilisateur() {
        empruntService.getAllEmprunts().forEach(e ->
                assertThat(e.getUtilisateur()).isNotNull());
    }

    @Test
    void testGetAllEmprunts_chaqueEmpruntAUnDocument() {
        empruntService.getAllEmprunts().forEach(e ->
                assertThat(e.getDocument()).isNotNull());
    }

    // =========================================================================
    // getEmpruntById
    // =========================================================================

    @Test
    void testGetEmpruntById_Alice_doc1() {
        EmpruntId id = new EmpruntId(3, 1);
        Emprunt e = empruntService.getEmpruntById(id);
        assertThat(e).isNotNull();
        assertThat(e.getUtilisateur().getId()).isEqualTo(3);
        assertThat(e.getDocument().getId()).isEqualTo(1);
        assertThat(e.getProlongation()).isFalse();
    }

    @Test
    void testGetEmpruntById_Lucas_doc7_estProlonge() {
        // Lucas(4) a prolongé l'emprunt du doc 7 (SDA)
        EmpruntId id = new EmpruntId(4, 7);
        Emprunt e = empruntService.getEmpruntById(id);
        assertThat(e).isNotNull();
        assertThat(e.getProlongation()).isTrue();
    }

    @Test
    void testGetEmpruntById_Romain_doc10_estProlonge() {
        EmpruntId id = new EmpruntId(8, 10);
        Emprunt e = empruntService.getEmpruntById(id);
        assertThat(e).isNotNull();
        assertThat(e.getProlongation()).isTrue();
    }

    @Test
    void testGetEmpruntById_idInexistantRetourneNull() {
        EmpruntId id = new EmpruntId(99999, 99999);
        assertThat(empruntService.getEmpruntById(id)).isNull();
    }

    // =========================================================================
    // getEmpruntsByUtilisateur
    // =========================================================================

    @Test
    void testGetEmpruntsByUtilisateur_Alice3_retourne3Emprunts() {
        List<Emprunt> emprunts = empruntService.getEmpruntsByUtilisateur(3);
        assertThat(emprunts).hasSize(3);
        assertThat(emprunts).allSatisfy(e ->
                assertThat(e.getUtilisateur().getId()).isEqualTo(3));
    }

    @Test
    void testGetEmpruntsByUtilisateur_Lucas4_retourne3Emprunts() {
        List<Emprunt> emprunts = empruntService.getEmpruntsByUtilisateur(4);
        assertThat(emprunts).hasSize(3);
    }

    @Test
    void testGetEmpruntsByUtilisateur_Romain8_retourne3Emprunts() {
        List<Emprunt> emprunts = empruntService.getEmpruntsByUtilisateur(8);
        assertThat(emprunts).hasSize(3);
        assertThat(emprunts).allSatisfy(e ->
                assertThat(e.getUtilisateur().getId()).isEqualTo(8));
    }

    @Test
    void testGetEmpruntsByUtilisateur_inexistantRetourneVide() {
        assertThat(empruntService.getEmpruntsByUtilisateur(99999)).isEmpty();
    }

    // =========================================================================
    // getEmpruntsByDocument
    // =========================================================================

    @Test
    void testGetEmpruntsByDocument_doc1_retourne1Emprunt() {
        // doc 1 (1984) emprunté uniquement par Alice
        List<Emprunt> emprunts = empruntService.getEmpruntsByDocument(1);
        assertThat(emprunts).hasSize(1);
        assertThat(emprunts.get(0).getUtilisateur().getId()).isEqualTo(3); // Alice
    }

    @Test
    void testGetEmpruntsByDocument_doc13_retourne1Emprunt() {
        // Inception emprunté par Alice en cours
        List<Emprunt> emprunts = empruntService.getEmpruntsByDocument(13);
        assertThat(emprunts).hasSize(1);
        assertThat(emprunts.get(0).getUtilisateur().getId()).isEqualTo(3);
    }

    @Test
    void testGetEmpruntsByDocument_inexistantRetourneVide() {
        assertThat(empruntService.getEmpruntsByDocument(99999)).isEmpty();
    }

    // =========================================================================
    // saveEmprunt
    // =========================================================================

    @Test
    @Transactional
    void testSaveEmprunt_nouveauEmpruntPersiste() {
        // Bibliothécaire mdurand (id=1) n'a aucun emprunt dans le seed
        // On utilise un document rendu (doc 2 disponible, jamais emprunté)
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        Document doc = documentService.getDocumentById(2);
        LocalDate debut = LocalDate.of(2024, 6, 1);
        LocalDate fin = LocalDate.of(2024, 6, 22);

        Emprunt emprunt = buildEmprunt(u, doc, debut, fin);
        Emprunt saved = empruntService.saveEmprunt(emprunt);

        assertThat(saved).isNotNull();
        assertThat(saved.getUtilisateur().getId()).isEqualTo(1);
        assertThat(saved.getDocument().getId()).isEqualTo(2);
        assertThat(saved.getDebutEmprunt()).isEqualTo(debut);
        assertThat(saved.getFinEmprunt()).isEqualTo(fin);
        assertThat(saved.getProlongation()).isFalse();
    }

    @Test
    @Transactional
    void testSaveEmprunt_retrouvableParId() {
        Utilisateur u = utilisateurService.getUtilisateurById(2); // jpires
        Document doc = documentService.getDocumentById(2);

        Emprunt saved = empruntService.saveEmprunt(
                buildEmprunt(u, doc, LocalDate.now(), LocalDate.now().plusWeeks(3)));

        EmpruntId id = new EmpruntId(u.getId(), doc.getId());
        assertThat(empruntService.getEmpruntById(id)).isNotNull();
    }

    // =========================================================================
    // updateEmprunt
    // =========================================================================

    @Test
    @Transactional
    void testUpdateEmprunt_prolongationMiseAJour() {
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        Document doc = documentService.getDocumentById(2);

        Emprunt saved = empruntService.saveEmprunt(
                buildEmprunt(u, doc, LocalDate.now(), LocalDate.now().plusWeeks(3)));

        saved.setProlongation(true);
        saved.setFinProlongation(LocalDate.now().plusWeeks(6));
        empruntService.updateEmprunt(saved);

        EmpruntId id = new EmpruntId(u.getId(), doc.getId());
        Emprunt updated = empruntService.getEmpruntById(id);
        assertThat(updated.getProlongation()).isTrue();
        assertThat(updated.getFinProlongation()).isEqualTo(LocalDate.now().plusWeeks(6));
    }

    // =========================================================================
    // deleteEmpruntById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteEmpruntById_empruntPlusPresent() {
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        Document doc = documentService.getDocumentById(2);

        empruntService.saveEmprunt(
                buildEmprunt(u, doc, LocalDate.now(), LocalDate.now().plusWeeks(3)));

        EmpruntId id = new EmpruntId(u.getId(), doc.getId());
        empruntService.deleteEmpruntById(id);
        assertThat(empruntService.getEmpruntById(id)).isNull();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Emprunt buildEmprunt(Utilisateur u, Document doc,
                                  LocalDate debut, LocalDate fin) {
        Emprunt e = new Emprunt();
        e.setUtilisateur(u);
        e.setDocument(doc);
        e.setDebutEmprunt(debut);
        e.setFinEmprunt(fin);
        e.setProlongation(false);
        return e;
    }
}