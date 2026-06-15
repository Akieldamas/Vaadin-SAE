package com.usmb.but3.td4biblio.view;

import org.springframework.util.StringUtils;

import com.usmb.but3.td4biblio.components.UtilisateurDrawer;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
    private final UtilisateurService utilisateurService;
    final Grid<Utilisateur> grid;

    public BiblioView(UtilisateurService utilisateurService) {
        if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		}
        this.grid = new Grid<Utilisateur>();
        this.utilisateurService = utilisateurService;
        var searchLayout = new HorizontalLayout();
        var nomField = new TextField();
        var numCarteField = new TextField();
        var checkDateEchue =new Checkbox();
        nomField.setPlaceholder("Filtrer par nom"); 
        nomField.setPrefixComponent(VaadinIcon.SEARCH.create());
        nomField.setValueChangeMode(ValueChangeMode.LAZY);
        numCarteField.setPlaceholder("Filtrer par numero de carte"); 
        numCarteField.setPrefixComponent(VaadinIcon.SEARCH.create());
        numCarteField.setValueChangeMode(ValueChangeMode.LAZY);
        checkDateEchue.setLabel("Afficher les emprunteurs dont l'abonnement n'est plus valide");        
        grid.addColumn(Utilisateur::getId) 
            .setHeader("Id")
            .setSortProperty("id");
            
        // Récupération sécurisée du libellé du rôle depuis l'objet associé
        grid.addColumn(u -> u.getRoleUtilisateur() != null ? u.getRoleUtilisateur().getLibelle() : "")
            .setHeader("Rôle");
            
        grid.addColumn(Utilisateur::getNom)
            .setHeader("Nom")
            .setSortProperty("nom");
            
        grid.addColumn(Utilisateur::getPrenom)
            .setHeader("Prénom")
            .setSortProperty("prenom");
            
        grid.addColumn(Utilisateur::getEmail)
            .setHeader("Email")
            .setSortProperty("email");
            
        grid.addColumn(Utilisateur::getNumeroCarte)
            .setHeader("Numéro de carte")
            .setSortProperty("numeroCarte");
            
        grid.addColumn(Utilisateur::getDateFinAbonnement)
            .setHeader("Date de fin d'abonnement")
            .setSortProperty("dateFinAbonnement");

        grid.addColumn(Utilisateur::getDureeEmpruntMax)
            .setHeader("Durée d'emprunt maximale (semaines)")
            .setSortProperty("dureeEmpruntMax");

        grid.addColumn(Utilisateur::getMaxEmprunts)
            .setHeader("Nombre maximum d'emprunts")
            .setSortProperty("maxEmprunts");
            
        var drawer = new UtilisateurDrawer(details -> {
            var saved = utilisateurService.saveUtilisateur(details);
            grid.getDataProvider().refreshItem(saved);
            return saved;
        });

        var deleteButton = new Button("Supprimer", e -> {
            Utilisateur selected = grid.asSingleSelect().getValue();
            if (selected != null) {
                utilisateurService.deleteUtilisateurById(selected.getId());
                grid.getDataProvider().refreshAll();
            }
        });

        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        nomField.addValueChangeListener(e -> listEmprunteurs(e.getValue(), numCarteField.getValue(), checkDateEchue.getValue()));
        numCarteField.addValueChangeListener(e -> listEmprunteurs(nomField.getValue(), e.getValue(), checkDateEchue.getValue()));
        checkDateEchue.addValueChangeListener(e -> listEmprunteurs(nomField.getValue(), numCarteField.getValue(), e.getValue()));

        grid.asSingleSelect().addValueChangeListener(event -> {
            Utilisateur selected = event.getValue();
            if (selected != null) {
                drawer.setUtilisateurDetails(utilisateurService.getUtilisateurById(selected.getId()));
            } else {
                drawer.setUtilisateurDetails(null);
            }
        });

        // Configuration de la mise en page Vaadin
        setSizeFull(); 
        setSpacing(false);
        
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        searchLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        searchLayout.setWidthFull();
        searchLayout.add(nomField);
        searchLayout.add(numCarteField);
        searchLayout.add(checkDateEchue);

        var listLayout = new VerticalLayout(searchLayout, grid, deleteButton); 
        listLayout.setSizeFull();

        var layout = new HorizontalLayout(listLayout, drawer);
        add(layout);
        layout.setSizeFull();
        setFlexShrink(0, drawer);

        listEmprunteurs(null, null, false);
    }

    void listEmprunteurs(String nom, String numeroCarte, Boolean check) {
		if (StringUtils.hasText(nom) || StringUtils.hasText(numeroCarte)) {
            if (check)
            {
                grid.setItems(utilisateurService.getByNomOrNumeroCarteWithDate(nom, numeroCarte));
            }
            else{
                grid.setItems(utilisateurService.getByNomOrNumeroCarte(nom, numeroCarte));
            }
		} else {
            if (check)
            {   
                grid.setItems(utilisateurService.getUtilisateursByRoleWithDate(2));
            }
            else {
                grid.setItems(utilisateurService.getUtilisateursByRole(2));
            }
			
		}
	}
}