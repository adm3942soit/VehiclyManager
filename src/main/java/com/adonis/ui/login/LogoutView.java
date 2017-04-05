package com.adonis.ui.login;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by oksdud on 05.04.2017.
 */
public class LogoutView implements View{

    public LogoutView() {
        /*build logout*/
        VerticalLayout infoLayout = new VerticalLayout();

        Button logoutButton = new Button("Log out", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Button b = event.getButton();
//                loginFormLayout.replaceComponent(b.getParent(),
//                        (LoginForm) b.getData());
            }

        });

        infoLayout.addComponent(logoutButton);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
