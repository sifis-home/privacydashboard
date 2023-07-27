package com.privacydashboard.application.views.usefulComponents;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class ToggleButtonTest {
    
    @Test
    public void toggleButtonStringTest(){
        ToggleButton button = new ToggleButton("TestText");
        assertEquals(button.getLabel(), "TestText");
    }

    @Test
    public void toggleButtonBooleanValueTest(){
        ToggleButton button = new ToggleButton(true);
        assertEquals(button.getValue(), true);
    }

    @Test
    public void ToggleButtonStringBooleanTest(){
        ToggleButton button = new ToggleButton("TestText", false);
        assertEquals(button.getLabel(), "TestText");
        assertEquals(button.getValue(), false);
    }

    @Test
    public void isDisabledNullTest(){
        ToggleButton button = new ToggleButton("TestText");
        assertEquals(button.isDisabled(), false);
    }

    @Test
    public void setIsDisabledTest(){
        ToggleButton button = new ToggleButton("TestText");
        button.setDisabled(true);
        assertEquals(button.isDisabled(), true);
    }
}
