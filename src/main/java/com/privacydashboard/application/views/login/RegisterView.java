package com.privacydashboard.application.views.login;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Route("registration")
@PageTitle("Registration")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final TextField username=new TextField("USERNAME");
    private final PasswordField password=new PasswordField("PASSWORD");
    private final PasswordField confirmPassword=new PasswordField("CONFIRM PASSWORD");
    private final ComboBox<Role> role= new ComboBox<>("ROLE");
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final Binder<User> binder= new Binder<>(User.class);

    public RegisterView(UserDetailsServiceImpl userDetailsServiceImpl){
        this.userDetailsServiceImpl=userDetailsServiceImpl;
        addClassName("registration");
        setAlignItems(Alignment.CENTER);
        implementBinder();
        Button registerButton= new Button("Register", e-> confirm());
        add(new H1("Registration"), username, password, confirmPassword, role, registerButton);
    }

    private void implementBinder(){
        // implements dataRole ComboBox
        List<Role> roleList=new LinkedList<>();
        roleList.add(Role.CONTROLLER);
        roleList.add(Role.SUBJECT);
        roleList.add(Role.DPO);
        role.setItems(roleList);

        // bind User and form
        password.setMinLength(8);
        password.setErrorMessage("the password must be at least 8 characters");
        binder.forField(username).withValidator(name -> name.length()>=5, "name must be at least 5 characters")
                .withValidator(this::isUniqueName, "username already in use, please use another one")
                .bind(User::getName, User::setName);
        binder.forField(confirmPassword).withValidator(pass-> pass.length()>=8, "the password must be at least 8 characters")
                .withValidator(pass -> pass.equals(password.getValue()), "the two passwords must be equals")
                .bind(User::getHashedPassword, User::setHashedPassword);
        binder.forField(role).withValidator(Objects::nonNull, "please select a role").bind(User::getRole, User::setRole);
    }

    private boolean isUniqueName(String name){
        return !userDetailsServiceImpl.isAlreadyPresent(name);
    }

    private void confirm(){
        User user=new User();
        try{
            binder.writeBean(user);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        userDetailsServiceImpl.hashPassAndAddUser(user);   // create user and hash the password (better do the hash in a different layer(not the one visible to the user)??)
        UI.getCurrent().getPage().setLocation("/"); // reinderizza alla pagina login
    }
}
