package com.usmb.but3.td4biblio.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;

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