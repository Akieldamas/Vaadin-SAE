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

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

/**
 * Seed auteurs (triés par id ASC) :
 *  1  Orwell       George        Britannique  1903-06-25 / 1950-01-21
 *  2  Camus        Albert        Française    1913-11-07 / 1960-01-04
 *  3  Le Guin      Ursula K.     Américaine   1929-10-21 / 2018-01-22
 *  4  King         Stephen       Américaine   1947-09-21 / vivant
 *  5  Nolan        Christopher   Britannique  1970-07-30 / vivant
 *  6  Spielberg    Steven        Américaine   1946-12-18 / vivant
 *  7  Zimmer       Hans          Allemande    1957-09-12 / vivant
 *  8  Cohen        Leonard       Canadienne   1934-09-21 / 2016-11-07
 *  9  Miyazaki     Hayao         Japonaise    1941-01-05 / vivant
 *  10 Duras        Marguerite    Française    1914-04-04 / 1996-03-03
 *  11 Tolkien      J.R.R.        Britannique  1892-01-03 / 1973-09-02
 *  12 Villeneuve   Denis         Canadienne   1967-10-03 / vivant
 */
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
    void testGetAllAuteurs_retourneAuMoins12Auteurs() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        assertThat(auteurs).hasSizeGreaterThanOrEqualTo(12);
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAllAuteurs_deuxiemeEstCamus() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        assertThat(auteurs.get(1).getNom()).isEqualTo("Camus");
    }

    @Test
    void testGetAllAuteurs_triParIdAscendant() {
        List<Auteur> auteurs = getAuteurs("/").getBody();
        for (int i = 0; i < auteurs.size() - 1; i++) {
            assertThat(auteurs.get(i).getId()).isLessThan(auteurs.get(i + 1).getId());
        }
    }

    // =========================================================================
    // GET /biblio/auteur/{id}
    // =========================================================================

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
        assertThat(auteur.getNationalite()).isEqualTo("Britannique");
        assertThat(auteur.getDateNaissance()).isEqualTo(LocalDate.of(1903, 6, 25));
        assertThat(auteur.getDateDeces()).isEqualTo(LocalDate.of(1950, 1, 21));
    }

    @Test
    void testGetAuteurById5_estNolan_vivant() {
        Auteur auteur = getAuteur("/5").getBody();
        assertThat(auteur.getNom()).isEqualTo("Nolan");
        assertThat(auteur.getPrenom()).isEqualTo("Christopher");
        assertThat(auteur.getDateDeces()).isNull();
    }

    @Test
    void testGetAuteurById8_estCohen() {
        Auteur auteur = getAuteur("/8").getBody();
        assertThat(auteur.getNom()).isEqualTo("Cohen");
        assertThat(auteur.getNationalite()).isEqualTo("Canadienne");
        assertThat(auteur.getDateDeces()).isEqualTo(LocalDate.of(2016, 11, 7));
    }

    @Test
    void testGetAuteurById11_estTolkien() {
        Auteur auteur = getAuteur("/11").getBody();
        assertThat(auteur.getNom()).isEqualTo("Tolkien");
        assertThat(auteur.getDateNaissance()).isEqualTo(LocalDate.of(1892, 1, 3));
    }

    @Test
    void testGetAuteurById_idInexistantRetourne404() {
        ResponseEntity<Auteur> response = getAuteur("/99999");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // GET /biblio/auteur/nom/{nom}
    // =========================================================================

    @Test
    void testGetAuteursByNom_Orwell_retourne200EtUnSeul() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/nom/Orwell");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNom_Camus_retourneId2() {
        List<Auteur> auteurs = getAuteurs("/nom/Camus").getBody();
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(2);
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
    void testGetAuteursByNomAndPrenom_KingStephen_retourneId4() {
        List<Auteur> auteurs = getAuteurs("/search?nom=King&prenom=Stephen").getBody();
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(4);
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingInexistantRetourneVide() {
        List<Auteur> auteurs = getAuteurs("/search?nom=Orwell&prenom=Albert").getBody();
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // GET /biblio/auteur/searchLike?nom=&prenom=
    // =========================================================================

    @Test
    void testGetAuteursByNomLike_Or_Geo_retourne200EtTrouveOrwell() {
        ResponseEntity<List<Auteur>> response = getAuteurs("/searchLike?nom=Or&prenom=Geo");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).anySatisfy(a ->
                assertThat(a.getNom()).isEqualTo("Orwell"));
    }

    @Test
    void testGetAuteursByNomLike_Nol_Chris_trouveNolan() {
        List<Auteur> auteurs = getAuteurs("/searchLike?nom=Nol&prenom=Chris").getBody();
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Nolan");
    }

    @Test
    void testGetAuteursByNomLike_inexistantRetourneVide() {
        List<Auteur> auteurs = getAuteurs("/searchLike?nom=ZZZZ&prenom=YYYY").getBody();
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // POST /biblio/auteur/
    // =========================================================================

    @Test
    @Transactional
    void testSaveAuteur_retourne200EtIdGenere() {
        Auteur auteur = buildAuteur("Zola", "Emile", "Française",
                LocalDate.of(1840, 4, 2), LocalDate.of(1902, 9, 29));

        ResponseEntity<Auteur> response = restTemplate.postForEntity(url("/"), auteur, Auteur.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull().isGreaterThan(12);
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

    // =========================================================================
    // PUT /biblio/auteur/
    // =========================================================================

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
    void testUpdateAuteur_nationaliteMiseAJour() {
        Auteur saved = restTemplate.postForEntity(url("/"),
                buildAuteur("TestNom", "TestPrenom", "Ancienne", null, null),
                Auteur.class).getBody();

        saved.setNationalite("Nouvelle");
        restTemplate.put(url("/"), saved);

        Auteur updated = getAuteur("/" + saved.getId()).getBody();
        assertThat(updated.getNationalite()).isEqualTo("Nouvelle");
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
