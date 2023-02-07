package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class ApiMessageController {
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get the message identified by the messageId
     * RESTRICTION: The one calling the function MUST BE the RECEIVER or SENDER of the message
     * @param messageId Id of the message
     * @return JSON with message information. Bad Request if ID invalid, user is not receiver or sender of the message
     */
    @GetMapping
    @RequestMapping("api/message/get")
    public ResponseEntity<?> get(@RequestParam() String messageId){
        try{
            Message message= apiGeneralController.getMessageFromId(messageId);
            if(!isSenderOrReceiver(message, apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("you must be sender or receiver of the message");
            }
            JsonNode messageJson= apiGeneralController.createJsonFromMessage(message);
            return ResponseEntity.ok(messageJson);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the messages between 2 users
     * RESTRICTION: The one calling the function MUST BE the one of the 2 users
     * @param user1Id one of the 2 users' id
     * @param user2Id one of the 2 users' id
     * @return Json containing all the messages between the 2 users. Bad Request if invalid IDs or users don't exist
     */
    @GetMapping
    @RequestMapping("api/message/getConversation")
    public ResponseEntity<?> getConversation(@RequestParam() String user1Id, @RequestParam() String user2Id){
        try{
            if(!apiGeneralController.isAuthenticatedUserId(user1Id) && !apiGeneralController.isAuthenticatedUserId(user2Id)){
                return ResponseEntity.badRequest().body("you must be one of the two users");
            }
            User user1= apiGeneralController.getUserFromId(user1Id);
            User user2= apiGeneralController.getUserFromId(user2Id);
            List<Message> conversation= dataBaseService.getConversationFromUsers(user1, user2);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode conversationArray = mapper.createArrayNode();
            for(Message message : conversation){
                conversationArray.add(apiGeneralController.createJsonFromMessage(message));
            }
            return ResponseEntity.ok(conversationArray);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the messages of the user, divided by contacts
     * RESTRICTIONS: userId MUST represent the one calling the function
     * @param userId Id of the user to get messages from
     * @return JSON with all the messages, divided by contacts. Bad request if invalid ID, user is not authorized
     */
    @GetMapping
    @RequestMapping("api/message/getAllMessagesFromUser")
    public ResponseEntity<?> getAllMessagesFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId");
            }
            List<User> userList= dataBaseService.getUserConversationFromUser(user);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode allMessagesJson = mapper.createArrayNode();
            for(User contact : userList){
                ObjectNode conversation= mapper.createObjectNode();
                conversation.put("contactName", contact.getName());
                ArrayNode conversationArray = mapper.createArrayNode();
                for(Message message : dataBaseService.getConversationFromUsers(user, contact)){
                    conversationArray.add(apiGeneralController.createJsonFromMessage(message));
                }
                conversation.set("messages", conversationArray);
                allMessagesJson.add(conversation);
            }
            return ResponseEntity.ok(allMessagesJson);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * Send a new message
     * RESTRICTIONS: SenderId MUST represent the one calling the function
     * @param body the message to be sent
     * @return OK if message sent successfully. Bad Request if JSON invalid, users don't have app in common, user is not the sender
     */
    @PostMapping
    @RequestMapping("api/message/add")
    public ResponseEntity<?> add(@RequestBody() String body){
        try{
            Message message= apiGeneralController.getMessageFromJsonString(body);
            if(!apiGeneralController.getAuthenicatedUser().equals(message.getSender())){
                return ResponseEntity.badRequest().body("you must be the sender of the message");
            }
            if(!dataBaseService.getAllContactsFromUser(message.getSender()).contains(message.getReceiver())){
                return ResponseEntity.badRequest().body("the 2 users must have 1 app in common");
            }
            if(message.getId()!=null && dataBaseService.getMessageFromID(message.getId()).isPresent()){
                return ResponseEntity.badRequest().body("there is already a message with that ID");
            }
            if(message.getTime()==null){
                dataBaseService.addNowMessage(message);
            }
            else{
                dataBaseService.addMessage(message);
            }
            return ResponseEntity.ok("Message sent successfully");
        } catch (IllegalArgumentException | DateTimeParseException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    private boolean isSenderOrReceiver(Message message, User user){
        return message.getSender().equals(user) || message.getReceiver().equals(user);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
