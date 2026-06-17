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
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.FormatService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

/**
 * Seed (extrait, triés par id ASC) :
 *  1  '1984'                   Orwell(1)      Livre  isbn=9782070368228 emplacement=A1-001 pub=1949-06-08
 *  2  'La Ferme des animaux'   Orwell(1)      Livre
 *  3  'L'Étranger'             Camus(2)       Livre  editeur=Gallimard(1)
 *  4  'La Peste'               Camus(2)       Livre  format=GrandFormat(2)
 *  5  'La Main gauche…'        Le Guin(3)     Livre
 *  6  'Ça'                     King(4)        Livre
 *  7  'Le Seigneur des…'       Tolkien(11)    Livre
 *  8  'L'Amant'                Duras(10)      Livre
 *  9  'Songs of Leonard Cohen' Cohen(8)       Album  type=5
 *  10 'I'm Your Man'           Cohen(8)       Album
 *  11 'BO Interstellar'        Zimmer(7)      Album  ← en cours d'emprunt
 *  12 'BO Dune'                Zimmer(7)      Album
 *  13 'Inception'              Nolan(5)       Film   pub=2010-07-16  ← en cours d'emprunt
 *  14 'E.T.'                   Spielberg(6)   Film
 *  15 'Le Voyage de Chihiro'   Miyazaki(9)    Film
 *  16 'Dune (2021)'            Villeneuve(12) Film   ← en cours d'emprunt
 *  17 'Interstellar — Blu-ray' Nolan(5)       Film   ← en cours d'emprunt
 *  18 'Le Château Ambulant'    Miyazaki(9)    Film   ← en cours d'emprunt
 *  19 'Shining — Blu-ray'      King(4)        Film
 *  20 'National Geographic…'   Orwell(1)      Magasine
 *  21 'Le Monde…'              Camus(2)       Journaux
 *  22 'Astérix le Gaulois'     King(4)        BD
 *  23 'Zelda BOTW'             Nolan(5)       Jeu vidéo
 *  24 'Breaking Bad S1'        Spielberg(6)   Série TV
 *  25 'Catan'                  Tolkien(11)    Jeu de société
 *  26 'Tenet'                  Nolan(5)       Film
 *  27 'The Batman'             Nolan(5)       Film
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private DocumentService documentService;
    @Autowired private AuteurService auteurService;
    @Autowired private FormatService formatService;
    @Autowired private EditeurService editeurService;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/document" + path;
    }

    /** Helper : GET une liste typée avec status + body. */
    private ResponseEntity<List<Document>> getDocuments(String path) {
        return restTemplate.exchange(
                url(path),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Document>>() {});
    }

    /** Helper : GET un seul document avec status + body. */
    private ResponseEntity<Document> getDocument(String path) {
        return restTemplate.getForEntity(url(path), Document.class);
    }

    // =========================================================================
    // GET /biblio/document/
    // =========================================================================

    @Test
    void testGetAllDocuments_retourne200() {
        ResponseEntity<List<Document>> response = getDocuments("/");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllDocuments_contientAuMoins27Documents() {
        List<Document> docs = getDocuments("/").getBody();
        assertThat(docs).hasSizeGreaterThanOrEqualTo(27);
    }

    @Test
    void testGetAllDocuments_premierEst1984() {
        List<Document> docs = getDocuments("/").getBody();
        assertThat(docs.get(0).getId()).isEqualTo(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testGetAllDocuments_auteurDuPremierEstOrwell() {
        List<Document> docs = getDocuments("/").getBody();
        assertThat(docs.get(0).getAuteur().getNom()).isEqualTo("Orwell");
        assertThat(docs.get(0).getAuteur().getId()).isEqualTo(1);
    }

    @Test
    void testGetAllDocuments_triParIdAscendant() {
        List<Document> docs = getDocuments("/").getBody();
        for (int i = 0; i < docs.size() - 1; i++) {
            assertThat(docs.get(i).getId()).isLessThan(docs.get(i + 1).getId());
        }
    }

    // =========================================================================
    // GET /biblio/document/{id}
    // =========================================================================

    @Test
    void testGetDocumentById1_retourne200() {
        ResponseEntity<Document> response = getDocument("/1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetDocumentById1_est1984() {
        Document doc = getDocument("/1").getBody();
        assertThat(doc.getId()).isEqualTo(1);
        assertThat(doc.getTitre()).isEqualTo("1984");
        assertThat(doc.getCodeIsbn()).isEqualTo("9782070368228");
        assertThat(doc.getCodeEmplacement()).isEqualTo("A1-001");
        assertThat(doc.getDatePublication()).isEqualTo(LocalDate.of(1949, 6, 8));
        assertThat(doc.getAuteur().getNom()).isEqualTo("Orwell");
        assertThat(doc.getEditeur().getNom()).isEqualTo("J'ai lu");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetDocumentById3_estEtranger() {
        Document doc = getDocument("/3").getBody();
        assertThat(doc.getTitre()).isEqualTo("L'Étranger");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Camus");
        assertThat(doc.getEditeur().getNom()).isEqualTo("Gallimard");
        assertThat(doc.getCodeEmplacement()).isEqualTo("A1-003");
    }

    @Test
    void testGetDocumentById9_estSongsOfCohen() {
        Document doc = getDocument("/9").getBody();
        assertThat(doc.getTitre()).isEqualTo("Songs of Leonard Cohen");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Cohen");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Album");
        assertThat(doc.getCodeIsbn()).isNull();
    }

    @Test
    void testGetDocumentById13_estInception() {
        Document doc = getDocument("/13").getBody();
        assertThat(doc.getTitre()).isEqualTo("Inception");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Film");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Nolan");
        assertThat(doc.getDatePublication()).isEqualTo(LocalDate.of(2010, 7, 16));
    }

    @Test
    void testGetDocumentById_idInexistantRetourne404() {
        ResponseEntity<Document> response = getDocument("/99999");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // GET /biblio/document/auteur/{auteurId}
    // =========================================================================

    @Test
    void testGetDocumentsByAuteurId1_retourne200() {
        ResponseEntity<List<Document>> response = getDocuments("/auteur/1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetDocumentsByAuteurId1_retourne2DocsOrwell() {
        List<Document> docs = getDocuments("/auteur/1").getBody();
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("1984", "La Ferme des animaux");
    }

    @Test
    void testGetDocumentsByAuteurId2_retourne2DocsCamus() {
        List<Document> docs = getDocuments("/auteur/2").getBody();
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("L'Étranger", "La Peste");
    }

    @Test
    void testGetDocumentsByAuteurId5_retourneAuMoins4Nolan() {
        List<Document> docs = getDocuments("/auteur/5").getBody();
        assertThat(docs).hasSizeGreaterThanOrEqualTo(4);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getId()).isEqualTo(5));
    }

    @Test
    void testGetDocumentsByAuteurId8_retourne2DocsCohen() {
        List<Document> docs = getDocuments("/auteur/8").getBody();
        assertThat(docs).hasSize(2);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getNom()).isEqualTo("Cohen"));
    }

    @Test
    void testGetDocumentsByAuteurId_auteurInexistantRetourneListeVide() {
        List<Document> docs = getDocuments("/auteur/99999").getBody();
        assertThat(docs).isEmpty();
    }

    // =========================================================================
    // GET /biblio/document/search?titre=
    // =========================================================================

    @Test
    void testSearchByTitre_1984_retourne200EtUnDoc() {
        ResponseEntity<List<Document>> response = getDocuments("/search?titre=1984");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Document> docs = response.getBody();
        assertThat(docs).hasSize(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testSearchByTitre_Interstellar_retourneDeuxDocs() {
        List<Document> docs = getDocuments("/search?titre=Interstellar").getBody();
        assertThat(docs).hasSizeGreaterThanOrEqualTo(2);
        assertThat(docs).allSatisfy(d ->
                assertThat(d.getTitre()).containsIgnoringCase("Interstellar"));
    }

    @Test
    void testSearchByTitre_insensibleCasse() {
        List<Document> lower = getDocuments("/search?titre=dune").getBody();
        List<Document> upper = getDocuments("/search?titre=DUNE").getBody();
        assertThat(lower).hasSameSizeAs(upper);
        assertThat(lower).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testSearchByTitre_Cohen_retourneUnAlbum() {
        List<Document> docs = getDocuments("/search?titre=Cohen").getBody();
        assertThat(docs).hasSize(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("Songs of Leonard Cohen");
    }

    @Test
    void testSearchByTitre_titreInexistantRetourneListeVide() {
        List<Document> docs = getDocuments("/search?titre=ZZZINEXISTANT999").getBody();
        assertThat(docs).isEmpty();
    }

    // =========================================================================
    // POST /biblio/document
    // =========================================================================

    @Test
    @Transactional
    void testSaveDocument_retourne200EtIdGenere() {
        ResponseEntity<Document> response = restTemplate.postForEntity(
                url(""), buildDocument("TitreTest"), Document.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @Transactional
    void testSaveDocument_champsCorrects() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("MonNouveauLivre"), Document.class).getBody();

        assertThat(saved.getTitre()).isEqualTo("MonNouveauLivre");
        assertThat(saved.getAuteur().getId()).isEqualTo(1);
        assertThat(saved.getAuteur().getNom()).isEqualTo("Orwell");
        assertThat(saved.getEditeur().getNom()).isEqualTo("Gallimard");
    }

    @Test
    @Transactional
    void testSaveDocument_retrouvableParGetId() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("LivreRetrouvable"), Document.class).getBody();

        Document found = getDocument("/" + saved.getId()).getBody();
        assertThat(found.getTitre()).isEqualTo("LivreRetrouvable");
    }

    // =========================================================================
    // PUT /biblio/document/{id}
    // =========================================================================

    @Test
    @Transactional
    void testUpdateDocument_titreMisAJour() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("TitreAncien"), Document.class).getBody();

        saved.setTitre("TitreNouveau");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = getDocument("/" + saved.getId()).getBody();
        assertThat(updated.getTitre()).isEqualTo("TitreNouveau");
    }

    @Test
    @Transactional
    void testUpdateDocument_isbnMisAJour() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("LivreISBN"), Document.class).getBody();

        saved.setCodeIsbn("9999999999999");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = getDocument("/" + saved.getId()).getBody();
        assertThat(updated.getCodeIsbn()).isEqualTo("9999999999999");
    }

    @Test
    @Transactional
    void testUpdateDocument_idInchangeApresUpdate() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("TitreStable"), Document.class).getBody();
        Integer originalId = saved.getId();

        saved.setTitre("TitreStableModifie");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = getDocument("/" + originalId).getBody();
        assertThat(updated.getId()).isEqualTo(originalId);
    }

    // =========================================================================
    // DELETE /biblio/document/{id}
    // =========================================================================

    @Test
    @Transactional
    void testDeleteDocumentById_retourne200() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("TitreDelete200"), Document.class).getBody();

        ResponseEntity<String> response = restTemplate.exchange(
                url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void testDeleteDocumentById_documentPlusExistantViaService() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("TitreDelete"), Document.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(documentService.getDocumentById(saved.getId())).isNull();
    }

    @Test
    @Transactional
    void testDeleteDocumentById_retourne404ApresSuppression() {
        Document saved = restTemplate.postForEntity(
                url(""), buildDocument("TitreDelete404"), Document.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        ResponseEntity<Document> response = getDocument("/" + saved.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Document minimal valide : Orwell(1), format poche(1), Gallimard(1).
     * Ces trois IDs sont garantis dans le seed.
     */
    private Document buildDocument(String titre) {
        Document doc = new Document();
        doc.setTitre(titre);
        doc.setAuteur(auteurService.getAuteurById(1));
        doc.setFormat(formatService.getFormatById(1));
        doc.setEditeur(editeurService.getEditeurById(1));
        return doc;
    }
}
