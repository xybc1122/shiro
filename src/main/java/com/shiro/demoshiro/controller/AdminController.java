package com.shiro.demoshiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/admin")
@Controller
public class AdminController {


    @RequestMapping("/add")
    public String login() {
        System.out.println("来到了用户页面~");
        return "redirect:/admin.html";
    }
}
