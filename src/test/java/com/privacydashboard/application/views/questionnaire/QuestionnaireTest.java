package com.privacydashboard.application.views.questionnaire;

import com.privacydashboard.application.views.questionnaire.Questionnaire;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.privacydashboard.application.data.entity.IoTApp;

import io.opentelemetry.sdk.metrics.data.Data;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;


public class QuestionnaireTest {
    
    private static DataBaseService dataBaseService;
    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;

    private static Questionnaire view;

    private static Field getNewQuestionnaireDialog(){
        try{
            Field field = Questionnaire.class.getDeclaredField("newQuestionnaireDialog");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception :"+e);
        }
        return null;
    }

    private static IoTApp getAppWithVoteGreen(){
        IoTApp app = new IoTApp();
        app.setName("TestName");
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);
        return app;
    }

    private static IoTApp getAppWithVoteRed(){
        IoTApp app = new IoTApp();
        app.setName("TestName");
        app.setQuestionnaireVote(QuestionnaireVote.RED);
        return app;
    }

    private static IoTApp getAppWithVoteOrange(){
        IoTApp app = new IoTApp();
        app.setName("TestName");
        app.setQuestionnaireVote(QuestionnaireVote.ORANGE);
        return app;
    }

    private static IoTApp getAppNoVote(){
        IoTApp app = new IoTApp();
        app.setName("TestName");
        return app;
    }

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);

        view = new Questionnaire(dataBaseService, authenticatedUser, communicationService);
    }

    @Test
    public void QuestionnaireConstructorTest(){

        assertEquals(view.getElement().getTag(), "div");
        assertEquals(view.getElement().getAttribute("class"), "grid-view");

        assertEquals(view.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");
        assertEquals(view.getElement().getChild(0).getAttribute("style"), "align-items:center");

        assertEquals(view.getElement().getChild(0).getChild(0).getTag(), "vaadin-text-field");
        assertEquals(view.getElement().getChild(0).getChild(0).getAttribute("class"), "search");
        assertEquals(view.getElement().getChild(0).getChild(1).getTag(), "vaadin-button");
        assertEquals(view.getElement().getChild(0).getChild(1).getText(), "Compile new Questionnaire");

        assertEquals(view.getElement().getChild(1).getTag(), "vaadin-grid");
        assertEquals(view.getElement().getChild(1).getThemeList().toString(), "[no-row-borders, no-border]");

        assertEquals(view.getElement().getChild(1).getChild(0).getTag(), "vaadin-grid-column");
    }

    @Test
    public void newQuestionnaireDialogTest(){
        MyDialog dialog = new MyDialog();

        try{
            dialog = (MyDialog) getNewQuestionnaireDialog().get((Object) view);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(dialog.getHeaderTitle(), "Select app");
        assertEquals(dialog.getElement().getTag(), "vaadin-dialog");
    }

    private static Method getCreateQuestionnaireApp(){
        try{
            Method method = Questionnaire.class.getDeclaredMethod("createQuestionnaireApp", IoTApp.class);
            method.setAccessible(true);
            return method;
        }
        catch(NoSuchMethodException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void createQuestionnaireAppNullVoteTest(){
        HorizontalLayout layout = new HorizontalLayout();
        Method method = getCreateQuestionnaireApp();

        try{
            layout = (HorizontalLayout) method.invoke(view, (Object) getAppNoVote());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getAttribute("class"), "card canOpen");
        assertEquals(layout.getElement().getTag(), "vaadin-horizontal-layout");

        assertEquals(layout.getElement().getChild(0).getTag(), "vaadin-avatar");
        assertEquals(layout.getElement().getChild(1).getTag(), "span");
        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name link");
        assertEquals(layout.getElement().getChild(1).getText(), "TestName");
    }

    @Test
    public void createQuestionnaireAppGreenVoteTest(){
        HorizontalLayout layout = new HorizontalLayout();
        Method method = getCreateQuestionnaireApp();

        try{
            layout = (HorizontalLayout) method.invoke(view, (Object) getAppWithVoteGreen());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name linkQuestionnaire greenName");
    }

    //This test fails because the class orangeName is
    //erroneously attributed to the card instead of the name
    @Test
    public void createQuestionnaireAppOrangeVoteTest(){
        HorizontalLayout layout = new HorizontalLayout();
        Method method = getCreateQuestionnaireApp();

        try{
            layout = (HorizontalLayout) method.invoke(view, (Object) getAppWithVoteOrange());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name linkQuestionnaire orangeName");
    }

    //This test fails because the class orangeName is
    //erroneously attributed to the card instead of the name
    @Test
    public void createQuestionnaireAppRedVoteTest(){
        HorizontalLayout layout = new HorizontalLayout();
        Method method = getCreateQuestionnaireApp();

        try{
            layout = (HorizontalLayout) method.invoke(view, (Object) getAppWithVoteRed());
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(layout.getElement().getChild(1).getAttribute("class"), "name linkQuestionnaire redName");
    }
}
