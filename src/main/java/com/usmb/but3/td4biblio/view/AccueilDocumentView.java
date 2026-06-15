package com.usmb.but3.td4biblio.view;

import java.util.List;

import com.nimbusds.jose.Header;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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

@PageTitle("AccueilDocument")
@Route(value = "accueil/document")
public class AccueilDocumentView extends VerticalLayout {

    public static Utilisateur utilisateur;
    public AccueilDocumentView(UtilisateurRepo utilisateurRepo, DocumentService documentService) {


        
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set( "border-bottom" , "2px solid Blue");

        add(header);
        VerticalLayout vLayout = new VerticalLayout();

        H1 title = new H1("Consultez les nouveaux documents !");
        title.getStyle().set("font-size", "25px");
        title.getStyle().set("font-weight","bold");
        List<Document> documents = documentService.getAllDocuments();
        Div container = new Div();
        container.getStyle().set("display", "flex");
        container.getStyle().set("flex-direction", "row");
        for(Document doc : documents){
            Div card = new Div();
            card.getStyle().set("diplay", "flex");

            card.getStyle().setWidth("350px");
            card.getStyle().setHeight("200px");

            card.getStyle().set("border", "1px solid blue");
            card.getStyle().set("border-radius", "5px");
            card.getStyle().set("flex-direction", "column");
            card.getStyle().setMargin("10px");
            card.getStyle().set("align-items", "center");
            card.getStyle().set("justify-content", "center");
            Div titreLivreDiv = new Div();
            titreLivreDiv.getStyle().set("width", "100%");
            titreLivreDiv.getStyle().set("border-bottom", "solid 1px blue");

            Paragraph titreLivre = new Paragraph(doc.getTitre());
            titreLivre.getStyle().set("font-size", "16px");
            titreLivre.getStyle().set("font-weight", "bold");
            titreLivre.getStyle().set("margin-left", "5px");

            titreLivreDiv.add(titreLivre);

            Paragraph descriptionLivre = new Paragraph(doc.getDescription());   
            descriptionLivre.getStyle().set("text-align", "justify");
            descriptionLivre.getStyle().set("margin", "5px");

            Paragraph datePublicationLivre = new Paragraph("Livre reçus le : "+doc.getDateAcquisition().toString());   
            descriptionLivre.getStyle().set("text-align", "justify");
            descriptionLivre.getStyle().set("margin", "5px");
            descriptionLivre.getStyle().set("height", "max");
            descriptionLivre.getStyle().set("text-align", "right");

            card.add(titreLivreDiv, descriptionLivre);
            container.add(card);

        }
        container.getStyle().setMargin("5%");
        container.getStyle().set("flex-wrap", "wrap");

        vLayout.add(title,container);
        
        add(vLayout);
    }
}