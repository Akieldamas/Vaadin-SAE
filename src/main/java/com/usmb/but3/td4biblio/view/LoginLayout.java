package com.usmb.but3.td4biblio.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Layout(value = "/login")
public final class LoginLayout extends AppLayout {

    LoginLayout() {
    }

    /*
     * private Component createUserMenu() {
     * // TODO Replace with real user information and actions
     * var avatar = new Avatar("John Smith");
     * avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
     * avatar.addClassNames(Margin.Right.SMALL);
     * avatar.setColorIndex(5);
     * 
     * var userMenu = new MenuBar();
     * userMenu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
     * userMenu.addClassNames(Margin.MEDIUM);
     * 
     * var userMenuItem = userMenu.addItem(avatar);
     * userMenuItem.add("John Smith");
     * userMenuItem.getSubMenu().addItem("View Profile").setEnabled(false);
     * userMenuItem.getSubMenu().addItem("Manage Settings").setEnabled(false);
     * userMenuItem.getSubMenu().addItem("Logout").setEnabled(false);
     * 
     * return userMenu;
     * }
     */

}