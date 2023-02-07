package com.privacydashboard.application.data.entity;

import com.privacydashboard.application.data.GlobalVariables.RightType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="right_request")
public class RightRequest extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "app_id")
    private IoTApp app;
    private LocalDateTime time;
    private String details;
    private Boolean handled;
    @Enumerated(EnumType.STRING)
    private RightType rightType;
    private String other;
    private String response;

    public User getSender(){ return sender;}
    public void setSender(User sender){ this.sender=sender;}
    public User getReceiver(){ return receiver;}
    public void setReceiver(User receiver){ this.receiver=receiver;}
    public IoTApp getApp(){ return app;}
    public void setApp(IoTApp app){ this.app=app;}
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public RightType getRightType() {
        return rightType;
    }
    public void setRightType(RightType rightType) {
        this.rightType = rightType;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public Boolean getHandled(){ return handled;}
    public void setHandled(Boolean handled){ this.handled=handled;}
    public String getOther(){ return other;}
    public void setOther(String other){ this.other=other; }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
}
