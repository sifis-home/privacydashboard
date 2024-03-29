package com.privacydashboard.application.security;

import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AuthenticatedUser {
    private final UserRepository userRepository;
    @Autowired
    public AuthenticatedUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }



    public Optional<User> get() {
        return getAuthentication().map(authentication -> userRepository.findByName(authentication.getName()));
    }

    // AGGIUNTA, RESTITUISCE USER SENZA DOVER OGNI VOLTA GUARDARE IL DB. E' SICURO??????
    public User getUser(){
        if(UI.getCurrent().getSession().getAttribute("user")==null){
            Optional<User> maybeUser=get();
            if (maybeUser == null) {
                maybeUser = getAuthentication().map(authentication -> userRepository.findByName(authentication.getName()));
            }
            maybeUser.ifPresent(user -> UI.getCurrent().getSession().setAttribute("user", user));
        }
        return (User) UI.getCurrent().getSession().getAttribute("user");



    }

    public User updateUser(){
        Optional<User> maybeUser=get();
        maybeUser.ifPresent(user -> UI.getCurrent().getSession().setAttribute("user", user));
        return (User) UI.getCurrent().getSession().getAttribute("user");
    }


    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}
