package com.privacydashboard.application.views.messages;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.time.ZoneOffset;
import java.util.*;

@Route(value="conversation", layout = MainLayout.class)
@PermitAll
public class SingleConversationView extends Span implements BeforeEnterObserver, AfterNavigationObserver,HasDynamicTitle{
    private final DataBaseService dataBaseService;
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;
    private User contact;

    private String title="";
    private final Grid<Message> grid=new Grid<>();
    private final TextArea messageText=new TextArea();
    private final Button sendMessageButton= new Button("Send Message");

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        contact=communicationService.getContact();
        if(contact==null || !dataBaseService.getAllContactsFromUser(authenticatedUser.getUser()).contains(contact)){
            UI.getCurrent().navigate(MessagesView.class);
            event.rerouteTo(MessagesView.class);
        }
        else{
            title= contact.getName();
            GlobalVariables.pageTitle= contact.getName();
        }
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    public SingleConversationView(DataBaseService dataBaseService, AuthenticatedUser authenticatedUser, CommunicationService communicationService) {
        this.dataBaseService = dataBaseService;
        this.authenticatedUser = authenticatedUser;
        this.communicationService=communicationService;

        addClassName("messages-view");
        initializeGrid();
        add(grid, initializeTextAndButton());
    }

    private HorizontalLayout initializeTextAndButton(){
        messageText.setPlaceholder("Text...");
        messageText.addClassName("messageText");
        sendMessageButton.addClickListener(e-> sendMessage());
        sendMessageButton.addClassName("buuutton");
        HorizontalLayout layout=new HorizontalLayout(messageText , sendMessageButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.addClassName("textAndButton");
        return layout;
    }

    private void initializeGrid(){
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(this::showMessage);
    }

    //DA SISTEMARE LA TIME ZONE
    private HorizontalLayout showMessage(Message message){
        HorizontalLayout card= new HorizontalLayout();
        card.addClassName("card");
        MessageListItem messageItem=new MessageListItem(message.getMessage(),message.getTime().toInstant(ZoneOffset.UTC), message.getSender().getName());
        MessageList messageList= new MessageList(messageItem);
        card.add(messageList);
        if(message.getSender().equals(authenticatedUser.getUser())){
            card.addClassName("isUser");
        }
        else{
            card.addClassName("isContact");
        }
        return card;
    }

    private void sendMessage(){
        if(messageText.getValue()==null || messageText.getValue().length()==0){
            return;
        }
        Message message=new Message();
        message.setMessage(messageText.getValue());
        message.setReceiver(contact);
        message.setSender(authenticatedUser.getUser());
        dataBaseService.addNowMessage(message);
        messageText.setValue("");
        updateConversation();
    }

    private void updateConversation(){
        List<Message> messageList= dataBaseService.getConversationFromUsers(authenticatedUser.getUser(), contact);
        grid.setItems(messageList);
        grid.scrollToIndex(messageList.size());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event){
        updateConversation();
    }
}
