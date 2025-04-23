package com.carnetvaccin.app.frontend.navigation;

public class NavigationEvent {

    private final String navigateTo;

    public NavigationEvent(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getNavigateTo() {
        return navigateTo;
    }
}
