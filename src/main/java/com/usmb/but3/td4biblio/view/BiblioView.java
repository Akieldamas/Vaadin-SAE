package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;

@Route (value="biblio") 
@PageTitle("Menu Bibliothécaire")
@Menu(title = "Menu Bibliothécaire", order = 2, icon = "vaadin:clipboard-check")

public class BiblioView extends VerticalLayout {
    BiblioView(UtilisateurRepo UtilisateurRepo) {
        var grid = new Grid<Utilisateur>();
        grid.addColumn(Utilisateur::getId) 
            .setHeader("Id");
        grid.addColumn(Utilisateur::getIdRole)
            .setHeader("Role Id");
        grid.addColumn(Utilisateur::getNom)
            .setHeader("Nom");
        grid.addColumn(Utilisateur::getPrenom)
            .setHeader("Prenom");
        grid.addColumn(Utilisateur::getEmail)
            .setHeader("Email");
        grid.addColumn(Utilisateur::getNumeroCarte)
            .setHeader("Numéro de carte");
        grid.addColumn(Utilisateur::getDateFinAbonnement)
            .setHeader("Date de fin d'abonnement");
        grid.setItemsPageable(pageable -> UtilisateurRepo.findByRole(2).getContent()
    );
        // Layout view
        setSizeFull(); 
        grid.setSizeFull(); 
        add(grid); 
    }
}
