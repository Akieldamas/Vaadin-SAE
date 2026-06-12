package com.usmb.but3.td4biblio.view;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

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

        }



        // Login / Logout button
        if(LoginView.utilisateur==null){
            Button loginButton = new Button("Login", e -> getUI().ifPresent(ui -> ui.navigate("login")));
            loginButton.getStyle().set("align", "right");

            add(loginButton);

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