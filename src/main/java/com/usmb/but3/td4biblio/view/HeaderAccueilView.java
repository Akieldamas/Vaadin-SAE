package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

public class HeaderAccueilView extends HorizontalLayout {
    
    public HeaderAccueilView() {
        setWidthFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        addClassNames(Padding.MEDIUM, BoxSizing.BORDER);
        getStyle().set("background-color", "white");
        getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.05)");

        // 1. Logo / Identité (Gauche)
        Div brand = new Div();
        if (LoginView.utilisateur != null) {
            Span name = new Span(LoginView.utilisateur.getPrenom() + " " + LoginView.utilisateur.getNom());
            name.addClassNames(FontWeight.BOLD, TextColor.PRIMARY);
            brand.add(VaadinIcon.USER.create(), new Span(" "), name);
        } else {
            var icon = VaadinIcon.CUBES.create();
            icon.addClassNames(TextColor.PRIMARY, IconSize.SMALL);
            var text = new Span("BIBLIO");
            text.addClassNames(FontWeight.BOLD, FontSize.LARGE);
            brand.add(icon, new Span(" "), text);
        }
        brand.addClassNames(Display.FLEX, AlignItems.CENTER, Gap.SMALL);

        // 2. Navigation (Centre)
        HorizontalLayout nav = new HorizontalLayout();
        nav.setAlignItems(FlexComponent.Alignment.CENTER);
        
        Button accueil = createMenuButton("Accueil", VaadinIcon.HOME, "accueil");
        Button docs = createMenuButton("Catalogue", VaadinIcon.BOOK, "accueil/document");
        
        nav.add(accueil, docs);

        if (LoginView.utilisateur != null) {
            if (LoginView.utilisateur.getRoleUtilisateur().getId() == 1) {
                nav.add(createMenuButton("Gestion", VaadinIcon.COG, "auteur"));
            } else if (LoginView.utilisateur.getRoleUtilisateur().getId() == 2) {
                nav.add(createMenuButton("Mes Emprunts", VaadinIcon.NOTEBOOK, "accueil/mes-emprunts"));
            }
        }

        // 3. Actions (Droite)
        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);

        if (LoginView.utilisateur == null) {
            Button login = new Button("Connexion", VaadinIcon.SIGN_IN.create());
            login.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            login.addClickListener(e -> UI.getCurrent().navigate("login"));
            
            Button reg = new Button("Inscription");
            reg.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            reg.addClickListener(e -> UI.getCurrent().navigate("register"));
            
            actions.add(login, reg);
        } else {
            Button logout = new Button("Déconnexion", VaadinIcon.SIGN_OUT.create());
            logout.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            logout.addClickListener(e -> {
                LoginView.utilisateur = null;
                UI.getCurrent().getPage().reload();
            });
            actions.add(logout);
        }

        // Assemblage : utilisation de FlexGrow pour pousser les éléments
        Div spacer = new Div();
        spacer.getStyle().set("flex-grow", "1");
        
        add(brand, spacer, nav, spacer, actions);
    }

    private Button createMenuButton(String text, VaadinIcon icon, String route) {
        Button btn = new Button(text, icon.create());
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.addClickListener(e -> UI.getCurrent().navigate(route));
        return btn;
    }
}