package com.usmb.but3.td4biblio.view;

import java.time.LocalDate;
import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class EmpruntEditor extends VerticalLayout implements KeyNotifier {

    private final EmpruntService empruntService;
    private final UtilisateurService utilisateurService;
    private final DocumentService documentService;

    private Emprunt emprunt;

    /* Champs d'édition */
    ComboBox<Utilisateur> utilisateur = new ComboBox<>("Utilisateur");
    ComboBox<Document> document = new ComboBox<>("Document");
    DatePicker dateDebut = new DatePicker("Début d'emprunt");
    DatePicker dateFinPrevue = new DatePicker("Fin prévue");
    DatePicker dateRendu = new DatePicker("Date de rendu");

    HorizontalLayout fieldsTop = new HorizontalLayout(utilisateur, document);
    HorizontalLayout fieldsBottom = new HorizontalLayout(dateDebut, dateFinPrevue, dateRendu);

    /* Boutons d'action standards */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
    
    /* Boutons métier */
    Button prolongerBtn = new Button("Prolonger", VaadinIcon.TIME_FORWARD.create());
    Button confirmerProlongationBtn = new Button("Confirmer la prolongation", VaadinIcon.CHECK.create());
    Button rendreBtn = new Button("Enregistrer le retour", VaadinIcon.DOWNLOAD.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    HorizontalLayout actionsMetier = new HorizontalLayout(prolongerBtn, confirmerProlongationBtn, rendreBtn);

    Binder<Emprunt> binder = new Binder<>(Emprunt.class);
    private ChangeHandler changeHandler;

    public EmpruntEditor(EmpruntService empruntService, UtilisateurService utilisateurService, DocumentService documentService) {
        this.empruntService = empruntService;
        this.utilisateurService = utilisateurService;
        this.documentService = documentService;

        add(fieldsTop, fieldsBottom, actionsMetier, actions);

        utilisateur.setItems(utilisateurService.getUtilisateursAutorises());
        utilisateur.setItemLabelGenerator(u -> u.getNom() + " " + u.getPrenom() + " (" + u.getNumeroCarte() + ")");
        document.setItemLabelGenerator(Document::getTitre);

        // Par défaut, tout est bloqué
        dateDebut.setReadOnly(true);
        dateFinPrevue.setReadOnly(true);
        dateRendu.setReadOnly(true);

        binder.bindInstanceFields(this);

        binder.forField(utilisateur).asRequired("L'utilisateur est obligatoire").bind(Emprunt::getUtilisateur, Emprunt::setUtilisateur);
        binder.forField(document).asRequired("Le document est obligatoire").bind(Emprunt::getDocument, Emprunt::setDocument);

        setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        prolongerBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        confirmerProlongationBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rendreBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmprunt(emprunt));
        
        // ETAPE 1 : Débloque la saisie de la date de fin
        prolongerBtn.addClickListener(e -> {
            if (emprunt != null) {
                dateFinPrevue.setReadOnly(false); 
                dateFinPrevue.setMin(emprunt.getDateFinPrevue().plusDays(1)); 
                
                prolongerBtn.setVisible(false);
                rendreBtn.setVisible(false);
                confirmerProlongationBtn.setVisible(true); 
                
                Notification.show("Veuillez choisir la nouvelle date de fin, puis confirmez.", 4000, Notification.Position.MIDDLE);
            }
        });

        // ETAPE 2 : Validation de la nouvelle date saisie
        confirmerProlongationBtn.addClickListener(e -> {
            if (emprunt != null) {
                LocalDate nouvelleDate = dateFinPrevue.getValue();
                if (nouvelleDate == null) {
                    Notification.show("Veuillez choisir une date valide.", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
                
                emprunt.setDateFinPrevue(nouvelleDate);
                emprunt.setProlongation(true); // 🔥 On bascule le booléen à TRUE !
                
                try {
                    empruntService.saveEmprunt(emprunt);
                    Notification.show("Prolongation enregistrée avec succès !").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    changeHandler.onChange();
                } catch (Exception ex) {
                    Notification.show("Erreur : " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        rendreBtn.addClickListener(e -> {
            if (emprunt != null) {
                emprunt.setDateRendu(LocalDate.now());
                try {
                    empruntService.saveEmprunt(emprunt);
                    Notification.show("Retour enregistré avec succès !").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    changeHandler.onChange();
                } catch (Exception ex) {
                    Notification.show("Erreur : " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        setVisible(false);
    }

    void delete() {
        empruntService.deleteEmprunt(emprunt);
        changeHandler.onChange();
    }

    void save() {
        if (binder.writeBeanIfValid(emprunt)) {
            try {
                empruntService.saveEmprunt(emprunt);
                changeHandler.onChange();
            } catch (IllegalStateException ex) {
                Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEmprunt(Emprunt e) {
        if (e == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = e.getId() != null; // L'ID existe, donc c'est une modification
        emprunt = e;

        cancel.setVisible(persisted);
        utilisateur.setReadOnly(persisted);
        document.setReadOnly(persisted);
        
        // On sécurise et réinitialise l'état des champs
        dateFinPrevue.setReadOnly(true);
        confirmerProlongationBtn.setVisible(false);

        if (persisted) {
            document.setItems(documentService.getAllDocuments());
            fieldsBottom.setVisible(true);
            
            boolean isActif = e.getDateRendu() == null;
            
            // 🔥 CORRECTION ICI : On vérifie proprement le booléen
            boolean dejaProlonge = Boolean.TRUE.equals(e.getProlongation());
            
            // Le bouton Prolonger s'affiche si l'emprunt est en cours ET n'a pas encore été prolongé
            prolongerBtn.setVisible(isActif && !dejaProlonge);
            rendreBtn.setVisible(isActif);
            save.setVisible(false); // On cache le bouton "Sauvegarder" global pour forcer l'usage des boutons métier
            
        } else {
            document.setItems(documentService.getDocumentsDisponibles());
            fieldsBottom.setVisible(false);
            prolongerBtn.setVisible(false);
            rendreBtn.setVisible(false);
            save.setVisible(true);
        }

        binder.setBean(emprunt);
        setVisible(true);
        utilisateur.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}