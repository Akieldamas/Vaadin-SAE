package com.usmb.but3.td4biblio.view;


import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.ImportExportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
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
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StringUtils;

@Route(value = "document") 
@PageTitle("Gestion des Documents")
@Menu(title = "Documents", order = 1, icon = "vaadin:book")
public class DocumentView extends VerticalLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur==null) {
            event.rerouteTo("login"); // redirect to login page
        }
}   

    private final DocumentService documentService;
    private final ImportExportService importExportService;
    final Grid<Document> grid;
    final TextField filter;
    private final Button addNewBtn;
    private final Button exportBtn;
    private final Button downloadTemplateBtn;


    public DocumentView(DocumentService documentService, DocumentEditor editor, ImportExportService importExportService) {
        if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		} 
        this.documentService = documentService;
        this.importExportService = importExportService;
        this.grid = new Grid<>(Document.class, false); // false pour définir les colonnes manuellement
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document", VaadinIcon.PLUS.create());
        this.exportBtn = new Button("Export CSV", VaadinIcon.DOWNLOAD.create());
        this.downloadTemplateBtn = new Button("Télécharger la Template Import CSV", VaadinIcon.FILE.create());
        // add button to download a template   

        exportBtn.addClickListener(e -> {
            String csvContent = importExportService.ExportDocumentsToCSV();
            InputStreamFactory factory = () -> new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.ISO_8859_1));
            StreamResource resource = new StreamResource("documents.csv", factory);
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("style", "display:none");
            add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
        });

        downloadTemplateBtn.addClickListener(e -> {
            String csvContent = importExportService.DownloadCSVTemplate("document");
            InputStreamFactory factory = () -> new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.ISO_8859_1));
            StreamResource resource = new StreamResource("documents.csv", factory);
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("style", "display:none");
            add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
        });

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, exportBtn, downloadTemplateBtn);
        add(actions, grid, editor);

        // Configuration des colonnes du Grid
        grid.addColumn(Document::getId).setHeader("ID").setWidth("70px").setFlexGrow(0);
        grid.addColumn(Document::getTitre).setHeader("Titre").setSortable(true);
        
        grid.addColumn(d -> d.getAuteur() != null ? d.getAuteur().getNom() : "")
            .setHeader("Auteur");
            
        grid.addColumn(d -> d.getEditeur() != null ? d.getEditeur().getNom() : "")
            .setHeader("Éditeur");

        grid.addColumn(document -> {
            StringBuilder genres = new StringBuilder();
        
            for (GenreDocument g : document.getGenres()) {
                if (genres.length() > 0) {
                    genres.append(", ");
                }
                genres.append(g.getNom());
            }
        
            return genres.toString();
        }).setHeader("Genre(s)");
            
        grid.addColumn(Document::getCodeIsbn).setHeader("ISBN");
        grid.addColumn(Document::getDatePublication).setHeader("Publication");

        grid.setHeight("400px");
        filter.setPlaceholder("Filtrer par titre...");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listDocuments(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> editor.editDocument(e.getValue()));
        
        addNewBtn.addClickListener(e -> editor.editDocument(new Document()));
        exportBtn.addClickListener(e -> importExportService.ExportDocumentsToCSV());

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