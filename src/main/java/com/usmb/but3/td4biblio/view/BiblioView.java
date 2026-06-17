package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.usmb.but3.td4biblio.components.UtilisateurDrawer;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Component
@Scope("prototype")
@Route(value = "biblio")
@PageTitle("Menu Bibliothécaire")
@Menu(title = "Menu Bibliothécaire", order = 2, icon = "vaadin:user-check")

public class BiblioView extends VerticalLayout implements BeforeEnterObserver {

    private final UtilisateurService utilisateurService;
    final Grid<Utilisateur> grid;
    final TextField nomField;
    final TextField numCarteField;
    final Checkbox checkDateEchue;
    private final Button addNewBtn;
    final BiblioEditor editor; // Remplacement du Drawer par le nouvel Editeur
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur == null) {
            event.rerouteTo("login");
        } else if(LoginView.utilisateur.getRoleUtilisateur().getId()!=1){
            event.rerouteTo("erreur/permission");

        }
    }


    public BiblioView(UtilisateurService utilisateurService, BiblioEditor editor) {
        if (LoginView.utilisateur==null) {
            this.getUI().ifPresent(ui -> ui.navigate("/login"));
        }
        this.utilisateurService = utilisateurService;
        this.editor = editor;
        
        this.grid = new Grid<>(Utilisateur.class, false);
        this.nomField = new TextField();
        this.numCarteField = new TextField();
        this.checkDateEchue = new Checkbox("Afficher abonnements échus");
        this.addNewBtn = new Button("Ajouter un emprunteur", VaadinIcon.PLUS.create());

        nomField.setPlaceholder("Filtrer par nom"); 
        nomField.setPrefixComponent(VaadinIcon.SEARCH.create());
        nomField.setValueChangeMode(ValueChangeMode.LAZY);
        
        numCarteField.setPlaceholder("Filtrer par n° carte"); 
        numCarteField.setPrefixComponent(VaadinIcon.SEARCH.create());
        numCarteField.setValueChangeMode(ValueChangeMode.LAZY);
        checkDateEchue.setLabel("Afficher les emprunteurs dont l'abonnement n'est plus valide");        
        HorizontalLayout searchLayout = new HorizontalLayout(nomField, numCarteField, checkDateEchue, addNewBtn);
        searchLayout.setAlignItems(Alignment.CENTER);
        
        // Ajout de l'éditeur sous la grille
        add(searchLayout, grid, editor);

        // Configuration de la grille
        grid.addColumn(Utilisateur::getId).setHeader("Id").setSortable(true);
        grid.addColumn(u -> u.getRoleUtilisateur() != null ? u.getRoleUtilisateur().getLibelle() : "").setHeader("Rôle");
        grid.addColumn(Utilisateur::getNom).setHeader("Nom").setSortable(true);
        grid.addColumn(Utilisateur::getPrenom).setHeader("Prénom").setSortable(true);
        grid.addColumn(Utilisateur::getEmail).setHeader("Email").setSortable(true);
        grid.addColumn(Utilisateur::getNumeroCarte).setHeader("Numéro de carte").setSortable(true);
        grid.addColumn(Utilisateur::getDateFinAbonnement).setHeader("Date fin d'abonnement").setSortable(true);
        grid.addColumn(Utilisateur::getDureeEmpruntMax).setHeader("Durée max (semaines)").setSortable(true);
        grid.addColumn(Utilisateur::getMaxEmprunts).setHeader("Max emprunts").setSortable(true);

        grid.setHeight("400px");

        // Listeners pour les filtres
        nomField.addValueChangeListener(e -> listEmprunteurs(e.getValue(), numCarteField.getValue(), checkDateEchue.getValue()));
        numCarteField.addValueChangeListener(e -> listEmprunteurs(nomField.getValue(), e.getValue(), checkDateEchue.getValue()));
        checkDateEchue.addValueChangeListener(e -> listEmprunteurs(nomField.getValue(), numCarteField.getValue(), e.getValue()));

        // Quand on clique sur une ligne, on ouvre l'éditeur
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editUtilisateur(e.getValue());
        });

        // Quand on veut ajouter un nouveau membre
        addNewBtn.addClickListener(e -> editor.editUtilisateur(new Utilisateur()));

        // Quand l'éditeur a fini de sauvegarder ou supprimer, on recharge la liste et on le cache
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listEmprunteurs(nomField.getValue(), numCarteField.getValue(), checkDateEchue.getValue());
        });

        // Chargement initial
        listEmprunteurs(null, null, false);
    }

    void listEmprunteurs(String nom, String numeroCarte, Boolean check) {
        if (StringUtils.hasText(nom) || StringUtils.hasText(numeroCarte)) {
            if (check != null && check) {
                grid.setItems(utilisateurService.getByNomOrNumeroCarteWithDate(nom, numeroCarte));
            } else {
                grid.setItems(utilisateurService.getByNomOrNumeroCarte(nom, numeroCarte));
            }
        } else {
            if (check != null && check) {
                grid.setItems(utilisateurService.getUtilisateursByRoleWithDate(2));
            } else {
                grid.setItems(utilisateurService.getUtilisateursByRole(2));
            }
        }
    }
}