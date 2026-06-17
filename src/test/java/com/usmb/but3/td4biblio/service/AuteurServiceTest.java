package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Auteur;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

/**
 * Seed auteurs :
 *  1 Orwell       George        Britannique  1903-06-25 / 1950-01-21
 *  2 Camus        Albert        Française    1913-11-07 / 1960-01-04
 *  3 Le Guin      Ursula K.     Américaine   1929-10-21 / 2018-01-22
 *  4 King         Stephen       Américaine   1947-09-21 / vivant
 *  5 Nolan        Christopher   Britannique  1970-07-30 / vivant
 *  6 Spielberg    Steven        Américaine   1946-12-18 / vivant
 *  7 Zimmer       Hans          Allemande    1957-09-12 / vivant
 *  8 Cohen        Leonard       Canadienne   1934-09-21 / 2016-11-07
 *  9 Miyazaki     Hayao         Japonaise    1941-01-05 / vivant
 * 10 Duras        Marguerite    Française    1914-04-04 / 1996-03-03
 * 11 Tolkien      J.R.R.        Britannique  1892-01-03 / 1973-09-02
 * 12 Villeneuve   Denis         Canadienne   1967-10-03 / vivant
 */
@SpringBootTest
public class AuteurServiceTest {

    @Autowired
    private AuteurService auteurService;

    // =========================================================================
    // getAllAuteurs
    // =========================================================================

    @Test
    void testGetAllAuteurs_retourne12Auteurs() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        assertThat(auteurs).hasSizeGreaterThanOrEqualTo(12);
    }

    @Test
    void testGetAllAuteurs_premierEstOrwell() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAllAuteurs_deuxiemeEstCamus() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        assertThat(auteurs.get(1).getNom()).isEqualTo("Camus");
    }

    @Test
    void testGetAllAuteurs_triParIdAscendant() {
        List<Auteur> auteurs = auteurService.getAllAuteurs();
        for (int i = 0; i < auteurs.size() - 1; i++) {
            assertThat(auteurs.get(i).getId()).isLessThan(auteurs.get(i + 1).getId());
        }
    }

    // =========================================================================
    // getAuteurById
    // =========================================================================

    @Test
    void testGetAuteurById1_estOrwell() {
        Auteur a = auteurService.getAuteurById(1);
        assertThat(a).isNotNull();
        assertThat(a.getNom()).isEqualTo("Orwell");
        assertThat(a.getPrenom()).isEqualTo("George");
        assertThat(a.getNationalite()).isEqualTo("Britannique");
        assertThat(a.getDateNaissance()).isEqualTo(LocalDate.of(1903, 6, 25));
        assertThat(a.getDateDeces()).isEqualTo(LocalDate.of(1950, 1, 21));
    }

    @Test
    void testGetAuteurById4_estKing_vivant() {
        Auteur a = auteurService.getAuteurById(4);
        assertThat(a.getNom()).isEqualTo("King");
        assertThat(a.getPrenom()).isEqualTo("Stephen");
        assertThat(a.getDateDeces()).isNull();
    }

    @Test
    void testGetAuteurById8_estCohen() {
        Auteur a = auteurService.getAuteurById(8);
        assertThat(a.getNom()).isEqualTo("Cohen");
        assertThat(a.getPrenom()).isEqualTo("Leonard");
        assertThat(a.getNationalite()).isEqualTo("Canadienne");
        assertThat(a.getDateDeces()).isEqualTo(LocalDate.of(2016, 11, 7));
    }

    @Test
    void testGetAuteurById11_estTolkien() {
        Auteur a = auteurService.getAuteurById(11);
        assertThat(a.getNom()).isEqualTo("Tolkien");
        assertThat(a.getDateNaissance()).isEqualTo(LocalDate.of(1892, 1, 3));
    }

    @Test
    void testGetAuteurById_idInexistantRetourneNull() {
        assertThat(auteurService.getAuteurById(99999)).isNull();
    }

    // =========================================================================
    // saveAuteur
    // =========================================================================

    @Test
    @Transactional
    void testSaveAuteur_idGenereApres12() {
        Auteur a = buildAuteur("Zola", "Emile", "Française",
                LocalDate.of(1840, 4, 2), LocalDate.of(1902, 9, 29));
        Auteur saved = auteurService.saveAuteur(a);
        assertThat(saved.getId()).isNotNull().isGreaterThan(12);
    }

    @Test
    @Transactional
    void testSaveAuteur_tousChampsPersistents() {
        Auteur a = buildAuteur("Flaubert", "Gustave", "Française",
                LocalDate.of(1821, 12, 12), LocalDate.of(1880, 5, 8));
        Auteur saved = auteurService.saveAuteur(a);
        Auteur found = auteurService.getAuteurById(saved.getId());

        assertThat(found.getNom()).isEqualTo("Flaubert");
        assertThat(found.getPrenom()).isEqualTo("Gustave");
        assertThat(found.getNationalite()).isEqualTo("Française");
        assertThat(found.getDateNaissance()).isEqualTo(LocalDate.of(1821, 12, 12));
        assertThat(found.getDateDeces()).isEqualTo(LocalDate.of(1880, 5, 8));
    }

    @Test
    @Transactional
    void testSaveAuteur_vivantSansDateDeces() {
        Auteur a = buildAuteur("Modiano", "Patrick", "Française",
                LocalDate.of(1945, 7, 30), null);
        Auteur saved = auteurService.saveAuteur(a);
        assertThat(auteurService.getAuteurById(saved.getId()).getDateDeces()).isNull();
    }

    // =========================================================================
    // updateAuteur
    // =========================================================================

    @Test
    @Transactional
    void testUpdateAuteur_nomMisAJour() {
        Auteur a = buildAuteur("AvantUpdate", "Prenom", "Française", null, null);
        Auteur saved = auteurService.saveAuteur(a);
        saved.setNom("ApresUpdate");
        auteurService.updateAuteur(saved);
        assertThat(auteurService.getAuteurById(saved.getId()).getNom()).isEqualTo("ApresUpdate");
    }

    @Test
    @Transactional
    void testUpdateAuteur_nationaliteMiseAJour() {
        Auteur a = buildAuteur("NomTest", "PrenomTest", "Ancienne", null, null);
        Auteur saved = auteurService.saveAuteur(a);
        saved.setNationalite("Nouvelle");
        auteurService.updateAuteur(saved);
        assertThat(auteurService.getAuteurById(saved.getId()).getNationalite()).isEqualTo("Nouvelle");
    }

    // =========================================================================
    // deleteAuteurById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteAuteurById_auteurPlusPresent() {
        Auteur a = buildAuteur("ASupprimer", "Test", null, null, null);
        Integer id = auteurService.saveAuteur(a).getId();
        auteurService.deleteAuteurById(id);
        assertThat(auteurService.getAuteurById(id)).isNull();
    }

    // =========================================================================
    // getAuteursByNom
    // =========================================================================

    @Test
    void testGetAuteursByNom_Orwell_retourneUnSeul() {
        List<Auteur> auteurs = auteurService.getAuteursByNom("Orwell");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getPrenom()).isEqualTo("George");
    }

    @Test
    void testGetAuteursByNom_King_retourneKing() {
        List<Auteur> auteurs = auteurService.getAuteursByNom("King");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(4);
    }

    @Test
    void testGetAuteursByNom_inexistantRetourneVide() {
        assertThat(auteurService.getAuteursByNom("NomInexistant999")).isEmpty();
    }

    // =========================================================================
    // getAuteursByNomAndPrenom
    // =========================================================================

    @Test
    void testGetAuteursByNomAndPrenom_OrwellGeorge_retourne1() {
        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Orwell", "George");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testGetAuteursByNomAndPrenom_NolanChristopher_retourne1() {
        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Nolan", "Christopher");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getId()).isEqualTo(5);
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingIncorrectRetourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomAndPrenom("Orwell", "Albert");
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // getAuteursByNomLikeAndPrenomLike
    // =========================================================================

    @Test
    void testGetAuteursByNomLike_Or_Geo_trouveOrwell() {
        List<Auteur> auteurs = auteurService.getAuteursByNomLikeAndPrenomLike("Or", "Geo");
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs).anySatisfy(a -> assertThat(a.getNom()).isEqualTo("Orwell"));
    }

    @Test
    void testGetAuteursByNomLike_Zi_Han_trouveZimmer() {
        List<Auteur> auteurs = auteurService.getAuteursByNomLikeAndPrenomLike("Zi", "Han");
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs).anySatisfy(a -> assertThat(a.getNom()).isEqualTo("Zimmer"));
    }

    @Test
    void testGetAuteursByNomLike_inexistantRetourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomLikeAndPrenomLike("ZZZZ", "YYYY");
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // getByNomContainingIgnoreCase
    // =========================================================================

    @Test
    void testGetByNomContainingIgnoreCase_orwell_minuscules() {
        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("orwell");
        assertThat(auteurs).hasSize(1);
        assertThat(auteurs.get(0).getNom()).isEqualTo("Orwell");
    }

    @Test
    void testGetByNomContainingIgnoreCase_ORWELL_majuscules() {
        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("ORWELL");
        assertThat(auteurs).hasSize(1);
    }

    @Test
    void testGetByNomContainingIgnoreCase_an_trouvePlusieursAuteurs() {
        // Nolan, Le Guin -> "an" dans "Nolan" → au moins 1
        List<Auteur> auteurs = auteurService.getByNomContainingIgnoreCase("an");
        assertThat(auteurs).isNotEmpty();
    }

    // =========================================================================
    // getAuteursByNomStartWithIgnoreCase
    // =========================================================================

    @Test
    void testGetAuteursByNomStartWith_Or_trouveOrwell() {
        List<Auteur> auteurs = auteurService.getAuteursByNomStartWithIgnoreCase("Or");
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs).allSatisfy(a ->
                assertThat(a.getNom()).startsWithIgnoringCase("Or"));
    }

    @Test
    void testGetAuteursByNomStartWith_Ca_trouveCamus() {
        List<Auteur> auteurs = auteurService.getAuteursByNomStartWithIgnoreCase("Ca");
        assertThat(auteurs).isNotEmpty();
        assertThat(auteurs).anySatisfy(a -> assertThat(a.getNom()).isEqualTo("Camus"));
    }

    @Test
    void testGetAuteursByNomStartWith_XXX_retourneVide() {
        List<Auteur> auteurs = auteurService.getAuteursByNomStartWithIgnoreCase("XXX");
        assertThat(auteurs).isEmpty();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Auteur buildAuteur(String nom, String prenom, String nationalite,
                                LocalDate naissance, LocalDate deces) {
        Auteur a = new Auteur();
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setNationalite(nationalite);
        a.setDateNaissance(naissance);
        a.setDateDeces(deces);
        return a;
    }
}