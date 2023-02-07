package com.privacydashboard.application.views.usefulComponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MyDialog extends Dialog{
    private final Button closeButton=new Button(VaadinIcon.CLOSE_SMALL.create(), e->close());
    private Button continueButton=new Button("Continue");
    private final Button cancelButton=new Button("Cancel", e->close());

    public MyDialog(){
        getElement().getThemeList().add("my-dialog");
        initializeButtons();
        this.setDraggable(true);
    }

    private void initializeButtons(){
        continueButton.addClassName("buuutton");
        closeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClassNames("cancelButton myDialogCancelButton");
    }

    public void setTitle(String title){
        this.setHeaderTitle(title);
        this.getHeader().add(closeButton);
    }

    public void setContent(Component content){
        add(content);
    }

    public void setContinueButton(Button button){
        this.continueButton=button;
        continueButton.addClassName("buuutton");
        this.getFooter().add(cancelButton, continueButton);
    }
}
