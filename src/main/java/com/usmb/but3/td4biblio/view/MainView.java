package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route
public final class MainView extends Main implements BeforeEnterObserver {

    MainView() {
        addClassName(LumoUtility.Padding.MEDIUM);
        //add(new ViewToolbar("Main"));
        add(new HeaderView());
        add(new Div("Choisissez une option dans le menu à gauche."));
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.forwardTo(LoginView.class);
    }
    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}
