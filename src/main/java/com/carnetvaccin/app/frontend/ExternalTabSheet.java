package com.carnetvaccin.app.frontend;

import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class ExternalTabSheet extends TabSheet {

    public ExternalTabSheet() {
        // Set size and configure TabSheet
        setSizeFull();

        // Add tabs
        addTab(createTab1Content(), "Carnet de vaccination");
        addTab(createTab2Content(), "Mon profil");
    }

    private VerticalLayout createTab1Content() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("This is Tab 1 content."));
        return layout;
    }

    private VerticalLayout createTab2Content() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("This is Tab 2 content."));
        return layout;
    }
}