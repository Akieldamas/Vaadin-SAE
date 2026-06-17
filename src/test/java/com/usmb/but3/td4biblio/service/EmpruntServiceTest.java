package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class EmpruntServiceTest {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Test
    void testGetAllEmprunts() {
        List<Emprunt> emprunts = empruntService.getAllEmprunts();
        assertThat(emprunts).isNotEmpty();
        assertThat(emprunts).allSatisfy(e -> {
            assertThat(e.getUtilisateur()).isNotNull();
            assertThat(e.getDocument()).isNotNull();
        });
    }

    @Test
    void testGetEmpruntById() {
        Emprunt reference = empruntService.getAllEmprunts().get(0);

        Emprunt e = empruntService.getEmpruntById(reference.getId());

        assertThat(e).isNotNull();
        assertThat(e.getId()).isEqualTo(reference.getId());
    }

    @Test
    void testGetEmpruntByIdInexistant() {
        assertThat(empruntService.getEmpruntById(99999)).isNull();
    }

    @Test
    void testGetEmpruntsDeLUtilisateur() {
        List<Emprunt> emprunts = empruntService.getEmpruntsDeLUtilisateur(3);
        assertThat(emprunts).isNotEmpty();
        assertThat(emprunts).allSatisfy(e ->
                assertThat(e.getUtilisateur().getId()).isEqualTo(3));
    }

    @Test
    void testGetEmpruntsDeLUtilisateurInexistant() {
        assertThat(empruntService.getEmpruntsDeLUtilisateur(99999)).isEmpty();
    }

    @Test
    void testSearchEmprunts_filtreVideRetourneTout() {
        assertThat(empruntService.searchEmprunts("")).isEqualTo(empruntService.getAllEmprunts());
    }

    @Test
    void testSearchEmprunts_filtreBlancRetourneTout() {
        assertThat(empruntService.searchEmprunts("   ")).isEqualTo(empruntService.getAllEmprunts());
    }

    @Test
    @Transactional
    void testSaveEmprunt() {
        Utilisateur u = utilisateurService.getUtilisateurById(3);
        Document doc = documentService.getDocumentById(2);

        Emprunt saved = empruntService.saveEmprunt(buildEmprunt(u, doc));

        LocalDate dateRetour = LocalDate.now().plusDays(1);
        saved.setDateRendu(dateRetour);
        saved.setProlongation(true);

        Emprunt updated = empruntService.saveEmprunt(saved);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getDateRendu()).isEqualTo(dateRetour);
        assertThat(updated.getProlongation()).isTrue();
        // La date de début ne doit pas être recalculée lors d'une mise à jour
        assertThat(updated.getDateDebut()).isEqualTo(saved.getDateDebut());
    }

    @Test
    @Transactional
    void testDeleteEmprunt() {
        Utilisateur u = utilisateurService.getUtilisateurById(3);
        Document doc = documentService.getDocumentById(2);

        Emprunt saved = empruntService.saveEmprunt(buildEmprunt(u, doc));

        empruntService.deleteEmprunt(saved);

        assertThat(empruntService.getEmpruntById(saved.getId())).isNull();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Emprunt buildEmprunt(Utilisateur u, Document doc) {
        Emprunt e = new Emprunt();
        e.setUtilisateur(u);
        e.setDocument(doc);
        return e;
    }
}