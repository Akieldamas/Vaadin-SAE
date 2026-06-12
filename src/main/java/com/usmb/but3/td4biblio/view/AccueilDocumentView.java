package com.usmb.but3.td4biblio.view;

import java.util.List;

import com.nimbusds.jose.Header;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.usmb.but3.td4biblio.service.DocumentService;
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
        List<Document> documents = documentService.getAllDocuments();
        for(Document doc : documents){
            Text text = new Text(doc.getTitre());
            add(text);
        }
        vLayout.add();
        
        add(vLayout);
    }
}