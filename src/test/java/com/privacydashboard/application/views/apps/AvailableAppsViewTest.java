package com.privacydashboard.application.views.apps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AvailableAppsViewTest {
    
    private CommunicationService communicationService;
    private AuthenticatedUser authenticatedUser;
    private DataBaseService dataBaseService;

    private AvailableAppsView view;

    private static User getAuthUser(){
        User user = new User();

        user.setName("AuthUser");
        user.setId(new UUID(0, 0));
        user.setRole(Role.SUBJECT);
        user.setMail("auth@mail.test");
        user.setHashedPassword("AuthPassword");
        user.setProfilePictureUrl("https://auth.html");

        return user;
    }

    private static List<User> getUserRole(Role role){
        User user = new User();

        user.setName("User"+role.toString());
        user.setId(new UUID(0, 0));
        user.setRole(role);
        user.setMail(role.toString()+"@mail.test");
        user.setHashedPassword(role.toString()+"Password");
        user.setProfilePictureUrl("https://"+role.toString()+".html");

        List<User> list = new ArrayList<>();
        list.add(user);

        return list;
    }

    private static IoTApp createOrangeApp(){
        String[] consenses = {"Consensus 1", "Consensus 2"};

        IoTApp app1 = new IoTApp();
        app1.setName("App1");
        app1.setQuestionnaireVote(QuestionnaireVote.GREEN);
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        return app1;
    }

    @BeforeEach
    public void setup(){
        authenticatedUser = mock(AuthenticatedUser.class);
        dataBaseService = mock(DataBaseService.class);
        communicationService = mock(CommunicationService.class);

        view = new AvailableAppsView(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void availableAppsConstructorTest(){
        assertEquals(view.getElement().getTag(), "div");
        assertEquals(view.getElement().getAttribute("class"), "grid-view");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-vertical-layout");
    }

    private static Method getCreateAppMethod(){
        try{
            Method method = AvailableAppsView.class.getDeclaredMethod("createApp", IoTApp.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }
    
    @Test
    public void createAppTest(){
        Method createApp = getCreateAppMethod();
        VerticalLayout app = new VerticalLayout();

        when(authenticatedUser.getUser()).thenReturn(getAuthUser());

        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(getUserRole(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(getUserRole(Role.CONTROLLER));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(getUserRole(Role.DPO));

        try{
            app = (VerticalLayout) createApp.invoke(view, (Object) createOrangeApp());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(app.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(app.getElement().getAttribute("class"), "card canOpen");

        assertEquals(app.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(app.getElement().getChild(0).getChild(0).getTag(), "vaadin-avatar");
        assertEquals(app.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(0).getChild(1).getAttribute("class"), "name");
        assertEquals(app.getElement().getChild(0).getChild(1).getText(), "App1");

        assertEquals(app.getElement().getChild(1).getTag(), "vaadin-details");

        assertEquals(app.getElement().getChild(1).getChild(0).getTag(), "div");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getChild(0).getAttribute("class"), "bold");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getChild(0).getText(), "Description:   ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getChild(1).getText(), "Description1");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getAttribute("class"), "bold");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getText(), "Evaluation: ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getAttribute("class"), "greenName");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "GREEN ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getText(), "Privacy Notice");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getAttribute("class"), "link");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getTag(), "vaadin-details");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getTag(), "div");        
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getText(), "Data Controllers: ");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getTag(), "vaadin-details");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(1).getText(), "Data Protection Officer: ");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getTag(), "vaadin-details");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(0).getTag(), "vaadin-horizontal-layout");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(0).getChild(0).getText(), "Consensus 1");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(0).getChild(1).getTag(), "vaadin-button");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(0).getChild(1).getText(), "Accept consent");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(1).getTag(), "vaadin-horizontal-layout");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(1).getChild(0).getText(), "Consensus 2");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "vaadin-button");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "Accept consent");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(1).getText(), "Consenses: ");

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(6).getTag(), "vaadin-button");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(6).getText(), "Remove everything");

        assertEquals(app.getElement().getChild(1).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(1).getText(), "More");
    }


}
