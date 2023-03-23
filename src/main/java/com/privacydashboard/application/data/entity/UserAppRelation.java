package com.privacydashboard.application.data.entity;

import com.privacydashboard.application.data.GlobalVariables.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_app_relation")
public class UserAppRelation extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "appId")
    private IoTApp app;
    @Column(length=2000)
    private String[] consenses;
    private Role role;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
        this.role=user.getRole();
    }
    public IoTApp getApp() {
        return app;
    }
    public void setApp(IoTApp app) {
        this.app = app;
    }
    public String[] getConsenses() {
        return consenses;
    }
    public void setConsenses(String[] consenses) {
        this.consenses = consenses;
    }
}
