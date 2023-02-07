package com.privacydashboard.application.data;

import java.util.*;

public class GlobalVariables {
    public static final int nQuestions=30;
    public static final List<String> notificationType= Arrays.asList("Message", "Request", "PrivacyNotice");
    public static String pageTitle="";
    public enum RightType {
        WITHDRAWCONSENT, COMPLAIN, ERASURE, DELTEEVERYTHING, INFO, PORTABILITY
    }
    public static final HashMap rightDict= new HashMap<RightType, String>(){{put(RightType.WITHDRAWCONSENT, "WITHDRAW CONSENT");
                                                                put(RightType.COMPLAIN, "COMPLAIN");
                                                                put(RightType.ERASURE, "ERASURE");
                                                                put(RightType.DELTEEVERYTHING, "DELETE EVERYTHING");
                                                                put(RightType.INFO, "INFORMATION");
                                                                put(RightType.PORTABILITY, "DATA PORTABILITY");
                                                                }};
    public enum Role {
        SUBJECT, CONTROLLER, DPO
    }
    public enum QuestionnaireVote {
        RED, ORANGE, GREEN
    }
}
