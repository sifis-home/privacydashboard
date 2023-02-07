package com.privacydashboard.application.data.entity;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;

import javax.persistence.*;
import java.util.Hashtable;

@Entity
@Table(name= "iot_app")
public class IoTApp extends AbstractEntity{
    private String name;
    private String description;
    private QuestionnaireVote questionnaireVote;
    @Column(length=2000)
    private String[] detailVote;
    @Lob
    private Hashtable<Integer, String> optionalAnswers;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public QuestionnaireVote getQuestionnaireVote() {
        return questionnaireVote;
    }
    public void setQuestionnaireVote(QuestionnaireVote questionnaireVote) {
        this.questionnaireVote = questionnaireVote;
    }
    public String[] getDetailVote() {
        return detailVote;
    }
    public void setDetailVote(String[] detailVote) {
        this.detailVote=detailVote;
    }
    public Hashtable<Integer, String> getOptionalAnswers() {
        return optionalAnswers;
    }
    public void setOptionalAnswers(Hashtable<Integer, String> optionalAnswers) {
        this.optionalAnswers = optionalAnswers;
    }
}
