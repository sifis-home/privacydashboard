package com.privacydashboard.application.views.usefulComponents;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Header;

public class MyDialogTest {
    
    @Test
    public void setTitleTest(){
        MyDialog dialog = new MyDialog();
        dialog.setTitle("TestTitle");
        assertEquals(dialog.getHeaderTitle(), "TestTitle");
    }

    @Test
    public void setContentNullTest(){
        assertThrows(NullPointerException.class, () -> {
            MyDialog dialog = new MyDialog();
            dialog.setContent(null);
        });
    }

    @Test
    public void setContentTest(){
        Header header = new Header();
        header.setText("TestText");
        MyDialog dialog = new MyDialog();
        dialog.setContent(header);
        assertEquals(dialog.getChildren().toList().get(0).getElement().getText(), "TestText");
    }

    @Test
    public void setContinueButtonNullTest(){
        assertThrows(NullPointerException.class, () -> {
            MyDialog dialog = new MyDialog();
            dialog.setContinueButton(null);
        });
    }

    @Test
    public void setContinueButtonTest(){
        MyDialog dialog = new MyDialog();
        Button button = new Button();
        button.setText("TestText");
        dialog.setContinueButton(button);
        assertEquals(dialog.getElement().getChild(1).getChild(0).getText(), "Cancel");
        assertEquals(dialog.getElement().getChild(1).getChild(0).getAttribute("class"), "cancelButton myDialogCancelButton");
        assertEquals(dialog.getElement().getChild(1).getChild(1).getText(), "TestText");
        assertEquals(dialog.getElement().getChild(1).getChild(1).getAttribute("class"), "buuutton");
    }
}
