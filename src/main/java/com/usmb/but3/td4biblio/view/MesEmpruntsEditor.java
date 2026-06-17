package com.usmb.but3.td4biblio.view;

import java.time.LocalDate;
import org.springframework.context.annotation.Scope;
import com.usmb.but3.td4biblio.entity.Emprunt;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@Scope("prototype")
@SpringComponent
@UIScope
public class MesEmpruntsEditor extends VerticalLayout implements BeforeEnterObserver {

    private final EmpruntService empruntService;
    private Emprunt emprunt;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur == null) {
            event.rerouteTo("login"); // redirect to login page
        } else if (LoginView.utilisateur.getRoleUtilisateur().getId() != 2) {
            event.rerouteTo("erreur/permission");

        }
    }

    /* Champs d'affichage (tous en Read-Only pour l'utilisateur) */
    TextField document = new TextField("Document emprunté");
    DatePicker dateDebut = new DatePicker("Date de début");
    DatePicker dateFinPrevue = new DatePicker("Date de fin prévue");
    DatePicker dateRendu = new DatePicker("Date de retour");

    HorizontalLayout fields = new HorizontalLayout(document, dateDebut, dateFinPrevue, dateRendu);

    /* Boutons */
    Button prolongerBtn = new Button("Prolonger mon emprunt", VaadinIcon.TIME_FORWARD.create());
    Button cancel = new Button("Fermer");

    HorizontalLayout actions = new HorizontalLayout(prolongerBtn, cancel);

    Binder<Emprunt> binder = new Binder<>(Emprunt.class);
    private ChangeHandler changeHandler;

    public MesEmpruntsEditor(EmpruntService empruntService) {
        this.empruntService = empruntService;

        add(fields, actions);

        // Tout est bloqué pour l'utilisateur lambda
        document.setReadOnly(true);
        dateDebut.setReadOnly(true);
        dateFinPrevue.setReadOnly(true);
        dateRendu.setReadOnly(true);

        // Liaison des données (on passe par le titre du document)
        binder.forField(document).bind(e -> e.getDocument() != null ? e.getDocument().getTitre() : "", (e, v) -> {
        });
        binder.bindInstanceFields(this);

        setSpacing(true);
        prolongerBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        cancel.addClickListener(e -> setVisible(false));

        // Logique de Prolongation Unique par l'utilisateur
        prolongerBtn.addClickListener(e -> {
            if (emprunt != null) {
                Utilisateur user = LoginView.utilisateur; // Récupération de l'utilisateur connecté

                // On récupère sa durée max en semaine (par défaut 2 si non renseigné)
                int semainesAProlonger = (user.getDureeEmpruntMax() != null) ? user.getDureeEmpruntMax() : 2;

                // Calcul de la nouvelle date
                LocalDate nouvelleDateFin = emprunt.getDateFinPrevue().plusWeeks(semainesAProlonger);

                // Application des changements
                emprunt.setDateFinPrevue(nouvelleDateFin);
                emprunt.setProlongation(true); // Bloque les futures prolongations

                try {
                    empruntService.saveEmprunt(emprunt);
                    Notification
                            .show("Votre emprunt a été prolongé de " + semainesAProlonger + " semaines avec succès !",
                                    4000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    changeHandler.onChange(); // Recharge la grille
                } catch (Exception ex) {
                    Notification
                            .show("Erreur lors de la prolongation : " + ex.getMessage(), 5000,
                                    Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEmprunt(Emprunt e) {
        if (e == null) {
            setVisible(false);
            return;
        }

        emprunt = e;
        binder.setBean(emprunt);

        // Un utilisateur peut prolonger uniquement si :
        // 1. Le livre n'est pas encore rendu
        // 2. Le livre n'a JAMAIS été prolongé auparavant
        boolean dejàRendu = e.getDateRendu() != null;
        boolean dejaProlonge = Boolean.TRUE.equals(e.getProlongation());

        prolongerBtn.setVisible(!dejàRendu && !dejaProlonge);

        setVisible(true);
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}