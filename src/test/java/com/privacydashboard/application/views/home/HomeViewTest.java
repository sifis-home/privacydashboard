package com.privacydashboard.application.views.home;

import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.views.home.HomeView;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

public class HomeViewTest {

    private static AuthenticatedUser authenticatedUser;
    private static CommunicationService communicationService;
    private static HomeView subjectView;
    private static HomeView notSubjectView;

    private static User createTestUser(Role role){
        User user = new User();

        user.setName("UserName");
        user.setId(new UUID(0, 0));
        user.setProfilePictureUrl("https://testurl.html");
        user.setHashedPassword("HashedPassword");
        user.setRole(role);
        user.setMail("test@mail.test");

        return user;
    }

    @BeforeAll
    private static void setup(){
        authenticatedUser = mock(AuthenticatedUser.class);
        communicationService = mock(CommunicationService.class);
        User subject = createTestUser(Role.SUBJECT);
        User notASubject = createTestUser(Role.DPO);
        when(authenticatedUser.getUser()).thenReturn(subject);
    
        subjectView = new HomeView(authenticatedUser, communicationService);

        when(authenticatedUser.getUser()).thenReturn(notASubject);
        notSubjectView = new HomeView(authenticatedUser, communicationService);
    }

    @Test
    public void HomeViewSubjectConstructorTest(){
        assertEquals(subjectView.getElement().getAttribute("class"), "home-view");

        assertEquals(subjectView.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(subjectView.getElement().getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getChild(0).getText(), "Contacts");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(0).getChild(0).getChild(1).getAttribute("class").contains("las la-10x icons la-address-book"), true);

        assertEquals(subjectView.getElement().getChild(0).getChild(1).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getChild(0).getText(), "Messages");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(0).getChild(1).getChild(1).getAttribute("class").contains("las la-10x icons la-comments"), true);

        assertEquals(subjectView.getElement().getChild(0).getChild(2).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getChild(0).getText(), "Rights");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(0).getChild(2).getChild(1).getAttribute("class").contains("las la-10x icons la-school"), true);

        assertEquals(subjectView.getElement().getChild(1).getTag(), "vaadin-horizontal-layout");

        assertEquals(subjectView.getElement().getChild(1).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getChild(0).getText(), "Apps");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(1).getChild(0).getChild(1).getAttribute("class").contains("las la-10x icons la-list"), true);

        assertEquals(subjectView.getElement().getChild(1).getChild(1).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getChild(0).getText(), "Privacy Notice");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(1).getChild(1).getChild(1).getAttribute("class").contains("las la-10x icons la-file"), true);

        assertEquals(subjectView.getElement().getChild(1).getChild(2).getTag(), "vaadin-vertical-layout");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getAttribute("class"), "section pointer");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getChild(0).getTag(), "span");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getChild(0).getAttribute("class"), "title");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getChild(0).getText(), "Pending Requests");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getChild(1).getTag(), "div");
        assertEquals(subjectView.getElement().getChild(1).getChild(2).getChild(1).getAttribute("class").contains("las la-10x icons la-archive"), true);
    }

    @Test
    public void HomeViewNotSubjectConstructorTest(){
        assertEquals(notSubjectView.getElement().getAttribute("class"), "home-view");

        assertEquals(notSubjectView.getElement().getChild(0).getTag(), "vaadin-horizontal-layout");

        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getChild(0).getText(), "Contacts");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(0).getChild(1).getAttribute("class").contains("las la-10x icons la-address-book"), true);

        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getChild(0).getText(), "Messages");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(1).getChild(1).getAttribute("class").contains("las la-10x icons la-comments"), true);

        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getChild(0).getText(), "Rights");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(0).getChild(2).getChild(1).getAttribute("class").contains("las la-10x icons la-school"), true);

        assertEquals(notSubjectView.getElement().getChild(1).getTag(), "vaadin-horizontal-layout");

        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getChild(0).getText(), "Apps");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(0).getChild(1).getAttribute("class").contains("las la-10x icons la-list"), true);

        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getChild(0).getText(), "Privacy Notice");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(1).getChild(1).getAttribute("class").contains("las la-10x icons la-file"), true);

        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getTag(), "vaadin-vertical-layout");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getAttribute("class"), "section pointer");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getChild(0).getTag(), "span");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getChild(0).getAttribute("class"), "title");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getChild(0).getText(), "Questionnaire");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getChild(1).getTag(), "div");
        assertEquals(notSubjectView.getElement().getChild(1).getChild(2).getChild(1).getAttribute("class").contains("las la-10x icons la-archive"), true);
    }
}
