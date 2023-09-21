package com.privacydashboard.application.data.apiController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.Mockito.any;

import java.util.UUID;
import java.io.StringReader;
import java.lang.reflect.Field;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpsRedirectSpec;
import org.springframework.web.client.support.RestGatewaySupport;
import org.springframework.http.HttpStatus;

import com.privacydashboard.application.data.service.DataBaseService;

public class ApiUserAppRelationControllerTest {
    
    private ApiGeneralController apiGeneralController;
    private DataBaseService dataBaseService;

    private ApiGeneralController apiJson;
    private ApiUserAppRelationController api;

    private Field getApiGeneralController(){
        try{
            Field field = ApiUserAppRelationController.class.getDeclaredField("apiGeneralController");
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
            Field field = ApiUserAppRelationController.class.getDeclaredField("dataBaseService");
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

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("AppName");
        app.setId(new UUID(2, 0));
        app.setDescription("AppDescription");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private UserAppRelation createUserAppRelation(){
        UserAppRelation rel = new UserAppRelation();
        rel.setApp(createApp());
        rel.setUser(createUser(Role.SUBJECT));
        rel.setId(new UUID(0, 0));
        
        return rel;
    }

    @BeforeEach
    public void setup(){
        apiGeneralController = mock(ApiGeneralController.class);
        dataBaseService = mock(DataBaseService.class);

        apiJson = new ApiGeneralController();
        api = new ApiUserAppRelationController();

        try{
            getApiGeneralController().set(api, apiGeneralController);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        try{
            getDataBaseService().set(api, dataBaseService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
    }

    @Test
    public void getInvalidIdTest(){
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.get(createUser(Role.CONTROLLER).getId().toString(), "1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getUserHasNotAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(false);

        ResponseEntity res = api.get(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be connected with the app");
    }

    @Test
    public void getUserIsNotAuthTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(true);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.get(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId or the controller/DPO of the app");
    }

    @Test
    public void getUserIsSubjectTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(true);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.get(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId or the controller/DPO of the app");
    }

    @Test
    public void getMatchTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();
        UserAppRelation rel = createUserAppRelation();

        ObjectNode node = apiJson.createJsonFromUserAppRelation(rel);

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(true);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);
        when(apiGeneralController.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString())).thenReturn(rel);
        when(apiGeneralController.createJsonFromUserAppRelation(rel)).thenReturn(node);

        ResponseEntity res = api.get(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void addInvalidIdTest(){
        IoTApp app = createApp();
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.add("1", app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addUserIsNotAuthTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.add(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId");
    }

    @Test
    public void addUserHasAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(true);

        ResponseEntity res = api.add(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user already has this app");
    }

    @Test
    public void addMatchTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(false);

        ResponseEntity res = api.add(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "app added successfully");
    }

    @Test
    public void addConsensesInvalidIdTest(){
        User user = createUser(Role.SUBJECT);
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.addConsenses(user.getId().toString(), "1", "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addConsensesSubjectTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.addConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You must be a controller/DPO");
    }

    @Test
    public void addConsensesUserHasNotAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.addConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be connected with the app");
    }

    @Test
    public void addConsenesInvalidJsonTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();
        UserAppRelation rel = createUserAppRelation();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString())).thenReturn(rel);

        ResponseEntity res = api.addConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void deleteInvalidIdTest(){
        IoTApp app = createApp();
        when(apiGeneralController.getUserFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.delete("1", app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteUserIsNotAuthTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(user.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(false);

        ResponseEntity res = api.delete(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId");
    }

    @Test
    public void deleteUserHsNotAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(user.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.userHasApp(user, app)).thenReturn(false);

        ResponseEntity res = api.delete(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user doesn't have this app");
    }

    @Test
    public void deleteMatchTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAppFromId(user.getId().toString())).thenReturn(app);
        when(apiGeneralController.isAuthenticatedUserId(user.getId().toString())).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(dataBaseService.deleteAppFromUser(any(), any())).thenReturn(true);

        ResponseEntity res = api.delete(user.getId().toString(), app.getId().toString());
        assertEquals(res.getBody().toString(), "app removed successfully");
        assertEquals(res.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void removeAllConsensesInvalidIdTest(){
        User user = createUser(Role.SUBJECT);

        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.removeAllConsenses(user.getId().toString(), "1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
    
    @Test
    public void removeAllConsensesSubjectTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.removeAllConsenses(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You must be a controller/DPO");
    }

    @Test
    public void removeAllConsensesUserHasNotAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.removeAllConsenses(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be connected with the app");
    }

    @Test
    public void removeAllConsensesMatchTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();
        UserAppRelation rel = createUserAppRelation();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString())).thenReturn(rel);

        ResponseEntity res = api.removeAllConsenses(user.getId().toString(), app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "consenses removed successfully");
    }

    @Test
    public void removeConsensesInvalidIdTest(){
        User user = createUser(Role.SUBJECT);
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.removeConsenses(user.getId().toString(), "1", "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void removeConsensesSubjectTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.removeConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You must be a controller/DPO");
    }

    @Test
    public void removeConsensesUserHasNotAppTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.removeConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be connected with the app");
    }

    @Test
    public void removeConsenesInvalidJsonTest(){
        User user = createUser(Role.SUBJECT);
        IoTApp app = createApp();
        UserAppRelation rel = createUserAppRelation();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString())).thenReturn(rel);

        ResponseEntity res = api.removeConsenses(user.getId().toString(), app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }
}
