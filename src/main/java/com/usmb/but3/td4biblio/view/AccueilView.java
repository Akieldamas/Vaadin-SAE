package com.usmb.but3.td4biblio.view;
import java.util.ArrayList;

import com.nimbusds.jose.Header;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Minus.Horizontal;

@PageTitle("Accueil")
@Route(value = "accueil")
public class AccueilView extends VerticalLayout {

    public static Utilisateur utilisateur;
    public AccueilView(UtilisateurRepo utilisateurRepo) {


        
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set( "border-bottom" , "2px solid Blue");
        if(LoginView.utilisateur!=null){
            ArrayList<Button> buttons = new ArrayList<Button>();
            Button accessApp = new Button("Application", e -> {
                this.getUI().ifPresent(ui -> ui.navigate("auteur"));
            });
            Button accessDocs = new Button("Documents", e -> {
                this.getUI().ifPresent(ui -> ui.navigate("auteur"));
            });
            buttons.add(accessApp);
            buttons.add(accessDocs);
        }


        add(header);
        VerticalLayout vLayout = new VerticalLayout();
        H1 title = new H1("BIBLIO Vaadin, votre ERP de bibliothèque !");
        Text paragraph = new Text("Notre logiciel vous permettra de visualiser, créer, supprimer et modifier simplement l'ensemble des constantes liées au métier de bibliothécaire.");
        Image illustration1 = new Image("images/bibliothecaire1.jpg", "illustration bibliothecaire");
        Image illustration2 = new Image("images/bibliothecaire2.jpg", "illustration bibliothecaire");
        Image illustration3 = new Image("images/bibliothecaire3.jpg", "illustration bibliothecaire");
        HorizontalLayout hLayout = new HorizontalLayout(illustration1,illustration2,illustration3);
        illustration1.setWidth("20vw");
        illustration2.setWidth("20vw");
        illustration3.setWidth("20vw");
        hLayout.setWidthFull();
        hLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        vLayout.add(title, paragraph, hLayout);
        
        add(vLayout);
    }
}