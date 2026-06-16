package com.usmb.but3.td4biblio.view;

import java.util.List;
import java.util.stream.Collectors;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("RechercheDocument")
@Route(value = "accueil/document/recherche")
public class RechercheDocumentView extends VerticalLayout {

    private final DocumentService documentService;
    private final Div container;
    private final TextField searchField;

    public RechercheDocumentView(DocumentService documentService) {
        this.documentService = documentService;
        
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set("border-bottom", "2px solid Blue");
        add(header);

        VerticalLayout vLayout = new VerticalLayout();

        // Titre + Bouton Retour
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setAlignItems(Alignment.CENTER);

        H1 title = new H1("Rechercher un document dans le catalogue");
        title.getStyle().set("font-size", "25px");
        title.getStyle().set("font-weight", "bold");

        Button retourBtn = new Button("Retour aux nouveautés", VaadinIcon.ARROW_LEFT.create());
        retourBtn.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate("accueil/document")));
        retourBtn.getStyle().set("margin-left", "auto");

        topLayout.add(title, retourBtn);

        // Zone de saisie pour la recherche
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidthFull();
        searchLayout.getStyle().set("margin-top", "10px");

        searchField = new TextField();
        searchField.setPlaceholder("Tapez un titre, un mot-clé, une description...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("500px");
        searchField.setClearButtonVisible(true);
        
        // Mode LAZY pour filtrer dynamiquement sans surcharger le serveur
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> refreshDocuments(e.getValue()));

        searchLayout.add(searchField);

        // Conteneur de grilles de cartes
        container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        container.getStyle().set("flex-wrap", "wrap");
        container.getStyle().set("margin-top", "20px");

        vLayout.add(topLayout, searchLayout, container);
        add(vLayout);

        // Charger tout le catalogue au premier affichage
        refreshDocuments("");
    }

    private void refreshDocuments(String filterText) {
        container.removeAll();

        List<Document> documents = documentService.getAllDocuments();

        String lowerCaseFilter = filterText == null ? "" : filterText.toLowerCase().trim();
        List<Document> filteredDocuments = documents.stream()
                .filter(doc -> lowerCaseFilter.isEmpty() ||
                        (doc.getTitre() != null && doc.getTitre().toLowerCase().contains(lowerCaseFilter)) ||
                        (doc.getDescription() != null && doc.getDescription().toLowerCase().contains(lowerCaseFilter)))
                .collect(Collectors.toList());

        if (filteredDocuments.isEmpty()) {
            Paragraph noResult = new Paragraph("Aucun document ne correspond à vos critères de recherche.");
            noResult.getStyle().set("font-style", "italic");
            noResult.getStyle().set("color", "gray");
            container.add(noResult);
            return;
        }

        for (Document doc : filteredDocuments) {
            Div card = new Div();
            card.addClassName("card");
            card.getStyle().set("display", "flex");
            card.getStyle().set("position", "relative");

            card.getStyle().setWidth("350px");
            card.getStyle().setHeight("200px");

            card.getStyle().set("border", "1px solid blue");
            card.getStyle().set("border-radius", "5px");
            card.getStyle().set("flex-direction", "column");
            card.getStyle().setMargin("10px");
            card.getStyle().set("align-items", "center");
            card.getStyle().set("justify-content", "center");
            
            Div titreLivreDiv = new Div();
            titreLivreDiv.getStyle().set("width", "100%");
            titreLivreDiv.getStyle().set("border-bottom", "solid 1px blue");

            Paragraph titreLivre = new Paragraph(doc.getTitre());
            titreLivre.getStyle().set("font-size", "16px");
            titreLivre.getStyle().set("font-weight", "bold");
            titreLivre.getStyle().set("margin-left", "5px");

            titreLivreDiv.add(titreLivre);

            Paragraph descriptionLivre = new Paragraph(doc.getDescription());   
            descriptionLivre.getStyle().set("text-align", "justify");
            descriptionLivre.getStyle().set("margin", "5px");

            Paragraph datePublicationLivre = new Paragraph("Livre reçu le : " + (doc.getDateAcquisition() != null ? doc.getDateAcquisition().toString() : "Inconnue"));   
            datePublicationLivre.getStyle().set("position", "absolute");
            datePublicationLivre.getStyle().set("right", "0");
            datePublicationLivre.getStyle().set("bottom", "0");

            datePublicationLivre.getStyle().set("text-align", "justify");
            datePublicationLivre.getStyle().set("margin", "5px");
            datePublicationLivre.getStyle().set("height", "max");
            datePublicationLivre.getStyle().set("text-align", "right");

            card.add(titreLivreDiv, descriptionLivre, datePublicationLivre);
            
            card.addClickListener(e -> {
                this.getUI().ifPresent(ui -> ui.navigate("accueil/document/" + doc.getId()));
            });
            
            container.add(card);
        }
    }
}