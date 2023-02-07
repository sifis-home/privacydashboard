package com.privacydashboard.application.views.apps;

import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.privacydashboard.application.views.privacyNotice.PrivacyNoticeView;
import com.privacydashboard.application.views.questionnaire.SingleQuestionnaire;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.Collections;
import java.util.List;

@PageTitle("Apps")
@Route(value = "apps-view", layout = MainLayout.class)
@PermitAll
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class AppsView extends Div implements AfterNavigationObserver, BeforeEnterObserver {
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;

    private final TextField searchText=new TextField();
    private final Grid<IoTApp> grid= new Grid<>();

    private IoTApp priorityApp;

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        priorityApp=communicationService.getApp();
    }

    public AppsView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService){
        this.dataBaseService=dataBaseService;
        this.authenticatedUser=authenticatedUser;
        this.communicationService=communicationService;
        addClassName("grid-view");
        initializeSearchText();
        initializeGrid();
        add(searchText, grid);
    }

    private void initializeSearchText(){
        searchText.setPlaceholder("Search...");
        searchText.setValueChangeMode(ValueChangeMode.LAZY);
        searchText.addValueChangeListener(e-> updateGrid());
        searchText.addClassName("search");
    }

    private void initializeGrid(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createApp);
    }

    private VerticalLayout createApp(IoTApp app){
        Avatar avatar = new Avatar(app.getName());
        Span name = new Span(app.getName());
        name.addClassName("name");
        Details details = new Details(new Span("More"), initializeApp(app));
        VerticalLayout card = new VerticalLayout();
        card.addClassName("card");
        card.addClassName("canOpen");
        card.setSpacing(false);
        card.add(new HorizontalLayout(avatar , name) , details);
        card.addClickListener(e-> {
            if(card.hasClassName("canOpen")){
                details.setOpened(true);
                card.removeClassNames("canOpen");
            }
            else if(!details.isOpened()){
                card.addClassName("canOpen");
            }
        });

        // se c'è un priorityUser apri details
        if(app.equals(priorityApp)){
            details.setOpened(true);
        }
        return card;
    }

    private VerticalLayout initializeApp(IoTApp i){
        Span descriptionSpan= new Span("Description:   ");
        descriptionSpan.addClassName("bold");
        Span description= new Span(descriptionSpan,  new Span(i.getDescription()));
        Div vote= getEvaluation(i);
        Span privacyNotice= new Span("Privacy Notice");
        privacyNotice.addClassName("link");
        privacyNotice.addClickListener(e-> goToPrivacyNotice(i));
        Details controllerDetails= new Details(new Span("Data Controllers: "), getUsers(i, Role.CONTROLLER));
        Details DPODetails= new Details(new Span("Data Protection Officer: "), getUsers(i, Role.DPO));

        VerticalLayout content=new VerticalLayout(description, vote, privacyNotice, controllerDetails, DPODetails);
        if(authenticatedUser.getUser().getRole().equals(Role.SUBJECT)){
            content.add(new Details(new Span("Consenses: ") , getConsenses(i)));
            Button removeEverythingButton= new Button("Remove everything", e->removeEverything(i));
            content.add(removeEverythingButton);
        }
        else{
            content.add(new Details(new Span("Data Subjects: ") , getUsers(i, Role.SUBJECT)));
        }
        //content.add(new Span("ID: " + i.getId().toString()));
        return content;
    }

    private Div getEvaluation(IoTApp app){
        Span icon= new Span();
        icon.addClassNames("las la-info-circle");
        icon.addClassName("pointer");
        ContextMenu contextMenu= new ContextMenu();
        contextMenu.setTarget(icon);
        contextMenu.setOpenOnClick(true);
        contextMenu.addClassName("info");
        Span descr= new Span("Evaluation: ");
        descr.addClassName("bold");
        Span vote=new Span();
        String text="";

        if(app.getQuestionnaireVote()==null){
            vote.setText("NO EVALUATION YET");
            vote.addClassName("redName");
            text= "The questionnaire to evaluate this app hasn't been performed yet";

        }
        else{
            switch (app.getQuestionnaireVote()){
                case RED:
                    vote.setText("RED ");
                    vote.addClassName("redName");
                    text= "The app is not compliant with the GDPR";
                    break;
                case ORANGE:
                    vote.setText("ORANGE ");
                    vote.addClassName("orangeName");
                    text= "The app is not so compliant with the GDPR";
                    break;
                case GREEN:
                    vote.setText("GREEN ");
                    vote.addClassName("greenName");
                    text= "The app is compliant with the GDPR";
                    break;
            }
        }
        if(!authenticatedUser.getUser().getRole().equals(Role.SUBJECT)) {
            contextMenu.addItem("Go to the questionnaire", e -> {
                communicationService.setApp(app);
                UI.getCurrent().navigate(SingleQuestionnaire.class);
            });
        }
        else {
            contextMenu.addItem(text);
        }
        return new Div(descr, vote, icon);
    }

    private void goToPrivacyNotice(IoTApp i){
        communicationService.setPrivacyNotice(dataBaseService.getPrivacyNoticeFromApp(i));
        UI.getCurrent().navigate(PrivacyNoticeView.class);
    }

    private VerticalLayout getUsers(IoTApp i, Role role){
        VerticalLayout layout=new VerticalLayout();
        List<User> users;
        switch(role) {
            case SUBJECT:
                users = dataBaseService.getSubjectsFromApp(i);
                break;
            case CONTROLLER:
                users = dataBaseService.getControllersFromApp(i);
                break;
            case DPO:
                users = dataBaseService.getDPOsFromApp(i);
                break;
            default:
                return null;
        }

        for(User u : users){
            if(u.equals(authenticatedUser.getUser())){
                continue;
            }
            Span contactLink=new Span(u.getName());
            contactLink.addClassName("link");
            contactLink.addClickListener(e->communicationService.setContact(u));
            contactLink.addClickListener(e-> UI.getCurrent().navigate(ContactsView.class));
            layout.add(contactLink);
        }
        return layout;
    }

    private VerticalLayout getConsenses(IoTApp i){
        VerticalLayout layout=new VerticalLayout();
        List<String> consenses=dataBaseService.getConsensesFromUserAndApp(authenticatedUser.getUser(), i);
        for(String consens :  consenses){
            HorizontalLayout l=new HorizontalLayout(new Span(consens), new Button("Withdraw consent", e -> withdrawConsent(i, consens)));
            l.setAlignItems(FlexComponent.Alignment.CENTER);
            l.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            layout.add(l);
        }
        return layout;
    }

    private void withdrawConsent(IoTApp i, String consent){
        RightRequest request=new RightRequest();
        request.setSender(authenticatedUser.getUser());
        request.setRightType(RightType.WITHDRAWCONSENT);
        request.setApp(i);
        request.setOther(consent);
        request.setReceiver(dataBaseService.getControllersFromApp(i).get(0));
        request.setHandled(false);
        communicationService.setRightRequest(request);
        UI.getCurrent().navigate("rights");
    }

    private void removeEverything(IoTApp app){
        MyDialog dialog= new MyDialog();
        Button confirm=new Button("Confirm", e-> {dataBaseService.removeEverythingFromUserAndApp(authenticatedUser.getUser(), app);
            Notification notification = Notification.show("The request has been sent to the Data Controllers!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();});

        dialog.setContinueButton(confirm);
        dialog.setTitle("Confirm to remove everything");
        dialog.setContent(
                new VerticalLayout(new Span("Are you sure you want to remove all your personal information from the app " +  app.getName() + "?")));
        dialog.open();
    }

    private void updateGrid(){
        List<IoTApp> ioTAppList;
        if(searchText.getValue()==null || searchText.getValue().length()==0){
            ioTAppList=dataBaseService.getUserApps(authenticatedUser.getUser());
        }
        else{
            ioTAppList=dataBaseService.getUserAppsByName(authenticatedUser.getUser(), searchText.getValue());
        }
        // se esiste l'app selezionata nei parametri, mettilo al primo posto
        if(priorityApp!=null){
            if(ioTAppList.contains(priorityApp)){
                Collections.swap(ioTAppList, 0, ioTAppList.indexOf(priorityApp));
            }
        }
        grid.setItems(ioTAppList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateGrid();
    }
}
