package com.privacydashboard.application.data.apiController;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.Notification;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;

public class ApiNotificationControllerTest {
    
    private DataBaseService dataBaseService;
    private ApiGeneralController apiGeneralController;

    private ApiNotificationController api;
    private ApiGeneralController apiJson;

    private Field getDataBaseService(){
        try{
            Field field = ApiNotificationController.class.getDeclaredField("dataBaseService");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private Field getApiGeneralController(){
        try{
            Field field = ApiNotificationController.class.getDeclaredField("apiGeneralController");
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

    private Notification createNotification(LocalDateTime time){
        Notification note = new Notification();
        note.setId(new UUID(0, 1));
        note.setDescription("TestDescription");
        note.setRead(false);
        note.setReceiver(createUser(Role.CONTROLLER));
        note.setSender(createUser(Role.SUBJECT));
        note.setType("Message");
        note.setObjectId(new UUID(1, 0));
        note.setTime(time);

        return note;
    }

    private Message createMessage(LocalDateTime time){
        Message mess = new Message();
        mess.setId(new UUID(1, 1));
        mess.setMessage("TestMessage");
        mess.setTime(time);
        mess.setReceiver(createUser(Role.CONTROLLER));
        mess.setSender(createUser(Role.SUBJECT));

        return mess;
    }

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("AppName");
        app.setId(new UUID(2, 0));
        app.setDescription("AppDescription");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private RightRequest createRequest(LocalDateTime time){
        RightRequest req = new RightRequest();
        req.setId(new UUID(0, 0));
        req.setSender(createUser(Role.SUBJECT));
        req.setReceiver(createUser(Role.CONTROLLER));
        req.setApp(createApp());
        req.setRightType(RightType.COMPLAIN);
        req.setTime(time);
        req.setHandled(false);

        return req;
    }

    private PrivacyNotice createNotice(){
        PrivacyNotice notice = new PrivacyNotice();
        notice.setId(new UUID(0, 0));
        notice.setApp(createApp());
        notice.setText("NoticeTest");

        return notice;
    }

    @BeforeEach
    public void setup(){
        dataBaseService = mock(DataBaseService.class);
        apiGeneralController = mock(ApiGeneralController.class);

        api = new ApiNotificationController();
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
    public void getIdNotValidTest(){
        when(apiGeneralController.getNotificationFromId(any())).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.get("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getUserHasNotNotificationTest(){
        LocalDateTime time = LocalDateTime.now();
        Notification note = createNotification(time);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));

        ResponseEntity res = api.get(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be the sender or the receiver of the notification");
    }

    @Test
    public void getUserIsSenderTest(){
        LocalDateTime time = LocalDateTime.now();
        Notification note = createNotification(time);

        JsonNode node = apiJson.createJsonFromNotification(note);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.createJsonFromNotification(note)).thenReturn(node);

        ResponseEntity res = api.get(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getUserIsReceiverTest(){
        LocalDateTime time = LocalDateTime.now();
        Notification note = createNotification(time);

        JsonNode node = apiJson.createJsonFromNotification(note);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.CONTROLLER));
        when(apiGeneralController.createJsonFromNotification(note)).thenReturn(node);

        ResponseEntity res = api.get(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getAllFromUserIdNotValidTest(){
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getAllFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllFromUserUserIsNotAuthTest(){
        User user = createUser(Role.CONTROLLER);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.getAllFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be the user identified by the userId");
    }

    @Test
    public void getAllFromMatchTest(){
        User user = createUser(Role.CONTROLLER);
        Notification note = createNotification(LocalDateTime.now());
        Notification note2 = createNotification(LocalDateTime.now());

        ArrayNode array = new ObjectMapper().createArrayNode();
        array.add(apiJson.createJsonFromNotification(note));
        array.add(apiJson.createJsonFromNotification(note2));

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.createJsonNotificationsFromUser(user, true, true)).thenReturn(array);

        ResponseEntity res = api.getAllFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getReadFromUserIdNotValidTest(){
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getReadFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getReadFromUserUserIsNotAuthTest(){
            User user = createUser(Role.CONTROLLER);
    
            when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
            when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);
    
            ResponseEntity res = api.getReadFromUser(user.getId().toString());
            assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertEquals(res.getBody().toString(), "you MUST be the user identified by the userId");
    }

    @Test
    public void getReadFromUserMatchTest(){
        User user = createUser(Role.CONTROLLER);
        Notification note = createNotification(LocalDateTime.now());
        Notification note2 = createNotification(LocalDateTime.now());

        ArrayNode array = new ObjectMapper().createArrayNode();
        array.add(apiJson.createJsonFromNotification(note));
        array.add(apiJson.createJsonFromNotification(note2));

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.createJsonNotificationsFromUser(user, false, true)).thenReturn(array);

        ResponseEntity res = api.getReadFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getNotReadFromUserIdNotValidTest(){
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getNotReadFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getNotReadFromUserUserIsNotAuthTest(){
            User user = createUser(Role.CONTROLLER);
    
            when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
            when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);
    
            ResponseEntity res = api.getNotReadFromUser(user.getId().toString());
            assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
            assertEquals(res.getBody().toString(), "you MUST be the user identified by the userId");
    }

    @Test
    public void getNotReadFromUserMatchTest(){
        User user = createUser(Role.CONTROLLER);
        Notification note = createNotification(LocalDateTime.now());
        Notification note2 = createNotification(LocalDateTime.now());

        ArrayNode array = new ObjectMapper().createArrayNode();
        array.add(apiJson.createJsonFromNotification(note));
        array.add(apiJson.createJsonFromNotification(note2));

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.createJsonNotificationsFromUser(user, false, false)).thenReturn(array);

        ResponseEntity res = api.getNotReadFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getObjectOfNotificationIdNotValidTest(){
        when(apiGeneralController.getNotificationFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getObjectOfNotification("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getObjectOfNotificationUserHasNotTest(){
        Notification note = createNotification(LocalDateTime.now());
        
        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));

        ResponseEntity res = api.getObjectOfNotification(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be the sender or the receiver of the notification");
    }

    @Test
    public void getObjectOfNotificationWrongTypeTest(){
        Notification note = createNotification(LocalDateTime.now());
        note.setType("NotAType");

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));

        ResponseEntity res = api.getObjectOfNotification(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "object associated with notification does not exist");
    }

    @Test
    public void getObjectOfNotificationMessageTest(){
        Notification note = createNotification(LocalDateTime.now());
        Message mess = createMessage(LocalDateTime.now());

        ObjectNode node = (ObjectNode) apiJson.createJsonFromMessage(mess);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getMessageFromId(any())).thenReturn(mess);
        when(apiGeneralController.createJsonFromMessage(mess)).thenReturn(node);

        ResponseEntity res = api.getObjectOfNotification(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getObjectOfNotificationRequestTest(){
        Notification note = createNotification(LocalDateTime.now());
        RightRequest req = createRequest(LocalDateTime.now());
        note.setType("Request");

        ObjectNode node = (ObjectNode) apiJson.createJsonFromRequest(req);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getRequestFromId(any())).thenReturn(req);
        when(apiGeneralController.createJsonFromRequest(req)).thenReturn(node);

        ResponseEntity res = api.getObjectOfNotification(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getObjectOfNotificationNoticeTest(){
        Notification note = createNotification(LocalDateTime.now());
        PrivacyNotice notice = createNotice();
        note.setType("PrivacyNotice");

        ObjectNode node = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getPrivacyNoticeFromId(any())).thenReturn(notice);
        when(apiGeneralController.createJsonFromPrivacyNotice(notice)).thenReturn(node);

        ResponseEntity res = api.getObjectOfNotification(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void addInvalidJSONTest(){
        try{
            when(apiGeneralController.getNotificationFromJsonString(any())).thenThrow(IOException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        ResponseEntity res = api.add("NotAJson");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addUserHasNotNotificationTest(){
        Notification note = createNotification(LocalDateTime.now());

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getReceiver());

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be the sender of the notification");
    }

    @Test
    public void addUserIsNotAuthTest(){
        Notification note = createNotification(LocalDateTime.now());

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }    

    @Test
    public void addReceiverHasNotObjectTest(){
        Notification note = createNotification(LocalDateTime.now());
        note.setRead(null);

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());
        when(dataBaseService.getMessageFromID(any())).thenReturn(Optional.empty());

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "Object is not associated with receiver");
    }

    @Test
    public void addMessageTest(){
        Notification note = createNotification(LocalDateTime.now());
        Message mess = createMessage(LocalDateTime.now());

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());
        when(dataBaseService.getMessageFromID(any())).thenReturn(Optional.of(mess));

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Notification added successfully");
    }

    @Test
    public void addRequestTest(){
        Notification note = createNotification(LocalDateTime.now());
        note.setTime(null);
        note.setType("Request");
        RightRequest req = createRequest(LocalDateTime.now());

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());
        when(dataBaseService.getRequestFromId(any())).thenReturn(req);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Notification added successfully");
    }

    @Test
    public void addPrivacyNoticeTest(){
        Notification note = createNotification(LocalDateTime.now());
        note.setType("PrivacyNotice");
        PrivacyNotice notice = createNotice();

        String body = apiJson.createJsonFromNotification(note).toString();

        try{
            when(apiGeneralController.getNotificationFromJsonString(body)).thenReturn(note);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());
        when(dataBaseService.getPrivacyNoticeFromId(any())).thenReturn(Optional.of(notice));
        when(dataBaseService.userHasApp(any(), any())).thenReturn(true);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Notification added successfully");
    }

    @Test
    public void deleteIdNotValidTest(){
        when(apiGeneralController.getNotificationFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.delete("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteUserIsNotReceiverTest(){
        Notification note = createNotification(LocalDateTime.now());

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());

        ResponseEntity res = api.delete(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You MUST be the receiver of the notification");
    }

    @Test
    public void deleteMatchTest(){
        Notification note = createNotification(LocalDateTime.now());

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getReceiver());

        ResponseEntity res = api.delete(note.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Notification deleted successfully");
    }

    @Test
    public void changeIsReadIdNotValidTest(){
        when(apiGeneralController.getNotificationFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.changeIsRead("1", "true");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void changeIsReadStringNotValidTest(){
        Notification note = createNotification(LocalDateTime.now());

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);

        ResponseEntity res = api.changeIsRead(note.getId().toString(), "NotABoolean");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "Invalid isReadString value. MUST be true or false");
    }

    @Test
    public void changeIsReadUserIsNotReceiverTest(){
        Notification note = createNotification(LocalDateTime.now());

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getSender());

        ResponseEntity res = api.changeIsRead(note.getId().toString(), "true");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You MUST be the receiver of the notification");
    }

    @Test
    public void changeIsReadMatchTest(){
        Notification note = createNotification(LocalDateTime.now());

        when(apiGeneralController.getNotificationFromId(note.getId().toString())).thenReturn(note);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(note.getReceiver());

        ResponseEntity res = api.changeIsRead(note.getId().toString(), "false");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Notification value changed successfully");
    }
}
