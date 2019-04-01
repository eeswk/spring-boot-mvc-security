package com.zerock.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log
public class LoginController {

    @GetMapping("/login")
    public void login() {

    }

    @GetMapping("/accessDenied")
    public void accessDenied() {

    }

    @GetMapping("/logout")
    public void logout() {
        log.info("logout..........................");
    }
}

