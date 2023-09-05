package com.privacydashboard.application.views.rights;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.views.usefulComponents.ToggleButton;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.util.List;

import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Vertical;

public class GridComponentRightsViewTest {

    private static RightRequest createRequestComplain(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.COMPLAIN);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }

    private static RightRequest createRequestWithdrawCconsent(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.WITHDRAWCONSENT);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }

    private static RightRequest createRequestInfo(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.INFO);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }

    private static RightRequest createRequestErasure(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.ERASURE);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }

    private static RightRequest createRequestDeleteEverything(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.DELTEEVERYTHING);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }

    private static RightRequest createRequestPortability(LocalDateTime time){
        User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

        User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

        IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

        RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.PORTABILITY);
        req1.setHandled(true);
        req1.setTime(time);
        req1.setOther("TestComplaint");
        req1.setDetails("TestDetails");
        req1.setResponse("TestResponse");

        return req1;
    }
    
    @Test
    public void getHeaderLayoutSubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.SUBJECT);
        HorizontalLayout layout = grid.getHeaderLayout();
        assertEquals(layout.getElement().getAttribute("class"), "headerLayout");
        assertEquals(layout.getElement().getChild(0).getText(), "RECEIVER");
        assertEquals(layout.getElement().getChild(0).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(1).getText(), "RIGHT TYPE");
        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(2).getText(), "APP");
        assertEquals(layout.getElement().getChild(2).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(3).getText(), "TIME");
        assertEquals(layout.getElement().getChild(3).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(4).getText(), "HANDLED");
        assertEquals(layout.getElement().getChild(4).getAttribute("class"), "name");
    }

    @Test
    public void getHeaderLayoutNotASubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);
        ToggleButton button = new ToggleButton("ButtonTest");
        HorizontalLayout layout = grid.getHeaderLayout(button);
        assertEquals(layout.getElement().getAttribute("class"), "headerLayout");
        assertEquals(layout.getElement().getChild(0).getText(), "SENDER");
        assertEquals(layout.getElement().getChild(0).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(1).getText(), "RIGHT TYPE");
        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(2).getText(), "APP");
        assertEquals(layout.getElement().getChild(2).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(3).getText(), "TIME");
        assertEquals(layout.getElement().getChild(3).getAttribute("class"), "name");
        assertEquals(layout.getElement().getChild(4).getChild(0).getProperty("label"), "ButtonTest");
        assertEquals(layout.getElement().getChild(4).getAttribute("class"), "name");
    }

    @Test
    public void getCardNullTest(){
        assertThrows(NullPointerException.class, () -> {
            GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);
            grid.getCard(null);
        });
    }

    @Test
    public void getCardSubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.SUBJECT);

        LocalDateTime time = LocalDateTime.now();

        HorizontalLayout card = grid.getCard(createRequestComplain(time));
        assertEquals(card.getElement().getAttribute("class"), "card canOpen");
        assertEquals(card.getElement().getChild(0).getText(), "Receiver1");
        assertEquals(card.getElement().getChild(0).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(1).getText(), "COMPLAIN");
        assertEquals(card.getElement().getChild(1).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(2).getText(), "App1");
        assertEquals(card.getElement().getChild(2).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(3).getText(), DateTimeFormatter.ofPattern("dd/MM/yyy").format(time));
        assertEquals(card.getElement().getChild(3).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(4).getText(), "true");
        assertEquals(card.getElement().getChild(4).getAttribute("class"), "name");
    }

    @Test
    public void getCardNotASubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        HorizontalLayout card = grid.getCard(createRequestComplain(time));
        assertEquals(card.getElement().getAttribute("class"), "card canOpen");
        assertEquals(card.getElement().getChild(0).getText(), "Sender1");
        assertEquals(card.getElement().getChild(0).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(1).getText(), "COMPLAIN");
        assertEquals(card.getElement().getChild(1).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(2).getText(), "App1");
        assertEquals(card.getElement().getChild(2).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(3).getText(), DateTimeFormatter.ofPattern("dd/MM/yyy").format(time));
        assertEquals(card.getElement().getChild(3).getAttribute("class"), "name");
        assertEquals(card.getElement().getChild(4).getText(), "true");
        assertEquals(card.getElement().getChild(4).getAttribute("class"), "name");
    }

    @Test
    public void getContentNullTest(){
        assertThrows(NullPointerException.class, () -> {
            GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);
            grid.getContent(null);
        }); 
    }

    @Test
    public void getContentSubjectComplaintTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.SUBJECT);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestComplain(time));


        assertEquals(layout.getElement().getTag(), "vaadin-vertical-layout");

        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(0).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(0).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(0).getChild(0).getText(), "Receiver User:   ");

        assertEquals(layout.getElement().getChild(0).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(0).getChild(1).getAttribute("class"), "link");
        assertEquals(layout.getElement().getChild(0).getChild(1).getText(), "Receiver1");

        assertEquals(layout.getElement().getChild(1).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(1).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(1).getChild(0).getText(), "Right:   ");

        assertEquals(layout.getElement().getChild(1).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getChild(1).getText(), "COMPLAIN");

        assertEquals(layout.getElement().getChild(2).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(2).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(2).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(2).getChild(0).getText(), "App:   ");

        assertEquals(layout.getElement().getChild(2).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(2).getChild(1).getAttribute("class"), "link");
        assertEquals(layout.getElement().getChild(2).getChild(1).getText(), "App1");

        assertEquals(layout.getElement().getChild(3).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(3).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(3).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(3).getChild(0).getText(), "Time:   ");

        assertEquals(layout.getElement().getChild(3).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(3).getChild(1).getText(), DateTimeFormatter.ofPattern("dd/MM/yyy").format(time));

        assertEquals(layout.getElement().getChild(4).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(4).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(4).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(4).getChild(0).getText(), "Content: ");

        assertEquals(layout.getElement().getChild(4).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(4).getChild(1).getText(), "Dear Receiver1"+", \n"+"I would like to know the complain with the supervision authority: TestComplaint about the app App1"+",\n"+"Best regards, \n"+"Sender1");

        assertEquals(layout.getElement().getChild(5).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(5).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(5).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(5).getChild(0).getText(), "Details:   ");

        assertEquals(layout.getElement().getChild(5).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(5).getChild(1).getText(), "TestDetails");

        assertEquals(layout.getElement().getChild(6).getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(6).getChild(0).getTag(), "span");
        assertEquals(layout.getElement().getChild(6).getChild(0).getAttribute("class"), "bold");
        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "Complain:   ");

        assertEquals(layout.getElement().getChild(6).getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(6).getChild(1).getText(), "TestComplaint");

        assertEquals(layout.getElement().getChild(7).getTag(), "vaadin-text-area");
        assertEquals(layout.getElement().getChild(7).getProperty("label"), "Controller response");
        assertEquals(layout.getElement().getChild(7).getProperty("value"), "TestResponse");        
        assertEquals(layout.getElement().getChild(7).getProperty("readonly"), "true");        

        assertEquals(layout.getElement().getChild(8).getTag(), "vaadin-checkbox");
        assertEquals(layout.getElement().getChild(8).getProperty("label"), "Handled");
        assertEquals(layout.getElement().getChild(8).getProperty("checked"), "true");
        assertEquals(layout.getElement().getChild(8).getProperty("readonly"), "true");         
    }

    @Test
    public void getContentNotSubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestComplain(time));

        assertEquals(layout.getElement().getChild(0).getChild(0).getText(), "Sender User:   ");

        assertEquals(layout.getElement().getChild(7).getTag(), "vaadin-text-area");
        assertEquals(layout.getElement().getChild(7).getProperty("label"), "Your response");
        assertEquals(layout.getElement().getChild(7).getProperty("value"), "TestResponse");
        assertEquals(layout.getElement().getChild(7).getProperty("placeholder"), "Write your response...");        
        assertEquals(layout.getElement().getChild(7).getProperty("readonly"), null);        

        assertEquals(layout.getElement().getChild(8).getTag(), "vaadin-checkbox");
        assertEquals(layout.getElement().getChild(8).getProperty("label"), "Handled");
        assertEquals(layout.getElement().getChild(8).getProperty("checked"), "true");
        assertEquals(layout.getElement().getChild(8).getProperty("readonly"), null);   
    }

    @Test
    public void getContentWithdrawConsentTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestWithdrawCconsent(time));

        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "Consent to withdraw:   ");
    }

    @Test
    public void getContentInfoTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestInfo(time));

        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "Info:   ");
    }

    @Test
    public void getContentErasureTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestErasure(time));

        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "What to erase:   ");
    }

    @Test
    public void getContentDeleteEverythingTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestDeleteEverything(time));

        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "");
    }

    @Test
    public void getContentPortabilityTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequestPortability(time));

        assertEquals(layout.getElement().getChild(6).getChild(0).getText(), "");
    }
}
