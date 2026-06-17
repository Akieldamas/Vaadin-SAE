package com.usmb.but3.td4biblio.service;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.repository.BibliothequeRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class BibliothequeService {
    private final BibliothequeRepo bibliothequeRepo;

    public List<Bibliotheque> getAllBibliotheques() {
        //return auteurRepo.findAll();
        // To specify a sort order, use:
        return bibliothequeRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Bibliotheque getBibliothequeById(Integer id) {
        return bibliothequeRepo.findById(id).orElse(null);
    }

    public Bibliotheque saveBibliotheque(Bibliotheque bibliotheque) {
        return bibliothequeRepo.save(bibliotheque);
    }

    public Bibliotheque updateBibliotheque(Bibliotheque bibliotheque) {
        return bibliothequeRepo.save(bibliotheque);
    }

    public void deleteBibliothequeById(Integer id) {
        bibliothequeRepo.deleteById(id);
    }
    public Bibliotheque getBibliothequeByNom(String nom) {
        return bibliothequeRepo.findByNom(nom).orElse(null);
    }
}
