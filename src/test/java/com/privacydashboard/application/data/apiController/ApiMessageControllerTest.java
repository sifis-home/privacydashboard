package com.privacydashboard.application.data.apiController;

import com.privacydashboard.application.data.service.DataBaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

public class ApiMessageControllerTest {
    
    private DataBaseService dataBaseService;
    private ApiGeneralController apiGeneralController;

    private ApiMessageController api;
    private ApiGeneralController apiJson;

    private Field getApiGeneralController(){
        try{
            Field field = ApiMessageController.class.getDeclaredField("apiGeneralController");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private Field getDataBaseService(){
        try{
            Field field = ApiMessageController.class.getDeclaredField("dataBaseService");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private User createUser(Role role){
        User user = new User();
        user.setId(new UUID(0, role.hashCode()));
        user.setRole(role);
        user.setName("User"+role.toString());
        user.setMail("test"+role.toString()+"@mail.test");

        return user;
    }

    private Message createMessage(){
        Message mess = new Message();
        mess.setId(new UUID(0,  1));
        mess.setMessage("TestMessage");
        mess.setSender(createUser(Role.SUBJECT));
        mess.setReceiver(createUser(Role.CONTROLLER));
        mess.setTime(LocalDateTime.now());

        return mess;
    }

    @BeforeEach
    public void setup(){
        apiGeneralController = mock(ApiGeneralController.class);
        dataBaseService = mock(DataBaseService.class);

        api = new ApiMessageController();
        apiJson = new ApiGeneralController();

        try{
            getDataBaseService().set(api, dataBaseService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        try{
            getApiGeneralController().set(api, apiGeneralController);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
    }

    @Test
    public void getInvalidIdTest(){
        when(apiGeneralController.getMessageFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.get("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getUserHasNotMessageTest(){
        Message mess = createMessage();

        when(apiGeneralController.getMessageFromId(mess.getId().toString())).thenReturn(mess);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));

        ResponseEntity res = api.get(mess.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be sender or receiver of the message");
    }

    @Test
    public void getUserIsSenderTest(){
        Message mess = createMessage();
        ObjectNode node = apiJson.createJsonFromMessage(mess);

        when(apiGeneralController.getMessageFromId(mess.getId().toString())).thenReturn(mess);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.createJsonFromMessage(mess)).thenReturn(node);

        ResponseEntity res = api.get(mess.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getUserIsReceiverTest(){
        Message mess = createMessage();
        ObjectNode node = apiJson.createJsonFromMessage(mess);

        when(apiGeneralController.getMessageFromId(mess.getId().toString())).thenReturn(mess);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.CONTROLLER));
        when(apiGeneralController.createJsonFromMessage(mess)).thenReturn(node);

        ResponseEntity res = api.get(mess.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getConversationUserHasNotConversationTest(){
        Message mess = createMessage();
        User sender = mess.getSender();
        User receiver = mess.getReceiver();

        when(apiGeneralController.isAuthenticatedUserId(sender.getId().toString())).thenReturn(false);
        when(apiGeneralController.isAuthenticatedUserId(receiver.getId().toString())).thenReturn(false);

        ResponseEntity res = api.getConversation(sender.getId().toString(), receiver.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be one of the two users");
    }

    @Test
    public void getConversationInvalidIdTest(){
        Message mess = createMessage();
        User sender = mess.getSender();
        User receiver = mess.getReceiver();

        when(apiGeneralController.isAuthenticatedUserId(sender.getId().toString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getConversation(sender.getId().toString(), receiver.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getConversationMatchTest(){
        Message mess = createMessage();
        User sender = mess.getSender();
        User receiver = mess.getReceiver();

        List<Message> list = List.of(mess);
        ArrayNode array = new ObjectMapper().createArrayNode();
        ObjectNode node = apiJson.createJsonFromMessage(mess);
        array.add(node);

        when(apiGeneralController.isAuthenticatedUserId(sender.getId().toString())).thenReturn(true);
        when(apiGeneralController.isAuthenticatedUserId(receiver.getId().toString())).thenReturn(true);
        when(apiGeneralController.getUserFromId(sender.getId().toString())).thenReturn(sender);
        when(apiGeneralController.getUserFromId(receiver.getId().toString())).thenReturn(receiver);
        when(dataBaseService.getConversationFromUsers(sender, receiver)).thenReturn(list);
        when(apiGeneralController.createJsonFromMessage(mess)).thenReturn(node);

        ResponseEntity res = api.getConversation(sender.getId().toString(), receiver.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getAllMessagesFromUserInvalidIdTest(){
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getAllMessagesFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllMessagesFromUserUserIsNotAuthTest(){
        User user = createUser(Role.SUBJECT);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.getAllMessagesFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId");
    }

    @Test
    public void getAllMessagesFromUserMatchTest(){
        User user = createUser(Role.SUBJECT);
        User contact = createUser(Role.CONTROLLER);
        Message mess = createMessage();

        ArrayNode array = new ObjectMapper().createArrayNode();
        ObjectNode conversation = new ObjectMapper().createObjectNode();
        conversation.put("contactName", contact.getName());
        ObjectNode node = apiJson.createJsonFromMessage(mess);
        ArrayNode messages = new ObjectMapper().createArrayNode();
        messages.add(node);
        conversation.set("messages", messages);
        array.add(conversation);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(dataBaseService.getUserConversationFromUser(user)).thenReturn(List.of(contact));
        when(dataBaseService.getConversationFromUsers(user, contact)).thenReturn(List.of(mess));
        when(apiGeneralController.createJsonFromMessage(mess)).thenReturn(node);

        ResponseEntity res = api.getAllMessagesFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void addInvalidJSONTest(){
        try{
            when(apiGeneralController.getMessageFromJsonString("Body")).thenThrow(IOException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);       
        }

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addInvalidIdTest(){
        Message mess = createMessage();

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenThrow(IllegalArgumentException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addUserIsNotSenderTest(){
        Message mess = createMessage();
        User user = createUser(Role.DPO);

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenReturn(mess);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the sender of the message");
    }

    @Test
    public void addReceiverIsNotAContactTest(){
        Message mess = createMessage();
        User user = createUser(Role.SUBJECT);
        User contact = createUser(Role.DPO);

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenReturn(mess);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getAllContactsFromUser(user)).thenReturn(List.of(contact));

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "the 2 users must have 1 app in common");
    }

    @Test
    public void addMessageAlreadyExistTest(){
        Message mess = createMessage();
        User user = createUser(Role.SUBJECT);
        User contact = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenReturn(mess);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getAllContactsFromUser(user)).thenReturn(List.of(contact));
        when(dataBaseService.getMessageFromID(mess.getId())).thenReturn(Optional.of(mess));

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "there is already a message with that ID");
    }

    @Test
    public void addMatchTimeTest(){
        Message mess = createMessage();
        User user = createUser(Role.SUBJECT);
        User contact = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenReturn(mess);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getAllContactsFromUser(user)).thenReturn(List.of(contact));
        when(dataBaseService.getMessageFromID(mess.getId())).thenReturn(Optional.empty());

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Message sent successfully");
    }

    @Test
    public void addMatchNoTimeTest(){
        Message mess = createMessage();
        mess.setTime(null);
        User user = createUser(Role.SUBJECT);
        User contact = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getMessageFromJsonString(any())).thenReturn(mess);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getAllContactsFromUser(user)).thenReturn(List.of(contact));
        when(dataBaseService.getMessageFromID(mess.getId())).thenReturn(Optional.empty());

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Message sent successfully");
    }
}
