package com.adonis.ui.login;

import com.adonis.data.service.PersonService;
import com.adonis.ui.MainUI;
import com.adonis.ui.main.MainScreen;
import com.adonis.ui.persons.RegistrationUI;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by oksdud on 03.04.2017.
 */
@Theme("mytheme")
//@SpringView(name = LoginView.NAME)
public class LoginView extends CssLayout implements View {

    private HorizontalLayout loginFormLayout;
    protected LoginForm loginForm;
    public static final String NAME =  "LoginView";
    PersonService service;

    public LoginView(PersonService personService, LoginListener loginListener){
        setSizeFull();
        this.service = personService;
        addStyleName("login-screen");
        loginFormLayout = new HorizontalLayout();
        loginFormLayout.setSizeFull();
        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.setSizeFull();
        centeringLayout.addStyleName(ValoTheme.PANEL_WELL);

        loginForm = new LoginForm();
        loginForm.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        loginForm.setPrimaryStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        updateCaption();
        loginForm.setSizeFull();
        loginForm.addLoginListener(new LoginForm.LoginListener() {
            @Override
            public void onLogin(LoginForm.LoginEvent event) {
                login((LoginForm)event.getSource(), event.getLoginParameter("username"), event.getLoginParameter("password"));
            }
        });
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);
        loginFormLayout.addComponent(centeringLayout);
        loginFormLayout.setComponentAlignment(centeringLayout, Alignment.MIDDLE_CENTER);
        addComponent(loginFormLayout);

    }
    protected void updateCaption() {
        float width = loginForm.getWidth();
        float height = loginForm.getHeight();

        String w = width < 0 ? "auto" : (int) width + "px";
        String h = height < 0 ? "auto" : (int) height + "px";
        loginForm.setSizeFull();
        loginForm.setCaption("Enter your login and password here");
    }
    private void login(LoginForm form, String user, String password){

        if((MainUI.loginPerson = service.findByCustomerLogin(user))!=null && service.findByCustomerLogin(user).getPassword().equals(password)){
            addStyleName(ValoTheme.UI_WITH_MENU);
            getUI().getNavigator().navigateTo(MainScreen.NAME);
            return;
        }else {
            addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
            getUI().getNavigator().navigateTo(RegistrationUI.NAME);
            return;
        }
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    @Override
    public void forEach(Consumer<? super Component> action) {

    }

    @Override
    public Spliterator<Component> spliterator() {
        return null;
    }
    public interface LoginListener extends Serializable {
        void loginSuccessful();
    }

}
