package com.privacydashboard.application.views.contacts;

import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.testbench.GridColumnElement;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.test.util.ReflectionTestUtils;

public class ContactsViewTest {
    
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;
    private static ContactsView view;

    private static User createTestUser(Role role){
        User user = new User();
    
        user.setName("UserName");
        user.setId(new UUID(0, 0));
        user.setProfilePictureUrl("https://testurl.html");
        user.setHashedPassword("HashedPassword");
        user.setRole(role);
        user.setMail("test@mail.test");

        return user;
    }

    private static List<IoTApp> createAppList(){
        IoTApp app1 = new IoTApp();
        app1.setName("App1");

        IoTApp app2 = new IoTApp();
        app2.setName("App2");

        List<IoTApp> list = new ArrayList();
        list.add(app1);
        list.add(app2);

        return list;
    }

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        view = new ContactsView(authenticatedUser, dataBaseService, communicationService);
    }

    @Test
    public void ContactsViewConstructorTest(){
        assertEquals(view.getElement().getAttribute("class"), "grid-view");
        assertEquals(view.getElement().getChild(0).getAttribute("class"), "search");
        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-text-field");
        assertEquals(view.getElement().getChild(1).getThemeList().toString(), "["+GridVariant.LUMO_NO_ROW_BORDERS.getVariantName()+", "+GridVariant.LUMO_NO_BORDER.getVariantName()+"]");
        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-grid-column");
    }

    private Method getCreateContactMethod(){
        try{
            Method method = ContactsView.class.getDeclaredMethod("createContact", User.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void createContactTest(){
        Method createContact = getCreateContactMethod();
        VerticalLayout card = new VerticalLayout();

        when(dataBaseService.getAppsFrom2Users(any(), any())).thenReturn(createAppList());

        try{
            card = (VerticalLayout) createContact.invoke(view, (Object) createTestUser(Role.SUBJECT));    
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(card.getElement().getAttribute("class"), "card canOpen");
        assertEquals(card.getElement().getTag(), "vaadin-vertical-layout");

        assertEquals(card.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(card.getElement().getChild(0).getChild(0).getTag(), "vaadin-avatar");
        assertEquals(card.getElement().getChild(0).getChild(0).getAttribute("img"), "https://testurl.html");
        assertEquals(card.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(card.getElement().getChild(0).getChild(1).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(0).getChild(1).getText(), "UserName");

        assertEquals(card.getElement().getChild(1).getTag(), "vaadin-details");

        assertEquals(card.getElement().getChild(1).getChild(0).getTag(), "div");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(0).getText(), "NAME: UserName");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(1).getText(), "ROLE: Data SUBJECT");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(2).getText(), "MAIL: test@mail.test");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getTag(), "vaadin-horizontal-layout");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getAttribute("class"), "link");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(0).getText(), "send message");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getTag(), "vaadin-icon");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(3).getChild(1).getAttribute("icon"), "vaadin:comment");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getTag(), "vaadin-details");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getTag(), "div");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(0).getAttribute("class"), "link");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(0).getText(), "App1");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(1).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(1).getAttribute("class"), "link");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getChild(1).getText(), "App2");

        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(1).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(0).getChild(0).getChild(4).getChild(1).getText(), "Apps:");

        assertEquals(card.getElement().getChild(1).getChild(1).getTag(), "span");
        assertEquals(card.getElement().getChild(1).getChild(1).getText(), "More");
    }
   
}