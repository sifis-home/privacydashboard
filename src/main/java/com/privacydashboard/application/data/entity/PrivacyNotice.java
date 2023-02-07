package com.privacydashboard.application.data.entity;

import javax.persistence.*;

@Entity
@Table(name="privacy_notice")
public class PrivacyNotice extends AbstractEntity{
    @OneToOne
    @JoinColumn(name="app_id")
    private IoTApp app;
    @Lob    // LOB=Large OBject. In DB they will be stored as TEXT
    private String text;

    public IoTApp getApp() {
        return app;
    }
    public void setApp(IoTApp app) {
        this.app = app;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
