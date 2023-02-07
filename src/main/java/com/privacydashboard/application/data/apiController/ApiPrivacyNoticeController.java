package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ApiPrivacyNoticeController {
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get information about Privacy Notice
     * RESTRICTIONS: NONE
     * @param privacyNoticeId Id of the privacy Notice
     * @return JSON with information about the Privacy Notice. Bad Request if invalid ID, or Privacy Notice does not exist
     */
    @GetMapping
    @RequestMapping("api/privacynotice/get")
    public ResponseEntity<?> get(@RequestParam() String privacyNoticeId){
        try{
            PrivacyNotice privacyNotice=apiGeneralController.getPrivacyNoticeFromId(privacyNoticeId);
            JsonNode privacyNoticeJson= apiGeneralController.createJsonFromPrivacyNotice(privacyNotice);
            return ResponseEntity.ok(privacyNoticeJson);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get information about Privacy Notice
     * RESTRICTIONS: NONE
     * @param appId Id of the app of the privacy Notice
     * @return JSON with information about the Privacy Notice. Bad Request if invalid ID, app or Privacy Notice does not exist
     */
    @GetMapping
    @RequestMapping("api/privacynotice/getFromApp")
    public ResponseEntity<?> getFromApp(@RequestParam() String appId){
        try{
            PrivacyNotice privacyNotice=apiGeneralController.getPrivacyNoticeFromAppId(appId);
            JsonNode privacyNoticeJson= apiGeneralController.createJsonFromPrivacyNotice(privacyNotice);
            return ResponseEntity.ok(privacyNoticeJson);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all Privacy Notices of a user
     * RESTRICTIONS: UserId of a CONTROLLER or DPO, or The one calling the function MUST BE the user identified by the userId
     * @param userId Id of the user to get the Privacy Notices from
     * @return JSON with information about all the Privacy Notices. Bad Request if invalid ID, app or Privacy Notice does not exist
     */
    @GetMapping
    @RequestMapping("api/privacynotice/getFromUser")
    public ResponseEntity<?> getFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            if(user.getRole().equals(Role.SUBJECT) && !apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("MUST be ID of controller or DPO, or you must be the user identified by userId");
            }
            List<IoTApp> appList= dataBaseService.getUserApps(user);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode privacyNoticesArray = mapper.createArrayNode();
            for(IoTApp app : appList){
                PrivacyNotice privacyNotice= dataBaseService.getPrivacyNoticeFromApp(app);
                if(privacyNotice!=null){
                    privacyNoticesArray.add(apiGeneralController.createJsonFromPrivacyNotice(privacyNotice));
                }
            }
            return ResponseEntity.ok(privacyNoticesArray);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add Privacy Notice to an app
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the app
     * @param body JSON representing the privacy notice
     * @return OK if successfully added. Bad Request if app is invalid or user is not authorized
     */
    @PostMapping
    @RequestMapping("api/privacynotice/add")
    public ResponseEntity<?> add(@RequestBody String body){
        try{
            PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromJsonString(true, body);
            IoTApp app= privacyNotice.getApp();

            User user= apiGeneralController.getAuthenicatedUser();
            if(dataBaseService.userHasApp(user, app) && apiGeneralController.isControllerOrDpo(true, null)){
                dataBaseService.addPrivacyNoticeForApp(app, privacyNotice.getText());
                return ResponseEntity.ok("Privacy Notice created successfully");
            }
            else{
                return ResponseEntity.badRequest().body("you MUST be controller and DPO of the app");
            }

        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Add Privacy Notice to various apps
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the apps
     * @param body JSON representing the app's IDs and the text of the Privacy Notice
     * @return OK if successfully added. Bad Request if user is not authorized
     */
    @PostMapping
    @RequestMapping("api/privacynotice/addVarious")
    public ResponseEntity<?> addVarious(@RequestBody String body){
        try{
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO");
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            List<IoTApp> appList= getAppsFromJson(rootNode);
            if(!rootNode.has("text")){
                return ResponseEntity.badRequest().body("text is missing");
            }
            for(IoTApp app : appList){
                dataBaseService.addPrivacyNoticeForApp(app, rootNode.get("text").asText());
            }
            return ResponseEntity.ok("Privacy Notice added to all the apps");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Copy a Privacy Notice to various apps
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the apps
     * @param privacyNoticeId Id of the Privacy Notice to be copied
     * @param body JSON representing the app's IDs
     * @return OK if successfully copied. Bad Request if user is not authorized
     */
    @PostMapping
    @RequestMapping("api/privacynotice/copy")
    public ResponseEntity<?> copy(@RequestParam() String privacyNoticeId, @RequestBody String body){
        try{
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO");
            }
            PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromId(privacyNoticeId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            List<IoTApp> appList= getAppsFromJson(rootNode);
            for(IoTApp app : appList){
                dataBaseService.addPrivacyNoticeForApp(app, privacyNotice.getText());
            }
            return ResponseEntity.ok("Privacy Notice added to all the apps");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Copy a Privacy Notice from an app to various apps
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the apps
     * @param appId Id of the App of the Privacy Notice to be copied
     * @param body JSON representing the app's IDs
     * @return OK if successfully copied. Bad Request if user is not authorized
     */
    @PostMapping
    @RequestMapping("api/privacynotice/copyFromApp")
    public ResponseEntity<?> copyFromApp(@RequestParam() String appId, @RequestBody String body){
        try{
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO");
            }
            PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromAppId(appId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new StringReader(body));
            List<IoTApp> appList= getAppsFromJson(rootNode);
            for(IoTApp app : appList){
                dataBaseService.addPrivacyNoticeForApp(app, privacyNotice.getText());
            }
            return ResponseEntity.ok("Privacy Notice added to all the apps");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Delete a Privacy Notice
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the app
     * @param privacyNoticeId ID of the Privacy Notice to be deleted
     * @return OK if successfully deleted. Bad Request if user is not authorized or privacy notice does not exist
     */
    @DeleteMapping
    @RequestMapping("api/privacynotice/delete")
    public ResponseEntity<?> delete(@RequestParam() String privacyNoticeId){
        try{
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO");
            }
            PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromId(privacyNoticeId);
            IoTApp app= privacyNotice.getApp();
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO of the APP");
            }
            dataBaseService.deletePrivacyNotice(privacyNotice);
            return ResponseEntity.ok("Privacy Notice successfully deleted");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Delete a Privacy Notice from an app
     * RESTRICTIONS: The one calling the function MUST BE controller or DPO of the app
     * @param appId ID of the app from which the Privacy Notice must be deleted
     * @return OK if successfully deleted. Bad Request if user is not authorized or privacy notice does not exist
     */
    @DeleteMapping
    @RequestMapping("api/privacynotice/deleteFromApp")
    public ResponseEntity<?> deleteFromApp(@RequestParam() String appId){
        try{
            if(!apiGeneralController.isControllerOrDpo(true, null)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO");
            }
            PrivacyNotice privacyNotice= apiGeneralController.getPrivacyNoticeFromAppId(appId);
            IoTApp app= privacyNotice.getApp();
            if(!apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                return ResponseEntity.badRequest().body("you must be a Controller or DPO of the APP");
            }
            dataBaseService.deletePrivacyNotice(privacyNotice);
            return ResponseEntity.ok("Privacy Notice successfully deleted");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private List<IoTApp> getAppsFromJson(JsonNode rootNode) throws IllegalArgumentException{
        if(!rootNode.has("appsId") ){
            throw new IllegalArgumentException("invalid JSON");
        }
        JsonNode appsIdJson = rootNode.get("appsId");
        if (!appsIdJson.isArray()) {
            throw new IllegalArgumentException("invalid JSON");
        }
        List<IoTApp> appList= new LinkedList<>();
        for (JsonNode appId : appsIdJson) {
            IoTApp app=apiGeneralController.getAppFromId(appId.asText());
            if(apiGeneralController.userHasApp(apiGeneralController.getAuthenicatedUser(), app)){
                appList.add(app);
            }
            else{
                throw new IllegalArgumentException("You don't have all the apps");
            }
        }
        return appList;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
