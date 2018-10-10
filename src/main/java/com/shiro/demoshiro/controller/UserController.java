package com.shiro.demoshiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user/")
@Controller
public class UserController {


    @RequestMapping("/index")
    public String index() {


        return "redirect:/index.html";
    }
}
