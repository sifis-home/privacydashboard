package com.privacydashboard.application.views.rights;

import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.privacydashboard.application.views.apps.AppsView;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.privacydashboard.application.views.usefulComponents.ToggleButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;
import java.util.List;

@PageTitle("Rights")
@Route(value="rights_controller", layout = MainLayout.class)
@RolesAllowed({"CONTROLLER", "DPO"})
public class ControllerDPORightsView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;

    private final Grid<RightRequest> grid= new Grid<>();
    private final ToggleButton toggleButton=new ToggleButton("HANDLED", false);
    private MyDialog requestDialog= new MyDialog();
    private final GridComponentRightsView gridComponentRightsView;
    private RightRequest priorityRight=null;

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        RightRequest request=communicationService.getRightFromNotification();
        if(request!=null){
            priorityRight=request;
        }
    }

    public ControllerDPORightsView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService) {
        this.dataBaseService = dataBaseService;
        this.authenticatedUser = authenticatedUser;
        this.communicationService= communicationService;

        addClassName("rights-view");
        gridComponentRightsView= new GridComponentRightsView(authenticatedUser.getUser().getRole());
        initializeGridComponentRightsView();
        initializeGrid();
    }

    private void initializeGridComponentRightsView(){
        gridComponentRightsView.addListener(GridComponentRightsView.ContactEvent.class, this::goToUser);
        gridComponentRightsView.addListener(GridComponentRightsView.AppEvent.class, this::goToApp);
    }

    private void initializeGrid(){
        HorizontalLayout headerGrid=gridComponentRightsView.getHeaderLayout(toggleButton);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::showRequestCard);
        grid.getColumns().get(0).setHeader(headerGrid);
        toggleButton.addClickListener(e-> updateGrid(toggleButton.getValue()));
        add(grid);
    }

    private HorizontalLayout showRequestCard(RightRequest request){
        HorizontalLayout card=gridComponentRightsView.getCard(request);
        card.addClickListener(e -> showRequest(request));
        return card;
    }

    private void showRequest(RightRequest request){
        if(request==null){
            return;
        }
        VerticalLayout contentLayout= gridComponentRightsView.getContent(request);
        Checkbox checkbox= (Checkbox) contentLayout.getComponentAt(8);
        TextArea textArea= (TextArea) contentLayout.getComponentAt(7);

        Button save=new Button("Save" , e->changeRequest(request, checkbox.getValue(), textArea.getValue()));
        requestDialog= new MyDialog();
        requestDialog.setTitle("Right Request");
        requestDialog.setContinueButton(save);
        requestDialog.setContent(contentLayout);
        requestDialog.open();
    }

    private void changeRequest(RightRequest request, Boolean handled, String response){
        if((response.equals(request.getResponse()) || response.equals(""))  && handled==request.getHandled()){
            requestDialog.close();
            return;
        }
        request.setResponse(response);
        request.setHandled(handled);
        dataBaseService.changeRightRequest(request);
        updateGrid(toggleButton.getValue());
        requestDialog.close();
    }

    private void updateGrid(boolean handled){
        List<RightRequest> rightRequests;
        if(handled){
            rightRequests=dataBaseService.getHandledRequestsFromReceiver(authenticatedUser.getUser());
        }
        else{
            rightRequests=dataBaseService.getPendingRequestsFromReceiver(authenticatedUser.getUser());
        }

        if(priorityRight!=null && rightRequests.contains(priorityRight)){
            Collections.swap(rightRequests, 0 , rightRequests.indexOf(priorityRight));
            showRequest(priorityRight);
        }
        grid.setItems(rightRequests);
        priorityRight=null;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event){
        updateGrid(false);
    }

    private void goToUser(GridComponentRightsView.ContactEvent event){
        requestDialog.close();
        communicationService.setContact(event.getContact());
        UI.getCurrent().navigate(ContactsView.class);
    }

    private void goToApp(GridComponentRightsView.AppEvent event){
        requestDialog.close();
        communicationService.setApp(event.getApp());
        UI.getCurrent().navigate(AppsView.class);
    }
}
