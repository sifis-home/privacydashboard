package com.privacydashboard.application.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.privacydashboard.application.data.service.UserRepository;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

public class UserDetailsServiceImplTest {

    private static UserRepository userRepository;
    private static PasswordEncoder passwordEncoder;
    private static UserDetailsServiceImpl service;

    @BeforeAll
    private static void setup(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        service = new UserDetailsServiceImpl(userRepository, passwordEncoder);
    }
    
    @Test
    public void loadUserByUsernameNullTest(){
        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(null);
        });
    }

    @Test
    public void loadUserByUsernameMatchTest(){
        User user = new User();
        user.setName("User");
        user.setHashedPassword("HashedPassword");
        user.setRole(Role.SUBJECT);

        when(userRepository.findByName("User")).thenReturn(user);
        UserDetails details = service.loadUserByUsername("User");
        assertEquals(details.getUsername(), "User");
        assertEquals(details.getPassword(), "HashedPassword");
        assertEquals(details.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUBJECT")), true);
    }

    @Test
    public void isAlreadyPresentNullTest(){
        assertEquals(service.isAlreadyPresent(null), false);
    }

    @Test
    public void isAlreadyPresentMatchTest(){
        User user = new User();
        user.setName("User");
        user.setHashedPassword("HashedPassword");
        user.setRole(Role.SUBJECT);

        when(userRepository.findByName("User")).thenReturn(user);
        assertEquals(service.isAlreadyPresent("User"), true);
    }
}
