        package com.usmb.but3.td4biblio.view;

        import java.util.Comparator;
        import java.util.List;
        import java.util.stream.Collectors;

        import com.usmb.but3.td4biblio.entity.Document;
        import com.usmb.but3.td4biblio.entity.Utilisateur;
        import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
        import com.usmb.but3.td4biblio.service.DocumentService;
        import com.vaadin.flow.component.button.Button;
        import com.vaadin.flow.component.button.ButtonVariant;
        import com.vaadin.flow.component.combobox.ComboBox;
        import com.vaadin.flow.component.html.Div;
        import com.vaadin.flow.component.html.H1;
        import com.vaadin.flow.component.html.Paragraph;
        import com.vaadin.flow.component.html.Span;
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

                        // --- EN-TÊTE AVEC BOUTON D'ACCÈS AUX EMPRUNTS ---
                        HorizontalLayout topLayout = new HorizontalLayout();
                        topLayout.setWidthFull();
                        topLayout.setAlignItems(Alignment.CENTER);

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
                                        .map(doc -> doc.getBibliotheque() != null ? doc.getBibliotheque().getNom()
                                                        : "Non spécifiée")
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
                                        .map(doc -> doc.getAuteur() != null
                                                        ? doc.getAuteur().getNom() + " " + doc.getAuteur().getPrenom()
                                                        : "Inconnu")
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
                        // Utilisation de CSS Grid pour un affichage responsive automatique
                        container.getStyle().set("display", "grid");
                        container.getStyle().set("grid-template-columns", "repeat(auto-fill, minmax(300px, 1fr))");
                        container.getStyle().set("gap", "20px"); // Espace entre les cartes
                        container.getStyle().set("padding", "20px");
                        container.setWidthFull();

                        vLayout.add(topLayout, filterLayout, container);
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
                        documents.sort(Comparator.comparing(Document::getDateAcquisition,
                                        Comparator.nullsLast(Comparator.naturalOrder())).reversed());

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
                                                        (doc.getTitre() != null
                                                                        && doc.getTitre().toLowerCase().contains(textFilter))
                                                        ||
                                                        (doc.getDescription() != null && doc.getDescription().toLowerCase()
                                                                        .contains(textFilter)))

                                        // Filtre Bibliothèque
                                        .filter(doc -> selectedBib == null ||
                                                        (doc.getBibliotheque() != null
                                                                        && doc.getBibliotheque().getNom().equals(selectedBib)))

                                        // Filtre Type
                                        .filter(doc -> selectedType == null ||
                                                        (doc.getTypeDocument() != null
                                                                        && doc.getTypeDocument().getNom().equals(selectedType)))

                                        // Filtre Éditeur
                                        .filter(doc -> selectedEditeur == null ||
                                                        (doc.getEditeur() != null
                                                                        && doc.getEditeur().getNom().equals(selectedEditeur)))

                                        // Filtre Auteur
                                        .filter(doc -> selectedAuteur == null ||
                                                        (doc.getAuteur() != null && (doc.getAuteur().getNom() + " "
                                                                        + doc.getAuteur().getPrenom()).equals(selectedAuteur)))

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
                        // Génération des composants cartes graphiques (Modernisées)
                        for (Document doc : filteredDocuments) {
                                Div card = new Div();
                                card.addClassName("card");

                                // Style de base de la carte
                                card.getStyle().set("display", "flex");
                                card.getStyle().set("flex-direction", "column");
                                card.getStyle().set("background-color", "white");
                                card.getStyle().set("border-radius", "10px");
                                card.getStyle().set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");
                                card.getStyle().set("padding", "15px");
                                card.getStyle().set("transition", "transform 0.2s, box-shadow 0.2s");
                                card.getStyle().set("cursor", "pointer"); // Indique que c'est cliquable
                                card.getStyle().set("height", "100%"); // Prend toute la hauteur de sa cellule de grille
                                card.getStyle().set("box-sizing", "border-box");

                                // Effet Hover
                                card.getElement().addEventListener("mouseenter", e -> {
                                        card.getStyle().set("transform", "translateY(-5px)");
                                        card.getStyle().set("box-shadow", "0 8px 16px rgba(0,0,0,0.2)");
                                });
                                card.getElement().addEventListener("mouseleave", e -> {
                                        card.getStyle().set("transform", "translateY(0)");
                                        card.getStyle().set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");
                                });

                                // --- En-tête de la carte (Titre + Badge Type) ---
                                HorizontalLayout headerLayout = new HorizontalLayout();
                                headerLayout.setWidthFull();
                                headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
                                headerLayout.setAlignItems(Alignment.START);

                                Paragraph titreLivre = new Paragraph(doc.getTitre());
                                titreLivre.getStyle().set("font-size", "18px");
                                titreLivre.getStyle().set("font-weight", "600");
                                titreLivre.getStyle().set("margin", "0");
                                titreLivre.getStyle().set("color", "#2c3e50");
                                titreLivre.getStyle().set("flex", "1"); // Prend l'espace disponible

                                // Badge Type de document
                                String typeDoc = doc.getTypeDocument() != null ? doc.getTypeDocument().getNom() : "Non classé";
                                Span badgeType = new Span(typeDoc);
                                badgeType.getElement().getThemeList().add("badge");
                                // Couleur dynamique selon le type (optionnel)
                                if (typeDoc.toLowerCase().contains("livre"))
                                        badgeType.getElement().getThemeList().add("success");
                                else if (typeDoc.toLowerCase().contains("dvd"))
                                        badgeType.getElement().getThemeList().add("error");
                                else
                                        badgeType.getElement().getThemeList().add("contrast");

                                headerLayout.add(titreLivre, badgeType);

                                // --- Séparateur ---
                                Div hr = new Div();
                                hr.getStyle().set("height", "1px");
                                hr.getStyle().set("background-color", "#eaeaea");
                                hr.getStyle().set("margin", "10px 0");
                                hr.setWidthFull();

                                // --- Corps de la carte (Description) ---
                                Paragraph descriptionLivre = new Paragraph(doc.getDescription());
                                descriptionLivre.getStyle().set("font-size", "14px");
                                descriptionLivre.getStyle().set("color", "#555");
                                descriptionLivre.getStyle().set("margin", "0 0 15px 0");
                                descriptionLivre.getStyle().set("flex-grow", "1"); // Pousse le footer vers le bas

                                // Limitation du texte pour éviter les cartes géantes (Truncate)
                                descriptionLivre.getStyle().set("display", "-webkit-box");
                                descriptionLivre.getStyle().set("-webkit-line-clamp", "4"); // Max 4 lignes
                                descriptionLivre.getStyle().set("-webkit-box-orient", "vertical");
                                descriptionLivre.getStyle().set("overflow", "hidden");

                                // --- Footer de la carte (Auteur, Biblio, Date) ---
                                VerticalLayout footerLayout = new VerticalLayout();
                                footerLayout.setPadding(false);
                                footerLayout.setSpacing(false);
                                footerLayout.getStyle().set("margin-top", "auto"); // Toujours en bas

                                String bibDoc = doc.getBibliotheque() != null ? doc.getBibliotheque().getNom() : "N/A";
                                Paragraph bibLabel = new Paragraph(bibDoc);
                                bibLabel.getStyle().set("font-size", "12px");
                                bibLabel.getStyle().set("color", "#888");
                                bibLabel.getStyle().set("margin", "0 0 5px 0");

                                String auteurDoc = doc.getAuteur() != null
                                                ? doc.getAuteur().getNom() + " " + doc.getAuteur().getPrenom()
                                                : "Inconnu";
                                Paragraph auteurLabel = new Paragraph(auteurDoc);
                                auteurLabel.getStyle().set("font-size", "12px");
                                auteurLabel.getStyle().set("color", "#888");
                                auteurLabel.getStyle().set("margin", "0 0 5px 0");

                                Paragraph datePublicationLivre = new Paragraph("Ajouté le : "
                                                + (doc.getDateAcquisition() != null ? doc.getDateAcquisition().toString()
                                                                : "?"));
                                datePublicationLivre.getStyle().set("font-size", "11px");
                                datePublicationLivre.getStyle().set("color", "#aaa");
                                datePublicationLivre.getStyle().set("margin", "0");
                                datePublicationLivre.getStyle().set("text-align", "right");
                                datePublicationLivre.setWidthFull();

                                footerLayout.add(bibLabel, auteurLabel, datePublicationLivre);

                                // --- Assemblage final ---
                                card.add(headerLayout, hr, descriptionLivre, footerLayout);

                                card.addClickListener(e -> {
                                        this.getUI().ifPresent(ui -> ui.navigate("accueil/document/" + doc.getId()));
                                });

                                container.add(card);
                        }
                }
        }