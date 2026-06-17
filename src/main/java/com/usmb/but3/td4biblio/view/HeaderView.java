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

public class HeaderView extends HorizontalLayout {
    public HeaderView() {
        setWidthFull();  // full width of the page
        setPadding(true);
        setSpacing(true);
        HorizontalLayout layout = new HorizontalLayout();
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

 
        // Login / Logout button
        if(LoginView.utilisateur==null){
            Button loginButton = new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login")));
            Button registerButton = new Button("S'enregistrer", e -> getUI().ifPresent(ui -> ui.navigate("register")));

            loginButton.getStyle().set("align", "right");
            registerButton.getStyle().set("align", "right");

            layout.addToEnd(loginButton, registerButton);
            layout.setWidthFull();  // full width of the page
        } else {
            Button logoutButton = new Button("Logout", e -> {
                LoginView.utilisateur=null;
                UI.getCurrent().getPage().reload();

            });

            layout.addToEnd(logoutButton);
            layout.setWidthFull();  // full width of the page

        }
        add(layout);
    }
}