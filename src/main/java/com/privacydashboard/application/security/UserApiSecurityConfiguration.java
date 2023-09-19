package com.privacydashboard.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(2)
public class UserApiSecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/userapi/**")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
