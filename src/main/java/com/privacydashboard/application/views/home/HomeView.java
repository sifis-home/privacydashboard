package com.privacydashboard.application.views.home;

import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.Arrays;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
@PermitAll
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class HomeView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;
    private final CommunicationService communicationService;

    private final int nSection=7;
    private final int nRows=2;
    private final VerticalLayout[] layouts= new VerticalLayout[nSection];
    private final Span[] titles= new Span[nSection];
    private final Div[] icons= new Div[nSection];

    public HomeView(AuthenticatedUser authenticatedUser, CommunicationService communicationService) {
        this.authenticatedUser= authenticatedUser;
        this.communicationService= communicationService;

        addClassName("home-view");

        initializeLayout();
        createSections();
        add(layouts);
        /*for(int i=0;i<nSection;i++){
        }*/
        //add(new Span("ID: " + authenticatedUser.getUser().getId().toString()));
    }

    private void initializeLayout(){
        for(int i=0; i<nSection; i++){
            layouts[i]= new VerticalLayout();
            layouts[i].addClassNames("section", "pointer");
            titles[i]= new Span();
            titles[i].addClassName("title");
            icons[i]= new Div();
            icons[i].addClassNames("las la-10x icons");
            layouts[i].add(titles[i], icons[i]);
            layouts[i].setAlignItems(Alignment.CENTER);
        }
    }

    private void createSections(){
        createSingleSection(0, "Contacts", "contacts", "la-address-book");
        createSingleSection(1, "Messages", "messages", "la-comments");
        createSingleSection(3, "Available Apps", "available_apps-view", "la-list");
        createSingleSection(4, "Apps", "apps-view", "la-list");
        createSingleSection(5, "Privacy Notice", "privacyNotice", "la-file");
        if(authenticatedUser.getUser().getRole().equals(Role.CONTROLLER) || authenticatedUser.getUser().getRole().equals(Role.DPO)){
            createSingleSection(2, "Rights", "rights_controller", "la-school");
            createSingleSection(6, "Questionnaire", "questionnaire","la-archive");
        }
        else{
            createSingleSection(2, "Rights", "rights", "la-school");
            titles[6].setText("Pending Requests");
            layouts[6].addClickListener((e->{
                communicationService.setOpenPendingRequests(true);
                UI.getCurrent().navigate("rights");
            }));
            icons[6].addClassName("la-archive");
        }

    }

    private void createSingleSection(int sectionNumber, String title, String navigationPage, String className){
        titles[sectionNumber].setText(title);
        layouts[sectionNumber].addClickListener(e-> UI.getCurrent().navigate(navigationPage));
        icons[sectionNumber].addClassName(className);
    }

}
