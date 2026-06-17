package com.usmb.but3.td4biblio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.service.GenreDocumentService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/biblio/genredocument")
@RequiredArgsConstructor
@Validated
public class GenreDocumentController {

    private final GenreDocumentService genreDocumentService;

    @GetMapping("/")
    public ResponseEntity<List<GenreDocument>> getAllGenreDocuments() {
        return ResponseEntity.ok().body(genreDocumentService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDocument> getGenreDocumentById(@PathVariable("id") Integer id) {
        GenreDocument genre = genreDocumentService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(genre);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<GenreDocument> getGenreDocumentByNom(@PathVariable("nom") String nom) {
        GenreDocument genre = genreDocumentService.getGenreByNom(nom);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(genre);
    }

    @PostMapping("/")
    public ResponseEntity<GenreDocument> saveGenreDocument(@RequestBody GenreDocument genreDocument) {
        return ResponseEntity.ok().body(genreDocumentService.saveGenre(genreDocument));
    }

    @PutMapping("/")
    public ResponseEntity<GenreDocument> updateGenreDocument(@RequestBody GenreDocument genreDocument) {
        return ResponseEntity.ok().body(genreDocumentService.updateGenre(genreDocument));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenreDocumentById(@PathVariable("id") Integer id) {
        try {
            genreDocumentService.deleteGenreById(id);
            return ResponseEntity.ok().body("Deleted genreDocument successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}