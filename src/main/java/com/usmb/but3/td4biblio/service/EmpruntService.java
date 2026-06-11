package com.usmb.but3.td4biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.EmpruntId;
import com.usmb.but3.td4biblio.repository.EmpruntRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpruntService {

    private final EmpruntRepo empruntRepo;

    public List<Emprunt> getAllEmprunts() {
        return empruntRepo.findAll();
    }

    public Emprunt getEmpruntById(EmpruntId id) {
        return empruntRepo.findById(id).orElse(null);
    }

    public Emprunt saveEmprunt(Emprunt emprunt) {
        return empruntRepo.save(emprunt);
    }

    public Emprunt updateEmprunt(Emprunt emprunt) {
        return empruntRepo.save(emprunt);
    }

    public void deleteEmpruntById(EmpruntId id) {
        empruntRepo.deleteById(id);
    }

    public List<Emprunt> getEmpruntsByUtilisateur(Integer utilisateurId) {
        return empruntRepo.findByUtilisateurId(utilisateurId);
    }

    public List<Emprunt> getEmpruntsByDocument(Integer documentId) {
        return empruntRepo.findByDocumentId(documentId);
    }
}