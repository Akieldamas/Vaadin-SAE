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

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EditeurControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private EditeurService editeurService;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/editeur" + path;
    }

    private ResponseEntity<List<Editeur>> getEditeurs(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Editeur>>() {});
    }

    private ResponseEntity<Editeur> getEditeur(String path) {
        return restTemplate.getForEntity(url(path), Editeur.class);
    }

    @Test
    void testGetAllEditeurs_retourne200() {
        assertThat(getEditeurs("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllEditeurs_premierEstGallimard() {
        assertThat(getEditeurs("/").getBody().get(0).getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetEditeurById1_retourne200() {
        assertThat(getEditeur("/1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetEditeurById1_estGallimard() {
        Editeur e = getEditeur("/1").getBody();
        assertThat(e.getNom()).isEqualTo("Gallimard");
    }

    @Test
    void testGetEditeurById_idInexistantRetourne404() {
        assertThat(getEditeur("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testSaveEditeur() {
        ResponseEntity<Editeur> response = restTemplate.postForEntity(
                url("/"), buildEditeur("Nouvel Editeur"), Editeur.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void testUpdateEditeur_nomMisAJour() {
        Editeur saved = restTemplate.postForEntity(
                url("/"), buildEditeur("AncienNom"), Editeur.class).getBody();

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        Editeur updated = getEditeur("/" + saved.getId()).getBody();
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }

    @Test
    void testDeleteEditeurById() {
        Editeur saved = restTemplate.postForEntity(
                url("/"), buildEditeur("ASupprimer3"), Editeur.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getEditeur("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Editeur buildEditeur(String nom) {
        Editeur e = new Editeur();
        e.setNom(nom);
        e.setAdresse("1 rue de l'Edition");
        e.setLienSiteWeb("https://editeur.fr");
        return e;
    }
}