package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * La classe Controller où sont traitées toutes les requests de l'utilisateur et où les
 * réponses appropriées sont renvoyées.
 * Elle interagit avec la couche Service pour accéder aux données.
 */
@RestController
@RequestMapping("/biblio/document")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;

    /**
     * This method is called when a GET request is made
     * URL: localhost:8080/biblio/document/
     * Purpose: Fetches all the documents in the document table
     * @return List of Documents
     */
    @GetMapping("/")
    public ResponseEntity<List<Document>> getAllDocuments(){
        return ResponseEntity.ok().body(documentService.getAllDocuments());
    }

    /**
     * This method is called when a GET request is made
     * URL: localhost:8080/biblio/document/1 (or any other id)
     * Purpose: Fetches document with the given id
     * @param id - document id
     * @return Document with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") Integer id)
    {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(document);
    }

    /**
     * This method is called when a POST request is made
     * URL: localhost:8080/biblio/document/
     * Purpose: Save a Document entity
     * @param document - Request body is a Document entity
     * @return Saved Document entity
     */
    @PostMapping("/")
    public ResponseEntity<Document> saveDocument(@RequestBody Document document)
    {
        return ResponseEntity.ok().body(documentService.saveDocument(document));
    }

    /**
     * This method is called when a PUT request is made
     * URL: localhost:8080/biblio/document/
     * Purpose: Update a Document entity
     * @param document - Document entity to be updated
     * @return Updated Document
     */
    @PutMapping("/")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document)
    {
        return ResponseEntity.ok().body(documentService.updateDocument(document));
    }

    /**
     * This method is called when a DELETE request is made
     * URL: localhost:8080/biblio/document/1 (or any other id)
     * Purpose: Delete a Document entity
     * @param id - document's id to be deleted
     * @return a String message indicating document record has been deleted successfully
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocumentById(@PathVariable("id") Integer id)
    {
        documentService.deleteDocumentById(id);
        return ResponseEntity.ok().body("Deleted document successfully");
    }

    /**
     * GET documents by auteur id.
     * URL: localhost:8080/biblio/document/auteur/{auteurId}
     */
    @GetMapping("/auteur/{auteurId}")
    public ResponseEntity<List<Document>> getDocumentsByAuteurId(@PathVariable("auteurId") Integer auteurId) {
        return ResponseEntity.ok().body(documentService.getByAuteurId(auteurId));
    }

    /**
     * GET documents by titre (--like--) as Request Parameters.
     * URL: localhost:8080/biblio/document/search?titre=miséra
     */
    @GetMapping("/search")
    public ResponseEntity<List<Document>> getDocumentsByTitreContaining(@RequestParam(name="titre") String titre) {
        return ResponseEntity.ok().body(documentService.getByTitreContainingIgnoreCase(titre));
    }  
}