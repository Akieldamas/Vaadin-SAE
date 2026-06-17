package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.EmpruntId;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.usmb.but3.td4biblio.service.UtilisateurService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmpruntControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private EmpruntService empruntService;
    @Autowired private DocumentService documentService;
    @Autowired private UtilisateurService utilisateurService;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/emprunt" + path;
    }

    private ResponseEntity<List<Emprunt>> getEmprunts(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Emprunt>>() {});
    }

    // =========================================================================
    // GET /biblio/emprunt/
    // =========================================================================

    @Test
    void testGetAllEmprunts_retourne200() {
        assertThat(getEmprunts("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllEmprunts_retourne18Emprunts() {
        assertThat(getEmprunts("/").getBody()).hasSizeGreaterThanOrEqualTo(18);
    }

    @Test
    void testGetAllEmprunts_chaqueEmpruntAUnUtilisateurEtUnDocument() {
        getEmprunts("/").getBody().forEach(e -> {
            assertThat(e.getUtilisateur()).isNotNull();
            assertThat(e.getDocument()).isNotNull();
        });
    }

    // =========================================================================
    // GET /biblio/emprunt/utilisateur/{id}
    // =========================================================================

    @Test
    void testGetEmpruntsByUtilisateur3_retourne200Et3Emprunts() {
        ResponseEntity<List<Emprunt>> response = getEmprunts("/utilisateur/3");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).allSatisfy(e ->
                assertThat(e.getUtilisateur().getId()).isEqualTo(3));
    }

    @Test
    void testGetEmpruntsByUtilisateur4_contientUneProlongation() {
        List<Emprunt> emprunts = getEmprunts("/utilisateur/4").getBody();
        assertThat(emprunts).anySatisfy(e -> assertThat(e.getProlongation()).isTrue());
    }

    @Test
    void testGetEmpruntsByUtilisateur_inexistantRetourneListeVide() {
        List<Emprunt> emprunts = getEmprunts("/utilisateur/99999").getBody();
        assertThat(emprunts).isEmpty();
    }

    // =========================================================================
    // GET /biblio/emprunt/document/{id}
    // =========================================================================

    @Test
    void testGetEmpruntsByDocument1_retourne200Et1Emprunt() {
        ResponseEntity<List<Emprunt>> response = getEmprunts("/document/1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Emprunt> emprunts = response.getBody();
        assertThat(emprunts).hasSize(1);
        assertThat(emprunts.get(0).getUtilisateur().getId()).isEqualTo(3); // Alice
    }

    @Test
    void testGetEmpruntsByDocument_inexistantRetourneListeVide() {
        assertThat(getEmprunts("/document/99999").getBody()).isEmpty();
    }

    // =========================================================================
    // POST /biblio/emprunt/
    // =========================================================================

    @Test
    @Transactional
    void testSaveEmprunt_retourne200EtPersiste() {
        // mdurand (id=1, bibliothécaire) n'a aucun emprunt dans le seed
        // doc 2 (La Ferme des animaux) disponible dans le seed
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        Document doc = documentService.getDocumentById(2);

        Emprunt emprunt = buildEmprunt(u, doc,
                LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 22));

        ResponseEntity<Emprunt> response = restTemplate.postForEntity(url("/"), emprunt, Emprunt.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Emprunt saved = response.getBody();
        assertThat(saved.getUtilisateur().getId()).isEqualTo(1);
        assertThat(saved.getDocument().getId()).isEqualTo(2);
        assertThat(saved.getDebutEmprunt()).isEqualTo(LocalDate.of(2024, 6, 1));
        assertThat(saved.getProlongation()).isFalse();
    }

    // =========================================================================
    // DELETE /biblio/emprunt/
    // =========================================================================

    @Test
    @Transactional
    void testDeleteEmprunt_retourne200EtEmpruntPlusExistant() {
        Utilisateur u = utilisateurService.getUtilisateurById(1);
        Document doc = documentService.getDocumentById(2);

        restTemplate.postForEntity(url("/"),
                buildEmprunt(u, doc, LocalDate.now(), LocalDate.now().plusWeeks(3)),
                Emprunt.class);

        ResponseEntity<String> response = restTemplate.exchange(
                url("?utilisateurId=1&documentId=2"),
                HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(empruntService.getEmpruntById(new EmpruntId(1, 2))).isNull();
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
