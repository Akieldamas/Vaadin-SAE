package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.GenreDocument;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * Seed genres :
 *  1 Roman          2 Science-fiction  3 Thriller     4 Fantasy
 *  5 Documentaire   6 Biographie       7 Musique classique  8 Rock/Pop
 *  9 Bande originale 10 Drame          11 Action/Aventure   12 Horreur
 */
@SpringBootTest
public class GenreDocumentServiceTest {

    @Autowired
    private GenreDocumentService genreDocumentService;

    // =========================================================================
    // getAllGenres
    // =========================================================================

    @Test
    void testGetAllGenres_retourne12Genres() {
        List<GenreDocument> genres = genreDocumentService.getAllGenres();
        assertThat(genres).hasSizeGreaterThanOrEqualTo(12);
    }

    @Test
    void testGetAllGenres_premierEstRoman() {
        List<GenreDocument> genres = genreDocumentService.getAllGenres();
        assertThat(genres.get(0).getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetAllGenres_triParIdAscendant() {
        List<GenreDocument> genres = genreDocumentService.getAllGenres();
        for (int i = 0; i < genres.size() - 1; i++) {
            assertThat(genres.get(i).getId()).isLessThan(genres.get(i + 1).getId());
        }
    }

    // =========================================================================
    // getGenreById
    // =========================================================================

    @Test
    void testGetGenreById1_estRoman() {
        GenreDocument g = genreDocumentService.getGenreById(1);
        assertThat(g).isNotNull();
        assertThat(g.getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetGenreById2_estScienceFiction() {
        GenreDocument g = genreDocumentService.getGenreById(2);
        assertThat(g.getNom()).isEqualTo("Science-fiction");
    }

    @Test
    void testGetGenreById4_estFantasy() {
        GenreDocument g = genreDocumentService.getGenreById(4);
        assertThat(g.getNom()).isEqualTo("Fantasy");
    }

    @Test
    void testGetGenreById9_estBandeOriginale() {
        GenreDocument g = genreDocumentService.getGenreById(9);
        assertThat(g.getNom()).isEqualTo("Bande originale");
    }

    @Test
    void testGetGenreById12_estHorreur() {
        GenreDocument g = genreDocumentService.getGenreById(12);
        assertThat(g.getNom()).isEqualTo("Horreur");
    }

    @Test
    void testGetGenreById_idInexistantRetourneNull() {
        assertThat(genreDocumentService.getGenreById(99999)).isNull();
    }

    // =========================================================================
    // getGenreByNom
    // =========================================================================

    @Test
    void testGetGenreByNom_Roman_retourneId1() {
        GenreDocument g = genreDocumentService.getGenreByNom("Roman");
        assertThat(g).isNotNull();
        assertThat(g.getId()).isEqualTo(1);
    }

    @Test
    void testGetGenreByNom_Fantasy_retourneId4() {
        GenreDocument g = genreDocumentService.getGenreByNom("Fantasy");
        assertThat(g).isNotNull();
        assertThat(g.getId()).isEqualTo(4);
    }

    @Test
    void testGetGenreByNom_Horreur_retourneId12() {
        GenreDocument g = genreDocumentService.getGenreByNom("Horreur");
        assertThat(g).isNotNull();
        assertThat(g.getId()).isEqualTo(12);
    }

    @Test
    void testGetGenreByNom_inexistantRetourneNull() {
        assertThat(genreDocumentService.getGenreByNom("GenreInexistant999")).isNull();
    }

    // =========================================================================
    // saveGenre
    // =========================================================================

    @Test
    @Transactional
    void testSaveGenre_idGenereApres12() {
        GenreDocument g = new GenreDocument();
        g.setNom("Polar Test");
        GenreDocument saved = genreDocumentService.saveGenre(g);
        assertThat(saved.getId()).isNotNull().isGreaterThan(12);
    }

    @Test
    @Transactional
    void testSaveGenre_nomPersiste() {
        GenreDocument g = new GenreDocument();
        g.setNom("Manga");
        GenreDocument saved = genreDocumentService.saveGenre(g);
        assertThat(genreDocumentService.getGenreById(saved.getId()).getNom()).isEqualTo("Manga");
    }

    // =========================================================================
    // updateGenre
    // =========================================================================

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

    // =========================================================================
    // deleteGenreById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteGenreById_genrePlusPresent() {
        GenreDocument g = new GenreDocument();
        g.setNom("GenreASupprimer");
        Integer id = genreDocumentService.saveGenre(g).getId();
        genreDocumentService.deleteGenreById(id);
        assertThat(genreDocumentService.getGenreById(id)).isNull();
    }
}