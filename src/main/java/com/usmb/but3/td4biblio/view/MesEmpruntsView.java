package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Route(value = "accueil/mes-emprunts")
@PageTitle("Mes Emprunts")
public class MesEmpruntsView extends VerticalLayout implements BeforeEnterObserver {

    private final EmpruntService empruntService;
    private final MesEmpruntsEditor editor;
    private final Div container;
    private final H1 titreHeader;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur == null) {
            event.rerouteTo("login");
        } else if (LoginView.utilisateur.getRoleUtilisateur().getId() != 2) {
            event.rerouteTo("erreur/permission");
        }
    }

    public MesEmpruntsView(EmpruntService empruntService, MesEmpruntsEditor editor) {
        this.empruntService = empruntService;
        this.editor = editor;

        setSizeFull();
        setPadding(false); // Padding géré par le contenu
        setSpacing(false);

        // 1. Ajout du Header
        HeaderAccueilView header = new HeaderAccueilView();
        add(header);

        // 2. Titre de page centré
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setPadding(true);
        
        this.titreHeader = new H1("Mon historique d'emprunts");
        titreHeader.getStyle().set("margin-top", "20px");
        titreHeader.getStyle().set("margin-bottom", "30px");
        titreHeader.getStyle().set("align-self", "center");

        // 3. Container des cartes
        container = new Div();
        container.getStyle().set("display", "grid");
        container.getStyle().set("grid-template-columns", "repeat(auto-fill, minmax(320px, 1fr))");
        container.getStyle().set("gap", "25px");
        container.setWidthFull();
        container.getStyle().set("padding", "0 20px 40px 20px");

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            refreshEmprunts();
        });

        mainLayout.add(titreHeader, editor, container);
        add(mainLayout);

        refreshEmprunts();
    }

    private void refreshEmprunts() {
        container.removeAll();
        Utilisateur connecte = LoginView.utilisateur;
        if (connecte == null) return;

        titreHeader.setText("Mes Emprunts : " + connecte.getPrenom() + " " + connecte.getNom());

        List<Emprunt> mesEmprunts = new ArrayList<>(empruntService.getEmpruntsDeLUtilisateur(connecte.getId()));
        mesEmprunts.sort(Comparator.comparing((Emprunt e) -> e.getDateRendu() != null)
                .thenComparing(Emprunt::getDateFinPrevue, Comparator.nullsLast(Comparator.naturalOrder())));

        if (mesEmprunts.isEmpty()) {
            Paragraph noResult = new Paragraph("Vous n'avez aucun emprunt enregistré.");
            noResult.getStyle().set("text-align", "center");
            noResult.getStyle().set("margin-top", "50px");
            noResult.getStyle().set("color", "gray");
            container.add(noResult);
            return;
        }

        for (Emprunt emprunt : mesEmprunts) {
            Div card = createEmpruntCard(emprunt);
            card.addClickListener(e -> editor.editEmprunt(emprunt));
            container.add(card);
        }
    }

    private Div createEmpruntCard(Emprunt emprunt) {
        Div card = new Div();
        card.getStyle().set("background", "white")
                      .set("border-radius", "12px")
                      .set("box-shadow", "0 4px 12px rgba(0,0,0,0.08)")
                      .set("padding", "20px")
                      .set("cursor", "pointer")
                      .set("display", "flex")
                      .set("flex-direction", "column");

        // Header carte
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        
        String titreStr = emprunt.getDocument() != null ? emprunt.getDocument().getTitre() : "Inconnu";
        Span titre = new Span(titreStr);
        titre.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Span status = new Span();
        status.getElement().getThemeList().add("badge");
        
        if (emprunt.getDateRendu() != null) {
            status.setText("Rendu");
            status.getElement().getThemeList().add("success");
        } else if (emprunt.getDateFinPrevue().isBefore(LocalDate.now())) {
            status.setText("Retard");
            status.getElement().getThemeList().add("error");
            card.getStyle().set("border-left", "5px solid var(--lumo-error-color)");
        } else {
            status.setText("En cours");
            status.getElement().getThemeList().add("contrast");
            card.getStyle().set("border-left", "5px solid var(--lumo-primary-color)");
        }
        
        headerLayout.add(titre, status);
        
        // Corps
        Paragraph dates = new Paragraph("Du " + emprunt.getDateDebut() + " au " + emprunt.getDateFinPrevue());
        dates.getStyle().set("font-size", "0.9em").set("color", "#666");

        card.add(headerLayout, dates);
        return card;
    }
}