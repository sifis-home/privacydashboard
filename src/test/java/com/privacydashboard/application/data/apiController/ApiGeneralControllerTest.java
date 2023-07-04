package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.*;
import org.junit.jupiter.engine.*;
import org.junit.platform.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        try{
            api.createJsonFromApp(app);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM APP NULL APP-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }
    
    @Test
    public void createJsonFromAppNameNullTest(){
        IoTApp app = new IoTApp();
        app.setId(new UUID(0, 4));
        try{
            api.createJsonFromApp(app);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM APP NULL APP NAME-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromAppIDNullTest(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        try{
            api.createJsonFromApp(app);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM APP NULL APP ID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
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

    //Il metodo createJsonFromApp richiede che ci siano
    //un numero di optionalAnswers pari a nQuestions
    //mentre non ha lo stesso requisito per detailVote,
    //con il loro numero che può essere maggiore o minore.
    //Questo va in contrasto con il metodo inverso,
    //getAppFromJsonNode, che invece vuole che i detailVote
    //siano al più nQuestions.
    //Il metodo createJsonFromApp e il suo inverso
    //non considerano l'attributo Consenses di IotApp.
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
        try{
            api.getAppFromJsonNode(false, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START APP FROM NODE NULL APP ID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getAppFromJsonNodeNullNameTest(){
        ObjectNode node = mapper.createObjectNode();
        try{
            api.getAppFromJsonNode(true, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START APP FROM NODE NULL APP NAME-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
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


    //Il test fallisce perché non si può assegnare
    //come valore di un record Hashtable null.
    //Errore alla linea 358 di ApiGeneralController
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

        try{
            assertEquals(api.getAppFromJsonNode(false, node), app);
        }
        catch(NullPointerException e){
            System.out.println("Non si può assegnare null ad un record di una Hashtable: "+e);
        }
    }

    @Test
    public void getAppFromJsonNodeMinimalParametersTest(){
        ObjectNode node = mapper.createObjectNode();
        assertEquals(isAppNull(api.getAppFromJsonNode(false, node)), true);
    }

    @Test
    public void getAppFromJsonStringNotValidJsonTest(){
        try{
            api.getAppFromJsonString(false, "NotAJson");
        }
        catch(IOException e){
            System.out.println("-----TEST START APP FROM STRING NULL BODY-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getAppFromJsonStringNameNullTest(){
        String body = "{\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            api.getAppFromJsonString(true, body);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START APP FROM STRING NULL NAME-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
        catch(IOException e){
            System.out.println('\n'+"Impossible path: "+e);
        }
    }

    @Test
    public void getAppFromJsonStringIDNotValidTest(){
        String body = "{\"id\":\""+"0"+"\"}";
        try{
            api.getAppFromJsonString(false, body);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START APP FROM STRING NOT VALID ID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
        catch(IOException e){
            System.out.println('\n'+"Impossible path: "+e);
        }
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
        try{
            api.createJsonFromUser(user);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserIDNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setHashedPassword("HashedTest");
        user.setRole(Role.CONTROLLER);

        try{
            api.createJsonFromUser(user);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USER ID NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserNameNullTest(){
        User user = new User();

        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");
        user.setRole(Role.CONTROLLER);

        try{
            api.createJsonFromUser(user);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USER NAME NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserPasswordNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setRole(Role.CONTROLLER);

        try{
            api.createJsonFromUser(user);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USER PASSWORD NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserRoleNullTest(){
        User user = new User();

        user.setName("TestName");
        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedTest");

        try{
            api.createJsonFromUser(user);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USER ROLE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
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

        try{
            api.getUserFromJsonNode(false, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON ID NOT VALID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonNodeNameNullTest(){
        ObjectNode node = mapper.createObjectNode();

        try{
            api.getUserFromJsonNode(true, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON NAME NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonNodePasswordNullTest(){
        ObjectNode node = mapper.createObjectNode();

        node.put("name", "TestName");

        try{
            api.getUserFromJsonNode(true, node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON PASSWORD NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    //Questo test e il successivo falliscono in quanto 
    //userDetailsServiceImpl non è inizializzato
    //e lancia una NullPointerException
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

    //Il campo ProfilePictureUrl non viene usato in
    //questo metodo o nel suo inverso
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
        try{
            api.getUserFromJsonString(false, "NotAJson");
        }
        catch(IOException e){
            System.out.println("-----TEST START USER FROM JSON STRING JSON NOT VALID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonStringIDNotValidTest(){
        String body = "{\"id\":\""+"0"+"\"}";
        try{
            api.getUserFromJsonString(false, body);
        }
        catch(IOException e){
            System.out.println("Impossible path");
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON STRING ID NOT VALID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonStringNameNullTest(){
        String body = "{\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            api.getUserFromJsonString(true, body);
        }
        catch(IOException e){
            System.out.println("Impossible path");
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON STRING NAME NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonStringRoleNullTest(){
        String body = "{\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            api.getUserFromJsonString(true, body);
        }
        catch(IOException e){
            System.out.println("Impossible path");
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON STRING ROLE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getUserFromJsonStringPasswordNullTest(){
        String body = "{\"role\":\""+Role.CONTROLLER.toString()+"\",\"name\":\""+"TestName"+"\",\"id\":\""+new UUID(0, 0).toString()+"\"}";
        try{
            api.getUserFromJsonString(true, body);
        }
        catch(IOException e){
            System.out.println("Impossible path");
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START USER FROM JSON STRING PASSWORD NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    //Il test fallisce perché userDetailsServiceImpl è null
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

        try{
            api.createJsonFromUserAppRelation(relation);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USERAPPRELATION NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserAppRelationIDNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setUser(new User());
        relation.setApp(new IoTApp());

        try{
            api.createJsonFromUserAppRelation(relation);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USERAPPRELATION ID NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserAppRelationUserNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setId(new UUID(0, 0));
        relation.setApp(new IoTApp());

        try{
            api.createJsonFromUserAppRelation(relation);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USERAPPRELATION USER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromUserAppRelationAppNull(){
        UserAppRelation relation = new UserAppRelation();

        relation.setId(new UUID(0, 0));
        relation.setUser(new User());

        try{
            api.createJsonFromUserAppRelation(relation);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM USERAPPRELATION APP NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    //Il test fallisce perché i parametri sono mappati male nel metodo
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

    //Il test fallisce perché i parametri sono mappati male nel metodo
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

        try{
            api.createJsonFromMessage(mess);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM MESSAGE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromMessageIDNullTest(){
        Message mess = new Message();

        User sender = new User();
        User receiver = new User();
        mess.setSender(sender);
        mess.setReceiver(receiver);

        try{
            api.createJsonFromMessage(mess);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM MESSAGE ID NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromMessageSenderNullTest(){
        Message mess = new Message();

        mess.setId(new UUID(0, 0));
        User receiver = new User();
        mess.setReceiver(receiver);

        try{
            api.createJsonFromMessage(mess);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM MESSAGE SENDER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void createJsonFromMessageReceiverNullTest(){
        Message mess = new Message();

        User sender = new User();
        mess.setId(new UUID(0, 0));
        mess.setSender(sender);

        try{
            api.createJsonFromMessage(mess);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START JSON FROM MESSAGE RECEIVER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    //Il test fallisce e mostra un falla nella logica del metodo
    //Il quale ritiene obbligatori message id, sender e receiver
    //Ma non può funzionare in assenza di tutti i parametri
    //La mancanza o meno di ID per gli user coinvolti è relativa
    //Il problema è nel non controllare Message e Time 
    //Il che porta a non poter gestire l'eccezione NullPointer    
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


    //C'è una incongruenza con le nomenclature degli attributi
    //In particolare message che il json chiama text
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
        try{
            api.getMessageFromJsonNode(node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START MESSAGE FROM JSON SENDER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getMessageFromJsonNodeReceiverNullTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("text", "TestMessage");
        node.put("senderId", new UUID(0, 1).toString());
        try{
            api.getMessageFromJsonNode(node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START MESSAGE FROM JSON RECEIVER NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getMessageFromJsonNodeMessageNullTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        try{
            api.getMessageFromJsonNode(node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START MESSAGE FROM JSON MESSAGE NULL-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getMessageFromJsonNodeIDNotValidTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        node.put("text", "TestMessage");
        node.put("id", "0");
        try{
            api.getMessageFromJsonNode(node);
        }
        catch(IllegalArgumentException e){
            System.out.println("-----TEST START MESSAGE FROM JSON ID NOT VALID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }

    @Test
    public void getMessageFromJsonNodeTimeNotValidTest(){
        ObjectNode node = mapper.createObjectNode();
        node.put("senderId", new UUID(0, 1).toString());
        node.put("receiverId", new UUID(0, 2).toString());
        node.put("text", "TestMessage");
        node.put("id", new UUID(0, 0).toString());
        node.put("time", "NotATime");
        try{
            api.getMessageFromJsonNode(node);
        }
        catch(DateTimeParseException e){
            System.out.println("-----TEST START MESSAGE FROM JSON TIME NOT VALID-----");
            System.out.println("Caught Exception:" + e);
            System.out.println("-----TEST END-----"+'\n');
        }
    }
    
    
}
