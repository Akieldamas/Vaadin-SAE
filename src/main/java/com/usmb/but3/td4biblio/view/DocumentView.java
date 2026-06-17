package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route(value = "document") 
@PageTitle("Gestion des Documents")
@Menu(title = "Documents", order = 1, icon = "vaadin:book")
public class DocumentView extends VerticalLayout implements BeforeEnterObserver {

    private final DocumentService documentService;
    final Grid<Document> grid;
    final TextField filter;
    private final Button addNewBtn;
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur==null) {
            event.rerouteTo("login"); // redirect to login page
        } else if(LoginView.utilisateur.getRoleUtilisateur().getId()!=1){
            event.rerouteTo("erreur/permission");

        }
    }
    public DocumentView(DocumentService documentService, DocumentEditor editor) {
        if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		} 
        this.documentService = documentService;
        this.grid = new Grid<>(Document.class, false); // false pour définir les colonnes manuellement
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        // Configuration des colonnes du Grid
        grid.addColumn(Document::getId).setHeader("ID").setWidth("70px").setFlexGrow(0);
        grid.addColumn(Document::getTitre).setHeader("Titre").setSortable(true);
        
        grid.addColumn(d -> d.getAuteur() != null ? d.getAuteur().getNom() : "")
            .setHeader("Auteur");
            
        grid.addColumn(d -> d.getEditeur() != null ? d.getEditeur().getNom() : "")
            .setHeader("Éditeur");
            
        grid.addColumn(Document::getCodeIsbn).setHeader("ISBN");
        grid.addColumn(Document::getDatePublication).setHeader("Publication");

        grid.setHeight("400px");
        filter.setPlaceholder("Filtrer par titre...");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listDocuments(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editDocument(e.getValue()));

        addNewBtn.addClickListener(e -> editor.editDocument(new Document()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listDocuments(filter.getValue());
        });

        listDocuments(null);
    }

    void listDocuments(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(documentService.getByTitreContainingIgnoreCase(filterText));
        } else {
            grid.setItems(documentService.getAllDocuments());
        }
    }
}