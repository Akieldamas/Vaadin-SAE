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
import com.usmb.but3.td4biblio.service.AuteurService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
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

    // =========================================================================
    // GET /biblio/auteur/
    // =========================================================================

    @Test
    void testGetAllAuteurs() {
        ResponseEntity<List<Auteur>> response = restTemplate.exchange(
        url("/"),
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<Auteur>>() {}
    );

        List<Auteur> auteurs = response.getBody();
        assertThat(auteurs).isNotEmpty();
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        ResponseEntity<List<Auteur>> response = restTemplate.exchange(
            url("/"),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Auteur>>() {}
        );

        List<Auteur> auteurs = response.getBody();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteurById1_estOrwell() {
        ResponseEntity<Auteur> response = restTemplate.getForEntity(
            url("/"),
            Auteur.class,
            null);
        
        Auteur auteur = response.getBody();

        assertThat(auteur).isNotNull();
        assertThat(auteur.getId()).isEqualTo(1);
        assertThat(auteur.getNom()).isEqualTo("Orwell");
        assertThat(auteur.getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteurById_idInexistantRetourne404() {
        ResponseEntity<Auteur> response = restTemplate.getForEntity(url("/99999"), Auteur.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetAuteursByNom_orwell_retourneUnSeul() {
        ResponseEntity<Auteur> response = restTemplate.getForEntity(url("/99999"), Auteur.class);


        assertThat(auteurs).hasSize(1);
        assertThat(auteurs[0].getNom()).isEqualTo("Orwell");
        assertThat(auteurs[0].getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNom_camus_retourneUnSeul() {
        Auteur[] auteurs = restTemplate.getForObject(url("/nom/Camus"), Auteur[].class);
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs[0].getPrenom()).isEqualTo("Albert");
    }

    @Test
    void testGetAuteursByNom_nomInexistantRetourneVide() {
        Auteur[] auteurs = restTemplate.getForObject(url("/nom/NomInexistant999"), Auteur[].class);
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // GET /biblio/auteur/search?nom=&prenom=
    // =========================================================================

    @Test
    void testGetAuteursByNomAndPrenom_orwellGeorge() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/search?nom=Orwell&prenom=George"), Auteur[].class);
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs[0].getNom()).isEqualTo("Orwell");
        assertThat(auteurs[0].getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNomAndPrenom_kingStephen() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/search?nom=King&prenom=Stephen"), Auteur[].class);
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs[0].getId()).isEqualTo(4);
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingInexistantRetourneVide() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/search?nom=Orwell&prenom=Albert"), Auteur[].class);
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // GET /biblio/auteur/searchLike?nom=&prenom=
    // =========================================================================

    @Test
    void testGetAuteursByNomLike_Or_trouveOrwell() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/searchLike?nom=Or&prenom=Geo"), Auteur[].class);
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs).anySatisfy(a -> assertThat(a.getNom()).isEqualTo("Orwell"));
    }

    @Test
    void testGetAuteursByNomLike_Nol_trouveNolan() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/searchLike?nom=Nol&prenom=Chris"), Auteur[].class);
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs[0].getNom()).isEqualTo("Nolan");
    }

    @Test
    void testGetAuteursByNomLike_inexistantRetourneVide() {
        Auteur[] auteurs = restTemplate.getForObject(
                url("/searchLike?nom=ZZZZ&prenom=YYYY"), Auteur[].class);
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // POST /biblio/auteur/
    // =========================================================================

    @Test
    @Transactional
    void testSaveAuteur_idGenere() {
        Auteur auteur = buildAuteur("Zola", "Emile", "Française",
                LocalDate.of(1840, 4, 2), LocalDate.of(1902, 9, 29));
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);
        assertThat(saved.getId()).isNotNull().isGreaterThan(12);
    }

    @Test
    @Transactional
    void testSaveAuteur_champsPresistent() {
        Auteur auteur = buildAuteur("Baudelaire", "Charles", "Française",
                LocalDate.of(1821, 4, 9), LocalDate.of(1867, 8, 31));
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        assertThat(saved.getNom()).isEqualTo("Baudelaire");
        assertThat(saved.getPrenom()).isEqualTo("Charles");
        assertThat(saved.getNationalite()).isEqualTo("Française");
        assertThat(saved.getDateNaissance()).isEqualTo(LocalDate.of(1821, 4, 9));
        assertThat(saved.getDateDeces()).isEqualTo(LocalDate.of(1867, 8, 31));
    }

    @Test
    @Transactional
    void testSaveAuteur_retrouvableParId() {
        Auteur auteur = buildAuteur("Proust", "Marcel", "Française",
                LocalDate.of(1871, 7, 10), LocalDate.of(1922, 11, 18));
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        Auteur found = restTemplate.getForObject(url("/" + saved.getId()), Auteur.class);
        assertThat(found.getNom()).isEqualTo("Proust");
    }

    // =========================================================================
    // PUT /biblio/auteur/
    // =========================================================================

    @Test
    @Transactional
    void testUpdateAuteur_nationaliteMiseAJour() {
        Auteur auteur = buildAuteur("TestNom", "TestPrenom", "Ancienne", null, null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        saved.setNationalite("Nouvelle");
        restTemplate.put(url("/"), saved);

        Auteur updated = restTemplate.getForObject(url("/" + saved.getId()), Auteur.class);
        assertThat(updated.getNationalite()).isEqualTo("Nouvelle");
    }

    @Test
    @Transactional
    void testUpdateAuteur_nomMisAJour() {
        Auteur auteur = buildAuteur("AncienNom", "Prenom", "Française", null, null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        saved.setNom("NouveauNom");
        restTemplate.put(url("/"), saved);

        Auteur updated = restTemplate.getForObject(url("/" + saved.getId()), Auteur.class);
        assertThat(updated.getNom()).isEqualTo("NouveauNom");
    }

    @Test
    @Transactional
    void testUpdateAuteur_dateDecesMiseAJour() {
        Auteur auteur = buildAuteur("TestDeces", "Prenom", "Française",
                LocalDate.of(1900, 1, 1), null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        saved.setDateDeces(LocalDate.of(1970, 6, 15));
        restTemplate.put(url("/"), saved);

        Auteur updated = restTemplate.getForObject(url("/" + saved.getId()), Auteur.class);
        assertThat(updated.getDateDeces()).isEqualTo(LocalDate.of(1970, 6, 15));
    }

    // =========================================================================
    // DELETE /biblio/auteur/{id}
    // =========================================================================

    @Test
    @Transactional
    void testDeleteAuteurById_auteurPlusExistant() {
        Auteur auteur = buildAuteur("ASupprimer", "Test", null, null, null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(auteurService.getAuteurById(saved.getId())).isNull();
    }

    @Test
    @Transactional
    void testDeleteAuteurById_retourne200() {
        Auteur auteur = buildAuteur("ASupprimer2", "Test", null, null, null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        ResponseEntity<String> response = restTemplate.exchange(
                url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void testDeleteAuteurById_retourne404Apres() {
        Auteur auteur = buildAuteur("ASupprimer3", "Test", null, null, null);
        Auteur saved = restTemplate.postForObject(url("/"), auteur, Auteur.class);

        restTemplate.delete(url("/" + saved.getId()));

        ResponseEntity<Auteur> response = restTemplate.getForEntity(
                url("/" + saved.getId()), Auteur.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Auteur buildAuteur(String nom, String prenom, String nationalite,
                                LocalDate naissance, LocalDate deces) {
        Auteur a = new Auteur();
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setNationalite(nationalite);
        a.setDateNaissance(naissance);
        a.setDateDeces(deces);
        return a;
    }
}