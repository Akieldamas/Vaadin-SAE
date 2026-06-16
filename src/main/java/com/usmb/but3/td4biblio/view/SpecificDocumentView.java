package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.GenreDocument;

import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "accueil/document")
public class SpecificDocumentView extends VerticalLayout implements HasUrlParameter<String> {
    DocumentService documentService;
    Document currentDocument;
    public SpecificDocumentView(DocumentService documentService){
        this.documentService=documentService;

    }
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        // TODO Auto-generated method stub
        this.currentDocument = documentService.getDocumentById(Integer.parseInt(parameter));
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set( "border-bottom" , "2px solid Blue");

        add(header);
        VerticalLayout vLayout = new VerticalLayout();
        H1 title = new H1(currentDocument.getTitre());
        Div div = new Div();
        Paragraph descParagraph = new Paragraph(currentDocument.getDescription());

        div.add(descParagraph);
        if(currentDocument.getLienGif()!=null){
            Image gif = new Image(currentDocument.getLienGif(), "gif descriptif");
            div.add(gif);
        }   
        String descriptionDetaillee = (currentDocument.getNbPages()!=null?"Ce livre comporte "+currentDocument.getNbPages()+" pages.":"")+" Il a été réalisé par "+currentDocument.getAuteur().getNom();
        Paragraph descriptionDetailleeP = new Paragraph(descriptionDetaillee);

        vLayout.add(title, div,descriptionDetailleeP);
        add(vLayout);
    }
    
}
