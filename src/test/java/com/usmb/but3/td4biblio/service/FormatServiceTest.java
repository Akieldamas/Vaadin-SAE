package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Format;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seed formats :
 *  1 longueur=17.5  largeur=10.8  poids=180   → Livre poche
 *  2 longueur=24.0  largeur=16.0  poids=420   → Livre grand format
 *  3 longueur=14.2  largeur=12.5  poids=80    → CD audio
 *  4 longueur=19.0  largeur=13.5  poids=100   → DVD
 *  5 longueur=17.2  largeur=13.5  poids=120   → Blu-ray
 */
@SpringBootTest
public class FormatServiceTest {

    @Autowired
    private FormatService formatService;

    // =========================================================================
    // getAllFormats
    // =========================================================================

    @Test
    void testGetAllFormats_retourne5Formats() {
        List<Format> formats = formatService.getAllFormats();
        assertThat(formats).hasSizeGreaterThanOrEqualTo(5);
    }

    @Test
    void testGetAllFormats_triParIdAscendant() {
        List<Format> formats = formatService.getAllFormats();
        for (int i = 0; i < formats.size() - 1; i++) {
            assertThat(formats.get(i).getId()).isLessThan(formats.get(i + 1).getId());
        }
    }

    @Test
    void testGetAllFormats_tousOntDimensions() {
        formatService.getAllFormats().forEach(f -> {
            assertThat(f.getLongueur()).isNotNull().isPositive();
            assertThat(f.getLargeur()).isNotNull().isPositive();
            assertThat(f.getPoids()).isNotNull().isPositive();
        });
    }

    // =========================================================================
    // getFormatById
    // =========================================================================

    @Test
    void testGetFormatById1_estLivrePoche() {
        Format f = formatService.getFormatById(1);
        assertThat(f).isNotNull();
        assertThat(f.getLongueur()).isEqualByComparingTo(new BigDecimal("17.5"));
        assertThat(f.getLargeur()).isEqualByComparingTo(new BigDecimal("10.8"));
        assertThat(f.getPoids()).isEqualByComparingTo(new BigDecimal("180"));
    }

    @Test
    void testGetFormatById2_estLivreGrandFormat() {
        Format f = formatService.getFormatById(2);
        assertThat(f.getLongueur()).isEqualByComparingTo(new BigDecimal("24.0"));
        assertThat(f.getLargeur()).isEqualByComparingTo(new BigDecimal("16.0"));
        assertThat(f.getPoids()).isEqualByComparingTo(new BigDecimal("420"));
    }

    @Test
    void testGetFormatById3_estCD() {
        Format f = formatService.getFormatById(3);
        assertThat(f.getLongueur()).isEqualByComparingTo(new BigDecimal("14.2"));
        assertThat(f.getLargeur()).isEqualByComparingTo(new BigDecimal("12.5"));
        assertThat(f.getPoids()).isEqualByComparingTo(new BigDecimal("80"));
    }

    @Test
    void testGetFormatById4_estDVD() {
        Format f = formatService.getFormatById(4);
        assertThat(f.getLongueur()).isEqualByComparingTo(new BigDecimal("19.0"));
        assertThat(f.getLargeur()).isEqualByComparingTo(new BigDecimal("13.5"));
        assertThat(f.getPoids()).isEqualByComparingTo(new BigDecimal("100"));
    }

    @Test
    void testGetFormatById5_estBluray() {
        Format f = formatService.getFormatById(5);
        assertThat(f.getLongueur()).isEqualByComparingTo(new BigDecimal("17.2"));
        assertThat(f.getLargeur()).isEqualByComparingTo(new BigDecimal("13.5"));
        assertThat(f.getPoids()).isEqualByComparingTo(new BigDecimal("120"));
    }

    @Test
    void testGetFormatById_idInexistantRetourneNull() {
        assertThat(formatService.getFormatById(99999)).isNull();
    }

    // =========================================================================
    // findByDimensions
    // =========================================================================

    @Test
    void testFindByDimensions_livrePoche_retourneFormat1() {
        List<Format> formats = formatService.findByDimensions(
                new BigDecimal("17.5"), new BigDecimal("10.8"));
        assertThat(formats).hasSize(1);
        assertThat(formats.get(0).getId()).isEqualTo(1);
        assertThat(formats.get(0).getPoids()).isEqualByComparingTo(new BigDecimal("180"));
    }

    @Test
    void testFindByDimensions_DVD_retourneFormat4() {
        List<Format> formats = formatService.findByDimensions(
                new BigDecimal("19.0"), new BigDecimal("13.5"));
        assertThat(formats).isNotEmpty();
        assertThat(formats).anySatisfy(f -> assertThat(f.getId()).isEqualTo(4));
    }

    @Test
    void testFindByDimensions_dimensionsInexistantesRetourneVide() {
        List<Format> formats = formatService.findByDimensions(
                new BigDecimal("999.99"), new BigDecimal("888.88"));
        assertThat(formats).isEmpty();
    }

    // =========================================================================
    // saveFormat
    // =========================================================================

    @Test
    @Transactional
    void testSaveFormat_idGenereApres5() {
        Format f = buildFormat("21.00", "14.80", "350.00");
        Format saved = formatService.saveFormat(f);
        assertThat(saved.getId()).isNotNull().isGreaterThan(5);
    }

    @Test
    @Transactional
    void testSaveFormat_dimensionsPersistent() {
        Format f = buildFormat("21.00", "14.80", "350.00");
        Format saved = formatService.saveFormat(f);
        Format found = formatService.getFormatById(saved.getId());

        assertThat(found.getLongueur()).isEqualByComparingTo(new BigDecimal("21.00"));
        assertThat(found.getLargeur()).isEqualByComparingTo(new BigDecimal("14.80"));
        assertThat(found.getPoids()).isEqualByComparingTo(new BigDecimal("350.00"));
    }

    // =========================================================================
    // updateFormat
    // =========================================================================

    @Test
    @Transactional
    void testUpdateFormat_poidsMisAJour() {
        Format f = buildFormat("10.00", "7.00", "200.00");
        Format saved = formatService.saveFormat(f);
        saved.setPoids(new BigDecimal("250.00"));
        formatService.updateFormat(saved);
        assertThat(formatService.getFormatById(saved.getId()).getPoids())
                .isEqualByComparingTo(new BigDecimal("250.00"));
    }

    // =========================================================================
    // deleteFormatById
    // =========================================================================

    @Test
    @Transactional
    void testDeleteFormatById_formatPlusPresent() {
        Integer id = formatService.saveFormat(buildFormat("5.00", "3.00", "50.00")).getId();
        formatService.deleteFormatById(id);
        assertThat(formatService.getFormatById(id)).isNull();
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Format buildFormat(String longueur, String largeur, String poids) {
        Format f = new Format();
        f.setLongueur(new BigDecimal(longueur));
        f.setLargeur(new BigDecimal(largeur));
        f.setPoids(new BigDecimal(poids));
        return f;
    }
}