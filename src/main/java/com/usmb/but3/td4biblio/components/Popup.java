package com.usmb.but3.td4biblio.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Popup extends Composite<VerticalLayout> {
    public Popup(String titre, String description){
        Dialog dialog = new Dialog();
        H1 title = new H1(titre);
        Paragraph descParagraph = new Paragraph(description);
        descParagraph.getStyle().set("text-wrap", "wrap");
        dialog.add(title,descParagraph);
        VerticalLayout layout = getContent();
        layout.add(dialog);
        dialog.open();
    }
}
