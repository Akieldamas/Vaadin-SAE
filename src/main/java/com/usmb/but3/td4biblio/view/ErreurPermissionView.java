package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
@Route(value = "erreur/permission")
@PageTitle("erreur")
public class ErreurPermissionView extends VerticalLayout {
    public ErreurPermissionView(){
        HeaderView headerView = new HeaderView();
        H1 titre = new H1("Malheureusement vous n'avez pas les permissions pour accéder à cette page ! :(");
        Button redirect = new Button("🡺 Redirection 🡸", e->{
            this.getUI().ifPresent(ui -> ui.navigate("/accueil"));

        });
        add(headerView,titre, redirect);
    }
}
