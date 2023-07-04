package com.privacydashboard.application.data.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.privacydashboard.application.data.GlobalVariables.Role;
public class UserTest {
    
    @Test
    @DisplayName("Name Test")
    public void getSetNameTest(){
        User user = new User();
        user.setName("Rosa");
        assertEquals("Rosa", user.getName(), "Il nome dovrebbe essere: Rosa");
    }

    @Test
    public void getSetHashedPassword(){
        User user = new User();
        user.setHashedPassword("hashHASHhash");
        assertEquals("hashHASHhash", user.getHashedPassword(), "La password recuperata dovrebbe essere: hashHASHhash");
    }

    @Test
    public void getSetRole(){
        User user = new User();
        Role role = Role.SUBJECT;
        user.setRole(role);
        assertEquals(role, user.getRole(), "Il ruole dovrebbe essere SUBJECT");
    }

    @Test
    public void getSetMail(){
        User user = new User();
        user.setMail("user.test@email.test");
        assertEquals("user.test@email.test", user.getMail(), "La mail dovrebbe essere user.test@email.test");
    }

    @Test
    public void getSetProfilePictureUrl(){
        User user = new User();
        user.setProfilePictureUrl("test/test/image.png");
        assertEquals("test/test/image.png", user.getProfilePictureUrl(), "L'url dovrebbe essere: test/test/image.png");
    }
}
