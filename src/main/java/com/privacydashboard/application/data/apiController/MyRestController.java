package com.privacydashboard.application.data.apiController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userapi")
public class MyRestController {

    @GetMapping("/login")
    public String login() {
        return "{  \"status\":\"logged in\"}";
    }
}
