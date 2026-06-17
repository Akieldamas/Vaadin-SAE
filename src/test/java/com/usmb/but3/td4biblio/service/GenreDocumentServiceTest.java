package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.GenreDocument;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class GenreDocumentServiceTest {

    @Autowired
    private GenreDocumentService genreDocumentService;


    @Test
    void testGetAllGenres_retourne12Genres() {
        List<GenreDocument> genres = genreDocumentService.getAllGenres();
        assertThat(genres).isNotEmpty();
    }

    @Test
    void testGetAllGenres_premierEstRoman() {
        List<GenreDocument> genres = genreDocumentService.getAllGenres();
        assertThat(genres.get(0).getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetGenreById1_estRoman() {
        GenreDocument g = genreDocumentService.getGenreById(1);
        assertThat(g).isNotNull();
        assertThat(g.getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetGenreById_idInexistantRetourneNull() {
        assertThat(genreDocumentService.getGenreById(99999)).isNull();
    }

    @Test
    void testGetGenreByNom_Roman_retourneId1() {
        GenreDocument g = genreDocumentService.getGenreByNom("Roman");
        assertThat(g).isNotNull();
        assertThat(g.getId()).isEqualTo(1);
    }

    @Test
    void testGetGenreByNom_inexistantRetourneNull() {
        assertThat(genreDocumentService.getGenreByNom("GenreInexistant999")).isNull();
    }

    @Test
    @Transactional
    void testSaveGenre_idGenereApres12() {
        GenreDocument g = new GenreDocument();
        g.setNom("GenreTest");
        GenreDocument saved = genreDocumentService.saveGenre(g);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @Transactional
    void testUpdateGenre_nomMisAJour() {
        GenreDocument g = new GenreDocument();
        g.setNom("GenreAvant");
        GenreDocument saved = genreDocumentService.saveGenre(g);
        saved.setNom("GenreApres");
        genreDocumentService.updateGenre(saved);
        assertThat(genreDocumentService.getGenreById(saved.getId()).getNom()).isEqualTo("GenreApres");
    }

    @Test
    @Transactional
    void testDeleteGenreById() {
        GenreDocument g = new GenreDocument();
        g.setNom("GenreASupprimer");
        int id = genreDocumentService.saveGenre(g).getId();
        genreDocumentService.deleteGenreById(id);
        assertThat(genreDocumentService.getGenreById(id)).isNull();
    }
}