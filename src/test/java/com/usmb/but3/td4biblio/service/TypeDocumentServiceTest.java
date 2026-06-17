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
        assertThat(types).hasSizeGreaterThanOrEqualTo(9);
    }

    @Test
    void testGetAllTypeDocuments_premierEstLivre() {
        List<TypeDocument> types = typeDocumentService.getAllTypeDocuments();
        assertThat(types.get(0).getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetAllTypeDocuments_triParIdAscendant() {
        List<TypeDocument> types = typeDocumentService.getAllTypeDocuments();
        for (int i = 0; i < types.size() - 1; i++) {
            assertThat(types.get(i).getId()).isLessThan(types.get(i + 1).getId());
        }
    }

    @Test
    void testGetTypeDocumentById1_estLivre() {
        TypeDocument t = typeDocumentService.getTypeDocumentById(1);
        assertThat(t).isNotNull();
        assertThat(t.getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetTypeDocumentById2_estFilm() {
        assertThat(typeDocumentService.getTypeDocumentById(2).getNom()).isEqualTo("Film");
    }

    @Test
    void testGetTypeDocumentById3_estMagasine() {
        assertThat(typeDocumentService.getTypeDocumentById(3).getNom()).isEqualTo("Magasine");
    }

    @Test
    void testGetTypeDocumentById5_estAlbum() {
        assertThat(typeDocumentService.getTypeDocumentById(5).getNom()).isEqualTo("Album");
    }

    @Test
    void testGetTypeDocumentById6_estBD() {
        assertThat(typeDocumentService.getTypeDocumentById(6).getNom()).isEqualTo("Bande dessinée");
    }

    @Test
    void testGetTypeDocumentById7_estJeuVideo() {
        assertThat(typeDocumentService.getTypeDocumentById(7).getNom()).isEqualTo("Jeu vidéo");
    }

    @Test
    void testGetTypeDocumentById9_estJeuSociete() {
        assertThat(typeDocumentService.getTypeDocumentById(9).getNom()).isEqualTo("Jeu de société");
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
    void testGetTypeDocumentByNom_Film_retourneId2() {
        TypeDocument t = typeDocumentService.getTypeDocumentByNom("Film");
        assertThat(t.getId()).isEqualTo(2);
    }

    @Test
    void testGetTypeDocumentByNom_Album_retourneId5() {
        TypeDocument t = typeDocumentService.getTypeDocumentByNom("Album");
        assertThat(t.getId()).isEqualTo(5);
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
    void testUpdateTypeDocument_nomMisAJour() {
        TypeDocument t = new TypeDocument();
        t.setNom("TypeAvant");
        TypeDocument saved = typeDocumentService.saveTypeDocument(t);
        saved.setNom("TypeApres");
        typeDocumentService.updaTypeDocument(saved);
        assertThat(typeDocumentService.getTypeDocumentById(saved.getId()).getNom()).isEqualTo("TypeApres");
    }

    @Test
    @Transactional
    void testDeleteTypeDocumentById_typePlusPresent() {
        TypeDocument t = new TypeDocument();
        t.setNom("TypeASupprimer");
        Integer id = typeDocumentService.saveTypeDocument(t).getId();
        typeDocumentService.deleteTypeDocumentById(id);
        assertThat(typeDocumentService.getTypeDocumentById(id)).isNull();
    }
}