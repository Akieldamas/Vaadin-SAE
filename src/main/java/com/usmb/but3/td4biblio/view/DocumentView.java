package com.usmb.but3.td4biblio.view;


import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAutoUpload(true);
        upload.setAcceptedFileTypes(".csv");
        
        // Add an explicit "Importer CSV" button
        Button uploadBtn = new Button("Importer CSV");
        upload.setUploadButton(uploadBtn);
        
        // Keep it compact — no drop zone text
        upload.setDropLabel(null);
        upload.setWidth("auto"); // don't let it stretch
        
        upload.addSucceededListener(event -> {
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(buffer.getInputStream(), Charset.forName("Windows-1252")))) {
                
                String headerLine = reader.readLine();
                String[] headers = headerLine.split(";");
                
                List<Map<String, String>> rows = new ArrayList<>();
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(";");
                    Map<String, String> row = new HashMap<>();
                    for (int i = 0; i < headers.length; i++) {
                        row.put(headers[i].trim(), i < values.length ? values[i].trim() : "");
                    }
                    rows.add(row);
                }

                documentService.importFromCsv(rows);
                listDocuments(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, upload);
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