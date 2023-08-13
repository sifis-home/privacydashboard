package com.privacydashboard.application.views.questionnaire;

import static org.mockito.Mockito.when;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.contextmenu.ContextMenu;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.privacydashboard.application.views.questionnaire.SingleQuestionnaire;
import com.vaadin.flow.component.tabs.Tabs;
import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;

public class SingleQuestionnaireTest {
    
    private static DataBaseService dataBaseService;
    private static CommunicationService communicationService;

    private static SingleQuestionnaire questionnaire;

    @BeforeAll
    private static void setup(){
        dataBaseService = mock(DataBaseService.class);
        communicationService = mock(CommunicationService.class);

        questionnaire = new SingleQuestionnaire(dataBaseService, communicationService);
    }

    @Test
    public void SingleQuestionnaireConstructorTest(){

        assertEquals(questionnaire.getElement().getTag(), "vaadin-app-layout");

        assertEquals(questionnaire.getElement().getChild(0).getTag(), "span");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getAttribute("class"), "section-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(0).getChild(0).getText(), "Have you identified all the personal data that are going to be processed?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getAttribute("class"), "singleQuestion-questionnaire singleQuestionHidden-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(0).getChild(0).getText(), "Are the personal data processed limited and used only for the purposes for which they are processed?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(1).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(0).getChild(0).getText(), "For how long are the data going to be stored?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "less than 1 month");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "between 1 month and 6 months");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "between 6 months and 2 years");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(3).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(3).getChild(0).getChild(0).getText(), "more than 2 years");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(4).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(4).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(2).getChild(1).getChild(4).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(0).getChild(0).getText(), "Do you have an automatic mechanism that deletes the personal data after the chosen period of time?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(3).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(0).getChild(0).getText(), "Does the app transfer data to a third party?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "Yes, only in European Union");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "Yes, also outside of European Union");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(3).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(1).getChild(3).getChild(0).getChild(0).getText(), "I don't know");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(2).getTag(), "vaadin-text-area");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(2).getAttribute("class"), "textArea-questionnaire");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(4).getChild(2).getProperty("label"), "List which third countries, if any");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getAttribute("class"), "singleQuestion-questionnaire singleQuestionHidden-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(0).getChild(0).getText(), "Does the Commission have decided that these countries have an adequate level of protection?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes, for all of them");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "Not for all of them");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "For none of them");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(3).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(5).getChild(1).getChild(3).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(0).getChild(0).getText(), "If you use external servers, where are they located?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "I don't use external servers");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "They're located only in United Europe");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "They're located also outside United Europe");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(3).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(6).getChild(1).getChild(3).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(0).getChild(0).getText(), "Is there an automated decision-making, including profiling?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(7).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getTag(), "vaadin-vertical-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getAttribute("class"), "singleQuestion-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(0).getTag(), "div");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(0).getChild(0).getText(), "Are the data stored in a way that they're easily accessible to its legitimate owner when needed?");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(0).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(0).getChild(1).getAttribute("class"), "las la-info-circle pointer");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getTag(), "vaadin-radio-group");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(0).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(1).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(2).getChild(0).getTag(), "label");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(8).getChild(1).getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getTag(), "vaadin-horizontal-layout");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getAttribute("class"), "buttonLayout-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getChild(0).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getChild(0).getAttribute("class"), "las la-2x la-arrow-circle-left button-questionnaire");

        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getChild(1).getTag(), "span");
        assertEquals(questionnaire.getElement().getChild(0).getChild(0).getChild(9).getChild(1).getAttribute("class"), "las la-2x la-arrow-circle-right button-questionnaire activeButton-questionnaire pointer");
    }

    private Field getTitles(){
        try{
            Field field = SingleQuestionnaire.class.getDeclaredField("titles");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    private boolean hasAnIcon(int i){
        switch(i){
            case 14:{
                return false;
            }
            case 15:{
                return false;
            }
            case 19:{
                return false;
            }
            case 29:{
                return false;
            }
            default:{
                return true;
            }
        }
    }

    @Test
    public void titlesTest(){
        Div[] titles = new Div[GlobalVariables.nQuestions];

        try{
            titles = (Div[]) getTitles().get((Object) questionnaire);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
        
        for (int i = 9; i< GlobalVariables.nQuestions; i++) {
            if(hasAnIcon(i))
                assertEquals(titles[i].getElement().getChild(1).getAttribute("class"), "las la-info-circle pointer");
        }

        //Section2
        assertEquals(titles[9].getElement().getChild(0).getText(), "Do you store passwords in plain text or do you encrypt them?");
        assertEquals(titles[10].getElement().getChild(0).getText(), "Which hash algorithm do you use to encrypt the passwords?");
        assertEquals(titles[11].getElement().getChild(0).getText(), "Do you have any password constraints?");
        assertEquals(titles[12].getElement().getChild(0).getText(), "Does your app guarantee confidentiality?");
        assertEquals(titles[13].getElement().getChild(0).getText(), "Does your app guarantee integrity?");
        assertEquals(titles[14].getElement().getChild(0).getText(), "Do you pseudonymize the personal data?");
        assertEquals(titles[15].getElement().getChild(0).getText(), "Do you encrypt the personal data?");
        assertEquals(titles[16].getElement().getChild(0).getText(), "Which cryptographic protocol are you using for communication?");
        assertEquals(titles[17].getElement().getChild(0).getText(), "Do you limit the communication ports to the strictly necessary? (ex: only port 443 and 80 for https)");
        assertEquals(titles[18].getElement().getChild(0).getText(), "How often do you regularly make backups");
        assertEquals(titles[19].getElement().getChild(0).getText(), "Have you identified some procedures the availability and access to personal data in case of an incident?");

        //Section2
        assertEquals(titles[20].getElement().getChild(0).getText(), "Did you follow OpenChain specification or other public specification for licensing compliance?");
        assertEquals(titles[21].getElement().getChild(0).getText(), "Did you perform a Privacy Impact Assessment for at least a standard use case?");
        assertEquals(titles[22].getElement().getChild(0).getText(), "Is the Privacy Impact Assessment easily available at request?");
        assertEquals(titles[23].getElement().getChild(0).getText(), "Do you use any static analysis tool for code quality?");
        assertEquals(titles[24].getElement().getChild(0).getText(), "Do you have any certifications to demonstrate compliance with security requirements?");
        assertEquals(titles[25].getElement().getChild(0).getText(), "Do you use third party libraries?");
        assertEquals(titles[26].getElement().getChild(0).getText(), "Have you checked if these libraries comply with the current regulation about personal data?");
        assertEquals(titles[27].getElement().getChild(0).getText(), "Did you successfully perform any test on the software functionalities (unit test, integration test...)?");
        assertEquals(titles[28].getElement().getChild(0).getText(), "Did you perform a code coverage analysis of your test?");
        assertEquals(titles[29].getElement().getChild(0).getText(), "Do you regularly test the effectiveness of your measures for ensuring the security of the process?");
    }

    private Field getTextAreas(){
        try{
            Field field = SingleQuestionnaire.class.getDeclaredField("textAreas");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void textAreasTest(){
        TextArea[] areas = new TextArea[GlobalVariables.nQuestions];

        try{
            areas = (TextArea[]) getTextAreas().get((Object) questionnaire);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        assertEquals(areas[10].getElement().getProperty("label"), "If other, which one?");
        assertEquals(areas[11].getElement().getProperty("label"), "List which constraints, if any");
        assertEquals(areas[12].getElement().getProperty("label"), "How do you guarantee it?");
        assertEquals(areas[13].getElement().getProperty("label"), "How do you guarantee it?");
        assertEquals(areas[20].getElement().getProperty("label"), "If other, which one?");
        assertEquals(areas[23].getElement().getProperty("label"), "If yes, which tools?");
        assertEquals(areas[27].getElement().getProperty("label"), "If yes, which tests did you perform?");
    }

    private Field getContextMenus(){
        try{
            Field field = SingleQuestionnaire.class.getDeclaredField("contextMenus");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void contextMenusTest(){
        ContextMenu[] menus = new ContextMenu[GlobalVariables.nQuestions];

        try{
            menus = (ContextMenu[]) getContextMenus().get((Object) questionnaire);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        for(int i = 0; i < GlobalVariables.nQuestions; i++){
            assertEquals(menus[i].getElement().getTag(), "vaadin-context-menu");
            assertEquals(menus[i].getElement().getAttribute("class"), "info");
        }

        assertEquals(menus[0].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[0].getItems().get(0).getElement().getText(), "According to the GDPR, personal data has to be processed in a particular and restricted way, so it it important to identify which are the personal dara that are going to be processed");

        assertEquals(menus[1].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[1].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[1].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[1].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 5</b>: <i>Personal data shall be: ... collected for specified, explicit and legitimate purposes and not further processed in a manner that is incompatible with those purposes ... adequate, relevant and limited to what is necessary in relation to the purposes for which they are processed</i>");

        assertEquals(menus[2].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[2].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[2].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[2].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 13</b>: <i>the controller shall, at the time when personal data are obtained, provide the data subject with the following further information... the period for which the personal data will be stored</i>");

        assertEquals(menus[3].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[3].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[3].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[3].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 13</b>: <i>the controller shall, at the time when personal data are obtained, provide the data subject with the following further information... the period for which the personal data will be stored</i>");

        assertEquals(menus[4].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[4].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[4].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[4].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 13</b>: <i>Where personal data relating to a data subject are collected from the data subject, the controller shall, at the time when personal data are obtained, provide the data subject with... the fact that the controller intends to transfer personal data to a third country or international organisation</i>");

        assertEquals(menus[5].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[5].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[5].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[5].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 45</b>: <i>A transfer of personal data to a third country or an international organisation may take place where the Commission has decided that the third country, a territory or one or more specified sectors within that third country, or the international organisation in question ensures an adequate level of protection</i>");

        assertEquals(menus[6].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[6].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[6].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[6].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 13</b>: <i>Where personal data relating to a data subject are collected from the data subject, the controller shall, at the time when personal data are obtained, provide the data subject with... the fact that the controller intends to transfer personal data to a third country or international organisation</i>");

        assertEquals(menus[7].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[7].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[7].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[7].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 13</b>: <i>the controller shall, at the time when personal data are obtained, provide the data subject with the following further information... the existence of automated decision-making, including profiling</i>");

        assertEquals(menus[8].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[8].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[8].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[8].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 12</b>: <i>The controller shall facilitate the exercise of data subject rights</i>");

        //section2
        assertEquals(menus[9].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[9].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[9].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[9].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[10].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[10].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[10].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[10].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[11].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[11].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[11].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[11].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[12].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[12].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[12].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[12].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[13].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[13].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[13].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[13].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[14].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[14].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[14].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[14].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 25</b>: <i> the controller shall, both at the time of the determination of the means for processing and at the time of the processing itself, implement appropriate technical and organisational measures, such as pseudonymisation, which are designed to implement data-protection principles</i>");

        assertEquals(menus[15].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[15].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[15].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[15].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i>the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk, including inter alia as appropriate: ... encryption of personal data;</i>");

        assertEquals(menus[16].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[16].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[16].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[16].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");        

        assertEquals(menus[17].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[17].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[17].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[17].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[18].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[18].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[18].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[18].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i> the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk</i>");

        assertEquals(menus[19].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[19].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[19].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[19].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i>the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk, including inter alia as appropriate: ... the ability to restore the availability and access to personal data in a timely manner in the event of a physical or technical incident;</i>");

        //section3
        assertEquals(menus[20].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[20].getItems().get(0).getElement().getText(), "It is easier to find if a software if compliant to the GDPR if it is open source");

        assertEquals(menus[21].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[21].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[21].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[21].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 35</b>: <i> the controller shall, prior to the processing, carry out an assessment of the impact of the envisaged processing operations on the protection of personal data ... A data protection impact assessment referred to in paragraph 1 shall in particular be required in the case of:\n" +
        "\n" +
        "    a systematic and extensive evaluation of personal aspects \n" +
        "    processing on a large scale of special categories of data \n" +
        "    a systematic monitoring of a publicly accessible area on a large scale</i>");

        assertEquals(menus[22].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[22].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[22].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[22].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 35</b>: <i> the controller shall, prior to the processing, carry out an assessment of the impact of the envisaged processing operations on the protection of personal data ... A data protection impact assessment referred to in paragraph 1 shall in particular be required in the case of:\n" +
        "\n" +
        "    a systematic and extensive evaluation of personal aspects \n" +
        "    processing on a large scale of special categories of data \n" +
        "    a systematic monitoring of a publicly accessible area on a large scale</i>");

        assertEquals(menus[23].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[23].getItems().get(0).getElement().getText(), "Static analysis tools can help find vulnerabilities and reduce the complexity of the software");

        assertEquals(menus[24].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[24].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[24].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[24].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 25</b>: <i>An approved certification mechanism pursuant to Article 42 may be used as an element to demonstrate compliance with the requirements set out in paragraphs 1 and 2 of this Article</i>");

        assertEquals(menus[25].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[25].getItems().get(0).getElement().getText(), "Third party libraries may reduce the security of the software, pay attention to what you include in your project");

        assertEquals(menus[26].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[26].getItems().get(0).getElement().getText(), "Third party libraries may reduce the security of the software, pay attention to what you include in your project");

        assertEquals(menus[27].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[27].getItems().get(0).getElement().getText(), "It is important to test your app to prevent any unintentional and possibly dangerous behaviour");

        assertEquals(menus[28].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");
        assertEquals(menus[28].getItems().get(0).getElement().getText(), "It is important to test your app to prevent any unintentional and possibly dangerous behaviour");

        assertEquals(menus[29].getItems().get(0).getElement().getTag(), "vaadin-context-menu-item");

        assertEquals(menus[29].getItems().get(0).getElement().getChild(0).getTag(), "p");
        assertEquals(menus[29].getItems().get(0).getElement().getChild(0).getAttribute("class"), "info");
        assertEquals(menus[29].getItems().get(0).getElement().getChild(0).getProperty("innerHTML"), "<b>GDPR Article 32</b>: <i>the controller and the processor shall implement appropriate technical and organisational measures to ensure a level of security appropriate to the risk, including inter alia as appropriate: ... a process for regularly testing, assessing and evaluating the effectiveness of technical and organisational measures for ensuring the security of the processing.</i>");
    }

    private Field getRadioGroups(){
        try{
            Field field = SingleQuestionnaire.class.getDeclaredField("radioGroups");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void radioGroupsTest(){
        RadioButtonGroup<String>[] groups = new RadioButtonGroup[GlobalVariables.nQuestions];

        try{
            groups = (RadioButtonGroup<String>[]) getRadioGroups().get((Object) questionnaire);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

        //section2
        assertEquals(groups[9].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[9].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[9].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[9].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[9].getElement().getChild(0).getChild(0).getChild(0).getText(), "I don't store passwords");

        assertEquals(groups[9].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[9].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[9].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[9].getElement().getChild(1).getChild(0).getChild(0).getText(), "I encrypt them");

        assertEquals(groups[9].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[9].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[9].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[9].getElement().getChild(2).getChild(0).getChild(0).getText(), "Plain text");

        assertEquals(groups[9].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[9].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[9].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[9].getElement().getChild(3).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[10].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[10].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[10].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[10].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[10].getElement().getChild(0).getChild(0).getChild(0).getText(), "SHA-3");

        assertEquals(groups[10].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[10].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[10].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[10].getElement().getChild(1).getChild(0).getChild(0).getText(), "SHA-2");

        assertEquals(groups[10].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[10].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[10].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[10].getElement().getChild(2).getChild(0).getChild(0).getText(), "SHA-1");

        assertEquals(groups[10].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[10].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[10].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[10].getElement().getChild(3).getChild(0).getChild(0).getText(), "MD-5");

        assertEquals(groups[10].getElement().getChild(4).getTag(), "vaadin-radio-button");
        assertEquals(groups[10].getElement().getChild(4).getChild(0).getTag(), "label");
        assertEquals(groups[10].getElement().getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[10].getElement().getChild(4).getChild(0).getChild(0).getText(), "other");

        
        assertEquals(groups[11].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[11].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[11].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[11].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[11].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[11].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[11].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[11].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[11].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[11].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[11].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[11].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[11].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[12].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[12].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[12].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[12].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[12].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[12].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[12].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[12].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[12].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[12].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[12].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[12].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[12].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[13].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[13].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[13].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[13].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[13].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[13].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[13].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[13].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[13].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[13].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[13].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[13].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[13].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[14].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[14].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[14].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[14].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[14].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[14].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[14].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[14].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[14].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[14].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[14].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[14].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[14].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[15].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[15].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[15].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[15].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[15].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[15].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[15].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[15].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[15].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[15].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[15].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[15].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[15].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[16].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[16].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(0).getChild(0).getChild(0).getText(), "My app doesn't need communication");

        assertEquals(groups[16].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(1).getChild(0).getChild(0).getText(), "TLS 1.2 or 1.3");

        assertEquals(groups[16].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(2).getChild(0).getChild(0).getText(), "TLS < 1.2");

        assertEquals(groups[16].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(3).getChild(0).getChild(0).getText(), "SSL");

        assertEquals(groups[16].getElement().getChild(4).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(4).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(4).getChild(0).getChild(0).getText(), "I don't use any");

        assertEquals(groups[16].getElement().getChild(5).getTag(), "vaadin-radio-button");
        assertEquals(groups[16].getElement().getChild(5).getChild(0).getTag(), "label");
        assertEquals(groups[16].getElement().getChild(5).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[16].getElement().getChild(5).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[17].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[17].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[17].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[17].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[17].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[17].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[17].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[17].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[17].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[17].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[17].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[17].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[17].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[18].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[18].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(0).getChild(0).getChild(0).getText(), "every week");

        assertEquals(groups[18].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(1).getChild(0).getChild(0).getText(), "between a week and a month");

        assertEquals(groups[18].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(2).getChild(0).getChild(0).getText(), "between a month and a year");

        assertEquals(groups[18].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(3).getChild(0).getChild(0).getText(), "more than a year");

        assertEquals(groups[18].getElement().getChild(4).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(4).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(4).getChild(0).getChild(0).getText(), "never");

        assertEquals(groups[18].getElement().getChild(5).getTag(), "vaadin-radio-button");
        assertEquals(groups[18].getElement().getChild(5).getChild(0).getTag(), "label");
        assertEquals(groups[18].getElement().getChild(5).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[18].getElement().getChild(5).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[19].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[19].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[19].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[19].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[19].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[19].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[19].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[19].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[19].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[19].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[19].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[19].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[19].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        //section3
        assertEquals(groups[20].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[20].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[20].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[20].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[20].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes, OpenChain");

        assertEquals(groups[20].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[20].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[20].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[20].getElement().getChild(1).getChild(0).getChild(0).getText(), "Yes, other");

        assertEquals(groups[20].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[20].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[20].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[20].getElement().getChild(2).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[20].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[20].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[20].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[20].getElement().getChild(3).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[21].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[21].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[21].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[21].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[21].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[21].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[21].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[21].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[21].getElement().getChild(1).getChild(0).getChild(0).getText(), "No, but it wasn't necessary");

        assertEquals(groups[21].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[21].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[21].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[21].getElement().getChild(2).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[21].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[21].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[21].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[21].getElement().getChild(3).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[22].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[22].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[22].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[22].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[22].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[22].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[22].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[22].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[22].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[22].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[22].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[22].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[22].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[23].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[23].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[23].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[23].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[23].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[23].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[23].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[23].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[23].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[23].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[23].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[23].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[23].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[24].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[24].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[24].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[24].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[24].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[24].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[24].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[24].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[24].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[24].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[24].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[24].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[24].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[25].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[25].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[25].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[25].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[25].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[25].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[25].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[25].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[25].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[25].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[25].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[25].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[25].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[26].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[26].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[26].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[26].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[26].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[26].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[26].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[26].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[26].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[26].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[26].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[26].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[26].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[27].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[27].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[27].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[27].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[27].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[27].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[27].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[27].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[27].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[27].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[27].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[27].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[27].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[28].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[28].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes, >=90%");

        assertEquals(groups[28].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(1).getChild(0).getChild(0).getText(), "Yes, >=75% <90%");

        assertEquals(groups[28].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(2).getChild(0).getChild(0).getText(), "Yes, >=50% <75%");

        assertEquals(groups[28].getElement().getChild(3).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(3).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(3).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(3).getChild(0).getChild(0).getText(), "Yes, <50%");

        assertEquals(groups[28].getElement().getChild(4).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(4).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(4).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(4).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[28].getElement().getChild(5).getTag(), "vaadin-radio-button");
        assertEquals(groups[28].getElement().getChild(5).getChild(0).getTag(), "label");
        assertEquals(groups[28].getElement().getChild(5).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[28].getElement().getChild(5).getChild(0).getChild(0).getText(), "I don't know");


        assertEquals(groups[29].getElement().getTag(), "vaadin-radio-group");

        assertEquals(groups[29].getElement().getChild(0).getTag(), "vaadin-radio-button");
        assertEquals(groups[29].getElement().getChild(0).getChild(0).getTag(), "label");
        assertEquals(groups[29].getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[29].getElement().getChild(0).getChild(0).getChild(0).getText(), "Yes");

        assertEquals(groups[29].getElement().getChild(1).getTag(), "vaadin-radio-button");
        assertEquals(groups[29].getElement().getChild(1).getChild(0).getTag(), "label");
        assertEquals(groups[29].getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[29].getElement().getChild(1).getChild(0).getChild(0).getText(), "No");

        assertEquals(groups[29].getElement().getChild(2).getTag(), "vaadin-radio-button");
        assertEquals(groups[29].getElement().getChild(2).getChild(0).getTag(), "label");
        assertEquals(groups[29].getElement().getChild(2).getChild(0).getChild(0).getTag(), "span");
        assertEquals(groups[29].getElement().getChild(2).getChild(0).getChild(0).getText(), "I don't know");
    }
}
