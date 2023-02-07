package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.*;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiGeneralController {
    @Autowired
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    private final ObjectMapper mapper = new ObjectMapper();

    // BOOLEAN CONTROLS

    public boolean isAuthenticatedUserId(String uuid) {
        Optional<User> maybeUser = dataBaseService.getUser(UUID.fromString(uuid));
        Optional<User> maybeAuthenticate = authenticatedUser.get();
        if (maybeUser.isPresent() && maybeAuthenticate.isPresent()) {
            return maybeUser.get().equals(maybeAuthenticate.get());
        }
        return false;
    }

    public boolean isControllerOrDpo(boolean considerAuthenticatedUser, String uuid) {
        Optional<User> maybeUser;
        if (considerAuthenticatedUser) {
            maybeUser = authenticatedUser.get();
        } else {
            maybeUser = dataBaseService.getUser(UUID.fromString(uuid));
        }
        return maybeUser.filter(user -> (user.getRole().equals(Role.CONTROLLER) || user.getRole().equals(Role.DPO))).isPresent();
    }

    public boolean userHasApp(User user, IoTApp app) {
        return (dataBaseService.getUserAppRelationByUserAndApp(user, app) != null);
    }

    // GET OBJECTS

    /**
     * @return the authenticate user
     * @throws IllegalArgumentException if there is no authenticated user
     */
    public User getAuthenicatedUser() throws IllegalArgumentException {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            return maybeUser.get();
        } else {
            throw new IllegalArgumentException("No authenticated user");
        }
    }

    /**
     * @param uuid the ID of the User
     * @return the User with that ID
     * @throws IllegalArgumentException If ID is an invalid UUID
     * @throws IllegalArgumentException If user with that ID does not exist
     */
    public User getUserFromId(String uuid) throws IllegalArgumentException {
        try {
            Optional<User> maybeUser = dataBaseService.getUser(UUID.fromString(uuid));
            if (maybeUser.isPresent()) {
                return maybeUser.get();
            } else {
                throw new IllegalArgumentException("user does not exist");
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    /**
     * @param uuid the ID of the app
     * @return the app with that ID
     * @throws IllegalArgumentException If ID is an invalid UUID
     * @throws IllegalArgumentException If app with that ID does not exist
     */
    public IoTApp getAppFromId(String uuid) throws IllegalArgumentException {
        try {
            Optional<IoTApp> maybeApp = dataBaseService.getApp(UUID.fromString(uuid));
            if (maybeApp.isPresent()) {
                return maybeApp.get();
            } else {
                throw new IllegalArgumentException("app does not exist");
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    public Message getMessageFromId(String uuid) throws IllegalArgumentException {
        try {
            Optional<Message> message = dataBaseService.getMessageFromID(UUID.fromString(uuid));
            if (message.isPresent()) {
                return message.get();
            } else {
                throw new IllegalArgumentException("message does not exist");
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    /**
     * @param userId the ID of the user
     * @param appId  the ID of the app
     * @return the object UserAppRelation
     * @throws IllegalArgumentException if IDs invalid, user or app does not exist, user does not have the app
     */
    public UserAppRelation getUserAppRelationByUserIdAndAppId(String userId, String appId) throws IllegalArgumentException {
        User user = getUserFromId(userId);
        IoTApp app = getAppFromId(appId);
        UserAppRelation userAppRelation = dataBaseService.getUserAppRelationByUserAndApp(user, app);
        if (userAppRelation == null) {
            throw new IllegalArgumentException("user does not have this app");
        }
        return userAppRelation;
    }

    /**
     * @param privacyNoticeId ID of the privacy notice
     * @return privacy notice
     * @throws IllegalArgumentException if IDs invalid, privacy notice does not exist
     */
    public PrivacyNotice getPrivacyNoticeFromId(String privacyNoticeId) throws IllegalArgumentException {
        try {
            Optional<PrivacyNotice> privacyNotice = dataBaseService.getPrivacyNoticeFromId(UUID.fromString(privacyNoticeId));
            if (privacyNotice.isPresent()) {
                return privacyNotice.get();
            } else {
                throw new IllegalArgumentException("privacy notice does not exist");
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    /**
     * @param appId ID of the app of the privacy notice
     * @return privacy notice
     * @throws IllegalArgumentException if IDs invalid, app does not exist, privacy notice does not exist
     */
    public PrivacyNotice getPrivacyNoticeFromAppId(String appId) throws IllegalArgumentException {
        PrivacyNotice privacyNotice = dataBaseService.getPrivacyNoticeFromApp(getAppFromId(appId));
        if (privacyNotice == null) {
            throw new IllegalArgumentException("privacy notice does not exist");
        } else {
            return privacyNotice;
        }
    }

    /**
     * @param notificationId ID of the Notification
     * @return notification
     * @throws IllegalArgumentException if IDs invalid, notification does not exist
     */
    public Notification getNotificationFromId(String notificationId) throws IllegalArgumentException {
        try {
            Optional<Notification> notification = dataBaseService.getNotificationFromId(UUID.fromString(notificationId));
            if (notification.isPresent()) {
                return notification.get();
            } else {
                throw new IllegalArgumentException("notification does not exist");
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    /**
     * @param requestId ID of the RightRequest
     * @return RightRequest
     * @throws IllegalArgumentException if IDs invalid, RightRequest does not exist
     */
    public RightRequest getRequestFromId(String requestId) throws IllegalArgumentException {
        try {
            RightRequest request = dataBaseService.getRequestFromId(UUID.fromString(requestId));
            if (request == null) {
                throw new IllegalArgumentException("Right Request does not exist");
            } else {
                return request;
            }
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid ID");
        }
    }

    // MANAGE JSON OBJECTS
    // App

    /**
     * @param app the app to be represented as a JSON. It MUST contain the ID and the NAME
     * @return the JSON object representing the app
     * @throws IllegalArgumentException If IoTApp object is null
     * @throws IllegalArgumentException If IoTApp object has not an ID
     * @throws IllegalArgumentException If IoTApp object has not a name
     */
    public ObjectNode createJsonFromApp(IoTApp app) throws IllegalArgumentException {
        ObjectNode appJson = mapper.createObjectNode();

        if (app == null || app.getId() == null || app.getName() == null) {
            throw new IllegalArgumentException("invalid app");
        }
        appJson.put("id", app.getId().toString());
        appJson.put("name", app.getName());

        if (app.getDescription() != null) {
            appJson.put("description", app.getDescription());
        }

        if (app.getQuestionnaireVote() != null) {
            appJson.put("questionnaireVote", app.getQuestionnaireVote().toString());
        }

        if (app.getDetailVote() != null) {
            ArrayNode detailVoteArray = mapper.createArrayNode();
            for (String answer : app.getDetailVote()) {
                detailVoteArray.add(answer);
            }
            appJson.set("detailVote", detailVoteArray);
        }

        if (app.getOptionalAnswers() != null) {
            ArrayNode optionalAnswersArray = mapper.createArrayNode();
            for (int i = 0; i < GlobalVariables.nQuestions; i++) {
                optionalAnswersArray.add(app.getOptionalAnswers().get(i));

            }
            appJson.set("optionalAnswers", optionalAnswersArray);
        }
        return appJson;
    }

    /**
     * @param nameMandatory specify if the name MUST be included (true) or not (false)
     * @param body          the JSON body from the POST request
     * @return the app object represented by the JSON
     * @throws IOException              If body is not a valid JSON
     * @throws IllegalArgumentException If the JSON does not contain name
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     */
    public IoTApp getAppFromJsonString(boolean nameMandatory, String body) throws IOException, IllegalArgumentException {
        try{
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getAppFromJsonNode(nameMandatory, rootNode);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        }
    }

    /**
     * @param nameMandatory specify if the name MUST be included (true) or not (false)
     * @param node          the JSON object to be turned into an app object.
     * @return the app object represented by the JSON
     * @throws IllegalArgumentException If the JSON does not contain name
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     */
    public IoTApp getAppFromJsonNode(boolean nameMandatory, JsonNode node) throws IllegalArgumentException {
        IoTApp app = new IoTApp();

        if (node.has("name")) {
            app.setName(node.get("name").asText());
        } else if (nameMandatory) {
            throw new IllegalArgumentException("JSON incomplete");
        }


        if (node.has("id")) {
            app.setId(UUID.fromString(node.get("id").asText()));
        }

        if (node.has("description")) {
            app.setId(UUID.fromString(node.get("description").asText()));
        }

        if (node.has("questionnaireVote")) {
            switch (node.get("questionnaireVote").asText()) {
                case "RED":
                    app.setQuestionnaireVote(QuestionnaireVote.RED);
                    break;
                case "ORANGE":
                    app.setQuestionnaireVote(QuestionnaireVote.ORANGE);
                    break;
                case "GREEN":
                    app.setQuestionnaireVote(QuestionnaireVote.GREEN);
                    break;

            }
        }

        if (node.has("detailVote")) {
            JsonNode detailVote = node.get("detailVote");
            if (detailVote.isArray()) {
                String[] detailVoteArray = new String[GlobalVariables.nQuestions];
                int i = 0;
                for (JsonNode singleAnswer : detailVote) {
                    detailVoteArray[i] = singleAnswer.asText();
                    i++;
                    if (i >= GlobalVariables.nQuestions) {
                        break;
                    }
                }
                if (i < GlobalVariables.nQuestions) {
                    for (int j = i; j < GlobalVariables.nQuestions; j++) {
                        detailVoteArray[j] = null;
                    }
                }
                app.setDetailVote(detailVoteArray);
            }
        }

        if (node.has("optionalAnswers")) {
            JsonNode optionalAnswers = node.get("optionalAnswers");
            if (optionalAnswers.isArray()) {
                Hashtable<Integer, String> optionalAnswersHash = new Hashtable<>();
                int i = 0;
                for (JsonNode singleAnswer : optionalAnswers) {
                    optionalAnswersHash.put(i, singleAnswer.asText());
                    i++;
                    if (i >= GlobalVariables.nQuestions) {
                        break;
                    }
                }
                if (i < GlobalVariables.nQuestions) {
                    for (int j = i; j < GlobalVariables.nQuestions; j++) {
                        optionalAnswersHash.put(i, null);
                    }
                }
                app.setOptionalAnswers(optionalAnswersHash);
            }
        }
        return app;
    }

    // User

    /**
     * @param user the user to be represented as a JSON. It MUST contain the ID, NAME, ROLE and PASSWORD
     * @return the JSON object representing the user
     * @throws IllegalArgumentException If User object is null
     * @throws IllegalArgumentException If User object has not an ID, name, role or password
     */
    public ObjectNode createJsonFromUser(User user) throws IllegalArgumentException {
        ObjectNode userJson = mapper.createObjectNode();

        if (user == null || user.getId() == null || user.getName() == null || user.getHashedPassword() == null || user.getRole() == null) {
            throw new IllegalArgumentException("invalid user");
        }
        userJson.put("id", user.getId().toString());
        userJson.put("name", user.getName());
        userJson.put("role", user.getRole().toString());
        userJson.put("hashedPassword", user.getHashedPassword());

        if (user.getMail() != null) {
            userJson.put("mail", user.getMail());
        }

        return userJson;
    }

    /**
     * @param parametersMandatory specify if the name, role and password MUST be included (true) or not (false)
     * @param body                the JSON body from the POST request
     * @return the user object represented by the JSON
     * @throws IOException              If body is not a valid JSON
     * @throws IllegalArgumentException If the JSON does not contain name, role or password
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     */
    public User getUserFromJsonString(boolean parametersMandatory, String body) throws IllegalArgumentException, IOException {
        try {
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getUserFromJsonNode(parametersMandatory, rootNode);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        }
    }

    /**
     * @param parametersMandatory specify if the name, role and password MUST be included (true) or not (false)
     * @param node                the JSON object to be turned into a user object.
     * @return the user object represented by the JSON
     * @throws IllegalArgumentException If the JSON does not contain name, role or password
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     */
    public User getUserFromJsonNode(boolean parametersMandatory, JsonNode node) throws IllegalArgumentException {
        User user = new User();

        if (node.has("name")) {
            user.setName(node.get("name").asText());
        } else if (parametersMandatory) {
            throw new IllegalArgumentException("JSON incomplete");
        }

        if (node.has("password")) {
            user.setHashedPassword(userDetailsServiceImpl.hashPass(node.get("password").asText()));
        } else if (parametersMandatory) {
            throw new IllegalArgumentException("JSON incomplete");
        }

        if (node.has("role")) {
            switch (node.get("role").asText()) {
                case "SUBJECT":
                    user.setRole(Role.SUBJECT);
                    break;
                case "CONTROLLER":
                    user.setRole(Role.CONTROLLER);
                    break;
                case "DPO":
                    user.setRole(Role.DPO);
                    break;
            }
        } else if (parametersMandatory) {
            throw new IllegalArgumentException("JSON incomplete");
        }

        if (node.has("id")) {
            user.setId(UUID.fromString(node.get("id").asText()));
        }

        if (node.has("mail")) {
            user.setMail(node.get("mail").asText());
        }

        return user;
    }

    // UserAppRelation

    /**
     * @param userAppRelation object to be transformed in JSON
     * @return JSON representing the object
     * @throws IllegalArgumentException if invalid object
     */
    public ObjectNode createJsonFromUserAppRelation(UserAppRelation userAppRelation) throws IllegalArgumentException {
        ObjectNode userAppRelationJson = mapper.createObjectNode();

        if (userAppRelation == null || userAppRelation.getId() == null || userAppRelation.getUser() == null || userAppRelation.getApp() == null) {
            throw new IllegalArgumentException("UserAppRelation invalid");
        }
        userAppRelationJson.put("id", userAppRelation.getId().toString());
        userAppRelationJson.put("userName", userAppRelation.getApp().getName());
        userAppRelationJson.put("userId", userAppRelation.getUser().getId().toString());
        userAppRelationJson.put("appName", userAppRelation.getUser().getName());
        userAppRelationJson.put("appId", userAppRelation.getApp().getId().toString());

        List<String> consenses = userAppRelation.getConsenses();
        if (consenses != null) {
            ArrayNode consensesArray = mapper.createArrayNode();
            for (String consens : consenses) {
                consensesArray.add(consens);
            }
            userAppRelationJson.set("consenses", consensesArray);
        }
        return userAppRelationJson;
    }

    // Message

    /**
     * @param message object to be transformed in JSON
     * @return JSON representing message
     * @throws IllegalArgumentException if invalid object
     */
    public ObjectNode createJsonFromMessage(Message message) throws IllegalArgumentException {
        ObjectNode messageJson = mapper.createObjectNode();

        if (message == null || message.getId() == null || message.getReceiver() == null || message.getSender() == null) {
            throw new IllegalArgumentException("Message invalid");
        }
        messageJson.put("id", message.getId().toString());
        messageJson.put("senderId", message.getSender().getId().toString());
        messageJson.put("senderName", message.getSender().getName());
        messageJson.put("receiverId", message.getReceiver().getId().toString());
        messageJson.put("receiverName", message.getReceiver().getName());
        messageJson.put("text", message.getMessage());
        messageJson.put("time", message.getTime().toString());
        return messageJson;
    }

    /**
     * @param body the JSON body from the POST request
     * @return the message object represented by the JSON
     * @throws IOException              If body is not a valid JSON
     * @throws IllegalArgumentException If the JSON does not contain senderId, receiverId or text
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     * @throws DateTimeParseException   If time is invalid
     */
    public Message getMessageFromJsonString(String body) throws IllegalArgumentException, IOException, DateTimeParseException {
        try {
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getMessageFromJsonNode(rootNode);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        } catch (DateTimeParseException e){
            throw new IllegalArgumentException("invalid date");
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid JSON parameters");
        }
    }

    /**
     * @param node the JSON object to be turned into a message object.
     * @return the message object represented by the JSON
     * @throws IllegalArgumentException If the JSON does not contain senderId, receiverId or text
     * @throws IllegalArgumentException If the indicated ID is an invalid UUID
     * @throws DateTimeParseException   If time is invalid
     */
    public Message getMessageFromJsonNode(JsonNode node) throws IllegalArgumentException, DateTimeParseException {
        Message message = new Message();
        if (!node.has("senderId") || !node.has("receiverId") || !node.has("text")) {
            throw new IllegalArgumentException("JSON incomplete");
        }

        if (node.has("id")) {
            message.setId(UUID.fromString(node.get("id").asText()));
        }

        if (node.has("time")) {
            message.setTime(LocalDateTime.parse(node.get("time").asText()));
        }

        message.setMessage(node.get("text").asText());

        Optional<User> sender = dataBaseService.getUser(UUID.fromString(node.get("senderId").asText()));
        if (sender.isEmpty()) {
            throw new IllegalArgumentException("sender does not exist");
        }
        message.setSender(sender.get());

        Optional<User> receiver = dataBaseService.getUser(UUID.fromString(node.get("receiverId").asText()));
        if (receiver.isEmpty()) {
            throw new IllegalArgumentException("receiver does not exist");
        }
        message.setReceiver(receiver.get());
        return message;
    }

    // Privacy Notice

    /**
     * @param privacyNotice object to be transformed into json
     * @return Json associated with Privacy Notice
     * @throws IllegalArgumentException if PrivacyNotice has not id, app or text
     */
    public JsonNode createJsonFromPrivacyNotice(PrivacyNotice privacyNotice) throws IllegalArgumentException {
        ObjectNode privacyNoticeJson = mapper.createObjectNode();
        if (privacyNotice.getId() == null || privacyNotice.getText() == null || privacyNotice.getApp() == null) {
            throw new IllegalArgumentException("invalid privacy notice");
        }
        privacyNoticeJson.put("id", privacyNotice.getId().toString());
        privacyNoticeJson.put("appname", privacyNotice.getApp().getName());
        privacyNoticeJson.put("appId", privacyNotice.getApp().getId().toString());
        privacyNoticeJson.put("text", privacyNotice.getText());
        return privacyNoticeJson;
    }

    /**
     * @param mandatoryParameters specify if the appId MUST be included (true) or not (false)
     * @param body                JSON representing the Privacy Notice
     * @return Privacy Notice
     * @throws IllegalArgumentException if invalid app does not exist, invalid JSON
     * @throws IOException              if invalid JSON
     */
    public PrivacyNotice getPrivacyNoticeFromJsonString(boolean mandatoryParameters, String body) throws IllegalArgumentException, IOException {
        try {
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getPrivacyNoticeFromJsonNode(mandatoryParameters, rootNode);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        }
    }

    /**
     * @param mandatoryParameters specify if the appId MUST be included (true) or not (false)
     * @param node                JSON representing the Privacy Notice
     * @return Privacy Notice
     * @throws IllegalArgumentException if invalid app does not exist, invalid JSON
     */
    public PrivacyNotice getPrivacyNoticeFromJsonNode(boolean mandatoryParameters, JsonNode node) throws IllegalArgumentException {
        PrivacyNotice privacyNotice = new PrivacyNotice();
        if (node.has("appId")) {
            privacyNotice.setApp(getAppFromId(node.get("appId").asText()));
        } else if (mandatoryParameters) {
            throw new IllegalArgumentException("missing appId");
        }

        if (node.has("text")) {
            privacyNotice.setText(node.get("text").asText());
        } else if (mandatoryParameters) {
            throw new IllegalArgumentException("missing appId");
        }
        return privacyNotice;
    }

    // Notification

    /**
     * @param notification object to be transformed into json
     * @return Json associated with Notification
     * @throws IllegalArgumentException if Notification has not id, senderId, receiverId, type, objectId
     */
    public JsonNode createJsonFromNotification(Notification notification) throws IllegalArgumentException {
        ObjectNode notificationJson = mapper.createObjectNode();
        if (notification.getId() == null || notification.getReceiver() == null || notification.getSender() == null || notification.getType()==null || notification.getObjectId()==null) {
            throw new IllegalArgumentException("invalid notification");
        }
        notificationJson.put("id", notification.getId().toString());
        notificationJson.put("senderId", notification.getSender().getId().toString());
        notificationJson.put("senderName", notification.getSender().getName());
        notificationJson.put("receiverId", notification.getReceiver().getId().toString());
        notificationJson.put("receiverName", notification.getReceiver().getName());
        notificationJson.put("description", notification.getDescription());
        notificationJson.put("type", notification.getType());
        notificationJson.put("objectId", notification.getObjectId().toString());

        if(notification.getTime()!=null){
            notificationJson.put("time", notification.getTime().toString());
        }
        if(notification.getRead()!=null){
            notificationJson.put("isRead", notification.getRead());
        }
        return notificationJson;
    }

    /**
     * @param user User to get Notifications from
     * @param all All the notification?
     * @param isRead if all==false, isRead notification or not
     * @return JSON representing all the notifications
     */
    public ArrayNode createJsonNotificationsFromUser(User user, boolean all, boolean isRead){
        List<Notification> notificationList;
        if(all){
            notificationList= dataBaseService.getNotificationsFromUser(user);
        }
        else{
            if(isRead){
                notificationList= dataBaseService.getOldNotificationsFromUser(user);
            }
            else{
                notificationList= dataBaseService.getNewNotificationsFromUser(user);
            }
        }
        ArrayNode notificationArray = mapper.createArrayNode();
        for(Notification notification : notificationList){
            notificationArray.add(createJsonFromNotification(notification));
        }
        return notificationArray;
    }

    /**
     *
     * @param body JSON representing the Notification
     * @return Notification
     * @throws IllegalArgumentException if JSONObject has not id, senderId, receiverId, type, objectId
     * @throws IOException if invalid JSON
     */
    public Notification getNotificationFromJsonString(String body) throws IllegalArgumentException, IOException{
        try {
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getNotificationFromJsonNode(rootNode);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        }
    }

    /**
     * @param node JSON representing the Notification
     * @return Notification
     * @throws IllegalArgumentException if JSONObject has not senderId, receiverId, type, objectId. If object does not exist or is wrong type
     */
    public Notification getNotificationFromJsonNode(JsonNode node) throws IllegalArgumentException {
        Notification notification= new Notification();
        if(!node.has("senderId") || !node.has("receiverId") || !node.has("type") || !node.has("objectId")){
            throw new IllegalArgumentException("missing mandatory parameters in the JSON object");
        }
        notification.setSender(getUserFromId(node.get("senderId").asText()));
        notification.setReceiver(getUserFromId(node.get("receiverId").asText()));

        // check if type is correct for the objectId
        String type=node.get("type").asText();
        if(GlobalVariables.notificationType.contains(type)){
            switch (type){
                case "Message":
                    Message message= getMessageFromId(node.get("objectId").asText()); break;
                case "Request":
                    RightRequest request= getRequestFromId(node.get("objectId").asText()); break;
                case "PrivacyNotice":
                    PrivacyNotice privacyNotice= getPrivacyNoticeFromId(node.get("objectId").asText()); break;
            }
        }
        notification.setType(type);
        try {
            notification.setObjectId(UUID.fromString(node.get("objectId").asText()));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid object ID");
        }

        if(node.has("description")){
            notification.setDescription(node.get("description").asText());
        }

        if(node.has("isRead")){
            notification.setRead(node.get("isRead").asBoolean());
        }

        if(node.has("time")){
            notification.setTime(LocalDateTime.parse(node.get("time").asText()));
        }
        return notification;
    }

    //  RightRequest

    /**
     * @param request object to be represented in JSON
     * @return JSON representing that request
     * @throws IllegalArgumentException if request does not exist or is invalid
     */
    public JsonNode createJsonFromRequest(RightRequest request) throws IllegalArgumentException{
        ObjectNode requestJson = mapper.createObjectNode();
        if(request.getId()==null || request.getSender()==null || request.getReceiver()==null || request.getRightType()==null){
            throw new IllegalArgumentException("invalid right request");
        }

        requestJson.put("id", request.getId().toString());
        requestJson.put("senderName", request.getSender().getName());
        requestJson.put("senderId", request.getSender().getId().toString());
        requestJson.put("receiverName", request.getReceiver().getName());
        requestJson.put("receiverId", request.getReceiver().getId().toString());
        if(request.getTime()!=null){
            requestJson.put("time", request.getTime().toString());
        }
        if(request.getApp()!=null){
            requestJson.put("appId", request.getApp().getId().toString());
            requestJson.put("appName", request.getApp().getName());
        }
        requestJson.put("rightType", request.getRightType().toString());
        if(request.getOther()!=null){
            requestJson.put("other", request.getOther());
        }
        if(request.getHandled()!=null){
            requestJson.put("handled", request.getHandled());
        }
        if(request.getDetails()!=null){
            requestJson.put("details", request.getDetails());
        }
        if(request.getResponse()!=null){
            requestJson.put("response", request.getResponse());
        }
        return requestJson;
    }

    /**
     * @return JSON representing the requests in the request list that have the sender or the receiver equals to the parameter user
     */
    public ArrayNode createJsonRequestCheckAuthorizedUser(List<RightRequest> requestList, User user){
        ArrayNode requestArray = mapper.createArrayNode();
        for(RightRequest request : requestList){
            if(request.getSender().equals(user) || request.getReceiver().equals(user)){
                requestArray.add(createJsonFromRequest(request));
            }
        }
        return requestArray;
    }

    /**
     * @return JSON representing the requests in the request list that have the app equals to the parameter app
     */
    public ArrayNode createJsonRequestOfApp(List<RightRequest> requestList, IoTApp app){
        ArrayNode requestArray = mapper.createArrayNode();
        for(RightRequest request : requestList){
            if(request.getApp().equals(app)){
                requestArray.add(createJsonFromRequest(request));
            }
        }
        return requestArray;
    }

    /**
     * @return JSON representing the requests in the request list that have the rightType equals to the parameter rightTypeString
     */
    public ArrayNode createJsonRequestOfRightType(List<RightRequest> requestList, String rightTypeString) throws IllegalArgumentException{
        RightType rightType;
        try {
            rightType = RightType.valueOf(rightTypeString.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid Right Type");
        }
        ArrayNode requestArray = mapper.createArrayNode();
        for(RightRequest request : requestList){
            if(request.getRightType().equals(rightType)){
                requestArray.add(createJsonFromRequest(request));
            }
        }
        return requestArray;
    }

    /**
     * @param body JSON representing the Request
     * @param mandatoryParameters specify if senderID, receiverId, rightType and objectId must be included
     * @return Request
     * @throws IllegalArgumentException if JSONObject has not senderId, receiverId, rightType, objectId. If object does not exist or is wrong type
     * @throws IOException if invalid JSON
     */
    public RightRequest getRequestFromJsonString(String body, boolean mandatoryParameters) throws IllegalArgumentException, IOException{
        try {
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            return getRequestFromJsonNode(rootNode, mandatoryParameters);
        } catch (IOException e){
            throw new IOException("invalid JSON");
        }
    }

    /**
     * @param node JSON representing the Request
     * @param mandatoryParameters specify if senderID, appId, rightType must be included
     * @return Request
     * @throws IllegalArgumentException if JSONObject has not senderId, receiverId, rightType. If object does not exist or is wrong type
     */
    public RightRequest getRequestFromJsonNode(JsonNode node, boolean mandatoryParameters) throws IllegalArgumentException {
        RightRequest request = new RightRequest();
        if(node.has("senderId")){
            request.setSender(getUserFromId(node.get("senderId").asText()));
        }
        else if(mandatoryParameters){
            throw new IllegalArgumentException("senderId value missing");
        }

        if(node.has("appId")){
            request.setApp(getAppFromId(node.get("appId").asText()));
        }
        else if(mandatoryParameters ){
            throw new IllegalArgumentException("appId value missing");
        }

        if(node.has("receiverId")){
            request.setReceiver(getUserFromId(node.get("receiverId").asText()));
        }

        if(node.has("rightType")){
            request.setRightType(RightType.valueOf(node.get("rightType").asText()));
        }
        else if(mandatoryParameters){
            throw new IllegalArgumentException("rightType value missing");
        }

        if(node.has("time")){
            try{
                request.setTime(LocalDateTime.parse(node.get("time").asText()));
            } catch (DateTimeParseException e){
                throw new IllegalArgumentException("invalid date");
            }
        }
        if(node.has("other")){
            request.setOther(node.get("other").asText());
        }
        if(node.has("details")){
            request.setDetails(node.get("details").asText());
        }
        if(node.has("response")){
            request.setDetails(node.get("response").asText());
        }
        if(node.has("handled")){
            if(node.get("handled").asText().equalsIgnoreCase("true") || node.get("handled").asText().equalsIgnoreCase("false")){
                request.setHandled(node.get("handled").asBoolean());
            }
        }
        return request;
    }
}
