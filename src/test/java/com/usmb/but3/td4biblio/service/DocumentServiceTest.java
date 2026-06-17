package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

/**
 * Seed : 27 documents min (id 1-27), répartis en Livres, Films, Albums, etc.
 *   id=1  '1984'              auteur=1 Orwell   Livre  isbn=9782070368228
 *   id=2  'La Ferme…'         auteur=1 Orwell   Livre
 *   id=3  'L'Étranger'        auteur=2 Camus    Livre  editeur=1 Gallimard
 *   id=4  'La Peste'          auteur=2 Camus    Livre  format=2
 *   id=5  'La Main gauche…'   auteur=3 Le Guin  Livre
 *   id=6  'Ça'                auteur=4 King     Livre
 *   id=7  'Le Seigneur…'      auteur=11 Tolkien Livre
 *   id=8  'L'Amant'           auteur=10 Duras   Livre
 *   id=9  'Songs of Cohen'    auteur=8 Cohen    Album  type=5
 *   id=10 'I'm Your Man'      auteur=8 Cohen    Album
 *   id=11 'BO Interstellar'   auteur=7 Zimmer   Album
 *   id=12 'BO Dune'           auteur=7 Zimmer   Album
 *   id=13 'Inception'         auteur=5 Nolan    Film   type=2
 *   ...
 *   id=26 'Tenet'             auteur=5 Nolan    Film
 *   id=27 'The Batman'        auteur=5 Nolan    Film
 *
 *  Docs NON RENDUS (emprunts en cours) : 11, 13, 16, 17, 18 → non disponibles
 */
@SpringBootTest
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private FormatService formatService;

    @Autowired
    private EditeurService editeurService;

    // =========================================================================
    // getAllDocuments
    // =========================================================================

    @Test
    void testGetAllDocuments_retourneAuMoins27() {
        List<Document> docs = documentService.getAllDocuments();
        assertThat(docs).hasSizeGreaterThanOrEqualTo(27);
    }

    @Test
    void testGetAllDocuments_premierEst1984() {
        List<Document> docs = documentService.getAllDocuments();
        assertThat(docs.get(0).getId()).isEqualTo(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testGetAllDocuments_triParIdAscendant() {
        List<Document> docs = documentService.getAllDocuments();
        for (int i = 0; i < docs.size() - 1; i++) {
            assertThat(docs.get(i).getId()).isLessThan(docs.get(i + 1).getId());
        }
    }

    @Test
    void testGetAllDocuments_tousOntUnAuteur() {
        List<Document> docs = documentService.getAllDocuments();
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur()).isNotNull());
    }

    // =========================================================================
    // getDocumentById
    // =========================================================================

    @Test
    void testGetDocumentById1_est1984() {
        Document doc = documentService.getDocumentById(1);
        assertThat(doc).isNotNull();
        assertThat(doc.getTitre()).isEqualTo("1984");
        assertThat(doc.getCodeIsbn()).isEqualTo("9782070368228");
        assertThat(doc.getCodeEmplacement()).isEqualTo("A1-001");
        assertThat(doc.getDatePublication()).isEqualTo(LocalDate.of(1949, 6, 8));
        assertThat(doc.getAuteur().getNom()).isEqualTo("Orwell");
    }

    @Test
    void testGetDocumentById3_estEtranger() {
        Document doc = documentService.getDocumentById(3);
        assertThat(doc.getTitre()).isEqualTo("L'Étranger");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Camus");
        assertThat(doc.getEditeur().getNom()).isEqualTo("Gallimard");
        assertThat(doc.getCodeEmplacement()).isEqualTo("A1-003");
    }

    @Test
    void testGetDocumentById9_estSongsOfCohen() {
        Document doc = documentService.getDocumentById(9);
        assertThat(doc.getTitre()).isEqualTo("Songs of Leonard Cohen");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Cohen");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Album");
        assertThat(doc.getCodeIsbn()).isNull();
    }

    @Test
    void testGetDocumentById13_estInception() {
        Document doc = documentService.getDocumentById(13);
        assertThat(doc.getTitre()).isEqualTo("Inception");
        assertThat(doc.getTypeDocument().getNom()).isEqualTo("Film");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Nolan");
        assertThat(doc.getDatePublication()).isEqualTo(LocalDate.of(2010, 7, 16));
    }

    @Test
    void testGetDocumentById_idInexistantRetourneNull() {
        assertThat(documentService.getDocumentById(99999)).isNull();
    }

    // =========================================================================
    // saveDocument
    // =========================================================================

    @Test
    @Transactional
    void testSaveDocument_idGenere() {
        Document saved = documentService.saveDocument(buildDocument("TitreTest"));
        assertThat(saved.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @Transactional
    void testSaveDocument_tousChampsPersistents() {
        Document saved = documentService.saveDocument(buildDocument("LivreComplet"));
        Document found = documentService.getDocumentById(saved.getId());

        assertThat(found.getTitre()).isEqualTo("LivreComplet");
        assertThat(found.getAuteur().getNom()).isEqualTo("Orwell");
        assertThat(found.getEditeur().getNom()).isEqualTo("Gallimard");
        assertThat(found.getFormat().getId()).isEqualTo(1);
    }

    // =========================================================================
    // updateDocument
    // =========================================================================

    @Test
    @Transactional
    void testUpdateDocument_titreMisAJour() {
        Document saved = documentService.saveDocument(buildDocument("AvantUpdate"));
        saved.setTitre("ApresUpdate");
        documentService.updateDocument(saved);
        assertThat(documentService.getDocumentById(saved.getId()).getTitre()).isEqualTo("ApresUpdate");
    }

    @Test
    @Transactional
    void testUpdateDocument_isbnMisAJour() {
        Document saved = documentService.saveDocument(buildDocument("LivreISBN"));
        saved.setCodeIsbn("1234567890123");
        documentService.updateDocument(saved);
        assertThat(documentService.getDocumentById(saved.getId()).getCodeIsbn()).isEqualTo("1234567890123");
    }

    // =========================================================================
    // deleteDocumentById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteDocumentById_documentPlusPresent() {
        Integer id = documentService.saveDocument(buildDocument("TitreDelete")).getId();
        documentService.deleteDocumentById(id);
        assertThat(documentService.getDocumentById(id)).isNull();
    }

    // =========================================================================
    // getByTitreContainingIgnoreCase
    // =========================================================================

    @Test
    void testGetByTitre_1984_retourne1984() {
        List<Document> docs = documentService.getByTitreContainingIgnoreCase("1984");
        assertThat(docs).hasSize(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testGetByTitre_Interstellar_retournePlusieursResultats() {
        List<Document> docs = documentService.getByTitreContainingIgnoreCase("Interstellar");
        assertThat(docs).hasSizeGreaterThanOrEqualTo(2); // Film BR + BO
        assertThat(docs).allSatisfy(d ->
                assertThat(d.getTitre()).containsIgnoringCase("Interstellar"));
    }

    @Test
    void testGetByTitre_dune_insensibleCasse() {
        List<Document> lower = documentService.getByTitreContainingIgnoreCase("dune");
        List<Document> upper = documentService.getByTitreContainingIgnoreCase("DUNE");
        assertThat(lower).hasSameSizeAs(upper);
        assertThat(lower).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetByTitre_Seigneur_retourneSDA() {
        List<Document> docs = documentService.getByTitreContainingIgnoreCase("Seigneur");
        assertThat(docs).hasSize(1);
        assertThat(docs.get(0).getTitre()).contains("Seigneur des Anneaux");
    }

    @Test
    void testGetByTitre_inexistantRetourneVide() {
        assertThat(documentService.getByTitreContainingIgnoreCase("ZZZZINEXISTANT999")).isEmpty();
    }

    // =========================================================================
    // getByAuteurId
    // =========================================================================

    @Test
    void testGetByAuteurId1_retourne2DocumentsOrwell() {
        List<Document> docs = documentService.getByAuteurId(1);
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("1984", "La Ferme des animaux");
    }

    @Test
    void testGetByAuteurId2_retourne2DocumentsCamus() {
        List<Document> docs = documentService.getByAuteurId(2);
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("L'Étranger", "La Peste");
    }

    @Test
    void testGetByAuteurId7_retourne2DocumentsZimmer() {
        // Zimmer : BO Interstellar + BO Dune
        List<Document> docs = documentService.getByAuteurId(7);
        assertThat(docs).hasSize(2);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getId()).isEqualTo(7));
    }

    @Test
    void testGetByAuteurId5_retourneAuMoins4Nolan() {
        // Nolan : Inception, Interstellar BR, Tenet, The Batman
        List<Document> docs = documentService.getByAuteurId(5);
        assertThat(docs).hasSizeGreaterThanOrEqualTo(4);
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur().getNom()).isEqualTo("Nolan"));
    }

    @Test
    void testGetByAuteurId_auteurInexistantRetourneVide() {
        assertThat(documentService.getByAuteurId(99999)).isEmpty();
    }

    // =========================================================================
    // getDocumentsDisponibles
    // =========================================================================

    @Test
    void testGetDocumentsDisponibles_retourneListeNonVide() {
        List<Document> disponibles = documentService.getDocumentsDisponibles();
        assertThat(disponibles).isNotEmpty();
    }

    @Test
    void testGetDocumentsDisponibles_neContientPasLesDocsEmpruntes() {
        // docs 13 (Inception), 17 (Interstellar BR), 11 (BO Interstellar),
        // 18 (Château BR), 16 (Dune DVD) sont empruntés et non rendus
        List<Document> disponibles = documentService.getDocumentsDisponibles();
        List<Integer> idsDisponibles = disponibles.stream()
                .map(Document::getId).toList();
        assertThat(idsDisponibles).doesNotContain(13, 17, 11, 18, 16);
    }

    @Test
    void testGetDocumentsDisponibles_contient1984() {
        // doc 1 (1984) rendu le 2024-01-24 → disponible
        List<Document> disponibles = documentService.getDocumentsDisponibles();
        assertThat(disponibles).anySatisfy(d -> assertThat(d.getId()).isEqualTo(1));
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Document buildDocument(String titre) {
        Document doc = new Document();
        doc.setTitre(titre);
        doc.setAuteur(auteurService.getAuteurById(1));    // Orwell
        doc.setFormat(formatService.getFormatById(1));    // Livre poche
        doc.setEditeur(editeurService.getEditeurById(1)); // Gallimard
        return doc;
    }
}