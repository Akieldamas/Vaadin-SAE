package com.usmb.but3.td4biblio.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.jspecify.annotations.Nullable;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class UtilisateurDrawer extends Composite<VerticalLayout> {

    @FunctionalInterface
    public interface SaveCallback { 
        Utilisateur save(Utilisateur utilisateurDetails);
    }

    private final SaveCallback saveCallback;
    private final UtilisateurForm form;

    public UtilisateurDrawer(SaveCallback saveCallback) {
        this.saveCallback = saveCallback;
        H2 title = new H2("Détail de l'utilisateur");
        form = new UtilisateurForm();

        var saveButton = new Button("Enregistrer", e -> save());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout layout = getContent();
        layout.add(title);
        layout.add(new Scroller(form));
        layout.add(saveButton);
        layout.setWidth("300px");
        addClassName(LumoUtility.BoxShadow.MEDIUM);
        setVisible(false);
    }

    public void setUtilisateurDetails(@Nullable Utilisateur details) {
        form.setUtilisateurDetails(details);
        setVisible(details != null);
    }

    private void save() {
        form.getFormDataObject().ifPresent(details -> {
            Utilisateur saved = saveCallback.save(details);
            form.setUtilisateurDetails(saved);
        });
    }

}
