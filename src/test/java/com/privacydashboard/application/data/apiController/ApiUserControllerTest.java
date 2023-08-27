package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;

import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.data.service.UserRepository;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.microsoft.schemas.office.office.UserdrawnAttribute;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;

public class ApiUserControllerTest{

    private DataBaseService databaseService;
    private UserDetailsServiceImpl userDetailsService;
    private ApiGeneralController apiGeneralController;
    private ApiGeneralController apiGeneralControllerJson;
    private ApiUserController api;

    private ObjectMapper mapper = new ObjectMapper();


    private User createUser(Role role){
        User user = new User();
        user.setName("User");
        user.setId(new UUID(0, role.hashCode()));
        user.setHashedPassword("HashedPassword");
        user.setMail("test"+role.toString()+"@mail.test");
        user.setRole(role);

        return user;
    }

    private List<User> getContacts(){
        User user1 = new User();
        user1.setName("Contact1");
        user1.setId(new UUID(0, 1));
        user1.setMail("contact1@mail.test");
        user1.setRole(Role.DPO);
        user1.setHashedPassword("Contact1Password");

        User user2 = new User();
        user2.setName("Contact2");
        user2.setId(new UUID(0, 2));
        user2.setMail("contact2@mail.test");
        user2.setRole(Role.CONTROLLER);
        user2.setHashedPassword("Contact2Password");

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        return list;
    }

    private List<IoTApp> getApps(){
        IoTApp app1 = new IoTApp();
        app1.setId(new UUID(1, 0));
        app1.setName("App1");
        app1.setDescription("Description1");
        app1.setQuestionnaireVote(QuestionnaireVote.GREEN);

        IoTApp app2 = new IoTApp();
        app2.setId(new UUID(1, 1));
        app2.setName("App2");
        app2.setDescription("Description2");
        app2.setQuestionnaireVote(QuestionnaireVote.ORANGE);

        List<IoTApp> list = new ArrayList<>();
        list.add(app1);
        list.add(app2);

        return list;
    }

    private Field getDatabaseService(){
        try{
            Field field = ApiUserController.class.getDeclaredField("dataBaseService");
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
            Field field = ApiUserController.class.getDeclaredField("apiGeneralController");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private Field getUserDetailsServiceImpl(){
        try{
            Field field = ApiUserController.class.getDeclaredField("userDetailsServiceImpl");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @BeforeEach
    private void setup(){
        apiGeneralController = mock(ApiGeneralController.class);
        databaseService = mock(DataBaseService.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        apiGeneralControllerJson = new ApiGeneralController();
        api = new ApiUserController();

        try{
            getDatabaseService().set(api, databaseService);
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

        try{
            getUserDetailsServiceImpl().set(api, userDetailsService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
    }


    @Test
    public void getIdNotValidTest(){
        when(apiGeneralController.getUserFromId(any())).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.get("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST); 
    }

    @Test
    public void getIdValidTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.createJsonFromUser(any())).thenReturn(apiGeneralControllerJson.createJsonFromUser(createUser(Role.SUBJECT)));
        ResponseEntity res = api.get(createUser(Role.SUBJECT).getId().toString());
        ObjectNode body = (ObjectNode) res.getBody();

        ObjectNode test = apiGeneralControllerJson.createJsonFromUser(createUser(Role.SUBJECT));
        test.remove("mail");
        test.remove("hashedPassword");
    
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    
    }

    @Test
    public void getAllContactsIdNotValidTest(){
        when(apiGeneralController.getUserFromId(any())).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.getAllContacts("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllContactsUserNotAuthenticatedTest(){
        when(apiGeneralController.getUserFromId(any())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.isAuthenticatedUserId(any())).thenReturn(false);
        ResponseEntity res = api.getAllContacts(createUser(Role.SUBJECT).getId().toString());

        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId");
    }

    @Test
    public void getAllContactsIdValidTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.isAuthenticatedUserId(createUser(Role.SUBJECT).getId().toString())).thenReturn(true);
        when(databaseService.getAllContactsFromUser(createUser(Role.SUBJECT))).thenReturn(getContacts());
        when(apiGeneralController.createJsonFromUser(getContacts().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromUser(getContacts().get(0)));
        when(apiGeneralController.createJsonFromUser(getContacts().get(1))).thenReturn(apiGeneralControllerJson.createJsonFromUser(getContacts().get(1)));

        ResponseEntity res = api.getAllContacts(createUser(Role.SUBJECT).getId().toString());
        ArrayNode body = (ArrayNode) res.getBody();

        ObjectNode test1 = apiGeneralControllerJson.createJsonFromUser(getContacts().get(0));
        test1.remove("hashedPassword");
        ObjectNode test2 = apiGeneralControllerJson.createJsonFromUser(getContacts().get(1));
        test2.remove("hashedPassword");

        ArrayNode test = mapper.createArrayNode();
        test.add(test1);
        test.add(test2);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getAppsIdNotValidTest(){
        when(apiGeneralController.getUserFromId(any())).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.getApps("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAppsUserNotAuthenticatedTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.getApps(createUser(Role.SUBJECT).getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAppsSubjectTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        ResponseEntity res = api.getApps(createUser(Role.SUBJECT).getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "User must be Controller/DPO or you must be associated with that user");
    }

    @Test
    public void getAppsUserControllerTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.CONTROLLER).getId().toString())).thenReturn(createUser(Role.CONTROLLER));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));
        when(databaseService.getUserApps(createUser(Role.CONTROLLER))).thenReturn(getApps());
        when(apiGeneralController.createJsonFromApp(getApps().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(0)));
        when(apiGeneralController.createJsonFromApp(getApps().get(1))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(1)));
        ResponseEntity res = api.getApps(createUser(Role.CONTROLLER).getId().toString());

        ArrayNode body = (ArrayNode) res.getBody();

        ObjectNode test1 = apiGeneralControllerJson.createJsonFromApp(getApps().get(0));
        ObjectNode test2 = apiGeneralControllerJson.createJsonFromApp(getApps().get(1));

        ArrayNode test = mapper.createArrayNode();
        test.add(test1);
        test.add(test2);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getAppsUserDPOTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.DPO).getId().toString())).thenReturn(createUser(Role.DPO));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));
        when(databaseService.getUserApps(createUser(Role.DPO))).thenReturn(getApps());
        when(apiGeneralController.createJsonFromApp(getApps().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(0)));
        when(apiGeneralController.createJsonFromApp(getApps().get(1))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(1)));
        ResponseEntity res = api.getApps(createUser(Role.DPO).getId().toString());

        ArrayNode body = (ArrayNode) res.getBody();

        ObjectNode test1 = apiGeneralControllerJson.createJsonFromApp(getApps().get(0));
        ObjectNode test2 = apiGeneralControllerJson.createJsonFromApp(getApps().get(1));

        ArrayNode test = mapper.createArrayNode();
        test.add(test1);
        test.add(test2);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getAppsAuthenticatedControllerTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.CONTROLLER));
        when(databaseService.getAppsFrom2Users(createUser(Role.SUBJECT), createUser(Role.CONTROLLER))).thenReturn(getApps());
        when(apiGeneralController.createJsonFromApp(getApps().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(0)));
        when(apiGeneralController.createJsonFromApp(getApps().get(1))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(1)));
        ResponseEntity res = api.getApps(createUser(Role.SUBJECT).getId().toString());

        ArrayNode body = (ArrayNode) res.getBody();

        ObjectNode test1 = apiGeneralControllerJson.createJsonFromApp(getApps().get(0));
        ObjectNode test2 = apiGeneralControllerJson.createJsonFromApp(getApps().get(1));

        ArrayNode test = mapper.createArrayNode();
        test.add(test1);
        test.add(test2);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getAppsAuthenticatedDPOTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));
        when(databaseService.getAppsFrom2Users(createUser(Role.SUBJECT), createUser(Role.DPO))).thenReturn(getApps());
        when(apiGeneralController.createJsonFromApp(getApps().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(0)));
        when(apiGeneralController.createJsonFromApp(getApps().get(1))).thenReturn(apiGeneralControllerJson.createJsonFromApp(getApps().get(1)));
        ResponseEntity res = api.getApps(createUser(Role.SUBJECT).getId().toString());

        ArrayNode body = (ArrayNode) res.getBody();

        ObjectNode test1 = apiGeneralControllerJson.createJsonFromApp(getApps().get(0));
        ObjectNode test2 = apiGeneralControllerJson.createJsonFromApp(getApps().get(1));

        ArrayNode test = mapper.createArrayNode();
        test.add(test1);
        test.add(test2);

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getDetailedIdNotValidTest(){
        when(apiGeneralController.getUserFromId(any())).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.getDetailed("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getDetailedUserNotAuthenticatedTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenThrow(IllegalArgumentException.class);
        ResponseEntity res = api.getDetailed(createUser(Role.SUBJECT).getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getDetailedAuthUserIsParamUserTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.createJsonFromUser(createUser(Role.SUBJECT))).thenReturn(apiGeneralControllerJson.createJsonFromUser(createUser(Role.SUBJECT)));
        ResponseEntity res = api.getDetailed(createUser(Role.SUBJECT).getId().toString());

        ObjectNode body = (ObjectNode) res.getBody();

        ObjectNode test = apiGeneralControllerJson.createJsonFromUser(createUser(Role.SUBJECT));
        test.remove("hashedPassword");

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getDetailedParamUserIsAContactOfAuthUserTest(){
        when(apiGeneralController.getUserFromId(getContacts().get(0).getId().toString())).thenReturn(getContacts().get(0));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(databaseService.getAllContactsFromUser(createUser(Role.SUBJECT))).thenReturn(getContacts());
        when(apiGeneralController.createJsonFromUser(getContacts().get(0))).thenReturn(apiGeneralControllerJson.createJsonFromUser(getContacts().get(0)));
        ResponseEntity res = api.getDetailed(getContacts().get(0).getId().toString());

        ObjectNode body = (ObjectNode) res.getBody();

        ObjectNode test = apiGeneralControllerJson.createJsonFromUser(getContacts().get(0));
        test.remove("hashedPassword");

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getDetailedParamUserIsControllerTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.CONTROLLER).getId().toString())).thenReturn(createUser(Role.CONTROLLER));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(databaseService.getAllContactsFromUser(createUser(Role.SUBJECT))).thenReturn(getContacts());
        when(apiGeneralController.createJsonFromUser(createUser(Role.CONTROLLER))).thenReturn(apiGeneralControllerJson.createJsonFromUser(createUser(Role.CONTROLLER)));

        ResponseEntity res = api.getDetailed(createUser(Role.CONTROLLER).getId().toString());

        ObjectNode body = (ObjectNode) res.getBody();

        ObjectNode test = apiGeneralControllerJson.createJsonFromUser(createUser(Role.CONTROLLER));
        test.remove("hashedPassword");

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getDetailedParamUserIsDPOTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.DPO).getId().toString())).thenReturn(createUser(Role.DPO));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.SUBJECT));
        when(databaseService.getAllContactsFromUser(createUser(Role.SUBJECT))).thenReturn(getContacts());
        when(apiGeneralController.createJsonFromUser(createUser(Role.DPO))).thenReturn(apiGeneralControllerJson.createJsonFromUser(createUser(Role.DPO)));

        ResponseEntity res = api.getDetailed(createUser(Role.DPO).getId().toString());

        ObjectNode body = (ObjectNode) res.getBody();

        ObjectNode test = apiGeneralControllerJson.createJsonFromUser(createUser(Role.DPO));
        test.remove("hashedPassword");

        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(body, test);
    }

    @Test
    public void getDetailedParamUserIsSubjectTest(){
        when(apiGeneralController.getUserFromId(createUser(Role.SUBJECT).getId().toString())).thenReturn(createUser(Role.SUBJECT));
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.CONTROLLER));
        when(databaseService.getAllContactsFromUser(createUser(Role.CONTROLLER))).thenReturn(getContacts());
        ResponseEntity res = api.getDetailed(createUser(Role.SUBJECT).getId().toString());

        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "you must be the user identified by the userId");
    }

   
}