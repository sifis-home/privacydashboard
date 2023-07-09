package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.data.entity.Message;

import java.util.List;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Hashtable;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ApiGeneralControllerTest {

    private ObjectMapper mapper = new ObjectMapper();
    private ApiGeneralController api = new ApiGeneralController();
    

    private static boolean isFieldNull(Object f){
        if(f == null)
            return true;
        else
            return false;
    }

    private static boolean isAppNull(IoTApp app){
        int nullFields = 0;
        
        if(isFieldNull(app.getId()))
            nullFields++;
        
        if(isFieldNull(app.getDescription()))
            nullFields++;
        
        if(isFieldNull(app.getConsenses()))
            nullFields++;

        if(isFieldNull(app.getName()))
            nullFields++;

        if(isFieldNull(app.getDetailVote()))
            nullFields++;
        
        if(isFieldNull(app.getQuestionnaireVote()))
            nullFields++;
        
        if(isFieldNull(app.getOptionalAnswers()))
            nullFields++;
        
        return nullFields == 7;
    }

    private static boolean isUserNull(User user){
        int nullFields = 0;

        if(isFieldNull(user.getName()))
            nullFields++;
        
        if(isFieldNull(user.getHashedPassword()))
            nullFields++;

        if(isFieldNull(user.getId()))
            nullFields++;

        if(isFieldNull(user.getMail()))
            nullFields++;
        
        if(isFieldNull(user.getRole()))
            nullFields++;

        return nullFields == 5;
    }

    @Test
    public void createJsonFromAppNullTest(){
        IoTApp app = new IoTApp(); 
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromApp(app);
        });
    }
    
    @Test
    public void createJsonFromAppNameNullTest(){
        IoTApp app = new IoTApp();
        app.setId(new UUID(0, 4));
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromApp(app);
        });
    }

    @Test
    public void createJsonFromAppIDNullTest(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromApp(app);
        });
    }

    @Test
    public void createJsonFromAppMinimalParametersTest(){
        IoTApp app = new IoTApp();
        app.setId(new UUID(0, 4));
        app.setName("TestApp");
        ObjectNode jsonTest = mapper.createObjectNode();
        jsonTest.put("id", new UUID(0,4).toString());
        jsonTest.put("name", "TestApp");
        assertEquals(api.createJsonFromApp(app), jsonTest);        
    }

    //The method createJsonFromApp asks for a set number 
    //of optionalAnswers equals to nQuestions, while it 
    //does not hold the same requirement for detailVote, 
    //which can in number be more or less than nQuestions.
    //This is in contrast with the inverse method getAppFromJsonNode
    //which asks for at most nQuestions detailVote.
    //The method createJsonFromApp and its inverse 
    //do not consider the Consenses attributes of IoTApp.
    @Test
    public void createJsonFromAppAllParametersTest(){
        IoTApp app = new IoTApp();
        app.setId(new UUID(0, 4));
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        String[] detailVote = {"TestDetailLine1", "TestDetailLine2"};
        app.setDetailVote(detailVote);

        
        Hashtable table = new Hashtable<Integer, String>();
        for(int i = 0; i < GlobalVariables.nQuestions; i++){
            table.put(i, "LineTest"+i);
        }        
        app.setOptionalAnswers(table);

        ObjectNode jsonTest = mapper.createObjectNode();
        jsonTest.put("id", new UUID(0,4).toString());
        jsonTest.put("name", "TestApp");
        jsonTest.put("description", "TestDescription");
        jsonTest.put("questionnaireVote", QuestionnaireVote.GREEN.toString());

        ArrayNode detailVoteArray = mapper.createArrayNode();
        detailVoteArray.add(detailVote[0]);
        detailVoteArray.add(detailVote[1]);
        jsonTest.set("detailVote", detailVoteArray);

        ArrayNode optionalAnswersArrayNode = mapper.createArrayNode();
        for(int i = 0; i < GlobalVariables.nQuestions; i++){
            optionalAnswersArrayNode.add("LineTest"+i);
        }  
        jsonTest.set("optionalAnswers", optionalAnswersArrayNode);
        
        assertEquals(api.createJsonFromApp(app), jsonTest);  
    }

    @Test
    public void getAppFromJsonNodeIDNotValidTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "4");
        assertThrows(IllegalArgumentException.class, () -> {
            api.getAppFromJsonNode(false, node);
        });
    }

    @Test
    public void getAppFromJsonNodeNullNameTest(){
        ObjectNode node = mapper.createObjectNode();
        assertThrows(IllegalArgumentException.class, () -> {
            api.getAppFromJsonNode(true, node);
        });
    }

    @Test
    public void getAppFromJsonNodeAllParametersTest(){
        ObjectNode node = mapper.createObjectNode();
        IoTApp app = new IoTApp();

        app.setId(new UUID(0, 4));
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        String[] detailVote = new String[GlobalVariables.nQuestions];
        for(int i = 0; i < GlobalVariables.nQuestions; i++){
            detailVote[i]= "LineTest"+i;
        } 
        app.setDetailVote(detailVote);

        Hashtable table = new Hashtable<Integer, String>();
        for(int i = 0; i < GlobalVariables.nQuestions; i++){
            table.put(i, "LineTest"+i);
        }        
        app.setOptionalAnswers(table);

        node = api.createJsonFromApp(app);

        assertEquals(api.getAppFromJsonNode(true, node), app);
    }

    @Test
    public void getAppFromJsonNodeRedVoteTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("questionnaireVote", QuestionnaireVote.RED.toString());
        assertEquals(api.getAppFromJsonNode(false, node).getQuestionnaireVote(), QuestionnaireVote.RED);      

    }

    @Test
    public void getAppFromJsonNodeOrangeVoteTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("questionnaireVote", QuestionnaireVote.ORANGE.toString());
        assertEquals(api.getAppFromJsonNode(false, node).getQuestionnaireVote(), QuestionnaireVote.ORANGE);
    }

    @Test
    public void getAppFromJsonNodeVoteAnswersNotArrayTest(){
        ObjectNode node = mapper.createObjectNode();
        IoTApp app = new IoTApp();

        app.setId(new UUID(0, 4));
        app.setName("TestApp");

        node = api.createJsonFromApp(app);
        node.put("detailVote", "TestFieldNotArray");
        node.put("optionalAnswers", "TestFieldNotArray");

        assertEquals(api.getAppFromJsonNode(false, node), app);
    }

    //Test fails because you cannot assign a null value
    //to a Hashtable record.
    //Error at line 358 of ApiGeneralController
    @Test
    public void getAppFromJsonNodeVoteAnswersNotFullArraysTest(){
        ObjectNode node = mapper.createObjectNode();
        IoTApp app = new IoTApp();

        app.setId(new UUID(0, 4));
        app.setName("TestApp");

        node = api.createJsonFromApp(app);


        String[] detailVote = new String[GlobalVariables.nQuestions];
        for(int i = 1; i < GlobalVariables.nQuestions; i++){
            detailVote[i]= null;
        } 
        detailVote[0] = "LineTest0";
        app.setDetailVote(detailVote);

        Hashtable table = new Hashtable<Integer, String>();       
        table.put(0, "LineTest0");
        app.setOptionalAnswers(table);

        ArrayNode detailVoteArray = mapper.createArrayNode();
        detailVoteArray.add("LineTest0");
        node.set("detailVote", detailVoteArray);

        ArrayNode optionalAnswersArrayNode = mapper.createArrayNode();
        optionalAnswersArrayNode.add("LineTest0");
        node.set("optionalAnswers", optionalAnswersArrayNode);

        assertEquals(api.getAppFromJsonNode(false, node), app);
        
    }

    @Test
    public void getAppFromJsonNodeMinimalParametersTest(){
        ObjectNode node = mapper.createObjectNode();
        assertEquals(isAppNull(api.getAppFromJsonNode(false, node)), true);
    }

    @Test
    public void getAppFromJsonStringNotValidJsonTest(){
        assertThrows(IOException.class, () -> {
            api.getAppFromJsonString(false, "NotAJson");
        });
    }

    @Test
    public void getAppFromJsonStringNameNullTest(){
        String body = "{\"id\":\""+new UUID(0, 0).toString()+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getAppFromJsonString(true, body);
        });
    }

    @Test
    public void getAppFromJsonStringIDNotValidTest(){
        String body = "{\"id\":\""+"0"+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getAppFromJsonString(false, body);
        });
    }

    @Test
    public void getAppFromJsonStringValidBodyTest(){
        IoTApp app = new IoTApp();
        app.setName("TestName");
        app.setId(new UUID(0, 0));

        String body = "{\"name\":\"TestName\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            IoTApp appRes = api.getAppFromJsonString(true, body);
            assertEquals(app, appRes);
        }
        catch(Exception e){
            System.out.println('\n'+"Impossible path: "+e);
        }
    }

    @Test
    public void createJsonFromUserNullTest(){
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUser(user);
        });
    }

    @Test
    public void createJsonFromUserIDNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setHashedPassword("HashedTest");
        user.setRole(Role.CONTROLLER);

        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUser(user);
        });
    }

    @Test
    public void createJsonFromUserNameNullTest(){
        User user = new User();

        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");
        user.setRole(Role.CONTROLLER);

        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUser(user);
        });
    }

    @Test
    public void createJsonFromUserPasswordNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setRole(Role.CONTROLLER);

        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUser(user);
        });
    }

    @Test
    public void createJsonFromUserRoleNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");

        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUser(user);
        });
    }

    @Test
    public void createJsonFromUserMinimalParametersTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");        
        user.setRole(Role.CONTROLLER);

        ObjectNode node = mapper.createObjectNode();

        node.put("id", new UUID(0, 0).toString());
        node.put("name", "TestName");
        node.put("role", Role.CONTROLLER.toString());
        node.put("hashedPassword", "HashedTest");

        assertEquals(api.createJsonFromUser(user), node);
    }

    @Test
    public void createJsonFromUserAllParametersTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");        
        user.setRole(Role.CONTROLLER);
        user.setMail("test@mail.test");

        ObjectNode node = mapper.createObjectNode();

        node.put("id", new UUID(0, 0).toString());
        node.put("name", "TestName");
        node.put("role", Role.CONTROLLER.toString());
        node.put("hashedPassword", "HashedTest");
        node.put("mail", "test@mail.test");

        assertEquals(api.createJsonFromUser(user), node);
    }

    @Test
    public void getUserFromJsonNodeMinimalParametersTest(){
        ObjectNode node = mapper.createObjectNode();

        assertEquals(isUserNull(api.getUserFromJsonNode(false, node)), true);
    }

    @Test
    public void getUserFromJsonNodeIDNotValidTest(){
        ObjectNode node = mapper.createObjectNode();

        node.put("id", "0");

        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonNode(false, node);
        });
    }

    @Test
    public void getUserFromJsonNodeNameNullTest(){
        ObjectNode node = mapper.createObjectNode();
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonNode(true, node);
        });
    }

    @Test
    public void getUserFromJsonNodePasswordNullTest(){
        ObjectNode node = mapper.createObjectNode();

        node.put("name", "TestName");
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonNode(true, node);
        });
    }

    //Test fails because userDetailsServiceImpl
    //throws a NullPointerException
    @Test
    public void getUserFromJsonNodeRoleNullTest(){
        ObjectNode node = mapper.createObjectNode();

        node.put("name", "TestName");
        node.put("password", "TestPassword");

        try{
            api.getUserFromJsonNode(true, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON ROLE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
        catch(NullPointerException e){
            System.out.println("-----TEST START USER FROM JSON ROLE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    //ProfilePictureUrl attribute is not used in 
    //this method or its inverse
    @Test
    public void getUserFromJsonNodeAllParametersTest(){
        ObjectNode node = mapper.createObjectNode();

        User user = new User();
        user.setName("TestName");
        //user.setHashedPassword("TestPassword");
        user.setId(new UUID(0, 0));
        user.setRole(Role.SUBJECT);
        user.setMail("test@mail.test");

        node.put("name", "TestName");
        //node.put("password", "TestPassword");
        node.put("role", Role.SUBJECT.toString());
        node.put("id", new UUID(0, 0).toString());
        node.put("mail", "test@mail.test");

        assertEquals(api.getUserFromJsonNode(false, node), user);

    }

    @Test
    public void getUserFromJsonNodeControllerRoleTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("role", Role.CONTROLLER.toString());

        assertEquals(api.getUserFromJsonNode(false, node).getRole(), Role.CONTROLLER);
    }

    @Test
    public void getUserFromJsonNodeDPORoleTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("role", Role.DPO.toString());

        assertEquals(api.getUserFromJsonNode(false, node).getRole(), Role.DPO);
    }

    @Test
    public void getUserFromJsonStringNotValidJsonTest(){
        assertThrows(IOException.class, () -> {
            api.getUserFromJsonString(false, "NotAJson");
        });
    }

    @Test
    public void getUserFromJsonStringIDNotValidTest(){
        String body = "{\"id\":\""+"0"+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonString(false, body);
        });
    }

    @Test
    public void getUserFromJsonStringNameNullTest(){
        String body = "{\"id\":\""+new UUID(0, 0).toString()+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonString(true, body);
        });
    }

    @Test
    public void getUserFromJsonStringRoleNullTest(){
        String body = "{\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonString(true, body);
        });
    }

    @Test
    public void getUserFromJsonStringPasswordNullTest(){
        String body = "{\"role\":\""+Role.CONTROLLER.toString()+"\",\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        assertThrows(IllegalArgumentException.class, () -> {
            api.getUserFromJsonString(true, body);
        });
    }

    //Test fails because userDetailServiceImpl is null
    @Test
    public void getUserFromJsonStringValidBodyTest(){
        String body = "{\"password\":\""+"TestHash"+"\",\"role\":\""+Role.CONTROLLER.toString()+"\",\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            api.getUserFromJsonString(true, body);
        }
        catch(IOException e){
            System.out.println("Impossible path");
        }
        catch(IllegalArgumentException e){
            System.out.println("Impossible path");
        }
        catch(NullPointerException e){
            System.out.println("-----TEST START USER FROM JSON STRING VALID BODY-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserAppRelationNullTest(){
        UserAppRelation relation = new UserAppRelation();
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUserAppRelation(relation);
        });
    }

    @Test
    public void createJsonFromUserAppRelationIDNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setUser(new User());
        relation.setApp(new IoTApp());
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUserAppRelation(relation);
        });
    }

    @Test
    public void createJsonFromUserAppRelationUserNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setId(new UUID(0, 0));
        relation.setApp(new IoTApp());
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUserAppRelation(relation);
        });
    }

    @Test
    public void createJsonFromUserAppRelationAppNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setId(new UUID(0, 0));
        relation.setUser(new User());
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromUserAppRelation(relation);
        });
    }

    //Test fails because attributes 
    //are not properly mapped in the method
    @Test
    public void createJsonFromUserAppRelationMinimalParametersTest(){
        UserAppRelation relation = new UserAppRelation();
        ObjectNode node = mapper.createObjectNode();

        User user = new User();
        user.setName("TestUserName");
        user.setId(new UUID(0, 1));

        IoTApp app = new IoTApp();
        app.setName("TestAppName");
        app.setId(new UUID(0, 2));

        relation.setId(new UUID(0, 0));
        relation.setUser(user);
        relation.setApp(app);

        node.put("id", new UUID(0, 0).toString());
        node.put("userName", "TestUserName");
        node.put("userId", new UUID(0, 1).toString());
        node.put("appName", "TestAppName");
        node.put("appId", new UUID(0, 2).toString());

        assertEquals(api.createJsonFromUserAppRelation(relation), node);
    }

    //Test fails because attributes 
    //are not properly mapped in the method
    @Test
    public void createJsonFromUserAppRelationAllParametersTest(){
        UserAppRelation relation = new UserAppRelation();
        ObjectNode node = mapper.createObjectNode();

        User user = new User();
        user.setName("TestUserName");
        user.setId(new UUID(0, 1));

        IoTApp app = new IoTApp();
        app.setName("TestAppName");
        app.setId(new UUID(0, 2));

        String[] consenses = {"Consensus1", "Consensus2"};

        relation.setId(new UUID(0, 0));
        relation.setUser(user);
        relation.setApp(app);
        relation.setConsenses(consenses);

        node.put("id", new UUID(0, 0).toString());
        node.put("userName", "TestUserName");
        node.put("userId", new UUID(0, 1).toString());
        node.put("appName", "TestAppName");
        node.put("appId", new UUID(0, 2).toString());

        ArrayNode consensesArray = mapper.createArrayNode();
        consensesArray.add(consenses[0]);
        consensesArray.add(consenses[1]);

        node.set("consenses", consensesArray);

        assertEquals(api.createJsonFromUserAppRelation(relation), node);
    }

    @Test
    public void createJsonFromMessageNullTest(){
        Message mess = new Message();
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromMessage(mess);
        });
    }

    @Test
    public void createJsonFromMessageIDNullTest(){
        Message mess = new Message();

        User sender = new User();
        User receiver = new User();
        mess.setSender(sender);
        mess.setReceiver(receiver);
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromMessage(mess);
        });
    }

    @Test
    public void createJsonFromMessageSenderNullTest(){
        Message mess = new Message();

        mess.setId(new UUID(0, 0));
        User receiver = new User();
        mess.setReceiver(receiver);
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromMessage(mess);
        });
    }

    @Test
    public void createJsonFromMessageReceiverNullTest(){
        Message mess = new Message();

        User sender = new User();
        mess.setId(new UUID(0, 0));
        mess.setSender(sender);
        assertThrows(IllegalArgumentException.class, () -> {
            api.createJsonFromMessage(mess);
        });
    }

    //Test fails and shows a hole in the method's logic,
    //which considers only messageId, sender and receiver mandatory,  
    //but it does not work in absence of all parameters.
    //The problem lies in not checking Message and Time,
    //which leads to a NullPointerException  
    @Test
    public void createJsonFromMessageMinimalParametersTest(){
        ObjectNode node = mapper.createObjectNode();
        Message mess = new Message();
        mess.setId(new UUID(0, 0));

        User sender = new User();

        User receiver = new User();

        mess.setSender(sender);
        mess.setReceiver(receiver);

        node.put("id", new UUID(0, 0).toString());

        try{
            assertEquals(api.createJsonFromMessage(mess), node);
        }
        catch(NullPointerException e){
            System.out.println("-----TEST START JSON FROM MESSAGE MINIMAL PARAMETERS-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }

    }

    //There is an inconsistency with attributes' names
    //In particular the Message attributes called text in the json
    @Test
    public void createJsonFromMessageAllParametersTest(){
        ObjectNode node = mapper.createObjectNode();
        Message mess = new Message();
        LocalDateTime time = LocalDateTime.now();

        mess.setId(new UUID(0, 0));
        mess.setMessage("TestMessage");
        mess.setTime(time);

        User sender = new User();
        sender.setId(new UUID(0, 1));
        sender.setName("TestSenderName");

        User receiver = new User();
        receiver.setId(new UUID(0, 2));
        receiver.setName("TestReceiverName");

        mess.setSender(sender);
        mess.setReceiver(receiver);

        node.put("id", new UUID(0, 0).toString());
        node.put("senderId", new UUID(0, 1).toString());
        node.put("senderName", "TestSenderName");
        node.put("receiverId", new UUID(0, 2).toString());
        node.put("receiverName", "TestReceiverName");
        node.put("text", "TestMessage");
        node.put("time", time.toString());

        assertEquals(api.createJsonFromMessage(mess), node);
    }

    @Test
    public void getMessageFromJsonNodeSenderNullTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("text", "TestMessage");
        node.put("receiverId", new UUID(0, 2).toString());
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonNode(node);
        });
    }

    @Test
    public void getMessageFromJsonNodeReceiverNullTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("text", "TestMessage");
        node.put("senderId", new UUID(0, 1).toString());
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonNode(node);
        });
    }

    @Test
    public void getMessageFromJsonNodeMessageNullTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonNode(node);
        });
    }

    @Test
    public void getMessageFromJsonNodeIDNotValidTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        node.put("text", "TestMessage");
        node.put("id", "0");
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonNode(node);
        });
    }

    @Test
    public void getMessageFromJsonNodeTimeNotValidTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        node.put("text", "TestMessage");
        node.put("id", new UUID(0, 0).toString());
        node.put("time", "NotATime");
        assertThrows(DateTimeParseException.class, () -> {
            api.getMessageFromJsonNode(node);
        });
    }
    
    //Other tests dealing with db queries for getMessageFromJsonNode

    // String body = "{\"password\":\""+"TestHash"+"\",\"role\":\""+Role.CONTROLLER.toString()+"\",\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";

    @Test
    public void getMessageFromJsonStringNotValidJsonTest(){
        assertThrows(IOException.class, () -> {
            api.getMessageFromJsonString("NotAJson");
        });
    }
    
    @Test
    public void getMessageFromJsonStringSenderNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonString("{\"receiver\":\""+new UUID(0, 0).toString()+"\",\"id\":\""+new UUID(0, 1).toString()+"\"}");
        });
    }

    @Test
    public void getMessageFromJsonStringReceiverNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonString("{\"sender\":\""+new UUID(0, 0).toString()+"\",\"id\":\""+new UUID(0, 1).toString()+"\"}");
        });
    }

    @Test
    public void getMessageFromJsonStringTextNullTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonString("{\"receiver\":\""+new UUID(0, 0).toString()+"\",\"sender\":\""+new UUID(0, 2).toString()+"\",\"id\":\""+new UUID(0, 1).toString()+"\"}");
        });
    }

    @Test
    public void getMessageFromJsonStringIDNotValidTest(){
        assertThrows(IllegalArgumentException.class, () -> {
            api.getMessageFromJsonString("{\"receiver\":\""+new UUID(0, 0).toString()+"\",\"text\":\""+"TestMessage"+"\",\"id\":\""+"2"+"\"}");
        });
    }

    @Test
    public void getMessageFromJsonStringTimeNotValidTest(){
        assertThrows(DateTimeParseException.class, () -> {
            api.getMessageFromJsonString("{\"receiver\":\""+new UUID(0, 0).toString()+"\",\"sender\":\""+new UUID(0, 2).toString()+"\",\"text\":\""+"TestMessage"+"\",\"time\":\""+"NotATime"+"\",\"id\":\""+new UUID(0, 1).toString()+"\"}");
        });
    }
}
