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

import com.usmb.but3.td4biblio.entity.GenreDocument;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GenreDocumentControllerMockTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/genredocument" + path;
    }

    private ResponseEntity<List<GenreDocument>> getGenres(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GenreDocument>>() {});
    }

    private ResponseEntity<GenreDocument> getGenre(String path) {
        return restTemplate.getForEntity(url(path), GenreDocument.class);
    }

    @Test
    void testGetAllGenres_retourne200() {
        assertThat(getGenres("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllGenres_premierEstRoman() {
        assertThat(getGenres("/").getBody().get(0).getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetGenreById1_estRoman() {
        assertThat(getGenre("/1").getBody().getNom()).isEqualTo("Roman");
    }

    @Test
    void testGetGenreById_idInexistantRetourne404() {
        assertThat(getGenre("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetGenreByNom_thriller_trouve() {
        GenreDocument g = restTemplate.getForEntity(url("/nom/Thriller"), GenreDocument.class).getBody();
        assertThat(g.getId()).isEqualTo(3);
    }

    @Test
    void testGetGenreByNom_introuvableRetourne404() {
        ResponseEntity<GenreDocument> response = restTemplate.getForEntity(url("/nom/NomInexistant999"), GenreDocument.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testSaveGenre_champsPersistent() {
        GenreDocument saved = restTemplate.postForEntity(
                url("/"), buildGenre("Nouveau Genre"), GenreDocument.class).getBody();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Nouveau Genre");
    }

    @Test
    @Transactional
    void testUpdateGenre_nomMisAJour() {
        GenreDocument saved = restTemplate.postForEntity(
                url("/"), buildGenre("AncienNom"), GenreDocument.class).getBody();

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        GenreDocument updated = getGenre("/" + saved.getId()).getBody();
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testDeleteGenreById_retourne404Apres() {
        GenreDocument saved = restTemplate.postForEntity(
                url("/"), buildGenre("ASupprimer"), GenreDocument.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getGenre("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private GenreDocument buildGenre(String nom) {
        GenreDocument g = new GenreDocument();
        g.setNom(nom);
        return g;
    }
}