package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuteurController.class)
public class AuteurControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuteurService auteurService;

    @Autowired
    private ObjectMapper objectMapper;

    private Auteur buildAuteur(Integer id, String nom, String prenom, LocalDate naissance) {
        Auteur a = new Auteur();
        a.setId(id);
        a.setNom(nom);
        a.setPrenom(prenom);
        a.setNationalite("Française");
        a.setDateNaissance(naissance);
        a.setTypes(new ArrayList<>());
        return a;
    }

    @Test
    void testGetAllAuteurs_retourne200() throws Exception {
        Auteur orwell = buildAuteur(1, "Orwell", "George", LocalDate.of(1903, 6, 25));
        when(auteurService.getAllAuteurs()).thenReturn(List.of(orwell));

        mockMvc.perform(get("/biblio/auteur/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Orwell"));
    }

    @Test
    void testGetAuteurById_trouve_retourne200() throws Exception {
        Auteur orwell = buildAuteur(1, "Orwell", "George", LocalDate.of(1903, 6, 25));
        when(auteurService.getAuteurById(1)).thenReturn(orwell);

        mockMvc.perform(get("/biblio/auteur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Orwell"))
                .andExpect(jsonPath("$.prenom").value("George"));
    }

    @Test
    void testGetAuteurById_introuvable_retourne404() throws Exception {
        when(auteurService.getAuteurById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/auteur/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAuteursByNom_retourne200() throws Exception {
        Auteur orwell = buildAuteur(1, "Orwell", "George", LocalDate.of(1903, 6, 25));
        when(auteurService.getAuteursByNom("Orwell")).thenReturn(List.of(orwell));

        mockMvc.perform(get("/biblio/auteur/nom/Orwell"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prenom").value("George"));
    }

    @Test
    void testGetAuteursByNom_inexistantRetourneListeVide() throws Exception {
        when(auteurService.getAuteursByNom("NomInexistant999")).thenReturn(List.of());

        mockMvc.perform(get("/biblio/auteur/nom/NomInexistant999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAuteursByNomAndPrenom_retourne200() throws Exception {
        Auteur orwell = buildAuteur(1, "Orwell", "George", LocalDate.of(1903, 6, 25));
        when(auteurService.getAuteursByNomAndPrenom("Orwell", "George")).thenReturn(List.of(orwell));

        mockMvc.perform(get("/biblio/auteur/search").param("nom", "Orwell").param("prenom", "George"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testGetAuteursByNomAndPrenom_pairingIncorrecteRetourneVide() throws Exception {
        when(auteurService.getAuteursByNomAndPrenom("Orwell", "Albert")).thenReturn(List.of());

        mockMvc.perform(get("/biblio/auteur/search").param("nom", "Orwell").param("prenom", "Albert"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAuteursByNomLikeAndPrenomLike_retourne200() throws Exception {
        Auteur orwell = buildAuteur(1, "Orwell", "George", LocalDate.of(1903, 6, 25));
        when(auteurService.getAuteursByNomLikeAndPrenomLike("Orwell", "George")).thenReturn(List.of(orwell));

        mockMvc.perform(get("/biblio/auteur/searchLike").param("nom", "Orwell").param("prenom", "George"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Orwell"));
    }

    @Test
    void testGetAuteursByNomLikeAndPrenomLike_introuvableRetourneVide() throws Exception {
        when(auteurService.getAuteursByNomLikeAndPrenomLike("ZZZZ", "YYYY")).thenReturn(List.of());

        mockMvc.perform(get("/biblio/auteur/searchLike").param("nom", "ZZZZ").param("prenom", "YYYY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSaveAuteur_retourne200EtAppelleService() throws Exception {
        Auteur toSend = buildAuteur(null, "Zola", "Emile", LocalDate.of(1840, 4, 2));
        Auteur saved = buildAuteur(7, "Zola", "Emile", LocalDate.of(1840, 4, 2));
        when(auteurService.saveAuteur(any(Auteur.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/auteur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.nom").value("Zola"));

        verify(auteurService, times(1)).saveAuteur(any(Auteur.class));
    }

    @Test
    void testUpdateAuteur_retourne200EtAppelleService() throws Exception {
        Auteur toSend = buildAuteur(7, "NouveauNom", "Emile", LocalDate.of(1840, 4, 2));
        when(auteurService.updateAuteur(any(Auteur.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/auteur/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));

        verify(auteurService, times(1)).updateAuteur(any(Auteur.class));
    }

    @Test
    void testDeleteAuteurById_retourne200() throws Exception {
        doNothing().when(auteurService).deleteAuteurById(7);

        mockMvc.perform(delete("/biblio/auteur/7"))
                .andExpect(status().isOk());

        verify(auteurService, times(1)).deleteAuteurById(7);
    }

    @Test
    void testDeleteAuteurById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(auteurService).deleteAuteurById(99999);

        mockMvc.perform(delete("/biblio/auteur/99999"))
                .andExpect(status().isInternalServerError());
    }
}