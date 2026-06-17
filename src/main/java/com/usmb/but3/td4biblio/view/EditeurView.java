package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;
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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// MS added for UI unit test : @Component and @Scope("prototype") are needed for the view to be instantiated correctly
@Component
@Scope("prototype")
@Route (value="editeur") 
@PageTitle("Les Editeurs")
@Menu(title = "Les Editeurs", order = 0, icon = "vaadin:office")
public class EditeurView extends VerticalLayout  implements BeforeEnterObserver {

	private final EditeurService editeurService;

	final Grid<Editeur> grid;

	final TextField filter;

	private final Button addNewBtn;

	public Button getAddNewBtn() {
		return addNewBtn;
	}

	final EditeurEditor editor;
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (LoginView.utilisateur==null) {
            event.rerouteTo("login"); // redirect to login page
        } else if(LoginView.utilisateur.getRoleUtilisateur().getId()!=1){
            event.rerouteTo("erreur/permission");

        }
    }
	public EditeurView(EditeurService editeurService, EditeurEditor editor) {
		if (LoginView.utilisateur==null) {
			this.getUI().ifPresent(ui -> ui.navigate("/login"));
		} 
		this.editeurService = editeurService;
		this.editor = editor;
		this.grid = new Grid<>(Editeur.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Ajouter un editeur", VaadinIcon.PLUS.create());
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		add(actions, grid, editor);

		grid.setHeight("300px");
		grid.setColumns("id", "nom","adresse", "lienSiteWeb", "lienWikipedia");
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
		
		filter.setPlaceholder("Filtrer par nom");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listEditeurs(e.getValue()));

		// Connect selected Customer to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editEditeur(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editEditeur(new Editeur(null, "", "", "","")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listEditeurs(filter.getValue());
		});

		// Initialize listing
		listEditeurs(null);
	}

	// tag::listEditeurs[]
	void listEditeurs(String filterText) {
		if (StringUtils.hasText(filterText)) {
			grid.setItems(editeurService.getByNomContainingIgnoreCase(filterText));
		} else {
			grid.setItems(editeurService.getAllEditeurs());
		}
	}
	// end::listCustomers[]

}
