package com.privacydashboard.application.views.privacyNotice;

import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.views.usefulComponents.MyDialog;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class FormPrivacyNotice extends VerticalLayout {
    private final PrivacyNotice privacyNotice;
    private  final DataBaseService dataBaseService;
    private final String ws= "&nbsp;&nbsp;&nbsp;";  // three white spaces to use while doing a list
    private final Integer nQuestions= 14;
    private final Span[] mainText= new Span[nQuestions];
    private final Html[] example= new Html[nQuestions];
    private final TextArea[] textAreas= new TextArea[nQuestions];
    private final Button saveButton= new Button("Save", e-> savePrivacyNotice());

    public FormPrivacyNotice(PrivacyNotice privacyNotice, DataBaseService dataBaseService){
        this.privacyNotice= privacyNotice;
        this.dataBaseService= dataBaseService;
        addClassName("privacy_notice-view");

        mainText[0]= new Span("What data do we collect?");
        example[0]= new Html("<p>Example:<i> We collect personal identification information such as name, phone number, mail...<i/></p>");

        mainText[1]= new Span("How do we collect the data?");
        example[1]= new Html("<p>Example: <i>We collect and process the data when you <br/>" +
                ws + "• register online or place an order for any of our products or services <br/>" +
                ws + "• use or view our website via your browser's cookies <br/>" +
                ws + "• ...</i>" +
                "</p>");

        mainText[2]= new Span("How will we use the data?");
        example[2]= new Html("<p>Example: <i>We collect your data so that we can <br/>" +
                ws + "• process your order, manage your account <br/>" +
                ws + "• email you with special offers on other products or services you might like <br/>" +
                ws + "• ... <br/>" +
                "If you agree we will share your data with our partner companies companies so that they may offer you their products and services <br/>" +
                ws + "• list of all the companies <br/>" +
                "When we process your order, we may send your data to, and also use the resulting information from, credit reference agencies to prevent fraudulent purchases.</i>" +
                "</p>");

        mainText[3]= new Span("How do we store your data?");
        example[3]= new Html("<p>Example:<i> We securely stores your data at [enter the location and describe security precautions taken]. <br/>" +
                "We will keep your [enter type of data] for [enter time period]. Once this time period has expired, we will delete your data by [enter how you delete users’ data].</i>" +
                "</p>");

        mainText[4]= new Span("Marketing");
        example[4]= new Html("<p>Example:<i> Company would like to send you information about products and services of ours that we think you might like, as well as those of our partner companies.<br/>" +
                ws + "• list of partner companies <br/>" +
                "If you have agreed to receive marketing, you may always opt out at a later date.<br/>" +
                "You have the right at any time to stop us from contacting you for marketing purposes or giving your data to other members of our partner companies.</i>" +
                "</p>");

        mainText[5]= new Span("What are the user data protection rights?");
        example[5]= new Html("<p>Example:<i> We would like to make sure you are fully aware of all of your data protection rights. Every user is entitled to the following: <br/>" +
                ws + "• The right to access - You have the right to request for copies of your personal data. We may charge you a small fee for this service.<br/>" +
                ws + "• The right to rectification - You have the right to request that we correct any information you believe is inaccurate. You also have the right to request us to complete information you believe is incomplete.<br/>" +
                ws + "• The right to erasure — You have the right to request that Our Company erase your personal data, under certain conditions.<br/>" +
                ws + "• The right to restrict processing - You have the right to request that Our Company restrict the processing of your personal data, under certain conditions.<br/>" +
                ws + "• The right to object to processing - You have the right to object to Our Company’s processing of your personal data, under certain conditions.<br/>" +
                ws + "• The right to data portability - You have the right to request that Our Company transfer the data that we have collected to another organization, or directly to you, under certain conditions.<br/>" +
                "If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us at our email:<br/>" +
                "Call us at: <br/>" +
                "Or write to us: </i>" +
                "</p>");

        mainText[6]= new Span("What are cookies?");
        example[6]= new Html("<p>Example:<i> Cookies are text files placed on your computer to collect standard Internet log information and visitor behavior information. When you visit our websites, we may collect information from you automatically through cookies or similar technology.</i> " +
                "</p>");

        mainText[7]= new Span("How do we use cookies?");
        example[7]= new Html("<p>Example:<i> We use cookies in a range of ways to improve your experience on our website, including: <br/>" +
                ws + "• Keeping you signed in <br/>" +
                ws + "• Understanding how you use our website <br/>" +
                ws + "• ...</i>" +
                "</p>");

        mainText[8]= new Span("What types of cookies do we use?");
        example[8]= new Html("<p>Example: <i>There are a number of different types of cookies, however, we use: <br/>" +
                ws + "• Functionality — We use these cookies so that we recognize you on our website and remember your previously selected preferences. These could include what language you prefer and location you are in. A mix of first-party and third-party cookies are used. <br/>" +
                ws + "• Advertising — We use these cookies to collect information about your visit to our website, the content you viewed, the links you followed and information about your browser, device, and your IP address. We sometimes share some limited aspects of this data with third parties for advertising purposes. We may also share online data collected through cookies with our advertising partners. This means that when you visit another website, you may be shown advertising based on your browsing patterns on our website.<br/>" +
                ws + "• ...</i>" +
                "</p>");

        mainText[9]= new Span("How to manage cookies");
        example[9]= new Html("<p>Example:<i> You can set your browser not to accept cookies, and the above website tells you how to remove cookies from your browser. However, in a few cases, some of our website features may not function as a result.</i> " +
                "</p>");

        mainText[10]= new Span("Privacy policies of other websites");
        example[10]= new Html("<p>Example:<i> Our website contains links to other websites. Our privacy policy applies only to our website, so if you click on a link to another website, you should read their privacy policy. </i>" +
                "</p>");

        mainText[11]= new Span("Changes to our privacy policy");
        example[11]= new Html("<p>Example:<i>We keep our privacy policy under regular review and places any updates on this web page. This privacy policy was last updated on [insert date] </i>" +
                "</p>");

        mainText[12]= new Span("How to contact us");
        example[12]= new Html("<p>Example:<i> If you have any questions about our privacy policy, the data we hold on you, or you would like to exercise one of your data protection rights, please do not hesitate to contact us. <br>" +
                ws + "Email us at: <br>" +
                ws + "Call us: <br>" +
                ws + "Or write us at: </i>" +
                "</p>");

        mainText[13]= new Span("How to contact the appropriate authority");
        example[13]= new Html("<p>Example:<i> Should you wish to report a complaint or if you feel that we have not addressed your concern in a satisfactory manner, you may contact the Information Commissioner’s Office.<br/>" +
                ws + "Email: <br/>" +
                ws + "Address: </i>" +
                "</p>");

        for(int i=0; i<nQuestions; i++){
            mainText[i].addClassName("mainText");
            textAreas[i]= new TextArea();
            textAreas[i].addClassName("textArea");
            add(mainText[i], example[i], textAreas[i]);
        }
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(saveButton);
    }

    private void savePrivacyNotice() {
        MyDialog dialog=new MyDialog();
        String titleText;
        String text=convertText();
        Button saveButton= new Button("Confirm");

        if (privacyNotice.getText()==null) {
            titleText= "Do you want to upload this Privacy Notice for the app: " + privacyNotice.getApp().getName() + "?";
            saveButton.addClickListener(e->{
                dataBaseService.addPrivacyNoticeForApp(privacyNotice.getApp(), text);
                Notification notification = Notification.show("Privacy Notice uploaded correctly");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate(PrivacyNoticeView.class);
                dialog.close();
            });
        }
        else {
            titleText= "Do you want to overwrite this Privacy Notice for the app: " + privacyNotice.getApp().getName() + "?";
            saveButton.addClickListener(e->{
                dataBaseService.changePrivacyNoticeForApp(privacyNotice.getApp(), text);
                Notification notification = Notification.show("Privacy Notice overwritten correctly");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate(PrivacyNoticeView.class);
                dialog.close();
            });
        }

        dialog.setTitle(titleText);
        TextArea textArea= new TextArea();
        textArea.setValue(text);
        textArea.setWidthFull();
        textArea.setReadOnly(true);
        dialog.setContent(new HorizontalLayout(textArea));
        dialog.setContinueButton(saveButton);
        dialog.open();
    }

    /*
    DA CAPIRE COME VISUALIZZARE IL TESTO.
    PER ORA LO SALVO COME HTML MA IN CASO SI VOGLIA CARICARE DIRETTAMENTE IL FILE BISOGNA CAPIRE COME FARE
     */
    private String convertText(){
        /*String privacyNoticeText = "<p>";
        for (int i = 0; i < nQuestions; i++) {
            privacyNoticeText += "<b>" + mainText[i].getText() + "</b><br/>" + textAreas[i].getValue() + "<br/><br/>";
        }
        return privacyNoticeText + "</p>";*/

        String privacyNoticeText = "";
        for (int i = 0; i < nQuestions; i++) {
            privacyNoticeText += mainText[i].getText() + "\n" + textAreas[i].getValue() + "\n";
        }
        return privacyNoticeText;
    }

    /*
    DA CAPIRE COME VISUALIZZARE IL TESTO.
    PER ORA LO SALVO COME HTML MA IN CASO SI VOGLIA CARICARE DIRETTAMENTE IL FILE BISOGNA CAPIRE COME FARE
     */
    private Html visualizeText(String text){
        return new Html(text);
    }
}
