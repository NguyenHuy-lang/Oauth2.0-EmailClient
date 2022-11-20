package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class preHomeController {
    @GetMapping("/")
    public String prehome() {
        System.out.println("ok");
        return "index";
    }
}
