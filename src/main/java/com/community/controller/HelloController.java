package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "key") String value, Model model){
        model.addAttribute("key",value);
        return "hello";
    }

}
