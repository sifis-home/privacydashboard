package com.privacydashboard.application.views.mainLayout;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.Notification;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.views.messages.SingleConversationView;
import com.privacydashboard.application.views.privacyNotice.PrivacyNoticeView;
import com.privacydashboard.application.views.rights.ControllerDPORightsView;
import com.privacydashboard.application.views.rights.SubjectRightsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Span;

import java.util.List;

@NpmPackage(value = "line-awesome", version = "1.3.0")
public class NotificationView extends Span{
    private final User user;
    private final DataBaseService dataBaseService;
    private final CommunicationService communicationService;

    private final ContextMenu menuNotifications=new ContextMenu();
    private final Span badge=new Span();

    public NotificationView(User user, DataBaseService dataBaseService, CommunicationService communicationService){
        this.dataBaseService=dataBaseService;
        this.user=user;
        this.communicationService=communicationService;
        initializeIcons();
        initializeMenuNotifications();
    }

    private void initializeIcons(){
        this.addClassNames("la la-bell la-2x bell-icon");
        badge.addClassNames("badge");
        add(badge);
    }

    private void initializeMenuNotifications(){
        menuNotifications.setOpenOnClick(true);
        menuNotifications.setTarget(this);
        updateNotifications();
    }

    private void updateNotifications(){
        menuNotifications.removeAll();
        List<Notification> notifications=dataBaseService.getNewNotificationsFromUser(user);
        for(Notification notification  : notifications){
            menuNotifications.addItem(notification.getDescription(), e-> goToNotification(notification));
        }

        if(notifications.size()==0){
            badge.setVisible(false);
        }
        else{
            badge.setText(String.valueOf(notifications.size()));
        }
    }

    private void goToNotification(Notification notification){
        if(notification==null || notification.getType()==null){
            return;
        }
        switch(notification.getType()){
            case "Message":
                dataBaseService.changeIsReadNotification(notification, true);
                communicationService.setContact(notification.getSender());
                UI.getCurrent().navigate(SingleConversationView.class);
                break;
            case "Request":
                dataBaseService.changeIsReadNotification(notification, true);
                communicationService.setRightNotification(notification);
                if(user.getRole().equals(Role.SUBJECT)){
                    UI.getCurrent().navigate(SubjectRightsView.class);
                }
                else{
                    UI.getCurrent().navigate(ControllerDPORightsView.class);
                }
                break;
            case "PrivacyNotice":
                dataBaseService.changeIsReadNotification(notification, true);
                communicationService.setPrivacyNoticeNotification(notification);
                UI.getCurrent().navigate(PrivacyNoticeView.class);
                break;
        }
        updateNotifications();
    }
}