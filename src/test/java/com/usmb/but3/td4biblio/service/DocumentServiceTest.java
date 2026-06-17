package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Document;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

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

    @Autowired
    private BibliothequeService bibliothequeService;

    @Test
    void testGetAllDocuments_premierEst1984() {
        List<Document> docs = documentService.getAllDocuments();
        assertThat(docs.get(0).getId()).isEqualTo(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testGetAllDocuments_tousOntUnAuteur() {
        List<Document> docs = documentService.getAllDocuments();
        assertThat(docs).allSatisfy(d -> assertThat(d.getAuteur()).isNotNull());
    }

    @Test
    void testGetDocumentById1_est1984() {
        Document doc = documentService.getDocumentById(1);
        assertThat(doc).isNotNull();
        assertThat(doc.getTitre()).isEqualTo("1984");
        assertThat(doc.getAuteur().getNom()).isEqualTo("Orwell");
    }

    @Test
    void testGetDocumentById_idInexistantRetourneNull() {
        assertThat(documentService.getDocumentById(99999)).isNull();
    }

    @Test
    @Transactional
    void testSaveDocument() {
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
    void testDeleteDocumentById() {
        Integer id = documentService.saveDocument(buildDocument("TitreDelete")).getId();
        documentService.deleteDocumentById(id);
        assertThat(documentService.getDocumentById(id)).isNull();
    }

    @Test
    void testGetByTitre() {
        List<Document> docs = documentService.getByTitreContainingIgnoreCase("1984");
        assertThat(docs).hasSize(1);
        assertThat(docs.get(0).getTitre()).isEqualTo("1984");
    }

    @Test
    void testGetByTitre_IgnoreCase() {
        List<Document> lower = documentService.getByTitreContainingIgnoreCase("peste");
        List<Document> upper = documentService.getByTitreContainingIgnoreCase("PESTE");
        assertThat(lower).hasSameSizeAs(upper);
    }

    @Test
    void testGetByTitre_inexistant() {
        assertThat(documentService.getByTitreContainingIgnoreCase("ZZZZINEXISTANT")).isEmpty();
    }

    @Test
    void testGetByAuteurId1_retourne2DocumentsOrwell() {
        List<Document> docs = documentService.getByAuteurId(1);
        assertThat(docs).hasSize(2);
        assertThat(docs).extracting(Document::getTitre)
                .containsExactlyInAnyOrder("1984", "La Ferme des animaux");
    }

    @Test
    void testGetByAuteurId_auteurInexistantRetourneVide() {
        assertThat(documentService.getByAuteurId(99999)).isEmpty();
    }

    @Test
    void testGetDocumentsDisponibles_contient1984() {
        List<Document> disponibles = documentService.getDocumentsDisponibles();
        assertThat(disponibles).anySatisfy(d -> assertThat(d.getId()).isEqualTo(1));
    }

    private Document buildDocument(String titre) {
        Document doc = new Document();
        doc.setTitre(titre);
        doc.setAuteur(auteurService.getAuteurById(1));    // Orwell
        doc.setFormat(formatService.getFormatById(1));    // Livre poche
        doc.setEditeur(editeurService.getEditeurById(1)); // Gallimard
        doc.setFormat(formatService.getFormatById(1));
        doc.setBibliotheque(bibliothequeService.getBibliothequeById(1));
        return doc;
    }
}