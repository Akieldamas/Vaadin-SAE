package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.service.TypeAuteurService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TypeAuteurController.class)
public class TypeAuteurControllerMockTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TypeAuteurService typeAuteurService;
    @Autowired private ObjectMapper objectMapper;

    private TypeAuteur buildType(Integer id, String label) {
        TypeAuteur t = new TypeAuteur();
        t.setId(id);
        t.setLabel(label);
        return t;
    }

    @Test
    void testGetAllTypeAuteurs_retourne200() throws Exception {
        when(typeAuteurService.getAllTypesAuteur()).thenReturn(List.of(buildType(1, "Écrivain")));

        mockMvc.perform(get("/biblio/typeauteur/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Écrivain"));
    }

    @Test
    void testGetTypeAuteurById_trouve_retourne200() throws Exception {
        when(typeAuteurService.getTypeAuteurById(1)).thenReturn(buildType(1, "Écrivain"));

        mockMvc.perform(get("/biblio/typeauteur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("Écrivain"));
    }

    @Test
    void testGetTypeAuteurById_introuvable_retourne404() throws Exception {
        when(typeAuteurService.getTypeAuteurById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/typeauteur/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTypeAuteurByLabel_trouve_retourne200() throws Exception {
        when(typeAuteurService.getTypeByLabel("Réalisateur")).thenReturn(buildType(2, "Réalisateur"));

        mockMvc.perform(get("/biblio/typeauteur/label/Réalisateur"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testGetTypeAuteurByLabel_introuvable_retourne404() throws Exception {
        when(typeAuteurService.getTypeByLabel("LabelInexistant999")).thenReturn(null);

        mockMvc.perform(get("/biblio/typeauteur/label/LabelInexistant999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveTypeAuteur_retourne200EtAppelleService() throws Exception {
        TypeAuteur toSend = buildType(null, "Nouveau Type");
        TypeAuteur saved = buildType(6, "Nouveau Type");
        when(typeAuteurService.saveTypeAuteur(any(TypeAuteur.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/typeauteur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6));

        verify(typeAuteurService, times(1)).saveTypeAuteur(any(TypeAuteur.class));
    }

    @Test
    void testUpdateTypeAuteur_retourne200EtAppelleService() throws Exception {
        TypeAuteur toSend = buildType(6, "NouveauLabel");
        when(typeAuteurService.updateTypeAuteur(any(TypeAuteur.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/typeauteur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("NouveauLabel"));

        verify(typeAuteurService, times(1)).updateTypeAuteur(any(TypeAuteur.class));
    }

    @Test
    void testDeleteTypeAuteurById_retourne200() throws Exception {
        doNothing().when(typeAuteurService).deleteTypeAuteurById(6);

        mockMvc.perform(delete("/biblio/typeauteur/6"))
                .andExpect(status().isOk());

        verify(typeAuteurService, times(1)).deleteTypeAuteurById(6);
    }

    @Test
    void testDeleteTypeAuteurById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(typeAuteurService).deleteTypeAuteurById(99999);

        mockMvc.perform(delete("/biblio/typeauteur/99999"))
                .andExpect(status().isInternalServerError());
    }
}