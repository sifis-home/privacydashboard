package com.privacydashboard.application.views.messages;

import com.privacydashboard.application.views.messages.MessagesView;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MessagesViewTest {
    
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;

    private static MessagesView view;

    private static List<User> getUserList(){
        User user1 = new User();

        user1.setName("User1");
        user1.setId(new UUID(0, 0));
        user1.setRole(Role.SUBJECT);
        user1.setMail("user1@mail.test");
        user1.setHashedPassword("Password");
        user1.setProfilePictureUrl("https://user1.html");

        User user2 = new User();

        user2.setName("User2");
        user2.setId(new UUID(0, 0));
        user2.setRole(Role.SUBJECT);
        user2.setMail("user2@mail.test");
        user2.setHashedPassword("Password");
        user2.setProfilePictureUrl("https://user2.html");

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        return list;
    }

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        when(dataBaseService.getAllContactsFromUser(any())).thenReturn(getUserList());

        view = new MessagesView(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void MessagesViewConstructorTest(){

        assertEquals(view.getElement().getAttribute("class"), "grid-view");
        assertEquals(view.getElement().getTag(), "div");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-text-field");
        assertEquals(view.getElement().getChild(0).getChild(0).getAttribute("class"), "search");
        assertEquals(view.getElement().getChild(0).getChild(1).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(0).getChild(1).getText(), "New Message");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(1).getThemeList().toString(), "[no-row-borders, no-border]");
        
        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-grid-column");
    }

    private static Field getNewMessageDialog(){
        try{
            Field field = MessagesView.class.getDeclaredField("newMessageDialog");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void newMessageDialogTest(){
        MyDialog dialog = new MyDialog();

        when(dataBaseService.getAllContactsFromUser(any())).thenReturn(getUserList());

        try{
            dialog = (MyDialog) getNewMessageDialog().get((Object) view);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(dialog.getElement().getTag(), "vaadin-dialog");
        assertEquals(dialog.getHeaderTitle(), "Select a contact");
    }

    private static Method getShowContact(){
        try{
            Method method = MessagesView.class.getDeclaredMethod("showContact", User.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void showContactTest(){
        Method showContact = getShowContact();
        HorizontalLayout layout = new HorizontalLayout();

        try{
            layout = (HorizontalLayout) showContact.invoke(view, (Object) getUserList().get(0));
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getTag(), "vaadin-horizontal-layout");
        assertEquals(layout.getElement().getAttribute("class"), "card canOpen");
        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-avatar");
        assertEquals(layout.getElement().getChild(0).getAttribute("img"), "https://user1.html");
        assertEquals(layout.getElement().getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(1).getText(), "User1");
        
        
    }
}
