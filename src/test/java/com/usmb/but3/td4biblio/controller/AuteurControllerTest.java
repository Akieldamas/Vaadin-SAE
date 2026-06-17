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

    /** Helper : GET une liste typée avec status + body. */
    private ResponseEntity<List<Auteur>> getAuteurs(String path) {
        return restTemplate.exchange(
                url(path),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Auteur>>() {});
    }

    /** Helper : GET un seul auteur avec status + body. */
    private ResponseEntity<Auteur> getAuteur(String path) {
        return restTemplate.getForEntity(url(path), Auteur.class);
    }

    // =========================================================================
    // GET /biblio/auteur/
    // =========================================================================

    @Test
    void testGetAllAuteurs_retourne200() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetAllAuteurs_contient12Auteurs() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        assertThat(auteurs).hasSizeGreaterThanOrEqualTo(12);
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
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

    // =========================================================================
    // GET /biblio/auteur/search?nom=&prenom=
    // =========================================================================

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
        ResponseEntity<List<Auteur>> response = getAuteurs("/searchLike?nom=Or&prenom=Geo");
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
                buildAuteur("AncienNom", "Prenom", "Française", null, null),
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

    // =========================================================================
    // DELETE /biblio/auteur/{id}
    // =========================================================================

    @Test
    @Transactional
    void testDeleteAuteurById_retourne200() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("ASupprimer", "Test", null, null, null),
                Auteur.class).getBody();

        ResponseEntity<String> response = restTemplate.exchange(
                url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void testDeleteAuteurById_auteurPlusExistantViaService() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("ASupprimer2", "Test", null, null, null),
                Auteur.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        assertThat(auteurService.getAuteurById(saved.getId())).isNull();
    }

    @Test
    @Transactional
    void testDeleteAuteurById_retourne404ApresSuppression() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("ASupprimer3", "Test", null, null, null),
                Auteur.class).getBody();

        restTemplate.delete(url("/" + saved.getId()));

        ResponseEntity<Auteur> response = getAuteur("/" + saved.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



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
