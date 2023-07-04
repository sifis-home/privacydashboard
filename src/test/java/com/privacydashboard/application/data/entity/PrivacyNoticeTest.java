package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrivacyNoticeTest {
    
    @Test
    public void getSetIoTApp(){
        PrivacyNotice pNotice = new PrivacyNotice();
        IoTApp app = new IoTApp();
        pNotice.setApp(app);
        assertEquals(app, pNotice.getApp());
    }

    @Test
    public void getSetText(){
        PrivacyNotice pNotice = new PrivacyNotice();
        pNotice.setText("Questo è un testo di prova per l'oggetto PrivacyNotice.");
        assertEquals("Questo è un testo di prova per l'oggetto PrivacyNotice.", pNotice.getText());
    }
}
