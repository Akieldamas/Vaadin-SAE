package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.usmb.but3.td4biblio.service.FormatService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DocumentEditor extends VerticalLayout implements KeyNotifier {

    private final DocumentService documentService;
    private final AuteurService auteurService;
    private final EditeurService editeurService;
    private final FormatService formatService;

    private Document document;

    /* Champs de saisie adaptés au nouveau schéma */
    TextField titre = new TextField("Titre");
    TextArea description = new TextArea("Description");
    TextField codeIsbn = new TextField("Code ISBN");
    IntegerField nbPages = new IntegerField("Nombre de pages");
    DatePicker datePublication = new DatePicker("Date de publication");
    DatePicker dateAcquisition = new DatePicker("Date d'acquisition");
    
    // ComboBox pour les relations
    ComboBox<Auteur> auteur = new ComboBox<>("Auteur");
    ComboBox<Editeur> editeur = new ComboBox<>("Éditeur");
    ComboBox<Format> format = new ComboBox<>("Format");

    /* Boutons d'action */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Document> binder = new Binder<>(Document.class);
    private ChangeHandler changeHandler;

    public DocumentEditor(DocumentService documentService, AuteurService auteurService, 
                          EditeurService editeurService, FormatService formatService) {
        this.documentService = documentService;
        this.auteurService = auteurService;
        this.editeurService = editeurService;
        this.formatService = formatService;

        // Configuration des ComboBox
        auteur.setItemLabelGenerator(Auteur::getDesc);
        editeur.setItemLabelGenerator(Editeur::getNom);
        format.setItemLabelGenerator(f -> f.getLongueur() + "x" + f.getLargeur() + " (" + f.getPoids() + "g)");

        add(titre, description, auteur, editeur, format, codeIsbn, nbPages, datePublication, dateAcquisition, actions);

        // Mapping automatique des champs
        binder.bindInstanceFields(this);

        // Validations obligatoires (NOT NULL dans le SQL)
        binder.forField(auteur).asRequired("L'auteur est obligatoire").bind(Document::getAuteur, Document::setAuteur);
        binder.forField(editeur).asRequired("L'éditeur est obligatoire").bind(Document::getEditeur, Document::setEditeur);
        binder.forField(format).asRequired("Le format est obligatoire").bind(Document::getFormat, Document::setFormat);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        addKeyPressListener(Key.ENTER, e -> save());
        
        setVisible(false);
    }

    void save() {
        if (binder.validate().isOk()) {
            documentService.saveDocument(document);
            changeHandler.onChange();
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

        cancel.setVisible(persisted);
        binder.setBean(document);
        setVisible(true);
        titre.focus();
    }

    public interface ChangeHandler { void onChange(); }
    public void setChangeHandler(ChangeHandler h) { this.changeHandler = h; }
}