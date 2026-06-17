package com.usmb.but3.td4biblio.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.TypeAuteur;
import com.usmb.but3.td4biblio.repository.TypeAuteurRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TypeAuteurService {

    private final TypeAuteurRepo typeAuteurRepo;

    public List<TypeAuteur> getAllTypesAuteur() {
        return typeAuteurRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public TypeAuteur getTypeByLabel(String label) {
        return typeAuteurRepo.findByLabel(label);
    }

    public TypeAuteur getTypeAuteurById(Integer id) {
        return typeAuteurRepo.findById(id).orElse(null);
    }

    public TypeAuteur saveTypeAuteur(TypeAuteur typeAuteur) {
        return typeAuteurRepo.save(typeAuteur);
    }

    public TypeAuteur updateTypeAuteur(TypeAuteur typeAuteur) {
        return typeAuteurRepo.save(typeAuteur);
    }

    public void deleteTypeAuteurById(Integer id) {
        typeAuteurRepo.deleteById(id);
    }
}