package com.usmb.but3.td4biblio.product;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.jspecify.annotations.Nullable;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import java.util.Optional;

public class UtilisateurForm extends Composite<FormLayout> {

    private final Binder<Utilisateur> binder;

    private TextField nomField;
    private TextField prenomField;
    private TextField emailField;
    public TextField cardField;
    private DatePicker dateFinAbonnementField;

    public UtilisateurForm() {
        FormLayout layout = getContent();

        nomField = new TextField("Nom");
        nomField.setRequired(true);
        prenomField = new TextField("Prénom");
        prenomField.setRequired(true);
        emailField = new TextField("Email");
        emailField.setRequired(true);
        cardField = new TextField("Numéro de carte");
        cardField.setReadOnly(true);
        dateFinAbonnementField = new DatePicker("Date de fin d'abonnement");

        layout.add(nomField, prenomField, emailField, cardField, dateFinAbonnementField);

        binder = new Binder<>();
        binder.bind(nomField, Utilisateur::getNom, Utilisateur::setNom);
        binder.bind(prenomField, Utilisateur::getPrenom, Utilisateur::setPrenom);
        binder.bind(emailField, Utilisateur::getEmail, Utilisateur::setEmail);
        binder.bind(cardField, Utilisateur::getNumeroCarte, Utilisateur::setNumeroCarte);
        binder.bind(dateFinAbonnementField, Utilisateur::getDateFinAbonnement, Utilisateur::setDateFinAbonnement);

    }

    public void setUtilisateurDetails(@Nullable Utilisateur details) {
        binder.setBean(details);
    }

    public Optional<Utilisateur> getFormDataObject() {
        if (binder.getBean() == null) {
            throw new IllegalStateException("No form data object");
        }
        if (binder.validate().isOk()) {
            return Optional.of(binder.getBean()); 
        } else {
            return Optional.empty(); 
        }
    }
}
