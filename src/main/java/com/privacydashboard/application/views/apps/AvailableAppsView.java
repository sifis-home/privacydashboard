package com.privacydashboard.application.views.apps;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.notification.Notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
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

@PageTitle("AvailableApps")
@Route(value = "available-apps-view", layout = MainLayout.class)
@PermitAll
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class AvailableAppsView extends Div {
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;


    private String cleanAppName(String name){
        String newName = name.split("3pa-")[1].split("-")[0];
        return newName;
    }



    private List<IoTApp> getJsonAppsFromUrl() throws NullPointerException {


        String responseString = "";
            
        try{       
            URL url = new URL("http://yggio.sifis-home.eu:3030");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();                

                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }
                in.close();

                responseString = response.toString();   
            }
            else{
                System.out.println("GET Request failed code: "+responseCode);
            }

            JsonNode json = new ObjectMapper().readTree(new StringReader(responseString));
            int size = json.get("packages").size();

            if(size == 0){
                throw new NullPointerException("Size was 0");
            }

            IoTApp[] appArray = new IoTApp[size];

            Iterator<JsonNode> iterator = json.get("packages").elements();

            for(int i = 0; i < size; i++){
                IoTApp app = new IoTApp();
                app.setName(cleanAppName(iterator.next().asText()));
                app.setId(new UUID(0, app.getName().hashCode()));
                appArray[i] = app;
            }

            List<IoTApp> apps = removeDuplicates(Arrays.asList(appArray));
            return apps;
        }
        catch(Exception e){
            System.out.println("JsonFromUrl Exception: "+e.getMessage());
            throw new NullPointerException(e.getMessage());
        }          
    }

    private ArrayList<IoTApp> removeDuplicates(List<IoTApp> list){
        ArrayList<IoTApp> newList = new ArrayList<>();

        for(IoTApp app : list){
            if(!newList.contains(app))
                newList.add(app);
        }
        
        return newList;
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

    private VerticalLayout createApp(IoTApp app){
        Avatar avatar = new Avatar(app.getName());
        Span name = new Span(app.getName());
        name.addClassName("name");
        Details details = new Details(new Span("More"), initializeApp(app));
        VerticalLayout card = new VerticalLayout();
        card.addClassName("card");
        card.addClassName("canOpen");
        card.setSpacing(false);
        card.add(new HorizontalLayout(avatar , name), details);
        card.addClickListener(e-> {
            if(card.hasClassName("canOpen")){
                details.setOpened(true);
                card.removeClassNames("canOpen");
            }
            else if(!details.isOpened()){
                card.addClassName("canOpen");
            }
        });

        return card;
    }

    public AvailableAppsView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService){
        this.dataBaseService=dataBaseService;
        this.authenticatedUser=authenticatedUser;
        this.communicationService=communicationService;
        addClassName("grid-view");
        VerticalLayout appLayout = new VerticalLayout();

        try{
           
            List<IoTApp> apps = getJsonAppsFromUrl();
            for (IoTApp app : apps) {
                appLayout.add(createApp(app));
            }
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
            Span exNotice = e.getMessage().equals("Size was 0") ? new Span("You do not have any apps.") : new Span("We could not retrieve your apps.");
            exNotice.addClassName("bold");
            appLayout.add(exNotice);
        }

        add(appLayout);
    }
    
}
