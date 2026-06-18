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

import com.usmb.but3.td4biblio.entity.Format;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FormatControllerTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/format" + path;
    }

    private ResponseEntity<List<Format>> getFormats(String path) {
        return restTemplate.exchange(url(path), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Format>>() {});
    }

    private ResponseEntity<Format> getFormat(String path) {
        return restTemplate.getForEntity(url(path), Format.class);
    }

    @Test
    void testGetAllFormats_retourne200() {
        assertThat(getFormats("/").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllFormats_premierEstLivrePoche() {
        Format f = getFormats("/").getBody().get(0);
        assertThat(f.getId()).isEqualTo(1);
        assertThat(f.getLongueur()).isEqualTo(new BigDecimal("17.5"));
    }

    @Test
    void testGetFormatById1_estLivrePoche() {
        Format f = getFormat("/1").getBody();
        assertThat(f.getLongueur()).isEqualTo(new BigDecimal("17.5"));
        assertThat(f.getLargeur()).isEqualTo(new BigDecimal("10.8"));
    }

    @Test
    void testGetFormatById_idInexistantRetourne404() {
        assertThat(getFormat("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetFormatsByDimensions_trouveLivrePoche() {
        ResponseEntity<List<Format>> response = getFormats("/dimensions?longueur=17.5&largeur=10.8");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1);
    }

    @Test
    void testGetFormatsByDimensions_introuvableRetourneVide() {
        List<Format> formats = getFormats("/dimensions?longueur=99.9&largeur=99.9").getBody();
        assertThat(formats).isEmpty();
    }

    @Test
    @Transactional
    void testSaveFormat_champsPersistent() {
        Format saved = restTemplate.postForEntity(
                url("/"), buildFormat("12.0", "8.0", "50"), Format.class).getBody();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLongueur()).isEqualTo(new BigDecimal("12.0"));
        assertThat(saved.getPoids()).isEqualTo(new BigDecimal("50"));
    }

    @Test
    @Transactional
    void testUpdateFormat_poidsMisAJour() {
        Format saved = restTemplate.postForEntity(
                url("/"), buildFormat("12.0", "8.0", "50"), Format.class).getBody();

        saved.setPoids(new BigDecimal("99"));
        restTemplate.put(url("/"), saved);

        Format updated = getFormat("/" + saved.getId()).getBody();
        assertThat(updated.getPoids()).isEqualTo(new BigDecimal("99"));
    }

    @Test
    @Transactional
    void testDeleteFormatById_retourne404Apres() {
        Format saved = restTemplate.postForEntity(
                url("/"), buildFormat("12.0", "8.0", "50"), Format.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(getFormat("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private Format buildFormat(String longueur, String largeur, String poids) {
        Format f = new Format();
        f.setLongueur(new BigDecimal(longueur));
        f.setLargeur(new BigDecimal(largeur));
        f.setPoids(new BigDecimal(poids));
        return f;
    }
}