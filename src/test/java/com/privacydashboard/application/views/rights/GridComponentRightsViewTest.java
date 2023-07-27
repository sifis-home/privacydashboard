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

    private static RightRequest createRequest(LocalDateTime time){
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

        return req1;
    }
    
    @Test
    public void getHeaderLayoutSubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.SUBJECT);
        HorizontalLayout layout = grid.getHeaderLayout();
        List<Component> children = layout.getChildren().toList();
        assertEquals(layout.getElement().getAttribute("class"), "headerLayout");
        assertEquals(children.get(0).getElement().getText(), "RECEIVER");
        assertEquals(children.get(0).getElement().getAttribute("class"), "name");
        assertEquals(children.get(1).getElement().getText(), "RIGHT TYPE");
        assertEquals(children.get(1).getElement().getAttribute("class"), "name");
        assertEquals(children.get(2).getElement().getText(), "APP");
        assertEquals(children.get(2).getElement().getAttribute("class"), "name");
        assertEquals(children.get(3).getElement().getText(), "TIME");
        assertEquals(children.get(3).getElement().getAttribute("class"), "name");
        assertEquals(children.get(4).getElement().getText(), "HANDLED");
        assertEquals(children.get(4).getElement().getAttribute("class"), "name");
    }

    @Test
    public void getHeaderLayoutNotASubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);
        ToggleButton button = new ToggleButton("ButtonTest");
        HorizontalLayout layout = grid.getHeaderLayout(button);
        List<Component> children = layout.getChildren().toList();
        assertEquals(layout.getElement().getAttribute("class"), "headerLayout");
        assertEquals(children.get(0).getElement().getText(), "SENDER");
        assertEquals(children.get(0).getElement().getAttribute("class"), "name");
        assertEquals(children.get(1).getElement().getText(), "RIGHT TYPE");
        assertEquals(children.get(1).getElement().getAttribute("class"), "name");
        assertEquals(children.get(2).getElement().getText(), "APP");
        assertEquals(children.get(2).getElement().getAttribute("class"), "name");
        assertEquals(children.get(3).getElement().getText(), "TIME");
        assertEquals(children.get(3).getElement().getAttribute("class"), "name");
        assertEquals(children.get(4).getElement().getChild(0).getProperty("label"), "ButtonTest");
        assertEquals(children.get(4).getElement().getAttribute("class"), "name");
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

        HorizontalLayout card = grid.getCard(createRequest(time));
        List<Component> children = card.getChildren().toList();
        assertEquals(card.getElement().getAttribute("class"), "card canOpen");
        assertEquals(children.get(0).getElement().getText(), "Receiver1");
        assertEquals(children.get(0).getElement().getAttribute("class"), "name");
        assertEquals(children.get(1).getElement().getText(), "COMPLAIN");
        assertEquals(children.get(1).getElement().getAttribute("class"), "name");
        assertEquals(children.get(2).getElement().getText(), "App1");
        assertEquals(children.get(2).getElement().getAttribute("class"), "name");
        assertEquals(children.get(3).getElement().getText(), DateTimeFormatter.ofPattern("dd/MM/yyy").format(time));
        assertEquals(children.get(3).getElement().getAttribute("class"), "name");
        assertEquals(children.get(4).getElement().getText(), "true");
        assertEquals(children.get(4).getElement().getAttribute("class"), "name");
    }

    @Test
    public void getCardNotASubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        HorizontalLayout card = grid.getCard(createRequest(time));
        List<Component> children = card.getChildren().toList();
        assertEquals(card.getElement().getAttribute("class"), "card canOpen");
        assertEquals(children.get(0).getElement().getText(), "Sender1");
        assertEquals(children.get(0).getElement().getAttribute("class"), "name");
        assertEquals(children.get(1).getElement().getText(), "COMPLAIN");
        assertEquals(children.get(1).getElement().getAttribute("class"), "name");
        assertEquals(children.get(2).getElement().getText(), "App1");
        assertEquals(children.get(2).getElement().getAttribute("class"), "name");
        assertEquals(children.get(3).getElement().getText(), DateTimeFormatter.ofPattern("dd/MM/yyy").format(time));
        assertEquals(children.get(3).getElement().getAttribute("class"), "name");
        assertEquals(children.get(4).getElement().getText(), "true");
        assertEquals(children.get(4).getElement().getAttribute("class"), "name");
    }

    @Test
    public void gtContentNullTest(){
        assertThrows(NullPointerException.class, () -> {
            GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);
            grid.getContent(null);
        }); 
    }

    /*@Test
    public void getContentSubjectTest(){
        GridComponentRightsView grid = new GridComponentRightsView(Role.DPO);

        LocalDateTime time = LocalDateTime.now();

        VerticalLayout layout = grid.getContent(createRequest(time));

        System.out.println(layout.getElement());
        assertEquals(true, true);
    }*/
}
