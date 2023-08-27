package com.privacydashboard.application.data.apiController;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.h2.command.ddl.CreateRole;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.UserDetailsServiceImpl;

public class ApiRightRequestContollerTest {
    
    private DataBaseService dataBaseService;
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private ApiGeneralController apiGeneralController;
    private ApiGeneralController apiJson;

    private ApiRightRequestController api;

    private Field getDataBaseService(){
        try{
            Field field = ApiRightRequestController.class.getDeclaredField("dataBaseService");
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
            Field field = ApiRightRequestController.class.getDeclaredField("userDetailsServiceImpl");
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
            Field field = ApiRightRequestController.class.getDeclaredField("apiGeneralController");
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
        user.setRole(role);
        user.setName(role.toString()+"User");
        user.setId(new UUID(0, role.hashCode()));

        return user;
    }

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setId(new UUID(0, 1));
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

        return req;
    }

    private List<RightRequest> createRequestList(){
        RightRequest req = new RightRequest();
        req.setId(new UUID(0, 0));
        req.setSender(createUser(Role.SUBJECT));
        req.setReceiver(createUser(Role.CONTROLLER));
        req.setApp(createApp());
        req.setRightType(RightType.COMPLAIN);
        req.setHandled(true);

        RightRequest req1 = new RightRequest();
        req1.setId(new UUID(0, 1));
        req1.setSender(createUser(Role.DPO));
        req1.setReceiver(createUser(Role.SUBJECT));
        req1.setApp(createApp());
        req1.setRightType(RightType.INFO);
        req.setHandled(true);

        RightRequest req2 = new RightRequest();
        req2.setId(new UUID(0, 2));
        req2.setSender(createUser(Role.CONTROLLER));
        req2.setReceiver(createUser(Role.SUBJECT));
        req2.setApp(createApp());
        req2.setRightType(RightType.INFO);
        req.setHandled(false);

        return List.of(req, req1, req2);
    }

    @BeforeEach
    public void setup(){
        dataBaseService = mock(DataBaseService.class);
        userDetailsServiceImpl = mock(UserDetailsServiceImpl.class);
        apiGeneralController = mock(ApiGeneralController.class);

        apiJson = new ApiGeneralController();

        api = new ApiRightRequestController();

        try{
            getDataBaseService().set(api, dataBaseService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        try{
            getUserDetailsServiceImpl().set(api, userDetailsServiceImpl);
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
        IllegalArgumentException ex = new IllegalArgumentException("invalid ID");
        when(apiGeneralController.getRequestFromId("1")).thenThrow(ex);
        ResponseEntity res = api.get("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid ID");
    }

    @Test
    public void getIdAuthIsNotSenderOrReceiverTest(){
        RightRequest req = createRequest(LocalDateTime.now());

        when(apiGeneralController.getRequestFromId(req.getId().toString())).thenReturn(req);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(createUser(Role.DPO));

        ResponseEntity res = api.get(req.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "You MUST be the sender or the receiver of the request");
    }

    @Test
    public void getIdAuthIsSenderTest(){
        RightRequest req = createRequest(LocalDateTime.now());
        User user = createUser(Role.SUBJECT);

        when(apiGeneralController.getRequestFromId(req.getId().toString())).thenReturn(req);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.createJsonFromRequest(req)).thenReturn(apiJson.createJsonFromRequest(req));

        ResponseEntity res = api.get(req.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), apiJson.createJsonFromRequest(req));
    }

    @Test
    public void getIdAuthIsReceiverTest(){
        RightRequest req = createRequest(LocalDateTime.now());
        User user = createUser(Role.CONTROLLER);

        when(apiGeneralController.getRequestFromId(req.getId().toString())).thenReturn(req);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.createJsonFromRequest(req)).thenReturn(apiJson.createJsonFromRequest(req));

        ResponseEntity res = api.get(req.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), apiJson.createJsonFromRequest(req));
    }

    @Test
    public void getAllFromUserNullUserTest(){
        IllegalArgumentException e = new IllegalArgumentException("invalid ID");
        when(apiGeneralController.getUserFromId("1")).thenThrow(e);

        ResponseEntity res = api.getAllFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid ID");
    }

    @Test
    public void getAllFromUserMatchTest(){
        User user = createUser(Role.SUBJECT);
        User authUser = createUser(Role.CONTROLLER);
        List<RightRequest> list = createRequestList();
        ArrayNode test = apiJson.createJsonRequestCheckAuthorizedUser(list, authUser);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(authUser);
        when(dataBaseService.getAllRequestsFromSender(user)).thenReturn(List.of(list.get(0)));
        when(dataBaseService.getAllRequestsFromReceiver(user)).thenReturn(List.of(list.get(1), list.get(2)));
        when(apiGeneralController.createJsonRequestCheckAuthorizedUser(any(), any())).thenReturn(apiJson.createJsonRequestCheckAuthorizedUser(list, authUser));

        ResponseEntity res = api.getAllFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getHandledFromUserNullUserTest(){
        IllegalArgumentException e = new IllegalArgumentException("invalid ID");
        when(apiGeneralController.getUserFromId("1")).thenThrow(e);

        ResponseEntity res = api.getHandledFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid ID");
    }

    @Test
    public void getHandledFromUserMatchTest(){
        User user = createUser(Role.SUBJECT);
        User authUser = createUser(Role.CONTROLLER);
        List<RightRequest> list = List.of(createRequestList().get(0), createRequestList().get(1));
        ArrayNode test = apiJson.createJsonRequestCheckAuthorizedUser(list, authUser);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(authUser);
        when(dataBaseService.getHandledRequestsFromSender(user)).thenReturn(List.of(list.get(0)));
        when(dataBaseService.getHandledRequestsFromReceiver(user)).thenReturn(List.of(list.get(1)));
        when(apiGeneralController.createJsonRequestCheckAuthorizedUser(any(), any())).thenReturn(apiJson.createJsonRequestCheckAuthorizedUser(list, authUser));

        ResponseEntity res = api.getHandledFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getNotHandledFromUserNullUserTest(){
        IllegalArgumentException e = new IllegalArgumentException("invalid ID");
        when(apiGeneralController.getUserFromId("1")).thenThrow(e);

        ResponseEntity res = api.getNotHandledFromUser("1");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid ID");
    }

    @Test
    public void getNotHandledFromUserMatchTest(){
        User user = createUser(Role.SUBJECT);
        User authUser = createUser(Role.CONTROLLER);
        List<RightRequest> list = List.of(createRequestList().get(2));
        ArrayNode test = apiJson.createJsonRequestCheckAuthorizedUser(list, authUser);

        when(apiGeneralController.getUserFromId(user.getId().toString())).thenReturn(user);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(authUser);
        when(dataBaseService.getPendingRequestsFromSender(user)).thenReturn(List.of());
        when(dataBaseService.getPendingRequestsFromReceiver(user)).thenReturn(List.of(list.get(0)));
        when(apiGeneralController.createJsonRequestCheckAuthorizedUser(any(), any())).thenReturn(apiJson.createJsonRequestCheckAuthorizedUser(list, authUser));

        ResponseEntity res = api.getNotHandledFromUser(user.getId().toString());
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getFromAppInvalidAppIDTest(){
        IllegalArgumentException e = new IllegalArgumentException("invalid ID");
        when(apiGeneralController.getAppFromId("1")).thenThrow(e);
        ResponseEntity res = api.getFromApp("1", null);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid ID");
    }

    @Test
    public void getFromAppInvalidHandledTest(){
        IoTApp app = createApp();

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);

        ResponseEntity res = api.getFromApp(app.getId().toString(), "NotHandled");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid isHandled parameter");
    }

    @Test
    public void getFromAppAllTest(){
        IoTApp app = createApp();
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = createRequestList();
        ArrayNode test = apiJson.createJsonRequestOfApp(list, app);

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.createJsonRequestOfApp(any(), any())).thenReturn(apiJson.createJsonRequestOfApp(list, app));
        when(dataBaseService.getAllRequestsFromReceiver(user)).thenReturn(List.of(list.get(1), list.get(2)));
        when(dataBaseService.getAllRequestsFromSender(user)).thenReturn(List.of(list.get(0)));

        ResponseEntity res = api.getFromApp(app.getId().toString(), null);
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getFromAppHandledTest(){
        IoTApp app = createApp();
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = List.of(createRequestList().get(0), createRequestList().get(1));
        ArrayNode test = apiJson.createJsonRequestOfApp(list, app);

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(apiGeneralController.createJsonRequestOfApp(any(), any())).thenReturn(apiJson.createJsonRequestOfApp(list, app));
        when(dataBaseService.getHandledRequestsFromReceiver(user)).thenReturn(List.of(list.get(1)));
        when(dataBaseService.getHandledRequestsFromSender(user)).thenReturn(List.of(list.get(0)));

        ResponseEntity res = api.getFromApp(app.getId().toString(), "true");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getFromAppNotHandledTest(){
        IoTApp app = createApp();
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = List.of(createRequestList().get(2));
        ArrayNode test = apiJson.createJsonRequestOfApp(list, app);

        when(apiGeneralController.getAppFromId(app.getId().toString())).thenReturn(app);
        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getPendingRequestsFromReceiver(user)).thenReturn(list);
        when(dataBaseService.getPendingRequestsFromSender(user)).thenReturn(List.of());
        when(apiGeneralController.createJsonRequestOfApp(any(), any())).thenReturn(apiJson.createJsonRequestOfApp(list, app));

        ResponseEntity res = api.getFromApp(app.getId().toString(), "false");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getFromRightTypeInvalidHandledTest(){
        ResponseEntity res = api.getFromRightType("COMPLAIN", "NotHandled");
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid isHandled parameter");
    }

    @Test
    public void getFromRightTypeInvalidRightTypeTest(){
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = createRequestList();
        IllegalArgumentException e = new IllegalArgumentException("invalid Right Type");

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getAllRequestsFromReceiver(user)).thenReturn(List.of(list.get(1), list.get(2)));
        when(dataBaseService.getAllRequestsFromSender(user)).thenReturn(List.of(list.get(0)));
        when(apiGeneralController.createJsonRequestOfRightType(any(), any())).thenThrow(e);
        
        ResponseEntity res = api.getFromRightType("NotRightType", null);
        assertEquals(res.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(res.getBody().toString(), "invalid Right Type");
    }

    @Test
    public void getFromRightTypeHandledMatchTest(){
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = createRequestList();
        ArrayNode test = apiJson.createJsonRequestOfRightType(List.of(list.get(1)), "INFO");

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getHandledRequestsFromReceiver(user)).thenReturn(List.of(list.get(1)));
        when(dataBaseService.getHandledRequestsFromSender(user)).thenReturn(List.of(list.get(0)));
        when(apiGeneralController.createJsonRequestOfRightType(any(), any())).thenReturn(apiJson.createJsonRequestOfRightType(List.of(list.get(1)), "INFO"));

        ResponseEntity res = api.getFromRightType("INFO", "true");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }

    @Test
    public void getFromRightTypeNotHandledMatchTest(){
        User user = createUser(Role.SUBJECT);
        List<RightRequest> list = createRequestList();
        ArrayNode test = apiJson.createJsonRequestOfRightType(List.of(list.get(2)), "INFO");

        when(apiGeneralController.getAuthenicatedUser()).thenReturn(user);
        when(dataBaseService.getPendingRequestsFromReceiver(user)).thenReturn(List.of(list.get(2)));
        when(dataBaseService.getPendingRequestsFromSender(user)).thenReturn(List.of());
        when(apiGeneralController.createJsonRequestOfRightType(any(), any())).thenReturn(apiJson.createJsonRequestOfRightType(List.of(list.get(2)), "INFO"));

        ResponseEntity res = api.getFromRightType("INFO", "false");
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), test);
    }
}
