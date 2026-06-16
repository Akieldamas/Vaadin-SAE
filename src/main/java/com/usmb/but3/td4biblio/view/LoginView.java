package com.usmb.but3.td4biblio.view;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout {

    public static Utilisateur utilisateur;
    public LoginView(UtilisateurRepo utilisateurRepo) {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        LoginForm loginForm = new LoginForm();

        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();
            utilisateur = utilisateurRepo.getUtilisateurByLoginAndMotDePasse(username, password);
            // Simple demo authentication
            if (utilisateur!=null) {
                this.getUI().ifPresent(ui -> ui.navigate("accueil"));
            } else {
                loginForm.setError(true);
            }
        });

        add(loginForm);
    }
}