package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

@Route(value = "accueil/document")
public class SpecificDocumentView extends VerticalLayout implements HasUrlParameter<String> {
    
    private final DocumentService documentService;
    private final VerticalLayout contentLayout;

    public SpecificDocumentView(DocumentService documentService) {
        this.documentService = documentService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // Header global
        add(new HeaderAccueilView());

        // Container central
        contentLayout = new VerticalLayout();
        contentLayout.setWidthFull();
        contentLayout.setMaxWidth("1000px");
        contentLayout.getStyle().set("margin", "0 auto");
        contentLayout.addClassNames(Padding.Vertical.LARGE, Padding.Horizontal.MEDIUM);
        add(contentLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        contentLayout.removeAll();

        try {
            int id = Integer.parseInt(parameter);
            Document currentDocument = documentService.getDocumentById(id);

            if (currentDocument == null) {
                contentLayout.add(new Paragraph("Document introuvable."));
                return;
            }

            HorizontalLayout mainBody = new HorizontalLayout();
            mainBody.setWidthFull();
            mainBody.setSpacing(true);
            mainBody.getStyle().set("flex-wrap", "wrap");

            // --- Logique d'image conditionnelle ---
            if (currentDocument.getLienGif() != null && !currentDocument.getLienGif().isEmpty()) {
                VerticalLayout leftColumn = new VerticalLayout();
                leftColumn.setWidth("300px");
                leftColumn.setPadding(false);
                leftColumn.setAlignItems(FlexComponent.Alignment.CENTER);

                Image gif = new Image(currentDocument.getLienGif(), "Illustration");
                gif.setWidth("100%");
                gif.setMaxWidth("280px");
                gif.getStyle().set("border-radius", "12px")
                              .set("box-shadow", "0 8px 16px rgba(0,0,0,0.12)");
                leftColumn.add(gif);
                mainBody.add(leftColumn);
            }

            // --- Colonne de droite (Détails) ---
            VerticalLayout rightColumn = new VerticalLayout();
            rightColumn.setPadding(false);
            rightColumn.getStyle().set("flex", "1");

            H1 title = new H1(currentDocument.getTitre());
            title.addClassNames(FontSize.XXLARGE, FontWeight.BOLD, Margin.Bottom.SMALL);

            String auteurNom = (currentDocument.getAuteur() != null) 
                    ? currentDocument.getAuteur().getPrenom() + " " + currentDocument.getAuteur().getNom()
                    : "Auteur inconnu";
            H3 auteurSpan = new H3("Par " + auteurNom);
            auteurSpan.addClassNames(FontSize.MEDIUM, TextColor.SECONDARY, Margin.Bottom.MEDIUM);

            // Badges Genres
            HorizontalLayout genresLayout = new HorizontalLayout();
            genresLayout.setWrap(true);
            if (currentDocument.getGenres() != null) {
                for (GenreDocument genre : currentDocument.getGenres()) {
                    Span badge = new Span(genre.getNom());
                    badge.getElement().getThemeList().add("badge pill contrast");
                    genresLayout.add(badge);
                }
            }

            // Description
            Paragraph desc = new Paragraph(currentDocument.getDescription() != null ? currentDocument.getDescription() : "Pas de description.");
            desc.getStyle().set("line-height", "1.6");

            // Footer infos
            Div metaBox = new Div();
            metaBox.addClassNames(Padding.MEDIUM, Background.CONTRAST_5, BorderRadius.MEDIUM, FontSize.SMALL);
            metaBox.add(new Span("Éditeur : " + (currentDocument.getEditeur() != null ? currentDocument.getEditeur().getNom() : "Non spécifié")));

            rightColumn.add(title, auteurSpan, genresLayout, new H3("Résumé"), desc, metaBox);
            mainBody.add(rightColumn);

            contentLayout.add(mainBody);

        } catch (Exception e) {
            contentLayout.add(new Paragraph("Erreur lors du chargement : " + e.getMessage()));
        }
    }
}