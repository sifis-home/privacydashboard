package com.privacydashboard.application.views.apps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.lang.reflect.Method;

public class AppsViewTest {

    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;

    private static AppsView orangeView;
    private static AppsView redView;
    private static AppsView greenView;

    private static List<IoTApp> createOrangeAppList(){
        String[] consenses = {"Consensus 1", "Consensus 2"};

        IoTApp app1 = new IoTApp();
        app1.setName("App1");
        app1.setQuestionnaireVote(QuestionnaireVote.GREEN);
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        IoTApp app2 = new IoTApp();
        app2.setName("App2");
        app2.setQuestionnaireVote(QuestionnaireVote.RED);
        app2.setDescription("Description2");
        app2.setConsenses(consenses);

        List<IoTApp> list = new ArrayList();
        list.add(app1);
        list.add(app2);

        return list;
    }

    private static List<IoTApp> createGreenAppList(){
        String[] consenses = {"Consensus 1", "Consensus 2"};

        IoTApp app1 = new IoTApp();
        app1.setName("App1");
        app1.setQuestionnaireVote(QuestionnaireVote.GREEN);
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        IoTApp app2 = new IoTApp();
        app2.setName("App2");
        app2.setQuestionnaireVote(QuestionnaireVote.GREEN);
        app2.setDescription("Description2");
        app2.setConsenses(consenses);

        List<IoTApp> list = new ArrayList();
        list.add(app1);
        list.add(app2);

        return list;
    }

    private static List<IoTApp> createRedAppList(){
        String[] consenses = {"Consensus 1", "Consensus 2"};

        IoTApp app1 = new IoTApp();
        app1.setName("App1");
        app1.setQuestionnaireVote(QuestionnaireVote.RED);
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        IoTApp app2 = new IoTApp();
        app2.setName("App2");
        app2.setQuestionnaireVote(QuestionnaireVote.RED);
        app2.setDescription("Description2");
        app2.setConsenses(consenses);

        List<IoTApp> list = new ArrayList<>();
        list.add(app1);
        list.add(app2);

        return list;
    }

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

    private static User getAuthUserNotSubject(){
        User user = new User();

        user.setName("AuthUser");
        user.setId(new UUID(0, 0));
        user.setRole(Role.CONTROLLER);
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
    
    @BeforeAll
    private static void setup(){
        authenticatedUser = mock(AuthenticatedUser.class);
        dataBaseService = mock(DataBaseService.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(getAuthUser());

        when(dataBaseService.getUserApps(any())).thenReturn(createOrangeAppList());
        orangeView = new AppsView(dataBaseService, authenticatedUser, communicationService);

        when(dataBaseService.getUserApps(any())).thenReturn(createGreenAppList());
        greenView = new AppsView(dataBaseService, authenticatedUser, communicationService);

        when(dataBaseService.getUserApps(any())).thenReturn(createRedAppList());
        redView = new AppsView(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void AppsViewConstructorTest(){

        assertEquals(orangeView.getElement().getAttribute("class"), "grid-view");
        assertEquals(orangeView.getElement().getTag(), "div");

        assertEquals(orangeView.getElement().getChild(0).getTag(), "div");
        assertEquals(orangeView.getElement().getChild(0).getAttribute("class"), "orangeName summaryEvaluation");

        assertEquals(orangeView.getElement().getChild(0).getChild(0).getTag(), "span");
        assertEquals(orangeView.getElement().getChild(0).getChild(0).getAttribute("class"), "bold");
        assertEquals(orangeView.getElement().getChild(0).getChild(0).getText(), "Privacy Infrastructure Evaluation: ");
        assertEquals(orangeView.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(orangeView.getElement().getChild(0).getChild(1).getText(), "ORANGE");

        assertEquals(orangeView.getElement().getChild(1).getTag(), "vaadin-text-field");
        assertEquals(orangeView.getElement().getChild(1).getAttribute("class"), "search");

        assertEquals(orangeView.getElement().getChild(2).getTag(), "vaadin-grid");
        assertEquals(orangeView.getElement().getChild(2).getThemeList().toString(), "[no-row-borders, no-border]");
        assertEquals(orangeView.getElement().getChild(2).getChild(0).getTag(), "vaadin-grid-column");

    }

    @Test
    public void redAppViewTest(){

        assertEquals(redView.getElement().getChild(0).getChild(1).getText(), "RED");
    }

    @Test
    public void greenAppViewTest(){

        assertEquals(greenView.getElement().getChild(0).getChild(1).getText(), "GREEN");
    }

    private static Method getCreateAppMethod(){
        try{
            Method method = AppsView.class.getDeclaredMethod("createApp", IoTApp.class);
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
            app = (VerticalLayout) createApp.invoke(orangeView, (Object) createOrangeAppList().get(0));
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

    @Test
    public void createAppRedTest(){
        Method createApp = getCreateAppMethod();
        VerticalLayout app = new VerticalLayout();

        when(authenticatedUser.getUser()).thenReturn(getAuthUser());

        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(getUserRole(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(getUserRole(Role.CONTROLLER));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(getUserRole(Role.DPO));

        try{
            app = (VerticalLayout) createApp.invoke(orangeView, (Object) createOrangeAppList().get(1));
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getAttribute("class"), "bold");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getText(), "Evaluation: ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getAttribute("class"), "redName");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "RED ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getAttribute("class"), "las la-info-circle pointer");
    }

    @Test
    public void createAppOrangeTest(){
        Method createApp = getCreateAppMethod();
        VerticalLayout app = new VerticalLayout();

        IoTApp app1 = new IoTApp();
        String[] consenses = {"Consensus 1", "Consensus 2"};
        app1.setName("App1");
        app1.setQuestionnaireVote(QuestionnaireVote.ORANGE);
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        when(authenticatedUser.getUser()).thenReturn(getAuthUser());

        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(getUserRole(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(getUserRole(Role.CONTROLLER));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(getUserRole(Role.DPO));

        try{
            app = (VerticalLayout) createApp.invoke(orangeView, (Object) app1);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getAttribute("class"), "bold");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getText(), "Evaluation: ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getAttribute("class"), "orangeName");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "ORANGE ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getAttribute("class"), "las la-info-circle pointer");
    }

    @Test
    public void createAppNullVoteTest(){
        Method createApp = getCreateAppMethod();
        VerticalLayout app = new VerticalLayout();

        IoTApp app1 = new IoTApp();
        String[] consenses = {"Consensus 1", "Consensus 2"};
        app1.setName("App1");
        app1.setDescription("Description1");
        app1.setConsenses(consenses);

        when(authenticatedUser.getUser()).thenReturn(getAuthUser());

        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(getUserRole(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(getUserRole(Role.CONTROLLER));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(getUserRole(Role.DPO));

        try{
            app = (VerticalLayout) createApp.invoke(orangeView, (Object) app1);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getAttribute("class"), "bold");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getText(), "Evaluation: ");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getAttribute("class"), "redName");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "NO EVALUATION YET");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(2).getAttribute("class"), "las la-info-circle pointer");
    }

    @Test
    public void createAppNotASubjectAuthTest(){
        Method createApp = getCreateAppMethod();
        VerticalLayout app = new VerticalLayout();

        when(authenticatedUser.getUser()).thenReturn(getAuthUserNotSubject());

        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(getUserRole(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(getUserRole(Role.CONTROLLER));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(getUserRole(Role.DPO));

        try{
            app = (VerticalLayout) createApp.invoke(orangeView, (Object) createOrangeAppList().get(0));
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(1).getTag(), "span");
        assertEquals(app.getElement().getChild(1).getChild(0).getChild(0).getChild(5).getChild(1).getText(), "Data Subjects: ");
    }

}
