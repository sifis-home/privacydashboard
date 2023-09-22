package com.privacydashboard.application.views.privacyNotice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import com.privacydashboard.application.data.service.DataBaseService;
import com.vaadin.flow.component.Html;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.PrivacyNotice;

public class FormPrivacyNoticeTest {
    
    private DataBaseService dataBaseService;    
    private FormPrivacyNotice form;

    private final String ws= "&nbsp;&nbsp;&nbsp;";

    private IoTApp createApp(){
        IoTApp app = new IoTApp();
        app.setName("TestApp");
        app.setDescription("TestDescription");
        app.setId(new UUID(1, 0));
        app.setQuestionnaireVote(QuestionnaireVote.GREEN);

        return app;
    }

    private PrivacyNotice createNotice(){
        PrivacyNotice notice = new PrivacyNotice();
        notice.setId(new UUID(0, 0));
        notice.setText("TestNotice");
        notice.setApp(createApp());

        return notice;
    }

    @Test
    public void formPrivacyNoticeConstructorTest(){
        dataBaseService = mock(DataBaseService.class);
        
        form = new FormPrivacyNotice(createNotice(), dataBaseService);

        System.out.println(form.getElement()+"\n");
        System.out.println(form.getElement().getChild(1).getOuterHTML());

        assertEquals(form.getElement().getTag(), "vaadin-vertical-layout");
        assertEquals(form.getElement().getAttribute("class"), "privacy_notice-view");

        assertEquals(form.getElement().getChild(0).getTag(), "span");
        assertEquals(form.getElement().getChild(0).getText(), "What data do we collect?");
        assertEquals(form.getElement().getChild(0).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(1).getOuterHTML(), "<p>Example:<i> We collect personal identification information such as name, phone number, mail...<i></i></i></p>");
    
        assertEquals(form.getElement().getChild(2).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(2).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(3).getTag(), "span");
        assertEquals(form.getElement().getChild(3).getText(), "How do we collect the data?");
        assertEquals(form.getElement().getChild(3).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(4).getOuterHTML(), "<p>Example: <i>We collect and process the data when you <br>" +
        ws + "• register online or place an order for any of our products or services <br>" +
        ws + "• use or view our website via your browser's cookies <br>" +
        ws + "• ...</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(5).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(5).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(6).getTag(), "span");
        assertEquals(form.getElement().getChild(6).getText(), "How will we use the data?");
        assertEquals(form.getElement().getChild(6).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(7).getOuterHTML(), "<p>Example: <i>We collect your data so that we can <br>" +
        ws + "• process your order, manage your account <br>" +
        ws + "• email you with special offers on other products or services you might like <br>" +
        ws + "• ... <br>" +
        "If you agree we will share your data with our partner companies companies so that they may offer you their products and services <br>" +
        ws + "• list of all the companies <br>" +
        "When we process your order, we may send your data to, and also use the resulting information from, credit reference agencies to prevent fraudulent purchases.</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(8).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(8).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(9).getTag(), "span");
        assertEquals(form.getElement().getChild(9).getText(), "How do we store your data?");
        assertEquals(form.getElement().getChild(9).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(10).getOuterHTML(), "<p>Example:<i> We securely stores your data at [enter the location and describe security precautions taken]. <br>" +
        "We will keep your [enter type of data] for [enter time period]. Once this time period has expired, we will delete your data by [enter how you delete users’ data].</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(11).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(11).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(12).getTag(), "span");
        assertEquals(form.getElement().getChild(12).getText(), "Marketing");
        assertEquals(form.getElement().getChild(12).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(13).getOuterHTML(), "<p>Example:<i> Company would like to send you information about products and services of ours that we think you might like, as well as those of our partner companies.<br>" +
        ws + "• list of partner companies <br>" +
        "If you have agreed to receive marketing, you may always opt out at a later date.<br>" +
        "You have the right at any time to stop us from contacting you for marketing purposes or giving your data to other members of our partner companies.</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(14).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(14).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(15).getTag(), "span");
        assertEquals(form.getElement().getChild(15).getText(), "What are the user data protection rights?");
        assertEquals(form.getElement().getChild(15).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(16).getOuterHTML(), "<p>Example:<i> We would like to make sure you are fully aware of all of your data protection rights. Every user is entitled to the following: <br>" +
        ws + "• The right to access - You have the right to request for copies of your personal data. We may charge you a small fee for this service.<br>" +
        ws + "• The right to rectification - You have the right to request that we correct any information you believe is inaccurate. You also have the right to request us to complete information you believe is incomplete.<br>" +
        ws + "• The right to erasure — You have the right to request that Our Company erase your personal data, under certain conditions.<br>" +
        ws + "• The right to restrict processing - You have the right to request that Our Company restrict the processing of your personal data, under certain conditions.<br>" +
        ws + "• The right to object to processing - You have the right to object to Our Company’s processing of your personal data, under certain conditions.<br>" +
        ws + "• The right to data portability - You have the right to request that Our Company transfer the data that we have collected to another organization, or directly to you, under certain conditions.<br>" +
        "If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us at our email:<br>" +
        "Call us at: <br>" +
        "Or write to us: </i>" +
        "</p>");

        assertEquals(form.getElement().getChild(17).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(17).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(18).getTag(), "span");
        assertEquals(form.getElement().getChild(18).getText(), "What are cookies?");
        assertEquals(form.getElement().getChild(18).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(19).getOuterHTML(), "<p>Example:<i> Cookies are text files placed on your computer to collect standard Internet log information and visitor behavior information. When you visit our websites, we may collect information from you automatically through cookies or similar technology.</i> " +
        "</p>");

        assertEquals(form.getElement().getChild(20).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(20).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(21).getTag(), "span");
        assertEquals(form.getElement().getChild(21).getText(), "How do we use cookies?");
        assertEquals(form.getElement().getChild(21).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(22).getOuterHTML(), "<p>Example:<i> We use cookies in a range of ways to improve your experience on our website, including: <br>" +
        ws + "• Keeping you signed in <br>" +
        ws + "• Understanding how you use our website <br>" +
        ws + "• ...</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(23).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(23).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(24).getTag(), "span");
        assertEquals(form.getElement().getChild(24).getText(), "What types of cookies do we use?");
        assertEquals(form.getElement().getChild(24).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(25).getOuterHTML(), "<p>Example: <i>There are a number of different types of cookies, however, we use: <br>" +
        ws + "• Functionality — We use these cookies so that we recognize you on our website and remember your previously selected preferences. These could include what language you prefer and location you are in. A mix of first-party and third-party cookies are used. <br>" +
        ws + "• Advertising — We use these cookies to collect information about your visit to our website, the content you viewed, the links you followed and information about your browser, device, and your IP address. We sometimes share some limited aspects of this data with third parties for advertising purposes. We may also share online data collected through cookies with our advertising partners. This means that when you visit another website, you may be shown advertising based on your browsing patterns on our website.<br>" +
        ws + "• ...</i>" +
        "</p>");

        assertEquals(form.getElement().getChild(26).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(26).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(27).getTag(), "span");
        assertEquals(form.getElement().getChild(27).getText(), "How to manage cookies");
        assertEquals(form.getElement().getChild(27).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(28).getOuterHTML(), "<p>Example:<i> You can set your browser not to accept cookies, and the above website tells you how to remove cookies from your browser. However, in a few cases, some of our website features may not function as a result.</i> " +
        "</p>");

        assertEquals(form.getElement().getChild(29).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(29).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(30).getTag(), "span");
        assertEquals(form.getElement().getChild(30).getText(), "Privacy policies of other websites");
        assertEquals(form.getElement().getChild(30).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(31).getOuterHTML(), "<p>Example:<i> Our website contains links to other websites. Our privacy policy applies only to our website, so if you click on a link to another website, you should read their privacy policy. </i>" +
        "</p>");

        assertEquals(form.getElement().getChild(32).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(32).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(33).getTag(), "span");
        assertEquals(form.getElement().getChild(33).getText(), "Changes to our privacy policy");
        assertEquals(form.getElement().getChild(33).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(34).getOuterHTML(), "<p>Example:<i>We keep our privacy policy under regular review and places any updates on this web page. This privacy policy was last updated on [insert date] </i>" +
        "</p>");

        assertEquals(form.getElement().getChild(35).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(35).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(36).getTag(), "span");
        assertEquals(form.getElement().getChild(36).getText(), "How to contact us");
        assertEquals(form.getElement().getChild(36).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(37).getOuterHTML(), "<p>Example:<i> If you have any questions about our privacy policy, the data we hold on you, or you would like to exercise one of your data protection rights, please do not hesitate to contact us. <br>" +
        ws + "Email us at: <br>" +
        ws + "Call us: <br>" +
        ws + "Or write us at: </i>" +
        "</p>");

        assertEquals(form.getElement().getChild(38).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(38).getAttribute("class"), "textArea");

        assertEquals(form.getElement().getChild(39).getTag(), "span");
        assertEquals(form.getElement().getChild(39).getText(), "How to contact the appropriate authority");
        assertEquals(form.getElement().getChild(39).getAttribute("class"), "mainText");

        assertEquals(form.getElement().getChild(40).getOuterHTML(), "<p>Example:<i> Should you wish to report a complaint or if you feel that we have not addressed your concern in a satisfactory manner, you may contact the Information Commissioner’s Office.<br>" +
        ws + "Email: <br>" +
        ws + "Address: </i>" +
        "</p>");

        assertEquals(form.getElement().getChild(41).getTag(), "vaadin-text-area");
        assertEquals(form.getElement().getChild(41).getAttribute("class"), "textArea");
    }
}
