package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;

@Route(value = "biblio") 
@PageTitle("Menu Bibliothécaire")
@Menu(title = "Menu Bibliothécaire", order = 2, icon = "vaadin:user-check")
public class BiblioView extends VerticalLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur==null) {
            event.rerouteTo("login"); // redirect to login page
        }
    }
    public BiblioView(UtilisateurRepo utilisateurRepo) {
        if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		} 
        var grid = new Grid<Utilisateur>();
        
        grid.addColumn(Utilisateur::getId) 
            .setHeader("Id");
            
        // Récupération sécurisée du libellé du rôle depuis l'objet associé
        grid.addColumn(u -> u.getRoleUtilisateur() != null ? u.getRoleUtilisateur().getLibelle() : "")
            .setHeader("Rôle");
            
        grid.addColumn(Utilisateur::getNom)
            .setHeader("Nom");
            
        grid.addColumn(Utilisateur::getPrenom)
            .setHeader("Prénom");
            
        grid.addColumn(Utilisateur::getEmail)
            .setHeader("Email");
            
        grid.addColumn(Utilisateur::getNumeroCarte)
            .setHeader("Numéro de carte");
            
        grid.addColumn(Utilisateur::getDateFinAbonnement)
            .setHeader("Date de fin d'abonnement");
            
        // Utilisation de la nouvelle méthode du repo (ici pour le rôle ID 2)
        grid.setItems(utilisateurRepo.findByRoleUtilisateurId(2));

        // Configuration de la mise en page Vaadin
        setSizeFull(); 
        grid.setSizeFull(); 
        add(grid); 
    }
}