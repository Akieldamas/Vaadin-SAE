package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.TypeAuteur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
public class TypeAuteurServiceTest {

    @Autowired
    private TypeAuteurService typeAuteurService;

    @Test
    void testGetAllTypesAuteur() {
        List<TypeAuteur> types = typeAuteurService.getAllTypesAuteur();
        assertThat(types).isNotEmpty();
    }

    @Test
    void testGetAllTypesAuteur_premierEstEcrivain() {
        List<TypeAuteur> types = typeAuteurService.getAllTypesAuteur();
        assertThat(types.get(0).getLabel()).isEqualTo("Écrivain");
    }

    @Test
    void testGetTypeAuteurById1_estEcrivain() {
        TypeAuteur t = typeAuteurService.getTypeAuteurById(1);
        assertThat(t).isNotNull();
        assertThat(t.getLabel()).isEqualTo("Écrivain");
    }

    @Test
    void testGetTypeAuteurById2_estRealisateur() {
        assertThat(typeAuteurService.getTypeAuteurById(2).getLabel()).isEqualTo("Réalisateur");
    }

    @Test
    void testGetTypeAuteurById_idInexistantRetourneNull() {
        assertThat(typeAuteurService.getTypeAuteurById(99999)).isNull();
    }

    @Test
    void testGetTypeByLabel() {
        TypeAuteur t = typeAuteurService.getTypeByLabel("Écrivain");
        assertThat(t).isNotNull();
        assertThat(t.getId()).isEqualTo(1);
    }

    @Test
    void testGetTypeByLabel_inexistantRetourneNull() {
        assertThat(typeAuteurService.getTypeByLabel("LabelInexistant999")).isNull();
    }

    @Test
    @Transactional
    void testSaveTypeAuteur() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("Test");
        TypeAuteur saved = typeAuteurService.saveTypeAuteur(t);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @Transactional
    void testUpdateTypeAuteur() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("LabelAvant");
        TypeAuteur saved = typeAuteurService.saveTypeAuteur(t);
        saved.setLabel("LabelApres");
        typeAuteurService.updateTypeAuteur(saved);
        assertThat(typeAuteurService.getTypeAuteurById(saved.getId()).getLabel()).isEqualTo("LabelApres");
    }

    @Test
    @Transactional
    void testDeleteTypeAuteurById() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("TypeASupprimer");
        Integer id = typeAuteurService.saveTypeAuteur(t).getId();
        typeAuteurService.deleteTypeAuteurById(id);
        assertThat(typeAuteurService.getTypeAuteurById(id)).isNull();
    }
}