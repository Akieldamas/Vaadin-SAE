package com.usmb.but3.td4biblio.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

/**
 * Tests d'intégration pour AuteurController.
 *
 * Seed (auteur trié par id ASC) :
 *  id=1  Orwell George        Britannique  1903-06-25
 *  id=2  Camus  Albert        Française    1913-11-07
 *  id=3  Le Guin Ursula K.   Américaine   1929-10-21
 *  id=4  King   Stephen       Américaine   1947-09-21
 *  id=5  Nolan  Christopher   Britannique  1970-07-30
 *  id=6  Spielberg Steven     Américaine   1946-12-18
 *  id=7  Zimmer Hans          Allemande    1957-09-12
 *  id=8  Cohen  Leonard       Canadienne   1934-09-21
 *  id=9  Miyazaki Hayao       Japonaise    1941-01-05
 *  id=10 Duras  Marguerite    Française    1914-04-04
 *  id=11 Tolkien J.R.R.       Britannique  1892-01-03
 *  id=12 Villeneuve Denis     Canadienne   1967-10-03
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

    // =========================================================================
    // GET /biblio/auteur/
    // =========================================================================

    @Test
    void testGetAllAuteurs_retourneListeNonVide() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        assertThat(auteurs).isNotEmpty();
    }

    @Test
    void testGetAllAuteurs_contient12Auteurs() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        assertThat(auteurs).hasSizeGreaterThanOrEqualTo(12);
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        assertThat(auteurs[0].getNom()).isEqualTo("Orwell");
        assertThat(auteurs[0].getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAllAuteurs_deuxiemeEstCamus() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        assertThat(auteurs[1].getNom()).isEqualTo("Camus");
        assertThat(auteurs[1].getPrenom()).isEqualTo("Albert");
    }

    @Test
    void testGetAllAuteurs_triParIdAscendant() {
        Auteur[] auteurs = restTemplate.getForObject(url("/"), Auteur[].class);
        for (int i = 0; i < auteurs.length - 1; i++) {
            assertThat(auteurs[i].getId()).isLessThan(auteurs[i + 1].getId());
        }
    }

    // =========================================================================
    // GET /biblio/auteur/{id}
    // =========================================================================

    @Test
    void testGetAuteurById1_estOrwell() {
        Auteur auteur = restTemplate.getForObject(url("/1"), Auteur.class);
        assertThat(auteur).isNotNull();
        assertThat(auteur.getId()).isEqualTo(1);
        assertThat(auteur.getNom()).isEqualTo("Orwell");
        assertThat(auteur.getPrenom()).isEqualTo("George");
        assertThat(auteur.getNationalite()).isEqualTo("Britannique");
        assertThat(auteur.getDateNaissance()).isEqualTo(LocalDate.of(1903, 6, 25));
        assertThat(auteur.getDateDeces()).isEqualTo(LocalDate.of(1950, 1, 21));
    }

    @Test
    void testGetAuteurById5_estNolan() {
        Auteur auteur = restTemplate.getForObject(url("/5"), Auteur.class);
        assertThat(auteur.getNom()).isEqualTo("Nolan");
        assertThat(auteur.getPrenom()).isEqualTo("Christopher");
        assertThat(auteur.getDateDeces()).isNull();  // vivant
    }

    @Test
    void testGetAuteurById11_estTolkien() {
        Auteur auteur = restTemplate.getForObject(url("/11"), Auteur.class);
        assertThat(auteur.getNom()).isEqualTo("Tolkien");
        assertThat(auteur.getNationalite()).isEqualTo("Britannique");
    }

    @Test
    void testGetAuteurById_idInexistantRetourne404() {
        ResponseEntity<Auteur> response = restTemplate.getForEntity(url("/99999"), Auteur.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // GET /biblio/auteur/nom/{nom}
    // =========================================================================

    @Test
    void testGetAuteursByNom_orwell_retourneUnSeul() {
        Auteur[] auteurs = restTemplate.getForObject(url("/nom/Orwell"), Auteur[].class);
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