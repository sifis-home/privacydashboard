package com.privacydashboard.application.views.messages;

import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.messages.SingleConversationView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.component.textfield.TextArea;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.entity.Message;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

public class SingleConversationViewTest {
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;
    private static SingleConversationView view;

    private static User getContact(){
        User user1 = new User();

        user1.setName("User1");
        user1.setId(new UUID(0, 0));
        user1.setRole(Role.SUBJECT);
        user1.setMail("user1@mail.test");
        user1.setHashedPassword("Password");
        user1.setProfilePictureUrl("https://user1.html");

        return user1;
    }

    private static User getNotContact(){
        User user1 = new User();

        user1.setName("User2");
        user1.setId(new UUID(0, 1));
        user1.setRole(Role.SUBJECT);
        user1.setMail("user2@mail.test");
        user1.setHashedPassword("Password");
        user1.setProfilePictureUrl("https://user2.html");

        return user1;
    }

    private static Message getMessage(LocalDateTime time){
        Message message = new Message();

        message.setMessage("TestMessage");
        message.setSender(getContact());
        message.setTime(time);

        return message;
    }

    private static List<Message> getMessageList(LocalDateTime time){
        Message message1 = new Message();

        message1.setMessage("TestMessage");
        message1.setSender(getContact());
        message1.setTime(time);

        Message message2 = new Message();

        message2.setMessage("TestMessage");
        message2.setSender(getNotContact());
        message2.setTime(time);

        List<Message> list = new ArrayList<>();
        list.add(message1);
        list.add(message2);

        return list;
    }

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        view = new SingleConversationView(dataBaseService, authenticatedUser, communicationService);
    }

    private static Field getContactField(){
        try{
            Field field = SingleConversationView.class.getDeclaredField("contact");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void beforeEnterTest(){
        List<User> list = new ArrayList<>();
        list.add(getContact());

        when(communicationService.getContact()).thenReturn(getContact());
        when(dataBaseService.getAllContactsFromUser(any())).thenReturn(list);

        BeforeEnterEvent event = mock(BeforeEnterEvent.class);        

        view.beforeEnter(event);

        Field field = getContactField();
        User contact = new User();

        try{
            contact = (User) field.get((Object) view);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(contact, getContact());
        assertEquals(GlobalVariables.pageTitle, "User1");
        assertEquals(view.getPageTitle(), "User1");
    }

    @Test
    public void SingleConversationViewConstructorTest(){

        assertEquals(view.getElement().getAttribute("class"), "messages-view");
        assertEquals(view.getElement().getTag(), "span");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(0).getThemeList().toString(), "[no-row-borders, no-border]");
        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-grid-column");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(1).getAttribute("style"), "align-items:center");
        assertEquals(view.getElement().getChild(1).getAttribute("class"), "textAndButton");

        assertEquals(view.getElement().getChild(1).getChild(0).getAttribute("class"), "messageText");
        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-text-area");

        assertEquals(view.getElement().getChild(1).getChild(1).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(1).getChild(1).getText(), "Send Message");
        assertEquals(view.getElement().getChild(1).getChild(1).getAttribute("class"), "buuutton");
    }

    private static Method getShowMessage(){
        try{
            Method method = SingleConversationView.class.getDeclaredMethod("showMessage", Message.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void showMessageSenderIsAuthenticatedTest(){
        Method showMessage = getShowMessage();
        HorizontalLayout layout = new HorizontalLayout();
        LocalDateTime time = LocalDateTime.now();

        when(authenticatedUser.getUser()).thenReturn(getContact());

        try{
            layout = (HorizontalLayout) showMessage.invoke(view, getMessage(time));
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getAttribute("class"), "card isUser");
        assertEquals(layout.getElement().getTag(), "vaadin-horizontal-layout");
        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-message-list");
    }

    @Test
    public void showMessageSenderIsNotAuthenticatedTest(){
        Method showMessage = getShowMessage();
        HorizontalLayout layout = new HorizontalLayout();
        LocalDateTime time = LocalDateTime.now();

        when(authenticatedUser.getUser()).thenReturn(getNotContact());

        try{
            layout = (HorizontalLayout) showMessage.invoke(view, getMessage(time));
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getAttribute("class"), "card isContact");
        assertEquals(layout.getElement().getTag(), "vaadin-horizontal-layout");
        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-message-list");
    }

    private static Method getSendMessage(){
        try{
            Method method = SingleConversationView.class.getDeclaredMethod("sendMessage");
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private static Field getMessageTextField(){
        try{
            Field field = SingleConversationView.class.getDeclaredField("messageText");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void sendMessageTest(){

        Field field = getMessageTextField();
        TextArea area = new TextArea();       

        try{
            area = (TextArea) field.get((Object) view);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        area.setValue("TestTextArea");

        Method sendMessage = getSendMessage();

        try{
            sendMessage.invoke(view);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(area.getValue(), "");
    }
}
