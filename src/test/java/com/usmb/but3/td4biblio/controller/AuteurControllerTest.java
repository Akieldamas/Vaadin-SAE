package com.usmb.but3.td4biblio.controller;

import org.assertj.core.util.Arrays;
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

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.AuteurService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuteurControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuteurService auteurService;

    private String url(String path) {
        return "http://localhost:" + port + "/biblio/auteur" + path;
    }

    private ResponseEntity<List<Auteur>> getAuteurs(String path) {
        System.out.println(url(path));
        System.out.println(url(path));
        // add this:
        ResponseEntity<String> raw = restTemplate.getForEntity(url(path), String.class);
        System.out.println("RAW JSON: " + raw.getBody());
        return restTemplate.exchange(
                url(path),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Auteur>>() {});
    }

    private ResponseEntity<Auteur> getAuteur(String path) {
        return restTemplate.getForEntity(url(path), Auteur.class);
    }

    @Test
    void testGetAllAuteurs_retourne200() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
    }

    @Test
    void testGetAuteurById1_retourne200() {
        ResponseEntity<Auteur> response = getAuteur("/1");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAuteurById1_estOrwell() {
        Auteur auteur = getAuteur("/1").getBody();
        assertThat(auteur.getId()).isEqualTo(1);
        assertThat(auteur.getNom()).isEqualTo("Orwell");
        assertThat(auteur.getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteurById_idInexistantRetourne404() {
        ResponseEntity<Auteur> response = getAuteur("/99999");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetAuteursByNom_orwell_retourneUnSeul() {
        Auteur[] auteurs = restTemplate.getForObject(url("/nom/Orwell"), Auteur[].class);
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs[0].getNom()).isEqualTo("Orwell");
        assertThat(auteurs[0].getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNom_nomInexistantRetourneListeVide() {
        List<Auteur> auteurs = getAuteurs("/nom/NomInexistant999").getBody();
        assertThat(auteurs).isEmpty();
    }

    @Test
    void testGetAuteursByNomAndPrenom_OrwellGeorge_retourne200() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/search?nom=Orwell&prenom=George");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1);
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingInexistantRetourneVide() {
        List<Auteur> auteurs = getAuteurs("/search?nom=Orwell&prenom=Albert").getBody();
        assertThat(auteurs).isEmpty();
    }

    @Test
    void testGetAuteursByNomLike_Or_Geo_retourne200EtTrouveOrwell() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/searchLike?nom=Orwell&prenom=George");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).anySatisfy(a ->
                assertThat(a.getNom()).isEqualTo("Orwell"));
    }

    @Test
    void testGetAuteursByNomLike_inexistantRetourneVide() {
        List<Auteur> auteurs = getAuteurs("/searchLike?nom=ZZZZ&prenom=YYYY").getBody();
        assertThat(auteurs).isEmpty();
    }

    @Test
    @Transactional
    void testSaveAuteur_retourne200() {
        Auteur auteur = buildAuteur("Zola", "Emile", "Française",
                LocalDate.of(1840, 4, 2), LocalDate.of(1902, 9, 29));

        ResponseEntity<Auteur> response = restTemplate.postForEntity(url("/"), auteur, Auteur.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void testSaveAuteur_champsPresistent() {
        Auteur auteur = buildAuteur("Baudelaire", "Charles", "Française",
                LocalDate.of(1821, 4, 9), LocalDate.of(1867, 8, 31));

        Auteur saved = restTemplate.postForEntity(url("/"), auteur, Auteur.class).getBody();

        assertThat(saved.getNom()).isEqualTo("Baudelaire");
        assertThat(saved.getPrenom()).isEqualTo("Charles");
        assertThat(saved.getNationalite()).isEqualTo("Française");
        assertThat(saved.getDateNaissance()).isEqualTo(LocalDate.of(1821, 4, 9));
        assertThat(saved.getDateDeces()).isEqualTo(LocalDate.of(1867, 8, 31));
    }

    @Test
    @Transactional
    void testSaveAuteur_retrouvableParGetId() {
        Auteur auteur = buildAuteur("Proust", "Marcel", "Française",
                LocalDate.of(1871, 7, 10), LocalDate.of(1922, 11, 18));

        Auteur saved = restTemplate.postForEntity(url("/"), auteur, Auteur.class).getBody();

        Auteur found = getAuteur("/" + saved.getId()).getBody();
        assertThat(found.getNom()).isEqualTo("Proust");
    }

    @Test
    @Transactional
    void testUpdateAuteur_nomMisAJour() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("AncienNom", "Prenom", "Française", LocalDate.of(1900, 1, 1), null),
                Auteur.class).getBody();

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        Auteur updated = getAuteur("/" + saved.getId()).getBody();
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }


    @Test
    @Transactional
    void testUpdateAuteur_dateDecesMiseAJour() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("TestDeces", "Prenom", "Française", LocalDate.of(1900, 1, 1), null),
                Auteur.class).getBody();

        saved.setDateDeces(LocalDate.of(1970, 6, 15));
        restTemplate.put(url("/"), saved);

        Auteur updated = getAuteur("/" + saved.getId()).getBody();
        assertThat(updated.getDateDeces()).isEqualTo(LocalDate.of(1970, 6, 15));
    }

        @Test
        void testDeleteAuteurById_retourne200() {
            Auteur saved = restTemplate.postForEntity(url("/"),
                    buildAuteur("ASupprimer", "Test", null, LocalDate.of(1900, 1, 1), null),
                    Auteur.class).getBody();

            ResponseEntity<String> response = restTemplate.exchange(
                    url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }


    private Auteur buildAuteur(String nom, String prenom, String nationalite,
                                LocalDate naissance, LocalDate deces) {
        Auteur a = new Auteur();
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setNationalite(nationalite);
        a.setDateNaissance(naissance);
        a.setDateDeces(deces);
        a.setTypes(new ArrayList<>());
        return a;
    }
    @Test
    private void auteurMethods() {
        Auteur a1 = new Auteur();
        a1.setId(1);
        a1.setNom("Hugo");
        a1.setPrenom("Victor");
        a1.setDateNaissance(LocalDate.of(1802, 2, 26));

        Auteur a2 = new Auteur();
        a2.setId(1);
        a2.setNom("Hugo");
        a2.setPrenom("Victor");
        a2.setDateNaissance(LocalDate.of(1802, 2, 26));

        assertTrue(a1.equals(a2));
        assertEquals(a1.hashCode(), a2.hashCode());

        a1.getDesc();
    }
}
