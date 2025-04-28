package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.frontend.Page;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;

public class ErrorView extends CustomComponent implements View {

    @Inject
    private CustomAccessControl customAccessControl;

    @Inject
    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    @Override
    public void enter(ViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        layout.addComponent(new Label("Unfortunately, the page you have requested does not exist"));
        if (customAccessControl.isUserSignedIn()){
            layout.addComponent(createHomebutton());
        } else {
            layout.addComponent(createLoginButton());
        }
        setCompositionRoot(layout);
    }

    private Button createLoginButton(){
        Button button = new Button("To login page");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                navigationEvent.fire(new NavigationEvent(Page.LOGIN));
            }
        });
        return button;
    }

    private Button createHomebutton(){
        Button button = new Button("Back to the main page");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                navigationEvent.fire(new NavigationEvent(Page.MAIN));
            }
        });
        return button;
    }

}
