package com.privacydashboard.application.data.apiController;

import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.h2.command.ddl.CreateLinkedTable;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiAppContollerTest {
    
    private ApiGeneralController apiGeneralController;
    private DataBaseService dataBaseService;

    private ApiGeneralController apiJson;
    private ApiAppController api;

    private Field getApiGeneralController(){
        try{
            Field field = ApiAppController.class.getDeclaredField("apiGeneralController");
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
            Field field = ApiAppController.class.getDeclaredField("dataBaseService");
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
        app.setName("AppName");
        app.setId(new UUID(2, 0));
        app.setDescription("AppDescription");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private List<User> createUserList(Role role){
        User user1 = new User();
        user1.setRole(role);
        user1.setHashedPassword("Pass1");
        user1.setId(new UUID(0, role.hashCode()));
        user1.setName("User1");

        User user2 = new User();
        user2.setRole(role);
        user2.setHashedPassword("Pass2");
        user2.setId(new UUID(0, role.hashCode()+2));
        user2.setName("User2");

        return List.of(user1, user2);
    }

    @BeforeEach
    public void setup(){
        apiGeneralController = mock(ApiGeneralController.class);
        dataBaseService = mock(DataBaseService.class);

        api = new ApiAppController();
        apiJson = new ApiGeneralController();

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
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.get("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getMatchTest(){
        IoTApp app = createApp();
        ObjectNode node = apiJson.createJsonFromApp(app);

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.createJsonFromApp(app)).thenReturn(node);

        ResponseEntity res = api.get(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), node);
    }

    @Test
    public void getControllersInvalidIdTest(){
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getControllers("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getControllersMatchTest(){
        IoTApp app = createApp();
        List<User> list = createUserList(Role.CONTROLLER);

        ArrayNode array = new ObjectMapper().createArrayNode();
        ObjectNode user1 = apiJson.createJsonFromUser(list.get(0));
        ObjectNode user2 = apiJson.createJsonFromUser(list.get(1));

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(dataBaseService.getControllersFromApp(app)).thenReturn(list);
        when(apiGeneralController.createJsonFromUser(list.get(0))).thenReturn(user1);
        when(apiGeneralController.createJsonFromUser(list.get(1))).thenReturn(user2);

        user1.remove("hashedPassword");
        user2.remove("hashedPassword");
        array.add(user1);
        array.add(user2);

        ResponseEntity res = api.getControllers(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getDPOsInvalidIdTest(){
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getDPOs("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getDPOsMatchTest(){
        IoTApp app = createApp();
        List<User> list = createUserList(Role.DPO);

        ArrayNode array = new ObjectMapper().createArrayNode();
        ObjectNode user1 = apiJson.createJsonFromUser(list.get(0));
        ObjectNode user2 = apiJson.createJsonFromUser(list.get(1));

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(dataBaseService.getDPOsFromApp(app)).thenReturn(list);
        when(apiGeneralController.createJsonFromUser(list.get(0))).thenReturn(user1);
        when(apiGeneralController.createJsonFromUser(list.get(1))).thenReturn(user2);

        user1.remove("hashedPassword");
        user2.remove("hashedPassword");
        array.add(user1);
        array.add(user2);

        ResponseEntity res = api.getDPOs(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void getSubjectsInvalidIdTest(){
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.getSubjects("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getSubjectsSubjectTest(){
        IoTApp app = createApp();
        User user = createUserList(Role.SUBJECT).get(0);

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.getSubjects(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You MUST be the controller/DPO of that app");
    }

    @Test
    public void getSubjectsMatchTest(){
        IoTApp app = createApp();
        List<User> list = createUserList(Role.SUBJECT);

        ArrayNode array = new ObjectMapper().createArrayNode();
        ObjectNode user1 = apiJson.createJsonFromUser(list.get(0));
        ObjectNode user2 = apiJson.createJsonFromUser(list.get(1));

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(list.get(0));
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(dataBaseService.getSubjectsFromApp(app)).thenReturn(list);
        when(apiGeneralController.createJsonFromUser(list.get(0))).thenReturn(user1);
        when(apiGeneralController.createJsonFromUser(list.get(1))).thenReturn(user2);

        user1.remove("hashedPassword");
        user2.remove("hashedPassword");
        array.add(user1);
        array.add(user2);

        ResponseEntity res = api.getSubjects(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), array);
    }

    @Test
    public void addSubjectTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user is not DPO or Controller");
    }

    @Test
    public void addInvalidJSONTest(){
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(true, "Body")).thenThrow(IOException.class);
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
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(true, "Body")).thenThrow(IllegalArgumentException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }


        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void addAppAlreadyExistTest(){
        IoTApp app = createApp();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(anyBoolean(), any())).thenReturn(app);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));        

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "cannot create this app");
    }

    @Test
    public void addMatchTest(){
        IoTApp app = createApp();

        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(anyBoolean(), any())).thenReturn(app);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        when(dataBaseService.getApp(app.getId())).thenReturn(Optional.empty());     
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUserList(Role.SUBJECT).get(0));   

        ResponseEntity res = api.add("Body");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "app created successfully");
    }

    @Test
    public void deleteInvalidIdTest(){
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.delete("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteUserHasNotAppTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.delete(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user not associated with the app");
    }

    @Test
    public void deleteSubjectTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.delete(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user is not a controller or DPO");
    }

    @Test
    public void deleteMatchTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);

        ResponseEntity res = api.delete(app.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "app deleted successfully");
    }

    @Test
    public void updateInvalidIdTest(){
        when(apiGeneralController.getAppFromId("1")).thenThrow(IllegalArgumentException.class);

        ResponseEntity res = api.update("1", "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateUserHasNotAppTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(false);

        ResponseEntity res = api.update(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user not associated with the app");
    }

    @Test
    public void updateSubjectTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(false);

        ResponseEntity res = api.update(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "user is not a controller or DPO");
    }

    @Test
    public void updateInvalidJSONTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(anyBoolean(), any())).thenThrow(IOException.class);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        ResponseEntity res = api.update(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid JSON");
    }

    @Test
    public void updateMatchTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.userHasApp(any(), any())).thenReturn(true);
        when(apiGeneralController.isControllerOrDpo(true, null)).thenReturn(true);
        try{
            when(apiGeneralController.getAppFromJsonString(anyBoolean(), any())).thenReturn(app);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        ResponseEntity res = api.update(app.getId().toString(), "Body");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody().toString(), "app updated successfully");
    }
}
