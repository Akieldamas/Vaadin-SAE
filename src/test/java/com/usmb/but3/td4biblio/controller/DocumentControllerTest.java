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

// import com.usmb.but3.td4biblio.entity.Document;
// import com.usmb.but3.td4biblio.service.AuteurService;
// import com.usmb.but3.td4biblio.service.BibliothequeService;
// import com.usmb.but3.td4biblio.service.DocumentService;
// import com.usmb.but3.td4biblio.service.EditeurService;
// import com.usmb.but3.td4biblio.service.FormatService;

// import static org.assertj.core.api.Assertions.assertThat;

// import java.time.LocalDate;
// import java.util.List;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class DocumentControllerTest {

//     @LocalServerPort
//     private int port;

//     @Autowired private TestRestTemplate restTemplate;
//     @Autowired private DocumentService documentService;
//     @Autowired private AuteurService auteurService;
//     @Autowired private FormatService formatService;
//     @Autowired private EditeurService editeurService;
//     @Autowired private BibliothequeService bibliothequeService;

//     private String url(String path) {
//         return "http://localhost:" + port + "/biblio/document" + path;
//     }

//     private ResponseEntity<List<Document>> getDocuments(String path) {
//         return restTemplate.exchange(
//                 url(path),
//                 HttpMethod.GET,
//                 null,
//                 new ParameterizedTypeReference<List<Document>>() {});
//     }

//     private ResponseEntity<Document> getDocument(String path) {
//         return restTemplate.getForEntity(url(path), Document.class);
//     }

//     @Test
//     void testGetAllDocuments() {
//         ResponseEntity<List<Document>> response = getDocuments("/");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetAllDocuments_premierEst1984() {
//         List<Document> docs = getDocuments("/").getBody();
//         assertThat(docs.get(0).getId()).isEqualTo(1);
//         assertThat(docs.get(0).getTitre()).isEqualTo("1984");
//     }

//     @Test
//     void testGetAllDocuments_auteurDuPremierEstOrwell() {
//         List<Document> docs = getDocuments("/").getBody();
//         assertThat(docs.get(0).getAuteur().getNom()).isEqualTo("Orwell");
//         assertThat(docs.get(0).getAuteur().getId()).isEqualTo(1);
//     }

//     @Test
//     void testGetDocumentById1_retourne200() {
//         ResponseEntity<Document> response = getDocument("/1");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetDocumentById1_est1984() {
//         Document doc = getDocument("/1").getBody();
//         assertThat(doc.getId()).isEqualTo(1);
//         assertThat(doc.getTitre()).isEqualTo("1984");
//     }

//     @Test
//     void testGetDocumentById_idInexistantRetourne404() {
//         ResponseEntity<Document> response = getDocument("/99999");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     @Test
//     void testGetDocumentsByAuteurId1_retourne200() {
//         ResponseEntity<List<Document>> response = getDocuments("/auteur/1");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     void testGetDocumentsByAuteurId1_retourne2DocsOrwell() {
//         List<Document> docs = getDocuments("/auteur/1").getBody();
//         assertThat(docs).hasSize(2);
//         assertThat(docs).extracting(Document::getTitre)
//                 .containsExactlyInAnyOrder("1984", "La Ferme des animaux");
//     }

//     @Test
//     void testGetDocumentsByAuteurId_auteurInexistantRetourneListeVide() {
//         List<Document> docs = getDocuments("/auteur/99999").getBody();
//         assertThat(docs).isEmpty();
//     }

//     @Test
//     void testSearchByTitre_1984_retourne200() {
//         ResponseEntity<List<Document>> response = getDocuments("/search?titre=1984");
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//         List<Document> docs = response.getBody();
//         assertThat(docs.get(0).getTitre()).isEqualTo("1984");
//     }

//     @Test
//     void testSearchByTitre_insensibleCasse() {
//         List<Document> lower = getDocuments("/search?titre=dune").getBody();
//         List<Document> upper = getDocuments("/search?titre=DUNE").getBody();
//         assertThat(lower).hasSameSizeAs(upper);
//     }

//     @Test
//     void testSearchByTitre_titreInexistantRetourneListeVide() {
//         List<Document> docs = getDocuments("/search?titre=ZZZINEXISTANT999").getBody();
//         assertThat(docs).isEmpty();
//     }

//     @Test
//     @Transactional
//     void testSaveDocument_retourne200EtIdGenere() {
//         ResponseEntity<Document> response = restTemplate.postForEntity(
//                 url(""), buildDocument("TitreTest"), Document.class);

//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//         assertThat(response.getBody().getId()).isNotNull().isGreaterThan(0);
//     }

//     @Test
//     @Transactional
//     void testSaveDocument_champsCorrects() {
//         Document saved = restTemplate.postForEntity(
//                 url(""), buildDocument("MonNouveauLivre"), Document.class).getBody();

//         assertThat(saved.getTitre()).isEqualTo("MonNouveauLivre");
//         assertThat(saved.getAuteur().getId()).isEqualTo(1);
//         assertThat(saved.getAuteur().getNom()).isEqualTo("Orwell");
//         assertThat(saved.getEditeur().getNom()).isEqualTo("Gallimard");
//     }

//     @Test
//     @Transactional
//     void testSaveDocument_retrouvableParGetId() {
//         Document saved = restTemplate.postForEntity(
//                 url(""), buildDocument("LivreRetrouvable"), Document.class).getBody();

//         Document found = getDocument("/" + saved.getId()).getBody();
//         assertThat(found.getTitre()).isEqualTo("LivreRetrouvable");
//     }

//     @Test
//     @Transactional
//     void testUpdateDocument_titreMisAJour() {
//         Document saved = restTemplate.postForEntity(
//                 url(""), buildDocument("TitreAncien"), Document.class).getBody();

//         saved.setTitre("TitreNouveau");
//         restTemplate.put(url("/" + saved.getId()), saved);

//         Document updated = getDocument("/" + saved.getId()).getBody();
//         assertThat(updated.getTitre()).isEqualTo("TitreNouveau");
//     }

//     @Test
//     @Transactional
//     void testDeleteDocumentById_retourne200() {
//         Document saved = restTemplate.postForEntity(
//                 url(""), buildDocument("TitreDelete200"), Document.class).getBody();

//         ResponseEntity<String> response = restTemplate.exchange(
//                 url("/" + saved.getId()), HttpMethod.DELETE, null, String.class);

//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//     }

//     @Test
//     @Transactional
//     void testDeleteDocumentById_retourne404ApresSuppression() {
//         Document saved = restTemplate.postForEntity(
//                 url(""), buildDocument("TitreDelete404"), Document.class).getBody();

//         restTemplate.delete(url("/" + saved.getId()));

//         ResponseEntity<Document> response = getDocument("/" + saved.getId());
//         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//     }

//     private Document buildDocument(String titre) {
//         Document doc = new Document();
//         doc.setTitre(titre);
//         doc.setAuteur(auteurService.getAuteurById(1));
//         doc.setFormat(formatService.getFormatById(1));
//         doc.setEditeur(editeurService.getEditeurById(1));
//         doc.setBibliotheque(bibliothequeService.getBibliothequeById(1));;
//         return doc;
//     }
// }
