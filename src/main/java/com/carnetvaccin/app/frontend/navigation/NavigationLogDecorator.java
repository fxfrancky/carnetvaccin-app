package com.carnetvaccin.app.frontend.navigation;

import com.carnetvaccin.app.frontend.utilisateur.UserInfo;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.logging.Logger;

@Decorator
public class NavigationLogDecorator implements NavigationService {

    @Inject
    @Delegate
    @Any
    NavigationService delegate;

    @Inject
    private UserInfo userInfo;

    @Override
    public void onNavigationEvent(NavigationEvent event) {
        getLogger().info(
                userInfo.getName() + " navigated to " + event.getNavigateTo());
        delegate.onNavigationEvent(event);
    }

    private Logger getLogger() {
        return Logger.getLogger(this.getClass().getSimpleName());
    }

}