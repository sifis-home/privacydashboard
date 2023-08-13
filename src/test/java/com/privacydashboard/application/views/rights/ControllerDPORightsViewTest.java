package com.privacydashboard.application.views.rights;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.GlobalVariables.Role;

import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;

public class ControllerDPORightsViewTest {
    
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;

    private static ControllerDPORightsView view;

    private static User getUser(){
        User user = new User();

        user.setName("TestUser");
        user.setRole(Role.SUBJECT);

        return user;
    }

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(getUser());

        view = new ControllerDPORightsView(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void controllerDPOConstructorTest(){

        assertEquals(view.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(view.getElement().getAttribute("class"), "rights-view");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(0).getThemeList().toString(), "[no-row-borders, no-border]");

        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-grid-column");
    }

}
