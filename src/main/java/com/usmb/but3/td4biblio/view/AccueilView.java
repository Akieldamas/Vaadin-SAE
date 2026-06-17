package com.usmb.but3.td4biblio.view;

import java.util.ArrayList;

import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.repository.UtilisateurRepo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Accueil")
// Force le retrait du menu latéral si tu as le même problème qu'avant
@Route(value = "accueil", layout = com.vaadin.flow.component.UI.class)
public class AccueilView extends VerticalLayout {

    public static Utilisateur utilisateur;

    public AccueilView(UtilisateurRepo utilisateurRepo) {
        
        setSizeFull();
        setPadding(false); // On gère le padding intérieurement
        setMargin(false);

        // --- EN-TÊTE ---
        HeaderAccueilView header = new HeaderAccueilView();
        header.getStyle().set("border-bottom", "2px solid var(--lumo-primary-color)");
        add(header);

        // --- CONTENU PRINCIPAL ---
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setWidth("100%");
        mainContent.setMaxWidth("1200px"); // Limite la largeur sur les grands écrans
        mainContent.setAlignItems(Alignment.CENTER);
        mainContent.getStyle().set("margin", "0 auto"); // Centre horizontalement
        mainContent.getStyle().set("padding", "40px 20px");

        // Titre
        H1 title = new H1("BIBLIO Vaadin, votre espace bibliothèque !");
        title.getStyle().set("font-size", "36px");
        title.getStyle().set("font-weight", "bold");
        title.getStyle().set("color", "var(--lumo-header-text-color)");
        title.getStyle().set("text-align", "center");
        title.getStyle().set("margin-bottom", "10px");

        // Paragraphe d'introduction
        Paragraph introduction = new Paragraph(
                "Bienvenue sur votre nouvel espace de gestion et de consultation. " +
                "Notre logiciel permet aux bibliothécaires d'administrer le catalogue " +
                "et aux lecteurs de consulter les nouveautés ou de suivre leurs emprunts en toute simplicité."
        );
        introduction.getStyle().set("font-size", "18px");
        introduction.getStyle().set("color", "var(--lumo-secondary-text-color)");
        introduction.getStyle().set("text-align", "center");
        introduction.getStyle().set("max-width", "800px");
        introduction.getStyle().set("line-height", "1.6");
        introduction.getStyle().set("margin-bottom", "40px");

        // Boutons d'actions rapides (Si connecté)
        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.setSpacing(true);
        actionsLayout.getStyle().set("margin-bottom", "50px");

        if (LoginView.utilisateur != null) {
            // Bouton Nouveautés (Pour tout le monde)
            Button accessDocs = new Button("Consulter le catalogue", VaadinIcon.BOOK.create());
            accessDocs.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
            accessDocs.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate("accueil/document")));
            actionsLayout.add(accessDocs);

            // Bouton Gestion (Uniquement Bibliothécaire)
            if (LoginView.utilisateur.getRoleUtilisateur().getId() == 1) {
                Button accessApp = new Button("Gérer l'application", VaadinIcon.COG.create());
                accessApp.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_LARGE);
                accessApp.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate("auteur")));
                actionsLayout.add(accessApp);
            }
            
            // Bouton Emprunts (Uniquement Lecteur)
            if (LoginView.utilisateur.getRoleUtilisateur().getId() == 2) {
                Button mesEmprunts = new Button("Mes emprunts en cours", VaadinIcon.NOTEBOOK.create());
                mesEmprunts.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_LARGE);
                mesEmprunts.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate("accueil/mes-emprunts")));
                actionsLayout.add(mesEmprunts);
            }
        } else {
            // Appel à l'action pour les visiteurs
            Button loginBtn = new Button("Connectez-vous pour commencer", VaadinIcon.SIGN_IN.create());
            loginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
            loginBtn.addClickListener(e -> this.getUI().ifPresent(ui -> ui.navigate("login")));
            actionsLayout.add(loginBtn);
        }

        // --- GALERIE D'IMAGES ---
        HorizontalLayout galleryLayout = new HorizontalLayout();
        galleryLayout.setWidthFull();
        galleryLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        galleryLayout.setSpacing(true);
        // Responsive : les images passent à la ligne sur petit écran
        galleryLayout.getStyle().set("flex-wrap", "wrap"); 

        Image illustration1 = createImage("images/bibliothecaire1.jpg", "Illustration 1");
        Image illustration2 = createImage("images/bibliothecaire2.jpg", "Illustration 2");
        Image illustration3 = createImage("images/bibliothecaire3.jpg", "Illustration 3");

        galleryLayout.add(illustration1, illustration2, illustration3);

        // Assemblage final
        mainContent.add(title, introduction, actionsLayout, galleryLayout);
        add(mainContent);
    }

    // Méthode utilitaire pour appliquer le même style à toutes les images
    private Image createImage(String src, String alt) {
        Image img = new Image(src, alt);
        img.setWidth("300px"); // Taille fixe pour garder de l'homogénéité
        img.setHeight("200px");
        img.getStyle().set("object-fit", "cover"); // L'image remplit la case sans se déformer
        img.getStyle().set("border-radius", "15px");
        img.getStyle().set("box-shadow", "0 10px 20px rgba(0,0,0,0.1)");
        img.getStyle().set("transition", "transform 0.3s ease");
        img.getStyle().set("margin", "10px");
        
        // Petit effet au survol
        img.getElement().addEventListener("mouseenter", e -> img.getStyle().set("transform", "scale(1.05)"));
        img.getElement().addEventListener("mouseleave", e -> img.getStyle().set("transform", "scale(1)"));
        
        return img;
    }
}