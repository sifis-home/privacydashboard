package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.*;
import com.privacydashboard.application.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataBaseService {
    private final AuthenticatedUser authenticatedUser;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final IoTAppRepository ioTAppRepository;
    private final UserAppRelationRepository userAppRelationRepository;
    private final RightRequestRepository rightRequestRepository;
    private final PrivacyNoticeRepository privacyNoticeRepository;
    private final NotificationRepository notificationRepository;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public DataBaseService(AuthenticatedUser authenticatedUser, PasswordEncoder passwordEncoder, UserRepository userRepository , MessageRepository messageRepository,
                           IoTAppRepository ioTAppRepository, UserAppRelationRepository userAppRelationRepository,
                           RightRequestRepository rightRequestRepository, PrivacyNoticeRepository privacyNoticeRepository,
                           NotificationRepository notificationRepository) {
        this.authenticatedUser= authenticatedUser;
        this.passwordEncoder= passwordEncoder;
        this.userRepository= userRepository;
        this.messageRepository= messageRepository;
        this.ioTAppRepository= ioTAppRepository;
        this.userAppRelationRepository= userAppRelationRepository;
        this.rightRequestRepository= rightRequestRepository;
        this.privacyNoticeRepository= privacyNoticeRepository;
        this.notificationRepository= notificationRepository;
    }

    // USER REPOSITORY

    public Optional<User> getUser(UUID id) {
        return userRepository.findById(id);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    /**
     * if 1 parameter is null, that parameter is not gonna change
     */
    public void changeValuesForUser(User user, String name, String hashedPassword, String mail){
        String newName= name==null ? user.getName() : name;
        String newHashedPassword= hashedPassword==null ? user.getHashedPassword() : hashedPassword;
        String newMail= mail==null ? user.getMail() : mail;
        userRepository.changeValues(user.getId(), newName, newHashedPassword, newMail);
    }



    // IOTAPP REPOSITORY

    public Optional<IoTApp> getApp(UUID id) {
        return ioTAppRepository.findById(id);
    }

    public void addApp(IoTApp app){
        ioTAppRepository.save(app);
    }

    public void deleteApp(UUID id){
        ioTAppRepository.deleteById(id);
    }

    /**
     * if 1 parameter is null, that parameter is not gonna change
     */
    public void changeNameAndDescriptionForApp(IoTApp app, String name, String description){
        String newName= name==null ? app.getName() : name;
        String newDescription= description==null ? app.getDescription() : description;
        ioTAppRepository.changeNameAndDescription(app.getId(), newName, newDescription);
    }

    /**
     * if 1 parameter is null, that parameter is not gonna change
     */
    public void updateQuestionnaireForApp(IoTApp app, QuestionnaireVote vote, String[] detailVote, Hashtable<Integer, String> optionalAnswers){
        QuestionnaireVote newVote= vote==null ? app.getQuestionnaireVote() : vote;
        String[] newDetailVote= detailVote==null ? app.getDetailVote() : detailVote;
        Hashtable<Integer, String> newOptionalAnswers= optionalAnswers==null ? app.getOptionalAnswers() : optionalAnswers;
        ioTAppRepository.changeQuestionnaire(app.getId(), newVote, newDetailVote, newOptionalAnswers);
    }

    // USERAPPRELATION REPOSITORY

    public Optional<UserAppRelation> getUserAppRelation(UUID id){
        return userAppRelationRepository.findById(id);
    }

    public UserAppRelation getUserAppRelationByUserAndApp(User user, IoTApp app){
        return userAppRelationRepository.findByUserAndApp(user, app);
    }

    public List<IoTApp> getUserApps(User user){
        return userAppRelationRepository.getIoTAppsFromUser(user);
    }

    public List<IoTApp> getUserAppsWithVote(User user){
        List<IoTApp> appList= userAppRelationRepository.getIoTAppsFromUser(user);
        appList.removeIf(app -> app.getQuestionnaireVote() == null);
        return appList;
    }

    public List<IoTApp> getUserAppsByName(User user, String name){
        return userAppRelationRepository.getIoTAppsFromUserFilterByName(user, name);
    }

    public List<IoTApp> getUserAppsWithVoteByName(User user, String name){
        List<IoTApp> appList= userAppRelationRepository.getIoTAppsFromUserFilterByName(user, name);
        appList.removeIf(app -> app.getQuestionnaireVote() == null);
        return appList;
    }

    public String[] getConsensesFromUserAndApp(User user, IoTApp app){
        UserAppRelation userAppRelation=userAppRelationRepository.findByUserAndApp(user, app);
        if(userAppRelation==null){
            return null;
        }
        return userAppRelation.getConsenses();
    }

    public void removeConsensesFromUserAppRelation(UserAppRelation userAppRelation, List<String> consenses){
        if(userAppRelation.getConsenses()==null){
            return;
        }
        List<String> actualConsenses= new ArrayList<>(List.of(userAppRelation.getConsenses()));
        for(String consens : consenses){
            if(actualConsenses.contains(consens)){
                actualConsenses.remove(consens);
                if(actualConsenses.isEmpty()){
                    break;
                }
            }
        }
        userAppRelationRepository.updateConsenses(userAppRelation.getId(), actualConsenses.toArray(new String[actualConsenses.size()]));
    }

    public void removeAllConsenses(UserAppRelation userAppRelation){
        userAppRelationRepository.updateConsenses(userAppRelation.getId(), null);
    }

    public void addConsenses(UserAppRelation userAppRelation, List<String> consenses){
        List<String> newConsenses= new ArrayList<>();
        if(userAppRelation.getConsenses()!=null){
            newConsenses.addAll(List.of(userAppRelation.getConsenses()));
        }
        newConsenses.addAll(consenses);
        userAppRelationRepository.updateConsenses(userAppRelation.getId(), newConsenses.toArray(new String[newConsenses.size()]));
    }

    public List<User> getUsersFromApp(IoTApp app){
        return userAppRelationRepository.getUsersFromApp(app);
    }

    public List<User> getAllContactsFromUser(User user){
        if(user.getRole()==Role.SUBJECT){
            return userAppRelationRepository.getAllContactsFilterBy2Roles(user, Role.CONTROLLER, Role.DPO);
        }
        else{
            return userAppRelationRepository.getAllDPOOrControllerContacts(user);
        }
    }

    public List<User> getAllContactsFromUserFilterByName(User user, String name){
        if(user.getRole()==Role.SUBJECT){
            return userAppRelationRepository.getAllContactsFilterBy2RolesFilterByName(user, Role.CONTROLLER, Role.DPO, name);
        }
        else{
            return userAppRelationRepository.getAllDPOOrControllerContactsFilterByName(user, name);
        }
    }

    public List<IoTApp> getAppsFrom2Users(User user1, User user2){
        return userAppRelationRepository.getAppsFrom2Users(user1, user2);
    }

    public List<User> getControllersFromApp(IoTApp app){
        return userAppRelationRepository.getUsersFromAppFilterByRole(app, Role.CONTROLLER);
    }

    public List<User> getDPOsFromApp(IoTApp app){
        return userAppRelationRepository.getUsersFromAppFilterByRole(app, Role.DPO);
    }

    public List<User> getSubjectsFromApp(IoTApp app){
        return userAppRelationRepository.getUsersFromAppFilterByRole(app, Role.SUBJECT);
    }

    public void addUserApp(User user, IoTApp app){
        UserAppRelation userAppRelation=new UserAppRelation();
        userAppRelation.setApp(app);
        userAppRelation.setUser(user);
        userAppRelationRepository.save(userAppRelation);
    }

    public boolean userHasApp(User user, IoTApp app){
        return userAppRelationRepository.findByUserAndApp(user, app)!=null;
    }

    public boolean deleteAppFromUser(User user, IoTApp app){
        UserAppRelation userAppRelation=userAppRelationRepository.findByUserAndApp(user, app);
        if(userAppRelation==null){
            return false;
        }
        else{
            userAppRelationRepository.delete(userAppRelation);
            return true;
        }
    }

    // MESSAGE REPOSITORY

    public Optional<Message> getMessageFromID(UUID id){
        return messageRepository.findById(id);
    }

    public List<Message> getConversationFromUsers(User user1, User user2){
        return messageRepository.getConversationFromUsers(user1, user2);
    }

    public List<User> getUserConversationFromUser(User user){
        return messageRepository.getUserConversationFromUser(user);
    }

    public List<User> getUserConversationFromUserFilterByName(User user, String name){
        return messageRepository.getUserConversationFromUserFilterByName(user, name);
    }

    public void addMessage(Message message){
        messageRepository.save(message);
        addNotification(message, "Message", message.getReceiver(), message.getSender(), message.getSender().getName() + " sent you a message");
    }

    public void addNowMessage(Message message){
        message.setTime(LocalDateTime.now());
        messageRepository.save(message);
        addNotification(message, "Message", message.getReceiver(), message.getSender(), message.getSender().getName() + " sent you a message");
    }

    // RIGHT REQUEST REPOSITORY
    public RightRequest getRequestFromId(UUID id){
        return rightRequestRepository.findById(id).isPresent() ? rightRequestRepository.findById(id).get() : null;
    }

    public List<RightRequest> getAllRequestsFromReceiver(User user){
        return rightRequestRepository.findAllByReceiverOrderByTimeDesc(user);
    }

    public List<RightRequest> getPendingRequestsFromReceiver(User user){
        return rightRequestRepository.findAllByReceiverAndHandledOrderByTimeDesc(user, false);
    }

    public List<RightRequest> getHandledRequestsFromReceiver(User user){
        return rightRequestRepository.findAllByReceiverAndHandledOrderByTimeDesc(user, true);
    }

    public List<RightRequest> getAllRequestsFromSender(User user){
        return rightRequestRepository.findAllBySenderOrderByTimeDesc(user);
    }

    public List<RightRequest> getPendingRequestsFromSender(User user){
        return rightRequestRepository.findAllBySenderAndHandledOrderByTimeDesc(user, false);
    }

    public List<RightRequest> getHandledRequestsFromSender(User user){
        return rightRequestRepository.findAllBySenderAndHandledOrderByTimeDesc(user, true);
    }

    public void addRequest(RightRequest request){
        rightRequestRepository.save(request);
        addNotification(request, "Request", request.getReceiver(), request.getSender(), request.getSender().getName() + " sent you a right request");
    }

    public void addNowRequest(RightRequest request){
        request.setTime(LocalDateTime.now());
        addRequest(request);
    }

    public void changeRightRequest(RightRequest request){
        rightRequestRepository.changeRequest(request.getId(), request.getHandled(), request.getResponse());
        addNotification(request, "Request", request.getSender(), request.getReceiver(), request.getReceiver().getName() + " changed the status of a request");
    }

    public void changeValuesOfRightRequest(RightRequest request){
        rightRequestRepository.changeValuesOfRequest(request.getId(), request.getOther(), request.getDetails());
        addNotification(request, "Request", request.getReceiver(), request.getSender(), request.getSender().getName() + " changed the status of a request");
    }

    public void deleteRequest(RightRequest request){
        rightRequestRepository.deleteById(request.getId());
    }

    // PRIVACYPOLICY REPOSITORY

    public Optional<PrivacyNotice> getPrivacyNoticeFromId(UUID id){
        return privacyNoticeRepository.findById(id);
    }

    public PrivacyNotice getPrivacyNoticeFromApp(IoTApp app){
        return privacyNoticeRepository.findByApp(app);
    }

    public List<PrivacyNotice> getAllPrivacyNoticeFromUser(User user){
        return privacyNoticeRepository.getAllPrivacyNoticeFromUser(user);
    }

    public List<PrivacyNotice> getUserPrivacyNoticeByAppName(User user, String name){
        return privacyNoticeRepository.getPrivacyNoticeFromUserFilterByAppName(user, name);
    }

    public boolean addPrivacyNoticeForApp(IoTApp app, String text){
        if(privacyNoticeRepository.findByApp(app)!=null){
            return false;
        }
        PrivacyNotice privacyNotice=new PrivacyNotice();
        privacyNotice.setApp(app);
        privacyNotice.setText(text);
        privacyNoticeRepository.save(privacyNotice);
        for(User u: getSubjectsFromApp(app)){
            addNotification(privacyNotice, "PrivacyNotice", u, authenticatedUser.getUser(), authenticatedUser.getUser().getName() + " created a new Privacy Notice for the app: " + app.getName());
        }
        return true;
    }

    public void deletePrivacyNotice(PrivacyNotice privacyNotice){
        privacyNoticeRepository.delete(privacyNotice);
    }

    public void changePrivacyNoticeForApp(IoTApp app, String text){
        PrivacyNotice privacyNotice=privacyNoticeRepository.findByApp(app);
        if(privacyNotice==null){
            addPrivacyNoticeForApp(app, text);
        }
        else{
            privacyNoticeRepository.changeText(privacyNotice.getId(), text);
        }
        for(User u: getSubjectsFromApp(app)){
            addNotification(privacyNotice, "PrivacyNotice", u, authenticatedUser.getUser(), authenticatedUser.getUser().getName() + " updated the Privacy Notice for the app: " + app.getName());
        }
    }

    // NOTIFICATION REPOSITORY

    public Optional<Notification> getNotificationFromId(UUID id){
        return notificationRepository.findById(id);
    }

    public void addNotification(Notification notification){
        notificationRepository.save(notification);
    }

    public void addNowNotification(Notification notification){
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsFromUser(User user){
        return notificationRepository.findAllByReceiver(user);
    }

    public List<Notification> getNewNotificationsFromUser(User user){
        return notificationRepository.findAllByReceiverAndIsRead(user, false);
    }

    public List<Notification> getOldNotificationsFromUser(User user){
        return notificationRepository.findAllByReceiverAndIsRead(user, true);
    }

    public void changeIsReadNotification(Notification notification, boolean isRead){
        notificationRepository.changeIsReadNotificationById(notification.getId(), isRead);
    }

    public void deleteNotification(Notification notification){
        notificationRepository.deleteById(notification.getId());
    }

    private boolean addNotification(Object object, String type, User receiver, User sender, String description){
        Notification notification= new Notification();
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setDescription(description);
        notification.setType(type);
        notification.setRead(false);
        switch(type) {
            case "Message":
                try {
                    Message message = (Message) object;
                    notification.setObjectId(message.getId());
                } catch (Exception e) {
                    return false;
                }
                break;
            case "Request":
                try {
                    RightRequest request = (RightRequest) object;
                    notification.setObjectId(request.getId());
                } catch (Exception e) {
                    return false;
                }
                break;
            case "PrivacyNotice":
                try {
                    PrivacyNotice privacyNotice = (PrivacyNotice) object;
                    notification.setObjectId(privacyNotice.getId());
                } catch (Exception e) {
                    return false;
                }
                break;
            default:
                return false;
        }
        addNowNotification(notification);
        return true;
    }

    // REMOVE EVERYTHING

    public void removeEverythingFromUser(User user){
        List<IoTApp> apps=getUserApps(user);
        for(IoTApp app : apps){
            RightRequest request=new RightRequest();
            request.setHandled(false);
            request.setRightType(RightType.DELTEEVERYTHING);
            request.setReceiver(getControllersFromApp(app).get(0));
            request.setSender(user);
            request.setApp(app);
            addNowRequest(request);
        }
    }

    public void removeEverythingFromUserAndApp(User user, IoTApp app){
        RightRequest request=new RightRequest();
        request.setHandled(false);
        request.setRightType(RightType.DELTEEVERYTHING);
        request.setReceiver(getControllersFromApp(app).get(0));
        request.setSender(user);
        request.setApp(app);
        addNowRequest(request);
    }
}
