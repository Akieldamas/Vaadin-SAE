package com.usmb.but3.td4biblio.view;

import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.service.EditeurService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */
// MS added for UI unit test : @Component and @Scope("prototype") are needed for the view to be instantiated correctly
//@Component
@Scope("prototype")
@SpringComponent
@UIScope
public class EditeurEditor extends VerticalLayout implements KeyNotifier {

	private final EditeurService EditeurService;

	/**
	 * The currently edited Editeur
	 */
	private Editeur Editeur;

	/* Fields to edit properties in Editeur entity */
	TextField adresse = new TextField("Adresse");
	TextField nom = new TextField("Nom");
    TextField lienSiteWeb = new TextField("Lien site web");
	TextField lienWikipedia = new TextField("Lien Wikipedia");
	HorizontalLayout fields = new HorizontalLayout(nom, adresse, lienSiteWeb, lienWikipedia);

	/* Action buttons */
	Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
	Button cancel = new Button("Annuler");
	Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Editeur> binder = new Binder<>(Editeur.class);
	private ChangeHandler changeHandler;

	public EditeurEditor(EditeurService service) {
		this.EditeurService = service;
		add(fields, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		cancel.addThemeVariants(ButtonVariant.LUMO_WARNING);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> setVisible(false));


		setVisible(false);
	}

	void delete() {
		EditeurService.deleteEditeurById(Editeur.getId());
		changeHandler.onChange();
	}

	void save() {
		if(binder.isValid()){
			EditeurService.saveEditeur(Editeur);
			changeHandler.onChange();
		} else {
		}

	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editEditeur(Editeur a) {
		if (a == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = a.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			// In a more complex app, you might want to load
			// the entity/DTO with lazy loaded relations for editing
			Editeur = EditeurService.getEditeurById(a.getId());
		}
		else {
			Editeur = a;
		}
		cancel.setVisible(persisted);

		// Bind Editeur properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(Editeur);

		setVisible(true);

	}
	

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
