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

// import com.usmb.but3.td4biblio.entity.Utilisateur;
// import com.usmb.but3.td4biblio.service.RoleUtilisateurService;
// import com.usmb.but3.td4biblio.service.UtilisateurService;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.time.LocalDate;
// import java.util.List;

// /**
//  * Seed : 2 bibliothécaires (role=1 : mdurand, jpires), 6 emprunteurs (role=2), total 8
//  *  id=1 mdurand  Marie Durand    role=1 sans abonnement
//  *  id=3 amoreau  Alice Moreau    role=2 carte=EMP0000001 fin=2026-12-31
//  */
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class UtilisateurControllerTest {

//     @LocalServerPort private int port;
//     @Autowired private TestRestTemplate restTemplate;
//     @Autowired private UtilisateurService utilisateurService;
//     @Autowired private RoleUtilisateurService roleUtilisateurService;

//     private String url(String path) {
//         return "http://localhost:" + port + "/biblio/utilisateur" + path;
//     }

//     private ResponseEntity<List<Utilisateur>> getUtilisateurs(String path) {
//         return restTemplate.exchange(url(path), HttpMethod.GET, null,
//                 new ParameterizedTypeReference<List<Utilisateur>>() {});
//     }

//     private ResponseEntity<Utilisateur> getUtilisateur(String path) {
//         return restTemplate.getForEntity(url(path), Utilisateur.class);
//     }

//     // =========================================================================
//     // GET /biblio/utilisateur/
//     // =========================================================================

//     @Test
//     void testGetAllUtilisateurs_retourne200() {
//         assertThat(getUtilisateurs("/").getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetAllUtilisateurs_retourne8Utilisateurs() {
//         assertThat(getUtilisateurs("/").getBody()).hasSizeGreaterThanOrEqualTo(8);
//     }

//     @Test
//     void testGetAllUtilisateurs_premierEstMdurand() {
//         Utilisateur premier = getUtilisateurs("/").getBody().get(0);
//         assertThat(premier.getLogin()).isEqualTo("mdurand");
//         assertThat(premier.getNom()).isEqualTo("Durand");
//     }

//     @Test
//     void testGetAllUtilisateurs_triParIdAscendant() {
//         List<Utilisateur> utilisateurs = getUtilisateurs("/").getBody();
//         for (int i = 0; i < utilisateurs.size() - 1; i++) {
//             assertThat(utilisateurs.get(i).getId()).isLessThan(utilisateurs.get(i + 1).getId());
//         }
//     }

//     // =========================================================================
//     // GET /biblio/utilisateur/{id}
//     // =========================================================================

//     @Test
//     void testGetUtilisateurById1_retourne200() {
//         assertThat(getUtilisateur("/1").getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetUtilisateurById1_estMdurand() {
//         Utilisateur u = getUtilisateur("/1").getBody();
//         assertThat(u.getLogin()).isEqualTo("mdurand");
//         assertThat(u.getNom()).isEqualTo("Durand");
//         assertThat(u.getPrenom()).isEqualTo("Marie");
//         assertThat(u.getRoleUtilisateur().getId()).isEqualTo(1);
//         assertThat(u.getDateFinAbonnement()).isNull();
//     }

//     @Test
//     void testGetUtilisateurById3_estAliceMoreau() {
//         Utilisateur u = getUtilisateur("/3").getBody();
//         assertThat(u.getLogin()).isEqualTo("amoreau");
//         assertThat(u.getNom()).isEqualTo("Moreau");
//         assertThat(u.getNumeroCarte()).isEqualTo("EMP0000001");
//         assertThat(u.getDateFinAbonnement()).isEqualTo(LocalDate.of(2026, 12, 31));
//         assertThat(u.getRoleUtilisateur().getId()).isEqualTo(2);
//     }

//     @Test
//     void testGetUtilisateurById_idInexistantRetourne404() {
//         assertThat(getUtilisateur("/99999").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     // =========================================================================
//     // GET /biblio/utilisateur/role/{roleId}
//     // =========================================================================

//     @Test
//     void testGetUtilisateursByRole1_retourne200Et2Bibliothecaires() {
//         ResponseEntity<List<Utilisateur>> response = getUtilisateurs("/role/1");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//         List<Utilisateur> utilisateurs = response.getBody();
//         assertThat(utilisateurs).hasSize(2);
//         assertThat(utilisateurs).extracting(Utilisateur::getLogin)
//                 .containsExactlyInAnyOrder("mdurand", "jpires");
//     }

//     @Test
//     void testGetUtilisateursByRole2_retourne6Emprunteurs() {
//         List<Utilisateur> utilisateurs = getUtilisateurs("/role/2").getBody();
//         assertThat(utilisateurs).hasSize(6);
//         assertThat(utilisateurs).allSatisfy(u ->
//                 assertThat(u.getRoleUtilisateur().getId()).isEqualTo(2));
//     }

//     @Test
//     void testGetUtilisateursByRole_roleInexistantRetourneListeVide() {
//         assertThat(getUtilisateurs("/role/99999").getBody()).isEmpty();
//     }

//     // =========================================================================
//     // POST /biblio/utilisateur/
//     // =========================================================================

//     @Test
//     @Transactional
//     void testSaveUtilisateur_retourne200EtIdGenere() {
//         ResponseEntity<Utilisateur> response = restTemplate.postForEntity(
//                 url("/"), buildUtilisateur("loginNew", "Nouveau", "Test"), Utilisateur.class);
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//         assertThat(response.getBody().getId()).isNotNull().isGreaterThan(8);
//     }

//     @Test
//     @Transactional
//     void testSaveUtilisateur_champsCorrects() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginChamps", "Dupont", "Jean"),
//                 Utilisateur.class).getBody();
//         assertThat(saved.getNom()).isEqualTo("Dupont");
//         assertThat(saved.getPrenom()).isEqualTo("Jean");
//         assertThat(saved.getLogin()).isEqualTo("loginChamps");
//         assertThat(saved.getRoleUtilisateur().getId()).isEqualTo(2);
//     }

//     @Test
//     @Transactional
//     void testSaveUtilisateur_retrouvableParGetId() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginRetrouvable", "Martin", "Sophie"),
//                 Utilisateur.class).getBody();
//         Utilisateur found = getUtilisateur("/" + saved.getId()).getBody();
//         assertThat(found.getNom()).isEqualTo("Martin");
//     }

//     // =========================================================================
//     // PUT /biblio/utilisateur/
//     // =========================================================================

//     @Test
//     @Transactional
//     void testUpdateUtilisateur_nomMisAJour() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginUpd", "AncienNom", "Prenom"),
//                 Utilisateur.class).getBody();

//         saved.setNom("NouveauNom");
//         restTemplate.put(url("/"), saved);

//         Utilisateur updated = getUtilisateur("/" + saved.getId()).getBody();
//         assertThat(updated.getNom()).isEqualTo("NouveauNom");
//     }

//     @Test
//     @Transactional
//     void testUpdateUtilisateur_emailMisAJour() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginEmail", "NomEmail", "Prenom"),
//                 Utilisateur.class).getBody();

//         saved.setEmail("nouveau@email.com");
//         restTemplate.put(url("/"), saved);

//         Utilisateur updated = getUtilisateur("/" + saved.getId()).getBody();
//         assertThat(updated.getEmail()).isEqualTo("nouveau@email.com");
//     }

//     // =========================================================================
//     // DELETE /biblio/utilisateur/{id}
//     // =========================================================================

//     @Test
//     @Transactional
//     void testDeleteUtilisateurById_retourne200() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginDel", "ASupprimer", "Test"),
//                 Utilisateur.class).getBody();

//         ResponseEntity<String> response = restTemplate.exchange(
//                 url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     @Transactional
//     void testDeleteUtilisateurById_utilisateurPlusExistantViaService() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginDel2", "ASupprimer2", "Test"),
//                 Utilisateur.class).getBody();

//         restTemplate.delete(url("/" + saved.getId()));
//         assertThat(utilisateurService.getUtilisateurById(saved.getId())).isNull();
//     }

//     @Test
//     @Transactional
//     void testDeleteUtilisateurById_retourne404ApresSuppression() {
//         Utilisateur saved = restTemplate.postForEntity(url("/"),
//                 buildUtilisateur("loginDel3", "ASupprimer3", "Test"),
//                 Utilisateur.class).getBody();

//         restTemplate.delete(url("/" + saved.getId()));
//         assertThat(getUtilisateur("/" + saved.getId()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     // =========================================================================
//     // Helpers
//     // =========================================================================

//     private Utilisateur buildUtilisateur(String login, String nom, String prenom) {
//         Utilisateur u = new Utilisateur();
//         u.setLogin(login);
//         u.setMotDePasse("mdp1234");
//         u.setNom(nom);
//         u.setPrenom(prenom);
//         u.setEmail(login + "@test.com");
//         u.setAdresse("1 rue Test");
//         u.setNumeroCarte("TESTC001");
//         u.setDateNaissance(LocalDate.of(1990, 1, 1));
//         u.setDateFinAbonnement(LocalDate.of(2027, 12, 31));
//         u.setDureeEmpruntMax(3);
//         u.setMaxEmprunts(5);
//         u.setRoleUtilisateur(roleUtilisateurService.getRoleById(2));
//         return u;
//     }
// }
