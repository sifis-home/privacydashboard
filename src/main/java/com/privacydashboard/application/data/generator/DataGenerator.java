package com.privacydashboard.application.data.generator;

import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.*;
import com.privacydashboard.application.data.service.IoTAppRepository;
import com.privacydashboard.application.data.service.MessageRepository;
import com.privacydashboard.application.data.service.NotificationRepository;
import com.privacydashboard.application.data.service.PrivacyNoticeRepository;
import com.privacydashboard.application.data.service.RightRequestRepository;
import com.privacydashboard.application.data.service.UserAppRelationRepository;
import com.privacydashboard.application.data.service.UserRepository;
import com.privacydashboard.application.security.*;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository, IoTAppRepository ioTAppRepository, UserAppRelationRepository userAppRelationRepository, MessageRepository messageRepository, RightRequestRepository rightRequestRepository, PrivacyNoticeRepository privacyNoticeRepository, NotificationRepository notificationRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            // APPS
            String[] greenAnswers= new String[]{"Yes", "Yes", "less than 1 month", "Yes", "No", "", "They're located only in United Europe", "No", "Yes",
                                                "I encrypt them", "SHA-3", "Yes", "Yes", "Yes", "Yes", "Yes", "TLS 1.2 or 1.3", "Yes", "every week", "Yes",
                                                "Yes, OpenChain", "Yes", "Yes", "Yes", "Yes", "No", "", "Yes", "Yes, >=90%", "Yes"};
            String[] orangeAnswers= new String[]{"Yes", "Yes", "between 1 month and 6 months", "Yes", "No", "", "They're located only in United Europe", "Yes", "Yes",
                                                "I encrypt them", "SHA-3", "Yes", "Yes", "Yes", "Yes", "Yes", "TLS 1.2 or 1.3", "Yes", "between a week and a month", "Yes",
                                                "No", "Yes", "Yes", "Yes", "No", "No", "", "Yes", "Yes, >=90%", "Yes"};
            String[] redAnswers= new String[]{"Yes", "Yes", "less than 1 month", "No", "No", "", "They're located only in United Europe", "Yes", "Yes",
                                                "Plain text", "", "No", "Yes", "Yes", "Yes", "Yes", "TLS 1.2 or 1.3", "Yes", "never", "Yes",
                                                "No", "No", "", "No", "No", "Yes", "I don't know", "Yes", "Yes, >=90%", "Yes"};
            IoTApp[] apps= new IoTApp[10];
            int i=0;
            apps[i]= new IoTApp();
            apps[i].setName("Thermostat");
            apps[i].setDescription("Efficiently regulates the temperature of the room using its sensors to perceive if there are people inside");
            apps[i].setQuestionnaireVote(QuestionnaireVote.GREEN);
            apps[i].setDetailVote(greenAnswers);
            //apps[i].setOptionalAnswers();
            i++;


            apps[i]= new IoTApp();
            apps[i].setName("Door Opener");
            apps[i].setDescription("Automatically opens and closes the door when a person is near");
            apps[i].setQuestionnaireVote(QuestionnaireVote.ORANGE);
            apps[i].setDetailVote(orangeAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Smart TV");
            apps[i].setDescription("TV connected with internet");
            apps[i].setQuestionnaireVote(QuestionnaireVote.RED);
            apps[i].setDetailVote(redAnswers);
            //apps[i].setOptionalAnswers();
            i++;


            apps[i]= new IoTApp();
            apps[i].setName("Air Purifier");
            apps[i].setDescription("Automatically analyzes and purifies the air in the room");
            apps[i].setQuestionnaireVote(QuestionnaireVote.ORANGE);
            apps[i].setDetailVote(orangeAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Security Cameras");
            apps[i].setDescription("Outdoor cameras connected with WIFI");
            apps[i].setQuestionnaireVote(QuestionnaireVote.RED);
            apps[i].setDetailVote(redAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Video Doorbell");
            apps[i].setDescription("Allows to let you see who's there without moving from the couch");
            apps[i].setQuestionnaireVote(QuestionnaireVote.GREEN);
            apps[i].setDetailVote(greenAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Voice Assistant");
            apps[i].setDescription("Takes care of all your needs");
            apps[i].setQuestionnaireVote(QuestionnaireVote.RED);
            apps[i].setDetailVote(redAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Smart Lights Bulbs");
            apps[i].setDescription("Vocally controlled light bulbs");
            apps[i].setQuestionnaireVote(QuestionnaireVote.GREEN);
            apps[i].setDetailVote(greenAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Windows Opener");
            apps[i].setDescription("Opens and closes the windows in the house based on the hour and on the outside climatic conditions");
            apps[i].setQuestionnaireVote(QuestionnaireVote.ORANGE);
            apps[i].setDetailVote(orangeAnswers);
            //apps[i].setOptionalAnswers();
            i++;

            apps[i]= new IoTApp();
            apps[i].setName("Smart Fridge");
            apps[i].setDescription("WiFI connected fridge with recipe database integrated");
            apps[i].setQuestionnaireVote(QuestionnaireVote.GREEN);
            apps[i].setDetailVote(greenAnswers);
            //apps[i].setOptionalAnswers();

            for(int m=0; m<10; m++){
                if(m==2 || m==6){
                    apps[m].setConsenses(new String[]{"processing of personal information to provide the service", "processing of personal information to personalize the content"});
                }
                else{
                    apps[m].setConsenses(new String[]{"processing of personal information to provide the service"});
                }
                logger.info(apps[m].getName());
                ioTAppRepository.save(apps[m]);
            }

            // MAIN USERS
            User subject, controller, dpo;
            subject= new User();
            subject.setRole(Role.SUBJECT);
            subject.setName("UserSubject");
            subject.setHashedPassword(passwordEncoder.encode("UserSubject"));
            subject.setMail("userSubject@protonmail.com");
            userRepository.save(subject);

            controller= new User();
            controller.setRole(Role.CONTROLLER);
            controller.setName("UserController");
            controller.setHashedPassword(passwordEncoder.encode("UserController"));
            controller.setMail("userController@protonmail.com");
            userRepository.save(controller);

            dpo= new User();
            dpo.setRole(Role.DPO);
            dpo.setName("UserDPO");
            dpo.setHashedPassword(passwordEncoder.encode("UserDPO"));
            dpo.setMail("userDPO@protonmail.com");
            userRepository.save(dpo);

            // UserAppRelation MAIN
            UserAppRelation[] userAppRelationSubject= new UserAppRelation[10];
            UserAppRelation[] userAppRelationController= new UserAppRelation[10];
            UserAppRelation[] userAppRelationDPO= new UserAppRelation[10];
            for(int j=0; j<10; j++){
                userAppRelationSubject[j]=new UserAppRelation();
                userAppRelationSubject[j].setUser(subject);
                userAppRelationSubject[j].setApp(apps[j]);
                if(j==2 || j==6){
                    userAppRelationSubject[j].setConsenses(new String[]{"processing of personal information to provide the service", "processing of personal information to personalize the content"});
                }
                else{
                    userAppRelationSubject[j].setConsenses(new String[]{"processing of personal information to provide the service"});
                }
                userAppRelationRepository.save(userAppRelationSubject[j]);

                userAppRelationController[j]=new UserAppRelation();
                userAppRelationController[j].setUser(controller);
                userAppRelationController[j].setApp(apps[j]);
                if(j==2 || j==6){
                    userAppRelationController[j].setConsenses(new String[]{"processing of personal information to provide the service", "processing of personal information to personalize the content"});
                }
                else{
                    userAppRelationController[j].setConsenses(new String[]{"processing of personal information to provide the service"});
                }
                userAppRelationRepository.save(userAppRelationController[j]);

                userAppRelationDPO[j]=new UserAppRelation();
                userAppRelationDPO[j].setUser(dpo);
                userAppRelationDPO[j].setApp(apps[j]);
                if(j==2 || j==6){
                    userAppRelationDPO[j].setConsenses(new String[]{"processing of personal information to provide the service", "processing of personal information to personalize the content"});
                }
                else{
                    userAppRelationDPO[j].setConsenses(new String[]{"processing of personal information to provide the service"});
                }
                userAppRelationRepository.save(userAppRelationDPO[j]);
            }

            // SUBJECTS
            User[] subjects= new User[30];
            String[] names= new String[]{"Luca Sandini", "Matt Brown", "Andrea Rizzato", "Alivia Wallace", "Lucinda Singleton", "Rehan Small", "Gilbert Melton", "Estelle Yates", "Jade Alvarado", "Tobias Bishop",
                                        "Enzo Pessegatto", "Lucia Pianezzola", "Lino Peruzzo", "Margherita Dimasi", "Rafael Lloyd", "Stephen Moran", "Dominik Gardner", "Natalie Ingram", "Leia Erickson", "Anisa Hutchinson",
                                        "Matteo Matei", "Daniele De Santi", "Victor Moran", "Todd Lyin", "Nicola Pozzato", "Benedetta Zenere", "Giorgia Gissi", "Nate Shelton", "Sofia Holman", "Teresa Silva"};

            for(int j=0; j<30; j++){
                subjects[j]=new User();
                subjects[j].setRole(Role.SUBJECT);
                subjects[j].setName(names[j]);
                subjects[j].setHashedPassword(passwordEncoder.encode(names[j]));
                subjects[j].setMail(names[j].replaceAll("\\s","") + "@protonmail.com");
                userRepository.save(subjects[j]);

                for(int k=j-2;k<j+2;k++){
                    if(k<0){
                        k*=-1;
                    }
                    int m=k%10;
                    UserAppRelation userAppRelation= new UserAppRelation();
                    userAppRelation.setApp(apps[m]);
                    userAppRelation.setUser(subjects[j]);
                    if(m==2 || m==6){
                        userAppRelation.setConsenses(new String[]{"processing of personal information to provide the service", "processing of personal information to personalize the content"});
                    }
                    else{
                        userAppRelation.setConsenses(new String[]{"processing of personal information to provide the service"});
                    }
                    userAppRelationRepository.save(userAppRelation);
                }
            }

            // CONTROLLERS
            User[] controllers= new User[5];
            String[] names2= new String[]{"Yuvraj Allison", "Vincent Houston", "Yousef Horne", "Wayne Cohen", "Xanthe Blackburn"};

            for(int j=0; j<5; j++){
                controllers[j]=new User();
                controllers[j].setRole(Role.CONTROLLER);
                controllers[j].setName(names2[j]);
                controllers[j].setHashedPassword(names2[j]);
                controllers[j].setMail(names2[j].replaceAll("\\s","") + "@protonmail.com");
                userRepository.save(controllers[j]);

                UserAppRelation userAppRelation= new UserAppRelation();
                userAppRelation.setUser(controllers[j]);
                userAppRelation.setApp(apps[j]);
                userAppRelationRepository.save(userAppRelation);
            }

            // DPOs
            User[] dpos= new User[5];
            String[] names3= new String[]{"Xander Mcconnell", "Virginia Santini", "Zaira Romani", "Walter Santi", "Walter Treschi"};

            for(int j=0; j<5; j++){
                dpos[j]=new User();
                dpos[j].setRole(Role.DPO);
                dpos[j].setName(names3[j]);
                dpos[j].setHashedPassword(names3[j]);
                dpos[j].setMail(names3[j].replaceAll("\\s","") + "@protonmail.com");
                userRepository.save(dpos[j]);

                UserAppRelation userAppRelation= new UserAppRelation();
                userAppRelation.setUser(dpos[j]);
                userAppRelation.setApp(apps[j+5]);
                userAppRelationRepository.save(userAppRelation);
            }

            // RIGHTS
            for(int j=0; j<6; j++){
                RightRequest request= new RightRequest();
                request.setReceiver(controller);
                request.setSender(subjects[j]);
                request.setApp(apps[j]);
                request.setHandled(j%2!=0);
                request.setTime(LocalDateTime.now());
                if(j==0){
                    request.setSender(subject);
                    request.setRightType(RightType.PORTABILITY);
                    request.setDetails("Could you send me by my mail?");
                }
                else if(j==1){
                    request.setSender(subject);
                    request.setRightType(RightType.DELTEEVERYTHING);
                    request.setDetails("I would like ALL my data to be deleted");
                }
                else if(j==2){
                    request.setRightType(RightType.WITHDRAWCONSENT);
                    request.setOther("processing of personal information to personalize the content");
                }
                else if(j==3){
                    request.setRightType(RightType.INFO);
                    request.setOther("The period for which the personal data will be stored");
                    request.setResponse("For 1 year");
                }
                else if(j==4){
                    request.setRightType(RightType.ERASURE);
                    request.setOther("The video made on August 19 2022");
                }
                else if(j==5){
                    request.setRightType(RightType.PORTABILITY);
                    request.setDetails("As soon as possible, thanks");
                }
                rightRequestRepository.save(request);
            }

            for(int j=0; j<4; j++){
                RightRequest request= new RightRequest();
                request.setReceiver(controllers[j]);
                request.setSender(subject);
                request.setApp(apps[j]);
                request.setHandled(j%2!=0);
                request.setTime(LocalDateTime.now());
                if(j==0){
                    request.setRightType(RightType.DELTEEVERYTHING);
                }
                else if(j==1){
                    request.setRightType(RightType.PORTABILITY);
                }
                else if(j==2){
                    request.setRightType(RightType.WITHDRAWCONSENT);
                    request.setOther("processing of personal information to personalize the content");
                }
                else if(j==3){
                    request.setRightType(RightType.INFO);
                    request.setOther("The contact details of the data protection officer");
                    request.setResponse("his mail is: carlocarli@protonmail.com");
                }
                rightRequestRepository.save(request);
            }


            // PRIVACY NOTICE
            for(int j=0; j<10; j++){
                PrivacyNotice privacyNotice= new PrivacyNotice();
                privacyNotice.setApp(apps[j]);
                if(j==0){
                    privacyNotice.setText("Information Collected: We collect data about your heating and cooling preferences, temperature settings, and usage patterns to provide you our services.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the thermostat, as well as to offer personalized features and services.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the National Authority.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. \n" +
                            "\n" +
                            "Your Data will be stored for 6 months. ");
                }
                if(j==1){
                    privacyNotice.setText("Information Collected: We collect information about your use of the door opener, including the dates and times when the door is opened and closed.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the door opener\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                if(j==2){
                    privacyNotice.setText("Information Collected: We collect information about your TV usage, such as the channels you watch, the programs you record, and the on-demand content you access.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to personalize your viewing experience, to improve the performance and functionality of the TV and, with your consent, to provide you personalized content.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com.");
                }
                if(j==3){
                    privacyNotice.setText("Information Collected: We collect information about your air purifier usage, such as the dates and times when it is turned on and off, the history of the air quality in your room and the settings you use.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the air purifier, and to offer personalized features and services.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                if(j==4){
                    privacyNotice.setText("Information Collected: We collect video and audio recordings captured by your security cameras, as well as information about when and how the cameras are used.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to provide the security features and services, and to improve their performance.\n" +
                            "\n" +
                            "Sharing Information: We may share your information with an online service provider to allow remote access to your cameras. This provider is subject to strict confidentiality and security obligations.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                if(j==5){
                    privacyNotice.setText("Information Collected: We collect video and audio recordings captured by your doorbell, as well as information about when and how the doorbell is used. \n" +
                            "\n" +
                            "Use of Information: The information collected is used to provide the doorbell's features and services, and to improve their performance.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. \n");
                }
                if(j==6){
                    privacyNotice.setText("Information Collected: We collect information about your use of the voice assistant, including voice commands and queries, and any related audio recordings.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to provide the voice assistant features and services, and to improve their performance, and, with your consent, to provide you personalized content\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                if(j==7){
                    privacyNotice.setText("Information Collected: We collect information about your use of the smart light bulbs, including the dates and times when they are turned on and off, the settings you use, and the time of day that is needed to provide the automatic on and off switching feature.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the smart light bulbs, and to offer personalized features and services.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                if(j==8){
                    privacyNotice.setText("Information Collected: We collect information about your use of the window opener, including the dates and times when it is activated and the settings you use.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the window opener, and to offer personalized features and services.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com.");
                }
                if(j==9){
                    privacyNotice.setText("Information Collected: We collect information about your use of the smart fridge, including the items you store and the dates and times when you open and close the fridge.\n" +
                            "\n" +
                            "Use of Information: The information collected is used to improve the performance and functionality of the smart fridge, and to offer personalized features and services.\n" +
                            "\n" +
                            "Sharing Information: We do not share your information with third parties, except as required by law or with your consent.\n" +
                            "\n" +
                            "Data Security: We take appropriate technical and organizational measures to secure your information against unauthorized access, disclosure, or destruction.\n" +
                            "\n" +
                            "Your Rights: You have the right to access, correct, or delete your information, and to request that we stop processing it. You can also file a complaint with the national authority.\n" +
                            "\n" +
                            "Data Retention: We retain your information for a period of 6 months.\n" +
                            "\n" +
                            "Contact Us: If you have any questions or concerns about our privacy notice or practices, please contact us at fakemail@protonmail.com. ");
                }
                privacyNoticeRepository.save(privacyNotice);
            }


            // MESSAGGI
            Message message= new Message();
            message.setSender(subject);
            message.setReceiver(controller);
            message.setMessage("Dear UserController,\n" +
                    "\n" +
                    "I am writing to request the removal of my personal information in accordance with the General Data Protection Regulation (GDPR). As the owner of the smart light bulbs, I have the right to have my personal data erased.\n" +
                    "\n" +
                    "Please confirm that you have received this request and provide me with a timeline for the removal of my personal information from your systems.\n" +
                    "\n" +
                    "Sincerely,\n" +
                    "UserSubject \n");
            message.setTime(LocalDateTime.now());
            messageRepository.save(message);

            Message message2= new Message();
            message2.setSender(subjects[0]);
            message2.setReceiver(controller);
            message2.setMessage("Dear UserController,\n" +
                    "\n" +
                    "I am writing to request the removal of my personal information in accordance with the General Data Protection Regulation (GDPR). As the owner of the smart light bulbs, I have the right to have my personal data erased.\n" +
                    "\n" +
                    "Please confirm that you have received this request and provide me with a timeline for the removal of my personal information from your systems.\n" +
                    "\n" +
                    "Sincerely,\n" +
                    "Luca Sandini \n");
            message2.setTime(LocalDateTime.now());
            messageRepository.save(message2);












            //AGGIUNTA SUBJECTS, CONTROLLER, DPO, APP. TUTTI HANNO 50 ELEMENTI
            /*User[] subjects, controller, DPO;
            IoTApp[] apps;
            subjects= new User[50];
            controller= new User[50];
            DPO=new User[50];
            apps=new IoTApp[50];
            for(int i=0; i<50;i++){
                subjects[i]= new User();
                subjects[i].setName("subject" + String.valueOf(i));
                subjects[i].setHashedPassword(passwordEncoder.encode("subject" + String.valueOf(i)));
                subjects[i].setRole(Role.SUBJECT);
                userRepository.save(subjects[i]);

                controller[i]= new User();
                controller[i].setName("controller" + String.valueOf(i));
                controller[i].setHashedPassword(passwordEncoder.encode("controller" + String.valueOf(i)));
                controller[i].setRole(Role.CONTROLLER);
                userRepository.save(controller[i]);

                DPO[i]= new User();
                DPO[i].setName("DPO" + String.valueOf(i));
                DPO[i].setHashedPassword(passwordEncoder.encode("DPO" + String.valueOf(i)));
                DPO[i].setRole(Role.DPO);
                userRepository.save(DPO[i]);

                apps[i]= new IoTApp();
                apps[i].setDescription("description" + String.valueOf(i));
                apps[i].setName("appppp" + String.valueOf(i));
                if(i==0){
                    apps[0].setQuestionnaireVote(QuestionnaireVote.GREEN);
                }
                //apps[i].setDataController(controller[i]);
                ioTAppRepository.save(apps[i]);

            }


            //AGGIUNTA USERAPPRELATION

            for(int i=0;i<50;i++){
                //SUBJECT I CON LE APP [I-5, I+5]
                for(int j=i-5;j<=i+5;j++){
                    if(j>=0 && j<50){
                        UserAppRelation userAppRelation=new UserAppRelation();
                        userAppRelation.setConsenses(List.of("consenso1", "consenso2", "consenso3"));
                        userAppRelation.setUser(subjects[i]);
                        userAppRelation.setApp(apps[j]);
                        userAppRelationRepository.save(userAppRelation);
                    }
                }

                //CONTROLLER I CON LE APP [I, I+5]
                for(int j=i;j<=i+5;j++){
                    if(j<50){
                        UserAppRelation userAppRelation=new UserAppRelation();
                        userAppRelation.setConsenses(List.of("consenso1", "consenso2", "consenso3"));
                        userAppRelation.setUser(controller[i]);
                        userAppRelation.setApp(apps[j]);
                        userAppRelationRepository.save(userAppRelation);
                    }
                }

                //DPO I CON LE APP [I-3,I+5]
                for(int j=i-3;j<=i+5;j++){
                    if(j<50 && j>=0){
                        UserAppRelation userAppRelation=new UserAppRelation();
                        userAppRelation.setConsenses(List.of("consenso1", "consenso2", "consenso3"));
                        userAppRelation.setUser(DPO[i]);
                        userAppRelation.setApp(apps[j]);
                        userAppRelationRepository.save(userAppRelation);
                    }
                }
            }

            //AGGIUNTA MESSAGGI
            for(int i=0;i<50;i++){
                //SUBJECT I INVIA 2 MESSAGGI A CONTROLLER I, 4 A I+2, e 6 A I+4
                for(int j=0;j<3;j++){
                    int k=i+j*2;        // Controller k
                    if(k>=50){
                        continue;
                    }
                    for(int z=0;z<j*2+2;z++){
                        Message message=new Message();
                        message.setMessage("questo è il " + String.valueOf(z) +" (non in ordine) messaggio da Subject " + String.valueOf(i) + " verso Controller " +String.valueOf(k));
                        message.setTime(LocalDateTime.of(2022, 6, 26-z, 22, 11, 30));
                        message.setSender(subjects[i]);
                        message.setReceiver(controller[k]);
                        messageRepository.save(message);
                    }
                    // CONTROLLER RISPONDE CON 1 SOLO MESSAGGIO
                    Message message=new Message();
                    message.setMessage(" Sono il Controller " + String.valueOf(k));
                    message.setTime(LocalDateTime.of(2022, 6, 26-j, 12, 10, 55));
                    message.setSender(controller[k]);
                    message.setReceiver(subjects[i]);
                    messageRepository.save(message);
                }
            }

            //AGGINUTA REQUEST. SUBJECT I INVIA 2 RICHIESTE A CONTROLLER I, UNA HANDLED UNA NON HANDLED
            for(int i=0;i<50;i++){
                for(int j=0;j<2;j++){
                    RightRequest request=new RightRequest();
                    if(j==0){
                        request.setHandled(false);
                    }
                    else{
                        request.setHandled(true);
                    }
                    request.setSender(subjects[i]);
                    request.setReceiver(controller[i]);
                    request.setApp(apps[i]);
                    request.setTime(LocalDateTime.of(2022, 6-j, 30-(i%20), 12-j, 10, 30));
                    request.setDetails("varie informazioni che potrebbero essere utili");
                    int k=i*2+j;
                    if(k%3==0){
                        request.setRightType(RightType.WITHDRAWCONSENT);
                        request.setOther("consenso1");
                    }
                    else if(k%2==0){
                        request.setRightType(RightType.ERASURE);
                    }
                    else{
                        request.setRightType(RightType.COMPLAIN);
                    }
                    rightRequestRepository.save(request);
                }

            }

            // AGGIUNTA PRIVACY NOTICE PER APP DISPARI
            for(int i=1; i<50; i=i+2){
                PrivacyNotice privacyNotice= new PrivacyNotice();
                privacyNotice.setApp(apps[i]);
                privacyNotice.setText("La presente Informativa descrive come Paramount e le sue affiliate (collettivamente \"Paramount\") raccolgono, utilizzano e divulgano alcune informazioni, inclusi i dati personali dell’utente, sia online che offline, e le scelte che l’utente può fare su tali informazioni.\n" +
                        "\n" +
                        "Siamo un’azienda di media e intrattenimento leader a livello mondiale che crea contenuti ed esperienze per il pubblico di tutto il mondo. Quando l'utente utilizza i nostri servizi di streaming, le applicazioni mobili e online o altri prodotti e servizi dei nostri marchi, visita i nostri siti web, partecipa ai nostri eventi live, visualizza i nostri contenuti o pubblicità, o contatta il nostro servizio clienti (collettivamente, i \"Servizi\"), noi possiamo raccogliere informazioni da o sull’utente stesso.\n" +
                        "\n" +
                        "La fiducia è un elemento fondamentale della nostra missione in Paramount. Ci impegniamo ad guadagnare e a mantenere la fiducia di ogni utente attraverso una serie di principi fondamentali sulla privacy:\n" +
                        "\n" +
                        "    Trasparenza – Informiamo i nostri utenti su quali informazioni raccogliamo e come le utilizziamo.\n" +
                        "    Scelta – Quando possibile, diamo ai nostri utenti la possibilità di scegliere come verranno raccolte e utilizzate le loro informazioni.\n" +
                        "    Accesso – Quando possibile, e quando richiesto dalla legge, forniamo l'accesso alle informazioni che raccogliamo sui nostri utenti.\n" +
                        "    Sicurezza – Proteggiamo le informazioni dei nostri utenti con adeguate misure di sicurezza.\n" +
                        "    Responsabilità – Ci assumiamo la responsabilità del trattamento sicuro delle informazioni dell’utente e facciamo del nostro meglio per dare riscontro a qualsiasi problema o domanda che dell’utente su come trattiamo le sue informazioni.\n" +
                        "\n" +
                        "La presente Informativa sulla privacy si applica a tutte le informazioni dell’utente che raccogliamo in relazione ai Servizi in tutto il mondo. Potrebbero essere presenti ulteriori avvisi sulle nostre pratiche e scelte in materia di informazioni per determinate offerte Paramount. Utilizzando uno qualsiasi dei Servizi, l'utente riconosce le pratiche e gli scopi della raccolta dei dati descritti nella presente Informativa sulla privacy. Per saperne di più su Paramount e sulle nostre consociate, è possibile visitare la nostra pagina di affiliazione https://www.viacomcbs.com/brands.\n" +
                        "\n" +
                        "Cliccare su ciascuna intestazione di seguito per ulteriori informazioni o scorrere verso il basso per leggere la nostra informativa per intero.\n" +
                        "Quali informazioni raccogliamo\n" +
                        "Come usiamo le informazioni dell’utente e perché\n" +
                        "Con chi condividiamo le informazioni dell’utente e perché\n" +
                        "Scelte, diritti e controlli dell'utente\n" +
                        "Ulteriori informazioni sulla privacy dei minori\n" +
                        "Ulteriori informazioni se ci si trova in California\n" +
                        "Ulteriori informazioni se ci si trova al di fuori degli Stati Uniti\n" +
                        "Trasferimenti internazionali dei dati\n" +
                        "Proteggere le informazioni dell’utente\n" +
                        "Modifiche alla presente Informativa\n" +
                        "Contattaci\n" +
                        "Quali informazioni raccogliamo\n" +
                        "\n" +
                        "Raccogliamo informazioni dall’utente e sull’utente in relazione al suo utilizzo dei Servizi. Alcune di queste informazioni possono essere considerate \"Informazioni personali\" o \"Dati personali\" (come definito dalla legge vigente applicabile) ovvero informazioni che identificano l'utente o il dispositivo dell'utente o sono ragionevolmente associate all'utente.\n" +
                        "\n" +
                        "Raccogliamo, utilizziamo e divulghiamo inoltre informazioni aggregate o anonime che non identificano ragionevolmente l’utente o il suo dispositivo e non sono considerate Informazioni personali.\n" +
                        "Informazioni che ci fornisce l’utente\n" +
                        "\n" +
                        "Raccogliamo informazioni che ci fornisce l’utente, tra cui:\n" +
                        "\n" +
                        "Informazioni di registrazione. Quando ci si registra per alcuni Servizi, si compila un modulo per rispondere ad un’offerta di lavoro, ci si iscrive a concorsi, offerte o competizione, si partecipa a uno dei nostri eventi, si usufruisce di premi o promozioni oppure si risponde a sondaggi e si partecipa a ricerche di mercato che sponsorizziamo, potremmo raccogliere le informazioni fornite dall’utente nei suddetti contesti, come il nome e l’indirizzo e-mail.\n" +
                        "\n" +
                        "Informazioni di fatturazione e pagamento. Quando l'utente acquista da noi un prodotto, un abbonamento o un biglietto per un evento, vengono raccolti alcuni dati di pagamento e di verifica dell'identità, tra cui nome, indirizzo e-mail, indirizzo fisico e informazioni sulla carta di credito.\n" +
                        "\n" +
                        "Informazioni pubblicate sui nostri Servizi. Potremmo raccogliere informazioni quando l’utente pubblica commenti o altri contenuti sui nostri Servizi, che possono includere nome utente o nome visualizzato, commenti, like, interessi, stati, immagini e riferimenti alla sua presenza online.\n" +
                        "\n" +
                        "La corrispondenza e il feedback sui nostri Servizi. Raccogliamo le informazioni che l’utente ci fornisce quando ci contatta direttamente o ci invia feedback, commenti o suggerimenti sui nostri Servizi.\n" +
                        "\n" +
                        "Informazioni che ci fornisce l’utente su altri. A volte, potremmo raccogliere informazioni che l'utente fornisce su altri tra cui nomi, e-mail e compleanni, ad esempio, per fare consigliare a un amico o a un familiare alcuni Servizi.\n" +
                        "\n" +
                        "Informazioni fornite dall'utente quando collabora con noi. Se l’utente è un fornitore, un provider di servizi o un partner commerciale di Paramount, potremmo raccogliere informazioni sull’utente e sui servizi forniti, inclusi i dati commerciali di contatto dell’utente o dei suoi dipendenti e altre informazioni che ci fornisce l’utente stesso o i suoi dipendenti nell'ambito dei servizi che fornisce l’utente e nell’ambito del nostro contratto con l’utente.\n" +
                        "\n" +
                        "Informazioni fornite offline dall’utente. L'utente può inoltre fornirci informazioni di persona e offline. Potremmo aver registrato i dati di un utente in visita presso i nostri uffici (anche tramite i sistemi di sorveglianza dei nostri locali, tra cui il CCTV), o se ha partecipato ad una presentazione dal vivo o alla registrazione di uno dei nostri programmi come parte del pubblico, oppure nel caso in cui ha inviato un modulo di iscrizione ad un nostro concorso o modulo di partecipazione ad uno dei nostri programmi.\n" +
                        "\n" +
                        "Altre informazioni. Raccogliamo inoltre informazioni che si riferiscono o possono essere associate all'utente, quali password, preferenze e interessi personali, età, genere e qualsiasi altra informazione che l’utente ha scelto di fornire.\n" +
                        "Raccolta automatica di informazioni\n" +
                        "\n" +
                        "Utilizzando i nostri Servizi, anche su piattaforme terze, noi e i nostri fornitori di servizi che operano per nostro conto, possiamo raccogliere o ricevere automaticamente alcune informazioni associate all’utente o ai suoi dispositivi di rete, come computer, dispositivi mobili, console di gioco, smart TV o altri dispositivi di streaming. Ciò include le informazioni sull'uso dei nostri Servizi da parte dell'utente e le preferenze dell'utente. Tali informazioni possono essere raccolte automaticamente tramite tecnologie di tracciamento basate su dispositivi quali cookie, pixel, tag, beacon, script o altre tecnologie. Per ulteriori informazioni sui cookie o altre tecnologie di tracciamento e le scelte relative all'uso degli stessi, invitiamo a visitare la nostra Politica sui cookie di Paramount https://www.viacomcbsprivacy.com/cookies.\n" +
                        "\n" +
                        "Le informazioni che raccogliamo automaticamente possono includere anche informazioni di geolocalizzazione, quali (a) informazioni che identificano la posizione precisa del dispositivo mobile e (b) l'indirizzo IP, che possono essere utilizzati per stimare la posizione approssimativa dell'utente.\n" +
                        "\n" +
                        "Informazioni dai nostri partner. Acquisiamo informazioni da altre fonti affidabili. Questi partner commerciali possono includere società, come il provider di servizi televisivi o internet, o altri fornitori di dispositivi multimediali di streaming che consentono la fornitura dei nostri Servizi o contenuti sui loro dispositivi, operatori di telefonia mobile o altre società che forniscono servizi all'utente. Potremmo inoltre raccogliere informazioni sull’utente da altre fonti, inclusi fornitori di servizi, licenziatari di dati e aggregatori, società di marketing, partner pubblicitari, distributori di programmi, piattaforme di social media e database pubblici.\n" +
                        "\n" +
                        "Le informazioni fornite dai nostri partner commerciali variano a seconda della natura dei loro servizi e possono includere indirizzi IP, ID di dispositivi o altri identificativi univoci, informazioni sugli interessi dell’utente, dati demografici, comportamenti di acquisto e attività online.\n" +
                        "Informazioni fornite dall’utente attraverso i social media\n" +
                        "\n" +
                        "Se ci si collega tramite una piattaforma di social media o si naviga su una piattaforma di social media da uno dei nostri siti, la piattaforma del social raccoglierà le informazioni dell’utente separatamente da noi. Occorre verificare le informative sulla privacy delle singole piattaforme social per comprendere come utilizzano le informazioni dell’utente e i diritti dello stesso in relazione a tali informazioni.\n" +
                        "Le informazioni che raccogliamo\n" +
                        "\n" +
                        "Possiamo ricavare ulteriori informazioni o trarre conclusioni sull’utente sulla base delle informazioni che abbiamo raccolto direttamente, passivamente o tramite terzi.\n" +
                        "Come usiamo le informazioni dell’utente e perché\n" +
                        "Possiamo utilizzare le informazioni come divulgate e descritte qui\n" +
                        "\n" +
                        "Potremmo utilizzare i dati per fornire all’utente i nostri Servizi. Possiamo utilizzare le informazioni dell’utente per creare, gestire e autenticare il suo account o i suoi abbonamenti e fornire assistenza e aggiornamenti dell'account. Possiamo inoltre utilizzare le informazioni dell’utente per completare le transazioni richieste dall’utente stesso e adempiere ai nostri obblighi contrattuali con l’utente o per garantire che i nostri Servizi funzionino correttamente.\n" +
                        "\n" +
                        "Potremmo utilizzare le informazioni per adempiere ai nostri contratti con l’utente. Se Paramount stipula un contratto con l’utente, anche nei casi in cui questi sia un fornitore o un fornitore di servizi per Paramount o un nostro partner commerciale, noi possiamo usare le informazioni dell’utente per adempiere ai nostri obblighi contrattuali.\n" +
                        "\n" +
                        "Potremmo utilizzare i dati per finalità di marketing. Possiamo utilizzare le informazioni dell’utente per inviare messaggi promozionali e newsletter a mezzo di e-mail, messaggi di testo o notifiche push. Potrebbero trattarsi di nostre offerte o prodotti oppure di offerte di terzi o prodotti che riteniamo possano essere interessanti per l’utente. Per verificare le proprie scelte in merito a queste comunicazioni, leggere la sezione delle proprie scelte, diritti e controlli relativi alla presente Informativa.\n" +
                        "\n" +
                        "Potremmo usare le informazioni dell’utente per fornire pubblicità o informazioni che riteniamo possano essere di interesse dell’utente. Ad esempio, in base alle informazioni in nostro possesso relativo all’utilizzo dei Servizi da parte dell'utente, incluse le informazioni fornite da terzi, potremmo inviare pubblicità personalizzate all'utente sia nell’ambito dei nostri Servizi sia al di fuori.\n" +
                        "\n" +
                        "Potremmo utilizzare informazioni per migliorare i nostri prodotti e Servizi. Utilizziamo le informazioni dell’utente per monitorare e migliorare il funzionamento, la fornitura e l'accessibilità generale dei nostri Servizi, identificare le aree o le caratteristiche visitate e ottimizzare e personalizzare i Servizi. Ciò può inoltre includere ricerche interne e procedure di sviluppo dei nostri Servizi.\n" +
                        "\n" +
                        "Potremmo utilizzare le informazioni per consentire all’utente di partecipare a piattaforme pubbliche o ad altre funzionalità interattive dei Servizi. Ad esempio, possiamo utilizzare le informazioni dell’utente quando vengono pubblicati commenti su uno dei nostri siti web.\n" +
                        "\n" +
                        "Potremmo utilizzare le informazioni per preservare la sicurezza dei nostri Servizi e per proteggerli. Utilizziamo le informazioni dell’utente per proteggere i diritti e le proprietà di Paramount e di altri e per rispettare i nostri obblighi legali, tra cui il rilevamento, l'indagine e la prevenzione di frodi e altre attività illecite e per far rispettare i nostri accordi.\n" +
                        "\n" +
                        "Potremmo utilizzare le informazioni come altrimenti consentito dalla legge. Possiamo utilizzare le informazioni dell’utente per risolvere controversie, far rispettare i nostri accordi e come diversamente richiesto dalla legge.\n" +
                        "Con chi condividiamo le informazioni dell’utente e perché\n" +
                        "\n" +
                        "Potremmo condividere le informazioni all'interno delle società del gruppo Paramount. Possiamo condividere le informazioni che raccogliamo sull'utente con le società Paramount per le finalità descritte nella sezione ‘Come usiamo le informazioni dell’utente’ e la sua documentazione relativa alla presente Informativa.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni dell’utente in relazione a una transazione aziendale. Possiamo divulgare o trasferire le informazioni dell’utente nell'ambito di, o durante le trattative per qualsiasi acquisto, vendita, locazione, fusione o qualsiasi altro tipo di acquisizione, cessione o finanziamento che coinvolga i nostri marchi.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni con terze parti che eseguono i Servizi per nostro conto. Possiamo condividere le informazioni dell’utente con società non affiliate o persone che assumiamo o con le quali collaboriamo che ci forniscono consulenza professionale, assistenza aziendale o prestazioni di servizi per nostro conto, inclusi assistenza clienti, web hosting, tecnologie dell'informazione, elaborazione dei pagamenti, fornitori di eventi, distribuzione diretta di posta ed e-mail, concorsi, lotterie, forniture e amministrazione di promozioni e servizi analitici. Questi fornitori di servizi sono autorizzati ad utilizzare le informazioni dell’utente per aiutarci ad offrire i nostri Servizi e non per altri scopi.\n" +
                        "\n" +
                        "Potremmo condividere informazioni con i nostri partner commerciali. I partner possono includere partner di marketing, di misurazione dell’audience digitale o altri partner come quelli che lavorano con noi sui Servizi o su gli eventi in co-branding e partner di distribuzione digitale nella quale mette a disposizione i nostri Servizi. I nostri partner utilizzano le informazioni che forniamo come descritto nelle loro politiche sulla privacy.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni degli utenti con sponsor e altri partner per concorsi, omaggi, lotterie e promozioni. Possiamo utilizzare le informazioni fornite dall'utente per verificare l'ingresso e l'idoneità dell'utente alla sua partecipazione a concorsi, lotterie, premio o promozioni e per informarlo in caso di vittoria. Se le informazioni di iscrizione alla partecipazione saranno utilizzate per qualsiasi altro scopo, le comunicheremo nei regolamenti applicabili che regolano tale lotteria, concorso, premio o promozione.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni per offrire pubblicità basate sugli interessi. Per ulteriori informazioni, consultare la sezione \"Scelte, diritti e controlli dell'utente\" della presente Informativa.\n" +
                        "\n" +
                        "Condivideremo le informazioni dell’utente se richiesto dallo stesso. Ad esempio, se l’utente ci chiede di condividere le sue informazioni per poter partecipare a un Servizio interattivo gestito da terzi.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni dell’utente se pensiamo di dover rispettare la legge o per proteggerci. Ad esempio, condivideremo le informazioni quando sarà necessario al fine di rispettare la legge vigente o i procedimenti legali, per rispondere a richiami legali o per proteggere i nostri diritti o proprietà o la sicurezza personale dei nostri utenti, dipendenti o del pubblico.\n" +
                        "\n" +
                        "Potremmo condividere le informazioni dei nostri utenti per motivi non descritti in questa informativa. Gli utenti verranno avvisati prima.\n" +
                        "Scelte, diritti e controlli dell'utente\n" +
                        "L’utente ha diritto a determinate scelte su come utilizziamo le informazioni dell’utente.\n" +
                        "\n" +
                        "Scegliere di non ricevere le nostre e-mail di marketing. Per cessare di ricevere le nostre e-mail promozionali, seguire le istruzioni in qualsiasi e-mail di marketing che si riceve. Ove applicabile, è inoltre possibile modificare le preferenze nel proprio account. Anche se si rifiuta la ricezione di e-mail di marketing, ci sarà comunque permesso di inviare messaggi transazionali all’utente. Ad esempio, potremmo sempre contattare l'utente per gli acquisti effettuati dallo stesso.\n" +
                        "\n" +
                        "Modificare o aggiornare le informazioni che ci ha fornito l’utente. Se si dispone di un account o un abbonamento a uno dei nostri Servizi, è possibile correggere o eliminare informazioni o aggiornare le impostazioni dell'account accedendo al proprio account e seguendo le istruzioni o contattando il team di assistenza clienti.\n" +
                        "\n" +
                        "Controllare i cookie e gli strumenti di tracciamento. TPer sapere come utilizziamo, noi e i nostri fornitori,utilizziamo i cookie e altri strumenti di tracciamento, consultare la nostra Politica sui cookie di Paramount https://www.viacomcbsprivacy.com/cookies.\n" +
                        "\n" +
                        "Opzioni per gli annunci. Noi, le nostre affiliate e qualsiasi parte terza associata possiamo raccogliere informazioni sui nostri Servizi e su siti web di terzi per contribuire a fornire pubblicità rilevanti per gli interessi dell’utente sui suoi dispositivi, nei browser e nei nostri Servizi. Ciò è noto come pubblicità comportamentale basata sugli interessi. Confidiamo che le terze parti che raccolgono informazioni sui Servizi offrano all’utente opzioni di opt-out o altri controlli. Per ulteriori informazioni su come scegliere di non ricevere pubblicità basate sugli interessi sui siti web visitate da pc e dispositivi mobili, visitare:\n" +
                        "\n" +
                        "    Digital Advertising Alliance (US) https://www.aboutads.info/choices/\n" +
                        "    Digital Advertising Alliance (Canada) https://youradchoices.ca/en/tools\n" +
                        "    Digital Advertising Alliance (EU) https://www.youronlinechoices.com/\n" +
                        "    Network Advertising Initiative https://optout.networkadvertising.org/?c=1\n" +
                        "\n" +
                        "L'utente può rifiutare la pubblicità basata sugli interessi anche tramite alcuni provider di servizi che utilizziamo, come Google https://adssettings.google.com/authenticated. Facciamo notare che, se si rifiuta la pubblicità basata sugli interessi, alcune informazioni saranno comunque raccolte per altri scopi, come la ricerca, l'analisi e le procedure interne. Si continuerà inoltre a ricevere pubblicità contestuali, che potrebbero però essere meno rilevanti per gli interessi dell’utente.\n" +
                        "\n" +
                        "Su molti dispositivi mobili, è possibile controllare la pubblicità comportamentale basata sugli interessi dell’utente attraverso le impostazioni del dispositivo. Queste opzioni possono includere il ripristino dell'ID pubblicitario del dispositivo o la selezione nelle impostazioni del dispositivo di “Limita il monitoraggio degli annunci”, (LAT, Limit Ad Tracking) per dispositivi iOS oppure “Disattiva Personalizzazione annunci (Opt out of Ads personalization) per i dispositivi Android.\n" +
                        "\n" +
                        "Le impostazioni e le opzioni degli annunci variano a seconda delle impostazioni del browser e del dispositivo, e questo non è da intendersi come un elenco esaustivo. Si tenga presente che le scelte di opt-out si applicheranno solo al browser o al dispositivo specifico dal quale l'utente ha effettuato la scelta. Consigliamo di esplorare il proprio dispositivo e le impostazioni del browser per comprendere meglio le proprie scelte.\n" +
                        "\n" +
                        "Notifiche push. Quando si utilizza uno dei nostri Servizi che inviano notifiche push o offerte al proprio dispositivo mobile, si può modificare le proprie preferenze in qualsiasi momento attraverso le impostazioni del dispositivo mobile che si usa.\n" +
                        "\n" +
                        "Posizione precisa È possibile revocare il consenso alla raccolta, all'uso e al trasferimento di informazioni precise sulla propria posizione modificando le impostazioni della posizione sul dispositivo.\n" +
                        "\n" +
                        "Misurazione video Nielsen. I Servizi possono essere dotati di un software di misurazione proprietario Nielsen che consentirà all’utente di contribuire a ricerche di mercato, come le classificazioni TV di Nielsen. Per ulteriori informazioni sui prodotti di misurazione digitale Nielsen, consultare l'informativa sulla privacy di Nielsen https://www.nielsen.com/digitalprivacy.\n" +
                        "\n" +
                        "Dispositivi e Servizi connessi dei nostri partner. I dispositivi collegati (come tablet, televisori connessi alla rete, set-top box, dispositivi per lo streaming e console di gioco) possono anche utilizzare un ID pubblicitario o altri metodi per identificare l'utente o per offrire pubblicità comportamentale. In alcuni casi, è possibile disattivare il tracciamento selezionando opzioni come “Limita il monitoraggio degli annunci” o disabilitando opzioni come \"pubblicità basata sugli interessi\" nelle impostazioni del dispositivo collegato. Queste opzioni variano a seconda del dispositivo e noi non siamo in grado di controllare le politiche o le pratiche impiegate da terzi in tali dispositivi. Paramount non è responsabile del tracciamento su dispositivi collegati o sui servizi forniti dai nostri partner commerciali e potrebbe non avere visibilità sulle scelte che l’utente compie in relazione al tracciamento su dispositivi collegati o sui servizi dei nostri partner commerciali.\n" +
                        "\n" +
                        "Diritti aggiuntivi. Per diritti specifici di cui l’utente può disporre a seconda della sua posizione, invitiamo a visitare le sezioni \"Informazioni aggiuntive se ci si trova in California\" o \"Ulteriori informazioni se ci si trova al di fuori degli Stati Uniti\" della presente Informativa.\n" +
                        "Ulteriori informazioni sulla privacy dei minori\n" +
                        "\n" +
                        "Alcuni dei nostri Servizi sono rivolte ai minori e sono disciplinati dalla nostra Informativa sulla privacy dei minori https://www.viacomcbsprivacy.com/childrens. I Servizi disciplinati dalla presente Informativa sulla privacy non sono generalmente destinati all'uso da parte dei minori. Nei casi in cui i canali disponibili attraverso i nostri Servizi generali al pubblico siano rivolti ai minori, raccogliamo solo una quantità limitata di dati personali dai suddetti canali, come previsto dal Children's Online Privacy Protection Act relativo alla raccolta online di informazioni personali sui minori e da altre leggi globali dirette alla protezione dei dati dei minori. Nello specifico, possiamo raccogliere l'indirizzo IP, gli identificativi del dispositivo, alcuni numeri identificativi univoci e informazioni di visualizzazione limitate. Non utilizziamo, né consentiamo ai nostri partner di utilizzare queste informazioni, se non al fine di supportare le nostre operazioni interne, come fornire ai bambini l'accesso a funzionalità e attività sui Servizi, personalizzare contenuti e migliorare i nostri Servizi, oppure fornire pubblicità contestuali o limitare il numero di volte in cui viene visualizzata una particolare pubblicità. Non consentiamo la pubblicità basata sugli interessi su parti dei nostri Servizi rivolte ai bambini o quando sappiamo che l'utente è un bambino o un adolescente di età inferiore a 16.\n" +
                        "Informazioni aggiuntive se ci si trova in California\n" +
                        "\n" +
                        "Legge sulla privacy dei consumatori della California. Se si è residente in California, si dispone di specifici diritti sulla privacy regolati dal California Consumer Privacy Act (CCPA). Questi diritti includono:\n" +
                        "\n" +
                        "Diritto di sapere. L’utente ha il diritto di richiedere una segnalazione che mostri i dati personali raccolti, condivisi e venduti sull’utente stesso negli ultimi 12 mesi.\n" +
                        "\n" +
                        "Diritto di rifiutare la vendita dei propri dati personali. L’utente ha il diritto di rifiutare la vendita dei propri dati personali a terzi.\n" +
                        "\n" +
                        "Diritto di cancellazione. L’utente ha il diritto di richiedere che vengano cancellati i propri dati personali raccolti.\n" +
                        "\n" +
                        "Diritto di non discriminazione. Non faremo discriminazioni nei confronti dell'utente per l'esercizio di uno qualsiasi di questi diritti.\n" +
                        "\n" +
                        "L'utente o il suo agente autorizzato può presentare una richiesta per esercitare i diritti di cui sopra compilando il modulo sulla nostra piattaforma https://www.viacomcbsprivacy.com/managemyrights o chiamandoci al 1 (888) 841-3343.\n" +
                        "\n" +
                        "Potremmo richiedere ulteriori informazioni per verificare l’identità dell’utente o per verificare che il suo agente autorizzato abbia l'autorità di presentare la richiesta per conto dell’utente prima di rispondere a una richiesta di diritti ai sensi del CCPA. Risponderemo alla richiesta di ciascun utente entro 45 giorni se possibile e richiesto dalla legge.\n" +
                        "\n" +
                        "CCPA metrics https://www.viacomcbsprivacy.com/metrics/ccpa/.\n" +
                        "Informazioni personali raccolte sui residenti in California\n" +
                        "\n" +
                        "Di seguito sono indicate le categorie di dati personali che potremmo raccogliere sull’utente, nonché esempi dei tipi di dati personali che possono essere raccolti:\n" +
                        "Identificativi\n" +
                        "\n" +
                        "    Nome e Alias\n" +
                        "    Indirizzo fisico\n" +
                        "    Numero di telefono\n" +
                        "    Indirizzo e-mail\n" +
                        "    Indirizzo IP\n" +
                        "    Identificativi univoci\n" +
                        "\n" +
                        "Caratteristiche giuridicamente protette\n" +
                        "\n" +
                        "    Genere\n" +
                        "    Età\n" +
                        "\n" +
                        "Informazioni commerciali\n" +
                        "\n" +
                        "    Interazioni con il Servizio Clienti\n" +
                        "    Informazioni sulle transazioni effettuate sui nostri Servizi, inclusa la cronologia degli acquisti\n" +
                        "    Preferenze e attributi dei clienti, quali like, interessi, stati e aggiornamenti dei social media, immagini e riferimenti alla loro presenza online\n" +
                        "\n" +
                        "Informazioni finanziarie\n" +
                        "\n" +
                        "    Numeri di carta di credito\n" +
                        "\n" +
                        "Inferenze tratte da categorie di dati personali\n" +
                        "\n" +
                        "    Informazioni relative a preferenze, orientamenti, comportamenti online, dati demografici, scelte di prodotto e di annunci e altre preferenze\n" +
                        "\n" +
                        "Geolocalizzazione\n" +
                        "\n" +
                        "    Informazioni relative alla posizione geografica dell’utente, compreso l'indirizzo IP\n" +
                        "\n" +
                        "Internet / attività elettronica\n" +
                        "\n" +
                        "    Informazioni raccolte tramite strumenti di tracciamento, quali cookie del browser e web beacon. Per saperne di più su questi strumenti e su come controllarli, visitare la Politica sui cookie Paramount https://www.viacomcbsprivacy.com/cookies.\n" +
                        "    Informazioni relative alla posizione geografica dell’utente, compreso l'indirizzo IP\n" +
                        "    Password\n" +
                        "    Attributi del dispositivo\n" +
                        "\n" +
                        "Raccogliamo dati personali dalle seguenti fonti:\n" +
                        "\n" +
                        "    Direttamente dall’utente (es. informazioni fornite quando ci si registra in uno dei nostri Servizi, invio di un pagamento per i nostri Servizi, pubblicazioni sui nostri Servizi, quando si corrisponde con noi, informazioni fornite e raccolte offline, ecc.)\n" +
                        "    Informazioni raccolte automaticamente (es. raccolte automaticamente tramite cookie e altre tecnologie di tracciamento online, ecc.)\n" +
                        "    Partner commerciali (es. società di analisi, ecc.)\n" +
                        "    Terze parti (es. broker dati, società di marketing, partner pubblicitari, piattaforme di social media, aggregatori di dati, database pubblici, ecc.)\n" +
                        "    Deduzioni dal profilo dell’utente o dai suoi comportamenti online\n" +
                        "\n" +
                        "Informazioni personali divulgate a terzi per scopi commerciali\n" +
                        "\n" +
                        "Le categorie di dati personali sopra elencate possono essere comunicate alle seguenti categorie di soggetti terzi:\n" +
                        "\n" +
                        "    Alle società del gruppo Paramount\n" +
                        "    In relazione a una transazione aziendale (es. acquisto, vendita, locazione, fusione, acquisizione, cessione o finanziamento che coinvolga i nostri marchi)\n" +
                        "    I nostri fornitori di servizi (ad esempio provider di servizi cloud, fornitori di servizi tecnici, studi legali, società di contabilità, ecc.)\n" +
                        "    Partner commerciali (ad esempio aziende di misurazione dell’audience digitale , società di marketing, partner di lotterie, altri partner che lavorano con noi su servizi o eventi in co-branding, ecc.)\n" +
                        "    Sponsor e altri partner per concorsi, offerte e promozioni\n" +
                        "    Partner pubblicitari per la fornitura di pubblicità sulla base degli interessi dell’utente\n" +
                        "    Quando è l’utente a chiederci di fornire informazioni a un soggetto terzo\n" +
                        "    Alle forze dell'ordine o ad altri se imposto dalla legge.\n" +
                        "\n" +
                        "Le comunicazioni a terzi sopra elencati possono avere uno dei seguenti scopi commerciali:\n" +
                        "\n" +
                        "    Per fornire i nostri Servizi.\n" +
                        "    A fini di marketing.\n" +
                        "    Per offrire annunci o informazioni che riteniamo possano essere di interesse dell’utente.\n" +
                        "    Per migliorare i nostri prodotti e Servizi.\n" +
                        "    Per consentire ai nostri utenti di partecipare a piattaforme pubbliche o altre funzionalità interattive dei Servizi.\n" +
                        "    Per preservare la sicurezza dei nostri Servizi e per proteggerli.\n" +
                        "    Come altrimenti consentito dalla legge.\n" +
                        "\n" +
                        "Informazioni sulla vendita dei dati personali\n" +
                        "\n" +
                        "A causa dell'ampia definizione di \"vendita\" ai sensi del CCPA, alcune informazioni che condividiamo con terzi possono costituire una \"vendita\" delle informazioni. Ad esempio, alcuni dei nostri marchi condividono identificativi, come i cookie e gli identificativi pubblicitari associati al dispositivo mobile o collegato a internet con i nostri partner pubblicitari, e tali partner utilizzano e condividono tali identificativi per mostrare pubblicità mirate all’utente sulla base dei suoi interessi.\n" +
                        "\n" +
                        "Se un marchio fa questo o altrimenti \"vende\" le informazioni dell’utente, lo stesso avrà l'opportunità di opporsi ed esercitare opzione di opt-out facendo clic su “DNSMPI” (\"Do Not Sell My Personal Information\") sulla home page del relativo sito web, sull'applicazione mobile e online o altro servizio. Ricordiamo che, quando si usano questi strumenti nelle nostre proprietà, si dovrà rinnovare le proprie scelte se si eliminano i cookie o se si utilizza un nuovo browser o dispositivo.\n" +
                        "\n" +
                        "I seguenti tipi di informazioni possono essere \"venduti\":\n" +
                        "\n" +
                        "    Nomi e Alias\n" +
                        "    Indirizzo e-mail\n" +
                        "    Indirizzo IP\n" +
                        "    Identificativo unico\n" +
                        "    Informazioni su transazioni o scelte di contenuto effettuate su o in relazione ai nostri Servizi\n" +
                        "    Preferenze e attributi dei clienti, quali like, interessi, stato e aggiornamenti dei social media, immagini e riferimenti alla propria presenza online\n" +
                        "    Informazioni relative a preferenze, orientamenti, comportamenti online, dati demografici, scelte di prodotto e di annunci e altre preferenze\n" +
                        "    Informazioni relative alla propria posizione geografica, compreso l'indirizzo IP\n" +
                        "    Informazioni raccolte tramite cookie e web beacon\n" +
                        "    Attributi del dispositivo\n" +
                        "\n" +
                        "Paramount non effettua alcuna \"vendita\" di dati personali di utenti di cui effettivamente sa avere meno di sedici anni di età ed essere quindi minori.\n" +
                        "\n" +
                        "Non tracciamento I nostri siti web e app non sono progettati per rispondere alle richieste di “non tracciamento” (\"do not track\") da parte dei browser.\n" +
                        "\n" +
                        "Leggi “Shine the Light” e “Eraser”. I residenti dello Stato della California possono richiedere un elenco di tutte le terze parti alle quali abbiamo comunicato alcune informazioni nel corso dell'anno precedente per finalità di marketing diretto di tali terze parti.\n" +
                        "\n" +
                        "Se l'utente è residente in California, di età inferiore ai 18 anni e registrato sul sito sul quale questa politica è pubblicata, è possibile richiedere la rimozione di contenuti o informazioni postate pubblicamente. Si prega di notare che tale richiesta non garantisce la completa rimozione da Internet del contenuto o delle informazioni pubblicate, e possono esservi circostanze nelle quali la legge non ne richiede o consente la rimozione.\n" +
                        "\n" +
                        "Per tutte le richieste ai sensi delle leggi \"Shine the Light\" o \"Eraser\", utilizzare la nostra piattaforma Privacy Rights Manager https://www.viacomcbsprivacy.com/managemyright. Accetteremo le richieste per l’esercizio di tali diritti solo attraverso questo meccanismo.\n" +
                        "Ulteriori informazioni se ci si trova al di fuori degli Stati Uniti\n" +
                        "\n" +
                        "La presente Informativa sulla privacy è di natura globale e il nostro obiettivo è quello di soddisfare i requisiti in ogni paese nel quale vengono forniti i nostri Servizi. Se necessario, forniamo all’utente diritti e opzioni specifiche. Per qualsiasi domanda sul trattamento dei propri dati personali nel proprio paese, inviare un modulo di \"Richiesta generale della privacy\" sul nostro Modulo di richiesta informazioni sui termini generali della privacy disponibile sul Privacy Rights Manager https://www.viacomcbsprivacy.com/managemyrights. I paesi specifici in cui forniamo ulteriori o differenti protezioni della privacy sono elencati di seguito.\n" +
                        "SEE, Regno Unito o Svizzera\n" +
                        "\n" +
                        "Se ci si trova nello Spazio Economico Europeo (il SEE) nel Regno Unito (UK) o in Svizzera, si potrebbe disporre di determinati diritti ai sensi delle leggi applicabili sulla tutela dei dati personali:\n" +
                        "\n" +
                        "    Il diritto di richiedere l'accesso e una copia delle informazioni in nostro possesso relative all’utente, inclusi i dettagli su come trattiamo tali informazioni.\n" +
                        "    Il diritto di opporsi o di limitare l'elaborazione delle informazioni da parte nostra.\n" +
                        "    Il diritto di richiedere la cancellazione delle proprie informazioni in modo da non essere utilizzate.\n" +
                        "    Il diritto di correggere, modificare o aggiornare le informazioni che ci ha fornito l’utente (in alcuni casi, quando hai un account con noi, è possibile farlo anche accedendo e aggiornando le proprie informazioni).\n" +
                        "    Il diritto di portabilità, che consente di richiedere la condivisione delle informazioni dell’utente con altri.\n" +
                        "    Il diritto di revocare il proprio consenso al trattamento. Facciamo notare che Paramount non intraprende alcun processo decisionale automatizzato sull’utenteTrasferimenti internazionali. Le informazioni raccolte in paesi SEE, nel Regno Unito o in Svizzera possono essere trasferite in paesi al di fuori del SEE, del Regno Unito o della Svizzera che, a seconda dei casi, potrebbero non fornire livelli di protezione per i dati personali equivalenti alla tutela fornita dalle leggi dell'Unione Europea, del Regno Unito o della Svizzera, inclusi gli Stati Uniti. Ai fini di tali trasferimenti di dati transfrontalieri e per proteggere le informazioni dell’utente, abbiamo stipulato clausole contrattuali standard con tutti i suddetti Titolari e Responsabili situati al di fuori di SEE, Regno Unito o Svizzera e, ove necessario, abbiamo adottato ulteriori misure per garantire che le Informazioni personali siano soggette a protezione equivalente.\n" +
                        "\n" +
                        "Conserveremo i dati personali degli utenti per il tempo necessario ai nostri fini commerciali e in conformità con la legge applicabile. Nel determinare ciò, terremo conto della quantità, della natura e della sensibilità dei dati personali, del potenziale rischio di danno per i dati personali e della possibilità di raggiungere i nostri scopi con altri mezzi.\n" +
                        "\n" +
                        "Base giuridica del trattamento. Raccoglieremo, conserveremo o tratteremo altrimenti i dati personali ottenuti dall'utente nel SEE o nel Regno Unito nelle seguenti situazioni:\n" +
                        "\n" +
                        "    Quando disponiamo del consenso a farlo dell’utente. Ad esempio, se si sceglie di ricevere e-mail promozionali. L’utente potrà ritirare il consenso in qualsiasi momento come descritto di seguito o direttamente attraverso i Servizi.\n" +
                        "    Quando dobbiamo usare i dati personali dell’utente per adempiere alle nostre responsabilità ai sensi del nostro contratto stipulato con l’utente (ad esempio, fornitura dei Servizi richiesti).\n" +
                        "    Quando noi o un soggetto terzo abbiamo un interesse legittimo nel trattamento dei dati personali dell’utente. Ad esempio, potremmo trattare i dati personali dell’utente per comunicare con lui/lei circa i nostri Servizi, per fornire, garantire e migliorare i nostri Servizi e, in determinate circostanze, per inviare pubblicità. L’utente ha il diritto di opporsi al nostro trattamento dei dati personali che si basano sui nostri interessi legittimi.\n" +
                        "    Quando per legge siamo tenuti a trattare i dati personali. \n" +
                        "\n" +
                        "Ai fini del trattamento dei dati personali nel SEE, Regno Unito o Svizzera, Viacom International Media Networks UK Limited è il titolare del trattamento. Per domande o per presentare un reclamo, è possibile contattare il Titolare e il nostro Responsabile della protezione dei dati all'indirizzo eu.dpo@viacomcbs.com.\n" +
                        "\n" +
                        "Se si desidera esercitare i propri diritti ai sensi del GDPR (o leggi simili nel Regno Unito o in Svizzera), invitiamo a presentare la richiesta usando la nostra piattaforma Privacy Rights Manager https://www.viacomcbsprivacy.com/managemyrights.\n" +
                        "\n" +
                        "Facciamo notare che, pur valutando attentamente ogni richiesta da noi ricevuta, i diritti dei nostri utenti potrebbero differire a seconda del luogo di residenza e potremmo non essere sempre tenuti a rispettarli. Qualora ciò accadesse, spiegheremo il perché.\n" +
                        "\n" +
                        "In caso di problemi di privacy irrisolti che non abbiamo affrontato in modo soddisfacente dopo averci contattato, ogni utente ha il diritto di contattare l'autorità di vigilanza per la protezione dei dati personali del Regno Unito o della Svizzera o un'autorità UE per la protezione dei dati e presentare un reclamo.\n" +
                        "Ulteriori informazioni se si risiede in paesi al di fuori degli Stati Uniti, del SEE, del Regno Unito o della Svizzera\n" +
                        "\n" +
                        "In alcune giurisdizioni, tra cui Argentina, Brasile e Singapore, richiediamo il consenso o l'autorizzazione dell’utente per utilizzare o o condividere i dati personali degli utenti per gli scopi descritti nella presente Informativa sulla privacy. Fornendoci i propri dati personali, l'utente concede espressamente tale consenso o autorizzazione.\n" +
                        "\n" +
                        "Inoltre, è possibile avere ulteriori diritti sulla privacy in alcune giurisdizioni quali Argentina, Australia, Brasile, Canada, Cina, Hong Kong, India, Giappone, Malesia, Messico, Filippine, Singapore, Sud Africa e Taiwan, inclusi, a titolo esemplificativo e non esaustivo:\n" +
                        "\n" +
                        "    Il diritto di accesso ai propri dati personali.\n" +
                        "    Il diritto di correzione/aggiornamento/rettifica dei propri dati personali.\n" +
                        "    Il diritto di cancellare i propri dati personali.\n" +
                        "    Il diritto di opporre o limitare l'elaborazione o la condivisione dei propri dati personali.\n" +
                        "    Il diritto di aggiunta o dissociazione rispetto ai propri dati.\n" +
                        "\n" +
                        "Società australiane\n" +
                        "\n" +
                        "    CBS Interactive Pty Limited (ABN 41 092 094 525)\n" +
                        "    Network Ten Pty Limited (ABN 91 052 515 250)\n" +
                        "    Network Ten All Access Pty Limited (ABN 60 629 391 117)\n" +
                        "    Network Ten (Adelaide) Pty Limited (ABN 007 577 666\n" +
                        "    Network Ten (Brisbane) Pty Limited (ABN 050 148 537)\n" +
                        "    Network Ten (Melbourne) Pty Limited (ABN 008 664 953)\n" +
                        "    Network Ten (Perth) Pty Limited (ABN 009 108 614)\n" +
                        "    Network Ten (Sydney) Pty Limited (ABN 008 664 962)\n" +
                        "    Capice Pty Ltd (ABN 008 655 847)\n" +
                        "    CBS International Television Australia Pty Limited (ABN 000 005 925)\n" +
                        "    Chartreuse Pty Limited (ABN 008 655 874)\n" +
                        "    Elevenco Pty Limited (ABN 147 043 981)\n" +
                        "    VIMN Australia Pty Ltd. (ABN 18 107 601 418)\n" +
                        "    Nickelodeon Australia (ABN 99 627 643 021)\n" +
                        "    Nickelodeon Australia Management Pty Ltd. (ABN 99 627 643 021)\n" +
                        "    Paramount Pictures Australia Pty. (ABN 85008573171)\n" +
                        "    Paramount Home Entertainment (Australasia) Pty Limited (ABN 29 003 914 609)\n" +
                        "    Rosy Haze Productions Pty Limited (ABN 63 630 698 529)\n" +
                        "    Springy Productions Pty. Limited (ABN 164 833 229)\n" +
                        "    Linbaba’s Story Pty Ltd (ABN 76 632 452 594)\n" +
                        "\n" +
                        "Brasile\n" +
                        "\n" +
                        "Se sei residente in Brasile, quando utilizzi i nostri Servizi i tuoi dati personali sono controllati da Viacom Networks Brasil Programação Televisiva e Publicidade Ltda.\n" +
                        "\n" +
                        "Se sei residente in Brasile, hai alcuni diritti aggiuntivi ai sensi del General Data Protection Act del Brasile:\n" +
                        "\n" +
                        "    Il diritto di confermare l'esistenza del trattamento dei propri dati personali.\n" +
                        "    Il diritto di modificare le preferenze di marketing, incluso il diritto di ritirare il proprio consenso in qualsiasi momento.\n" +
                        "    Il diritto di richiedere informazioni sulla possibilità di negare il consenso e sulle conseguenze di tale rifiuto.\n" +
                        "    Il diritto di richiedere informazioni su entità, pubbliche o private, con le quali Paramount ha condiviso i tuoi dati personali.\n" +
                        "    Il diritto di richiedere l'anonimato, il blocco o la cancellazione quando (a) si vuole stabilire l'accuratezza dei dati personali raccolti, (b) è necessario conservare i dati personali anche se non ne abbiamo più bisogno per rivendicare, esercitare o difendere diritti legali, oppure (c) opporti all'uso dei tuoi dati personali, a meno che non sia stato verificata la presenza di un motivo legittimo per utilizzarli.\n" +
                        "    Il diritto di esaminare le decisioni automatizzate prese esclusivamente sulla base di un trattamento automatizzato dei tuoi dati personali che riguarda i tuoi interessi.\n" +
                        "\n" +
                        "Paramount può trattare i dati personali dell’utente utilizzando tecnologie automatizzate che impiegano algoritmi per creare un profilo sull’utente sulla base delle sue interazioni con i nostri Servizi che selezionano automaticamente pubblicità o contenuti che potrebbero interessare. Tuttavia, queste tecnologie non prendono decisioni automatizzate che potrebbero avere un effetto legale o significativo sull’utente senza il suo consenso, o che non siano consentite dalla legge vigente applicabile.\n" +
                        "Cina\n" +
                        "\n" +
                        "Se sei residente nella Repubblica Popolare Cinese, quando utilizzi i nostri Servizi i tuoi dati personali sono controllati da Paramount Asia (Beijing) Advertising e Media Co. Ltd.\n" +
                        "\n" +
                        "Il trasferimento dei tuoi dati personali al di fuori della Cina avverrà solo con il tuo previo consenso esplicito o dopo aver applicato misure di sicurezza ai tuoi dati, come la ’anonimizzazione dei tuoi dati personali prima del trasferimento.\n" +
                        "India\n" +
                        "\n" +
                        "Se sei residente in India, quando utilizzi i nostri Servizi i tuoi dati personali sono controllati da Nickelodeon India Pvt Ltd o Simon & Schuster Publishers India Private Limited per Simon e Schuster.\n" +
                        "Messico\n" +
                        "\n" +
                        "Se sei residente in Messico, quando utilizzi i nostri Servizi i tuoi dati personali sono controllati da MTV Networks de Messico, S. de R.L. de C.V.\n" +
                        "Giappone\n" +
                        "\n" +
                        "Se sei residente in Giappone, quando utilizzi i nostri Servizi i tuoi dati personali sono controllati da Paramount Networks Japan K.K.\n" +
                        "Come contattarci e esercitare i propri diritti:\n" +
                        "\n" +
                        "Se si desidera esercitare i propri diritti o se si hanno domande o dubbi sull'Informativa sulla privacy, incluse informazioni di contatto del responsabile incaricato della protezione dei dati nella propria giurisdizione, è possibile contattarci tramite il nostro Privacy Right Manager https://www.viacomcbsprivacy.com/managemyrights. Se si preferisce inviare le proprie domande o commenti per posta, inviare una comunicazione scritta al seguente indirizzo:\n" +
                        "\n" +
                        "Paramount\n" +
                        "All’Attenzione del Team Privacy Paramount\n" +
                        "1515 Broadway\n" +
                        "NY 10036\n" +
                        "USA\n" +
                        "\n" +
                        "Se non siamo in grado di risolvere una richiesta, l’utente potrebbe disporre del diritto di presentare un reclamo presso l'autorità di regolamentazione competente nella giurisdizione dell’utente.\n" +
                        "Trasferimenti internazionali dei dati\n" +
                        "\n" +
                        "Trasferiamo le informazioni personali oltre i confini nazionali in conformità con la legge vigente in materia. Operiamo a livello globale e possiamo trasferire le informazioni dell’utente al di fuori del suo paese di residenza, inclusi gli Stati Uniti. Ciò è necessario per i nostri Servizi e per le finalità descritte nella presente Informativa sulla privacy. Quando lo facciamo, adottiamo misure appropriate per garantire un adeguato livello di protezione delle suddette informazioni, nel rispetto della legge vigente applicabile.\n" +
                        "Proteggere le informazioni dell’utente\n" +
                        "\n" +
                        "Impieghiamo ragionevoli misure di sicurezza. Ci impegniamo a proteggere le informazioni dei nostri utenti. Abbiamo adottato procedure di sicurezza tecniche, amministrative e fisiche appropriate sul piano commerciale per proteggere le informazioni dei nostri utenti da perdita, uso improprio, accesso non autorizzato e alterazione. Facciamo notare che nessuna trasmissione o archiviazione dei dati può essere garantita come sicura al 100%. Vogliamo che i nostri utenti si sentano protetti mentre utilizzano i nostri Servizi, ma non possiamo assicurare o garantire la sicurezza delle informazioni che i nostri utenti ci trasmettono.\n" +
                        "Modifiche alla presente Informativa\n" +
                        "\n" +
                        "Di volta in volta, potremmo modificare la presente Informativa sulla privacy per adattarla a nuove tecnologie, procedure di settore, requisiti normativi o per altri scopi. Informeremo i nostri utenti se tali cambiamenti saranno sostanziali e, laddove richiesto dalla legge applicabile, chiederemo prima il consenso degli utenti. La notifica potrà essere inviata per e-mail all'ultimo indirizzo e-mail fornitoci, pubblicando tali modifiche sui nostri siti web, applicazioni e Servizi, o con altri mezzi, in linea con la legge vigente.\n" +
                        "Contattaci\n" +
                        "\n" +
                        "In caso di domande sulla presente Informativa sulla privacy, è possibile contattarci tramite il nostro Modulo di richiesta informazioni sui termini generali della privacy disponibile sul Privacy Rights Manager https://www.viacomcbsprivacy.com/managemyrights. Se si preferisce inviare le proprie domande o commenti per posta, inviare una comunicazione scritta al seguente indirizzo:\n" +
                        "\n" +
                        "Paramount\n" +
                        "All’Attenzione del Team Privacy Paramount\n" +
                        "1515 Broadway\n" +
                        "NY 10036\n" +
                        "USA");
                privacyNoticeRepository.save(privacyNotice);
            }





            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime notNow=LocalDateTime.of(2020, 4, 13, 22, 11, 30);
            logger.info(dtf.format(notNow));
            */
            //FINE AGGIUNTA

            logger.info("Generated demo data");
        };
    }

}