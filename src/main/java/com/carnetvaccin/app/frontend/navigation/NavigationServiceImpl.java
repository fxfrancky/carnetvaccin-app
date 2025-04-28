package com.carnetvaccin.app.frontend.navigation;

import com.carnetvaccin.app.frontend.security.ErrorView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.NormalUIScoped;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@NormalUIScoped
public class NavigationServiceImpl implements NavigationService {

    @Inject
    private CDIViewProvider viewProvider;

    @Inject
    private ErrorView errorView;

    @Inject
    private UI ui;

    @PostConstruct
    public void initialize() {

        if (ui.getNavigator() == null) {
            Navigator navigator = new Navigator(ui, ui);
            navigator.addProvider(viewProvider);
            navigator.setErrorView(errorView);
        }
    }

    @Override
    public void onNavigationEvent(@Observes NavigationEvent event) {
        try {
            ui.getNavigator().navigateTo(event.getNavigateTo());
        } catch (Exception e) {
            ui.getNavigator().setErrorView(ErrorView.class);
//            ui.getNavigator().navigateTo(ErrorView.ERROR);
//            Notification.show("You Requested a strange Page", Notification.Type.ASSISTIVE_NOTIFICATION);
//            throw new RuntimeException(e);
        }
    }

}