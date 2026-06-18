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

import com.usmb.but3.td4biblio.entity.TypeAuteur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TypeAuteurControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/typeauteur" + path;
    }

    private ResponseEntity<List<TypeAuteur>> getTypes(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeAuteur>>() {});
    }

    private ResponseEntity<TypeAuteur> getType(String path) {
        return restTemplate.getForEntity(url(path), TypeAuteur.class);
    }

    @Test
    void testGetAllTypeAuteurs_retourne200() {
        assertThat(getTypes("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllTypeAuteurs_premierEstEcrivain() {
        assertThat(getTypes("/").getBody().get(0).getLabel()).isEqualTo("Écrivain");
    }

    @Test
    void testGetTypeAuteurById1_estEcrivain() {
        assertThat(getType("/1").getBody().getLabel()).isEqualTo("Écrivain");
    }

    @Test
    void testGetTypeAuteurById_idInexistantRetourne404() {
        assertThat(getType("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetTypeAuteurByLabel_realisateur_trouve() {
        TypeAuteur t = restTemplate.getForEntity(url("/label/Réalisateur"), TypeAuteur.class).getBody();
        assertThat(t.getId()).isEqualTo(2);
    }

    @Test
    void testGetTypeAuteurByLabel_introuvableRetourne404() {
        ResponseEntity<TypeAuteur> response = restTemplate.getForEntity(url("/label/LabelInexistant999"), TypeAuteur.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testSaveTypeAuteur_champsPersistent() {
        TypeAuteur saved = restTemplate.postForEntity(
                url("/"), buildType("Nouveau Type"), TypeAuteur.class).getBody();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLabel()).isEqualTo("Nouveau Type");
    }

    @Test
    @Transactional
    void testUpdateTypeAuteur_labelMisAJour() {
        TypeAuteur saved = restTemplate.postForEntity(
                url("/"), buildType("AncienLabel"), TypeAuteur.class).getBody();

        saved.setLabel("NouveauLabel");
        restTemplate.put(url("/"), saved);

        TypeAuteur updated = getType("/" + saved.getId()).getBody();
        assertThat(updated.getLabel()).isEqualTo("NouveauLabel");
    }

    @Test
    @Transactional
    void testDeleteTypeAuteurById_retourne404Apres() {
        TypeAuteur saved = restTemplate.postForEntity(
                url("/"), buildType("ASupprimer"), TypeAuteur.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getType("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private TypeAuteur buildType(String label) {
        TypeAuteur t = new TypeAuteur();
        t.setLabel(label);
        return t;
    }
}