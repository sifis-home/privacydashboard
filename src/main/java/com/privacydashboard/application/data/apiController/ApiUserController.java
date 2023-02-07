package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class ApiUserController {
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get information about a user
     * RESTRICTIONS: NONE
     * @param userId Id of the User to get information from
     * @return Json object representing the User. Bad request if User with that Id does not exist
     */
    @GetMapping
    @RequestMapping("api/user/get")
    public ResponseEntity<?> get(@RequestParam() String userId){
        try {
            User user= apiGeneralController.getUserFromId(userId);
            ObjectNode userJson = apiGeneralController.createJsonFromUser(user);
            userJson.remove("mail");
            userJson.remove("hashedPassword");
            return ResponseEntity.ok(userJson);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get information about a user, including the mail
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the userId, a contact of him or userId is a Controller or DPO
     * @param userId Id of the User to get information from
     * @return Json object representing the User. Bad request if User with that Id does not exist or the one calling the function is not allowed to access these informations
     */
    @GetMapping
    @RequestMapping("api/user/getDetailed")
    public ResponseEntity<?> getDetailed(@RequestParam() String userId){
        try {
            User user= apiGeneralController.getUserFromId(userId);
            if(!users1CanGetDetailOfUser2(apiGeneralController.getAuthenicatedUser(), user)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId");
            }
            ObjectNode userJson = apiGeneralController.createJsonFromUser(user);
            userJson.remove("hashedPassword");
            return ResponseEntity.ok(userJson);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the contacts of a user, excluding password
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the userId
     * @param userId Id of the User to get information from
     * @return Json object representing all the contacts. Bad request if User with that Id does not exist or the one calling the function is not the user identified by the userId
     */
    @GetMapping
    @RequestMapping("api/user/getAllContacts")
    public ResponseEntity<?> getAllContacts(@RequestParam() String userId){
        try {
            User user= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you must be the user identified by the userId");
            }
            List<User> contacts= dataBaseService.getAllContactsFromUser(user);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode contactJson;
            ArrayNode contactsArray = mapper.createArrayNode();
            for(User contact : contacts){
                contactJson= apiGeneralController.createJsonFromUser(contact);
                contactJson.remove("hashedPassword");
                contactsArray.add(contactJson);
            }

            return ResponseEntity.ok(contactsArray);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the apps of a user if user=controller/dpo. Get only the app you have if user=subject
     * RESTRICTIONS: user MUST be controller/DPO OR You must be controller/dpo of some apps of the user
     * @param userId ID of the user to get apps from
     * @return JSON representing all the apps. Bad request if user does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/user/getApps")
    public  ResponseEntity<?> getApps(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            List<IoTApp> appList;
            User authenticatedUser= apiGeneralController.getAuthenicatedUser();
            if(user.getRole().equals(Role.CONTROLLER) || user.getRole().equals(Role.DPO)) {
                appList = dataBaseService.getUserApps(user);
            }
            else if(authenticatedUser.getRole().equals(Role.CONTROLLER) || authenticatedUser.getRole().equals(Role.DPO)){
                appList= dataBaseService.getAppsFrom2Users(user, authenticatedUser);
            }
            else{
                return ResponseEntity.badRequest().body("User must be Controller/DPO or you must be associated with that user");
            }
            ArrayNode appsArray= new ObjectMapper().createArrayNode();
            for(IoTApp app : appList){
                appsArray.add(apiGeneralController.createJsonFromApp(app));
            }
            return ResponseEntity.ok(appsArray);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Create a new user
     * RESTRICTIONS: NONE
     * @param body a JSON with the information about the user. It MUST include the NAME, ROLE, PASSWORD
     * @return OK If user successfully added. Bad Request if Json is invalid, ID is invalid, there is already a user with that ID
     */
    @PostMapping
    @RequestMapping("api/user/add")
    public ResponseEntity<?> add(@RequestBody String body){
        try {
            User user=apiGeneralController.getUserFromJsonString(true, body);
            if(user.getId()!=null && dataBaseService.getApp(user.getId()).isPresent()){
                return ResponseEntity.badRequest().body("cannot create this user");
            }
            dataBaseService.addUser(user);
            return ResponseEntity.ok("user created successfully");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Delete the user identified by the userId
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the userId
     * @param userId Id of the user to be deleted
     * @return Ok if successfully deleted. BadRequest if ID not valid, or the one calling the function is not the user identified by the userId
     */
    @DeleteMapping
    @RequestMapping("api/user/delete")
    public ResponseEntity<?> delete(@RequestParam() String userId){
        try {
            User user= apiGeneralController.getUserFromId(userId);
            if(apiGeneralController.isAuthenticatedUserId(userId)){
                dataBaseService.deleteUser(user.getId());
                return ResponseEntity.ok("app deleted successfully");
            }
            else{
                return ResponseEntity.badRequest().body("you must be the user to be deleted");
            }
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update some values of the user identified by the userID
     * RESTRICTIONS: The one calling the function MUST BE the user identified by the userId
     * @param userId Id of the user to be updated
     * @param body JSON with values to be updated
     * @return Ok if successfully updated, BadRequest if JSON invalid, ID does not exist, User performing the action is not the one with that ID
     */
    @PutMapping
    @RequestMapping("api/user/update")
    public ResponseEntity<?> update(@RequestParam String userId, @RequestBody String body){
        try{
            User user1= apiGeneralController.getUserFromId(userId);
            if(!apiGeneralController.isAuthenticatedUserId(userId)){
                return ResponseEntity.badRequest().body("you must be the user to be updated");
            }
            User user2= apiGeneralController.getUserFromJsonString(false, body);
            dataBaseService.changeValuesForUser(user1, user2.getName(), user2.getHashedPassword(), user2.getMail());
            return ResponseEntity.ok("user updated successfully");
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    private boolean users1CanGetDetailOfUser2(User user1, User user2){
        if(user1.equals(user2)){
            return true;
        }
        if(dataBaseService.getAllContactsFromUser(user1).contains(user2)){
            return true;
        }
        return user2.getRole().equals(Role.CONTROLLER) || user2.getRole().equals(Role.DPO);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
