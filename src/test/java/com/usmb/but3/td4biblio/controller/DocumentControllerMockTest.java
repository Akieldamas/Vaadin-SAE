package com.usmb.but3.td4biblio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.usmb.but3.td4biblio.entity.*;
import com.usmb.but3.td4biblio.service.DocumentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
public class DocumentControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Auteur buildAuteur(Integer id, String nom) {
        Auteur a = new Auteur();
        a.setId(id);
        a.setNom(nom);
        a.setPrenom("George");
        a.setDateNaissance(LocalDate.of(10, 10, 10));
        return a;
    }

    private Editeur buildEditeur(Integer id, String nom) {
        Editeur e = new Editeur();
        e.setId(id);
        e.setNom(nom);
        e.setAdresse("1 rue de l'Edition");
        e.setLienSiteWeb("https://editeur.fr");
        return e;
    }

    private Format buildFormat(Integer id) {
        Format f = new Format();
        f.setId(id);
        f.setLongueur(BigDecimal.valueOf(20.0));
        f.setLargeur(BigDecimal.valueOf(15.0));
        f.setPoids(BigDecimal.valueOf(0.5));
        return f;
    }

    private TypeDocument buildTypeDocument(Integer id, String nom) {
        TypeDocument t = new TypeDocument();
        t.setId(id);
        t.setNom(nom);
        return t;
    }

    private Bibliotheque buildBibliotheque(Integer id, String nom) {
        Bibliotheque b = new Bibliotheque();
        b.setId(id);
        b.setNom(nom);
        return b;
    }

    private Document buildDocument(Integer id, String titre) {
        Document doc = new Document();
        doc.setId(id);
        doc.setTitre(titre);
        doc.setDescription("Une description");
        doc.setDateAcquisition(LocalDate.of(2020, 1, 1));
        doc.setDatePublication(LocalDate.of(1949, 6, 8));
        doc.setAuteur(buildAuteur(1, "Orwell"));
        doc.setFormat(buildFormat(1));
        doc.setEditeur(buildEditeur(1, "Gallimard"));
        doc.setBibliotheque(buildBibliotheque(1, "Médiathèque La Turbine"));
        doc.setTypeDocument(buildTypeDocument(1, "Roman"));
        doc.setGenres(null); // not required per the entity setup
        return doc;
    }

    @Test
    void testGetAllDocuments_retourne200() throws Exception {
        Document doc1 = buildDocument(1, "1984");
        when(documentService.getAllDocuments()).thenReturn(List.of(doc1));

        mockMvc.perform(get("/biblio/document/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("1984"))
                .andExpect(jsonPath("$[0].auteur.nom").value("Orwell"));
    }

    @Test
    void testGetDocumentById_trouve_retourne200() throws Exception {
        Document doc1 = buildDocument(1, "1984");
        when(documentService.getDocumentById(1)).thenReturn(doc1);

        mockMvc.perform(get("/biblio/document/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("1984"))
                .andExpect(jsonPath("$.editeur.nom").value("Gallimard"));
    }

    @Test
    void testGetDocumentById_introuvable_retourne404() throws Exception {
        when(documentService.getDocumentById(99999)).thenReturn(null);

        mockMvc.perform(get("/biblio/document/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDocumentsByAuteurId_retourne200() throws Exception {
        Document doc1 = buildDocument(1, "1984");
        Document doc2 = buildDocument(2, "La Ferme des animaux");
        when(documentService.getByAuteurId(1)).thenReturn(List.of(doc1, doc2));

        mockMvc.perform(get("/biblio/document/auteur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].titre").value("La Ferme des animaux"));
    }

    @Test
    void testGetDocumentsByAuteurId_auteurInexistant_retourneListeVide() throws Exception {
        when(documentService.getByAuteurId(99999)).thenReturn(List.of());

        mockMvc.perform(get("/biblio/document/auteur/99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSearchByTitre_retourne200() throws Exception {
        Document doc1 = buildDocument(1, "1984");
        when(documentService.getByTitreContainingIgnoreCase("1984")).thenReturn(List.of(doc1));

        mockMvc.perform(get("/biblio/document/search").param("titre", "1984"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("1984"));
    }

    @Test
    void testSearchByTitre_introuvable_retourneListeVide() throws Exception {
        when(documentService.getByTitreContainingIgnoreCase("ZZZINEXISTANT999")).thenReturn(List.of());

        mockMvc.perform(get("/biblio/document/search").param("titre", "ZZZINEXISTANT999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testSaveDocument_retourne200EtAppelleService() throws Exception {
        Document toSend = buildDocument(null, "TitreTest");
        Document saved = buildDocument(10, "TitreTest");
        when(documentService.saveDocument(any(Document.class))).thenReturn(saved);

        mockMvc.perform(post("/biblio/document/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titre").value("TitreTest"));

        verify(documentService, times(1)).saveDocument(any(Document.class));
    }

    @Test
    void testUpdateDocument_retourne200EtAppelleService() throws Exception {
        Document toSend = buildDocument(10, "TitreNouveau");
        when(documentService.updateDocument(any(Document.class))).thenReturn(toSend);

        mockMvc.perform(put("/biblio/document/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toSend)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("TitreNouveau"));

        verify(documentService, times(1)).updateDocument(any(Document.class));
    }

    @Test
    void testDeleteDocumentById_retourne200() throws Exception {
        doNothing().when(documentService).deleteDocumentById(10);

        mockMvc.perform(delete("/biblio/document/10"))
                .andExpect(status().isOk());

        verify(documentService, times(1)).deleteDocumentById(10);
    }
}