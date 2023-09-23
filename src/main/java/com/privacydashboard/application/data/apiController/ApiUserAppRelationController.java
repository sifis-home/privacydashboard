package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ApiUserAppRelationController {

    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get information about relation between user and app
     * RESTRICTIONS: Controller/DPO of the app or userId must belong to the one calling this function
     * @param userId ID of the user
     * @param appId ID of the app
     * @return Json with information about relation between user and app. Bad Request if Id not valid or not allowed to get information
     */
    @GetMapping
    @RequestMapping("api/userapprelation/get")
    public ResponseEntity<?> get(@RequestParam() String userId, @RequestParam() String appId){
        try{
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), apiGeneralController.getAppFromId(appId))){
                return ResponseEntity.badRequest().body("you must be connected with the app");
            }
            if(!apiGeneralController.isAuthenticatedUserId(userId) && !apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId or the controller/DPO of the app");
            }

            UserAppRelation userAppRelation= apiGeneralController.getUserAppRelationByUserIdAndAppId(userId, appId);
            ObjectNode userAppRelationJson= apiGeneralController.createJsonFromUserAppRelation(userAppRelation);
            return ResponseEntity.ok(userAppRelationJson);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Associate a user with an app
     * RESTRICTIONS: userId must belong to the one calling this function
     * @param userId ID of the User
     * @param appId ID of the app
     * @return OK if relation added successfully. Bad request if IDs invalid, user or app does not exist, user already has app, user is not the one calling this function
     */
    @PostMapping
    @RequestMapping("api/userapprelation/add")
    public ResponseEntity<?> add(@RequestParam() String userId, @RequestParam() String appId){
        try{
            User user=apiGeneralController.getUserFromId(userId);
            IoTApp app=apiGeneralController.getAppFromId(appId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId");
            }
            if(!apiGeneralController.userHasApp(user,app)){
                dataBaseService.addUserApp(user, app);
                return ResponseEntity.ok("app added successfully");
            }
            else{
                return ResponseEntity.badRequest().body("user already has this app");
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add some consenses for the user identified with userId in the app identified with the appId
     * RESTRICTIONS: The one calling the function MUST be controller/DPO of the app
     * @param userId ID of the user
     * @param appId ID of the app
     * @param body JSON representing the consenses to add
     * @return OK if successfully added. Bad Request if user or app don't exist, invalid JSON, not authorized
     */
    @PostMapping
    @RequestMapping("api/userapprelation/addConsenses")
    public ResponseEntity<?> addConsenses(@RequestParam() String userId, @RequestParam() String appId, @RequestBody() String body){
        try{
            IoTApp app= apiGeneralController.getAppFromId(appId);
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("You must be a controller/DPO");
            }
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                return ResponseEntity.badRequest().body("you must be connected with the app");
            }
            UserAppRelation userAppRelation= apiGeneralController.getUserAppRelationByUserIdAndAppId(userId, appId);

            JsonNode rootNode = new ObjectMapper().readTree(new StringReader(body));
            if(!rootNode.has("consenses") || !rootNode.isArray()){
                return ResponseEntity.badRequest().body("invalid JSON");
            }
            List<String> newConsenses= new LinkedList<>();
            for (JsonNode consens : rootNode.get("consenses")) {
                newConsenses.add(consens.asText());
            }
            dataBaseService.addConsenses(userAppRelation, newConsenses);
            return ResponseEntity.ok("consenses added successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Dissociate a user with an app
     * RESTRICTIONS: userId must belong to the one calling this function
     * @param userId ID of the User
     * @param appId ID of the App
     * @return OK if relation deleted successfully. Bad request if IDs invalid, user or app does not exist, user does not have app, user is not the one calling this function
     */
    @DeleteMapping
    @RequestMapping("api/userapprelation/delete")
    public ResponseEntity<?> delete(@RequestParam() String userId, @RequestParam() String appId){
        try{
            User user=apiGeneralController.getUserFromId(userId);
            IoTApp app=apiGeneralController.getAppFromId(appId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId");
            }

            if(apiGeneralController.userHasApp(user,app) && dataBaseService.deleteAppFromUser(user, app)){
                return ResponseEntity.ok("app removed successfully");
            }
            else{
                return ResponseEntity.badRequest().body("user doesn't have this app");
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Remove some consenses for the user identified with userId in the app identified with the appId
     * RESTRICTIONS: The one calling the function MUST be controller/DPO of the app
     * @param userId ID of the user
     * @param appId ID of the app
     * @param body JSON representing the consenses to remove
     * @return OK if successfully removed. Bad Request if user or app don't exist, invalid JSON, not authorized
     */
    @DeleteMapping
    @RequestMapping("api/userapprelation/removeConsenses")
    public ResponseEntity<?> removeConsenses(@RequestParam() String userId, @RequestParam() String appId, @RequestBody() String body){
        try{
            IoTApp app= apiGeneralController.getAppFromId(appId);
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("You must be a controller/DPO");
            }
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                return ResponseEntity.badRequest().body("you must be connected with the app");
            }
            UserAppRelation userAppRelation= apiGeneralController.getUserAppRelationByUserIdAndAppId(userId, appId);

            JsonNode rootNode = new ObjectMapper().readTree(new StringReader(body));
            if(!rootNode.has("consenses") || !rootNode.isArray()){
                return ResponseEntity.badRequest().body("invalid JSON");
            }
            List<String> newConsenses= new LinkedList<>();
            for (JsonNode consens : rootNode.get("consenses")) {
                newConsenses.add(consens.asText());
            }
            dataBaseService.removeConsensesFromUserAppRelation(userAppRelation, newConsenses);
            return ResponseEntity.ok("consenses removed successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Remove all the consenses for the user identified with userId in the app identified with the appId
     * RESTRICTIONS: The one calling the function MUST be controller/DPO of the app
     * @param userId ID of the user
     * @param appId ID of the app
     * @return OK if successfully removed. Bad Request if user or app don't exist, invalid JSON, not authorized
     */
    @DeleteMapping
    @RequestMapping("api/userapprelation/removeAllConsenses")
    public ResponseEntity<?> removeAllConsenses(@RequestParam() String userId, @RequestParam() String appId){
        try{
            IoTApp app= apiGeneralController.getAppFromId(appId);
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("You must be a controller/DPO");
            }
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                return ResponseEntity.badRequest().body("you must be connected with the app");
            }
            UserAppRelation userAppRelation= apiGeneralController.getUserAppRelationByUserIdAndAppId(userId, appId);
            dataBaseService.removeAllConsenses(userAppRelation);
            return ResponseEntity.ok("consenses removed successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
