package com.privacydashboard.application.security;

import com.privacydashboard.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Order(1) // Set the order of this security configuration (lower order value = applied first)
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    public static final String LOGOUT_URL = "/"; // Specify the URL for logout

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define the password encoder for password-based authentication
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
//
//    @Bean
//    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
//        return new MyAuthenticationSuccessHandler();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
//        return (request, response, authException) -> {
//            // Redirect unauthenticated users to the login page
//            response.sendRedirect("/login");
//        };
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // Add custom JWT filter before the UsernamePasswordAuthenticationFilter
                .authorizeRequests().antMatchers("/api/**").authenticated()
                .and().httpBasic();
        super.configure(http);
        setLoginView(http, LoginView.class, LOGOUT_URL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/images/*.png"); // Ignore static resources (images) for better performance
    }
}
