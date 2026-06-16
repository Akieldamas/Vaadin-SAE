package com.usmb.but3.td4biblio.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.repository.AuteurRepo;
import com.usmb.but3.td4biblio.repository.TypeDocumentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class TypeDocumentService {
    private final TypeDocumentRepo typeDocumentRepo;

    public List<TypeDocument> getAllTypeDocuments() {
        //return auteurRepo.findAll();
        // To specify a sort order, use:
        return typeDocumentRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public TypeDocument getTypeDocumentById(Integer id) {
        return typeDocumentRepo.findById(id).orElse(null);
    }

    public TypeDocument saveTypeDocument(TypeDocument typeDocument) {
        return typeDocumentRepo.save(typeDocument);
    }

    public TypeDocument updaTypeDocument(TypeDocument typeDocument) {
        return typeDocumentRepo.save(typeDocument);
    }

    public void deleteTypeDocumentById(Integer id) {
        typeDocumentRepo.deleteById(id);
    }
    public TypeDocument getTypeDocumentByNom(String nom) {
        return typeDocumentRepo.findByNom(nom).orElse(null);
    }
}
