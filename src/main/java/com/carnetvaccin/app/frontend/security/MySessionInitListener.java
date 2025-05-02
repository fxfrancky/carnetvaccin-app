package com.carnetvaccin.app.frontend.security;

import com.carnetvaccin.app.MainUI;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MySessionInitListener implements SessionInitListener {

    @Override
    public void sessionInit(SessionInitEvent event) {
        event.getSession().addUIProvider(new UIProvider() {
            @Override
            public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
                return MainUI.class; // Replace MyUI.class with your actual UI class.
            }
        });
    }
}