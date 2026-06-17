package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.TypeAuteur;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

/**
 * Seed types auteur :
 *  1 Écrivain  2 Réalisateur  3 Compositeur  4 Scénariste  5 Illustrateur
 */
@SpringBootTest
public class TypeAuteurServiceTest {

    @Autowired
    private TypeAuteurService typeAuteurService;

    @Test
    void testGetAllTypesAuteur_retourne5Types() {
        List<TypeAuteur> types = typeAuteurService.getAllTypesAuteur();
        assertThat(types).hasSizeGreaterThanOrEqualTo(5);
    }

    @Test
    void testGetAllTypesAuteur_premierEstEcrivain() {
        List<TypeAuteur> types = typeAuteurService.getAllTypesAuteur();
        assertThat(types.get(0).getLabel()).isEqualTo("Écrivain");
    }

    @Test
    void testGetAllTypesAuteur_triParIdAscendant() {
        List<TypeAuteur> types = typeAuteurService.getAllTypesAuteur();
        for (int i = 0; i < types.size() - 1; i++) {
            assertThat(types.get(i).getId()).isLessThan(types.get(i + 1).getId());
        }
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
    void testGetTypeAuteurById3_estCompositeur() {
        assertThat(typeAuteurService.getTypeAuteurById(3).getLabel()).isEqualTo("Compositeur");
    }

    @Test
    void testGetTypeAuteurById4_estScenariste() {
        assertThat(typeAuteurService.getTypeAuteurById(4).getLabel()).isEqualTo("Scénariste");
    }

    @Test
    void testGetTypeAuteurById5_estIllustrateur() {
        assertThat(typeAuteurService.getTypeAuteurById(5).getLabel()).isEqualTo("Illustrateur");
    }

    @Test
    void testGetTypeAuteurById_idInexistantRetourneNull() {
        assertThat(typeAuteurService.getTypeAuteurById(99999)).isNull();
    }

    @Test
    void testGetTypeByLabel_Ecrivain_retourneId1() {
        TypeAuteur t = typeAuteurService.getTypeByLabel("Écrivain");
        assertThat(t).isNotNull();
        assertThat(t.getId()).isEqualTo(1);
    }

    @Test
    void testGetTypeByLabel_Realisateur_retourneId2() {
        TypeAuteur t = typeAuteurService.getTypeByLabel("Réalisateur");
        assertThat(t.getId()).isEqualTo(2);
    }

    @Test
    void testGetTypeByLabel_inexistantRetourneNull() {
        assertThat(typeAuteurService.getTypeByLabel("LabelInexistant999")).isNull();
    }

    @Test
    @Transactional
    void testSaveTypeAuteur_idGenereApres5() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("Dramaturge Test");
        TypeAuteur saved = typeAuteurService.saveTypeAuteur(t);
        assertThat(saved.getId()).isNotNull().isGreaterThan(5);
        assertThat(saved.getLabel()).isEqualTo("Dramaturge Test");
    }

    @Test
    @Transactional
    void testUpdateTypeAuteur_labelMisAJour() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("LabelAvant");
        TypeAuteur saved = typeAuteurService.saveTypeAuteur(t);
        saved.setLabel("LabelApres");
        typeAuteurService.updateTypeAuteur(saved);
        assertThat(typeAuteurService.getTypeAuteurById(saved.getId()).getLabel()).isEqualTo("LabelApres");
    }

    @Test
    @Transactional
    void testDeleteTypeAuteurById_typePlusPresent() {
        TypeAuteur t = new TypeAuteur();
        t.setLabel("TypeASupprimer");
        Integer id = typeAuteurService.saveTypeAuteur(t).getId();
        typeAuteurService.deleteTypeAuteurById(id);
        assertThat(typeAuteurService.getTypeAuteurById(id)).isNull();
    }
}