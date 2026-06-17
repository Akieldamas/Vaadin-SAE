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

import com.usmb.but3.td4biblio.entity.TypeDocument;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TypeDocumentControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/typedocument" + path;
    }

    private ResponseEntity<List<TypeDocument>> getTypes(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TypeDocument>>() {});
    }

    private ResponseEntity<TypeDocument> getType(String path) {
        return restTemplate.getForEntity(url(path), TypeDocument.class);
    }

    @Test
    void testGetAllTypeDocuments_retourne200() {
        assertThat(getTypes("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllTypeDocuments_premierEstLivre() {
        assertThat(getTypes("/").getBody().get(0).getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetTypeDocumentById1_estLivre() {
        assertThat(getType("/1").getBody().getNom()).isEqualTo("Livre");
    }

    @Test
    void testGetTypeDocumentById_idInexistantRetourne404() {
        assertThat(getType("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetTypeDocumentByNom_film_trouve() {
        TypeDocument t = restTemplate.getForEntity(url("/nom/Film"), TypeDocument.class).getBody();
        assertThat(t.getId()).isEqualTo(2);
    }

    @Test
    void testGetTypeDocumentByNom_introuvableRetourne404() {
        ResponseEntity<TypeDocument> response = restTemplate.getForEntity(url("/nom/NomInexistant999"), TypeDocument.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Transactional
    void testSaveTypeDocument_champsPersistent() {
        TypeDocument saved = restTemplate.postForEntity(
                url("/"), buildType("Nouveau Type"), TypeDocument.class).getBody();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNom()).isEqualTo("Nouveau Type");
    }

    @Test
    @Transactional
    void testUpdateTypeDocument_nomMisAJour() {
        TypeDocument saved = restTemplate.postForEntity(
                url("/"), buildType("AncienNom"), TypeDocument.class).getBody();

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        TypeDocument updated = getType("/" + saved.getId()).getBody();
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testDeleteTypeDocumentById_retourne404Apres() {
        TypeDocument saved = restTemplate.postForEntity(
                url("/"), buildType("ASupprimer"), TypeDocument.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getType("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private TypeDocument buildType(String nom) {
        TypeDocument t = new TypeDocument();
        t.setNom(nom);
        return t;
    }
}