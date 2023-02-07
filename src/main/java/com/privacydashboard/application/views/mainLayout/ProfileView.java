package com.privacydashboard.application.views.mainLayout;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class ProfileView extends VerticalLayout {
    private User user;
    private final AuthenticatedUser authenticatedUser;
    private final DataBaseService dataBaseService;
    private final UserDetailsServiceImpl userDetailsService;

    private Span image;
    private Span name;
    private Span role;
    private Details changeMailDetails;
    private TextField newMail;
    private Details changePasswordDetails;
    private PasswordField actualPassword;
    private PasswordField newPassword;
    private PasswordField confirmPassword;
    private final MyDialog removeEverythingDialog= new MyDialog();
    private final Button logOutButton= new Button("LogOut", e-> logout());

    public ProfileView(User user, AuthenticatedUser authenticatedUser, DataBaseService dataBaseService, UserDetailsServiceImpl userDetailsService){
        this.user=user;
        this.authenticatedUser=authenticatedUser;
        this.dataBaseService=dataBaseService;
        this.userDetailsService=userDetailsService;

        if(!authenticatedUser.getUser().equals(user)){
            return;
        }

        addClassName("profile-view");
        initializeInformation();
        add(image, name, role, changeMailDetails, changePasswordDetails);
        if(user.getRole().equals(Role.SUBJECT)){
            initializeRemoveEverything();
            Button removeEverythingButton= new Button("Remove everything", e-> removeEverythingDialog.setOpened(true));
            removeEverythingButton.addClassName("buuutton");
            add(removeEverythingButton);
        }
        logOutButton.addClassName("cancelButton");
        add(logOutButton);
        setAlignItems(Alignment.CENTER);
    }

    private void initializeInformation(){
        Avatar avatar=new Avatar(user.getName(), user.getProfilePictureUrl());
        image=new Span(avatar);
        image.addClassName("pointer");
        image.addClickListener(e-> changeImage());

        name=new Span("Name: " + user.getName());
        role=new Span("Role: " + user.getRole());
        changeMailDetails= new Details("Mail: " + (user.getMail()==null ? "There's no mail yet" : user.getMail()), changeMailLayout());
        changePasswordDetails= new Details("Change password", changePasswordLayout());
    }

    private void initializeRemoveEverything(){
        Button confirm=new Button("Confirm", e-> {dataBaseService.removeEverythingFromUser(user);
            Notification notification = Notification.show("The request has been sent to the Data Controllers!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            removeEverythingDialog.close();});

        removeEverythingDialog.setContinueButton(confirm);
        removeEverythingDialog.setTitle("Confirm to remove everything");
        removeEverythingDialog.setContent(
                new VerticalLayout(new Span("Are you sure you want to remove all your personal information from every app you have?")));
    }


    private void changeImage(){

    }

    private VerticalLayout changeMailLayout(){
        newMail= new TextField("New Mail: ");
        Button save= new Button("Save");
        save.addClassName("buuutton");
        MyDialog dialog= new MyDialog();
        dialog.setTitle("Confirm new mail");
        dialog.setContent(new HorizontalLayout(new Span("Are you sure you want to change the mail?")));
        dialog.setContinueButton(new Button("Confirm", e->{saveMail();
                                                                dialog.close();}));
        save.addClickListener(e->dialog.open());
        newMail.addKeyDownListener(Key.ENTER, e->dialog.open());
        VerticalLayout layout=new VerticalLayout(newMail, save);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }

    private void saveMail(){
        userDetailsService.changeUserMail(user, newMail.getValue());
        closeMailDetail();
        Notification notification=Notification.show("Mail changed correctly!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        // update user information and corresponding components
        user=authenticatedUser.updateUser();
        newMail.setValue("");
        changeMailDetails.setSummaryText("Actual Mail: " + (user.getMail()==null ? "There's no mail yet" : user.getMail()));
    }

    private VerticalLayout changePasswordLayout(){
        actualPassword= new PasswordField("Actual Password");
        actualPassword.setErrorMessage("password must be the same as old one");
        actualPassword.setMinLength(8);
        actualPassword.addValueChangeListener(e-> checkActualPassword());

        newPassword= new PasswordField("New Password");
        newPassword.setMinLength(8);
        newPassword.setErrorMessage("the password must be at least 8 characters");
        newPassword.addValueChangeListener(e-> checkConfirmPassword());

        confirmPassword= new PasswordField("Confirm Password");
        confirmPassword.setErrorMessage("the two passwords must be equal");
        confirmPassword.addValueChangeListener(e-> checkConfirmPassword());

        Button save= new Button("Save", e->savePassword());
        save.addClassName("buuutton");
        VerticalLayout layout=new VerticalLayout(actualPassword, newPassword, confirmPassword, save);
        layout.setAlignItems(Alignment.CENTER);
        return layout;
    }

    private void checkActualPassword() {
        boolean valid=userDetailsService.isSamePassword(actualPassword.getValue(), user.getHashedPassword());
        actualPassword.setInvalid(!valid);
    }

    private void checkConfirmPassword(){
        boolean valid=confirmPassword.getValue().equals(newPassword.getValue());
        confirmPassword.setInvalid(!valid);
    }

    private void savePassword(){
        checkActualPassword();
        checkConfirmPassword();
        if(newPassword.getValue().length()<8){
            newPassword.setInvalid(true);
        }
        if(actualPassword.isInvalid() || newPassword.isInvalid() || confirmPassword.isInvalid()) {
            return;
        }

        MyDialog dialog= new MyDialog();
        dialog.setTitle("Confirm new password");
        dialog.setContent(new HorizontalLayout(new Span("Are you sure you want to change the password?")));
        dialog.setContinueButton(new Button("Confirm", e->{userDetailsService.changeUserPassword(user, newPassword.getValue());
            actualPassword.setValue("");
            newPassword.setValue("");
            confirmPassword.setValue("");
            closePasswordDetail();
            Notification notification = Notification.show("Password correctly changed!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            user=authenticatedUser.updateUser();
            dialog.close();}));
        dialog.open();
    }

    private void logout(){
        authenticatedUser.logout();
    }

    public void closePasswordDetail(){
        changePasswordDetails.setOpened(false);
    }

    public void closeMailDetail(){
        changeMailDetails.setOpened(false);
    }

}
