package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.service.FormatService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormatController.class)
public class FormatControllerMockTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private FormatService formatService;
    @Autowired private ObjectMapper objectMapper;

    private Format buildFormat(Integer id, String longueur, String largeur, String poids) {
        Format f = new Format();
        f.setId(id);
        f.setLongueur(new BigDecimal(longueur));
        f.setLargeur(new BigDecimal(largeur));
        f.setPoids(new BigDecimal(poids));
        return f;
    }

    @Test
    void testGetAllFormats_retourne200() throws Exception {
        Format f1 = buildFormat(1, "17.5", "10.8", "180");
        when(formatService.getAllFormats()).thenReturn(List.of(f1));

        mockMvc.perform(get("/biblio/format/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].longueur").value(17.5));
    }

    @Test
    void testGetFormatById_trouve_retourne200() throws Exception {
        Format f1 = buildFormat(1, "17.5", "10.8", "180");
        when(formatService.getFormatById(1)).thenReturn(f1);

        mockMvc.perform(get("/biblio/format/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poids").value(180));
    }

    @Test
    void testGetFormatById_introuvable_retourne404() throws Exception {
        when(formatService.getFormatById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/format/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFormatsByDimensions_retourne200() throws Exception {
        Format f1 = buildFormat(1, "17.5", "10.8", "180");
        when(formatService.findByDimensions(new BigDecimal("17.5"), new BigDecimal("10.8")))
                .thenReturn(List.of(f1));

        mockMvc.perform(get("/biblio/format/dimensions")
                        .param("longueur", "17.5")
                        .param("largeur", "10.8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetFormatsByDimensions_introuvableRetourneVide() throws Exception {
        when(formatService.findByDimensions(new BigDecimal("99.9"), new BigDecimal("99.9")))
                .thenReturn(List.of());

        mockMvc.perform(get("/biblio/format/dimensions")
                        .param("longueur", "99.9")
                        .param("largeur", "99.9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSaveFormat_retourne200EtAppelleService() throws Exception {
        Format toSend = buildFormat(null, "12.0", "8.0", "50");
        Format saved = buildFormat(6, "12.0", "8.0", "50");
        when(formatService.saveFormat(any(Format.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/format/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6));

        verify(formatService, times(1)).saveFormat(any(Format.class));
    }

    @Test
    void testUpdateFormat_retourne200EtAppelleService() throws Exception {
        Format toSend = buildFormat(6, "12.0", "8.0", "99");
        when(formatService.updateFormat(any(Format.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/format/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.poids").value(99));

        verify(formatService, times(1)).updateFormat(any(Format.class));
    }

    @Test
    void testDeleteFormatById_retourne200() throws Exception {
        doNothing().when(formatService).deleteFormatById(6);

        mockMvc.perform(delete("/biblio/format/6"))
                .andExpect(status().isOk());

        verify(formatService, times(1)).deleteFormatById(6);
    }

    @Test
    void testDeleteFormatById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(formatService).deleteFormatById(99999);

        mockMvc.perform(delete("/biblio/format/99999"))
                .andExpect(status().isInternalServerError());
    }
}