package com.usmb.but3.td4biblio.view;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;

import com.nimbusds.jose.Header;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Register")
@Route(value = "register")
public class RegisterView extends Main {

    public UtilisateurRepo utilisateurRepo;
    public RegisterView(UtilisateurRepo utilisateurRepo) {
        HeaderView header = new HeaderView(); 
        add(header);
        this.utilisateurRepo=utilisateurRepo;
        add(initContent());
       
    }
    /*
    
    private String login;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    private String prenom;
    private String nom;
    private String adresse;
    private String email;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_fin_abonnement")
    private LocalDate dateFinAbonnement;

    @Column(name = "numero_carte", length = 10)
    private String numeroCarte;

    @Column(name = "duree_emprunt_semaines")
    private Integer dureeEmpruntMax;

    @Column(name = "max_emprunts")
    private Integer maxEmprunts;

    */
    protected Component initContent() {
        TextField login = new TextField("Login");
        TextField nom = new TextField("Nom");
        TextField prenom = new TextField("Prenom");
        EmailField email = new EmailField("Email");
        DatePicker dateNaissance = new DatePicker("Date de naissance");

        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        return new VerticalLayout(
                new H2("Register"),
                new HorizontalLayout(login, nom, prenom),
                new HorizontalLayout(email, dateNaissance),
                new HorizontalLayout(password1, password2),
                new Button("Register", event -> register(
                        login.getValue(),
                        nom.getValue(),
                        prenom.getValue(),

                        password1.getValue(),
                        password2.getValue(),
                        email.getValue(),
                        dateNaissance.getValue()
                ))
        );
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
            //utilisateur.saveUtilisateur(new Utilisateur(login, mot_de_passe, nom, prenom, adresse));
            Notification.show("Check your email.");
        }
    }
}