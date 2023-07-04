package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.privacydashboard.application.data.GlobalVariables.RightType;
import java.time.LocalDateTime;

public class RightRequestTest {
    
    @Test
    public void getSetSender(){
        RightRequest req = new RightRequest();
        User user = new User();
        req.setSender(user);
        assertEquals(user, req.getSender());
    }

    @Test
    public void getSetReceiver(){
        RightRequest req = new RightRequest();
        User user = new User();
        req.setReceiver(user);
        assertEquals(user, req.getReceiver());
    }

    @Test
    public void getSetIoTApp(){
        RightRequest req = new RightRequest();
        IoTApp app = new IoTApp();
        req.setApp(app);
        assertEquals(app, req.getApp());
    }

    @Test
    public void getSetTime(){
        RightRequest req = new RightRequest();
        LocalDateTime time = LocalDateTime.now();
        req.setTime(time);
        assertEquals(time, req.getTime());
    }

    @Test
    public void getSetRightType(){
        RightRequest req = new RightRequest();
        req.setRightType(RightType.COMPLAIN);
        assertEquals(RightType.COMPLAIN, req.getRightType());
    }

    @Test
    public void getSetHandled(){
        RightRequest req = new RightRequest();
        req.setHandled(false);
        assertEquals(false, req.getHandled());
    }

    @Test
    public void getSetOther(){
        RightRequest req = new RightRequest();
        req.setOther("Questo è un Other di prova per l'oggetto RightRequest.");
        assertEquals("Questo è un Other di prova per l'oggetto RightRequest.", req.getOther());
    }

    @Test
    public void getSetResponse(){
        RightRequest req = new RightRequest();
        req.setResponse("Questo è un Response di prova per l'oggetto RightRequest.");
        assertEquals("Questo è un Response di prova per l'oggetto RightRequest.", req.getResponse());
    }

    @Test
    public void getSetDetails(){
        RightRequest req = new RightRequest();
        req.setDetails("Questo è un Details di prova per l'oggetto RightRequest.");
        assertEquals("Questo è un Details di prova per l'oggetto RightRequest.", req.getDetails());
    }
}

