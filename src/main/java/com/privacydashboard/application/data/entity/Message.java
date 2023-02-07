package com.privacydashboard.application.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="message")
public class Message extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "senderId")
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @Lob
    private String message;
    private LocalDateTime time;

    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
