package com.usmb.but3.td4biblio.view;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.cglib.core.Local;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.usmb.but3.td4biblio.service.UtilisateurService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Register")
@Route(value = "register")
public class RegisterView extends VerticalLayout {
    private TextField login = new TextField("Login");
    private TextField nom = new TextField("Nom");
    private TextField prenom = new TextField("Prenom");
    private EmailField email = new EmailField("Email");
    private DatePicker dateNaissance = new DatePicker("Date de naissance");

    private PasswordField password1 = new PasswordField("Password");
    private PasswordField password2 = new PasswordField("Confirm password");
	Binder<Utilisateur> binder = new Binder<>(Utilisateur.class);
    private Utilisateur utilisateur = new Utilisateur();
    public UtilisateurService utilisateurRepo;
    public RegisterView(UtilisateurService utilisateurRepo) {
        this.utilisateurRepo=utilisateurRepo;
        add(new HeaderAccueilView());
        add(initContent());

    }
    protected Component initContent() {

        Button save = new Button("S'enregistrer");
		// bind using naming convention
		binder.bindInstanceFields(this);
		binder.setBean(utilisateur);

		// Configure and style components
		setSpacing(true);

		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


		// wire action buttons to save, delete and reset
		save.addClickListener(e -> register(
            login.getValue(),
            nom.getValue(),
            prenom.getValue(),

            password1.getValue(),
            password2.getValue(),
            email.getValue(),
            dateNaissance.getValue()
        ));

        binder.forField(login)
        .asRequired("Login obligatoire")
        .bind(Utilisateur::getLogin, Utilisateur::setLogin);
    
        binder.forField(nom)
            .asRequired("Nom obligatoire")
            .bind(Utilisateur::getNom, Utilisateur::setNom);
        
        binder.forField(prenom)
            .asRequired("Prénom obligatoire")
            .bind(Utilisateur::getPrenom, Utilisateur::setPrenom);
        
        binder.forField(email)
            .asRequired("Email obligatoire")
            .bind(Utilisateur::getEmail, Utilisateur::setEmail);
        
        binder.forField(dateNaissance)
            .bind(Utilisateur::getDateNaissance, Utilisateur::setDateNaissance);
        
        binder.forField(password1)
            .asRequired("Mot de passe obligatoire")
            .bind(Utilisateur::getMotDePasse, Utilisateur::setMotDePasse);
        
        binder.forField(password2)
            .asRequired("Confirmation obligatoire")
            .withValidator(pass2 -> pass2.equals(password1.getValue()),
                "Les mots de passe ne correspondent pas")
            .bind(u -> "", (u, v) -> {}); // champ fictif (non stocké)

        VerticalLayout layout = new VerticalLayout(
            new H2("Register"),
            new HorizontalLayout(login, nom, prenom),
            new HorizontalLayout(email, dateNaissance),
            new HorizontalLayout(password1, password2),
            save
            
        );
        layout.getStyle().set("align-items", "center");
        return layout;
    }

    private void register(String login, String nom, String prenom, String password1, String password2, String email,  LocalDate dateNaissance) {
        if (login.trim().isEmpty()) {
            Notification.show("Enter your name");
        } else if (nom.trim().isEmpty()) {
            Notification.show("Enter your last name");
        } else if (email.isEmpty()) {
            Notification.show("Enter your email");
        }else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            utilisateur.setNumeroCarte(generateCardNumber());
            utilisateur.setMaxEmprunts(10);
            utilisateur.setDureeEmpruntMax(5);
            utilisateur.setDateFinAbonnement(LocalDate.now().plusWeeks(5));
            utilisateur.setRoleUtilisateur(new RoleUtilisateur(2,"Emprunteur"));
            utilisateurRepo.saveUtilisateur(utilisateur);
            Notification.show("Compte créé avec succès.");
        }
    }
    public String generateCardNumber(){
        Random rand = new Random();
        String cardNumber="";
        for(int i=0; i<9; i++){
            cardNumber+=Integer.toString(rand.nextInt(9));
        }
        return cardNumber;
    }
}