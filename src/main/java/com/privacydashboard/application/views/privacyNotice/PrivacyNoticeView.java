package com.privacydashboard.application.views.privacyNotice;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.privacydashboard.application.views.apps.AppsView;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.Collections;
import java.util.List;

@PageTitle("PrivacyNotice")
@Route(value="privacyNotice", layout = MainLayout.class)
@PermitAll
public class PrivacyNoticeView extends Div implements AfterNavigationObserver, BeforeEnterObserver {
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;

    private final TextField searchText=new TextField();
    private final Grid<PrivacyNotice> grid=new Grid<>();
    private final MyDialog newPrivacyNoticeDialog=new MyDialog();
    private final Button newPrivacyNoticeButton= new Button("Compile new Privacy Notice", e-> newPrivacyNoticeDialog.open());
    private final ComboBox<IoTApp> appComboBox= new ComboBox<>("Apps");

    private PrivacyNotice priorityNotice;

    // ONLY FOR SUBJECTS
    @Override
    public void beforeEnter(BeforeEnterEvent event){
        if(!(authenticatedUser.getUser().getRole().equals(Role.SUBJECT))){
            return;
        }
        // get from notification
        priorityNotice= communicationService.getPrivacyNoticeFromNotification();
        if(priorityNotice!=null){
            showPrivacyNotice(priorityNotice);
            return;
        }

        // get from link of another View
        priorityNotice= communicationService.getPrivacyNotice();
        if(priorityNotice!=null){
            showPrivacyNotice(priorityNotice);
        }
    }

    public PrivacyNoticeView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService){
        this.dataBaseService= dataBaseService;
        this.authenticatedUser= authenticatedUser;
        this.communicationService= communicationService;

        addClassName("grid-view");
        initializeSearchText();
        initializeGrid();
        if(authenticatedUser.getUser().getRole().equals(Role.SUBJECT)){
            add(searchText, grid);
        }
        else{
            initializeNewPrivacyNoticeDialog();
            HorizontalLayout headerLayout= new HorizontalLayout(searchText, newPrivacyNoticeButton);
            headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            add(headerLayout, grid);
        }
    }

    private void initializeSearchText(){
        searchText.setPlaceholder("Search...");
        searchText.setValueChangeMode(ValueChangeMode.LAZY);
        searchText.addValueChangeListener(e-> updateGrid());
        searchText.addClassName("search");
    }

    private void initializeGrid(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::createPrivacyNoticeCard);
    }

    private VerticalLayout createPrivacyNoticeCard(PrivacyNotice privacyNotice){
        Avatar avatar = new Avatar(privacyNotice.getApp().getName());
        avatar.addClassName("avatar");
        Span name= new Span(privacyNotice.getApp().getName());
        name.addClassNames("name link pointer");
        Details details = new Details(new Span("More"), detailsLayout(privacyNotice));
        HorizontalLayout avatarLayout= new HorizontalLayout(avatar, name);
        avatarLayout.addClassName("pointer");
        if(authenticatedUser.getUser().getRole().equals(Role.SUBJECT)){
            avatarLayout.addClickListener(e-> showPrivacyNotice(privacyNotice));
        }
        else{
            avatarLayout.addClickListener(e-> goToSinglePrivacyNoticeView(privacyNotice));
        }

        VerticalLayout card = new VerticalLayout(avatarLayout , details);
        card.addClassName("card");
        card.addClassName("canOpen");
        card.addClickListener(e-> {
            if(card.hasClassName("canOpen")){
                details.setOpened(true);
                card.removeClassNames("canOpen");
            }
            else if(!details.isOpened()){
                card.addClassName("canOpen");
            }
        });
        //card.add(new Span("ID: " + privacyNotice.getId().toString()));
        return card;
    }

    private VerticalLayout detailsLayout(PrivacyNotice privacyNotice){
        Span goToApp=new Span("Go to the app");
        goToApp.addClassName("link");
        goToApp.addClickListener(e-> communicationService.setApp(privacyNotice.getApp()));
        goToApp.addClickListener(e-> UI.getCurrent().navigate(AppsView.class));

        List<User> controllers= dataBaseService.getControllersFromApp(privacyNotice.getApp());
        VerticalLayout controllersLayout= new VerticalLayout();
        for(User u : controllers){
            Span contactLink=new Span(u.getName());
            contactLink.addClassName("link");
            contactLink.addClickListener(e->communicationService.setContact(u));
            contactLink.addClickListener(e-> UI.getCurrent().navigate(ContactsView.class));
            controllersLayout.add(contactLink);
        }
        Details controllersDetails= new Details(new Span("Data Controllers:"), controllersLayout);

        List<User> dpos= dataBaseService.getDPOsFromApp(privacyNotice.getApp());
        VerticalLayout dposLayout= new VerticalLayout();
        for(User u : dpos){
            Span contactLink=new Span(u.getName());
            contactLink.addClassName("link");
            contactLink.addClickListener(e->communicationService.setContact(u));
            contactLink.addClickListener(e-> UI.getCurrent().navigate(ContactsView.class));
            dposLayout.add(contactLink);
        }
        Details dposDetails= new Details(new Span("Data Protection Officers:"), dposLayout);

        if(!authenticatedUser.getUser().getRole().equals(Role.SUBJECT)){
            List<User> subjects= dataBaseService.getSubjectsFromApp(privacyNotice.getApp());
            VerticalLayout subjectsLayout= new VerticalLayout();
            for(User u : subjects){
                Span contactLink=new Span(u.getName());
                contactLink.addClassName("link");
                contactLink.addClickListener(e->communicationService.setContact(u));
                contactLink.addClickListener(e-> UI.getCurrent().navigate(ContactsView.class));
                subjectsLayout.add(contactLink);
            }
            Details subjectsDetails= new Details(new Span("Data Subjects:"), subjectsLayout);

            return new VerticalLayout(goToApp, controllersDetails, dposDetails, subjectsDetails);
        }
        else{
            return new VerticalLayout(goToApp, controllersDetails, dposDetails);
        }
    }

    // ONLY FOR SUBJECTS
    private void showPrivacyNotice(PrivacyNotice privacyNotice){
        MyDialog privacyNoticeDialog= new MyDialog();
        privacyNoticeDialog.setTitle("Privacy Notice " + privacyNotice.getApp().getName());
        privacyNoticeDialog.setContent(new VerticalLayout(convertText(privacyNotice.getText())));
        privacyNoticeDialog.setWidth("100%");
        privacyNoticeDialog.open();
    }

    // ONLY FOR CONTROLLERS/DPO
    private void initializeNewPrivacyNoticeDialog(){
        appComboBox.setItems(dataBaseService.getUserApps(authenticatedUser.getUser()));
        appComboBox.setItemLabelGenerator(IoTApp::getName);
        Button continueButton= new Button("Continue", e-> confirmNewPrivacyNotice());

        newPrivacyNoticeDialog.setTitle("Select app");
        newPrivacyNoticeDialog.setContent(new VerticalLayout(appComboBox));
        newPrivacyNoticeDialog.setContinueButton(continueButton);
    }

    // ONLY FOR CONTROLLERS/DPO
    private void confirmNewPrivacyNotice(){
        if(appComboBox.getValue()==null){
            return;
        }
        IoTApp app=appComboBox.getValue();
        if(dataBaseService.getPrivacyNoticeFromApp(app)!=null){
            MyDialog dialog=new MyDialog();
            Button confirmButton=new Button("Confirm",
                    e->{
                        createNewPrivacyNotice(app);
                        newPrivacyNoticeDialog.close();
                        dialog.close();
                    });

            dialog.setTitle("Confirm");
            dialog.setContent(new HorizontalLayout(new Span("There is already a Privacy Notice for this app, do you want to create a new one?" +
                    " The current one will be lost")));
            dialog.setContinueButton(confirmButton);
            dialog.open();
        }
        else{
            createNewPrivacyNotice(app);
            newPrivacyNoticeDialog.close();
        }
    }

    // ONLY FOR CONTROLLERS/DPO
    private void createNewPrivacyNotice(IoTApp app){
        PrivacyNotice privacyNotice= new PrivacyNotice();
        privacyNotice.setText(null);
        privacyNotice.setApp(app);
        goToSinglePrivacyNoticeView(privacyNotice);
    }

    // ONLY FOR CONTROLLERS/DPO
    private void goToSinglePrivacyNoticeView(PrivacyNotice privacyNotice){
        communicationService.setPrivacyNotice(privacyNotice);
        UI.getCurrent().navigate(SinglePrivacyNoticeView.class);
    }

    private void updateGrid(){
        List<PrivacyNotice> privacyNoticeList;
        if(searchText.getValue()==null || searchText.getValue().length()==0){
            privacyNoticeList=dataBaseService.getAllPrivacyNoticeFromUser(authenticatedUser.getUser());
        }
        else{
            privacyNoticeList=dataBaseService.getUserPrivacyNoticeByAppName(authenticatedUser.getUser(), searchText.getValue());
        }

        if(priorityNotice!=null){
            if(privacyNoticeList.contains(priorityNotice)){
                Collections.swap(privacyNoticeList, 0, privacyNoticeList.indexOf(priorityNotice));
            }
        }
        grid.setItems(privacyNoticeList);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event){
        updateGrid();
    }

    /*
   DA CAPIRE COME VISUALIZZARE IL TESTO.
   PER ORA LO SALVO COME HTML IN CASO NON DIA ERRORI ALTRIMENTI COME TEXTAREA
   MA IN CASO SI VOGLIA CARICARE DIRETTAMENTE IL FILE BISOGNA CAPIRE COME FARE
    */
    // ONLY FOR SUBJECTS
    private Component convertText(String text){
        try{
            return new Html(text);
        } catch (Exception e){
            //e.printStackTrace();
            TextArea textArea= new TextArea();
            textArea.setValue(text);
            textArea.setWidthFull();
            textArea.setReadOnly(true);
            return textArea;
        }
    }
}
