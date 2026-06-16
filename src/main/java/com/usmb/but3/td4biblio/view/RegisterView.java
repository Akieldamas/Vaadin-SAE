package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.PasswordField;
@PageTitle("Register")
@Route(value = "register")
public class RegisterView extends VerticalLayout {

    public static Utilisateur utilisateur;
    public RegisterView(UtilisateurRepo utilisateurRepo) {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        H3 title = new H3("Signup form");
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        EmailField email = new EmailField("Email");

        Checkbox allowMarketing = new Checkbox("Allow Marketing Emails?");
        allowMarketing.getStyle().set("margin-top", "10px");

        PasswordField password = new PasswordField("Password");
        PasswordField passwordConfirm = new PasswordField("Confirm password");


        Button submitButton = new Button("S'enregistrer");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(title, firstName, lastName, email, password,
                passwordConfirm, allowMarketing,
                submitButton);

        // Max width of the Form
        setMaxWidth("500px");
        
        submitButton.addClickListener(event -> {
            String username = email.getValue();
            String passwordText = password.getValue();
            utilisateur = utilisateurRepo.getUtilisateurByLoginAndMotDePasse(username, passwordText);
            // Simple demo authentication
            if (utilisateur!=null) {
                this.getUI().ifPresent(ui -> ui.navigate("accueil"));
            } else {
            }
        });

        add(title, firstName, lastName, email);
    }
}