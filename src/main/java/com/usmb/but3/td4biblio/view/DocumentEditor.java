package com.usmb.but3.td4biblio.view;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Bibliotheque;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.entity.TypeDocument;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.BibliothequeService;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.FormatService;
import com.usmb.but3.td4biblio.service.GenreDocumentService;
import com.usmb.but3.td4biblio.service.ImportExportService;
import com.usmb.but3.td4biblio.service.NotificationService;
import com.usmb.but3.td4biblio.service.TypeDocumentService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;
    private final AuteurService auteurService;
    private final EditeurService editeurService;
    private final FormatService formatService;
    private final ImportExportService importExportService;
    private final BibliothequeService bibliothequeService;
    private final GenreDocumentService genreDocumentService;
    private final TypeDocumentService typeDocumentService;

    private Document document;

    /* Champs de saisie adaptés au nouveau schéma */
    TextField titre = new TextField("Titre");
    TextArea description = new TextArea("Description");
    TextField codeIsbn = new TextField("Code ISBN");
    IntegerField nbPages = new IntegerField("Nombre de pages");
    DatePicker datePublication = new DatePicker("Date de publication");
    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
                
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    Button uploadBtn = new Button("Importer CSV");
    
    // ComboBox pour les relations
    ComboBox<Auteur> auteur = new ComboBox<>("Auteur");
    ComboBox<Editeur> editeur = new ComboBox<>("Éditeur");
    ComboBox<Format> format = new ComboBox<>("Format");
    ComboBox<Bibliotheque> bibliothequeCombo = new ComboBox<>("Bibliothèque");
    ComboBox<TypeDocument> typeDocument = new ComboBox<>("Type de Document");
    @PropertyId("genresList")
    MultiSelectComboBox<GenreDocument> genres = new MultiSelectComboBox<>("Genre(s)");

    /* Boutons d'action */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete, uploadBtn); 

    Binder<Document> binder = new Binder<>(Document.class);
    private ChangeHandler changeHandler;

    public DocumentEditor(DocumentService documentService, AuteurService auteurService, 
                          EditeurService editeurService, FormatService formatService, ImportExportService importExportService, BibliothequeService bibliothequeService, 
                           GenreDocumentService genreDocumentService, TypeDocumentService typeDocumentService) {

        this.documentService = documentService;
        this.auteurService = auteurService;
        this.editeurService = editeurService;
        this.formatService = formatService;
        this.importExportService = importExportService;
        this.bibliothequeService = bibliothequeService;
        this.genreDocumentService = genreDocumentService;
        this.typeDocumentService = typeDocumentService;

        // Configuration des ComboBox
        auteur.setItemLabelGenerator(Auteur::getDesc);
        editeur.setItemLabelGenerator(Editeur::getNom);
        format.setItemLabelGenerator(f -> f.getLongueur() + "x" + f.getLargeur() + " (" + f.getPoids() + "g)");
        
        bibliothequeCombo.setItems(bibliothequeService.getAllBibliotheques());
        bibliothequeCombo.setItemLabelGenerator(Bibliotheque::getNom);
        bibliothequeCombo.setRequiredIndicatorVisible(true);

        genres.setItems(genreDocumentService.getAllGenres());
        genres.setItemLabelGenerator(GenreDocument::getNom);

        typeDocument.setItems(typeDocumentService.getAllTypeDocuments());
        typeDocument.setItemLabelGenerator(TypeDocument::getNom);

        upload.setAutoUpload(true);
        upload.setAcceptedFileTypes(".csv");

        upload.setUploadButton(uploadBtn);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancel.addThemeVariants(ButtonVariant.LUMO_WARNING);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        // Keep it compact — no drop zone text
        upload.setDropLabel(null);
        upload.setWidth("auto"); // don't let it stretch
        
        upload.addSucceededListener(event -> {
            Bibliotheque bibliotheque = bibliothequeCombo.getValue();

            if (bibliotheque == null) {
                NotificationService.showError("Bibliothèque manquante pour importer les documents.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(buffer.getInputStream(), Charset.forName("Windows-1252")))) {
                
                String headerLine = reader.readLine();
                String[] headers = headerLine.split(";");
                
                List<Map<String, String>> rows = new ArrayList<>();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(";");
                    Map<String, String> row = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        row.put(headers[i].trim(), i < values.length ? values[i].trim() : "");
                    }
                    rows.add(row);
                }
        
                Pair<Boolean, String> returned = importExportService.ImportDocumentsFromCSV(rows, bibliothequeCombo.getValue());
                Boolean result = returned.getLeft();
                String message = returned.getRight();
                
                if (result) {
                    NotificationService.showSuccess(message);
                    setVisible(false);;
                    changeHandler.onChange();
                }
                else
                    NotificationService.showError(message);        
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        HorizontalLayout row1 = new HorizontalLayout(titre, description, genres, typeDocument);

        HorizontalLayout row2 = new HorizontalLayout(auteur, editeur, format, codeIsbn);

        HorizontalLayout row3 = new HorizontalLayout(datePublication, dateAcquisition, bibliothequeCombo);

        HorizontalLayout row4 = new HorizontalLayout(save, cancel, delete, upload);

        add(row1, row2, row3, row4);
        
        // Mapping automatique des champs
        binder.bindInstanceFields(this);

        // Validations obligatoires (NOT NULL dans le SQL)
        binder.forField(auteur).asRequired("L'auteur est obligatoire").bind(Document::getAuteur, Document::setAuteur);
        binder.forField(editeur).asRequired("L'éditeur est obligatoire").bind(Document::getEditeur, Document::setEditeur);
        binder.forField(format).asRequired("Le format est obligatoire").bind(Document::getFormat, Document::setFormat);
        binder.forField(genres)
        .asRequired("Genre(s) obligatoires")
        .bind(
            doc -> doc.getGenres() != null
                    ? new HashSet<>(doc.getGenres())
                    : Set.of(),
            (doc, value) -> doc.setGenres(new ArrayList<>(value))
        );
        binder.forField(typeDocument).asRequired("Type de document obligatoire").bind(Document::getTypeDocument, Document::setTypeDocument);
        binder.forField(bibliothequeCombo).asRequired("Bibliothèque à choisir obligatoire").bind(Document::getBibliotheque, Document::setBibliotheque);
        binder.forField(codeIsbn)
        .withValidator(
            isbn -> isbn == null || isbn.length() <= 13,
            "Le code ISBN ne doit pas dépasser 13 caractères"
        )
        .bind(Document::getCodeIsbn, Document::setCodeIsbn);
        
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        addKeyPressListener(Key.ENTER, e -> save());
        
        setVisible(false);
    }

    void save() {
        var validationResult = binder.validate();
    
        if (validationResult.isOk()) {
            try {
                documentService.saveDocument(document);
    
                NotificationService.showSuccess("Document enregistré avec succès.");
    
                changeHandler.onChange();
    
            } catch (Exception e) {
                NotificationService.showError("Erreur lors de l'enregistrement : " + e.getMessage());
            }
        } else {
            NotificationService.showError("Veuillez corriger les erreurs du formulaire.");
        }
    }

    void delete() {
        documentService.deleteDocumentById(document.getId());
        changeHandler.onChange();
    }

    public final void editDocument(Document d) {
        if (d == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = d.getId() != null;
        if (persisted) {
            document = documentService.getDocumentById(d.getId());
        } else {
            document = d;
        }
        
        // Rafraîchir les listes des ComboBox
        auteur.setItems(auteurService.getAllAuteurs());
        editeur.setItems(editeurService.getAllEditeurs());
        format.setItems(formatService.getAllFormats());
        typeDocument.setItems(typeDocumentService.getAllTypeDocuments());
        bibliothequeCombo.setItems(bibliothequeService.getAllBibliotheques());
        genres.setItems(genreDocumentService.getAllGenres());

        cancel.setVisible(true);
        if (document.getGenres() == null) {
            document.setGenres(new ArrayList<>());
        }
        binder.setBean(document);
        setVisible(true);
        titre.focus();
    }

    public interface ChangeHandler { void onChange(); }
    public void setChangeHandler(ChangeHandler h) { this.changeHandler = h; }
}