package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class MessageTest {
    
    @Test
    public void getSetSender(){
        Message mess = new Message();
        User user = new User();
        mess.setSender(user);
        assertEquals(user, mess.getSender());
    }
    
    @Test
    public void getSetReceiver(){
        Message mess = new Message();
        User user = new User();
        mess.setReceiver(user);
        assertEquals(user, mess.getReceiver());
    }

    @Test
    public void getSetMessage(){
        Message mess = new Message();
        mess.setMessage("Questo è un messaggio di prova.");
        assertEquals("Questo è un messaggio di prova.", mess.getMessage());
    }

    @Test
    public void getSetTime(){
        Message mess = new Message();
        LocalDateTime time = LocalDateTime.now();
        mess.setTime(time);
        assertEquals(time, mess.getTime());
    }
}
