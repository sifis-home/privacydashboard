package com.privacydashboard.application.data.service;

import com.privacydashboard.application.data.entity.*;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import org.springframework.stereotype.Service;

import java.util.Optional;

// classe usata per passare vari oggetti tra le Views senza dover usare parametri nell'URL
// quando si usa il metodo get<Something> il valore si resetta per evitare azioni non volute in caso si visiti una pagina
@Service
public class CommunicationService {
    private final DataBaseService dataBaseService;
    public CommunicationService(DataBaseService dataBaseService){
        this.dataBaseService= dataBaseService;
    }

    // NON PIU IN USO MA POTREBBE TORNARE UTILE
    public Notification getRightNotification() {
        Notification notification = null;
        Object object = ComponentUtil.getData(UI.getCurrent(), "RightNotification");
        try {
            if (object != null) {
                notification = (Notification) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "RightNotification", null);
        }
        return notification;
    }

    public RightRequest getRightFromNotification() {
        RightRequest request= null;
        Object object = ComponentUtil.getData(UI.getCurrent(), "RightNotification");
        try {
            if (object != null) {
                Notification notification = (Notification) object;
                request= dataBaseService.getRequestFromId(notification.getObjectId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "RightNotification", null);
        }
        return request;
    }

    public void setRightNotification(Notification notification){
        ComponentUtil.setData(UI.getCurrent(), "RightNotification", notification);
    }

    public RightRequest getRightRequest(){
        RightRequest request=null;
        Object object=ComponentUtil.getData(UI.getCurrent(), "RightRequest");
        try {
            if (object != null) {
                request = (RightRequest) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "RightRequest", null);
        }
        return request;
    }

    public void setRightRequest(RightRequest request){
        ComponentUtil.setData(UI.getCurrent(), "RightRequest", request);
    }

    public IoTApp getApp(){
        IoTApp app=null;
        Object object=ComponentUtil.getData(UI.getCurrent(), "App");
        try {
            if (object != null) {
                app = (IoTApp) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "App", null);
        }
        return app;
    }

    public void setApp(IoTApp app){
        ComponentUtil.setData(UI.getCurrent(), "App", app);
    }

    public User getContact(){
        User contact=null;
        Object object=ComponentUtil.getData(UI.getCurrent(), "Contact");
        try {
            if (object != null) {
                contact = (User) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "Contact", null);
        }
        return contact;
    }

    public void setContact(User contact){
        ComponentUtil.setData(UI.getCurrent(), "Contact", contact);
    }

    public PrivacyNotice getPrivacyNotice(){
        PrivacyNotice privacyNotice=null;
        Object object=ComponentUtil.getData(UI.getCurrent(), "PrivacyNotice");
        try{
            if(object!=null){
                privacyNotice= (PrivacyNotice) object;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "PrivacyNotice", null);
        }
        return privacyNotice;
    }

    public void setPrivacyNotice(PrivacyNotice privacyNotice){
        ComponentUtil.setData(UI.getCurrent(), "PrivacyNotice", privacyNotice);
    }

    public PrivacyNotice getPrivacyNoticeFromNotification(){
        PrivacyNotice privacyNotice=null;
        Object object= ComponentUtil.getData(UI.getCurrent(), "PrivacyNoticeNotification");
        try{
            if(object!=null){
                Notification notification= (Notification) object;
                Optional<PrivacyNotice> maybePrivacyNotice= dataBaseService.getPrivacyNoticeFromId(notification.getObjectId());
                if(maybePrivacyNotice.isPresent()){
                    privacyNotice=maybePrivacyNotice.get();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            ComponentUtil.setData(UI.getCurrent(), "PrivacyNoticeNotification", null);
        }
        return privacyNotice;
    }

    public void setPrivacyNoticeNotification(Notification notification){
        ComponentUtil.setData(UI.getCurrent(), "PrivacyNoticeNotification", notification);
    }

    public void setOpenPendingRequests(Boolean open){
        ComponentUtil.setData(UI.getCurrent(), "openPendingRequest", open);
    }

    public Boolean getOpenPendingRequests(){
        Boolean open= (Boolean) ComponentUtil.getData(UI.getCurrent(), "openPendingRequest");
        ComponentUtil.setData(UI.getCurrent(), "openPendingRequest", null);
        return open;
    }
}
