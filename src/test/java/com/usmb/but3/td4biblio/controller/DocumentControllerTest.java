package com.usmb.but3.td4biblio.controller;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.FormatService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatObject;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentControllerTest {

    @LocalServerPort
	private int port;

 	@Autowired
	private TestRestTemplate restTemplate;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FormatService formatService;
    @Autowired
    private AuteurService auteurService;
    @Autowired
    private EditeurService editeurService;

    Document testDocument;
    Document savedDocument;

    @BeforeAll
    static void setUp() {
        // This method can be used to set up any required data before all tests run
        // For example, you can initialize the database with some Livre objects
        
    }

    @BeforeEach
    void init() {
        // This method can be used to reset the state before each test
        // For example, you can clear the database or set up a specific state        
    }   

    @Test
    void testGetAllDocuments() {

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/document/",
                Document[].class)).satisfies(documents -> {
                    assertThat(documents).isNotEmpty();
                    assertThat(documents[0].getTitre()).isEqualTo("1984");
                    assertThat(documents[0].getAuteur().getNom()).isEqualTo("Orwell");
                });
    }

    @Test
    void testGetDocumentById() {
        //OK :
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/auteur/1",
        //        Auteur.class)).satisfies(livre -> assertThat(livre.getNom()).equals("Hugo"));  // Assuming the book with ID 1 is "Les Misérables"				

        // OK : much simpler :
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/document/1",
                Document.class)).satisfies(document -> (document.getId()).equals(1));  				

        // TESTed : DO  FAIL : New assertion to check the Livre object			
        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/biblio/livre/1",
        //        Livre.class)).satisfies(livre -> assertThat(livre.getTitre()).isEqualTo("Les Miséreux"));  // Assuming the book with ID 1 is "Les Misérables"				
    }
        
    @Test
    @Transactional
    void testSaveDocument() {
        Document testDocument = new Document();
        testDocument.setTitre("494949");
        testDocument.setAuteur(auteurService.getAuteurById(1));
        testDocument.setFormat(formatService.getFormatById(1));
        testDocument.setEditeur(editeurService.getEditeurById(1));

        Document saved = restTemplate.postForObject(
            "http://localhost:" + port + "/biblio/document",
            testDocument,
            Document.class
        );

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitre()).isEqualTo("494949");
    }

    @Test
    @Transactional
    void testUpdateDocument() {
        Document doc = new Document();
        doc.setTitre("old");
        doc.setAuteur(auteurService.getAuteurById(1));
        doc.setFormat(formatService.getFormatById(1));
        doc.setEditeur(editeurService.getEditeurById(1));
    
        Document saved = restTemplate.postForObject(
            "http://localhost:" + port + "/biblio/document",
            doc,
            Document.class
        );
    
        saved.setTitre("new");
    
        restTemplate.put(
            "http://localhost:" + port + "/biblio/document/" + saved.getId(),
            saved
        );
    
        Document updated = restTemplate.getForObject(
            "http://localhost:" + port + "/biblio/document/" + saved.getId(),
            Document.class
        );
    
        assertThat(updated.getTitre()).isEqualTo("new");
    }

    @Test
    @Transactional
    void testDeleteDocumentById() {
        Document doc = new Document();
        doc.setTitre("to delete");
        doc.setAuteur(auteurService.getAuteurById(1));
        doc.setFormat(formatService.getFormatById(1));
        doc.setEditeur(editeurService.getEditeurById(1));
    
        Document saved = restTemplate.postForObject(
            "http://localhost:" + port + "/biblio/document",
            doc,
            Document.class
        );
    
        restTemplate.delete(
            "http://localhost:" + port + "/biblio/document/" + saved.getId()
        );
    
        assertThat(
            documentService.getDocumentById(saved.getId())
        ).isNull();
    }

    @Test
    void testGetDocumentsByAuteurId() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/document/auteur/1",
        Document[].class)).allSatisfy(document -> {
            assertThat(document.getAuteur().getNom()).isEqualTo("Orwell");
        });
    }

    @Test
    @Transactional
    void testGetDocumentsByTitreContaining() {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/biblio/document/search?=A",
                Document[].class)).allSatisfy(document -> {
            assertThat(document.getTitre()).containsIgnoringCase("A");
        });
    }

    @Test
    void testFailGetDocumentByTitreContaining() {
        ResponseEntity<Document> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/biblio/document/99999",
            Document.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}