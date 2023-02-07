package com.privacydashboard.application.security;

import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.UserRepository;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getName(), user.getHashedPassword(),
                    getAuthorities(user));
        }
    }

    /*private static List<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

    }*/

    // Per com'è fatto, c'è solo un elemento Authority (User può essere solo uno tra Subject, Controller e DPO)
    // springframework...User però vuole una lista
    private static List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> list=new LinkedList<>();
        list.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return list;

    }

    public boolean registerUser(User user){
        userRepository.save(user);
        return true;
    }

    public void hashPassAndAddUser(User user){
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        registerUser(user);
    }

    public String hashPass(String pass){
        return passwordEncoder.encode(pass);
    }

    public boolean isAlreadyPresent(String name){
        return userRepository.findByName(name)!=null;
    }

    public void changeUserPassword(User user, String password){
        userRepository.changePasswordByUserID(user.getId(), passwordEncoder.encode(password));
    }

    public void changeUserMail(User user, String mail){
        userRepository.changeMailByUserID(user.getId(), mail);
    }

    public boolean isSamePassword(String rawPass, String encodedPass){
        return passwordEncoder.matches(rawPass, encodedPass);
    }

}
