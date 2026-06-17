package com.usmb.but3.td4biblio.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
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
        if (emprunt.getUtilisateur() == null || emprunt.getDocument() == null) {
            throw new IllegalArgumentException("L'utilisateur et le document sont obligatoires.");
        }

        // Vérification du rôle interdit (Rôle 1 = Bibliothécaire)
        if (emprunt.getUtilisateur().getRoleUtilisateur() != null
                && emprunt.getUtilisateur().getRoleUtilisateur().getId() == 1) {
            throw new IllegalStateException("Erreur : Les bibliothécaires ne peuvent pas faire d'emprunt.");
        }

        // Détermination s'il s'agit d'un nouvel emprunt :
        // Si l'ID est null, c'est que l'emprunt n'existe pas encore en base.
        boolean isCreation = (emprunt.getId() == null);

        if (isCreation) {
            Utilisateur user = emprunt.getUtilisateur();

            long nbEmpruntsActuels = empruntRepo.countEmpruntsActifs(user.getId());

            // On lève une exception si le quota max est atteint ou dépassé
            if (user.getMaxEmprunts() != null && nbEmpruntsActuels >= user.getMaxEmprunts()) {
                throw new IllegalStateException("Règle bafouée : Cet utilisateur a déjà atteint sa limite maximale de "
                        + user.getMaxEmprunts() + " emprunts simultanés.");
            }

            emprunt.setDateDebut(LocalDate.now());

            int semaines = (user.getDureeEmpruntMax() != null) ? user.getDureeEmpruntMax() : 2;
            emprunt.setDateFinPrevue(emprunt.getDateDebut().plusWeeks(semaines));

            emprunt.setDateRendu(null);
            emprunt.setProlongation(false);
        }

        return empruntRepo.save(emprunt);
    }

    public void deleteEmprunt(Emprunt emprunt) {
        empruntRepo.delete(emprunt);
    }

    public List<Emprunt> searchEmprunts(String filterText) {
        if (!StringUtils.hasText(filterText)) {
            return getAllEmprunts();
        }
        return empruntRepo.searchEmprunts(filterText);
    }
    public List<Emprunt> getEmpruntsDeLUtilisateur(Integer utilisateurId) {
        return empruntRepo.findByUtilisateurId(utilisateurId);
    }

}