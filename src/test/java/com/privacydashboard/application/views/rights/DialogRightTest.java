package com.privacydashboard.application.views.rights;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.RightRequest;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.rights.DialogRight;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.privacydashboard.application.data.entity.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class DialogRightTest {
    
    private DataBaseService dataBaseService;
    private AuthenticatedUser authenticatedUser;
    private DialogRight dialog;
    private User authUser;

    private RightRequest getRequest(RightType type){
        User sender = authUser;

        User receiver = new User();
        receiver.setName("TestReceiver");

        IoTApp app = new IoTApp();
        app.setName("TestApp");

        RightRequest req = new RightRequest();
        if(type != null)
            req.setRightType(type);
        req.setReceiver(receiver);
        req.setSender(sender);
        req.setApp(app);
        req.setOther("TestOther");

        return req;
    }

    private List<IoTApp> getApps(){
        IoTApp app1 = new IoTApp();
        app1.setName("TestApp1");

        IoTApp app2 = new IoTApp();
        app2.setName("TestApp2");

        List<IoTApp> list = new ArrayList<>();
        list.add(app1);
        list.add(app2);

        return list;
    }

    private User getUser(){
        User user = new User();
        user.setName("TestSender");
        user.setRole(Role.SUBJECT);

        return user;
    }

    @BeforeEach
    private void setup(){
        dataBaseService = mock(DataBaseService.class);
        authenticatedUser = mock(AuthenticatedUser.class);

        dialog = new DialogRight(dataBaseService, authenticatedUser);
        authUser = getUser();
    }

    @Test
    public void getPremadeTextNullTest(){
        assertThrows(NullPointerException.class, () -> {
            dialog.getPremadeText(getRequest(null));
        });
    }

    @Test
    public void getPremadeTextPortabilityTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.PORTABILITY)), "Dear TestReceiver, \nI would like to have access to my data from the app TestApp in a commonly used open format (XML, JSON),\nBest regards, \nTestSender");
    }

    @Test
    public void getPremadeTextWithdrawConsentTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.WITHDRAWCONSENT)), "Dear TestReceiver, \nI would like to withdraw the consent: TestOther from the app TestApp,\nBest regards, \nTestSender");
    }

    @Test
    public void getPremadeTextErasureTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.ERASURE)), "Dear TestReceiver, \nI would like to erase the following information: TestOther ,from the app TestApp,\nBest regards, \nTestSender");
    }

    @Test
    public void getPremadeTextInfoTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.INFO)), "Dear TestReceiver, \nI would like to know the following information: TestOther regarding the app TestApp,\nBest regards, \nTestSender");
    }

    @Test
    public void getPremadeTextComplainTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.COMPLAIN)), "Dear TestReceiver, \nI would like to know the complain with the supervision authority: TestOther about the app TestApp,\nBest regards, \nTestSender");
    }

    @Test
    public void getPremadeTextDeleteEverythingTest(){
        assertEquals(dialog.getPremadeText(getRequest(RightType.DELTEEVERYTHING)), "Dear TestReceiver, \nI would like to remove all my personal data from the app TestApp ,\nBest regards, \nTestSender");
    }

    private Field getRequestDialog(){
        try{
            Field field = DialogRight.class.getDeclaredField("requestDialog");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void showDialogRequestNullUserTest(){

        when(authenticatedUser.getUser()).thenReturn(null);

        dialog.showDialogRequest(RightType.COMPLAIN);

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(reqDialog.getElement().getTag(), "vaadin-dialog");
        assertEquals(reqDialog.getHeaderTitle(), "");
        assertEquals(reqDialog.getElement().getChildCount(), 1);
        assertEquals(reqDialog.getElement().getChild(0).getTag(), "template");
    }

    @Test
    public void showDialogRequestPortabilityTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.PORTABILITY);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(reqDialog.getHeaderTitle(), "Access data");
    }

    @Test
    public void showDialogRequestWithdrawConsentTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.WITHDRAWCONSENT);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(reqDialog.getHeaderTitle(), "Withdraw consent");
    }

    @Test
    public void showDialogRequestErasureTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.ERASURE);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(reqDialog.getHeaderTitle(), "Erase content");
    }

    @Test
    public void showDialogRequestInfoTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.INFO);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        


        assertEquals(reqDialog.getHeaderTitle(), "Get information");
    }

    @Test
    public void showDialogRequestComplainTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.COMPLAIN);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(reqDialog.getHeaderTitle(), "Compile a complain");
    }

    @Test
    public void showDialogRequestDeleteEverythingTest(){

        when(authenticatedUser.getUser()).thenReturn(getUser());
        when(dataBaseService.getUserApps(any())).thenReturn(getApps());

        try{
            dialog.showDialogRequest(RightType.DELTEEVERYTHING);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog reqDialog = new MyDialog();
        try{
            reqDialog = (MyDialog) getRequestDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        


        assertEquals(reqDialog.getHeaderTitle(), "Delete everything");
    }

    private Field getConfirmDialog(){
        try{
            Field field = DialogRight.class.getDeclaredField("confirmDialog");
            field.setAccessible(true);
            return field;
        }
        catch(NoSuchFieldException e){
            System.out.println("Exception: "+e);
        }
        return null;
    }

    @Test
    public void showDialogConfirmNullRequestTest(){

        try{
            dialog.showDialogConfirm(null);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmNullRightTypeTest(){
        User sender = new User();
        sender.setName("TestSender");
        sender.setRole(Role.SUBJECT);

        User receiver = new User();
        receiver.setName("TestReceiver");

        IoTApp app = new IoTApp();
        app.setName("TestApp");

        RightRequest req = new RightRequest();
        req.setReceiver(receiver);
        req.setSender(sender);
        req.setApp(app);
        req.setOther("TestOther");

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmNullReceiverTest(){
        User sender = new User();
        sender.setName("TestSender");
        sender.setRole(Role.SUBJECT);

        IoTApp app = new IoTApp();
        app.setName("TestApp");

        RightRequest req = new RightRequest();
        req.setRightType(RightType.COMPLAIN);
        req.setSender(sender);
        req.setApp(app);
        req.setOther("TestOther");

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmNullSenderTest(){
        User receiver = new User();
        receiver.setName("TestReceiver");

        IoTApp app = new IoTApp();
        app.setName("TestApp");

        RightRequest req = new RightRequest();
        req.setRightType(RightType.COMPLAIN);
        req.setReceiver(receiver);
        req.setApp(app);
        req.setOther("TestOther");

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmNullAppTest(){
        User sender = new User();
        sender.setName("TestSender");
        sender.setRole(Role.SUBJECT);

        User receiver = new User();
        receiver.setName("TestReceiver");

        RightRequest req = new RightRequest();
        req.setRightType(RightType.COMPLAIN);
        req.setReceiver(receiver);
        req.setSender(sender);
        req.setOther("TestOther");

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmSenderIsNotAuthUserTest(){
        User sender = new User();
        sender.setName("TestSender1");
        sender.setRole(Role.SUBJECT);

        User receiver = new User();
        receiver.setName("TestReceiver");

        IoTApp app = new IoTApp();
        app.setName("TestApp");

        RightRequest req = new RightRequest();
        req.setRightType(RightType.COMPLAIN);
        req.setReceiver(receiver);
        req.setSender(sender);
        req.setApp(app);
        req.setOther("TestOther");

        when(authenticatedUser.getUser()).thenReturn(getUser());

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmEmptyRequestTest(){
        RightRequest req = new RightRequest();

        try{
            dialog.showDialogConfirm(req);
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        
        
        assertEquals(cDialog.getHeaderTitle(), "");
    }

    @Test
    public void showDialogConfirmTest(){

        when(authenticatedUser.getUser()).thenReturn(authUser);

        try{
            dialog.showDialogConfirm(getRequest(RightType.COMPLAIN));
        }
        catch(IllegalStateException e){
            System.out.println("Dialog opened");
        }

        MyDialog cDialog = new MyDialog();
        try{
            cDialog = (MyDialog) getConfirmDialog().get((Object) dialog);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }        

        assertEquals(cDialog.getHeaderTitle(), "Confirm");
        assertEquals(cDialog.getElement().getChild(2).getChild(1).getText(), "Confirm");
    }
}
