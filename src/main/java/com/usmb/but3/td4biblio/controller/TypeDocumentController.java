package com.usmb.but3.td4biblio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.TypeDocumentService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/biblio/typedocument")
@RequiredArgsConstructor
@Validated
public class TypeDocumentController {

    private final TypeDocumentService typeDocumentService;

    @GetMapping("/")
    public ResponseEntity<List<TypeDocument>> getAllTypeDocuments() {
        return ResponseEntity.ok().body(typeDocumentService.getAllTypeDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDocument> getTypeDocumentById(@PathVariable("id") Integer id) {
        TypeDocument type = typeDocumentService.getTypeDocumentById(id);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(type);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<TypeDocument> getTypeDocumentByNom(@PathVariable("nom") String nom) {
        TypeDocument type = typeDocumentService.getTypeDocumentByNom(nom);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(type);
    }

    @PostMapping("/")
    public ResponseEntity<TypeDocument> saveTypeDocument(@RequestBody TypeDocument typeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.saveTypeDocument(typeDocument));
    }

    @PutMapping("/")
    public ResponseEntity<TypeDocument> updateTypeDocument(@RequestBody TypeDocument typeDocument) {
        return ResponseEntity.ok().body(typeDocumentService.updateTypeDocument(typeDocument));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTypeDocumentById(@PathVariable("id") Integer id) {
        try {
            typeDocumentService.deleteTypeDocumentById(id);
            return ResponseEntity.ok().body("Deleted typeDocument successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}