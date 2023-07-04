package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationTest {
    
    @Test
    public void getSetReceiver(){
        Notification note = new Notification();
        User user = new User();
        note.setReceiver(user);
        assertEquals(user, note.getReceiver());
    }

    @Test
    public void getSetSender(){
        Notification note = new Notification();
        User user = new User();
        note.setSender(user);
        assertEquals(user, note.getSender());
    }

    @Test
    public void getSetDescription(){
        Notification note = new Notification();
        note.setDescription("Questa è la descrizione di un test dell'oggetto Notification.");
        assertEquals("Questa è la descrizione di un test dell'oggetto Notification.", note.getDescription());
    }

    @Test 
    public void getSetObjectId(){
        Notification note = new Notification();
        note.setObjectId(new UUID(0, 1));
        assertEquals(new UUID(0, 1), note.getObjectId());
    }

    @Test
    public void getSetTime(){
        Notification note = new Notification();
        LocalDateTime time = LocalDateTime.now();
        note.setTime(time);
        assertEquals(time, note.getTime());
    }

    @Test
    public void getSetType(){
        Notification note = new Notification();
        note.setType("TestType");
        assertEquals("TestType", note.getType());
    }

    @Test
    public void getSetRead(){
        Notification note = new Notification();
        note.setRead(false);
        assertEquals(false, note.getRead());
    }
}
