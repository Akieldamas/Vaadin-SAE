package com.usmb.but3.td4biblio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/biblio/bibliotheque")
@RequiredArgsConstructor
@Validated
public class BibliothequeController {

    private final BibliothequeService bibliothequeService;

    /**
     * GET all bibliotheques.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @GetMapping("/")
    public ResponseEntity<List<Bibliotheque>> getAllBibliotheques() {
        return ResponseEntity.ok().body(bibliothequeService.getAllBibliotheques());
    }

    /**
     * GET bibliotheque by id.
     * URL: localhost:8080/biblio/bibliotheque/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bibliotheque> getBibliothequeById(@PathVariable("id") Integer id) {
        Bibliotheque bibliotheque = bibliothequeService.getBibliothequeById(id);
        if (bibliotheque == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bibliotheque);
    }

    /**
     * GET bibliotheque by nom.
     * URL: localhost:8080/biblio/bibliotheque/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<Bibliotheque> getBibliothequeByNom(@PathVariable("nom") String nom) {
        Bibliotheque bibliotheque = bibliothequeService.getBibliothequeByNom(nom);
        if (bibliotheque == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bibliotheque);
    }

    /**
     * POST a new bibliotheque.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @PostMapping("/")
    public ResponseEntity<Bibliotheque> saveBibliotheque(@RequestBody Bibliotheque bibliotheque) {
        return ResponseEntity.ok().body(bibliothequeService.saveBibliotheque(bibliotheque));
    }

    /**
     * PUT (update) a bibliotheque.
     * URL: localhost:8080/biblio/bibliotheque/
     */
    @PutMapping("/")
    public ResponseEntity<Bibliotheque> updateBibliotheque(@RequestBody Bibliotheque bibliotheque) {
        return ResponseEntity.ok().body(bibliothequeService.updateBibliotheque(bibliotheque));
    }

    /**
     * DELETE a bibliotheque by id.
     * URL: localhost:8080/biblio/bibliotheque/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBibliothequeById(@PathVariable("id") Integer id) {
        try {
            bibliothequeService.deleteBibliothequeById(id);
            return ResponseEntity.ok().body("Deleted bibliotheque successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}