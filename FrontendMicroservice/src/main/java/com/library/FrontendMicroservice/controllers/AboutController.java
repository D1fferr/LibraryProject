package com.library.FrontendMicroservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {


    @GetMapping("/about")
    public String viewAbout(){
        return "about";
    }
    @GetMapping("/contact")
    public String viewContact(){
        return "contact";
    }
}
