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

import com.usmb.but3.td4biblio.entity.Bibliotheque;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BibliothequeControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/bibliotheque" + path;
    }

    private ResponseEntity<List<Bibliotheque>> getBibliotheques(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Bibliotheque>>() {});
    }

    private ResponseEntity<Bibliotheque> getBibliotheque(String path) {
        return restTemplate.getForEntity(url(path), Bibliotheque.class);
    }

    @Test
    void testGetAllBibliotheques_retourne200() {
        assertThat(getBibliotheques("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllBibliotheques_premierEstLaTurbine() {
        assertThat(getBibliotheques("/").getBody().get(0).getNom()).isEqualTo("Médiathèque La Turbine");
    }

    @Test
    void testGetBibliothequeById1_estLaTurbine() {
        Bibliotheque b = getBibliotheque("/1").getBody();
        assertThat(b.getNom()).isEqualTo("Médiathèque La Turbine");
        assertThat(b.getHoraireOuverture()).isEqualTo(LocalTime.of(8, 0));
    }

    @Test
    void testGetBibliothequeById_idInexistantRetourne404() {
        assertThat(getBibliotheque("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetBibliothequeByNom_trouve() {
        Bibliotheque b = restTemplate.getForEntity(url("/nom/Médiathèque La Turbine"), Bibliotheque.class).getBody();
        assertThat(b.getId()).isEqualTo(1);
    }

    @Test
    void testGetBibliothequeByNom_introuvableRetourne404() {
        ResponseEntity<Bibliotheque> response = restTemplate.getForEntity(url("/nom/NomInexistant999"), Bibliotheque.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testSaveBibliotheque_champsPersistent() {
        Bibliotheque saved = restTemplate.postForEntity(
                url("/"), buildBibliotheque("Nouvelle Biblio"), Bibliotheque.class).getBody();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Nouvelle Biblio");
    }

    @Test
    @Transactional
    void testUpdateBibliotheque_nomMisAJour() {
        Bibliotheque saved = restTemplate.postForEntity(
                url("/"), buildBibliotheque("AncienNom"), Bibliotheque.class).getBody();

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        Bibliotheque updated = getBibliotheque("/" + saved.getId()).getBody();
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testDeleteBibliothequeById_retourne404Apres() {
        Bibliotheque saved = restTemplate.postForEntity(
                url("/"), buildBibliotheque("ASupprimer"), Bibliotheque.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getBibliotheque("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Bibliotheque buildBibliotheque(String nom) {
        Bibliotheque b = new Bibliotheque();
        b.setNom(nom);
        b.setAdresse("1 rue des Livres");
        b.setHoraireOuverture(LocalTime.of(8, 0));
        b.setHoraireFermeture(LocalTime.of(18, 0));
        return b;
    }
}