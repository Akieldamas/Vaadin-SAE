package com.usmb.but3.td4biblio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.TypeAuteurService;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/biblio/typeauteur")
@RequiredArgsConstructor
@Validated
public class TypeAuteurController {

    private final TypeAuteurService typeAuteurService;

    @GetMapping("/")
    public ResponseEntity<List<TypeAuteur>> getAllTypeAuteurs() {
        return ResponseEntity.ok().body(typeAuteurService.getAllTypesAuteur());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeAuteur> getTypeAuteurById(@PathVariable("id") Integer id) {
        TypeAuteur type = typeAuteurService.getTypeAuteurById(id);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(type);
    }

    @GetMapping("/label/{label}")
    public ResponseEntity<TypeAuteur> getTypeAuteurByLabel(@PathVariable("label") String label) {
        TypeAuteur type = typeAuteurService.getTypeByLabel(label);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(type);
    }

    @PostMapping("/")
    public ResponseEntity<TypeAuteur> saveTypeAuteur(@RequestBody TypeAuteur typeAuteur) {
        return ResponseEntity.ok().body(typeAuteurService.saveTypeAuteur(typeAuteur));
    }

    @PutMapping("/")
    public ResponseEntity<TypeAuteur> updateTypeAuteur(@RequestBody TypeAuteur typeAuteur) {
        return ResponseEntity.ok().body(typeAuteurService.updateTypeAuteur(typeAuteur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTypeAuteurById(@PathVariable("id") Integer id) {
        try {
            typeAuteurService.deleteTypeAuteurById(id);
            return ResponseEntity.ok().body("Deleted typeAuteur successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}