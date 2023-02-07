package com.privacydashboard.application.data.apiController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.*;
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
public class ApiRightRequestController {
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private ApiGeneralController apiGeneralController;

    /**
     * Get information about a Right Request
     * RESTRICTIONS: The one calling the function must be the sender or the receiver of the request
     * @param requestId ID of the request
     * @return JSON with information about the request. Bad Request if request does not exist or not authorized
     */
    @GetMapping
    @RequestMapping("api/rightrequest/get")
    public ResponseEntity<?> get(@RequestParam() String requestId){
        try{
            RightRequest request= apiGeneralController.getRequestFromId(requestId);
            if(!isSenderOrReceiver(request, apiGeneralController.getAuthenicatedUser())){
                return ResponseEntity.badRequest().body("You MUST be the sender or the receiver of the request");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonFromRequest(request));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the Requests of a user
     * RESTRICTIONS: NONE BUT get only the requests associated with the one calling the function
     * @param userId Id of the user to get requests from
     * @return JSON with information about all the requests associated. Bad Request if user does not exist
     */
    @GetMapping
    @RequestMapping("api/rightrequest/getAllFromUser")
    public ResponseEntity<?> getAllFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            List<RightRequest> requestList= getRequestList(user, true, true);
            return ResponseEntity.ok(apiGeneralController.createJsonRequestCheckAuthorizedUser(requestList, apiGeneralController.getAuthenicatedUser()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all handled Requests of a user
     * RESTRICTIONS: NONE BUT get only the requests associated with the one calling the function
     * @param userId Id of the user to get requests from
     * @return JSON with information about all the requests associated. Bad Request if user does not exist
     */
    @GetMapping
    @RequestMapping("api/rightrequest/getHandledFromUser")
    public ResponseEntity<?> getHandledFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            List<RightRequest> requestList= getRequestList(user, false, true);
            return ResponseEntity.ok(apiGeneralController.createJsonRequestCheckAuthorizedUser(requestList, apiGeneralController.getAuthenicatedUser()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all unhandled Requests of a user
     * RESTRICTIONS: NONE BUT get only the requests associated with the one calling the function
     * @param userId Id of the user to get requests from
     * @return JSON with information about all the requests associated. Bad Request if user does not exist
     */
    @GetMapping
    @RequestMapping("api/rightrequest/getNotHandledFromUser")
    public ResponseEntity<?> getNotHandledFromUser(@RequestParam() String userId){
        try{
            User user= apiGeneralController.getUserFromId(userId);
            List<RightRequest> requestList= getRequestList(user, false, false);
            return ResponseEntity.ok(apiGeneralController.createJsonRequestCheckAuthorizedUser(requestList, apiGeneralController.getAuthenicatedUser()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the requests associated to an app
     * RESTRICTIONS: NONE BUT get only the requests associated with the one calling the function
     * @param appId ID of the app to get requests from
     * @param isHandled optional value. Filter by handled or unhandled requests
     * @return list of requests from that app. Bad request if app does not exist, invalid parameter isHandled
     */
    @PostMapping
    @RequestMapping("api/rightrequest/getFromApp")
    public ResponseEntity<?> getFromApp(@RequestParam() String appId, @RequestParam(required = false) String isHandled){
        try{
            IoTApp app= apiGeneralController.getAppFromId(appId);
            List<RightRequest> requestList;
            if(isHandled==null){
                requestList= getRequestList(apiGeneralController.getAuthenicatedUser(), true, true);
            }
            else if(isHandled.equalsIgnoreCase("true") || isHandled.equalsIgnoreCase("false")){
                requestList = getRequestList(apiGeneralController.getAuthenicatedUser(), false, Boolean.parseBoolean(isHandled));
            }
            else{
                return ResponseEntity.badRequest().body("invalid isHandled parameter");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonRequestOfApp(requestList, app));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all the requests of a specific type (WITHDRAWCONSENT, COMPLAIN, ERASURE, DELETEEVERYTHING, INFO, PORTABILITY)
     * RESTRICTIONS: NONE BUT get only the requests associated with the one calling the function
     * @param rightType ID of the app to get requests from
     * @param isHandled optional value. Filter by handled or unhandled requests
     * @return list of requests from that app. Bad request if invalid right type, invalid parameter isHandled
     */
    @PostMapping
    @RequestMapping("api/rightrequest/getFromRightType")
    public ResponseEntity<?> getFromRightType(@RequestParam() String rightType, @RequestParam(required = false) String isHandled){
        try{
            List<RightRequest> requestList;
            if(isHandled==null){
                requestList= getRequestList(apiGeneralController.getAuthenicatedUser(), true, true);
            }
            else if(isHandled.equalsIgnoreCase("true") || isHandled.equalsIgnoreCase("false")){
                requestList = getRequestList(apiGeneralController.getAuthenicatedUser(), false, Boolean.parseBoolean(isHandled));
            }
            else{
                return ResponseEntity.badRequest().body("invalid isHandled parameter");
            }
            return ResponseEntity.ok(apiGeneralController.createJsonRequestOfRightType(requestList, rightType));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add a new Right Request. If handled missing it is put to false. If time is missing it is considered the actual time.
     * If receiver is missing it is considered one of the controllers of the app.
     * LIMITAIONS: The one calling the function must be the sender of the request
     * @param body JSON representing the request
     * @return OK if successfully added. Bad Request if invalid JSON, not authorized or receiver doesn't have the app.
     */
    @PostMapping
    @RequestMapping("api/rightrequest/add")
    public ResponseEntity<?> add(@RequestBody() String body){
        try{
            RightRequest request= apiGeneralController.getRequestFromJsonString(body, true);
            if(!apiGeneralController.getAuthenicatedUser().equals(request.getSender())){
                return ResponseEntity.badRequest().body("you MUST be the sender of the request");
            }
            if(!apiGeneralController.userHasApp(request.getSender(), request.getApp())){
                return ResponseEntity.badRequest().body("you MUST have the associated app");
            }
            if(request.getId()!=null && dataBaseService.getRequestFromId(request.getId())!=null){
                return ResponseEntity.badRequest().body("there is already a Request with that ID");
            }
            if(request.getReceiver()!=null && !apiGeneralController.userHasApp(request.getReceiver(), request.getApp())){
                return ResponseEntity.badRequest().body("receiver of the request MUST have that app");
            }
            if(request.getReceiver()==null){
                request.setReceiver(dataBaseService.getControllersFromApp(request.getApp()).get(0));
            }
            if(request.getHandled()==null){
                request.setHandled(false);
            }
            if(request.getTime()==null){
                dataBaseService.addNowRequest(request);
            }
            else{
                dataBaseService.addRequest(request);
            }
            return ResponseEntity.ok("request added successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * Delete a RightRequest
     * RESTRICTIONS: The one calling the function MUST be the sender of the request
     * @param requestId ID of the request to be deleted
     * @return OK if successfully deleted. Bad Response if Request does not exist or not authorized
     */
    @DeleteMapping
    @RequestMapping("api/rightrequest /delete")
    public ResponseEntity<?> delete(@RequestParam() String requestId){
        try{
            User user= apiGeneralController.getAuthenicatedUser();
            RightRequest request= apiGeneralController.getRequestFromId(requestId);
            if(request.getSender().equals(user)){
                dataBaseService.deleteRequest(request);
                return ResponseEntity.ok("Request successfully deleted");
            }
            else{
                return ResponseEntity.badRequest().body("You MUST be the sender of the request");
            }
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Add a response
     * RESTRICTIONS: The one calling the function MUST be the receiver of the request and a Controller/DPO of that app
     * @param requestId ID of the request specified
     * @return OK if response successfully added. Bad Response if Request does not exist or not authorized
     */
    @PostMapping
    @RequestMapping("api/rightrequest/addResponse")
    public ResponseEntity<?> addResponse(@RequestParam() String requestId, @RequestBody() String body){
        try{
            User user= apiGeneralController.getAuthenicatedUser();
            RightRequest request= apiGeneralController.getRequestFromId(requestId);
            if(!request.getReceiver().equals(user)){
                return ResponseEntity.badRequest().body("You MUST be the receiver of the request");
            }
            if(!apiGeneralController.userHasApp(user, request.getApp())){
                return ResponseEntity.badRequest().body("You MUST have the app of the request");
            }
            if(user.getRole().equals(Role.SUBJECT)){
                return ResponseEntity.badRequest().body("You MUST be a controller or DPO");
            }

            JsonNode rootNode = new ObjectMapper().readTree(new StringReader(body));
            if(!rootNode.has("response")){
                return ResponseEntity.badRequest().body("JSON invalid: must include response value");
            }
            request.setResponse(rootNode.get("response").asText());
            dataBaseService.changeRightRequest(request);
            return ResponseEntity.ok("Response added successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * change Handled parameter of a request
     * RESTRICTIONS: The one calling the function MUST be the receiver of the request and a Controller/DPO of that app
     * @param requestId ID of the request specified
     * @return OK if value correctly changed. Bad Response if Request does not exist or not authorized
     */
    @PostMapping
    @RequestMapping("api/rightrequest/changeHandled")
    public ResponseEntity<?> changeHandled(@RequestParam() String requestId, @RequestParam() String isHandled){
        try{
            User user= apiGeneralController.getAuthenicatedUser();
            RightRequest request= apiGeneralController.getRequestFromId(requestId);
            if(!request.getReceiver().equals(user)){
                return ResponseEntity.badRequest().body("You MUST be the receiver of the request");
            }
            if(!apiGeneralController.userHasApp(user, request.getApp())){
                return ResponseEntity.badRequest().body("You MUST have the app of the request");
            }
            if(user.getRole().equals(Role.SUBJECT)){
                return ResponseEntity.badRequest().body("You MUST be a controller or DPO");
            }

            if(!isHandled.equalsIgnoreCase("true") && !isHandled.equalsIgnoreCase("false")){
                return ResponseEntity.badRequest().body("handled parameter invalid: must be true or false");
            }
            request.setHandled(Boolean.parseBoolean(isHandled));
            dataBaseService.changeRightRequest(request);
            return ResponseEntity.ok("Handled parameter successfully changed");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Change tha parameters other and/or details
     * RESTRICTIONS: The one calling the function MUST be the sender of the request
     * @param requestId ID of the request specified
     * @return OK if parameters successfully changed. Bad Response if Request does not exist or not authorized
     */
    @PostMapping
    @RequestMapping("api/rightrequest/update")
    public ResponseEntity<?> update(@RequestParam() String requestId, @RequestBody() String body){
        try{
            User user= apiGeneralController.getAuthenicatedUser();
            RightRequest request= apiGeneralController.getRequestFromId(requestId);
            if(!request.getSender().equals(user)){
                return ResponseEntity.badRequest().body("You MUST be the sender of the request");
            }
            if(!apiGeneralController.userHasApp(user, request.getApp())){
                return ResponseEntity.badRequest().body("You MUST have the app of the request");
            }

            JsonNode rootNode = new ObjectMapper().readTree(new StringReader(body));
            if(!rootNode.has("other") && !rootNode.has("details")){
                return ResponseEntity.badRequest().body("JSON invalid: must include other and/or details value");
            }
            if(rootNode.has("other")){
                request.setOther(rootNode.get("other").asText());
            }
            if(rootNode.has("details")){
                request.setDetails(rootNode.get("details").asText());
            }
            dataBaseService.changeValuesOfRightRequest(request);
            return ResponseEntity.ok("Response added successfully");
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e){
            return ResponseEntity.badRequest().body("invalid JSON");
        }
    }

    /**
     * @param user requests from/to this user
     * @param all TRUE: return both handled and not handled request
     * @param handled if param handledAndNot=FALSE, if TRUE return only handled request, if FALSE return only not handled request
     * @return List with all the requests
     */
    private List<RightRequest> getRequestList(User user, boolean all, boolean handled){
        List<RightRequest> requestList= new LinkedList<>();
        if(all){
            requestList.addAll(dataBaseService.getAllRequestsFromReceiver(user));
            requestList.addAll(dataBaseService.getAllRequestsFromSender(user));
        }
        else {
            if (handled) {
                requestList.addAll(dataBaseService.getHandledRequestsFromReceiver(user));
                requestList.addAll(dataBaseService.getHandledRequestsFromSender(user));
            } else {
                requestList.addAll(dataBaseService.getPendingRequestsFromReceiver(user));
                requestList.addAll(dataBaseService.getPendingRequestsFromSender(user));
            }
        }
        return requestList;
    }

    private boolean isSenderOrReceiver(RightRequest request, User user){
        return request.getSender().equals(user) || request.getReceiver().equals(user);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(ex.getParameterName() + " parameter is missing");
    }
}
