package com.carnetvaccin.app.frontend.navigation;

import lombok.Getter;

@Getter
public class NavigationEvent {

    private final String navigateTo;

    public NavigationEvent(String navigateTo) {
        this.navigateTo = navigateTo;
    }

}
