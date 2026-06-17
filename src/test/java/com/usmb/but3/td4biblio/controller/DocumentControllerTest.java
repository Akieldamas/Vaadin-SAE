package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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

/**
 * Tests d'intégration pour DocumentController.
 *
 * Extrait du seed (documents triés par id ASC) :
 *  id=1  '1984'                      auteur=1 (Orwell)    format=1 editeur=3  type=1 Livre    bib=1
 *  id=2  'La Ferme des animaux'      auteur=1 (Orwell)    format=1 editeur=3  type=1 Livre    bib=1
 *  id=3  'L'Étranger'               auteur=2 (Camus)     format=1 editeur=1  type=1 Livre    bib=1
 *  id=4  'La Peste'                  auteur=2 (Camus)     format=2 editeur=1  type=1 Livre    bib=1
 *  id=5  'La Main gauche de la nuit' auteur=3 (Le Guin)   format=1 editeur=1  type=1 Livre    bib=2
 *  id=6  'Ça'                        auteur=4 (King)      format=2 editeur=3  type=1 Livre    bib=2
 *  id=7  'Le Seigneur des Anneaux'   auteur=11 (Tolkien)  format=2 editeur=2  type=1 Livre    bib=2
 *  id=8  'L'Amant'                   auteur=10 (Duras)    format=1 editeur=2  type=1 Livre    bib=2
 *  id=9  'Songs of Leonard Cohen'    auteur=8  (Cohen)    format=3 editeur=4  type=5 Album    bib=1
 *  id=10 'I'm Your Man'              auteur=8  (Cohen)    format=3 editeur=4  type=5 Album    bib=1
 *  id=11 'Interstellar — BO'         auteur=7  (Zimmer)   format=3 editeur=4  type=5 Album    bib=2
 *  id=12 'Dune — BO'                 auteur=7  (Zimmer)   format=3 editeur=4  type=5 Album    bib=2
 *  id=13 'Inception'                 auteur=5  (Nolan)    format=4 editeur=5  type=2 Film     bib=1
 *  id=14 'E.T.'                      auteur=6  (Spielberg)format=4 editeur=6  type=2 Film     bib=1
 *  id=15 'Le Voyage de Chihiro'      auteur=9  (Miyazaki) format=4 editeur=6  type=2 Film     bib=1
 *  id=16 'Dune (2021)'               auteur=12 (Villeneuve)format=4 editeur=5 type=2 Film     bib=1
 *  id=17 'Interstellar — Blu-ray'    auteur=5  (Nolan)    format=5 editeur=5  type=2 Film     bib=2
 *  ...
 *  ISBN '1984' : 9782070368228
 *
 *  Emprunts en cours (document_id non rendu) :
 *    doc 13 (Inception), doc 17 (Interstellar BR), doc 11 (BO Interstellar CD),
 *    doc 18 (Château BR), doc 16 (Dune DVD), doc 15 (Chihiro — attendez, rendu le 29/03)
 *  Nota : doc 15 Chihiro rendu, doc 16 Dune non rendu → disponibles ≠ empruntés
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private FormatService formatService;

    @Autowired
    private EditeurService editeurService;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/document" + path;
    }

    // =========================================================================
    // GET /biblio/document/
    // =========================================================================

    @Test
    void testGetAllDocuments_retourneListeNonVide() {
        Document[] docs = restTemplate.getForObject(url("/"), Document[].class);
        assertThat(docs).isNotEmpty();
    }

    @Test
    void testGetAllDocuments_contientAuMoins27Documents() {
        Document[] docs = restTemplate.getForObject(url("/"), Document[].class);
        assertThat(docs).hasSizeGreaterThanOrEqualTo(27);
    }

    @Test
    void testGetAllDocuments_premierDocumentEst1984() {
        Document[] docs = restTemplate.getForObject(url("/"), Document[].class);
        assertThat(docs[0].getTitre()).isEqualTo("1984");
        assertThat(docs[0].getId()).isEqualTo(1);
    }

    @Test
    void testGetAllDocuments_auteurDuPremierEstOrwell() {
        Document[] docs = restTemplate.getForObject(url("/"), Document[].class);
        assertThat(docs[0].getAuteur().getNom()).isEqualTo("Orwell");
        assertThat(docs[0].getAuteur().getId()).isEqualTo(1);
    }

    @Test
    void testGetAllDocuments_triParIdAscendant() {
        Document[] docs = restTemplate.getForObject(url("/"), Document[].class);
        for (int i = 0; i < docs.length - 1; i++) {
            assertThat(docs[i].getId()).isLessThan(docs[i + 1].getId());
        }
    }

    // =========================================================================
    // GET /biblio/document/{id}
    // =========================================================================

    @Test
    void testGetDocumentById1_est1984() {
        Document doc = restTemplate.getForObject(url("/1"), Document.class);
        assertThat(doc).isNotNull();
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
        Document doc = restTemplate.getForObject(url("/3"), Document.class);
        assertThat(doc.getTitre()).isEqualTo("L'Étranger");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Camus");
        assertThat(doc.getEditeur().getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetDocumentById13_estInception() {
        Document doc = restTemplate.getForObject(url("/13"), Document.class);
        assertThat(doc.getTitre()).isEqualTo("Inception");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Nolan");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Film");
        assertThat(doc.getCodeIsbn()).isNull();
    }

    @Test
    void testGetDocumentById9_estSongsOfCohen() {
        Document doc = restTemplate.getForObject(url("/9"), Document.class);
        assertThat(doc.getTitre()).isEqualTo("Songs of Leonard Cohen");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Cohen");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Album");
    }

    @Test
    void testGetDocumentById_idInexistantRetourne404() {
        ResponseEntity<Document> response = restTemplate.getForEntity(url("/99999"), Document.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // GET /biblio/document/auteur/{auteurId}
    // =========================================================================

    @Test
    void testGetDocumentsByAuteurId1_retourneOrwell() {
        // Orwell (id=1) a 2 documents : 1984 et La Ferme des animaux
        Document[] docs = restTemplate.getForObject(url("/auteur/1"), Document[].class);
        assertThat(docs).hasSize(2);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getNom()).isEqualTo("Orwell"));
    }

    @Test
    void testGetDocumentsByAuteurId1_titulairesSontCorrects() {
        Document[] docs = restTemplate.getForObject(url("/auteur/1"), Document[].class);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("1984", "La Ferme des animaux");
    }

    @Test
    void testGetDocumentsByAuteurId2_retourneCamus() {
        // Camus (id=2) : L'Étranger + La Peste
        Document[] docs = restTemplate.getForObject(url("/auteur/2"), Document[].class);
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("L'Étranger", "La Peste");
    }

    @Test
    void testGetDocumentsByAuteurId8_retourneCohen() {
        // Cohen (id=8) : Songs of Leonard Cohen + I'm Your Man
        Document[] docs = restTemplate.getForObject(url("/auteur/8"), Document[].class);
        assertThat(docs).hasSize(2);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getNom()).isEqualTo("Cohen"));
    }

    @Test
    void testGetDocumentsByAuteurId5_retourneNolan() {
        // Nolan (id=5) : Inception, Interstellar Blu-ray, Tenet, The Batman → 4 docs
        Document[] docs = restTemplate.getForObject(url("/auteur/5"), Document[].class);
        assertThat(docs).hasSizeGreaterThanOrEqualTo(4);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getId()).isEqualTo(5));
    }

    @Test
    void testGetDocumentsByAuteurId_auteurInexistantRetourneVide() {
        Document[] docs = restTemplate.getForObject(url("/auteur/99999"), Document[].class);
        assertThat(docs).isEmpty();
    }

    // =========================================================================
    // GET /biblio/document/search?titre=
    // =========================================================================

    @Test
    void testSearchByTitre_1984_retourne1984() {
        Document[] docs = restTemplate.getForObject(url("/search?titre=1984"), Document[].class);
        assertThat(docs).hasSize(1);
        assertThat(docs[0].getTitre()).isEqualTo("1984");
    }

    @Test
    void testSearchByTitre_Interstellar_retourneDeuxDocs() {
        // 'Interstellar — Blu-ray' et 'Interstellar — Bande originale'
        Document[] docs = restTemplate.getForObject(url("/search?titre=Interstellar"), Document[].class);
        assertThat(docs).hasSizeGreaterThanOrEqualTo(2);
        assertThat(docs).allSatisfy(d ->
                assertThat(d.getTitre()).containsIgnoringCase("Interstellar"));
    }

    @Test
    void testSearchByTitre_insensibleCasse_MajMinuscule() {
        Document[] upper = restTemplate.getForObject(url("/search?titre=DUNE"), Document[].class);
        Document[] lower = restTemplate.getForObject(url("/search?titre=dune"), Document[].class);
        assertThat(upper).hasSameSizeAs(lower);
        assertThat(upper).hasSizeGreaterThanOrEqualTo(2); // Dune DVD + BO Dune
    }

    @Test
    void testSearchByTitre_Cohen_retourneAlbumsCohen() {
        Document[] docs = restTemplate.getForObject(url("/search?titre=Cohen"), Document[].class);
        assertThat(docs).hasSize(1);
        assertThat(docs[0].getTitre()).isEqualTo("Songs of Leonard Cohen");
    }

    @Test
    void testSearchByTitre_titreInexistantRetourneVide() {
        Document[] docs = restTemplate.getForObject(
                url("/search?titre=ZZZINEXISTANT999"), Document[].class);
        assertThat(docs).isEmpty();
    }

    // =========================================================================
    // POST /biblio/document
    // =========================================================================

    @Test
    @Transactional
    void testSaveDocument_idGenere() {
        Document doc = buildDocument("TitreSave");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);
        assertThat(saved.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @Transactional
    void testSaveDocument_titreCorrect() {
        Document doc = buildDocument("MonNouveauLivre");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);
        assertThat(saved.getTitre()).isEqualTo("MonNouveauLivre");
    }

    @Test
    @Transactional
    void testSaveDocument_auteurOrwell() {
        Document doc = buildDocument("LivreOrwell");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);
        assertThat(saved.getAuteur().getId()).isEqualTo(1);
        assertThat(saved.getAuteur().getNom()).isEqualTo("Orwell");
    }

    @Test
    @Transactional
    void testSaveDocument_retrouvableParId() {
        Document doc = buildDocument("LivreRetrouvable");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);
        Document found = restTemplate.getForObject(url("/" + saved.getId()), Document.class);
        assertThat(found.getTitre()).isEqualTo("LivreRetrouvable");
    }

    // =========================================================================
    // PUT /biblio/document/{id}
    // =========================================================================

    @Test
    @Transactional
    void testUpdateDocument_titreMisAJour() {
        Document doc = buildDocument("TitreAncien");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);

        saved.setTitre("TitreNouveau");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = restTemplate.getForObject(url("/" + saved.getId()), Document.class);
        assertThat(updated.getTitre()).isEqualTo("TitreNouveau");
    }

    @Test
    @Transactional
    void testUpdateDocument_isbnMisAJour() {
        Document doc = buildDocument("LivreISBN");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);

        saved.setCodeIsbn("9999999999999");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = restTemplate.getForObject(url("/" + saved.getId()), Document.class);
        assertThat(updated.getCodeIsbn()).isEqualTo("9999999999999");
    }

    @Test
    @Transactional
    void testUpdateDocument_idInchangeApresUpdate() {
        Document doc = buildDocument("TitreStable");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);
        Integer originalId = saved.getId();

        saved.setTitre("TitreStableModifie");
        restTemplate.put(url("/" + saved.getId()), saved);

        Document updated = restTemplate.getForObject(url("/" + originalId), Document.class);
        assertThat(updated.getId()).isEqualTo(originalId);
    }

    // =========================================================================
    // DELETE /biblio/document/{id}
    // =========================================================================

    @Test
    @Transactional
    void testDeleteDocumentById_documentPlusExistant() {
        Document doc = buildDocument("TitreDelete");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(documentService.getDocumentById(saved.getId())).isNull();
    }

    @Test
    @Transactional
    void testDeleteDocumentById_retourne200() {
        Document doc = buildDocument("TitreDelete200");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);

        ResponseEntity<String> response = restTemplate.exchange(
                url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void testDeleteDocumentById_retourne404Apres() {
        Document doc = buildDocument("TitreDelete404");
        Document saved = restTemplate.postForObject(url(""), doc, Document.class);

        restTemplate.delete(url("/" + saved.getId()));

        ResponseEntity<Document> response = restTemplate.getForEntity(
                url("/" + saved.getId()), Document.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Construit un Document minimal valide avec Orwell (id=1), format poche (id=1),
     * éditeur Gallimard (id=1). Ces trois valeurs sont garanties dans le seed.
     */
    private Document buildDocument(String titre) {
        Document doc = new Document();
        doc.setTitre(titre);
        doc.setAuteur(auteurService.getAuteurById(1));       // Orwell
        doc.setFormat(formatService.getFormatById(1));       // Livre poche
        doc.setEditeur(editeurService.getEditeurById(1));    // Gallimard
        return doc;
    }
}