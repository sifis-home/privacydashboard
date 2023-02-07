package com.privacydashboard.application.views.rights;

//import com.privacydashboard.application.data.RightType;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.privacydashboard.application.views.apps.AppsView;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;

@PageTitle("Rights")
@Route(value="rights", layout = MainLayout.class)
@RolesAllowed("SUBJECT")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class SubjectRightsView extends VerticalLayout implements BeforeEnterObserver{
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;
    private final Grid<RightRequest> grid= new Grid<>();
    private MyDialog rightList;
    private MyDialog requestDialog;
    private RightRequest priorityRight=null;
    private final GridComponentRightsView gridComponentRightsView;

    // Uso ComponentUtil per passare le informazioni invece dei parametri dell'url. Dopo bisogna resettarlo
    @Override
    public void beforeEnter(BeforeEnterEvent event){
        // apply right
        RightRequest request= communicationService.getRightRequest();
        if(request!=null){
            DialogRight dialogRight=new DialogRight(dataBaseService, authenticatedUser);
            dialogRight.showDialogConfirm(request);
            return;
        }
        // show notification
        request= communicationService.getRightFromNotification();
        if(request!= null){
            priorityRight=request;
            showRequestList(priorityRight.getHandled());
            return;
        }
        // show Pending Requests (action available from Home)
        Boolean open= communicationService.getOpenPendingRequests();
        if(open!=null && open){
            showRequestList(false);
        }
    }

    public SubjectRightsView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService) {
        this.dataBaseService = dataBaseService;
        this.authenticatedUser = authenticatedUser;
        this.communicationService=communicationService;

        addClassName("rights-view");

        gridComponentRightsView= new GridComponentRightsView(authenticatedUser.getUser().getRole());
        initializeGridComponentRightsView();
        initializeGrid();
        createButtons();
        generateAllRightsDetails();
    }

    private void initializeGridComponentRightsView(){
        gridComponentRightsView.addListener(GridComponentRightsView.ContactEvent.class, this::goToUser);
        gridComponentRightsView.addListener(GridComponentRightsView.AppEvent.class, this::goToApp);
    }

    private void initializeGrid(){
        HorizontalLayout headerGrid=gridComponentRightsView.getHeaderLayout();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::showRequestCard);
        grid.getColumns().get(0).setHeader(headerGrid);
    }

    private void createButtons(){
        Button pendingRequests=new Button("Pending requests", event -> showRequestList(false));
        Button handledRequests=new Button("Handled requests", event -> showRequestList(true));
        pendingRequests.addClassName("buuutton");
        handledRequests.addClassName("buuutton");
        add(new HorizontalLayout(pendingRequests, handledRequests));
    }

    private void showRequestList(Boolean handled){
        rightList=new MyDialog();
        List<RightRequest> rightRequests;
        if(handled) {
            rightRequests = dataBaseService.getHandledRequestsFromSender(authenticatedUser.getUser());
            rightList.setTitle("Handled requests");
        }
        else{
            rightRequests = dataBaseService.getPendingRequestsFromSender(authenticatedUser.getUser());
            rightList.setTitle("Pending requests");
        }
        if(priorityRight!=null && rightRequests.contains(priorityRight)){
            Collections.swap(rightRequests, 0 , rightRequests.indexOf(priorityRight));
            showRequestCard(priorityRight);
        }
        grid.setItems(rightRequests);
        HorizontalLayout contentLayout= new HorizontalLayout(grid);
        contentLayout.addClassName("rights-view");
        rightList.setContent(contentLayout);
        rightList.setWidthFull();
        rightList.setHeight("70%");
        rightList.open();
        priorityRight=null;
    }

    private HorizontalLayout showRequestCard(RightRequest request){
        HorizontalLayout card=gridComponentRightsView.getCard(request);
        card.addClickListener(e -> showRequest(request));
        return card;
    }

    private void showRequest(RightRequest request){
        requestDialog=new MyDialog();
        if(request==null){
            return;
        }
        VerticalLayout contentLayout=gridComponentRightsView.getContent(request);
        requestDialog.setTitle("Right request");
        requestDialog.setContent(contentLayout);
        requestDialog.open();
    }

    private void generateAllRightsDetails(){
        add(generateRightCard("Access data", RightType.PORTABILITY));
        add(generateRightCard("Withdraw a consent", RightType.WITHDRAWCONSENT));
        add(generateRightCard("Ask information", RightType.INFO));
        add(generateRightCard("Compile a complain", RightType.COMPLAIN));
        add(generateRightCard("Erase data", RightType.ERASURE));
        add(generateRightCard("Remove everything", RightType.DELTEEVERYTHING));
    }

    private void startRequest(RightType rightType){
        DialogRight dialogRight=new DialogRight(dataBaseService, authenticatedUser);
        dialogRight.showDialogRequest(rightType);
    }

    private HorizontalLayout generateRightCard(String title, RightType rightType){
        Span name= new Span(title);
        name.addClassName("name");
        HorizontalLayout card = new HorizontalLayout(name);
        card.addClassNames("card canOpen");
        card.addClickListener(e-> startRequest(rightType));
        return card;
    }

    private void goToUser(GridComponentRightsView.ContactEvent event){
        rightList.close();
        requestDialog.close();
        communicationService.setContact(event.getContact());
        UI.getCurrent().navigate(ContactsView.class);
    }

    private void goToApp(GridComponentRightsView.AppEvent event){
        rightList.close();
        requestDialog.close();
        communicationService.setApp(event.getApp());
        UI.getCurrent().navigate(AppsView.class);
    }
}
