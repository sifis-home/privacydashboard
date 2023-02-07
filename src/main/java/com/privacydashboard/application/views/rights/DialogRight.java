package com.privacydashboard.application.views.rights;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.util.LinkedList;

public class DialogRight{
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;

    private final MyDialog requestDialog= new MyDialog();
    private final Button continueButton=new Button("Continue");
    private final MyDialog confirmDialog=new MyDialog();

    public DialogRight(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser){
        this.dataBaseService=dataBaseService;
        this.authenticatedUser=authenticatedUser;
    }

    public void showDialogRequest(RightType rightType){
        User user=authenticatedUser.getUser();
        if(user==null){
            return;
        }
        RightRequest request=initializeRequest(user, rightType);
        if(request.getRightType().equals(RightType.PORTABILITY)){
            portability(user, request);
        }
        else if(request.getRightType().equals(RightType.WITHDRAWCONSENT)){
            withdrawConsent(user, request);
        }
        else if(request.getRightType().equals(RightType.ERASURE)){
            erasure(user, request);
        }
        else if(request.getRightType().equals(RightType.INFO)){
            info(user, request);
        }
        else if(request.getRightType().equals(RightType.COMPLAIN)){
            complain(user, request);
        }
        else if(request.getRightType().equals(RightType.DELTEEVERYTHING)){
            deleteEverything(user, request);
        }
        requestDialog.open();
    }

    private RightRequest initializeRequest(User user, RightType rightType){
        RightRequest request= new RightRequest();
        request.setSender(user);
        request.setRightType(rightType);
        request.setHandled(false);
        return request;
    }

    private void portability(User user, RightRequest request){
        requestDialog.setTitle("Access data");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");

        requestDialog.setContent(new HorizontalLayout(appComboBox));

        continueButton.addClickListener(e->{
            if(appComboBox.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                requestDialog.close();
                showDialogConfirm(request);
            }
        });
        requestDialog.setContinueButton(continueButton);

    }

    private void withdrawConsent(User user, RightRequest request){
        requestDialog.setTitle("Withdraw consent");

        ComboBox<String> consensComboBox= new ComboBox<>("Consens");
        consensComboBox.setPlaceholder("Filter by name...");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");
        appComboBox.addValueChangeListener(e-> consensComboBox.setItems(dataBaseService.getConsensesFromUserAndApp(user,appComboBox.getValue())));
        requestDialog.setContent(new HorizontalLayout(appComboBox, consensComboBox));

        continueButton.addClickListener( e->{
            if(appComboBox.getValue()!=null && consensComboBox.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                request.setOther(consensComboBox.getValue());
                requestDialog.close();
                showDialogConfirm(request);
            }});
        requestDialog.setContinueButton(continueButton);
    }

    private void erasure(User user, RightRequest request){
        requestDialog.setTitle("Erase content");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");

        TextArea textArea=new TextArea("What to erase");
        textArea.setPlaceholder("Descrive what you want to erase");
        textArea.setWidthFull();
        requestDialog.setContent(new HorizontalLayout(appComboBox, textArea));

        continueButton.addClickListener(e->{
            if(appComboBox.getValue()!=null && textArea.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                request.setOther(textArea.getValue());
                requestDialog.close();
                showDialogConfirm(request);
            }
        });
        requestDialog.setContinueButton(continueButton);
    }

    private void info(User user, RightRequest request){
        requestDialog.setTitle("Get information");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");

        ComboBox<String> infoComboBox= new ComboBox<>("info");
        LinkedList<String> infos= new LinkedList<>();

        infos.add("The period for which the personal data will be stored");
        infos.add("The contact details of the data protection officer");
        infos.add("The purposes of the processing and the legal basis for it");
        infos.add("The legitimate interests pursued by the controller or by a third party");
        infos.add("The recipients or categories of recipients of the personal data");
        infos.add("The fact that the controller intends to transfer personal data to a third country");
        infos.add("Other");
        infoComboBox.setItems(infos);
        requestDialog.setContent(new VerticalLayout(appComboBox, infoComboBox));

        continueButton.addClickListener(e->{
            if(appComboBox.getValue()!=null && infoComboBox.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                request.setOther(infoComboBox.getValue());
                requestDialog.close();
                showDialogConfirm(request);
            }
        });
        requestDialog.setContinueButton(continueButton);
    }

    private void complain(User user, RightRequest request){
        requestDialog.setTitle("Compile a complain");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");

        TextArea textArea=new TextArea("Complain");
        textArea.setPlaceholder("Write your complain");
        textArea.setWidthFull();
        requestDialog.setContent(new VerticalLayout(appComboBox, textArea));

        continueButton.addClickListener(e->{
            if(appComboBox.getValue()!=null && textArea.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                request.setOther(textArea.getValue());
                requestDialog.close();
                showDialogConfirm(request);
            }
        });
        requestDialog.setContinueButton(continueButton);
    }

    private void deleteEverything(User user, RightRequest request){
        requestDialog.setTitle("Delete everything");

        ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");
        appComboBox.setItems(dataBaseService.getUserApps(user));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        appComboBox.setPlaceholder("Filter by name...");

        requestDialog.setContent(appComboBox);

        continueButton.addClickListener(e->{
            if(appComboBox.getValue()!=null){
                request.setApp(appComboBox.getValue());
                request.setReceiver(dataBaseService.getControllersFromApp(appComboBox.getValue()).get(0));
                requestDialog.close();
                showDialogConfirm(request);
            }
        });
        requestDialog.setContinueButton(continueButton);
    }

    public void showDialogConfirm(RightRequest request){
        if(request==null){
            return;
        }
        if(request.getRightType()==null || request.getReceiver()==null || request.getSender()==null || request.getApp()==null){
            return;
        }
        // Sender of the request and authenticated user must be the same person
        if(!request.getSender().equals(authenticatedUser.getUser())){
            return;
        }
        confirmDialog.setWidth("50%");

        Span appNameBold= new Span("APP: ");
        Span rightBold= new Span("RIGHT: ");
        appNameBold.addClassName("bold");
        rightBold.addClassName("bold");

        HorizontalLayout appName=new HorizontalLayout(appNameBold, new Span(request.getApp().getName()));
        HorizontalLayout right= new HorizontalLayout(rightBold, new Span(GlobalVariables.rightDict.get(request.getRightType()).toString()));

        TextArea premadeMessage=new TextArea();
        premadeMessage.setValue(getPremadeText(request));
        premadeMessage.setWidthFull();
        premadeMessage.setReadOnly(true);
        TextArea details=new TextArea();
        details.setPlaceholder("Add additional information");
        details.setWidthFull();

        confirmDialog.setTitle("Confirm");
        confirmDialog.setContent(new VerticalLayout(appName, right, premadeMessage, details));
        confirmDialog.setContinueButton(new Button("Confirm", e-> confirmRequest(request, details.getValue())));
        confirmDialog.open();
    }

    public String getPremadeText(RightRequest request){
        if(request.getRightType().equals(RightType.PORTABILITY)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to have access to my data from the app " +  request.getApp().getName() + " in a commonly used open format (XML, JSON),\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        else if(request.getRightType().equals(RightType.WITHDRAWCONSENT)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to withdraw the consent: " +request.getOther() + " from the app " + request.getApp().getName() + ",\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        else if(request.getRightType().equals(RightType.ERASURE)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to erase the following information: " +request.getOther() + " ,from the app " + request.getApp().getName() + ",\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        else if(request.getRightType().equals(RightType.INFO)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to know the following information: " +request.getOther() + " regarding the app " + request.getApp().getName() + ",\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        else if(request.getRightType().equals(RightType.COMPLAIN)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to know the complain with the supervision authority: " +request.getOther() + " about the app " + request.getApp().getName() + ",\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        else if(request.getRightType().equals(RightType.DELTEEVERYTHING)){
            return("Dear " + request.getReceiver().getName() + ", \n" +
                    "I would like to remove all my personal data from the app " +  request.getApp().getName() + " ,\n" +
                    "Best regards, \n" +
                    request.getSender().getName());
        }
        return null;
    }

    private void confirmRequest(RightRequest request, String details){
        request.setDetails(details);
        dataBaseService.addNowRequest(request);
        confirmDialog.close();
        Notification notification=Notification.show("The request has been sent to the Data Controllers!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}