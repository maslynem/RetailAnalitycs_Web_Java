package ru.s21school.retailanalytics_web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "/error/403";
    }
}
