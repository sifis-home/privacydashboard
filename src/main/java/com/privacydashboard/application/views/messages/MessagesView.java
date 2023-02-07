package com.privacydashboard.application.views.messages;

import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@PageTitle("Messages")
@Route(value="messages", layout = MainLayout.class)
@PermitAll
public class MessagesView extends Div implements AfterNavigationObserver{
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;

    private final Grid<User> grid=new Grid<>();
    private final TextField searchText=new TextField();
    private final Button newMessageButton=new Button("New Message");
    private final HorizontalLayout headerLayout=new HorizontalLayout(searchText, newMessageButton);

    private final MyDialog newMessageDialog=new MyDialog();
    private final ComboBox<User> contactComboBox= new ComboBox<>("Contacts");

    public MessagesView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService) {
        this.dataBaseService= dataBaseService;
        this.authenticatedUser= authenticatedUser;
        this.communicationService= communicationService;

        addClassName("grid-view");
        initializeHeaderLayout();
        initializeGrid();
        initializeNewMessageDialog();
        add(headerLayout, grid);
    }

    private void initializeGrid(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::showContact);
    }

    private void initializeHeaderLayout(){
        newMessageButton.addClickListener(e-> newMessageDialog.open());
        searchText.setPlaceholder("Search...");
        searchText.setValueChangeMode(ValueChangeMode.LAZY);
        searchText.addValueChangeListener(e-> updateGrid());
        searchText.addClassName("search");
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void initializeNewMessageDialog(){
        contactComboBox.setItems(dataBaseService.getAllContactsFromUser(authenticatedUser.getUser()));
        contactComboBox.setItemLabelGenerator(User::getName);

        Button newMessageButton=new Button("Continue", e-> confirmNewMessage());

        newMessageDialog.setTitle("Select a contact");
        newMessageDialog.setContent(new VerticalLayout(contactComboBox));
        newMessageDialog.setContinueButton(newMessageButton);
    }

    private void confirmNewMessage(){
        if(contactComboBox.getValue()==null){
            return;
        }
        newMessageDialog.close();
        goToConversation(contactComboBox.getValue());
    }

    private HorizontalLayout showContact(User contact){
        Avatar avatar = new Avatar(contact.getName(), contact.getProfilePictureUrl());
        Span name= new Span(contact.getName());
        name.addClassName("name");
        HorizontalLayout card = new HorizontalLayout(avatar, name);
        card.addClassName("card");
        card.addClassName("canOpen");
        card.addClickListener(e-> goToConversation(contact));
        return card;
    }

    private void goToConversation(User contact){
        communicationService.setContact(contact);
        UI.getCurrent().navigate(SingleConversationView.class);
    }

    private void updateGrid(){
        List<User> usersConversation;
        if(searchText.getValue()==null || searchText.getValue().length()==0){
            usersConversation=dataBaseService.getUserConversationFromUser(authenticatedUser.getUser());
        }
        else{
            usersConversation=dataBaseService.getUserConversationFromUserFilterByName(authenticatedUser.getUser(), searchText.getValue());
        }
        grid.setItems(usersConversation);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        updateGrid();
    }

}
