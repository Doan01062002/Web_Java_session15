package com.example.session15.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    @GetMapping("/homeb1")
    public String home(Model model){
        model.addAttribute("message","Hello, Thymeleaf!");
        return "homeb1";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
