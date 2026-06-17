package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Scope("prototype")
@Route(value = "mes-emprunts")
@PageTitle("Mes Emprunts")
@Menu(title = "Mes Emprunts", order = 1, icon = "vaadin:notebook")
public class MesEmpruntsView extends VerticalLayout implements BeforeEnterObserver {

    private final EmpruntService empruntService;
    final Grid<Emprunt> grid;
    final MesEmpruntsEditor editor;
    private final H2 titreHeader;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur == null ) {
            event.rerouteTo("login");
        }
    }

    public MesEmpruntsView(EmpruntService empruntService, MesEmpruntsEditor editor) {
        this.empruntService = empruntService;
        this.editor = editor;
        
        this.grid = new Grid<>(Emprunt.class, false);
        this.titreHeader = new H2("Mon historique d'emprunts'");

        add(titreHeader, grid, editor);

        grid.setHeight("400px");

        // Colonne Document
        grid.addColumn(e -> e.getDocument() != null ? e.getDocument().getTitre() : "")
            .setHeader("Document").setSortable(true);

        // Colonne Début
        grid.addColumn(Emprunt::getDateDebut).setHeader("Emprunté le").setSortable(true);
        
        // Colonne Fin Prévue avec Alerte Rouge si retard
        grid.addColumn(new ComponentRenderer<>(e -> {
            Span span = new Span(e.getDateFinPrevue() != null ? e.getDateFinPrevue().toString() : "");
            if (e.getDateRendu() == null && e.getDateFinPrevue() != null && e.getDateFinPrevue().isBefore(LocalDate.now())) {
                span.getStyle().set("color", "var(--lumo-error-color)");
                span.getStyle().set("font-weight", "bold");
            }
            return span;
        })).setHeader("À rendre avant le").setSortable(true).setComparator(Emprunt::getDateFinPrevue);
        
        // Colonne Statut / Date retour
        grid.addColumn(e -> e.getDateRendu() != null ? "Rendu le " + e.getDateRendu() : "En ma possession")
            .setHeader("Statut").setSortable(true);
            
        // Colonne Badge Prolongation
        grid.addColumn(new ComponentRenderer<>(e -> {
            Span badge = new Span();
            if (Boolean.TRUE.equals(e.getProlongation())) {
                badge.setText("Déjà prolongé");
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.setText("Non prolongé");
                badge.getElement().getThemeList().add("badge contrast");
            }
            return badge;
        })).setHeader("Prolongation").setSortable(true).setComparator(Emprunt::getProlongation);

        // Clic sur une ligne pour ouvrir le panneau de prolongation
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmprunt(e.getValue());
        });

        // Handler après modification
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            refreshGrid();
        });

        refreshGrid();
    }

    void refreshGrid() {
        Utilisateur connecte = LoginView.utilisateur;
        if (connecte == null) return;
        
        titreHeader.setText("Mon historique d'emprunts' (" + connecte.getPrenom() + " " + connecte.getNom() + ")");

        // Récupération uniquement de SES emprunts
        List<Emprunt> mesEmprunts = new ArrayList<>(empruntService.getEmpruntsDeLUtilisateur(connecte.getId()));

        // Tri : les encours d'abord, puis par date de fin
        mesEmprunts.sort(Comparator.comparing((Emprunt e) -> e.getDateRendu() != null)
                                   .thenComparing(Emprunt::getDateFinPrevue, Comparator.nullsLast(Comparator.naturalOrder())));

        grid.setItems(mesEmprunts);
    }
}