package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.privacydashboard.application.data.entity.*;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ApiNotificationController {
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get information about Notification
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the senderId or receiverId
     * @param notificationId Id of the notification
     * @return JSON with information about the Notification. Bad Request if invalid ID, Notification does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/notification/get")
    public ResponseEntity<?> get(@RequestParam() String notificationId){
        try{
            Notification notification= apiGeneralController.getNotificationFromId(notificationId);
            if(!isSenderOrReceiver(notification, apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("you MUST be the sender or the receiver of the notification");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonFromNotification(notification));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the Notification of a user
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the receiverId
     * @param userId Id of the user to get notification from
     * @return JSON with information about all the Notification. Bad Request if invalid ID, user does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/notification/getAllFromUser")
    public ResponseEntity<?> getAllFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you MUST be the user identified by the userId");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonNotificationsFromUser(user, true, true));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the read Notification of a user
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the receiverId
     * @param userId Id of the user to get notification from
     * @return JSON with information about all the read Notification. Bad Request if invalid ID, user does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/notification/getReadFromUser")
    public ResponseEntity<?> getReadFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you MUST be the user identified by the userId");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonNotificationsFromUser(user, false, true));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the not read Notification of a user
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the receiverId
     * @param userId Id of the user to get notification from
     * @return JSON with information about all the not read Notification. Bad Request if invalid ID, user does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/notification/getNotReadFromUser")
    public ResponseEntity<?> getNotReadFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you MUST be the user identified by the userId");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonNotificationsFromUser(user, false, false));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Return the object associated with the Notification
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the receiverId
     * @param notificationId ID of the notification
     * @return JSON representing the object. Bad Request if type different than ObjectType, not authorized or notification does not exist
     */
    @GetMapping
    @RequestMapping("api/notification/getObjectOfNotification")
    public ResponseEntity<?> getObjectOfNotification(@RequestParam() String notificationId){
        try{
            Notification notification= apiGeneralController.getNotificationFromId(notificationId);
            if(!isSenderOrReceiver(notification, apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("you MUST be the sender or the receiver of the notification");
            }
            JsonNode objectNode;
            switch (notification.getType()){
                case "Message":
                    Message message= apiGeneralController.getMessageFromId(notification.getObjectId().toString());
                    objectNode= apiGeneralController.createJsonFromMessage(message);
                    break;
                case "Request":
                    RightRequest request= apiGeneralController.getRequestFromId(notification.getObjectId().toString());
                    objectNode= apiGeneralController.createJsonFromRequest(request);
                    break;
                case "PrivacyNotice":
                    PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromId(notification.getObjectId().toString());
                    objectNode= apiGeneralController.createJsonFromPrivacyNotice(privacyNotice);
                    break;
                default:
                    return ResponseEntity.badRequest().body("object associated with notification does not exist");
            }
            return ResponseEntity.ok(objectNode);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add a notification
     * RESTRICTIONS: sender and receiver MUST be associated with the object indicated with objectId
     * @param body JSON representing the Notification
     * @return OK id successfully added. Bad Request if invalid JSON, type different than Object, not authorized
     */
    @PostMapping
    @RequestMapping("api/notification/add")
    public ResponseEntity<?> add(@RequestBody() String body){
        try{
            Notification notification= apiGeneralController.getNotificationFromJsonString(body);
            if(!apiGeneralController.getAuthenicatedUser().equals(notification.getSender())){
                return ResponseEntity.badRequest().body("you MUST be the sender of the notification");
            }
            if(notification.getRead()==null){
                notification.setRead(false);
            }
            if(!isObjectAssociatedWithUser(notification)){
                return ResponseEntity.badRequest().body("Object is not associated with receiver");
            }
            if(notification.getTime()==null){
                dataBaseService.addNowNotification(notification);
            }
            else{
                dataBaseService.addNotification(notification);
            }
            return ResponseEntity.ok("Notification added successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Delete a notification
     * RESTRICTIONS: receiver MUST be be the one identifies by receiverId
     * @param notificationId ID of the notification to be deleted
     * @return OK if deleted successfully. Bad Request if not authorized or notification does not exist
     */
    @DeleteMapping
    @RequestMapping("api/notification/delete")
    public ResponseEntity<?> delete(@RequestParam() String notificationId){
        try{
            Notification notification= apiGeneralController.getNotificationFromId(notificationId);
            if(!notification.getReceiver().equals(apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("You MUST be the receiver of the notification");
            }
            dataBaseService.deleteNotification(notification);
            return ResponseEntity.ok("Notification deleted successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Make the notification read or unread
     * RESTRICTIONS: receiver MUST be be the one identifies by receiverId
     * @param notificationId ID of the notification
     * @param isReadString isRead value (TRUE or FALSE)
     * @return Ok if value changed correctly. Bad Request if not authorized
     */
    @PostMapping
    @RequestMapping("api/notification/changeIsRead")
    public ResponseEntity<?> changeIsRead(@RequestParam() String notificationId, @RequestParam() String isReadString){
        try {
            Notification notification= apiGeneralController.getNotificationFromId(notificationId);
            if(!isReadString.equalsIgnoreCase("true") && !isReadString.equalsIgnoreCase("false")){
                return ResponseEntity.badRequest().body("Invalid isReadString value. MUST be true or false");
            }
            boolean isRead=Boolean.parseBoolean(isReadString);
            if(!notification.getReceiver().equals(apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("You MUST be the receiver of the notification");
            }
            dataBaseService.changeIsReadNotification(notification, isRead);
            return ResponseEntity.ok("Notification value changed successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private boolean isSenderOrReceiver(Notification notification, User user){
        return notification.getSender().equals(user) || notification.getReceiver().equals(user);
    }

    private boolean isObjectAssociatedWithUser(Notification notification){
        switch (notification.getType()){
            case "Message":
                Optional<Message> message= dataBaseService.getMessageFromID(notification.getObjectId());
                if(message.isPresent() && message.get().getReceiver().equals(notification.getReceiver())){
                    return true;
                }
                break;
            case "Request":
                RightRequest request= dataBaseService.getRequestFromId(notification.getObjectId());
                if(request.getReceiver().equals(notification.getReceiver()) || request.getSender().equals(notification.getReceiver())){
                    if(request.getReceiver().equals(notification.getSender()) || request.getSender().equals(notification.getSender())){
                        return true;
                    }
                }
                break;
            case "PrivacyNotice":
                Optional<PrivacyNotice> privacyNotice= dataBaseService.getPrivacyNoticeFromId(notification.getObjectId());
                IoTApp app= privacyNotice.map(PrivacyNotice::getApp).orElse(null);
                if(dataBaseService.userHasApp(notification.getReceiver(), app) && dataBaseService.userHasApp(notification.getSender(), app)){
                    return true;
                }
        }
        return false;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
