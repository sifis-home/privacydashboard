package com.privacydashboard.application.views.rights;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.views.usefulComponents.ToggleButton;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.shared.Registration;

import java.time.format.DateTimeFormatter;

public class GridComponentRightsView extends Dialog {
    private final Role role;

    public GridComponentRightsView(Role role){
        this.role=role;
    }

    public HorizontalLayout getHeaderLayout(){
        return getHeaderLayout(null);
    }

    public HorizontalLayout getHeaderLayout(ToggleButton toggleButton){
        Span[] spans= new Span[5];
        if(role.equals(Role.SUBJECT)){
            spans[0]= new Span("RECEIVER");
            spans[4]= new Span("HANDLED");
        }
        else{
            spans[0]= new Span("SENDER");
            spans[4] = new Span(toggleButton);
        }
        spans[1]= new Span("RIGHT TYPE");
        spans[2]= new Span("APP");
        spans[3]= new Span("TIME");
        for(Span span : spans){
            span.addClassName("name");
        }
        HorizontalLayout headerLayout= new HorizontalLayout(spans);
        headerLayout.addClassName("headerLayout");
        return headerLayout;
    }

    public HorizontalLayout getCard(RightRequest request){
        Span[] spans= new Span[5];
        if(role.equals(Role.SUBJECT)){
            spans[0] = new Span(request.getReceiver().getName());
        }
        else {
            spans[0] = new Span(request.getSender().getName());
        }
        spans[1]= new Span(GlobalVariables.rightDict.get(request.getRightType()).toString());
        spans[2]= new Span(request.getApp().getName());
        spans[3]= new Span(DateTimeFormatter.ofPattern("dd/MM/yyy").format(request.getTime()));
        spans[4]= new Span(request.getHandled().toString());
        for(Span span : spans){
            span.addClassName("name");
        }

        HorizontalLayout card= new HorizontalLayout(spans);
        card.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        card.addClassName("card");
        card.addClassName("canOpen");
        return card;
    }

    public VerticalLayout getContent(RightRequest request){
        Span[] boldSpans= new Span[7];
        Span[] normalSpans= new Span[7];
        HorizontalLayout[] horizontalLayouts= new HorizontalLayout[7];

        User contact;
        if(role.equals(Role.SUBJECT)){
            boldSpans[0]= new Span("Receiver User:   ");
            contact=request.getReceiver();
        }
        else{
            boldSpans[0]= new Span("Sender User:   ");
            contact=request.getSender();
        }
        normalSpans[0]= new Span(contact.getName());
        normalSpans[0].addClassName("link");
        normalSpans[0].addClickListener(e-> fireEvent(new ContactEvent(this, contact)));

        boldSpans[1]= new Span("Right:   ");
        normalSpans[1]= new Span(GlobalVariables.rightDict.get(request.getRightType()).toString());

        boldSpans[2]= new Span("App:   ");
        normalSpans[2]= new Span(request.getApp().getName());
        normalSpans[2].addClickListener(e-> fireEvent(new AppEvent(this, request.getApp())));
        normalSpans[2].addClassName("link");

        boldSpans[3]= new Span("Time:   ");
        normalSpans[3]= new Span(DateTimeFormatter.ofPattern("dd/MM/yyy").format(request.getTime()));

        boldSpans[4]= new Span("Content: ");
        normalSpans[4]= new Span(new DialogRight(null, null).getPremadeText(request));

        boldSpans[5]= new Span("Details:   ");
        normalSpans[5]= new Span(request.getDetails());

        if(request.getRightType().equals(RightType.WITHDRAWCONSENT)){
            boldSpans[6]= new Span("Consent to withdraw:   ");
        }
        else if(request.getRightType().equals(RightType.COMPLAIN)){
            boldSpans[6]= new Span("Complain:   ");
        }
        else if(request.getRightType().equals(RightType.INFO)){
            boldSpans[6]= new Span("Info:   ");
        }
        else if(request.getRightType().equals(RightType.ERASURE)){
            boldSpans[6]= new Span("What to erase:   ");
        }
        else if(request.getRightType().equals(RightType.DELTEEVERYTHING)){
            boldSpans[6]= new Span("");
        }
        else if(request.getRightType().equals(RightType.PORTABILITY)){
            boldSpans[6]= new Span("");
        }
        normalSpans[6]= new Span(request.getOther()==null ? "" : request.getOther());

        for(int i=0; i<7; i++){
            boldSpans[i].addClassName("bold");
            horizontalLayouts[i]= new HorizontalLayout(boldSpans[i], normalSpans[i]);
        }

        TextArea textArea;
        Checkbox checkbox= new Checkbox();
        if(role.equals(Role.SUBJECT)){
            textArea= new TextArea("Controller response");
            textArea.setReadOnly(true);
            checkbox.setReadOnly(true);
        }
        else{
            textArea=new TextArea("Your response");
            textArea.setPlaceholder("Write your response...");
        }
        textArea.setValue(request.getResponse()==null ? "" : request.getResponse());
        textArea.setWidthFull();
        checkbox.setValue(request.getHandled());
        checkbox.setLabel("Handled");

        VerticalLayout layout= new VerticalLayout(horizontalLayouts);
        layout.add(textArea, checkbox);
        return layout;
    }

    public static class ContactEvent extends ComponentEvent<GridComponentRightsView> {
        private final User contact;
        ContactEvent(GridComponentRightsView source, User contact){
            super(source, false);
            this.contact= contact;
        }

        public User getContact(){
            return contact;
        }
    }

    public static class AppEvent extends ComponentEvent<GridComponentRightsView> {
        private final IoTApp app;
        AppEvent(GridComponentRightsView source, IoTApp app){
            super(source, false);
            this.app= app;
        }

        public IoTApp getApp(){
            return app;
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
