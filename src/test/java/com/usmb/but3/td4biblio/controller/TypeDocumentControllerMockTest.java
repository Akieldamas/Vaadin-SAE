package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.TypeDocumentService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TypeDocumentController.class)
public class TypeDocumentControllerMockTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TypeDocumentService typeDocumentService;
    @Autowired private ObjectMapper objectMapper;

    private TypeDocument buildType(Integer id, String nom) {
        TypeDocument t = new TypeDocument();
        t.setId(id);
        t.setNom(nom);
        return t;
    }

    @Test
    void testGetAllTypeDocuments_retourne200() throws Exception {
        when(typeDocumentService.getAllTypeDocuments()).thenReturn(List.of(buildType(1, "Livre")));

        mockMvc.perform(get("/biblio/typedocument/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Livre"));
    }

    @Test
    void testGetTypeDocumentById_trouve_retourne200() throws Exception {
        when(typeDocumentService.getTypeDocumentById(1)).thenReturn(buildType(1, "Livre"));

        mockMvc.perform(get("/biblio/typedocument/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Livre"));
    }

    @Test
    void testGetTypeDocumentById_introuvable_retourne404() throws Exception {
        when(typeDocumentService.getTypeDocumentById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/typedocument/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTypeDocumentByNom_trouve_retourne200() throws Exception {
        when(typeDocumentService.getTypeDocumentByNom("Film")).thenReturn(buildType(2, "Film"));

        mockMvc.perform(get("/biblio/typedocument/nom/Film"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testGetTypeDocumentByNom_introuvable_retourne404() throws Exception {
        when(typeDocumentService.getTypeDocumentByNom("NomInexistant999")).thenReturn(null);

        mockMvc.perform(get("/biblio/typedocument/nom/NomInexistant999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveTypeDocument_retourne200EtAppelleService() throws Exception {
        TypeDocument toSend = buildType(null, "Nouveau Type");
        TypeDocument saved = buildType(10, "Nouveau Type");
        when(typeDocumentService.saveTypeDocument(any(TypeDocument.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/typedocument/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(typeDocumentService, times(1)).saveTypeDocument(any(TypeDocument.class));
    }

    @Test
    void testUpdateTypeDocument_retourne200EtAppelleService() throws Exception {
        TypeDocument toSend = buildType(10, "NouveauNom");
        when(typeDocumentService.updateTypeDocument(any(TypeDocument.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/typedocument/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));

        verify(typeDocumentService, times(1)).updateTypeDocument(any(TypeDocument.class));
    }

    @Test
    void testDeleteTypeDocumentById_retourne200() throws Exception {
        doNothing().when(typeDocumentService).deleteTypeDocumentById(10);

        mockMvc.perform(delete("/biblio/typedocument/10"))
                .andExpect(status().isOk());

        verify(typeDocumentService, times(1)).deleteTypeDocumentById(10);
    }

    @Test
    void testDeleteTypeDocumentById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(typeDocumentService).deleteTypeDocumentById(99999);

        mockMvc.perform(delete("/biblio/typedocument/99999"))
                .andExpect(status().isInternalServerError());
    }
}