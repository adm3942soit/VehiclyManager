package com.adonis.ui.main;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.Version;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AboutView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ABOUT";

    public AboutView() {
        CustomLayout aboutContent = new CustomLayout("aboutview");
        aboutContent.setStyleName("about-content");

        // you can add Vaadin components in predefined slots in the custom
        // layout
        aboutContent.addComponent(
                new Label(" This application is using Vaadin "
                        + Version.getFullVersion()), "info");

        setSizeFull();
        setStyleName("about-view");
        addComponent(aboutContent);
        setComponentAlignment(aboutContent, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

}
