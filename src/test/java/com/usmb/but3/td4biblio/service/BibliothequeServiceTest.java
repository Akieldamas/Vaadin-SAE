package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Bibliotheque;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class BibliothequeServiceTest {

    @Autowired
    private BibliothequeService bibliothequeService;

    @Test
    void testGetAllBibliotheques_premierEstLaTurbine() {
        List<Bibliotheque> bibliotheques = bibliothequeService.getAllBibliotheques();
        assertThat(bibliotheques.get(0).getNom()).isEqualTo("Médiathèque La Turbine");
    }

    @Test
    void testGetBibliothequeById1() {
        Bibliotheque bibliotheque = bibliothequeService.getBibliothequeById(1);
        assertThat(bibliotheque).isNotNull();
        assertThat(bibliotheque.getNom()).isEqualTo("Médiathèque La Turbine");
        assertThat(bibliotheque.getHoraireOuverture()).isEqualTo(LocalTime.of(8, 0));
        assertThat(bibliotheque.getHoraireFermeture()).isEqualTo(LocalTime.of(18, 0));
    }

    @Test
    void testGetBibliothequeById_idInexistant() {
        assertThat(bibliothequeService.getBibliothequeById(99999)).isNull();
    }

    @Test
    @Transactional
    void testSaveBibliotheque() {
        Bibliotheque bibliotheque = buildBibliotheque("Nouvelle Bibliotheque",
                LocalTime.of(9, 0), LocalTime.of(19, 0));
        Bibliotheque saved = bibliothequeService.saveBibliotheque(bibliotheque);
        Bibliotheque found = bibliothequeService.getBibliothequeById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Nouvelle Bibliotheque");
        assertThat(found.getAdresse()).isEqualTo("1 rue des Livres");
        assertThat(found.getHoraireOuverture()).isEqualTo(LocalTime.of(9, 0));
        assertThat(found.getHoraireFermeture()).isEqualTo(LocalTime.of(19, 0));
    }

    @Test
    @Transactional
    void testUpdateBibliotheque() {
        Bibliotheque bibliotheque = buildBibliotheque("AvantUpdate", LocalTime.of(8, 0), LocalTime.of(18, 0));
        Bibliotheque saved = bibliothequeService.saveBibliotheque(bibliotheque);

        saved.setNom("ApresUpdate");
        bibliothequeService.updateBibliotheque(saved);

        assertThat(bibliothequeService.getBibliothequeById(saved.getId()).getNom()).isEqualTo("ApresUpdate");
    }

    @Test
    @Transactional
    void testDeleteBibliothequeById() {
        Bibliotheque bibliotheque = buildBibliotheque("ASupprimer", LocalTime.of(8, 0), LocalTime.of(18, 0));
        Integer id = bibliothequeService.saveBibliotheque(bibliotheque).getId();

        bibliothequeService.deleteBibliothequeById(id);

        assertThat(bibliothequeService.getBibliothequeById(id)).isNull();
    }

    @Test
    void testGetBibliothequeByNom_LaTurbine_trouve() {
        Bibliotheque bibliotheque = bibliothequeService.getBibliothequeByNom("Médiathèque La Turbine");
        assertThat(bibliotheque).isNotNull();
        assertThat(bibliotheque.getId()).isEqualTo(1);
    }

    @Test
    void testGetBibliothequeByNom_inexistantRetourneNull() {
        assertThat(bibliothequeService.getBibliothequeByNom("NomInexistant")).isNull();
    }

    private Bibliotheque buildBibliotheque(String nom, LocalTime ouverture, LocalTime fermeture) {
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.setNom(nom);
        bibliotheque.setAdresse("1 rue des Livres");
        bibliotheque.setHoraireOuverture(ouverture);
        bibliotheque.setHoraireFermeture(fermeture);
        return bibliotheque;
    }
}