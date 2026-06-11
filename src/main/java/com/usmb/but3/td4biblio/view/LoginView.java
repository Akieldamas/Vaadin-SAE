package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value = "login", layout = MainLayout.class)
public class LoginView extends VerticalLayout {

    public LoginView() {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        LoginForm loginForm = new LoginForm();

        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            // Simple demo authentication
            if ("admin".equals(username) && "password".equals(password)) {
                getUI().ifPresent(ui -> ui.navigate(""));
            } else {
                loginForm.setError(true);
            }
        });

        add(loginForm);
    }
}