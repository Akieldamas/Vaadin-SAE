package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class BiblioEditor extends VerticalLayout implements KeyNotifier {

    private final UtilisateurService utilisateurService;
    private Utilisateur utilisateur;

    /* Champs du formulaire */
    TextField nom = new TextField("Nom");
    TextField prenom = new TextField("Prénom");
    EmailField email = new EmailField("Email");
    TextField numeroCarte = new TextField("Numéro de carte");
    DatePicker dateFinAbonnement = new DatePicker("Date fin d'abonnement");
    IntegerField dureeEmpruntMax = new IntegerField("Durée max (semaines)");
    IntegerField maxEmprunts = new IntegerField("Max emprunts simultanés");

    /* Boutons d'action */
    Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
    Button cancel = new Button("Annuler");
    Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());

    HorizontalLayout champsInfos = new HorizontalLayout(nom, prenom, email, numeroCarte);
    HorizontalLayout champsAbo = new HorizontalLayout(dateFinAbonnement, dureeEmpruntMax, maxEmprunts);
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Utilisateur> binder = new Binder<>(Utilisateur.class);
    private ChangeHandler changeHandler;

    public BiblioEditor(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;

        add(champsInfos, champsAbo, actions);

        // Lier les champs Vaadin aux attributs de l'entité Utilisateur
        binder.bindInstanceFields(this);

        setSpacing(true);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editUtilisateur(utilisateur));
        
        setVisible(false);
    }

    void delete() {
        utilisateurService.deleteUtilisateurById(utilisateur.getId());
        changeHandler.onChange();
    }

    void save() {
        if (binder.writeBeanIfValid(utilisateur)) {
            utilisateurService.saveUtilisateur(utilisateur);
            changeHandler.onChange();
        }
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editUtilisateur(Utilisateur u) {
        if (u == null) {
            setVisible(false);
            return;
        }

        final boolean persisted = u.getId() != null;
        utilisateur = u;

        // Le bouton annuler n'apparaît que si on modifie un utilisateur existant
        cancel.setVisible(persisted);
        
        // Charger les données de l'utilisateur dans le formulaire
        binder.setBean(utilisateur);
        setVisible(true);
        nom.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}