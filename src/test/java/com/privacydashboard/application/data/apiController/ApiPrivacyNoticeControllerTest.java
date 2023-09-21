package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import org.springframework.http.HttpStatus;
import org.h2.engine.Database;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;

public class ApiPrivacyNoticeControllerTest {
    
    private DataBaseService dataBaseService;
    private ApiGeneralController apiGeneralController;
    
    private ApiGeneralController apiJson;

    private ApiPrivacyNoticeController api;

    private Field getDataBaseService(){
        try{
            Field field = ApiPrivacyNoticeController.class.getDeclaredField("dataBaseService");
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
            Field field = ApiPrivacyNoticeController.class.getDeclaredField("apiGeneralController");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setId(new UUID(0, 1));
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private List<IoTApp> createAppList(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setId(new UUID(0, 0));
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        IoTApp app1 = new IoTApp();
        app1.setName("TestApp1");
        app1.setDescription("TestDescription1");
        app1.setId(new UUID(0, 1));
        app1.setQuestionnaireVote(QuestionnaireVote.RED);

        IoTApp app2 = new IoTApp();
        app2.setName("TestApp2");
        app2.setDescription("TestDescription2");
        app2.setId(new UUID(0, 2));
        app2.setQuestionnaireVote(QuestionnaireVote.ORANGE);

        return List.of(app, app1, app2);
    }

    private List<PrivacyNotice> createNoticeList(){
        PrivacyNotice notice = new PrivacyNotice();
        notice.setId(new UUID(1, 0));
        notice.setApp(createAppList().get(0));
        notice.setText("NoticeTest");

        PrivacyNotice notice1 = new PrivacyNotice();
        notice1.setId(new UUID(1, 1));
        notice1.setApp(createAppList().get(1));
        notice1.setText("NoticeTest1");

        PrivacyNotice notice2 = new PrivacyNotice();
        notice2.setId(new UUID(1, 2));
        notice2.setApp(createAppList().get(2));
        notice2.setText("NoticeTest2");

        return List.of(notice, notice1, notice2);
    }

    private PrivacyNotice createNotice(){
        PrivacyNotice notice = new PrivacyNotice();
        notice.setId(new UUID(0, 0));
        notice.setApp(createApp());
        notice.setText("NoticeTest");

        return notice;
    }

    private User createUser(Role role){
        User user = new User();
        user.setRole(role);
        user.setId(new UUID(2, role.hashCode()));
        user.setName("User"+role.toString());
        user.setMail("test"+role.toString()+"mail@test.test");

        return user;
    }

    @BeforeEach
    public void setup(){
        dataBaseService = mock(DataBaseService.class);
        apiGeneralController = mock(ApiGeneralController.class);

        apiJson = new ApiGeneralController();
        api = new ApiPrivacyNoticeController();

        try{
            getDataBaseService().set(api, dataBaseService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e.getMessage());
        }

        try{
            getApiGeneralController().set(api, apiGeneralController);
        }
        catch(Exception e){
            System.out.println("Exception: "+e.getMessage());
        }
    }

    @Test
    public void getIdNotValidTest(){
        when(apiGeneralController.getPrivacyNoticeFromId(new UUID(0, 0).toString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.get(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getMatchTest(){
        PrivacyNotice notice = createNotice();

        JsonNode node = apiJson.createJsonFromPrivacyNotice(notice);

        when(apiGeneralController.getPrivacyNoticeFromId(notice.getId().toString())).thenReturn(notice);
        when(apiGeneralController.createJsonFromPrivacyNotice(notice)).thenReturn(node);

        ResponseEntity res = api.get(notice.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getFromAppIdNotValidTest(){
        when(apiGeneralController.getPrivacyNoticeFromAppId(new UUID(0, 0).toString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getFromApp(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getFromAppMatchTest(){
        PrivacyNotice notice = createNotice();

        JsonNode node = apiJson.createJsonFromPrivacyNotice(notice);

        when(apiGeneralController.getPrivacyNoticeFromAppId(notice.getApp().getId().toString())).thenReturn(notice);
        when(apiGeneralController.createJsonFromPrivacyNotice(notice)).thenReturn(node);

        ResponseEntity res = api.getFromApp(notice.getApp().getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getFromUserIdNotValidTest(){
        when(apiGeneralController.getUserFromId(new UUID(0, 0).toString())).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getFromUser(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getFromUserSubjectAndNotAuthTest(){
        User user = createUser(Role.SUBJECT);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.getFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "MUST be ID of controller or DPO, or you must be the user identified by userId");
    }

    @Test
    public void getFromUserMatchTest(){
        User user = createUser(Role.CONTROLLER);

        ArrayNode array = new ObjectMapper().createArrayNode();
        array.add(apiJson.createJsonFromPrivacyNotice(createNotice()));

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);
        when(dataBaseService.getUserApps(user)).thenReturn(createAppList());
        when(dataBaseService.getPrivacyNoticeFromApp(createAppList().get(0))).thenReturn(createNotice());
        when(dataBaseService.getPrivacyNoticeFromApp(createAppList().get(1))).thenReturn(null);
        when(dataBaseService.getPrivacyNoticeFromApp(createAppList().get(2))).thenReturn(null);
        when(apiGeneralController.createJsonFromPrivacyNotice(createNotice())).thenReturn(apiJson.createJsonFromPrivacyNotice(createNotice()));

        ResponseEntity res = api.getFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void addInvalidJSONTest(){
        try{
            when(apiGeneralController.getPrivacyNoticeFromJsonString(anyBoolean(), any())).thenThrow(IOException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        
        ResponseEntity res = api.add("NotAJson");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addSubjectTest(){
        PrivacyNotice notice = createNotice();

        String body = apiJson.createJsonFromPrivacyNotice(notice).toString();

        User user = createUser(Role.SUBJECT);

        try{
            when(apiGeneralController.getPrivacyNoticeFromJsonString(true, body)).thenReturn(notice);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(dataBaseService.userHasApp(user, notice.getApp())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be controller and DPO of the app");
    }

    @Test
    public void addUserHasNotAppTest(){
        PrivacyNotice notice = createNotice();

        String body = apiJson.createJsonFromPrivacyNotice(notice).toString();

        User user = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getPrivacyNoticeFromJsonString(true, body)).thenReturn(notice);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(dataBaseService.userHasApp(user, notice.getApp())).thenReturn(false);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you MUST be controller and DPO of the app");
    }

    @Test
    public void addMatchTest(){
        PrivacyNotice notice = createNotice();

        String body = apiJson.createJsonFromPrivacyNotice(notice).toString();

        User user = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getPrivacyNoticeFromJsonString(true, body)).thenReturn(notice);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.userHasApp(user, notice.getApp())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice created successfully");
    }

    @Test
    public void addInvalidAuthTest(){
        PrivacyNotice notice = createNotice();

        String body = apiJson.createJsonFromPrivacyNotice(notice).toString();

        User user = createUser(Role.CONTROLLER);

        try{
            when(apiGeneralController.getPrivacyNoticeFromJsonString(true, body)).thenReturn(notice);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        when(apiGeneralController.getAuthenicatedUser()).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.add(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addVariousSubjectTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.addVarious("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO");
    }

    @Test
    public void addVariousUserNotAuth(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.addVarious("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addVariousInvalidJSONTest(){
        String body = "NotAJson";

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addVariousTextMissingTest(){
        PrivacyNotice notice = createNotice();

        List<IoTApp> apps = createAppList();

        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        ArrayNode appsId = new ObjectMapper().createArrayNode();
        appsId.add(apps.get(0).getId().toString());
        appsId.add(apps.get(1).getId().toString());
        appsId.add(apps.get(2).getId().toString());

        nodeBody.set("appsId", appsId);
        nodeBody.remove("text");

        String body = nodeBody.toString();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getAppFromId(appsId.get(0).asText())).thenReturn(apps.get(0));
        when(apiGeneralController.getAppFromId(appsId.get(1).asText())).thenReturn(apps.get(1));
        when(apiGeneralController.getAppFromId(appsId.get(2).asText())).thenReturn(apps.get(2));

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "text is missing");
    }

    @Test
    public void addVariousMatchTest(){
        PrivacyNotice notice = createNotice();

        List<IoTApp> apps = createAppList();

        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        ArrayNode appsId = new ObjectMapper().createArrayNode();
        appsId.add(apps.get(0).getId().toString());
        appsId.add(apps.get(1).getId().toString());
        appsId.add(apps.get(2).getId().toString());

        nodeBody.set("appsId", appsId);

        String body = nodeBody.toString();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getAppFromId(appsId.get(0).asText())).thenReturn(apps.get(0));
        when(apiGeneralController.getAppFromId(appsId.get(1).asText())).thenReturn(apps.get(1));
        when(apiGeneralController.getAppFromId(appsId.get(2).asText())).thenReturn(apps.get(2));

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice added to all the apps");
    }

    @Test
    public void addVariousMissingAppsIdTest(){
        PrivacyNotice notice = createNotice();
        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        String body = nodeBody.toString();


        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addVariousAppsIdNotArrayTest(){
        PrivacyNotice notice = createNotice();
        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        nodeBody.put("appsId", "NotAnArray");
        String body = nodeBody.toString();


        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void addVariousUserHasNotAppsTest(){
        PrivacyNotice notice = createNotice();

        List<IoTApp> apps = createAppList();

        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        ArrayNode appsId = new ObjectMapper().createArrayNode();
        appsId.add(apps.get(0).getId().toString());
        appsId.add(apps.get(1).getId().toString());
        appsId.add(apps.get(2).getId().toString());

        nodeBody.set("appsId", appsId);

        String body = nodeBody.toString();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);
        when(apiGeneralController.getAppFromId(appsId.get(0).asText())).thenReturn(apps.get(0));
        when(apiGeneralController.getAppFromId(appsId.get(1).asText())).thenReturn(apps.get(1));
        when(apiGeneralController.getAppFromId(appsId.get(2).asText())).thenReturn(apps.get(2));

        ResponseEntity res = api.addVarious(body);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You don't have all the apps");
    }

    @Test
    public void copySubjectTest(){
        PrivacyNotice notice = createNotice();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.copy(notice.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO");
    }

    @Test
    public void copyUserNotAuthTest(){
        PrivacyNotice notice = createNotice();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.copy(notice.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void copyInvalidJSONTest(){
        PrivacyNotice notice = createNotice();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromId(notice.getId().toString())).thenReturn(notice);

        ResponseEntity res = api.copy(notice.getId().toString(), "NotAJson");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void copyMatchTest(){
        PrivacyNotice notice = createNotice();

        List<IoTApp> apps = createAppList();

        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        ArrayNode appsId = new ObjectMapper().createArrayNode();
        appsId.add(apps.get(0).getId().toString());
        appsId.add(apps.get(1).getId().toString());
        appsId.add(apps.get(2).getId().toString());

        nodeBody.set("appsId", appsId);

        String body = nodeBody.toString();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromId(notice.getId().toString())).thenReturn(notice);

        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getAppFromId(appsId.get(0).asText())).thenReturn(apps.get(0));
        when(apiGeneralController.getAppFromId(appsId.get(1).asText())).thenReturn(apps.get(1));
        when(apiGeneralController.getAppFromId(appsId.get(2).asText())).thenReturn(apps.get(2));

        ResponseEntity res = api.copy(notice.getId().toString(), body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice added to all the apps");
    }

    @Test
    public void copyFromAppSubjectTest(){
        IoTApp app = createApp();
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.copyFromApp(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO");
    }

    @Test
    public void copyFromAppUserNotAuthTest(){
        IoTApp app = createApp();
        when(apiGeneralController.isControllerOrDpo(true, null)).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.copyFromApp(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void copyFromAppInvalidJSONTest(){
        IoTApp app = createApp();
        PrivacyNotice notice = createNotice();
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromAppId(app.getId().toString())).thenReturn(notice);

        ResponseEntity res = api.copyFromApp(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void copyFromAppMatchTest(){
        PrivacyNotice notice = createNotice();
        IoTApp app = createApp();

        List<IoTApp> apps = createAppList();

        ObjectNode nodeBody = (ObjectNode) apiJson.createJsonFromPrivacyNotice(notice);
        ArrayNode appsId = new ObjectMapper().createArrayNode();
        appsId.add(apps.get(0).getId().toString());
        appsId.add(apps.get(1).getId().toString());
        appsId.add(apps.get(2).getId().toString());

        nodeBody.set("appsId", appsId);

        String body = nodeBody.toString();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromAppId(app.getId().toString())).thenReturn(notice);

        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getAppFromId(appsId.get(0).asText())).thenReturn(apps.get(0));
        when(apiGeneralController.getAppFromId(appsId.get(1).asText())).thenReturn(apps.get(1));
        when(apiGeneralController.getAppFromId(appsId.get(2).asText())).thenReturn(apps.get(2));

        ResponseEntity res = api.copyFromApp(app.getId().toString(), body);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice added to all the apps");
    }

    @Test
    public void deleteSubjectTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.delete(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO");
    }

    @Test
    public void deleteUserNotAuthTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.delete(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteUserHasNotAppTest(){
        PrivacyNotice notice = createNotice();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromId(notice.getId().toString())).thenReturn(notice);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.delete(notice.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO of the APP");
    }

    @Test
    public void deleteMatchTest(){
        PrivacyNotice notice = createNotice();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromId(notice.getId().toString())).thenReturn(notice);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);

        ResponseEntity res = api.delete(notice.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice successfully deleted");
    }

    @Test
    public void deleteFromAppSubjectTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.deleteFromApp(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO");
    }

    @Test
    public void deleteFromAppUserNotAuthTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.deleteFromApp(new UUID(0, 0).toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteFromAppUserHasNotAppTest(){
        PrivacyNotice notice = createNotice();
        IoTApp app = createApp();
    
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromAppId(app.getId().toString())).thenReturn(notice);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.deleteFromApp(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be a Controller or DPO of the APP");
    }

    @Test
    public void deleteFromAppMatchTest(){
        PrivacyNotice notice = createNotice();
        IoTApp app = createApp();
    
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.getPrivacyNoticeFromAppId(app.getId().toString())).thenReturn(notice);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);        

        ResponseEntity res = api.deleteFromApp(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "Privacy Notice successfully deleted");
    }
}
