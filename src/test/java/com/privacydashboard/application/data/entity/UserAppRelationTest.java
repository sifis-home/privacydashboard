package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.privacydashboard.application.data.GlobalVariables.Role;

public class UserAppRelationTest {
    
    @Test
    //Role viene inizializzato ma non può mai essere letto.
    //Inoltre, essendo un campo di User che viene già salvato 
    //all'interno di UserAppRelation, risulta essere ridondante
    public void getSetUser(){
        UserAppRelation uApp = new UserAppRelation();
        User user = new User();
        user.setRole(Role.SUBJECT);
        uApp.setUser(user);
        assertEquals(user, uApp.getUser());
    }

    @Test
    public void getSetIoTApp(){
        UserAppRelation uApp = new UserAppRelation();
        IoTApp app = new IoTApp();
        uApp.setApp(app);
        assertEquals(app, uApp.getApp());
    }

    @Test
    public void getSetConsenses(){
        UserAppRelation uApp = new UserAppRelation();
        String[] consenses = {"Consensus 1", "Consensus 2"};
        uApp.setConsenses(consenses);
        assertEquals(consenses, uApp.getConsenses());
    }
}
