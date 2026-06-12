package com.usmb.but3.td4biblio.view;

import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.context.annotation.Scope;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
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
public class AuteurEditor extends VerticalLayout implements KeyNotifier {

	private final AuteurService auteurService;

	/**
	 * The currently edited auteur
	 */
	private Auteur auteur;
	private String regWikipediaUrl = "((http|https):\\/\\/[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*))|^$"; // match url with http / https or nothing (|a^)
	private String wikipediaErrorMessage = "L'URL wikipedia n'est pas valide.";

	/* Fields to edit properties in Auteur entity */
	TextField prenom = new TextField("Prénom");
	TextField nom = new TextField("Nom");
    DatePicker dateNaissance = new DatePicker("Date de naissance");
    DatePicker dateDeces = new DatePicker("Date de décès");
    ComboBox<String> nationalite = new ComboBox<>("Nationalité");
    {
        nationalite.setItems("Française", "Belge", "Suisse", "Américaine", "Autre");
        nationalite.setPlaceholder("Sélectionner une nationalité");
        nationalite.setClearButtonVisible(true);
        nationalite.setAllowCustomValue(true);
		nationalite.addCustomValueSetListener(event -> {
    		String customValue = event.getDetail();
    		nationalite.setValue(customValue); // Set the custom value as the selected value
		});
    }
	RegexpValidator urlValidator = new RegexpValidator(wikipediaErrorMessage,regWikipediaUrl);
    TextField villeNaissance = new TextField("Ville de naissance");
	TextField lienWikipedia = new TextField("Lien Wikipedia");
	HorizontalLayout fields = new HorizontalLayout(prenom, nom, nationalite, dateNaissance, dateDeces, villeNaissance, lienWikipedia);

	/* Action buttons */
	Button save = new Button("Sauvegarder", VaadinIcon.CHECK.create());
	Button cancel = new Button("Annuler");
	Button delete = new Button("Supprimer", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Auteur> binder = new Binder<>(Auteur.class);
	private ChangeHandler changeHandler;

	public AuteurEditor(AuteurService service) {
		this.auteurService = service;
		add(fields, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editAuteur(auteur));
		binder.forField(prenom)
		.asRequired()
		.bind(Auteur::getPrenom, Auteur::setPrenom);

		binder.forField(nom)
		.asRequired()
		.bind(Auteur::getNom, Auteur::setNom);

		binder.forField(dateNaissance)
		.asRequired()
		.bind(Auteur::getDateNaissance, Auteur::setDateNaissance);

		binder.forField(lienWikipedia)
		.withValidator(urlValidator)
		.bind(Auteur::getLienWikipedia, Auteur::setLienWikipedia);
		setVisible(false);
	}

	void delete() {
		auteurService.deleteAuteurById(auteur.getId());
		changeHandler.onChange();
	}

	void save() {
		if(binder.isValid()){
			auteurService.saveAuteur(auteur);
			changeHandler.onChange();
		} else {
		}

	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editAuteur(Auteur a) {
		if (a == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = a.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			// In a more complex app, you might want to load
			// the entity/DTO with lazy loaded relations for editing
			auteur = auteurService.getAuteurById(a.getId());
		}
		else {
			auteur = a;
		}
		cancel.setVisible(persisted);

		// Bind auteur properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(auteur);

		setVisible(true);

		// Focus first name initially
		prenom.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
