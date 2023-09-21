package com.privacydashboard.application.views.login;

import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private LoginForm loginForm = new LoginForm();

    @Autowired
    public LoginView(UserDetailsServiceImpl userProvider) {
        addClassName("login");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setAction("login");
        add(new H1("Privacy Dashboard"), loginForm, new RouterLink("Register", RegisterView.class));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent){
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")){
            loginForm.setError(true);
        }

    }
}
