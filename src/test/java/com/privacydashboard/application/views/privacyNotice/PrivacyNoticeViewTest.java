package com.privacydashboard.application.views.privacyNotice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Vertical;

public class PrivacyNoticeViewTest {
    
    private DataBaseService dataBaseService;
    private AuthenticatedUser authenticatedUser;
    private CommunicationService communicationService;

    private PrivacyNoticeView view;

    private User createUser(Role role){
        User user = new User();
        user.setId(new UUID(0, role.hashCode()));
        user.setName("User"+role.toString());
        user.setRole(role);

        return user;
    }

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setId(new UUID(1, 0));
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private PrivacyNotice createNotice(){
        PrivacyNotice notice = new PrivacyNotice();
        notice.setId(new UUID(0, 0));
        notice.setText("TestNotice");
        notice.setApp(createApp());

        return notice;
    }

    private Method getCreatePrivacyNoticeCard(){
        try{
            Method method = PrivacyNoticeView.class.getDeclaredMethod("createPrivacyNoticeCard", PrivacyNotice.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }

        return null;
    }

    @Test
    public void privacyNoticeViewConstructorSubjectTest(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(createUser(Role.SUBJECT));

        view = new PrivacyNoticeView(dataBaseService, authenticatedUser, communicationService);

        assertEquals(view.getClassName(), "grid-view");
        assertEquals(view.getElement().getTag(), "div");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-text-field");
        assertEquals(view.getElement().getChild(0).getAttribute("class"), "search");
        assertEquals(view.getElement().getChild(0).getProperty("placeholder"), "Search...");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(1).getThemeList().toString(), "[no-row-borders, no-border]");

        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-grid-column");
    }

    @Test
    public void privacyNoticeViewConstructorNotASubjectTest(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(createUser(Role.CONTROLLER));

        view = new PrivacyNoticeView(dataBaseService, authenticatedUser, communicationService);

        assertEquals(view.getClassName(), "grid-view");
        assertEquals(view.getElement().getTag(), "div");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(0).getAttribute("style"), "align-items:center");

        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-text-field");
        assertEquals(view.getElement().getChild(0).getChild(0).getAttribute("class"), "search");
        assertEquals(view.getElement().getChild(0).getChild(0).getProperty("placeholder"), "Search...");

        assertEquals(view.getElement().getChild(0).getChild(1).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(0).getChild(1).getText(), "Compile new Privacy Notice");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(1).getThemeList().toString(), "[no-row-borders, no-border]");

        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-grid-column");
    }

    @Test
    public void createPrivacyNoticeCardSubjectTest(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(createUser(Role.SUBJECT));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(List.of(createUser(Role.CONTROLLER)));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(List.of(createUser(Role.DPO)));

        view = new PrivacyNoticeView(dataBaseService, authenticatedUser, communicationService);

        VerticalLayout layout = new VerticalLayout();
        Method createPrivacyNoticeCard = getCreatePrivacyNoticeCard();
        try{
            layout = (VerticalLayout) createPrivacyNoticeCard.invoke(view, (Object) createNotice());
        }
        catch(InvocationTargetException e){
            System.out.println("Exception: "+e.getTargetException());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(layout.getElement().getAttribute("class"), "card canOpen");

        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");
        assertEquals(layout.getElement().getChild(0).getAttribute("class"), "pointer");

        assertEquals(layout.getElement().getChild(0).getChild(0).getTag(), "vaadin-avatar");
        assertEquals(layout.getElement().getChild(0).getChild(0).getAttribute("class"), "avatar");
        assertEquals(layout.getElement().getChild(0).getChild(0).getProperty("name"), "TestApp");
        assertEquals(layout.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(0).getChild(1).getAttribute("class"), "name link pointer");

        assertEquals(layout.getElement().getChild(1).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Go to the app");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "UserCONTROLLER");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "Data Controllers:");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getText(), "UserDPO");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(1).getText(), "Data Protection Officers:");

        assertEquals(layout.getElement().getChild(1).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(1).getText(), "More");
    }

    @Test
    public void createPrivacyNoticeCardNotASubjectTest(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(authenticatedUser.getUser()).thenReturn(createUser(Role.CONTROLLER));
        when(dataBaseService.getControllersFromApp(any())).thenReturn(List.of(createUser(Role.CONTROLLER)));
        when(dataBaseService.getDPOsFromApp(any())).thenReturn(List.of(createUser(Role.DPO)));
        when(dataBaseService.getSubjectsFromApp(any())).thenReturn(List.of(createUser(Role.SUBJECT)));

        view = new PrivacyNoticeView(dataBaseService, authenticatedUser, communicationService);

        VerticalLayout layout = new VerticalLayout();
        Method createPrivacyNoticeCard = getCreatePrivacyNoticeCard();
        try{
            layout = (VerticalLayout) createPrivacyNoticeCard.invoke(view, (Object) createNotice());
        }
        catch(InvocationTargetException e){
            System.out.println("Exception: "+e.getTargetException());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(layout.getElement().getAttribute("class"), "card canOpen");

        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");
        assertEquals(layout.getElement().getChild(0).getAttribute("class"), "pointer");

        assertEquals(layout.getElement().getChild(0).getChild(0).getTag(), "vaadin-avatar");
        assertEquals(layout.getElement().getChild(0).getChild(0).getAttribute("class"), "avatar");
        assertEquals(layout.getElement().getChild(0).getChild(0).getProperty("name"), "TestApp");
        assertEquals(layout.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(0).getChild(1).getAttribute("class"), "name link pointer");

        assertEquals(layout.getElement().getChild(1).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Go to the app");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "UserCONTROLLER");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getChild(1).getText(), "Data Controllers:");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getText(), "UserDPO");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getChild(1).getText(), "Data Protection Officers:");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getTag(), "vaadin-details");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getTag(), "div");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getText(), "UserSUBJECT");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");

        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getText(), "Data Subjects:");

        assertEquals(layout.getElement().getChild(1).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(1).getText(), "More");
    }
}
