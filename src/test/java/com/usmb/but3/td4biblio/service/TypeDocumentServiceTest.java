package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.TypeDocument;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * Seed type_document :
 *  1 Livre  2 Film  3 Magasine  4 Journaux  5 Album
 *  6 Bande dessinée  7 Jeu vidéo  8 Série TV  9 Jeu de société
 */
@SpringBootTest
public class TypeDocumentServiceTest {

    @Autowired
    private TypeDocumentService typeDocumentService;

    @Test
    void testGetAllTypeDocuments_retourne9Types() {
        List<TypeDocument> types = typeDocumentService.getAllTypeDocuments();
        assertThat(types).isNotEmpty();
    }

    @Test
    void testGetTypeDocumentById1_estLivre() {
        TypeDocument t = typeDocumentService.getTypeDocumentById(1);
        assertThat(t).isNotNull();
        assertThat(t.getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetTypeDocumentById_idInexistantRetourneNull() {
        assertThat(typeDocumentService.getTypeDocumentById(99999)).isNull();
    }

    @Test
    void testGetTypeDocumentByNom_Livre_retourneId1() {
        TypeDocument t = typeDocumentService.getTypeDocumentByNom("Livre");
        assertThat(t).isNotNull();
        assertThat(t.getId()).isEqualTo(1);
    }

    @Test
    void testGetTypeDocumentByNom_inexistantRetourneNull() {
        assertThat(typeDocumentService.getTypeDocumentByNom("TypeInexistant999")).isNull();
    }

    @Test
    @Transactional
    void testSaveTypeDocument_idGenereApres9() {
        TypeDocument t = new TypeDocument();
        t.setNom("Podcast");
        TypeDocument saved = typeDocumentService.saveTypeDocument(t);
        assertThat(saved.getId()).isNotNull().isGreaterThan(9);
        assertThat(saved.getNom()).isEqualTo("Podcast");
    }

    @Test
    @Transactional
    void testUpdateTypeDocument() {
        TypeDocument t = new TypeDocument();
        t.setNom("TypeAvant");
        TypeDocument saved = typeDocumentService.saveTypeDocument(t);
        saved.setNom("TypeApres");
        typeDocumentService.updateTypeDocument(saved);
        assertThat(typeDocumentService.getTypeDocumentById(saved.getId()).getNom()).isEqualTo("TypeApres");
    }

    @Test
    @Transactional
    void testDeleteTypeDocumentById() {
        TypeDocument t = new TypeDocument();
        t.setNom("TypeASupprimer");
        Integer id = typeDocumentService.saveTypeDocument(t).getId();
        typeDocumentService.deleteTypeDocumentById(id);
        assertThat(typeDocumentService.getTypeDocumentById(id)).isNull();
    }
}