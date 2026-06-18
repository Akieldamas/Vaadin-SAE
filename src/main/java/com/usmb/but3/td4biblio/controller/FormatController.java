package com.usmb.but3.td4biblio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.service.FormatService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/biblio/format")
@RequiredArgsConstructor
@Validated
public class FormatController {

    private final FormatService formatService;

    /**
     * GET all formats.
     * URL: localhost:8080/biblio/format/
     */
    @GetMapping("/")
    public ResponseEntity<List<Format>> getAllFormats() {
        return ResponseEntity.ok().body(formatService.getAllFormats());
    }

    /**
     * GET format by id.
     * URL: localhost:8080/biblio/format/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Format> getFormatById(@PathVariable("id") Integer id) {
        Format format = formatService.getFormatById(id);
        if (format == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(format);
    }

    /**
     * GET formats by exact longueur and largeur, as Request Parameters.
     * URL: localhost:8080/biblio/format/dimensions?longueur=17.5&largeur=10.8
     */
    @GetMapping("/dimensions")
    public ResponseEntity<List<Format>> getFormatsByDimensions(
            @RequestParam("longueur") BigDecimal longueur,
            @RequestParam("largeur") BigDecimal largeur) {
        return ResponseEntity.ok().body(formatService.findByDimensions(longueur, largeur));
    }

    /**
     * POST a new format.
     * URL: localhost:8080/biblio/format/
     */
    @PostMapping("/")
    public ResponseEntity<Format> saveFormat(@RequestBody Format format) {
        return ResponseEntity.ok().body(formatService.saveFormat(format));
    }

    /**
     * PUT (update) a format.
     * URL: localhost:8080/biblio/format/
     */
    @PutMapping("/")
    public ResponseEntity<Format> updateFormat(@RequestBody Format format) {
        return ResponseEntity.ok().body(formatService.updateFormat(format));
    }

    /**
     * DELETE a format by id.
     * URL: localhost:8080/biblio/format/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFormatById(@PathVariable("id") Integer id) {
        try {
            formatService.deleteFormatById(id);
            return ResponseEntity.ok().body("Deleted format successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}