package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.data.service.UserRepository;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.User;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;

public class ApiUserControllerTest{

    private static DataBaseService databaseService;
    private static UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;
    private static UserDetailsServiceImpl userDetailsService;
    private static ApiGeneralController apiGeneralController;
    private static ApiUserController api;

    private User createUser(){
        User user = new User();
        user.setName("User");
        user.setId(new UUID(0, 0));
        user.setHashedPassword("HashedPassword");
        user.setMail("test@mail.test");
        user.setRole(Role.SUBJECT);

        return user;
    }

    @BeforeAll
    private static void setup(){
        apiGeneralController = new ApiGeneralController();
        databaseService = mock(DataBaseService.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository, passwordEncoder);
        api = new ApiUserController();
    }

    @Test
    public void getIdNullTest(){
        assertThrows(NullPointerException.class, () -> {
            api.get(null);
        });
    }
}