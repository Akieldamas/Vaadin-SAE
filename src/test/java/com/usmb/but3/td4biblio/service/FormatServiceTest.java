package com.usmb.but3.td4biblio.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.usmb.but3.td4biblio.entity.Format;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class FormatServiceTest {

    @Autowired
    private FormatService formatService;

    @Test
    void testGetAllFormats_retourne5Formats() {
        List<Format> formats = formatService.getAllFormats();
        assertThat(formats).isNotEmpty();
    }

    @Test
    void testGetAllFormats_tousOntDimensions() {
        formatService.getAllFormats().forEach(f -> {
            assertThat(f.getLongueur()).isNotNull().isPositive();
            assertThat(f.getLargeur()).isNotNull().isPositive();
            assertThat(f.getPoids()).isNotNull().isPositive();
        });
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
        assertThat(formats.get(0).getId()).isEqualTo(1);
    }

    @Test
    void testFindByDimensions_dimensionsInexistantesRetourneVide() {
        List<Format> formats = formatService.findByDimensions(
                new BigDecimal("999.99"), new BigDecimal("888.88"));
        assertThat(formats).isEmpty();
    }

    @Test
    @Transactional
    void testSaveFormat() {
        Format f = buildFormat("21.00", "14.80", "350.00");
        Format saved = formatService.saveFormat(f);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @Transactional
    void testUpdateFormat() {
        Format f = buildFormat("10.00", "7.00", "200.00");
        Format saved = formatService.saveFormat(f);
        saved.setPoids(new BigDecimal("250.00"));
        formatService.updateFormat(saved);
        assertThat(formatService.getFormatById(saved.getId()).getPoids())
                .isEqualByComparingTo(new BigDecimal("250.00"));
    }

    @Test
    @Transactional
    void testDeleteFormatById() {
        int id = formatService.saveFormat(buildFormat("5.00", "3.00", "50.00")).getId();
        formatService.deleteFormatById(id);
        assertThat(formatService.getFormatById(id)).isNull();
    }

    private Format buildFormat(String longueur, String largeur, String poids) {
        Format f = new Format();
        f.setLongueur(new BigDecimal(longueur));
        f.setLargeur(new BigDecimal(largeur));
        f.setPoids(new BigDecimal(poids));
        return f;
    }
}