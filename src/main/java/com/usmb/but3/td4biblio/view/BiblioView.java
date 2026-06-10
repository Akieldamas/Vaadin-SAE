package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.usmb.but3.td4biblio.entity.UtilisateurDTO;
import com.usmb.but3.td4biblio.repository.UtilisateurRepository;

@Route (value="biblio") 
@PageTitle("Menu Bibliothécaire")
@Menu(title = "Menu Bibliothécaire", order = 2, icon = "vaadin:clipboard-check")

public class BiblioView extends VerticalLayout {
    BiblioView(UtilisateurRepository utilisateurRepository) {
        var grid = new Grid<UtilisateurDTO>();
        grid.addColumn(UtilisateurDTO::getId) 
            .setHeader("Id");
        grid.addColumn(UtilisateurDTO::getIdRole)
            .setHeader("Role Id");
        grid.addColumn(UtilisateurDTO::getNom)
            .setHeader("Nom");
        grid.addColumn(UtilisateurDTO::getPrenom)
            .setHeader("Prenom");
        grid.addColumn(UtilisateurDTO::getEmail)
            .setHeader("Email");
        grid.addColumn(UtilisateurDTO::getNumeroCarte)
            .setHeader("Numéro de carte");
        grid.addColumn(UtilisateurDTO::getDateFinAbonnement)
            .setHeader("Date de fin d'abonnement");
        grid.setItemsPageable(pageable -> utilisateurRepository.findByRole(2).getContent()
    );
        // Layout view
        setSizeFull(); 
        grid.setSizeFull(); 
        add(grid); 
    }
}
