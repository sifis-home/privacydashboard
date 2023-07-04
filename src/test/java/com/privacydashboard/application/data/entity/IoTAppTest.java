package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Hashtable;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;

public class IoTAppTest {
    
    @Test
    public void getSetName(){
        IoTApp app = new IoTApp();
        app.setName("SmartLed");
        assertEquals("SmartLed", app.getName());
    }

    @Test
    public void getSetDescription(){
        IoTApp app = new IoTApp();
        app.setDescription("Questa è una descrizione di prova per un oggetto IoTApp.");
        assertEquals("Questa è una descrizione di prova per un oggetto IoTApp.", app.getDescription());
    }

    @Test
    public void getSetConsenses(){
        IoTApp app = new IoTApp();
        String[] consenses = {"Consensus 1", "Consensus 2"};
        app.setConsenses(consenses);
        assertEquals(consenses, app.getConsenses());
    }

    @Test
    public void getSetQuestionnaireVote(){
        IoTApp app = new IoTApp();
        QuestionnaireVote vote = QuestionnaireVote.RED;
        app.setQuestionnaireVote(vote);
        assertEquals(vote, app.getQuestionnaireVote());
    }

    @Test
    public void getSetDetailVote(){
        IoTApp app = new IoTApp();
        String[] votes = {"Vote 1", "Vote 2"};
        app.setDetailVote(votes);
        assertEquals(votes, app.getDetailVote());
    }

    @Test
    public void getSetOptionalAnswers(){
        IoTApp app = new IoTApp();
        Hashtable table = new Hashtable<Integer, String>();
        table.put(0, "Primo");
        app.setOptionalAnswers(table);
        assertEquals(table, app.getOptionalAnswers());
    }
}
