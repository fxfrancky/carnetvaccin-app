package com.carnetvaccin.app.frontend.security;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

//@ApplicationScoped
@CDIView(ErrorView.NAME)
public class ErrorView extends VerticalLayout implements View {

    public static final String NAME = "error";


//    public static final String NAME = "error";

    public ErrorView() {
        Label errorLabel = new Label("An unexpected error occurred.");
        errorLabel.setStyleName(ValoTheme.LABEL_FAILURE);
        addComponent(errorLabel);
        setSizeFull();
//        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

    }
//    @Inject
////    @Named("customAccessControl")
//    private AccessControl accessControl;
//
////    @Inject
////    private javax.enterprise.event.Event<NavigationEvent> navigationEvent;
//
//    @Override
//    public void enter(ViewChangeEvent event) {
////        if (!accessControl.isUserSignedIn()) {
////            Notification.show("Cannot Render the HomeView. User is not Signed In", Notification.Type.ERROR_MESSAGE);
////            navigationEvent.fire(new NavigationEvent(LoginView.LOGIN));
////            return;
////        }
//        VerticalLayout layout = new VerticalLayout();
//        layout.setSizeFull();
//
//        // event.getParameters() contains the unmatched part of the URL.
//        String parameters = event.getParameters();
//        Label errorLabel = new Label("Unfortunately, the page you have requested does not exist:  " + parameters);
//        layout.addComponent(errorLabel);
//        layout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);
//        Button loginButton = createLoginButton();
//        Button homeButton = createHomebutton();
//        if (((CustomAccessControl)accessControl).isUserSignedIn()){
//            layout.addComponent(homeButton);
//            layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
//        } else {
//            layout.addComponent(loginButton);
//            layout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
//        }
//        setCompositionRoot(layout);
//    }
//
//    private Button createLoginButton(){
//        Button button = new Button("To login page");
//        button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//        button.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                getUI().getNavigator().navigateTo(LoginView.NAME);
//            }
//        });
//        return button;
//    }
//
//    private Button createHomebutton(){
//        Button button = new Button("Back to the main page");
//        button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//        button.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                getUI().getNavigator().navigateTo(HomeView.NAME);
//            }
//        });
//        return button;
//    }

}
