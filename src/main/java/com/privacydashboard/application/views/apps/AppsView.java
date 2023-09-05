package com.privacydashboard.application.views.apps;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.annotation.security.PermitAll;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.StringReader;
import java.util.UUID;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    private final Div summaryEvaluation= new Div();
    private final TextField searchText=new TextField();
    private final Grid<IoTApp> grid= new Grid<>();

    private IoTApp priorityApp;
/*
    private String getJsonAppsFromUrl(){
        HttpURLConnection connection = null;
        System.err.println("Entered getJsonAppsFromUrl()\n");

        try{
            URL url = new URL("https://yggio.sifis-home.eu:3000/dht-insecure/topic_name/SIFIS:container_list");
            System.err.println("About to open the connection\n");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int resCode = connection.getResponseCode();

            if(resCode == HttpURLConnection.HTTP_OK){
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer res = new StringBuffer();
                String line;
                while((line = rd.readLine()) != null){
                    System.err.print(line);
                    res.append(line);
                }
                rd.close();
                return res.toString();
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally{
            if(connection != null)
                connection.disconnect();
        }
    }
  */  

  private List<IoTApp> getJsonAppsFromUrl(){

    TrustManager[] dummyTrustManager = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
    
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
    
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }

    };

    String responseString = "";
        
    try{
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, dummyTrustManager, new java.security.SecureRandom());        
        URL url = new URL("https://yggio.sifis-home.eu:3000/dht-insecure/topic_name/SIFIS:container_list");

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("GET response code: "+responseCode);

        if(responseCode == HttpsURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            responseString = response.toString().substring(response.toString().indexOf("[")+1, response.toString().lastIndexOf("]"));               

            System.out.println(response.toString()+"\n");
        }
        else{
            System.out.println("GET Request failed");
        }

        
        JsonNode json = new ObjectMapper().readTree(new StringReader(responseString));
        json = json.get("value");
        int size = json.get("containers").size();
        IoTApp[] appArray = new IoTApp[size];
        System.out.println(json.get("containers").elements().next().asText());

        for(int i = 0; i < size; i++){
            IoTApp app = new IoTApp();
            app.setName(json.get("containers").elements().next().asText());
            app.setId(new UUID(0, app.getName().hashCode()));
            System.out.println("App: "+app.getName());
            appArray[i] = app;
        }
        /*
            IoTApp app = new IoTApp();
            app.setName("TestApp");
            appArray[size] = app;
         */
        List<IoTApp> apps = List.of(appArray);
        for(IoTApp app1 : apps){
            System.out.println("App "+apps.indexOf(app1)+": "+app1.getName());
        }
        
        return apps;
    }
    catch(Exception e){
        System.out.println("JsonFromUrl Exception: "+e.getMessage());
    }       
    return null;      
}

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        priorityApp=communicationService.getApp();
    }

    public AppsView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService){
        this.dataBaseService=dataBaseService;
        this.authenticatedUser=authenticatedUser;
        this.communicationService=communicationService;
        addClassName("grid-view");
        initilizeInfrastuctureEvaluation();
        initializeSearchText();
        initializeGrid();
        
        VerticalLayout appLayout = new VerticalLayout();
        List<IoTApp> apps = getJsonAppsFromUrl();
        for (IoTApp app : apps) {
            appLayout.add(createApp(app));
        }

        add(summaryEvaluation, searchText, appLayout, grid);
    }

    private void initilizeInfrastuctureEvaluation(){
        Span evaluationDescription= new Span("Privacy Infrastructure Evaluation: ");
        evaluationDescription.addClassName("bold");
        Span evaluation= new Span();
        GlobalVariables.QuestionnaireVote vote= GlobalVariables.QuestionnaireVote.GREEN;
        int nRed=0;
        for(IoTApp app : dataBaseService.getUserApps(authenticatedUser.getUser())){
            if(app.getQuestionnaireVote().equals(GlobalVariables.QuestionnaireVote.RED)){
                nRed++;
                if(nRed>1){
                    vote= GlobalVariables.QuestionnaireVote.RED;
                    break;
                }
            }
            if(app.getQuestionnaireVote().equals(GlobalVariables.QuestionnaireVote.RED) && vote.equals(GlobalVariables.QuestionnaireVote.GREEN)){
                vote= GlobalVariables.QuestionnaireVote.ORANGE;
            }
        }
        switch (vote){
            case GREEN:
                evaluation.setText("GREEN");
                summaryEvaluation.addClassName("greenName");
                break;
            case ORANGE:
                evaluation.setText("ORANGE");
                summaryEvaluation.addClassName("orangeName");
                break;
            case RED:
                evaluation.setText("RED");
                summaryEvaluation.addClassName("redName");
                break;
        }
        summaryEvaluation.add(evaluationDescription, evaluation);
        summaryEvaluation.addClassName("summaryEvaluation");
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
        if(i.getConsenses() == null){
            return layout;
        }
        String[] appConsenses= i.getConsenses();
        String[] userConsenses=dataBaseService.getConsensesFromUserAndApp(authenticatedUser.getUser(), i);
        for(String consens :  appConsenses){
            Button button= new Button();
            if(userConsenses!=null && List.of(userConsenses).contains(consens)){
                button.setText("Withdraw consent");
                button.addClickListener(e -> changeConsent(i, consens, "withdraw"));
            }
            else{
                button.setText("Accept consent");
                button.addClickListener(e -> changeConsent(i, consens, "accept"));
            }
            HorizontalLayout l=new HorizontalLayout(new Span(consens), button);
            l.setAlignItems(FlexComponent.Alignment.CENTER);
            l.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            layout.add(l);
        }
        return layout;
    }

    private void changeConsent(IoTApp i, String consent, String action){

        /*HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://www.a-domain.example/foo/");

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("user", authenticatedUser.getUser().getId().toString()));
        params.add(new BasicNameValuePair("app", i.getId().toString()));
        params.add(new BasicNameValuePair("consent", consent));
        params.add(new BasicNameValuePair("action", action));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

//Execute and get the response.
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    // do something useful
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        */

        // DA ELIMINARE
        if(action.equals("withdraw")){
            dataBaseService.removeConsensesFromUserAppRelation(dataBaseService.getUserAppRelationByUserAndApp(authenticatedUser.getUser(), i), List.of(consent));
        }
        else{
            dataBaseService.addConsenses(dataBaseService.getUserAppRelationByUserAndApp(authenticatedUser.getUser(), i), List.of(consent));
        }
        // FINE DA ELIMINARE

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
