package com.usmb.but3.td4biblio.service;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class NotificationService {

    private NotificationService() {
        // prevent instantiation
    }

    public static void showSuccess(String message) {
        show(message, NotificationVariant.LUMO_SUCCESS, VaadinIcon.CHECK_CIRCLE);
    }

    public static void showError(String message) {
        show(message, NotificationVariant.LUMO_ERROR, VaadinIcon.EXCLAMATION_CIRCLE);
    }

    public static void showInfo(String message) {
        show(message, NotificationVariant.LUMO_PRIMARY, VaadinIcon.INFO_CIRCLE);
    }

    private static void show(String message,
                             NotificationVariant variant,
                             VaadinIcon iconType) {

        UI ui = UI.getCurrent();
        if (ui == null) return;

        ui.access(() -> {
            Notification notification = new Notification();
            notification.setDuration(5000);
            notification.setPosition(Notification.Position.BOTTOM_START);
            notification.addThemeVariants(variant);

            Icon icon = iconType.create();

            HorizontalLayout layout =
                new HorizontalLayout(icon, new Text(message));
            layout.setAlignItems(FlexComponent.Alignment.CENTER);

            notification.add(layout);
            notification.open();
        });
    }
}