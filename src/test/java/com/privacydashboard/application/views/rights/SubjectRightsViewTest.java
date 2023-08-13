package com.privacydashboard.application.views.rights;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import java.time.LocalDateTime;
import java.util.UUID;
import com.privacydashboard.application.data.entity.RightRequest;

import com.vaadin.flow.component.grid.Grid;

public class SubjectRightsViewTest {
    
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;

    private static SubjectRightsView view;


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

        view = new SubjectRightsView(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void subjectRightsViewConstructorTest(){

        assertEquals(view.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(view.getElement().getAttribute("class"), "rights-view");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(0).getChild(0).getAttribute("class"), "buuutton");
        assertEquals(view.getElement().getChild(0).getChild(0).getText(), "Pending requests");

        assertEquals(view.getElement().getChild(0).getChild(1).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(0).getChild(1).getAttribute("class"), "buuutton");
        assertEquals(view.getElement().getChild(0).getChild(1).getText(), "Handled requests");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(1).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(1).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(1).getChild(0).getText(), "Access data");

        assertEquals(view.getElement().getChild(2).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(2).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(2).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(2).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(2).getChild(0).getText(), "Withdraw a consent");

        assertEquals(view.getElement().getChild(3).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(3).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(3).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(3).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(3).getChild(0).getText(), "Ask information");

        assertEquals(view.getElement().getChild(4).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(4).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(4).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(4).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(4).getChild(0).getText(), "Compile a complain");

        assertEquals(view.getElement().getChild(5).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(5).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(5).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(5).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(5).getChild(0).getText(), "Erase data");

        assertEquals(view.getElement().getChild(6).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(6).getAttribute("class"), "card canOpen");
        assertEquals(view.getElement().getChild(6).getChild(0).getTag(), "span");
        assertEquals(view.getElement().getChild(6).getChild(0).getAttribute("class"), "name");
        assertEquals(view.getElement().getChild(6).getChild(0).getText(), "Remove everything");
    }
}
