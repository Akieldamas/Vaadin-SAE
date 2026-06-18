package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest loads ONLY the web layer for this one controller (no DB, no full app context).
// It auto-configures MockMvc for you, so no real HTTP port is opened.
@WebMvcTest(EditeurController.class)
public class EditeurControllerMockTest {

    @Autowired
    private MockMvc mockMvc; // simulates HTTP requests against the controller, in-memory

    @MockBean
    private EditeurService editeurService; // replaces the real service with a Mockito mock

    @Autowired
    private ObjectMapper objectMapper; // Spring's Jackson mapper, to turn objects into JSON strings

    private Editeur buildEditeur(Integer id, String nom) {
        Editeur e = new Editeur();
        e.setId(id);
        e.setNom(nom);
        e.setAdresse("1 rue de l'Edition");
        e.setLienSiteWeb("https://editeur.fr");
        return e;
    }

    @Test
    void testGetAllEditeurs_retourne200() throws Exception {
        Editeur e1 = buildEditeur(1, "Gallimard");
        // Tell the mock what to return WHEN the controller calls this method
        when(editeurService.getAllEditeurs()).thenReturn(List.of(e1));

        mockMvc.perform(get("/biblio/editeur/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Gallimard"));
    }

    @Test
    void testGetEditeurById_trouve_retourne200() throws Exception {
        Editeur e1 = buildEditeur(1, "Gallimard");
        when(editeurService.getEditeurById(1)).thenReturn(e1);

        mockMvc.perform(get("/biblio/editeur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Gallimard"));
    }

    @Test
    void testGetEditeurById_introuvable_retourne404() throws Exception {
        when(editeurService.getEditeurById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/editeur/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveEditeur_retourne200EtAppelleService() throws Exception {
        Editeur toSend = buildEditeur(null, "Nouvel Editeur");
        Editeur saved = buildEditeur(5, "Nouvel Editeur");

        // any(Editeur.class) because the exact object instance passed by Spring after
        // deserialization won't be == toSend, just equal in content
        when(editeurService.saveEditeur(any(Editeur.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/editeur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.nom").value("Nouvel Editeur"));

        verify(editeurService, times(1)).saveEditeur(any(Editeur.class));
    }

    @Test
    void testUpdateEditeur_retourne200EtAppelleService() throws Exception {
        Editeur toSend = buildEditeur(5, "NouveauNom");
        when(editeurService.updateEditeur(any(Editeur.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/editeur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));

        verify(editeurService, times(1)).updateEditeur(any(Editeur.class));
    }

    @Test
    void testDeleteEditeurById_retourne200() throws Exception {
        // deleteEditeurById returns void, so we just let it run without throwing
        doNothing().when(editeurService).deleteEditeurById(5);

        mockMvc.perform(delete("/biblio/editeur/5"))
                .andExpect(status().isOk());

        verify(editeurService, times(1)).deleteEditeurById(5);
    }

    @Test
    void testDeleteEditeurById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(editeurService).deleteEditeurById(99999);

        mockMvc.perform(delete("/biblio/editeur/99999"))
                .andExpect(status().isInternalServerError());
    }
}