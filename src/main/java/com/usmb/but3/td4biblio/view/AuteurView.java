package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.usmb.but3.td4biblio.service.ImportExportService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// MS added for UI unit test : @Component and @Scope("prototype") are needed for the view to be instantiated correctly
@Component
@Scope("prototype")
@Route (value="auteur") 
@PageTitle("Les Auteurs")
@Menu(title = "Les Auteurs", order = 0, icon = "vaadin:user")
public class AuteurView extends VerticalLayout implements BeforeEnterObserver{

	private final AuteurService auteurService;
	private final ImportExportService importExportService;

	final Grid<Auteur> grid;

	final TextField filter;

	private final Button addNewBtn;
	private final Button exportBtn;
	private final Button downloadTemplateBtn;

	public Button getAddNewBtn() {
		return addNewBtn;
	}

	final AuteurEditor editor;
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur==null) {
            event.rerouteTo("login"); // redirect to login page
        }
    }
	public AuteurView(AuteurService auteurService, AuteurEditor editor, ImportExportService importExportService) {
		this.auteurService = auteurService;
		this.importExportService = importExportService;
		this.editor = editor;
		this.grid = new Grid<>(Auteur.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Ajouter un auteur", VaadinIcon.PLUS.create());
        this.exportBtn = new Button("Export CSV", VaadinIcon.DOWNLOAD.create());  
        this.downloadTemplateBtn = new Button("Télécharger la Template Import CSV", VaadinIcon.FILE.create());
		addNewBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exportBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        downloadTemplateBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);

        exportBtn.addClickListener(e -> {
            String csvContent = importExportService.ExportAuteursToCSV();
            InputStreamFactory factory = () -> new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.ISO_8859_1));
            StreamResource resource = new StreamResource("auteurs.csv", factory);
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("style", "display:none");
            add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
        });

		downloadTemplateBtn.addClickListener(e -> {
            String csvContent = importExportService.DownloadCSVTemplate("auteur");
            InputStreamFactory factory = () -> new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.ISO_8859_1));
            StreamResource resource = new StreamResource("auteur.csv", factory);
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().setAttribute("style", "display:none");
            add(downloadLink);
            downloadLink.getElement().callJsFunction("click");
        });

		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, exportBtn, downloadTemplateBtn);
		add(actions, grid, editor);

		grid.setHeight("300px");
		grid.setColumns("id", "nom", "prenom", "nationalite", "dateNaissance", "dateDeces", "villeNaissance","lienWikipedia");
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
		
		filter.setPlaceholder("Filtrer par nom");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listAuteurs(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editAuteur(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editAuteur(new Auteur(null, "", "", "", null, null,null,null,null)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listAuteurs(filter.getValue());
		});

		// Initialize listing
		listAuteurs(null);
	}

	// tag::listAuteurs[]
	void listAuteurs(String filterText) {
		if (StringUtils.hasText(filterText)) {
			grid.setItems(auteurService.getByNomContainingIgnoreCase(filterText));
		} else {
			grid.setItems(auteurService.getAllAuteurs());
		}
	}
	// end::listCustomers[]

}
