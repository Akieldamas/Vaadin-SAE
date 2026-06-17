// package com.usmb.but3.td4biblio.controller;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.transaction.annotation.Transactional;

// import com.usmb.but3.td4biblio.entity.Editeur;
// import com.usmb.but3.td4biblio.service.EditeurService;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.List;

// /**
//  * Seed éditeurs :
//  *  1 Gallimard       5 rue Gaston-Gallimard, 75007 Paris
//  *  2 Le Seuil        25 bd Romain-Rolland, 75014 Paris
//  *  3 J'ai lu         87 quai Panhard, 75013 Paris
//  *  4 Universal Music
//  *  5 Warner Bros.
//  *  6 Sony Pictures
//  *  7 Actes Sud
//  */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class EditeurControllerTest {

//     @LocalServerPort private int port;
//     @Autowired private TestRestTemplate restTemplate;
//     @Autowired private EditeurService editeurService;

//     private String url(String path) {
//         return "http://localhost:" + port + "/biblio/editeur" + path;
//     }

//     private ResponseEntity<List<Editeur>> getEditeurs(String path) {
//         return restTemplate.exchange(url(path), HttpMethod.GET, null,
//                 new ParameterizedTypeReference<List<Editeur>>() {});
//     }

//     private ResponseEntity<Editeur> getEditeur(String path) {
//         return restTemplate.getForEntity(url(path), Editeur.class);
//     }

//     // =========================================================================
//     // GET /biblio/editeur/
//     // =========================================================================

//     @Test
//     void testGetAllEditeurs_retourne200() {
//         assertThat(getEditeurs("/").getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetAllEditeurs_retourne7Editeurs() {
//         assertThat(getEditeurs("/").getBody()).hasSizeGreaterThanOrEqualTo(7);
//     }

//     @Test
//     void testGetAllEditeurs_premierEstGallimard() {
//         assertThat(getEditeurs("/").getBody().get(0).getNom()).isEqualTo("Gallimard");
//     }

//     @Test
//     void testGetAllEditeurs_triParIdAscendant() {
//         List<Editeur> editeurs = getEditeurs("/").getBody();
//         for (int i = 0; i < editeurs.size() - 1; i++) {
//             assertThat(editeurs.get(i).getId()).isLessThan(editeurs.get(i + 1).getId());
//         }
//     }

//     // =========================================================================
//     // GET /biblio/editeur/{id}
//     // =========================================================================

//     @Test
//     void testGetEditeurById1_retourne200() {
//         assertThat(getEditeur("/1").getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetEditeurById1_estGallimard() {
//         Editeur e = getEditeur("/1").getBody();
//         assertThat(e.getNom()).isEqualTo("Gallimard");
//         assertThat(e.getAdresse()).isEqualTo("5 rue Gaston-Gallimard, 75007 Paris");
//     }

//     @Test
//     void testGetEditeurById3_estJaiLu() {
//         assertThat(getEditeur("/3").getBody().getNom()).isEqualTo("J'ai lu");
//     }

//     @Test
//     void testGetEditeurById5_estWarnerBros() {
//         assertThat(getEditeur("/5").getBody().getNom()).isEqualTo("Warner Bros.");
//     }

//     @Test
//     void testGetEditeurById_idInexistantRetourne404() {
//         assertThat(getEditeur("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     // =========================================================================
//     // POST /biblio/editeur/
//     // =========================================================================

//     @Test
//     @Transactional
//     void testSaveEditeur_retourne200EtIdGenere() {
//         Editeur e = buildEditeur("Nouvel Editeur");
//         ResponseEntity<Editeur> response = restTemplate.postForEntity(url("/"), e, Editeur.class);
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//         assertThat(response.getBody().getId()).isNotNull().isGreaterThan(7);
//     }

//     @Test
//     @Transactional
//     void testSaveEditeur_nomCorrect() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("Editeur Test"), Editeur.class).getBody();
//         assertThat(saved.getNom()).isEqualTo("Editeur Test");
//     }

//     @Test
//     @Transactional
//     void testSaveEditeur_retrouvableParGetId() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("Editeur Retrouvable"), Editeur.class).getBody();
//         Editeur found = getEditeur("/" + saved.getId()).getBody();
//         assertThat(found.getNom()).isEqualTo("Editeur Retrouvable");
//     }

//     // =========================================================================
//     // PUT /biblio/editeur/
//     // =========================================================================

//     @Test
//     @Transactional
//     void testUpdateEditeur_nomMisAJour() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("AncienNom"), Editeur.class).getBody();

//         saved.setNom("NouveauNom");
//         restTemplate.put(url("/"), saved);

//         Editeur updated = getEditeur("/" + saved.getId()).getBody();
//         assertThat(updated.getNom()).isEqualTo("NouveauNom");
//     }

//     // =========================================================================
//     // DELETE /biblio/editeur/{id}
//     // =========================================================================

//     @Test
//     @Transactional
//     void testDeleteEditeurById_retourne200() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("ASupprimer"), Editeur.class).getBody();

//         ResponseEntity<String> response = restTemplate.exchange(
//                 url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     @Transactional
//     void testDeleteEditeurById_editeurPlusExistantViaService() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("ASupprimer2"), Editeur.class).getBody();

//         restTemplate.delete(url("/" + saved.getId()));
//         assertThat(editeurService.getEditeurById(saved.getId())).isNull();
//     }

//     @Test
//     @Transactional
//     void testDeleteEditeurById_retourne404ApresSuppression() {
//         Editeur saved = restTemplate.postForEntity(url("/"),
//                 buildEditeur("ASupprimer3"), Editeur.class).getBody();

//         restTemplate.delete(url("/" + saved.getId()));
//         assertThat(getEditeur("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     // =========================================================================
//     // Helpers
//     // =========================================================================

//     private Editeur buildEditeur(String nom) {
//         Editeur e = new Editeur();
//         e.setNom(nom);
//         e.setAdresse("1 rue de l'Edition");
//         e.setLienSiteWeb("https://editeur.fr");
//         return e;
//     }
// }
