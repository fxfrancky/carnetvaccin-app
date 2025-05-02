package com.carnetvaccin.app.frontend.security;

import com.vaadin.server.ServiceInitEvent;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class MyServiceInitListener implements com.vaadin.server.VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(new MySessionInitListener());
    }
}
