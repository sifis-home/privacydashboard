package com.privacydashboard.application.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification")
public class Notification extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "senderId")
    private User sender;
    private String description;
    private UUID objectId;
    private String type;
    private LocalDateTime time;
    private Boolean isRead;

    public User getReceiver() {
        return receiver;
    }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public UUID getObjectId() {
        return objectId;
    }
    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public Boolean getRead() {
        return isRead;
    }
    public void setRead(Boolean read) {
        isRead = read;
    }
}
