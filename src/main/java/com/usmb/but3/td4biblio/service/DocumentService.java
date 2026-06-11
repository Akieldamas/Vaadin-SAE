package com.usmb.but3.td4biblio.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final AuteurService auteurService;
    private final FormatService formatService;
    private final EditeurService editeurService;
    private final GenreDocumentService genreDocumentService;

    public List<Document> getAllDocuments() {
        return documentRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Document getDocumentById(Integer id) {
        return documentRepo.findById(id).orElse(null);
    }

    public Document saveDocument(Document document) {
        return documentRepo.save(document);
    }

    public Document updateDocument(Document document) {
        return documentRepo.save(document);
    }

    public void deleteDocumentById(Integer id) {
        documentRepo.deleteById(id);
    }

    public List<Document> getByTitreContainingIgnoreCase(String filter) {
        return documentRepo.findByTitreContainingIgnoreCase(filter);
    }

    public List<Document> getByAuteurId(Integer auteurId) {
        return documentRepo.findByAuteurId(auteurId);
    }
    public List<Document> getDocumentsDisponibles() {
        return documentRepo.findDocumentsDisponibles();
    }   
}