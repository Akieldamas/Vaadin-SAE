package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Scope("prototype")
@Route(value = "emprunt")
@PageTitle("Les Emprunts")
@Menu(title = "Les Emprunts", order = 2, icon = "vaadin:exchange")
public class EmpruntView extends VerticalLayout implements BeforeEnterObserver {

    private final EmpruntService empruntService;
    final Grid<Emprunt> grid;
    final TextField filter;
    private final Button addNewBtn;
    final EmpruntEditor editor;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur == null) {
            event.rerouteTo("login");
        } else if(LoginView.utilisateur.getRoleUtilisateur().getId()!=1){
            event.rerouteTo("erreur/permission");
        }
    }

    public EmpruntView(EmpruntService empruntService, EmpruntEditor editor) {
        this.empruntService = empruntService;
        this.editor = editor;
        
        this.grid = new Grid<>(Emprunt.class, false);
        this.filter = new TextField();
        this.addNewBtn = new Button("Saisir un emprunt", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("400px");

        grid.addColumn(e -> e.getUtilisateur() != null ? e.getUtilisateur().getNom() + " " + e.getUtilisateur().getPrenom() : "")
            .setHeader("Emprunteur").setSortable(true).setKey("utilisateur");
            
        grid.addColumn(e -> e.getDocument() != null ? e.getDocument().getTitre() : "")
            .setHeader("Document").setSortable(true).setKey("document");

        grid.addColumn(Emprunt::getDateDebut).setHeader("Début").setSortable(true);
        
        // Alerte Rouge si dépassement de date
        grid.addColumn(new ComponentRenderer<>(e -> {
            Span span = new Span(e.getDateFinPrevue() != null ? e.getDateFinPrevue().toString() : "");
            
            // Si le livre n'est pas rendu ET que la date de fin est strictement avant aujourd'hui
            if (e.getDateRendu() == null && e.getDateFinPrevue() != null && e.getDateFinPrevue().isBefore(LocalDate.now())) {
                span.getStyle().set("color", "var(--lumo-error-color)"); // Rouge standard Vaadin
                span.getStyle().set("font-weight", "bold");
            }
            return span;
        })).setHeader("Fin prévue").setSortable(true).setComparator(Emprunt::getDateFinPrevue);
        
        grid.addColumn(e -> e.getDateRendu() != null ? e.getDateRendu().toString() : "En cours")
            .setHeader("Statut / Rendu le").setSortable(true);
            
        // 🔥 AMÉLIORATION VISUELLE : Badge pour le booléen Prolongation
        grid.addColumn(new ComponentRenderer<>(e -> {
            Span badge = new Span();
            if (Boolean.TRUE.equals(e.getProlongation())) {
                badge.setText("Oui");
                badge.getElement().getThemeList().add("badge success"); // Badge vert vif
            } else {
                badge.setText("Non");
                badge.getElement().getThemeList().add("badge contrast"); // Badge gris discret
            }
            return badge;
        })).setHeader("Prolongé ?").setSortable(true).setComparator(Emprunt::getProlongation);

        filter.setPlaceholder("Chercher par nom ou document...");
        filter.setWidth("300px");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEmprunts(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmprunt(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editEmprunt(new Emprunt()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listEmprunts(filter.getValue());
        });

        listEmprunts(null);
    }

    void listEmprunts(String filterText) {
        List<Emprunt> emprunts;
        if (StringUtils.hasText(filterText)) {
            emprunts = new ArrayList<>(empruntService.searchEmprunts(filterText));
        } else {
            emprunts = new ArrayList<>(empruntService.getAllEmprunts());
        }

        // Tri : les emprunts en cours d'abord, puis par date de fin
        emprunts.sort(Comparator.comparing((Emprunt e) -> e.getDateRendu() != null)
                               .thenComparing(Emprunt::getDateFinPrevue, Comparator.nullsLast(Comparator.naturalOrder())));

        grid.setItems(emprunts);
    }
}