package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.service.BibliothequeService;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BibliothequeController.class)
public class BibliothequeControllerMockTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BibliothequeService bibliothequeService;
    @Autowired private ObjectMapper objectMapper;

    private Bibliotheque buildBibliotheque(Integer id, String nom) {
        Bibliotheque b = new Bibliotheque();
        b.setId(id);
        b.setNom(nom);
        b.setAdresse("1 rue des Livres");
        b.setHoraireOuverture(LocalTime.of(8, 0));
        b.setHoraireFermeture(LocalTime.of(18, 0));
        return b;
    }

    @Test
    void testGetAllBibliotheques_retourne200() throws Exception {
        Bibliotheque b1 = buildBibliotheque(1, "Médiathèque La Turbine");
        when(bibliothequeService.getAllBibliotheques()).thenReturn(List.of(b1));

        mockMvc.perform(get("/biblio/bibliotheque/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Médiathèque La Turbine"));
    }

    @Test
    void testGetBibliothequeById_trouve_retourne200() throws Exception {
        Bibliotheque b1 = buildBibliotheque(1, "Médiathèque La Turbine");
        when(bibliothequeService.getBibliothequeById(1)).thenReturn(b1);

        mockMvc.perform(get("/biblio/bibliotheque/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Médiathèque La Turbine"));
    }

    @Test
    void testGetBibliothequeById_introuvable_retourne404() throws Exception {
        when(bibliothequeService.getBibliothequeById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/bibliotheque/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBibliothequeByNom_trouve_retourne200() throws Exception {
        Bibliotheque b1 = buildBibliotheque(1, "Médiathèque La Turbine");
        when(bibliothequeService.getBibliothequeByNom("Médiathèque La Turbine")).thenReturn(b1);

        mockMvc.perform(get("/biblio/bibliotheque/nom/Médiathèque La Turbine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetBibliothequeByNom_introuvable_retourne404() throws Exception {
        when(bibliothequeService.getBibliothequeByNom("NomInexistant999")).thenReturn(null);

        mockMvc.perform(get("/biblio/bibliotheque/nom/NomInexistant999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveBibliotheque_retourne200EtAppelleService() throws Exception {
        Bibliotheque toSend = buildBibliotheque(null, "Nouvelle Biblio");
        Bibliotheque saved = buildBibliotheque(5, "Nouvelle Biblio");
        when(bibliothequeService.saveBibliotheque(any(Bibliotheque.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/bibliotheque/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        verify(bibliothequeService, times(1)).saveBibliotheque(any(Bibliotheque.class));
    }

    @Test
    void testUpdateBibliotheque_retourne200EtAppelleService() throws Exception {
        Bibliotheque toSend = buildBibliotheque(5, "NouveauNom");
        when(bibliothequeService.updateBibliotheque(any(Bibliotheque.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/bibliotheque/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("NouveauNom"));

        verify(bibliothequeService, times(1)).updateBibliotheque(any(Bibliotheque.class));
    }

    @Test
    void testDeleteBibliothequeById_retourne200() throws Exception {
        doNothing().when(bibliothequeService).deleteBibliothequeById(5);

        mockMvc.perform(delete("/biblio/bibliotheque/5"))
                .andExpect(status().isOk());

        verify(bibliothequeService, times(1)).deleteBibliothequeById(5);
    }

    @Test
    void testDeleteBibliothequeById_serviceThrows_retourne500() throws Exception {
        doThrow(new RuntimeException("boom")).when(bibliothequeService).deleteBibliothequeById(99999);

        mockMvc.perform(delete("/biblio/bibliotheque/99999"))
                .andExpect(status().isInternalServerError());
    }
}