package com.usmb.but3.td4biblio.view;


import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.service.DocumentService;
import com.usmb.but3.td4biblio.service.EmpruntService;
import com.usmb.but3.td4biblio.service.ImportExportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
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
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import ch.qos.logback.classic.spi.STEUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    private final EmpruntService empruntService;
    final Grid<Document> grid;
    final TextField filter;
    private final Button addNewBtn;
    private final Button exportBtn;
    private final Button downloadTemplateBtn;

    private List<Document> documents = new ArrayList<>();
    
    private final Span totalSpan = new Span();
    private final Span empruntesSpan = new Span();
    private final Span disponiblesSpan = new Span();

    public DocumentView(DocumentService documentService, DocumentEditor editor, ImportExportService importExportService, EmpruntService empruntService) {
        if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		} 
        this.documentService = documentService;
        this.importExportService = importExportService;
        this.empruntService = empruntService;
        this.grid = new Grid<>(Document.class, false); // false pour définir les colonnes manuellement
        this.filter = new TextField();
        this.addNewBtn = new Button("Ajouter un document", VaadinIcon.PLUS.create());
        this.exportBtn = new Button("Export CSV", VaadinIcon.DOWNLOAD.create());
        this.downloadTemplateBtn = new Button("Télécharger la Template Import CSV", VaadinIcon.FILE.create());
        addNewBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        downloadTemplateBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COMPACT);
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

        int empruntes = empruntService.getAllEmprunts().size();

        HorizontalLayout statsBar = new HorizontalLayout(
            createStatCard("Total", totalSpan, documents.size()),
            createStatCard("Empruntés", empruntesSpan, empruntes),
            createStatCard("Disponibles", disponiblesSpan, )
        );

        statsBar.addClassNames(Gap.MEDIUM, Padding.SMALL);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, exportBtn, downloadTemplateBtn);
        add(actions, statsBar, grid, editor);

        // Configuration des colonnes du Grid
        grid.addColumn(Document::getId).setHeader("ID").setWidth("70px").setFlexGrow(0);
        grid.addColumn(Document::getTitre).setHeader("Titre").setSortable(true);
        
        grid.addColumn(d -> d.getAuteur() != null ? d.getAuteur().getNom() : "")
            .setHeader("Auteur");
            
        grid.addColumn(d -> d.getEditeur() != null ? d.getEditeur().getNom() : "")
            .setHeader("Éditeur");

        grid.addComponentColumn(document -> {
            HorizontalLayout badges = new HorizontalLayout();
            badges.setSpacing(true);
            for (GenreDocument g : document.getGenres()) {
                Span badge = new Span(g.getNom());
                badge.getElement().getThemeList().add("badge");
                badges.add(badge);
            }
            return badges;
        }).setHeader("Genre(s)");
            
        grid.addColumn(Document::getCodeIsbn).setHeader("ISBN");
        //grid.addColumn(Document::getDatePublication).setHeader("Publication");
        grid.addColumn(d -> d.getDatePublication() != null ? String.valueOf(d.getDatePublication().getYear()) : "")
        .setHeader("Publication");

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
            documents = documentService.getByTitreContainingIgnoreCase(filterText)
            grid.setItems(documents);
        } else {
            documents = documentService.getAllDocuments();
            grid.setItems(documents);
        }
    }

    // Helper method
    private VerticalLayout createStatCard(String label, Span valueSpan, int value) {
        var labelSpan = new Span(label);
        labelSpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);

        var textValueSpan = new Span(Integer.toString(value));
        valueSpan.addClassNames(FontSize.XXLARGE, FontWeight.SEMIBOLD);

        valueSpan.addClassNames(FontSize.XXLARGE, FontWeight.SEMIBOLD);

        var card = new VerticalLayout(labelSpan, valueSpan, textValueSpan);
        card.addClassNames(
            Background.BASE,
            BorderRadius.LARGE,
            Padding.MEDIUM
        );
        card.setSpacing(false);
        card.getStyle().set("border", "1px solid var(--lumo-contrast-10pct)");
        //card.setWidth("140px");
        return card;
    }

}