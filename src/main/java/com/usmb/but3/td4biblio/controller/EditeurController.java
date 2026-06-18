package com.usmb.but3.td4biblio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/biblio/editeur")
@RequiredArgsConstructor
@Validated
public class EditeurController {
   
    private final EditeurService editeurService;

    /**
     * GET all auteurs.
     * URL: localhost:8080/biblio/editeur/
     */
    @GetMapping("/")
    public ResponseEntity<List<Editeur>> getAllEditeurs() {
        return ResponseEntity.ok().body(editeurService.getAllEditeurs());
    }

    /**
     * GET editeur by id.
     * URL: localhost:8080/biblio/editeur/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Editeur> getEditeurById(@PathVariable("id") Integer id) {
        Editeur editeur = editeurService.getEditeurById(id);
        if (editeur == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(editeur);
    }

    /**
     * GET editeur by nom.
     * URL: localhost:8080/biblio/editeur/nom/{nom}
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<Editeur> getEditeurByNom(@PathVariable("nom") String nom) {
        return ResponseEntity.ok().body(editeurService.getEditeurByNom(nom));
    }

    /**
     * POST a new editeur.
     * URL: localhost:8080/biblio/editeur/
     */
    @PostMapping("/")
    public ResponseEntity<Editeur> saveEditeur(@RequestBody Editeur editeur) {
        return ResponseEntity.ok().body(editeurService.saveEditeur(editeur));
    }

    /**
     * PUT (update) an editeur.
     * URL: localhost:8080/biblio/editeur/
     */
    @PutMapping("/")
    public ResponseEntity<Editeur> updateEditeur(@RequestBody Editeur editeur) {
        return ResponseEntity.ok().body(editeurService.updateEditeur(editeur));
    }

    /**
     * DELETE an editeur by id.
     * URL: localhost:8080/biblio/editeur/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEditeurById(@PathVariable("id") Integer id) {
        try {
            editeurService.deleteEditeurById(id);
            return ResponseEntity.ok().body("Deleted editeur successfully");
        } catch (Exception e) {
            e.printStackTrace(); // check your logs for the exact exception
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Editeur>> getEditeursByNomContaining(@RequestParam(name = "filter") String filter) {
        return ResponseEntity.ok().body(editeurService.getEditeursByNomContainingIgnoreCase(filter));
    }
}
