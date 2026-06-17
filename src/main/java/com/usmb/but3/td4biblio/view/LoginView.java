package com.usmb.but3.td4biblio.view;

import org.mindrot.jbcrypt.BCrypt;

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

        add(new HeaderAccueilView());

        LoginForm loginForm = new LoginForm();
        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        vLayout.setAlignItems(Alignment.CENTER);
        loginForm.addLoginListener(event -> {
            
            String username = event.getUsername();
            String password = event.getPassword();
            Utilisateur utilisateurTest = utilisateurRepo.getUtilisateurByLogin(username);
            if(BCrypt.checkpw(password, utilisateurTest.getMotDePasse())){
                utilisateur=utilisateurTest;
            }
            // Simple demo authentication
            if (utilisateur!=null) {
                this.getUI().ifPresent(ui -> ui.navigate("accueil"));
            } else {
                loginForm.setError(true);
            }
        });
        vLayout.add(loginForm);
        add(vLayout);
    }
}