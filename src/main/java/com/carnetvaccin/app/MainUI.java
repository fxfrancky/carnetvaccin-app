package com.carnetvaccin.app;

import com.carnetvaccin.app.frontend.HomeView;
import com.carnetvaccin.app.frontend.security.ErrorView;
import com.carnetvaccin.app.frontend.security.LoginView;
import com.carnetvaccin.app.frontend.security.RegistrationView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import javax.inject.Inject;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@CDIUI(MainUI.MAIN)
@Push
@PreserveOnRefresh
public class MainUI extends UI {

    public static final String MAIN = "";

    @Inject
    private CDIViewProvider viewProvider;
//    @Inject
//    @Named
//    private CDIViewProvider viewProvider;

//    @Inject
//    private UI ui;

//    @Inject
//    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;

    @Override
    protected void init(VaadinRequest request) {
        //Initialize your views here
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.addView(LoginView.NAME, LoginView.class);
        navigator.addView(HomeView.NAME, HomeView.class);
        navigator.addView(RegistrationView.NAME, RegistrationView.class);
        navigator.setErrorView(ErrorView.class);
        navigator.navigateTo(LoginView.NAME);

//        navigationEvent.fire(new NavigationEvent(LoginView.LOGIN));
    }

}
