package com.usmb.but3.td4biblio.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.repository.EditeurRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditeurService {

    private final EditeurRepo editeurRepo;

    public List<Editeur> getAllEditeurs() {
        return editeurRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Editeur getEditeurById(Integer id) {
        return editeurRepo.findById(id).orElse(null);
    }

    public Editeur saveEditeur(Editeur editeur) {
        return editeurRepo.save(editeur);
    }

    public Editeur updateEditeur(Editeur editeur) {
        return editeurRepo.save(editeur);
    }

    public void deleteEditeurById(Integer id) {
        editeurRepo.deleteById(id);
    }
}