package com.usmb.but3.td4biblio.view;
import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.IconSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

public class HeaderAccueilView extends HorizontalLayout {
    public HeaderAccueilView() {
        setWidthFull();  // full width of the page
        setPadding(true);
        setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout();
        HorizontalLayout center = new HorizontalLayout();
        HorizontalLayout end = new HorizontalLayout();

        if(LoginView.utilisateur!=null){
            // App title / logo
            H1 title = new H1(LoginView.utilisateur.getNom()+" "+LoginView.utilisateur.getPrenom());
            layout.add(title);

        } else {
            var appLogo = VaadinIcon.CUBES.create();
            appLogo.addClassNames(TextColor.PRIMARY, IconSize.LARGE);

            var appName = new Span("BIBLIO Vaadin");
            appName.addClassNames(FontWeight.SEMIBOLD, FontSize.LARGE);

            var header = new Div(appLogo, appName);
            header.addClassNames(Display.FLEX, Padding.MEDIUM, Gap.MEDIUM, AlignItems.CENTER);
            layout.add(header);
        }
        Button accueil = new Button("Accueil", e -> {
            this.getUI().ifPresent(ui -> ui.navigate("/accueil"));

        });

        Button document = new Button("Documents", e -> {
            this.getUI().ifPresent(ui -> ui.navigate("/accueil/document"));

        });
        center.add(accueil, document);
        // Login / Logout button
        Button logoutButton=null;
        if(LoginView.utilisateur==null){
            Button loginButton = new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login")));
            loginButton.getStyle().set("align", "right");

            end.addToEnd(loginButton);
            layout.setWidthFull();  // full width of the page
        } else {
            logoutButton = new Button("Logout", e -> {
                LoginView.utilisateur=null;
                UI.getCurrent().getPage().reload();

            });
            Button application = new Button("BIBLIOVaadin", e -> {
                this.getUI().ifPresent(ui -> ui.navigate("/auteur"));

            });
            end.addToEnd(logoutButton);
            center.add(application);
            layout.setWidthFull();  // full width of the page

        }
        center.setWidth("70vw");
        center.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.addToEnd(center, end);

        add(layout);
    }
}