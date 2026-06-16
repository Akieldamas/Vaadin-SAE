package com.usmb.but3.td4biblio.view;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

@PageTitle("Accueil / Recherche")
@Route(value = "accueil/document")
public class AccueilDocumentView extends VerticalLayout {

    public static Utilisateur utilisateur;
    
    private final DocumentService documentService;
    private final Div container;
    
    // Éléments composants de recherche et de filtres (Basés sur le SQL)
    private final TextField searchField;
    private final ComboBox<String> bibliothequeFilter;
    private final ComboBox<String> typeFilter;
    private final ComboBox<String> editeurFilter;
    private final ComboBox<String> auteurFilter;

    public AccueilDocumentView(UtilisateurRepo utilisateurRepo, DocumentService documentService) {
        this.documentService = documentService;
        
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set("border-bottom", "2px solid Blue");
        add(header);

        VerticalLayout vLayout = new VerticalLayout();

        H1 title = new H1("Catalogue & Nouveautés");
        title.getStyle().set("font-size", "25px");
        title.getStyle().set("font-weight", "bold");

        // ---------------------------------------------------------
        // 1. BANDEAU DE FILTRES MULTI-CRITÈRES (SQL MATCHED)
        // ---------------------------------------------------------
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setWidthFull();
        filterLayout.setAlignItems(Alignment.BASELINE);
        filterLayout.getStyle().set("margin-top", "10px");
        filterLayout.getStyle().set("padding", "15px");
        filterLayout.getStyle().set("background-color", "#f8f9fa");
        filterLayout.getStyle().set("border", "1px solid #e3e6f0");
        filterLayout.getStyle().set("border-radius", "8px");

        // Barre textuelle (titre & description)
        searchField = new TextField("Recherche");
        searchField.setPlaceholder("Titre, description...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setWidth("250px");
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        // Liste défilante Bibliothèque (bibliotheque_id -> nom)
        bibliothequeFilter = new ComboBox<>("Bibliothèque");
        bibliothequeFilter.setPlaceholder("Toutes");
        bibliothequeFilter.setClearButtonVisible(true);
        bibliothequeFilter.setWidth("200px");

        // Liste défilante Type de document (type_id -> nom)
        typeFilter = new ComboBox<>("Type");
        typeFilter.setPlaceholder("Tous");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("180px");

        // Liste défilante Éditeur (editeur_id -> nom)
        editeurFilter = new ComboBox<>("Éditeur");
        editeurFilter.setPlaceholder("Tous");
        editeurFilter.setClearButtonVisible(true);
        editeurFilter.setWidth("180px");

        // Liste défilante Auteur (auteur_id -> nom + prenom)
        auteurFilter = new ComboBox<>("Auteur");
        auteurFilter.setPlaceholder("Tous");
        auteurFilter.setClearButtonVisible(true);
        auteurFilter.setWidth("220px");
        
        // Bouton de remise à zéro rapide
        Button resetBtn = new Button("Effacer", VaadinIcon.REFRESH.create());
        resetBtn.addClickListener(e -> {
            searchField.clear();
            bibliothequeFilter.clear();
            typeFilter.clear();
            editeurFilter.clear();
            auteurFilter.clear();
        });

        filterLayout.add(searchField, bibliothequeFilter, typeFilter, editeurFilter, auteurFilter, resetBtn);

        // ---------------------------------------------------------
        // 2. EXTRACTION ET ALIMENTATION DYNAMIQUE DES FILTRES
        // ---------------------------------------------------------
        List<Document> tousLesDocuments = documentService.getAllDocuments();
        
        // Alimentation Bibliothèque
        List<String> bibliotheques = tousLesDocuments.stream()
                .map(doc -> doc.getBibliotheque() != null ? doc.getBibliotheque().getNom() : "Non spécifiée")
                .distinct().sorted().collect(Collectors.toList());
        bibliothequeFilter.setItems(bibliotheques);

        // Alimentation Type de Document
        List<String> types = tousLesDocuments.stream()
                .map(doc -> doc.getTypeDocument() != null ? doc.getTypeDocument().getNom() : "Inconnu") 
                .distinct().sorted().collect(Collectors.toList());
        typeFilter.setItems(types);

        // Alimentation Éditeur
        List<String> editeurs = tousLesDocuments.stream()
                .map(doc -> doc.getEditeur() != null ? doc.getEditeur().getNom() : "Inconnu")
                .distinct().sorted().collect(Collectors.toList());
        editeurFilter.setItems(editeurs);

        // Alimentation Auteur (Nom + Prénom)
        List<String> auteurs = tousLesDocuments.stream()
                .map(doc -> doc.getAuteur() != null ? doc.getAuteur().getNom() + " " + doc.getAuteur().getPrenom() : "Inconnu")
                .distinct().sorted().collect(Collectors.toList());
        auteurFilter.setItems(auteurs);

        // Liaisons d'événements : rafraîchit la vue à chaque interaction
        searchField.addValueChangeListener(e -> refreshDocuments());
        bibliothequeFilter.addValueChangeListener(e -> refreshDocuments());
        typeFilter.addValueChangeListener(e -> refreshDocuments());
        editeurFilter.addValueChangeListener(e -> refreshDocuments());
        auteurFilter.addValueChangeListener(e -> refreshDocuments());

        // ---------------------------------------------------------
        // 3. ZONE D'AFFICHAGE DES CARTES
        // ---------------------------------------------------------
        container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        container.getStyle().set("flex-wrap", "wrap");
        container.getStyle().set("margin-top", "20px");

        vLayout.add(title, filterLayout, container);
        add(vLayout);

        // Premier affichage complet
        refreshDocuments();
    }

    // ---------------------------------------------------------
    // 4. LOGIQUE DE FILTRAGE ET DE RENDU DES CARTES
    // ---------------------------------------------------------
    private void refreshDocuments() {
        container.removeAll();

        List<Document> documents = documentService.getAllDocuments();
        
        // Tri décroissant sur la date d'acquisition
        documents.sort(Comparator.comparing(Document::getDateAcquisition, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        // Récupération des valeurs sélectionnées
        String textFilter = searchField.getValue() != null ? searchField.getValue().toLowerCase().trim() : "";
        String selectedBib = bibliothequeFilter.getValue();
        String selectedType = typeFilter.getValue();
        String selectedEditeur = editeurFilter.getValue();
        String selectedAuteur = auteurFilter.getValue();

        // Filtrage croisé
        List<Document> filteredDocuments = documents.stream()
                // Recherche textuelle
                .filter(doc -> textFilter.isEmpty() ||
                        (doc.getTitre() != null && doc.getTitre().toLowerCase().contains(textFilter)) ||
                        (doc.getDescription() != null && doc.getDescription().toLowerCase().contains(textFilter)))
                
                // Filtre Bibliothèque
                .filter(doc -> selectedBib == null || 
                        (doc.getBibliotheque() != null && doc.getBibliotheque().getNom().equals(selectedBib)))
                
                // Filtre Type
                .filter(doc -> selectedType == null || 
                        (doc.getTypeDocument() != null && doc.getTypeDocument().getNom().equals(selectedType)))
                
                // Filtre Éditeur
                .filter(doc -> selectedEditeur == null || 
                        (doc.getEditeur() != null && doc.getEditeur().getNom().equals(selectedEditeur)))
                
                // Filtre Auteur
                .filter(doc -> selectedAuteur == null || 
                        (doc.getAuteur() != null && (doc.getAuteur().getNom() + " " + doc.getAuteur().getPrenom()).equals(selectedAuteur)))
                
                .collect(Collectors.toList());

        // Message si aucun résultat
        if (filteredDocuments.isEmpty()) {
            Paragraph noResult = new Paragraph("Aucun document ne correspond à vos critères de sélection.");
            noResult.getStyle().set("font-style", "italic");
            noResult.getStyle().set("color", "gray");
            noResult.getStyle().set("margin", "20px");
            container.add(noResult);
            return;
        }

        // Génération des composants cartes graphiques
        for (Document doc : filteredDocuments) {
            Div card = new Div();
            card.addClassName("card");
            card.getStyle().set("display", "flex");
            card.getStyle().set("position", "relative");
            card.getStyle().setWidth("350px");
            card.getStyle().setHeight("220px"); // Légèrement agrandi pour accueillir les badges d'infos

            card.getStyle().set("border", "1px solid blue");
            card.getStyle().set("border-radius", "5px");
            card.getStyle().set("flex-direction", "column");
            card.getStyle().setMargin("10px");
            
            // En-tête de la carte (Titre)
            Div titreLivreDiv = new Div();
            titreLivreDiv.getStyle().set("width", "100%");
            titreLivreDiv.getStyle().set("border-bottom", "solid 1px blue");

            Paragraph titreLivre = new Paragraph(doc.getTitre());
            titreLivre.getStyle().set("font-size", "16px");
            titreLivre.getStyle().set("font-weight", "bold");
            titreLivre.getStyle().set("margin", "5px");
            titreLivreDiv.add(titreLivre);

            // Métadonnées (Type & Bibliothèque sous forme de labels)
            String typeDoc = doc.getTypeDocument() != null ? doc.getTypeDocument().getNom() : "Non classé";
            String bibDoc = doc.getBibliotheque() != null ? doc.getBibliotheque().getNom() : "N/A";
            Paragraph metaLabel = new Paragraph("[" + typeDoc + "] - Localisation : " + bibDoc);
            metaLabel.getStyle().set("font-size", "12px");
            metaLabel.getStyle().set("color", "darkblue");
            metaLabel.getStyle().set("margin", "0 5px");

            // Description
            Paragraph descriptionLivre = new Paragraph(doc.getDescription());   
            descriptionLivre.getStyle().set("text-align", "justify");
            descriptionLivre.getStyle().set("margin", "5px");
            descriptionLivre.getStyle().set("font-size", "14px");

            // Date de réception d'acquisition
            Paragraph datePublicationLivre = new Paragraph("Reçu le : " + (doc.getDateAcquisition() != null ? doc.getDateAcquisition().toString() : "?"));   
            datePublicationLivre.getStyle().set("position", "absolute");
            datePublicationLivre.getStyle().set("right", "5px");
            datePublicationLivre.getStyle().set("bottom", "5px");
            datePublicationLivre.getStyle().set("margin", "0");
            datePublicationLivre.getStyle().set("font-size", "11px");

            card.add(titreLivreDiv, metaLabel, descriptionLivre, datePublicationLivre);
            
            card.addClickListener(e -> {
                this.getUI().ifPresent(ui -> ui.navigate("accueil/document/" + doc.getId()));
            });
            
            container.add(card);
        }
    }
}