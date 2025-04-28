package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.frontend.HomeView;
import com.carnetvaccin.app.frontend.navigation.NavigationEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

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

        // event.getParameters() contains the unmatched part of the URL.
        String parameters = event.getParameters();
        Label errorLabel = new Label("Unfortunately, the page you have requested does not exist:  " + parameters);
        layout.addComponent(errorLabel);
        layout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
        Button loginButton = createLoginButton();
        Button homeButton = createHomebutton();
        if (customAccessControl.isUserSignedIn()){
            layout.addComponent(homeButton);
            layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
        } else {
            layout.addComponent(loginButton);
            layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        }
        setCompositionRoot(layout);
    }

    private Button createLoginButton(){
        Button button = new Button("To login page");
        button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                navigationEvent.fire(new NavigationEvent(LoginView.LOGIN));
            }
        });
        return button;
    }

    private Button createHomebutton(){
        Button button = new Button("Back to the main page");
        button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                navigationEvent.fire(new NavigationEvent(HomeView.HOME));
            }
        });
        return button;
    }

}
